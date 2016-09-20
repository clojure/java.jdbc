;;  Copyright (c) 2008-2016 Sean Corfield, Stephen C. Gilardi. All rights reserved.
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

http://clojure-doc.org/articles/ecosystem/java_jdbc/home.html" }
  clojure.java.jdbc
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [clojure.walk :as walk])
  (:import (java.net URI)
           (java.sql BatchUpdateException DriverManager
                     PreparedStatement ResultSet SQLException Statement Types)
           (java.util Hashtable Map Properties)
           (javax.sql DataSource)))

(defn as-sql-name
  "Given a naming strategy function and a keyword or string, return
   a string per that naming strategy.
   A name of the form x.y is treated as multiple names, x, y, etc,
   and each are turned into strings via the naming strategy and then
   joined back together so x.y might become `x`.`y` if the naming
   strategy quotes identifiers with `."
  [f x]
  (let [n (name x)
        i (.indexOf n (int \.))]
    (if (= -1 i)
      (f n)
      (str/join "." (map f (.split n "\\."))))))

(defn quoted
  "Given a (vector) pair of delimiters (characters or strings), return a naming
  strategy function that will quote SQL entities with them.
  Given a single delimiter, treat it as a (vector) pair of that delimiter.
    ((quoted [\\[ \\]]) \"foo\") will return \"[foo]\" -- for MS SQL Server
    ((quoted \\`') \"foo\") will return \"`foo`\" -- for MySQL
  Intended to be used with :entities to provide a quoting (naming) strategy that
  is appropriate for your database."
  [q]
  (if (vector? q)
    (fn [x]
      (str (first q) x (last q)))
    (quoted [q q])))

(defn- table-str
  "Transform a table spec to an entity name for SQL. The table spec may be a
  string, a keyword or a map with a single pair - table name and alias."
  [table entities]
  (let [entities (or entities identity)]
    (if (map? table)
      (let [[k v] (first table)]
        (str (as-sql-name entities k) " " (as-sql-name entities v)))
      (as-sql-name entities table))))

(defn- kv-sql
  "Given a sequence of column name keys and a matching sequence of column
  values, and an entities mapping function, return a sequence of SQL fragments
  that can be joined for part of an UPDATE SET or a SELECT WHERE clause.
  Note that we pass the appropriate operator for NULL since it is different
  in each case."
  [ks vs entities null-op]
  (map (fn [k v]
         (str (as-sql-name entities k)
              (if (nil? v) null-op " = ?")))
       ks vs))

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

;; convenience for working with different forms of connections
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
  {"derby"          "org.apache.derby.jdbc.EmbeddedDriver"
   "h2"             "org.h2.Driver"
   "hsqldb"         "org.hsqldb.jdbcDriver"
   "jtds:sqlserver" "net.sourceforge.jtds.jdbc.Driver"
   "mysql"          "com.mysql.jdbc.Driver"
   "oracle:oci"     "oracle.jdbc.OracleDriver"
   "oracle:thin"    "oracle.jdbc.OracleDriver"
   "postgresql"     "org.postgresql.Driver"
   "sqlite"         "org.sqlite.JDBC"
   "sqlserver"      "com.microsoft.sqlserver.jdbc.SQLServerDriver"})

(def ^{:private true :doc "Map of schemes to subprotocols"} subprotocols
  {"hsql"     "hsqldb"
   "jtds"     "jtds:sqlserver"
   "mssql"    "sqlserver"
   "oracle"   "oracle:thin"
   "postgres" "postgresql"})

