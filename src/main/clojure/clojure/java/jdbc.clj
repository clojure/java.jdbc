;;  Copyright (c) Sean Corfield, Stephen C. Gilardi. All rights reserved.
;;  The use and distribution terms for this software are covered by
;;  the Eclipse Public License 1.0
;;  (http://opensource.org/licenses/eclipse-1.0.php) which can be
;;  found in the file epl-v10.html at the root of this distribution.
;;  By using this software in any fashion, you are agreeing to be
;;  bound by the terms of this license.  You must not remove this
;;  notice, or any other, from this software.
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
  ^{:author "Stephen C. Gilardi, Sean Corfield",
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
generated keys are returned (as a map).

For more documentation, see:

http://clojure-doc.org/articles/ecosystem/java_jdbc/home.html

As of release 0.3.0, the API has undergone a major overhaul and most of the
original API has been deprecated in favor of a more idiomatic API. The
original API has been moved to java.jdbc.deprecated for backward
compatibility but it will be removed before a 1.0.0 release." }
  clojure.java.jdbc
  (:import [java.net URI]
           [java.sql BatchUpdateException DriverManager
            PreparedStatement ResultSet SQLException Statement Types]
           [java.util Hashtable Map Properties]
           [javax.sql DataSource])
  (:require [clojure.string :as str]))

(defn as-sql-name
  "Given a naming strategy and a keyword or string, return the keyword
   as a string per that naming strategy.
   A keyword of the form :x.y is treated as keywords :x and :y,
   both are turned into strings via the naming strategy and then
   joined back together so :x.y might become `x`.`y` if the naming
   strategy quotes identifiers with `."
  ([f]
     (fn [x]
       (as-sql-name f x)))
  ([f x]
     (let [n (name x)
           i (.indexOf n (int \.))]
       (if (= -1 i)
         (f n)
         (str/join "." (map f (.split n "\\.")))))))

(defn quoted
  "Given a quoting pattern - either a single character or a vector pair of
   characters - and a string, return the quoted string:
     (quoted \\X \"foo\") will return \"XfooX\"
     (quoted [\\A \\B] \"foo\") will return \"AfooB\""
  ([q]
     (fn [x]
       (quoted q x)))
  ([q x]
     (if (vector? q)
       (str (first q) x (last q))
       (str q x q))))

(defn- ^Properties as-properties
  "Convert any seq of pairs to a java.utils.Properties instance.
   Uses as-sql-name to convert both keys and values into strings."
  [m]
  (let [p (Properties.)]
    (doseq [[k v] m]
      (.setProperty p (as-sql-name identity k)
                    (if (instance? clojure.lang.Named v)
                      (as-sql-name identity v)
                      (str v))))
    p))

(defprotocol Connectable
  (add-connection [db connection])
  (get-level [db]))

(defn- inc-level
  "Increment the nesting level for a transacted database connection.
   If we are at the top level, also add in a rollback state."
  [db]
  (let [nested-db (update-in db [:level] (fnil inc 0))]
    (if (= 1 (:level nested-db))
      (assoc nested-db :rollback (atom false))
      nested-db)))

(extend-protocol Connectable
  String
  (add-connection [s connection] {:connection connection :level 0 :connection-string s})
  (get-level [_] 0)

  clojure.lang.Associative
  (add-connection [m connection] (assoc m :connection connection))
  (get-level [m] (or (:level m) 0))

  nil
  (add-connection [_ connection] {:connection connection :level 0 :legacy true})
  (get-level [_] 0))

(def ^{:private true :doc "Map of classnames to subprotocols"} classnames
  {"postgresql"     "org.postgresql.Driver"
   "mysql"          "com.mysql.jdbc.Driver"
   "sqlserver"      "com.microsoft.sqlserver.jdbc.SQLServerDriver"
   "jtds:sqlserver" "net.sourceforge.jtds.jdbc.Driver"
   "derby"          "org.apache.derby.jdbc.EmbeddedDriver"
   "hsqldb"         "org.hsqldb.jdbcDriver"
   "h2"             "org.h2.Driver"
   "sqlite"         "org.sqlite.JDBC"})

(def ^{:private true :doc "Map of schemes to subprotocols"} subprotocols
  {"postgres" "postgresql"})

(defn- parse-properties-uri [^URI uri]
  (let [host (.getHost uri)
        port (if (pos? (.getPort uri)) (.getPort uri))
        path (.getPath uri)
        scheme (.getScheme uri)]
    (merge
     {:subname (if port
                 (str "//" host ":" port path)
                 (str "//" host path))
      :subprotocol (subprotocols scheme scheme)}
     (if-let [user-info (.getUserInfo uri)]
             {:user (first (str/split user-info #":"))
              :password (second (str/split user-info #":"))}))))

(defn- strip-jdbc [^String spec]
  (if (.startsWith spec "jdbc:")
    (.substring spec 5)
    spec))

;; feature testing macro, based on suggestion from Chas Emerick:
(defmacro when-available
  [sym & body]
  (try
    (when (resolve sym)
      (list* 'do body))
    (catch ClassNotFoundException _#)))

(defn get-connection
  "Creates a connection to a database. db-spec is a map containing connection
  parameters. db-spec is a map containing values for one of the following
  parameter sets:

  Existing Connection:
    :connection  (required) an existing open connection that can be used
                 but cannot be closed (only the parent connection can be closed)

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
    :user        (optional) a String - an alternate alias for :username
                            (added after 0.3.0-beta2 for consistency JDBC-74)
    :password    (optional) a String, required if :username is supplied

  JNDI:
    :name        (required) a String or javax.naming.Name
    :environment (optional) a java.util.Map

  Raw:
    :connection-uri (required) a String
                 Passed directly to DriverManager/getConnection

  URI:
    Parsed JDBC connection string - see below
  
  String:
    subprotocol://user:password@host:post/subname
                 An optional prefix of jdbc: is allowed."
  ^java.sql.Connection
  [{:keys [connection
           factory
           connection-uri
           classname subprotocol subname
           datasource username password user
           name environment]
    :as db-spec}]
  (cond
   connection
   connection
   
   (instance? URI db-spec)
   (get-connection (parse-properties-uri db-spec))
   
   (string? db-spec)
   (get-connection (URI. (strip-jdbc db-spec)))
   
   factory
   (factory (dissoc db-spec :factory))
   
   connection-uri
   (DriverManager/getConnection connection-uri)
   
   (and subprotocol subname)
   (let [url (format "jdbc:%s:%s" subprotocol subname)
         etc (dissoc db-spec :classname :subprotocol :subname)
         classname (or classname (classnames subprotocol))]
     (clojure.lang.RT/loadClassForName classname)
     (DriverManager/getConnection url (as-properties etc)))
   
   (or (and datasource username password)
       (and datasource user     password))
   (.getConnection ^DataSource datasource ^String (or username user) ^String password)
   
   datasource
   (.getConnection ^DataSource datasource)
   
   name
   (when-available
    javax.naming.InitialContext
    (let [env (and environment (Hashtable. ^Map environment))
          context (javax.naming.InitialContext. env)
          ^DataSource datasource (.lookup context ^String name)]
      (.getConnection datasource)))
   
   :else
   (let [^String msg (format "db-spec %s is missing a required parameter" db-spec)]
     (throw (IllegalArgumentException. msg)))))

(defn- make-name-unique
  "Given a collection of column names and a new column name,
   return the new column name made unique, if necessary, by
   appending _N where N is some unique integer suffix."
  [cols col-name n]
  (let [suffixed-name (if (= n 1) col-name (str col-name "_" n))]
    (if (apply distinct? suffixed-name cols)
      suffixed-name
      (recur cols col-name (inc n)))))

(defn- make-cols-unique
  "Given a collection of column names, rename duplicates so
   that the result is a collection of unique column names."
  [cols]
  (if (or (empty? cols) (apply distinct? cols))
    cols
    (reduce (fn [unique-cols col-name]
              (conj unique-cols (make-name-unique unique-cols col-name 1))) []  cols)))

(defprotocol ISQLValue
  "Protocol for creating SQL values from Clojure values. Default
   implementations (for Object and nil) just return the argument,
   but it can be extended to provide custom behavior to support
   exotic types supported by different databases."
  (sql-value [val] "Convert a Clojure value into a SQL value."))

(extend-protocol ISQLValue
  Object
  (sql-value [v] v)

  nil
  (sql-value [_] nil))

(defprotocol IResultSetReadColumn
  "Protocol for reading objects from the java.sql.ResultSet. Default
   implementations (for Object and nil) return the argument, and the
   Boolean implementation ensures a canonicalized true/false value,
   but it can be extended to provide custom behavior for special types."
  (result-set-read-column [val rsmeta idx] "Function for transforming values after reading them
                              from the database"))

(extend-protocol IResultSetReadColumn
  Object
  (result-set-read-column [x _2 _3] x)

  Boolean
  (result-set-read-column [x _2 _3] (if (= true x) true false))
  
  nil
  (result-set-read-column [_1 _2 _3] nil))

(defn result-set-seq
  "Creates and returns a lazy sequence of maps corresponding to the rows in the
   java.sql.ResultSet rs. Loosely based on clojure.core/resultset-seq but it
   respects the specified naming strategy. Duplicate column names are made unique
   by appending _N before applying the naming strategy (where N is a unique integer)."
  [^ResultSet rs & {:keys [identifiers as-arrays?]
                    :or {identifiers str/lower-case}}]
  (let [rsmeta (.getMetaData rs)
        idxs (range 1 (inc (.getColumnCount rsmeta)))
        keys (->> idxs
                  (map (fn [^Integer i] (.getColumnLabel rsmeta i)))
                  make-cols-unique
                  (map (comp keyword identifiers)))
        row-values (fn [] (map (fn [^Integer i] (result-set-read-column (.getObject rs i) rsmeta i)) idxs))
        ;; This used to use create-struct (on keys) and then struct to populate each row.
        ;; That had the side effect of preserving the order of columns in each row. As
        ;; part of JDBC-15, this was changed because structmaps are deprecated. We don't
        ;; want to switch to records so we're using regular maps instead. We no longer
        ;; guarantee column order in rows but using into {} should preserve order for up
        ;; to 16 columns (because it will use a PersistentArrayMap). If someone is relying
        ;; on the order-preserving behavior of structmaps, we can reconsider...
        records (fn thisfn []
                  (when (.next rs)
                    (cons (zipmap keys (row-values)) (lazy-seq (thisfn)))))
        rows (fn thisfn []
               (when (.next rs)
                 (cons (vec (row-values)) (lazy-seq (thisfn)))))]
    (if as-arrays?
      (cons (vec keys) (rows))
      (records))))

(defn- execute-batch
  "Executes a batch of SQL commands and returns a sequence of update counts.
   (-2) indicates a single operation operating on an unknown number of rows.
   Specifically, Oracle returns that and we must call getUpdateCount() to get
   the actual number of rows affected. In general, operations return an array
   of update counts, so this may not be a general solution for Oracle..."
  [^Statement stmt]
  (let [result (.executeBatch stmt)]
    (if (and (= 1 (count result)) (= -2 (first result)))
      (list (.getUpdateCount stmt))
      (seq result))))

(def ^{:private true
       :doc "Map friendly :concurrency values to ResultSet constants."} 
  result-set-concurrency
  {:read-only ResultSet/CONCUR_READ_ONLY
   :updatable ResultSet/CONCUR_UPDATABLE})

(def ^{:private true
       :doc "Map friendly :cursors values to ResultSet constants."} 
  result-set-holdability
  {:hold ResultSet/HOLD_CURSORS_OVER_COMMIT
   :close ResultSet/CLOSE_CURSORS_AT_COMMIT})

(def ^{:private true
       :doc "Map friendly :type values to ResultSet constants."} 
  result-set-type
  {:forward-only ResultSet/TYPE_FORWARD_ONLY
   :scroll-insensitive ResultSet/TYPE_SCROLL_INSENSITIVE
   :scroll-sensitive ResultSet/TYPE_SCROLL_SENSITIVE})

(defn prepare-statement
  "Create a prepared statement from a connection, a SQL string and an
   optional list of parameters:
     :return-keys true | false - default false
     :result-type :forward-only | :scroll-insensitive | :scroll-sensitive
     :concurrency :read-only | :updatable
     :cursors
     :fetch-size n
     :max-rows n"
  [^java.sql.Connection con ^String sql &
   {:keys [return-keys result-type concurrency cursors fetch-size max-rows]}]
  (let [^PreparedStatement
        stmt (cond return-keys
                   (try
                     (.prepareStatement con sql java.sql.Statement/RETURN_GENERATED_KEYS)
                     (catch Exception _
                       ;; assume it is unsupported and try basic PreparedStatement:
                       (.prepareStatement con sql)))
                   
                   (and result-type concurrency)
                   (if cursors
                     (.prepareStatement con sql 
                                        (result-type result-set-type)
                                        (concurrency result-set-concurrency)
                                        (cursors result-set-holdability))
                     (.prepareStatement con sql 
                                        (result-type result-set-type)
                                        (concurrency result-set-concurrency)))
                   
                   :else
                   (.prepareStatement con sql))]
    (when fetch-size (.setFetchSize stmt fetch-size))
    (when max-rows (.setMaxRows stmt max-rows))
    stmt))

(defn- set-parameters
  "Add the parameters to the given statement."
  [^PreparedStatement stmt params]
  (dorun (map-indexed (fn [ix value]
                        (.setObject stmt (inc ix) (sql-value value)))
                      params)))

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

(def ^{:private true} special-counts
  {Statement/EXECUTE_FAILED "EXECUTE_FAILED"
   Statement/SUCCESS_NO_INFO "SUCCESS_NO_INFO"})

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

;; java.jdbc pieces rewritten to not use dynamic bindings

(defn db-find-connection
  "Returns the current database connection (or nil if there is none)"
  ^java.sql.Connection [db]
  (and (map? db)
       (:connection db)))

(defn db-connection
  "Returns the current database connection (or throws if there is none)"
  ^java.sql.Connection [db]
  (or (db-find-connection db)
      (throw (Exception. "no current database connection"))))

(defn- throw-non-rte
  "This ugliness makes it easier to catch SQLException objects
  rather than something wrapped in a RuntimeException which
  can really obscure your code when working with JDBC from
  Clojure... :("
  [^Throwable ex]
  (cond (instance? java.sql.SQLException ex) (throw ex)
        (and (instance? RuntimeException ex) (.getCause ex)) (throw-non-rte (.getCause ex))
        :else (throw ex)))

(defn db-set-rollback-only!
  "Marks the outermost transaction such that it will rollback rather than
  commit when complete"
  [db]
  (reset! (:rollback db) true))

(defn db-unset-rollback-only!
  "Marks the outermost transaction such that it will not rollback when complete"
  [db]
  (reset! (:rollback db) false))

(defn db-is-rollback-only
  "Returns true if the outermost transaction will rollback rather than
  commit when complete"
  [db]
  (deref (:rollback db)))

(def ^{:private true :doc "Transaction isolation levels."}
  isolation-levels
  {:none             java.sql.Connection/TRANSACTION_NONE
   :read-committed   java.sql.Connection/TRANSACTION_READ_COMMITTED
   :read-uncommitted java.sql.Connection/TRANSACTION_READ_UNCOMMITTED
   :repeatable-read  java.sql.Connection/TRANSACTION_REPEATABLE_READ
   :serializable     java.sql.Connection/TRANSACTION_SERIALIZABLE})

(defn db-transaction*
  "Evaluates func as a transaction on the open database connection. Any
  nested transactions are absorbed into the outermost transaction. By
  default, all database updates are committed together as a group after
  evaluating the outermost body, or rolled back on any uncaught
  exception. If rollback is set within scope of the outermost transaction,
  the entire transaction will be rolled back rather than committed when
  complete."
  [db func & {:keys [isolation]}]
  (if (zero? (get-level db))
    (if-let [^java.sql.Connection con (db-find-connection db)]
      (let [nested-db (inc-level db)
            auto-commit (.getAutoCommit con)
            old-isolation (.getTransactionIsolation con)]
        (io!
         (when isolation
           (.setTransactionIsolation con (isolation isolation-levels)))
         (.setAutoCommit con false)
         (try
           (let [result (func nested-db)]
             (if (db-is-rollback-only nested-db)
               (.rollback con)
               (.commit con))
             result)
           (catch Throwable t
             (.rollback con)
             (throw-non-rte t))
           (finally
             (db-unset-rollback-only! nested-db)
             (.setAutoCommit con auto-commit)
             (when isolation
               (.setTransactionIsolation con old-isolation))))))
      (with-open [^java.sql.Connection con (get-connection db)]
        (db-transaction* (add-connection db con) func :isolation isolation)))
    (try
      (func (inc-level db))
      (catch Exception e
        (throw-non-rte e)))))

(defmacro with-db-transaction
  "Evaluates body in the context of a transaction on the specified database connection.
  The binding provides the database connection for the transaction and the name to which
  that is bound for evaluation of the body.
  See db-transaction* for more details."
  [binding & body]
  `(db-transaction* ~(second binding)
                    (^{:once true} fn* [~(first binding)] ~@body)
                    ~@(rest (rest binding))))

(defmacro with-db-connection
  "Evaluates body in the context of an active connection to the database.
  (with-db-connection [con-db db-spec] ... con-db ...)"
  [binding & body]
  `(let [db-spec# ~(second binding)]
     (with-open [^java.sql.Connection con# (get-connection db-spec#)]
       (let [~(first binding) (add-connection db-spec# con#)]
         ~@body))))

(defmacro with-db-metadata
  "Evaluates body in the context of an active connection with metadata bound
   to the specified name. See also metadata-result below.
   (with-db-metadata [md db-spec] ... md ..."
  [binding & body]
  `(with-open [^java.sql.Connection con# (get-connection ~(second binding))]
     (let [~(first binding) (.getMetaData con#)]
       ~@body)))

(defn metadata-result
  "If the argument is a java.sql.ResultSet, turn it into a result-set-seq,
   else return it as-is. This makes working with metadata easier."
  [rs-or-value & {:keys [identifiers as-arrays?]
                  :or {identifiers str/lower-case}}]
  (if (instance? java.sql.ResultSet rs-or-value)
    (result-set-seq rs-or-value :identifiers identifiers :as-arrays? as-arrays?)
    rs-or-value))

(defn db-do-commands
  "Executes SQL commands on the specified database connection. Wraps the commands
  in a transaction if transaction? is true. transaction? can be ommitted and it
  defaults to true."
  [db transaction? & commands]
  (if (string? transaction?)
    (apply db-do-commands db true transaction? commands)
    (if-let [^java.sql.Connection con (db-find-connection db)]
      (with-open [^Statement stmt (.createStatement con)]
        (doseq [^String cmd commands]
          (.addBatch stmt cmd))
        (if transaction?
          (with-db-transaction [t-db (add-connection db (.getConnection stmt))]
            (execute-batch stmt))
          (try
            (execute-batch stmt)
            (catch Exception e
              (throw-non-rte e)))))
      (with-open [^java.sql.Connection con (get-connection db)]
        (apply db-do-commands (add-connection db con) transaction? commands)))))

(defn db-do-prepared-return-keys
  "Executes an (optionally parameterized) SQL prepared statement on the
  open database connection. The param-group is a seq of values for all of
  the parameters. transaction? can be ommitted and will default to true.
  Return the generated keys for the (single) update/insert."
  ([db sql param-group]
     (db-do-prepared-return-keys db true sql param-group))
  ([db transaction? sql param-group]
     (if-let [^java.sql.Connection con (db-find-connection db)]
       (with-open [^PreparedStatement stmt (prepare-statement con sql :return-keys true)]
         ((or (:set-parameters db) set-parameters) stmt param-group)
         (let [exec-and-return-keys
               (^{:once true} fn* []
                (let [counts (.executeUpdate stmt)]
                  (try
                    (let [rs (.getGeneratedKeys stmt)
                          result (first (result-set-seq rs))]
                      ;; sqlite (and maybe others?) requires
                      ;; record set to be closed
                      (.close rs)
                      result)
                    (catch Exception _
                      ;; assume generated keys is unsupported and return counts instead: 
                      counts))))]
           (if transaction?
             (with-db-transaction [t-db (add-connection db (.getConnection stmt))]
               (exec-and-return-keys))
             (try
               (exec-and-return-keys)
               (catch Exception e
                 (throw-non-rte e))))))
       (with-open [^java.sql.Connection con (get-connection db)]
         (db-do-prepared-return-keys (add-connection db con) transaction? sql param-group)))))

(defn db-do-prepared
  "Executes an (optionally parameterized) SQL prepared statement on the
  open database connection. Each param-group is a seq of values for all of
  the parameters. transaction? can be omitted and defaults to true.
  Return a seq of update counts (one count for each param-group)."
  [db transaction? & [sql & param-groups :as opts]]
  (if (string? transaction?)
    (apply db-do-prepared db true transaction? opts)
    (if-let [^java.sql.Connection con (db-find-connection db)]
      (with-open [^PreparedStatement stmt (prepare-statement con sql)]
        (if (empty? param-groups)
          (if transaction?
            (with-db-transaction [t-db (add-connection db (.getConnection stmt))]
              (vector (.executeUpdate stmt)))
            (try
              (vector (.executeUpdate stmt))
              (catch Exception e
                (throw-non-rte e))))
          (do
            (doseq [param-group param-groups]
              ((or (:set-parameters db) set-parameters) stmt param-group)
              (.addBatch stmt))
            (if transaction?
              (with-db-transaction [t-db (add-connection db (.getConnection stmt))]
                (execute-batch stmt))
              (try
                (execute-batch stmt)
                (catch Exception e
                  (throw-non-rte e)))))))
      (with-open [^java.sql.Connection con (get-connection db)]
        (apply db-do-prepared (add-connection db con) transaction? sql param-groups)))))

(defn db-query-with-resultset
  "Executes a query, then evaluates func passing in the raw ResultSet as an
   argument. The first argument is a vector containing either:
    [sql & params] - a SQL query, followed by any parameters it needs
    [stmt & params] - a PreparedStatement, followed by any parameters it needs
                      (the PreparedStatement already contains the SQL query)
    [options sql & params] - options and a SQL query for creating a
                      PreparedStatement, followed by any parameters it needs
  See prepare-statement for supported options."
  [db sql-params func]
  (when-not (vector? sql-params)
    (let [^Class sql-params-class (class sql-params)
          ^String msg (format "\"%s\" expected %s %s, found %s %s"
                              "sql-params"
                              "vector"
                              "[sql param*]"
                              (.getName sql-params-class)
                              (pr-str sql-params))] 
      (throw (IllegalArgumentException. msg))))
  (let [special (first sql-params)
        sql-is-first (string? special)
        options-are-first (map? special)
        sql (cond sql-is-first special 
                  options-are-first (second sql-params))
        params (vec (cond sql-is-first (rest sql-params)
                          options-are-first (rest (rest sql-params))
                          :else (rest sql-params)))
        prepare-args (when (map? special) (flatten (seq special)))
        run-query-with-params (^{:once true} fn* [^PreparedStatement stmt]
                               ((or (:set-parameters db) set-parameters) stmt params)
                               (with-open [rset (.executeQuery stmt)]
                                 (func rset)))]
    (if (instance? PreparedStatement special)
      (let [^PreparedStatement stmt special]
        (run-query-with-params stmt))
      (if-let [^java.sql.Connection con (db-find-connection db)]
        (with-open [^PreparedStatement stmt (apply prepare-statement con sql prepare-args)]
          (run-query-with-params stmt))
        (with-open [^java.sql.Connection con (get-connection db)]
          (with-open [^PreparedStatement stmt (apply prepare-statement con sql prepare-args)]
            (run-query-with-params stmt)))))))

;; top-level API for actual SQL operations

(defn query
  "Given a database connection and a vector containing SQL and optional parameters,
  perform a simple database query. The optional keyword arguments specify how to
  construct the result set:
    :result-set-fn - applied to the entire result set, default doall / vec
        if :as-arrays? true, :result-set-fn will default to vec
        if :as-arrays? false, :result-set-fn will default to doall
    :row-fn - applied to each row as the result set is constructed, default identity
    :identifiers - applied to each column name in the result set, default lower-case
    :as-arrays? - return the results as a set of arrays, default false."
  [db sql-params & {:keys [result-set-fn row-fn identifiers as-arrays?]
                    :or {row-fn identity
                         identifiers str/lower-case}}]
  (let [result-set-fn (or result-set-fn (if as-arrays? vec doall))]
    (db-query-with-resultset db (vec sql-params)
      (^{:once true} fn* [rset]
       ((^{:once true} fn* [rs]
         (result-set-fn (if as-arrays?
                          (cons (first rs)
                                (map row-fn (rest rs)))
                          (map row-fn rs))))
        (result-set-seq rset :identifiers identifiers :as-arrays? as-arrays?))))))

(defn execute!
  "Given a database connection and a vector containing SQL and optional parameters,
  perform a general (non-select) SQL operation. The optional keyword argument specifies
  whether to run the operation in a transaction or not (default true)."
  [db sql-params & {:keys [transaction? multi?]
                    :or {transaction? true multi? false}}]
  (let [param-groups (rest sql-params)
        execute-helper
        (^{:once true} fn* [db]
         (if multi?
           (apply db-do-prepared db transaction? (first sql-params) param-groups)
           (if (seq param-groups)
             (db-do-prepared db transaction? (first sql-params) param-groups)
             (db-do-prepared db transaction? (first sql-params)))))]
    (if-let [con (db-find-connection db)]
      (execute-helper db)
      (with-open [^java.sql.Connection con (get-connection db)]
        (execute-helper (add-connection db con))))))

(defn- table-str
  "Transform a table spec to an entity name for SQL. The table spec may be a
  string, a keyword or a map with a single pair - table name and alias."
  [table entities]
  (if (map? table)
    (let [[k v] (first table)]
      (str (as-sql-name entities k) " " (as-sql-name entities v)))
    (as-sql-name entities table)))

(defn- delete-sql
  "Given a table name, a where class and its parameters and an optional entities spec,
  return a vector of the SQL for that delete operation followed by its parameters. The
  entities spec (default 'as-is') specifies how to transform column names."
  [table [where & params] & {:keys [entities] :or {entities identity}}]
  (into [(str "DELETE FROM " (table-str table entities)
              (when where " WHERE ") where)]
        params))

(defn delete!
  "Given a database connection, a table name and a where clause of columns to match,
  perform a delete. The optional keyword arguments specify how to transform
  column names in the map (default 'as-is') and whether to run the delete in
  a transaction (default true).
  Example:
    (delete! db :person [\"zip = ?\" 94546])
  is equivalent to:
    (execute! db [\"DELETE FROM person WHERE zip = ?\" 94546])"
  [db table where-clause & {:keys [entities transaction?]
                            :or {entities identity transaction? true}}]
  (execute! db
            (delete-sql table where-clause :entities entities)
            :transaction? transaction?))

(defn- multi-insert-helper
  "Given a (connected) database connection and some SQL statements (for multiple
   inserts), run a prepared statement on each and return any generated keys.
   Note: we are eager so an unrealized lazy-seq cannot escape from the connection."
  [db stmts]
  (doall (map (fn [row]
                (db-do-prepared-return-keys db false (first row) (rest row)))
              stmts)))

(defn- insert-helper
  "Given a (connected) database connection, a transaction flag and some SQL statements
   (for one or more inserts), run a prepared statement or a sequence of them."
  [db transaction? stmts]
  (if (string? (first stmts))
    (apply db-do-prepared db transaction? (first stmts) (rest stmts))
    (if transaction?
      (with-db-transaction [t-db db] (multi-insert-helper t-db stmts))
      (multi-insert-helper db stmts))))

(defn- extract-transaction?
  "Given a sequence of data, look for :transaction? arg in it and return a pair of
   the transaction? value (defaulting to true) and the data without the option."
  [data]
  (let [before (take-while (partial not= :transaction?) data)
        after  (drop-while (partial not= :transaction?) data)]
    (if (seq after)
      [(second after) (concat before (nnext after))]
      [true data])))

(defn- col-str
  "Transform a column spec to an entity name for SQL. The column spec may be a
  string, a keyword or a map with a single pair - column name and alias."
  [col entities]
  (if (map? col)
    (let [[k v] (first col)]
      (str (as-sql-name entities k) " AS " (as-sql-name entities v)))
    (as-sql-name entities col)))

(defn- insert-multi-row-sql
  "Given a table and a list of columns, followed by a list of column value sequences,
  return a vector of the SQL needed for the insert followed by the list of column
  value sequences. The entities function specifies how column names are transformed."
  [table columns values entities]
  (let [nc (count columns)
        vcs (map count values)]
    (if (not (and (or (zero? nc) (= nc (first vcs))) (apply = vcs)))
      (throw (IllegalArgumentException. "insert called with inconsistent number of columns / values"))
      (into [(str "INSERT INTO " (table-str table entities)
                  (when (seq columns)
                    (str " ( "
                         (str/join ", " (map (fn [col] (col-str col entities)) columns))
                         " )"))
                  " VALUES ( "
                  (str/join ", " (repeat (first vcs) "?"))
                  " )")]
            values))))

(defn- insert-single-row-sql
  "Given a table and a map representing a row, return a vector of the SQL needed for
  the insert followed by the list of column values. The entities function specifies
  how column names are transformed."
  [table row entities]
  (let [ks (keys row)]
    (into [(str "INSERT INTO " (table-str table entities) " ( "
                (str/join ", " (map (fn [col] (col-str col entities)) ks))
                " ) VALUES ( "
                (str/join ", " (repeat (count ks) "?"))
                " )")]
          (vals row))))

(defn insert-sql
  "Given a table name and either column names and values or maps representing rows,
  return a vector of the SQL for that insert operation followed by its parameters. An
  optional entities spec (default 'as-is') specifies how to transform column names."
  [table & clauses]
  (let [rows (take-while map? clauses)
        n-rows (count rows)
        cols-and-vals-etc (drop n-rows clauses)
        cols-and-vals (take-while (comp not keyword?) cols-and-vals-etc)
        n-cols-and-vals (count cols-and-vals)
        no-cols-and-vals (zero? n-cols-and-vals)
        options (drop (+ (count rows) (count cols-and-vals)) clauses)
        {:keys [entities] :or {entities identity}} (apply hash-map options)]
    (if (zero? n-rows)
      (if no-cols-and-vals
        (throw (IllegalArgumentException. "insert called without data to insert"))
        (if (< n-cols-and-vals 2)
          (throw (IllegalArgumentException. "insert called with columns but no values"))
          (insert-multi-row-sql table (first cols-and-vals) (rest cols-and-vals) entities)))
      (if no-cols-and-vals
        (map (fn [row] (insert-single-row-sql table row entities)) rows)
        (throw (IllegalArgumentException. "insert may take records or columns and values, not both"))))))

(defn insert!
  "Given a database connection, a table name and either maps representing rows or
   a list of column names followed by lists of column values, perform an insert.
   Use :transaction? argument to specify whether to run in a transaction or not.
   The default is true (use a transaction)."
  [db table & options]
  (let [[transaction? maps-or-cols-and-values-etc] (extract-transaction? options)
        stmts (apply insert-sql table maps-or-cols-and-values-etc)]
    (if-let [con (db-find-connection db)]
      (insert-helper db transaction? stmts)
      (with-open [^java.sql.Connection con (get-connection db)]
        (insert-helper (add-connection db con) transaction? stmts)))))

(defn- update-sql
  "Given a table name and a map of columns to set, and optional map of columns to
  match (and an optional entities spec), return a vector of the SQL for that update
  followed by its parameters. Example:
    (update :person {:zip 94540} [\"zip = ?\" 94546])
  returns:
    [\"UPDATE person SET zip = ? WHERE zip = ?\" 94540 94546]"
  [table set-map & where-etc]
  (let [[where-clause & options] (when-not (keyword? (first where-etc)) where-etc)
        [where & params] where-clause
        {:keys [entities] :or {entities identity}} (if (keyword? (first where-etc)) where-etc options)
        ks (keys set-map)
        vs (vals set-map)]
    (cons (str "UPDATE " (table-str table entities)
               " SET " (str/join
                        ","
                        (map (fn [k v]
                               (str (as-sql-name entities k)
                                    " = "
                                    (if (nil? v) "NULL" "?")))
                             ks vs))
               (when where " WHERE ")
               where)
          (concat (remove nil? vs) params))))

(defn update!
  "Given a database connection, a table name, a map of column values to set and a
  where clause of columns to match, perform an update. The optional keyword arguments
  specify how column names (in the set / match maps) should be transformed (default
  'as-is') and whether to run the update in a transaction (default true).
  Example:
    (update! db :person {:zip 94540} [\"zip = ?\" 94546])
  is equivalent to:
    (execute! db [\"UPDATE person SET zip = ? WHERE zip = ?\" 94540 94546])"
  [db table set-map where-clause & {:keys [entities transaction?]
                                    :or {entities identity transaction? true}}]
  (execute! db
            (update-sql table set-map where-clause :entities entities)
            :transaction? transaction?))

(defn create-table-ddl
  "Given a table name and column specs with an optional table-spec
   return the DDL string for creating that table."
  [table & specs]
  (let [col-specs (take-while (fn [s]
                                (not (or (= :table-spec s)
                                         (= :entities s)))) specs)
        other-specs (drop (count col-specs) specs)
        {:keys [table-spec entities] :or {entities identity}} other-specs
        table-spec-str (or (and table-spec (str " " table-spec)) "")
        spec-to-string (fn [spec]
                         (str/join " " (cons (as-sql-name entities (first spec))
                                             (map name (rest spec)))))]
    (format "CREATE TABLE %s (%s)%s"
            (as-sql-name entities table)
            (str/join ", " (map spec-to-string col-specs))
            table-spec-str)))

(defn drop-table-ddl
  "Given a table name, return the DDL string for dropping that table."
  [name & {:keys [entities] :or {entities identity}}]
  (format "DROP TABLE %s" (as-sql-name entities name)))

(defmacro
  ^{:doc "Original name for with-db-transaction. Use that instead."
    :deprecated "0.3.0"}
  db-transaction
  [binding & body]
  (println "DEPRECATED: Use with-db-transaction instead of db-transaction.")
  `(db-transaction* ~(second binding)
                    (^{:once true} fn* [~(first binding)] ~@body)))

