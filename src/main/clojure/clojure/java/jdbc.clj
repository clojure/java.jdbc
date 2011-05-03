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
  (:require clojure.string)
  (:use clojure.java.jdbc.internal))

(def as-identifier 
  "Given a string, return it as-is.
   Given a keyword, return it as a string using the current naming strategy."
  as-identifier*)

(def as-keyword 
  "Given a string, return it as a keyword using the current naming strategy.
   Given a keyword, return it as-is."
  as-keyword*)

(def find-connection find-connection*)
(def connection connection*)

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
  (binding [*as-str* (if (map? naming-strategy) (or (:entity naming-strategy) identity) naming-strategy)] (as-identifier x)))

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
  (binding [*as-key* (if (and (map? naming-strategy) (:keyword naming-strategy)) (:keyword naming-strategy) clojure.string/lower-case)] (as-keyword x)))

(defn as-quoted-identifier
  "Given a quote pattern - either a single character or a pair of characters in
   a vector - and a keyword, return the keyword as a string using a simple
   quoting naming strategy.
   Given a qote pattern and a string, return the string as-is.
     (as-quoted-identifier X :name) will return XnameX as a string.
     (as-quoted-identifier [A B] :name) will return AnameB as a string."
  [q x]
  (binding [*as-str* (partial as-quoted-str q)] (as-identifier x)))

(defmacro with-naming-strategy
  "Evaluates body in the context of a naming strategy."
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
    :classname   (required) a String, the jdbc driver class name
    :subprotocol (required) a String, the jdbc subprotocol
    :subname     (required) a String, the jdbc subname
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
  (with-open [stmt (.createStatement (connection))]
    (doseq [cmd commands]
      (.addBatch stmt cmd))
    (transaction
      (seq (.executeBatch stmt)))))

(defn do-prepared
  "Executes an (optionally parameterized) SQL prepared statement on the
  open database connection. Each param-group is a seq of values for all of
  the parameters."
  [sql & param-groups]
  (apply do-prepared* false sql param-groups))

(defn create-table
  "Creates a table on the open database connection given a table name and
  specs. Each spec is either a column spec: a vector containing a column
  name and optionally a type and other constraints, or a table-level
  constraint: a vector containing words that express the constraint. All
  words used to describe the table may be supplied as strings or keywords."
  [name & specs]
  (do-commands
    (format "CREATE TABLE %s (%s)"
            (as-identifier name)
            (apply str
                   (map as-identifier
                        (apply concat
                               (interpose [", "]
                                          (map (partial interpose " ") specs))))))))

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
        template (apply str (interpose "," (replicate n "?")))
        columns (if (seq column-names)
                  (format "(%s)" (apply str (interpose "," column-strs)))
                  "")]
    (apply do-prepared*
           return-keys
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
      false
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
      false
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
  results. sql-params is a vector containing a string providing
  the (optionally parameterized) SQL query followed by values for any
  parameters."
  [results sql-params & body]
  `(with-query-results* ~sql-params (fn [~results] ~@body)))

(defn print-sql-exception
  "Prints the contents of an SQLException to *out*"
  [exception]
  (println
    (format (str "%s:" \newline
                 " Message: %s" \newline
                 " SQLState: %s" \newline
                 " Error Code: %d")
            (.getSimpleName (class exception))
            (.getMessage exception)
            (.getSQLState exception)
            (.getErrorCode exception))))

(defn print-sql-exception-chain
  "Prints a chain of SQLExceptions to *out*"
  [exception]
  (loop [e exception]
    (when e
      (print-sql-exception e)
      (recur (.getNextException e)))))

(defn print-update-counts
  "Prints the update counts from a BatchUpdateException to *out*"
  [exception]
  (println "Update counts:")
  (dorun 
    (map-indexed 
      (fn [index count] 
        (println (format " Statement %d: %s"
                         index
                         (get special-counts count count)))) 
      (.getUpdateCounts exception))))