(defn- parse-properties-uri [^URI uri]
  (let [host (.getHost uri)
        port (if (pos? (.getPort uri)) (.getPort uri))
        path (.getPath uri)
        scheme (.getScheme uri)
        ^String query (.getQuery uri)
        query-parts (and query (for [^String kvs (.split query "&")]
                                 (vec (.split kvs "="))))]
    (merge
     {:subname (if host
                 (if port
                   (str "//" host ":" port path)
                   (str "//" host path))
                 (.getSchemeSpecificPart uri))
      :subprotocol (subprotocols scheme scheme)}
     (if-let [user-info (.getUserInfo uri)]
       {:user (first (str/split user-info #":"))
        :password (second (str/split user-info #":"))})
     (walk/keywordize-keys (into {} query-parts)))))

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
  "Creates a connection to a database. db-spec is usually a map containing connection
  parameters but can also be a URI or a String. The various possibilities are described
  below:

  DriverManager (preferred):
    :dbtype      (required) a String, the type of the database (the jdbc subprotocol)
    :dbname      (required) a String, the name of the database
    :host        (optional) a String, the host name/IP of the database
                            (defaults to 127.0.0.1)
    :port        (optional) a Long, the port of the database
                            (defaults to 3306 for mysql, 1433 for mssql/jtds, else nil)
    (others)     (optional) passed to the driver as properties.

  Raw:
    :connection-uri (required) a String
                 Passed directly to DriverManager/getConnection

  Other formats accepted:

  Existing Connection:
    :connection  (required) an existing open connection that can be used
                 but cannot be closed (only the parent connection can be closed)

  DriverManager (alternative / legacy style):
    :subprotocol (required) a String, the jdbc subprotocol
    :subname     (required) a String, the jdbc subname
    :classname   (optional) a String, the jdbc driver class name
    (others)     (optional) passed to the driver as properties.

  Factory:
    :factory     (required) a function of one argument, a map of params
    (others)     (optional) passed to the factory function in a map

  DataSource:
    :datasource  (required) a javax.sql.DataSource
    :username    (optional) a String
    :user        (optional) a String - an alternate alias for :username
                            (added after 0.3.0-beta2 for consistency JDBC-74)
    :password    (optional) a String, required if :username is supplied

  JNDI:
    :name        (required) a String or javax.naming.Name
    :environment (optional) a java.util.Map

  java.net.URI:
    Parsed JDBC connection string (see java.lang.String format next)

  java.lang.String:
    subprotocol://user:password@host:post/subname
                 An optional prefix of jdbc: is allowed."
  ^java.sql.Connection
  [{:keys [connection
           factory
           connection-uri
           classname subprotocol subname
           dbtype dbname host port
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
    (let [;; allow aliases for subprotocols
          subprotocol (subprotocols subprotocol subprotocol)
          url (format "jdbc:%s:%s" subprotocol subname)
          etc (dissoc db-spec :classname :subprotocol :subname)
          classname (or classname (classnames subprotocol))]
      (clojure.lang.RT/loadClassForName classname)
      (DriverManager/getConnection url (as-properties etc)))

    (and dbtype dbname)
    (let [;; allow aliases for dbtype
          subprotocol (subprotocols dbtype dbtype)
          host (or host "127.0.0.1")
          port (or port (condp = subprotocol
                          "jtds:sqlserver" 1433
                          "mysql"          3306
                          "postgresql"     5432
                          "sqlserver"      1433
                          nil))
          db-sep (if (= "sqlserver" subprotocol) ";DATABASENAME=" "/")
          url (if (#{"derby" "h2" "hsqldb" "sqlite"} subprotocol)
                (str "jdbc:" subprotocol ":" dbname)
                (str "jdbc:" subprotocol "://" host
                     (when port (str ":" port))
                     db-sep dbname))
          etc (dissoc db-spec :dbtype :dbname)]
      (clojure.lang.RT/loadClassForName (classnames subprotocol))
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

(defprotocol ISQLParameter
  "Protocol for setting SQL parameters in statement objects, which
   can convert from Clojure values. The default implementation just
   delegates the conversion to ISQLValue's sql-value conversion and
   uses .setObject on the parameter. It can be extended to use other
   methods of PreparedStatement to convert and set parameter values."
  (set-parameter [val stmt ix]
    "Convert a Clojure value into a SQL value and store it as the ix'th
     parameter in the given SQL statement object."))

(extend-protocol ISQLParameter
  Object
  (set-parameter [v ^PreparedStatement s ^long i]
    (.setObject s i (sql-value v)))

  nil
  (set-parameter [_ ^PreparedStatement s ^long i]
    (.setObject s i (sql-value nil))))

(defn- dft-set-parameters
  "Default implementation of parameter setting for the given statement."
  [stmt params]
  (dorun (map-indexed (fn [ix value]
                        (set-parameter value stmt (inc ix)))
                      params)))

(defprotocol IResultSetReadColumn
  "Protocol for reading objects from the java.sql.ResultSet. Default
   implementations (for Object and nil) return the argument, and the
   Boolean implementation ensures a canonicalized true/false value,
   but it can be extended to provide custom behavior for special types."
  (result-set-read-column [val rsmeta idx]
    "Function for transforming values after reading them from the database"))

(extend-protocol IResultSetReadColumn
  Object
  (result-set-read-column [x _2 _3] x)

  Boolean
  (result-set-read-column [x _2 _3] (if (= true x) true false))

  nil
  (result-set-read-column [_1 _2 _3] nil))

(defn- dft-read-columns
  "Default implementation of reading row values from result set, given the
  result set metadata and the indices."
  [^ResultSet rs rsmeta idxs]
  (mapv (fn [^Integer i] (result-set-read-column (.getObject rs i) rsmeta i)) idxs))

(defn result-set-seq
  "Creates and returns a lazy sequence of maps corresponding to the rows in the
  java.sql.ResultSet rs. Loosely based on clojure.core/resultset-seq but it
  respects the specified naming strategy. Duplicate column names are made unique
  by appending _N before applying the naming strategy (where N is a unique integer),
  unless the :as-arrays? option is :cols-as-is, in which case the column names
  are untouched (the result set maintains column name/value order).
  The :identifiers option specifies how SQL column names are converted to Clojure
  keywords. The default is to convert them to lower case.
  The :qualifier option specifies the namespace qualifier for those identifiers."
  ([rs] (result-set-seq rs {}))
  ([^ResultSet rs {:keys [as-arrays? identifiers qualifier read-columns]
                   :or {identifiers str/lower-case
                        read-columns dft-read-columns}}]
   (let [rsmeta (.getMetaData rs)
         idxs (range 1 (inc (.getColumnCount rsmeta)))
         col-name-fn (if (= :cols-as-is as-arrays?) identity make-cols-unique)
         identifier-fn (if qualifier
                         (comp (partial keyword qualifier) identifiers)
                         (comp keyword identifiers))
         keys (->> idxs
                   (mapv (fn [^Integer i] (.getColumnLabel rsmeta i)))
                   col-name-fn
                   (mapv identifier-fn))
         row-values (fn [] (read-columns rs rsmeta idxs))
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
       (records)))))

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

(defn ^{:tag (class (into-array String []))} string-array
  [return-keys]
  (into-array String return-keys))

(defn prepare-statement
  "Create a prepared statement from a connection, a SQL string and a map
  of options:
     :return-keys truthy | nil - default nil
       for some drivers, this may be a vector of column names to identify
       the generated keys to return, otherwise it should just be true
     :result-type :forward-only | :scroll-insensitive | :scroll-sensitive
     :concurrency :read-only | :updatable
     :cursors
     :fetch-size n
     :max-rows n
     :timeout n"
  ([con sql] (prepare-statement con sql {}))
  ([^java.sql.Connection con ^String sql
    {:keys [return-keys result-type concurrency cursors
            fetch-size max-rows timeout]}]
   (let [^PreparedStatement
         stmt (cond return-keys
                    (try
                      (if (vector? return-keys)
                        (try
                          (.prepareStatement con sql (string-array return-keys))
                          (catch Exception _
                            ;; assume it is unsupported and try regular generated keys:
                            (.prepareStatement con sql java.sql.Statement/RETURN_GENERATED_KEYS)))
                        (.prepareStatement con sql java.sql.Statement/RETURN_GENERATED_KEYS))
                      (catch Exception _
                        ;; assume it is unsupported and try basic PreparedStatement:
                        (.prepareStatement con sql)))

                    (and result-type concurrency)
                    (if cursors
                      (.prepareStatement con sql
                                         (get result-set-type result-type result-type)
                                         (get result-set-concurrency concurrency concurrency)
                                         (get result-set-holdability cursors cursors))
                      (.prepareStatement con sql
                                         (get result-set-type result-type result-type)
                                         (get result-set-concurrency concurrency concurrency)))

                    :else
                    (.prepareStatement con sql))]
     (when fetch-size (.setFetchSize stmt fetch-size))
     (when max-rows (.setMaxRows stmt max-rows))
     (when timeout (.setQueryTimeout stmt timeout))
     stmt)))

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

(def ^:private
  isolation-levels
  "Transaction isolation levels."
  {:none             java.sql.Connection/TRANSACTION_NONE
   :read-committed   java.sql.Connection/TRANSACTION_READ_COMMITTED
   :read-uncommitted java.sql.Connection/TRANSACTION_READ_UNCOMMITTED
   :repeatable-read  java.sql.Connection/TRANSACTION_REPEATABLE_READ
   :serializable     java.sql.Connection/TRANSACTION_SERIALIZABLE})

(def ^:private isolation-kws
  "Map transaction isolation constants to our keywords."
  (set/map-invert isolation-levels))

(defn get-isolation-level
  "Given a db-spec (with an optional connection), return the current
  transaction isolation level, if known. Return nil if there is no
  active connection in the db-spec. Return :unknown if we do not
  recognize the isolation level."
  [db]
  (when-let [con (db-find-connection db)]
    (isolation-kws (.getTransactionIsolation con) :unknown)))

(defn db-transaction*
  "Evaluates func as a transaction on the open database connection. Any
  nested transactions are absorbed into the outermost transaction. By
  default, all database updates are committed together as a group after
  evaluating the outermost body, or rolled back on any uncaught
  exception. If rollback is set within scope of the outermost transaction,
  the entire transaction will be rolled back rather than committed when
  complete.
  The isolation option may be :none, :read-committed, :read-uncommitted,
  :repeatable-read, or :serializable. Note that not all databases support
  all of those isolation levels, and may either throw an exception or
  substitute another isolation level.
  The read-only? option puts the transaction in readonly mode (if supported)."
  ([db func] (db-transaction* db func {}))
  ([db func opts]
   (let [{:keys [isolation read-only?] :as opts}
         (merge (when (map? db) db) opts)]
     (if (zero? (get-level db))
       (if-let [con (db-find-connection db)]
         (let [nested-db (inc-level db)
               auto-commit (.getAutoCommit con)
               old-isolation (.getTransactionIsolation con)
               old-readonly  (.isReadOnly con)]
           (io!
            (when isolation
              (.setTransactionIsolation con (isolation isolation-levels)))
            (when read-only?
              (.setReadOnly con true))
            (.setAutoCommit con false)
            (try
              (let [result (func nested-db)]
                (if (db-is-rollback-only nested-db)
                  (.rollback con)
                  (.commit con))
                result)
              (catch Throwable t
                (.rollback con)
                (throw t))
              (finally
                (db-unset-rollback-only! nested-db)
                ;; the following can throw SQLExceptions but we do not
                ;; want those to replace any exception currently being
                ;; handled -- and if the connection got closed, we just
                ;; want to ignore exceptions here anyway
                (try
                  (.setAutoCommit con auto-commit)
                  (catch Exception _))
                (when isolation
                  (try
                    (.setTransactionIsolation con old-isolation)
                    (catch Exception _)))
                (when read-only?
                  (try
                    (.setReadOnly con old-readonly)
                    (catch Exception _)))))))
         (with-open [con (get-connection db)]
           (db-transaction* (add-connection db con) func opts)))
       (do
         (when (and isolation
                    (let [con (db-find-connection db)]
                      (not= (isolation isolation-levels)
                            (.getTransactionIsolation con))))
           (let [msg "Nested transactions may not have different isolation levels"]
             (throw (IllegalStateException. msg))))
         (func (inc-level db)))))))

(defmacro with-db-transaction
  "Evaluates body in the context of a transaction on the specified database connection.
  The binding provides the database connection for the transaction and the name to which
  that is bound for evaluation of the body. The binding may also specify the isolation
  level for the transaction, via the :isolation option and/or set the transaction to
  readonly via the :read-only? option.
  (with-db-transaction [t-con db-spec {:isolation level :read-only? true}]
    ... t-con ...)
  See db-transaction* for more details."
  [binding & body]
  `(db-transaction* ~(second binding)
                    (^{:once true} fn* [~(first binding)] ~@body)
                    ~@(rest (rest binding))))

(defmacro with-db-connection
  "Evaluates body in the context of an active connection to the database.
  (with-db-connection [con-db db-spec]
    ... con-db ...)"
  [binding & body]
  `(let [db-spec# ~(second binding)]
     (with-open [con# (get-connection db-spec#)]
       (let [~(first binding) (add-connection db-spec# con#)]
         ~@body))))

(defmacro with-db-metadata
  "Evaluates body in the context of an active connection with metadata bound
   to the specified name. See also metadata-result for dealing with the results
   of operations that retrieve information from the metadata.
   (with-db-metadata [md db-spec]
     ... md ...)"
  [binding & body]
  `(with-open [con# (get-connection ~(second binding))]
     (let [~(first binding) (.getMetaData con#)]
       ~@body)))

(defn metadata-result
  "If the argument is a java.sql.ResultSet, turn it into a result-set-seq,
  else return it as-is. This makes working with metadata easier.
  Also accepts an option map containing :identifiers, :qualifier, :as-arrays?,
  :row-fn,and :result-set-fn to control how the ResultSet is transformed and
  returned. See query for more details."
  ([rs-or-value] (metadata-result rs-or-value {}))
  ([rs-or-value {:keys [as-arrays? identifiers qualifier read-columns
                        result-set-fn row-fn]
                 :or {identifiers str/lower-case
                      read-columns dft-read-columns
                      row-fn identity}}]
   (let [result-set-fn (or result-set-fn (if as-arrays? vec doall))]
     (if (instance? java.sql.ResultSet rs-or-value)
       ((^{:once true} fn* [rs]
         (result-set-fn (if as-arrays?
                          (cons (first rs)
                                (map row-fn (rest rs)))
                          (map row-fn rs))))
        (result-set-seq rs-or-value {:as-arrays? as-arrays?
                                     :identifiers identifiers
                                     :qualifier qualifier
                                     :read-columns read-columns}))
       rs-or-value))))

(defmacro metadata-query
  "Given a Java expression that extracts metadata (in the context of with-db-metadata),
  and a map of options like metadata-result, manage the connection for a single
  metadata-based query. Example usage:

  (with-db-metadata [meta db-spec]
    (metadata-query (.getTables meta nil nil nil (into-array String [\"TABLE\"]))
      {:row-fn ... :result-set-fn ...}))"
  [meta-query & opt-args]
  `(with-open [rs# ~meta-query]
     (metadata-result rs# ~@opt-args)))

(defn db-do-commands
  "Executes SQL commands on the specified database connection. Wraps the commands
  in a transaction if transaction? is true. transaction? can be ommitted and it
  defaults to true. Accepts a single SQL command (string) or a vector of them.
  Uses executeBatch. This may affect what SQL you can run via db-do-commands."
  ([db sql-commands]
   (db-do-commands db true (if (string? sql-commands) [sql-commands] sql-commands)))
  ([db transaction? sql-commands]
   (if (string? sql-commands)
     (db-do-commands db transaction? [sql-commands])
     (if-let [con (db-find-connection db)]
       (with-open [^Statement stmt (.createStatement con)]
         (doseq [^String cmd sql-commands]
           (.addBatch stmt cmd))
         (if transaction?
           (with-db-transaction [t-db (add-connection db (.getConnection stmt))]
             (execute-batch stmt))
           (execute-batch stmt)))
       (with-open [con (get-connection db)]
         (db-do-commands (add-connection db con) transaction? sql-commands))))))

(defn- db-do-execute-prepared-return-keys
  "Executes a PreparedStatement, optionally in a transaction, and (attempts to)
  return any generated keys."
  [db ^PreparedStatement stmt param-group {:keys [transaction?] :as opts}]
  (let [opts (merge (when (map? db) db) opts)
        exec-and-return-keys
        (^{:once true} fn* []
         (let [counts (.executeUpdate stmt)]
           (try
             (let [rs (.getGeneratedKeys stmt)
                   result (first (result-set-seq rs opts))]
               ;; sqlite (and maybe others?) requires
               ;; record set to be closed
               (.close rs)
               result)
             (catch Exception _
               ;; assume generated keys is unsupported and return counts instead:
               counts))))]
    ((:set-parameters opts dft-set-parameters) stmt param-group)
    (if transaction?
      (with-db-transaction [t-db (add-connection db (.getConnection stmt))]
        (exec-and-return-keys))
      (exec-and-return-keys))))

(defn- sql-stmt?
  "Given an expression, return true if it is either a string (SQL) or a
  PreparedStatement."
  [expr]
  (or (string? expr) (instance? PreparedStatement expr)))

(defn db-do-prepared-return-keys
  "Executes an (optionally parameterized) SQL prepared statement on the
  open database connection. The param-group is a seq of values for all of
  the parameters. transaction? can be ommitted and will default to true.
  Return the generated keys for the (single) update/insert.
  A PreparedStatement may be passed in, instead of a SQL string, in which
  case :return-keys MUST BE SET on that PreparedStatement!"
  ([db sql-params]
   (db-do-prepared-return-keys db true sql-params {}))
  ([db transaction? sql-params]
   (if (map? sql-params)
     (db-do-prepared-return-keys db true transaction? sql-params)
     (db-do-prepared-return-keys db transaction? sql-params {})))
  ([db transaction? sql-params opts]
   (let [opts (merge (when (map? db) db) opts)]
     (if-let [con (db-find-connection db)]
       (let [[sql & params] (if (sql-stmt? sql-params) (vector sql-params) (vec sql-params))]
         (if (instance? PreparedStatement sql)
           (db-do-execute-prepared-return-keys db sql params (assoc opts :transaction? transaction?))
           (with-open [^PreparedStatement stmt (prepare-statement con sql (assoc opts :return-keys true))]
             (db-do-execute-prepared-return-keys db stmt params (assoc opts :transaction? transaction?)))))
       (with-open [con (get-connection db)]
         (db-do-prepared-return-keys (add-connection db con) transaction? sql-params opts))))))

(defn- db-do-execute-prepared-statement
  "Execute a PreparedStatement, optionally in a transaction."
  [db ^PreparedStatement stmt param-groups {:keys [transaction?] :as opts}]
  (let [opts (merge (when (map? db) db) opts)]
    (if (empty? param-groups)
      (if transaction?
        (with-db-transaction [t-db (add-connection db (.getConnection stmt))]
          (vector (.executeUpdate stmt)))
        (vector (.executeUpdate stmt)))
      (do
        (doseq [param-group param-groups]
          ((:set-parameters opts dft-set-parameters) stmt param-group)
          (.addBatch stmt))
        (if transaction?
          (with-db-transaction [t-db (add-connection db (.getConnection stmt))]
            (execute-batch stmt))
          (execute-batch stmt))))))

(defn db-do-prepared
  "Executes an (optionally parameterized) SQL prepared statement on the
  open database connection. Each param-group is a seq of values for all of
  the parameters. transaction? can be omitted and defaults to true.
  The sql parameter can either be a SQL string or a PreparedStatement.
  Return a seq of update counts (one count for each param-group)."
  ([db sql-params]
   (db-do-prepared db true sql-params {}))
  ([db transaction? sql-params]
   (if (map? sql-params)
     (db-do-prepared db true transaction? sql-params)
     (db-do-prepared db transaction? sql-params {})))
  ([db transaction? sql-params opts]
   (let [opts (merge (when (map? db) db) opts)]
     (if-let [con (db-find-connection db)]
       (let [[sql & params] (if (sql-stmt? sql-params) (vector sql-params) (vec sql-params))
             params         (if (or (:multi? opts) (empty? params)) params [params])]
         (if (instance? PreparedStatement sql)
           (db-do-execute-prepared-statement db sql params (assoc opts :transaction? transaction?))
           (with-open [^PreparedStatement stmt (prepare-statement con sql opts)]
             (db-do-execute-prepared-statement db stmt params (assoc opts :transaction? transaction?)))))
       (with-open [con (get-connection db)]
         (db-do-prepared (add-connection db con) transaction? sql-params opts))))))

(defn db-query-with-resultset
  "Executes a query, then evaluates func passing in the raw ResultSet as an
   argument. The second argument is a vector containing either:
    [sql & params] - a SQL query, followed by any parameters it needs
    [stmt & params] - a PreparedStatement, followed by any parameters it needs
                      (the PreparedStatement already contains the SQL query)
  The opts map is passed to prepare-statement.
  Uses executeQuery. This may affect what SQL you can run via query."
  ([db sql-params func] (db-query-with-resultset db sql-params func {}))
  ([db sql-params func opts]
   (let [opts (merge (when (map? db) db) opts)
         [sql & params] (if (sql-stmt? sql-params) (vector sql-params) (vec sql-params))
         run-query-with-params (^{:once true} fn* [^PreparedStatement stmt]
                                ((:set-parameters opts dft-set-parameters) stmt params)
                                (with-open [rset (.executeQuery stmt)]
                                  (func rset)))]
     (when-not (sql-stmt? sql)
       (let [^Class sql-class (class sql)
             ^String msg (format "\"%s\" expected %s %s, found %s %s"
                                 "sql-params"
                                 "vector"
                                 "[sql param*]"
                                 (.getName sql-class)
                                 (pr-str sql))]
         (throw (IllegalArgumentException. msg))))
     (if (instance? PreparedStatement sql)
       (let [^PreparedStatement stmt sql]
         (run-query-with-params stmt))
       (if-let [con (db-find-connection db)]
         (with-open [^PreparedStatement stmt (prepare-statement con sql opts)]
           (run-query-with-params stmt))
         (with-open [con (get-connection db)]
           (with-open [^PreparedStatement stmt (prepare-statement con sql opts)]
             (run-query-with-params stmt))))))))

;; top-level API for actual SQL operations

(defn query
  "Given a database connection and a vector containing SQL and optional parameters,
  perform a simple database query. The options specify how to construct the result
  set (and are also passed to prepare-statement as needed):
    :as-arrays? - return the results as a set of arrays, default false.
    :identifiers - applied to each column name in the result set, default lower-case
    :qualifier - optionally provides the namespace qualifier for identifiers
    :result-set-fn - applied to the entire result set, default doall / vec
        if :as-arrays? true, :result-set-fn will default to vec
        if :as-arrays? false, :result-set-fn will default to doall
    :row-fn - applied to each row as the result set is constructed, default identity
  The second argument is a vector containing a SQL string or PreparedStatement, followed
  by any parameters it needs.
  See also prepare-statement for additional options."
  ([db sql-params] (query db sql-params {}))
  ([db sql-params opts]
   (let [{:keys [as-arrays? explain? explain-fn identifiers qualifier
                 read-columns result-set-fn row-fn] :as opts}
         (merge {:explain-fn println :identifiers str/lower-case
                 :read-columns dft-read-columns :row-fn identity}
                (when (map? db) db)
                opts)
         result-set-fn (or result-set-fn (if as-arrays? vec doall))
         sql-params-vector (if (sql-stmt? sql-params) (vector sql-params) (vec sql-params))]
     (when (and explain? (string? (first sql-params-vector)))
       (query db (into [(str (if (string? explain?) explain? "EXPLAIN")
                             " "
                             (first sql-params-vector))]
                       (rest sql-params-vector))
              (-> opts
                  (dissoc :explain? :result-set-fn :row-fn)
                  (assoc :result-set-fn explain-fn))))
     (db-query-with-resultset db sql-params-vector
                              (^{:once true} fn* [rset]
                               ((^{:once true} fn* [rs]
                                 (result-set-fn (if as-arrays?
                                                  (cons (first rs)
                                                        (map row-fn (rest rs)))
                                                  (map row-fn rs))))
                                (result-set-seq rset {:as-arrays? as-arrays?
                                                      :identifiers identifiers
                                                      :qualifier qualifier
                                                      :read-columns read-columns})))
                              opts))))

(defn- direction
  "Given an entities function, a column name, and a direction,
  return the matching SQL column / order.
  Throw an exception for an invalid direction."
  [entities c d]
  (str (as-sql-name entities c) " "
       (if-let [dir (#{"ASC" "DESC"} (str/upper-case (name d)))]
         dir
         (throw (IllegalArgumentException. (str "expected :asc or :desc, found: " d))))))

(defn- order-by-sql
  "Given a sequence of column specs and an entities function, return
  a SQL fragment for the ORDER BY clause. A column spec may be a name
  (either a string or keyword) or a map from column name to direction
  (:asc or :desc)."
  [order-by entities]
  (str/join ", " (mapcat (fn [col]
                           (if (map? col)
                             (reduce-kv (fn [v c d]
                                          (conj v (direction entities c d)))
                                        []
                                        col)
                             [(direction entities col :asc)])) order-by)))

(defn find-by-keys
  "Given a database connection, a table name, a map of column name/value
  pairs, and an optional options map, return any matching rows.
  An :order-by option may be supplied to sort the rows by a sequence of
  columns, e.g,. {:order-by [:name {:age :desc]}"
  ([db table columns] (find-by-keys db table columns {}))
  ([db table columns opts]
   (let [{:keys [entities order-by] :as opts}
         (merge {:entities identity}
                (when (map? db) db)
                opts)
         ks (keys columns)
         vs (vals columns)]
     (query db (into [(str "SELECT * FROM " (table-str table entities)
                           " WHERE " (str/join " AND "
                                               (kv-sql ks vs entities " IS NULL"))
                           (when (seq order-by)
                             (str " ORDER BY "
                                  (order-by-sql order-by entities))))]
                     (remove nil? vs))
            opts))))

(defn get-by-id
  "Given a database connection, a table name, a primary key value, an
  optional primary key column name, and an optional options map, return
  a single matching row, or nil.
  The primary key column name defaults to :id."
  ([db table pk-value] (get-by-id db table pk-value :id {}))
  ([db table pk-value pk-name-or-opts]
   (if (map? pk-name-or-opts)
     (get-by-id db table pk-value :id pk-name-or-opts)
     (get-by-id db table pk-value pk-name-or-opts {})))
  ([db table pk-value pk-name opts]
   (let [opts (merge (when (map? db) db) opts)
         r-s-fn (or (:result-set-fn opts) identity)]
     (find-by-keys db table {pk-name pk-value}
                   (assoc opts :result-set-fn (comp first r-s-fn))))))

(defn execute!
  "Given a database connection and a vector containing SQL (or PreparedStatement)
  followed by optional parameters, perform a general (non-select) SQL operation.
  The :transaction? option specifies whether to run the operation in a
  transaction or not (default true).
  If the :multi? option is false (the default), the SQL statement should be
  followed by the parameters for that statement.
  If the :multi? option is true, the SQL statement should be followed by one or
  more vectors of parameters, one for each application of the SQL statement.
  If there are no parameters specified, executeUpdate will be used, otherwise
  executeBatch will be used. This may affect what SQL you can run via execute!"
  ([db sql-params] (execute! db sql-params {}))
  ([db sql-params opts]
   (let [{:keys [transaction? multi?]
          :as opts}
         (merge {:transaction? true :multi? false} (when (map? db) db) opts)
         execute-helper
         (^{:once true} fn* [db]
          (db-do-prepared db transaction? sql-params opts))]
     (if-let [con (db-find-connection db)]
       (execute-helper db)
       (with-open [con (get-connection db)]
         (execute-helper (add-connection db con)))))))

(defn- delete-sql
  "Given a table name, a where class and its parameters and an optional entities spec,
  return a vector of the SQL for that delete operation followed by its parameters. The
  entities spec (default 'as-is') specifies how to transform column names."
  [table [where & params] entities]
  (into [(str "DELETE FROM " (table-str table entities)
              (when where " WHERE ") where)]
        params))

(defn delete!
  "Given a database connection, a table name and a where clause of columns to match,
  perform a delete. The options may specify how to transform column names in the
  map (default 'as-is') and whether to run the delete in a transaction (default true).
  Example:
    (delete! db :person [\"zip = ?\" 94546])
  is equivalent to:
    (execute! db [\"DELETE FROM person WHERE zip = ?\" 94546])"
  ([db table where-clause] (delete! db table where-clause {}))
  ([db table where-clause opts]
   (let [{:keys [entities transaction?]
          :as   opts}
         (merge {:entities identity :transaction? true} (when (map? db) db) opts)]
     (execute! db
               (delete-sql table where-clause entities)
               opts))))

(defn- multi-insert-helper
  "Given a (connected) database connection and some SQL statements (for multiple
   inserts), run a prepared statement on each and return any generated keys.
   Note: we are eager so an unrealized lazy-seq cannot escape from the connection."
  [db stmts opts]
  (doall (map (fn [row] (db-do-prepared-return-keys db false row opts)) stmts)))

(defn- insert-helper
  "Given a (connected) database connection, a transaction flag and some SQL statements
   (for one or more inserts), run a prepared statement or a sequence of them."
  [db transaction? stmts opts]
  (if transaction?
    (with-db-transaction [t-db db] (multi-insert-helper t-db stmts opts))
    (multi-insert-helper db stmts opts)))

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
      (throw (IllegalArgumentException. "insert! called with inconsistent number of columns / values"))
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

(defn- insert-rows!
  "Given a database connection, a table name, a sequence of rows, and an options
  map, insert the rows into the database."
  [db table rows opts]
  (let [{:keys [entities identifiers qualifier transaction?]
         :as opts}
        (merge {:entities identity :identifiers str/lower-case :transaction? true}
               (when (map? db) db)
               opts)
        sql-params (map (fn [row]
                          (when-not (map? row)
                            (throw (IllegalArgumentException. "insert! / insert-multi! called with a non-map row")))
                          (insert-single-row-sql table row entities)) rows)]
    (if-let [con (db-find-connection db)]
      (insert-helper db transaction? sql-params opts)
      (with-open [con (get-connection db)]
        (insert-helper (add-connection db con) transaction? sql-params opts)))))

(defn- insert-cols!
  "Given a database connection, a table name, a sequence of columns names, a
  sequence of vectors of column values, one per row, and an options map,
  insert the rows into the database."
  [db table cols values opts]
  (let [{:keys [entities transaction?]
         :as opts}
        (merge {:entities identity :transaction? true :multi? true} (when (map? db) db) opts)
        sql-params (insert-multi-row-sql table cols values entities)]
    (if-let [con (db-find-connection db)]
      (db-do-prepared db transaction? sql-params opts)
      (with-open [con (get-connection db)]
        (db-do-prepared (add-connection db con) transaction? sql-params opts)))))

(defn insert!
  "Given a database connection, a table name and either a map representing a rows,
  or a list of column names followed by a list of column values also representing
  a single row, perform an insert.
  When inserting a row as a map, the result is the database-specific form of the
  generated keys, if available (note: PostgreSQL returns the whole row).
  When inserting a row as a list of column values, the result is the count of
  rows affected (1), if available (from getUpdateCount after executeBatch).
  The row map or column value vector may be followed by a map of options:
  The :transaction? option specifies whether to run in a transaction or not.
  The default is true (use a transaction). The :entities option specifies how
  to convert the table name and column names to SQL entities."
  ([db table row] (insert! db table row {}))
  ([db table cols-or-row values-or-opts]
   (if (map? values-or-opts)
     (insert-rows! db table [cols-or-row] values-or-opts)
     (insert-cols! db table cols-or-row [values-or-opts] {})))
  ([db table cols values opts]
   (insert-cols! db table cols [values] opts)))

(defn insert-multi!
  "Given a database connection, a table name and either a sequence of maps (for
  rows) or a sequence of column names, followed by a sequence of vectors (for
  the values in each row), and possibly a map of options, insert that data into
  the database.
  When inserting rows as a sequence of maps, the result is a sequence of the
  generated keys, if available (note: PostgreSQL returns the whole rows).
  When inserting rows as a sequence of lists of column values, the result is
  a sequence of the counts of rows affected (a sequence of 1's), if available.
  Yes, that is singularly unhelpful. Thank you getUpdateCount and executeBatch!
  The :transaction? option specifies whether to run in a transaction or not.
  The default is true (use a transaction). The :entities option specifies how
  to convert the table name and column names to SQL entities."
  ([db table rows] (insert-rows! db table rows {}))
  ([db table cols-or-rows values-or-opts]
   (if (map? values-or-opts)
     (insert-rows! db table cols-or-rows values-or-opts)
     (insert-cols! db table cols-or-rows values-or-opts {})))
  ([db table cols values opts]
   (insert-cols! db table cols values opts)))

(defn- update-sql
  "Given a table name, a map of columns to set, a optional map of columns to
  match, and an entities, return a vector of the SQL for that update followed
  by its parameters. Example:
    (update :person {:zip 94540} [\"zip = ?\" 94546] identity)
  returns:
    [\"UPDATE person SET zip = ? WHERE zip = ?\" 94540 94546]"
  [table set-map [where & params] entities]
  (let [ks (keys set-map)
        vs (vals set-map)]
    (cons (str "UPDATE " (table-str table entities)
               " SET " (str/join
                        ","
                        (kv-sql ks vs entities " = NULL"))
               (when where " WHERE ")
               where)
          (concat (remove nil? vs) params))))

(defn update!
  "Given a database connection, a table name, a map of column values to set and a
  where clause of columns to match, perform an update. The options may specify
  how column names (in the set / match maps) should be transformed (default
  'as-is') and whether to run the update in a transaction (default true).
  Example:
    (update! db :person {:zip 94540} [\"zip = ?\" 94546])
  is equivalent to:
    (execute! db [\"UPDATE person SET zip = ? WHERE zip = ?\" 94540 94546])"
  ([db table set-map where-clause] (update! db table set-map where-clause {}))
  ([db table set-map where-clause opts]
   (let [{:keys [entities]
          :as opts}
         (merge {:entities identity :transaction? true} (when (map? db) db) opts)]
     (execute! db
               (update-sql table set-map where-clause entities)
               opts))))

(defn create-table-ddl
  "Given a table name and a vector of column specs, return the DDL string for
  creating that table. Each column spec is, in turn, a vector of keywords or
  strings that is converted to strings and concatenated with spaces to form
  a single column description in DDL, e.g.,
    [:cost :int \"not null\"]
    [:name \"varchar(32)\"]
  The first element of a column spec is treated as a SQL entity (so if you
  provide the :entities option, that will be used to transform it). The
  remaining elements are left as-is when converting them to strings.
  An options map may be provided that can contain:
  :table-spec -- a string that is appended to the DDL -- and/or
  :entities -- a function to specify how column names are transformed."
  ([table specs] (create-table-ddl table specs {}))
  ([table specs opts]
   (let [table-spec     (:table-spec opts)
         entities       (:entities   opts identity)
         table-spec-str (or (and table-spec (str " " table-spec)) "")
         spec-to-string (fn [spec]
                          (try
                            (str/join " " (cons (as-sql-name entities (first spec))
                                                (map name (rest spec))))
                            (catch Exception _
                              (throw (IllegalArgumentException.
                                      "column spec is not a sequence of keywords / strings")))))]
     (format "CREATE TABLE %s (%s)%s"
             (as-sql-name entities table)
             (str/join ", " (map spec-to-string specs))
             table-spec-str))))

(defn drop-table-ddl
  "Given a table name, return the DDL string for dropping that table."
  ([name] (drop-table-ddl name {}))
  ([name {:keys [entities] :or {entities identity}}]
   (format "DROP TABLE %s" (as-sql-name entities name))))
