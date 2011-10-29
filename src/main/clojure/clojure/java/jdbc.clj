;;  Copyright (c) Stephen C. Gilardi. All rights reserved.  The use and
;;  distribution terms for this software are covered by the Eclipse Public
;;  License 1.0 (http://opensource.org/licenses/eclipse-1.0.php) which can
;;  be found in the file epl-v10.html at the root of this distribution.  By
;;  using this software in any fashion, you are agreeing to be bound by the
;;  terms of this license.  You must not remove this notice, or any other,
;;  from this software.
;;
;;  jdbc.clj
;;
;;  A Clojure interface to sql databases via jdbc
;;
;;  scgilardi (gmail)
;;  Created 2 April 2008
;;
;;  seancorfield (gmail)
;;  Migrated from clojure.contrib.sql 17 April 2011

(ns
  ^{
    :author "Stephen C. Gilardi, Sean Corfield",
    :doc "A Clojure interface to SQL databases via JDBC

clojure.java.jdbc provides a simple abstraction for CRUD (create, read,
update, delete) operations on a SQL database, along with basic transaction
support. Basic DDL operations are also supported (create table, drop table,
access to table metadata).

Maps are used to represent records, making it easy to store and retrieve
data. Results can be processed using any standard sequence operations.

For most operations, Java's PreparedStatement is used so your SQL and
parameters can be represented as simple vectors where the first element
is the SQL string, with ? for each parameter, and the remaining elements
are the parameter values to be substituted. In general, operations return
the number of rows affected, except for a single record insert where any
generated keys are returned (as a map)." }
   clojure.java.jdbc
  (:import [java.sql BatchUpdateException Connection SQLException Statement])
  (:refer-clojure :exclude [resultset-seq])
  (:require clojure.string)
  (:use clojure.java.jdbc.internal))

(def ^{:doc "Given a string, return it as-is.  Given a keyword, return
            it as a string using the current naming strategy."}
  as-identifier
  as-identifier*)

(def ^{:doc "Given a string, return it as a keyword using the current
            naming strategy.  Given a keyword, return it as-is."}
  as-keyword
  as-keyword*)

(def ^{:doc "Returns the current database connection (or nil if there
            is none)"
       :tag Connection}
  find-connection
  find-connection*)

(def ^{:doc "Returns the current database connection (or throws if
            there is none)"
       :tag Connection}
  connection
  connection*)

(def ^{:doc "Creates and returns a lazy sequence of maps
            corresponding to the rows in the java.sql.ResultSet
            rs. Based on clojure.core/resultset-seq but it respects
            the current naming strategy. Duplicate column names are
            made unique by appending _N before applying the naming
            strategy (where N is a unique integer)."}
  resultset-seq
  resultset-seq*)

(defn as-quoted-str
  "Given a quoting pattern - either a single character or a vector pair of
   characters - and a string, return the quoted string:
     (as-quoted-str X foo) will return XfooX
     (as-quoted-str [A B] foo) will return AfooB"
  [q x]
  (if (vector? q)
    (str (first q) x (last q))
    (str q x q)))

(defn as-named-identifier
  "Given a naming strategy and a keyword, return the keyword as a string using the 
   entity naming strategy.
   Given a naming strategy and a string, return the string as-is.
   The naming strategy should either be a function (the entity naming strategy) or 
   a map containing :entity and/or :keyword keys which provide the entity naming
   strategy and/or keyword naming strategy respectively."
  [naming-strategy x]
  (as-identifier x (if (map? naming-strategy) (or (:entity naming-strategy) identity) naming-strategy)))

(defn as-named-keyword
  "Given a naming strategy and a string, return the string as a keyword using the 
   keyword naming strategy.
   Given a naming strategy and a keyword, return the keyword as-is.
   The naming strategy should either be a function (the entity naming strategy) or 
   a map containing :entity and/or :keyword keys which provide the entity naming
   strategy and/or keyword naming strategy respectively.
   Note that providing a single function will cause the default keyword naming
   strategy to be used!"
  [naming-strategy x]
  (as-keyword x (if (and (map? naming-strategy) (:keyword naming-strategy)) (:keyword naming-strategy) clojure.string/lower-case)))

(defn as-quoted-identifier
  "Given a quote pattern - either a single character or a pair of characters in
   a vector - and a keyword, return the keyword as a string using a simple
   quoting naming strategy.
   Given a qote pattern and a string, return the string as-is.
     (as-quoted-identifier X :name) will return XnameX as a string.
     (as-quoted-identifier [A B] :name) will return AnameB as a string."
  [q x]
  (as-identifier x (partial as-quoted-str q)))

(defmacro with-naming-strategy
  "Evaluates body in the context of a naming strategy.
   The naming strategy is either a function - the entity naming strategy - or
   a map containing :entity and/or :keyword keys which provide the entity naming
   strategy and/or the keyword naming strategy respectively. The default entity
   naming strategy is identity; the default keyword naming strategy is lower-case."
  [naming-strategy & body ]
  `(binding [*as-str* (if (map? ~naming-strategy) (or (:entity ~naming-strategy) identity) ~naming-strategy)
             *as-key* (if (map? ~naming-strategy) (or (:keyword ~naming-strategy) clojure.string/lower-case))] ~@body))

(defmacro with-quoted-identifiers
  "Evaluates body in the context of a simple quoting naming strategy."
  [q & body ]
  `(binding [*as-str* (partial as-quoted-str ~q)] ~@body))

(defmacro with-connection
  "Evaluates body in the context of a new connection to a database then
  closes the connection. db-spec is a map containing values for one of the
  following parameter sets:

  Factory:
    :factory     (required) a function of one argument, a map of params
    (others)     (optional) passed to the factory function in a map

  DriverManager:
    :subprotocol (required) a String, the jdbc subprotocol
    :subname     (required) a String, the jdbc subname
    :classname   (optional) a String, the jdbc driver class name
    (others)     (optional) passed to the driver as properties.

  DataSource:
    :datasource  (required) a javax.sql.DataSource
    :username    (optional) a String
    :password    (optional) a String, required if :username is supplied

  JNDI:
    :name        (required) a String or javax.naming.Name
    :environment (optional) a java.util.Map"
  [db-spec & body]
  `(with-connection* ~db-spec (fn [] ~@body)))

(defmacro transaction
  "Evaluates body as a transaction on the open database connection. Any
  nested transactions are absorbed into the outermost transaction. By
  default, all database updates are committed together as a group after
  evaluating the outermost body, or rolled back on any uncaught
  exception. If set-rollback-only is called within scope of the outermost
  transaction, the entire transaction will be rolled back rather than
  committed when complete."
  [& body]
  `(transaction* (fn [] ~@body)))

(defn set-rollback-only
  "Marks the outermost transaction such that it will rollback rather than
  commit when complete"
  []
  (rollback true))

(defn is-rollback-only
  "Returns true if the outermost transaction will rollback rather than
  commit when complete"
  []
  (rollback))

(defn do-commands
  "Executes SQL commands on the open database connection."
  [& commands]
  (with-open [^Statement stmt (let [^Connection con (connection)] (.createStatement con))]
    (doseq [^String cmd commands]
      (.addBatch stmt cmd))
    (transaction
      (seq (.executeBatch stmt)))))

(defn prepare-statement
  "Create a prepared statement from a connection, a SQL string and an
   optional list of parameters:
     :return-keys true | false - default false
     :result-type :forward-only | :scroll-insensitive | :scroll-sensitive
     :concurrency :read-only | :updatable
     :fetch-size n
     :max-rows n"
  [con sql & options]
  (apply prepare-statement* con sql options))

(defn do-prepared
  "Executes an (optionally parameterized) SQL prepared statement on the
  open database connection. Each param-group is a seq of values for all of
  the parameters."
  [sql & param-groups]
  (apply do-prepared* sql param-groups))

(defn create-table-ddl
  "Given a table name and column specs with an optional table-spec
   return the DDL string for creating a table based on that."
  [name & specs]
  (let [split-specs (partition-by #(= :table-spec %) specs)
        col-specs (first split-specs)
        table-spec (first (second (rest split-specs)))
        table-spec-str (or (and table-spec (str " " table-spec)) "")
        specs-to-string (fn [specs]
                          (apply str
                                 (map as-identifier
                                      (apply concat
                                             (interpose [", "]
                                                        (map (partial interpose " ") specs))))))]
    (format "CREATE TABLE %s (%s)%s"
            (as-identifier name)
            (specs-to-string col-specs)
            table-spec-str)))

(defn create-table
  "Creates a table on the open database connection given a table name and
  specs. Each spec is either a column spec: a vector containing a column
  name and optionally a type and other constraints, or a table-level
  constraint: a vector containing words that express the constraint. An
  optional suffix to the CREATE TABLE DDL describing table attributes may
  by provided as :table-spec {table-attributes-string}. All words used to
  describe the table may be supplied as strings or keywords."
  [name & specs]
  (do-commands (apply create-table-ddl name specs)))

(defn drop-table
  "Drops a table on the open database connection given its name, a string
  or keyword"
  [name]
  (do-commands
    (format "DROP TABLE %s" (as-identifier name))))

(defn insert-values
  "Inserts rows into a table with values for specified columns only.
  column-names is a vector of strings or keywords identifying columns. Each
  value-group is a vector containing a values for each column in
  order. When inserting complete rows (all columns), consider using
  insert-rows instead.
  If a single set of values is inserted, returns a map of the generated keys."
  [table column-names & value-groups]
  (let [column-strs (map as-identifier column-names)
        n (count (first value-groups))
        return-keys (= 1 (count value-groups))
        prepared-statement (if return-keys do-prepared-return-keys* do-prepared*)
        template (apply str (interpose "," (replicate n "?")))
        columns (if (seq column-names)
                  (format "(%s)" (apply str (interpose "," column-strs)))
                  "")]
    (apply prepared-statement
           (format "INSERT INTO %s %s VALUES (%s)"
                   (as-identifier table) columns template)
           value-groups)))

(defn insert-rows
  "Inserts complete rows into a table. Each row is a vector of values for
  each of the table's columns in order.
  If a single row is inserted, returns a map of the generated keys."
  [table & rows]
  (apply insert-values table nil rows))

(defn insert-records
  "Inserts records into a table. records are maps from strings or keywords
  (identifying columns) to values. Inserts the records one at a time.
  Returns a sequence of maps containing the generated keys for each record."
  [table & records]
  (let [ins-v (fn [record] (insert-values table (keys record) (vals record)))]
    (doall (map ins-v records))))

(defn insert-record
  "Inserts a single record into a table. A record is a map from strings or
  keywords (identifying columns) to values.
  Returns a map of the generated keys."
  [table record]
  (let [keys (insert-records table record)]
    (first keys)))

(defn delete-rows
  "Deletes rows from a table. where-params is a vector containing a string
  providing the (optionally parameterized) selection criteria followed by
  values for any parameters."
  [table where-params]
  (let [[where & params] where-params]
    (do-prepared*
      (format "DELETE FROM %s WHERE %s"
              (as-identifier table) where)
      params)))

(defn update-values
  "Updates values on selected rows in a table. where-params is a vector
  containing a string providing the (optionally parameterized) selection
  criteria followed by values for any parameters. record is a map from
  strings or keywords (identifying columns) to updated values."
  [table where-params record]
  (let [[where & params] where-params
        column-strs (map as-identifier (keys record))
        columns (apply str (concat (interpose "=?, " column-strs) "=?"))]
    (do-prepared*
      (format "UPDATE %s SET %s WHERE %s"
              (as-identifier table) columns where)
      (concat (vals record) params))))

(defn update-or-insert-values
  "Updates values on selected rows in a table, or inserts a new row when no
  existing row matches the selection criteria. where-params is a vector
  containing a string providing the (optionally parameterized) selection
  criteria followed by values for any parameters. record is a map from
  strings or keywords (identifying columns) to updated values."
  [table where-params record]
  (transaction
   (let [result (update-values table where-params record)]
     (if (zero? (first result))
       (insert-values table (keys record) (vals record))
       result))))

(defmacro with-query-results
  "Executes a query, then evaluates body with results bound to a seq of the
  results. sql-params is a vector containing either:
    [sql & params] - a SQL query, followed by any parameters it needs
    [stmt & params] - a PreparedStatement, followed by any parameters it needs
                      (the PreparedStatement already contains the SQL query)
    [options sql & params] - options and a SQL query for creating a
                      PreparedStatement, follwed by any parameters it needs
  See prepare-statement for supported options."
  [results sql-params & body]
  `(with-query-results* ~sql-params (fn [~results] ~@body)))

(defn print-sql-exception
  "Prints the contents of an SQLException to *out*"
  [^SQLException exception]
  (let [^Class exception-class (class exception)]
    (println
      (format (str "%s:" \newline
                   " Message: %s" \newline
                   " SQLState: %s" \newline
                   " Error Code: %d")
              (.getSimpleName exception-class)
              (.getMessage exception)
              (.getSQLState exception)
              (.getErrorCode exception)))))

(defn print-sql-exception-chain
  "Prints a chain of SQLExceptions to *out*"
  [^SQLException exception]
  (loop [e exception]
    (when e
      (print-sql-exception e)
      (recur (.getNextException e)))))

(defn print-update-counts
  "Prints the update counts from a BatchUpdateException to *out*"
  [^BatchUpdateException exception]
  (println "Update counts:")
  (dorun 
    (map-indexed 
      (fn [index count] 
        (println (format " Statement %d: %s"
                         index
                         (get special-counts count count)))) 
      (.getUpdateCounts exception))))


(defn filter-existing [specs existing]
  "Filters out existing columns for the update-table function"
  (filter (fn [col] (nil? (some #{(as-identifier (first col))} existing))) specs)
  )

(defn table-exists [table-name]
  (not (nil? (with-query-results rs [(str "SHOW TABLES LIKE \"" table-name "\"")] (first rs))))
  )

(defn update-table 
  "Adds non-existing columns to a table on the open database connection given a table name and
  specs. This can be used instead of create-table Each spec is either a column spec: a vector containing a column
  name and optionally a type and other constraints, or a table-level
  constraint: a vector containing words that express the constraint.  All words used to
  describe the table may be supplied as strings or keywords."  
  [table-name & specs]
  (def exists (table-exists table-name))
  ; Create the table with placeholder column (to remove after other columns are added.
  (if (not exists)
    (do-commands (str "CREATE TABLE " table-name " (__placeholder__ int)")))
  
  (with-query-results rs [(str "SHOW COLUMNS FROM " table-name)]
     (let [existing-columns (map #(:field %) rs)
           split-specs (partition-by #(= :table-spec %) specs)
	         col-specs (first split-specs)
		       table-spec (first (second (rest split-specs)))
		       table-spec-str (or (and table-spec (str " " table-spec)) "")
           remaining-cols (filter-existing specs existing-columns)
		       specs-to-string (fn [specs]
		                          (apply str
		                                 (map as-identifier
		                                      (apply concat
		                                             (interpose [", ADD COLUMN "]
		                                                        (map (partial interpose " ") 
                                                               remaining-cols))))))
           query (format "ALTER TABLE %s ADD COLUMN %s %s"
	            (as-identifier table-name)
	            (specs-to-string col-specs)
	            table-spec-str)]
       (if (> (count remaining-cols) 0)
         (do-commands query)
         )))
     
  ; Remove placeholder column
  (if (not exists)
    (do-commands (str "ALTER TABLE " table-name " DROP COLUMN __placeholder__")))
  )

