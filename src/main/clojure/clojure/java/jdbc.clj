;;  Copyright (c) 2008-2018 Sean Corfield, Stephen C. Gilardi. All rights reserved.
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
are the parameter values to be substituted.

For more documentation, see:

http://clojure-doc.org/articles/ecosystem/java_jdbc/home.html"}
  clojure.java.jdbc
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [clojure.walk :as walk])
  (:import (java.net URI)
           (java.sql BatchUpdateException DriverManager
                     PreparedStatement ResultSet ResultSetMetaData
                     SQLException Statement Types)
           (java.util Hashtable Map Properties)
           (javax.sql DataSource)))

(set! *warn-on-reflection* true)

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
  (cond (vector? q)
        (fn [x]
          (str (first q) x (last q)))
        (keyword? q)
        (case q
          :ansi      (quoted \")
          :mysql     (quoted \`)
          :oracle    (quoted \")
          :sqlserver (quoted [\[ \]]))
        :else
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
  (add-connection [s connection] {:connection connection :level 0 :connection-uri s})
  (get-level [_] 0)

  clojure.lang.Associative
  (add-connection [m connection] (assoc m :connection connection))
  (get-level [m] (or (:level m) 0))

  nil
  (add-connection [_ connection] {:connection connection :level 0 :legacy true})
  (get-level [_] 0))

(def ^:private classnames
  "Map of subprotocols to classnames. dbtype specifies one of these keys.

  The subprotocols map below provides aliases for dbtype.

  Most databases have just a single class name for their driver but we
  support a sequence of class names to try in order to allow for drivers
  that change their names over time (e.g., MySQL)."
  {"derby"          "org.apache.derby.jdbc.EmbeddedDriver"
   "h2"             "org.h2.Driver"
   "h2:mem"         "org.h2.Driver"
   "hsqldb"         "org.hsqldb.jdbcDriver"
   "jtds:sqlserver" "net.sourceforge.jtds.jdbc.Driver"
   "mysql"          ["com.mysql.cj.jdbc.Driver"
                     "com.mysql.jdbc.Driver"]
   "oracle:oci"     "oracle.jdbc.OracleDriver"
   "oracle:thin"    "oracle.jdbc.OracleDriver"
   "postgresql"     "org.postgresql.Driver"
   "pgsql"          "com.impossibl.postgres.jdbc.PGDriver"
   "redshift"       "com.amazon.redshift.jdbc.Driver"
   "sqlite"         "org.sqlite.JDBC"
   "sqlserver"      "com.microsoft.sqlserver.jdbc.SQLServerDriver"})

(def ^:private subprotocols
  "Map of schemes to subprotocols. Used to provide aliases for dbtype."
  {"hsql"       "hsqldb"
   "jtds"       "jtds:sqlserver"
   "mssql"      "sqlserver"
   "oracle"     "oracle:thin"
   "oracle:sid" "oracle:thin"
   "postgres"   "postgresql"})

(def ^:private host-prefixes
  "Map of subprotocols to non-standard host-prefixes.
  Anything not listed is assumed to use //."
  {"oracle:oci"  "@"
   "oracle:thin" "@"})

(def ^:private ports
  "Map of subprotocols to ports."
  {"jtds:sqlserver" 1433
   "mysql"          3306
   "oracle:oci"     1521
   "oracle:sid"     1521
   "oracle:thin"    1521
   "postgresql"     5432
   "sqlserver"      1433})

(def ^:private dbname-separators
  "Map of schemes to separators. The default is / but a couple are different."
  {"mssql"      ";DATABASENAME="
   "sqlserver"  ";DATABASENAME="
   "oracle:sid" ":"})

(defn- parse-properties-uri [^URI uri]
  (let [host (.getHost uri)
        port (if (pos? (.getPort uri)) (.getPort uri))
        path (.getPath uri)
        scheme (.getScheme uri)
        subprotocol (subprotocols scheme scheme)
        host-prefix (host-prefixes subprotocol "//")
        ^String query (.getQuery uri)
        query-parts (and query
                         (for [^String kvs (.split query "&")]
                           ((juxt first second) (.split kvs "="))))]
    (merge
     {:subname (if host
                 (if port
                   (str host-prefix host ":" port path)
                   (str host-prefix host path))
                 (.getSchemeSpecificPart uri))
      :subprotocol subprotocol}
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

(defn- modify-connection
  "Given a database connection and a map of options, update the connection
  as specified by the options."
  ^java.sql.Connection
  [^java.sql.Connection connection opts]
  (when (and connection (contains? opts :auto-commit?))
    (.setAutoCommit connection (boolean (:auto-commit? opts))))
  (when (and connection (contains? opts :read-only?))
    (.setReadOnly connection (boolean (:read-only? opts))))
  connection)

(defn- get-driver-connection
  "Common logic for loading the DriverManager and the designed JDBC driver
  class and obtaining the appropriate Connection object."
  [classname subprotocol db-spec url etc opts error-msg]
  (if-let [class-name (or classname (classnames subprotocol))]
    (do
      ;; force DriverManager to be loaded
      (DriverManager/getLoginTimeout)
      (if (string? class-name)
        (clojure.lang.RT/loadClassForName class-name)
        (loop [[clazz & more] class-name]
          (when-let [load-failure
                     (try
                       (clojure.lang.RT/loadClassForName clazz)
                       nil
                       (catch Exception e
                         e))]
            (if (seq more)
              (recur more)
              (throw load-failure))))))
    (throw (ex-info error-msg db-spec)))
  (-> (DriverManager/getConnection url (as-properties etc))
      (modify-connection opts)))

(defn get-connection
  "Creates a connection to a database. db-spec is usually a map containing connection
  parameters but can also be a URI or a String.

  The only time you should call this function is when you need a Connection for
  prepare-statement -- no other public functions in clojure.java.jdbc accept a
  raw Connection object: they all expect a db-spec (either a raw db-spec or one
  obtained via with-db-connection or with-db-transaction).

  The correct usage of get-connection for prepare-statement is:

      (with-open [conn (jdbc/get-connection db-spec)]
        ... (jdbc/prepare-statement conn sql-statement options) ...)

  Any connection obtained via calling get-connection directly must be closed
  explicitly (via with-open or a direct call to .close on the Connection object).

  The various possibilities are described below:

  DriverManager (preferred):
    :dbtype      (required) a String, the type of the database (the jdbc subprotocol)
    :dbname      (required) a String, the name of the database
    :classname   (optional) a String, the jdbc driver class name
    :host        (optional) a String, the host name/IP of the database
                            (defaults to 127.0.0.1)
    :port        (optional) a Long, the port of the database
                            (defaults to 3306 for mysql, 1433 for mssql/jtds, else nil)
    (others)     (optional) passed to the driver as properties
                            (may include :user and :password)

  Raw:
    :connection-uri (required) a String
                 Passed directly to DriverManager/getConnection
                 (both :user and :password may be specified as well, rather
                  than passing them as part of the connection string)

  Other formats accepted:

  Existing Connection:
    :connection  (required) an existing open connection that can be used
                 but cannot be closed (only the parent connection can be closed)

  DriverManager (alternative / legacy style):
    :subprotocol (required) a String, the jdbc subprotocol
    :subname     (required) a String, the jdbc subname
    :classname   (optional) a String, the jdbc driver class name
    (others)     (optional) passed to the driver as properties
                            (may include :user and :password)

  Factory:
    :factory     (required) a function of one argument, a map of params
    (others)     (optional) passed to the factory function in a map

  DataSource:
    :datasource  (required) a javax.sql.DataSource
    :username    (optional) a String - deprecated, use :user instead
    :user        (optional) a String - preferred
    :password    (optional) a String, required if :user is supplied

  JNDI:
    :name        (required) a String or javax.naming.Name
    :environment (optional) a java.util.Map

  java.net.URI:
    Parsed JDBC connection string (see java.lang.String format next)

  java.lang.String:
    subprotocol://user:password@host:post/subname
                 An optional prefix of jdbc: is allowed."
  (^java.sql.Connection [db-spec] (get-connection db-spec {}))
  (^java.sql.Connection
    [{:keys [connection
             factory
             connection-uri
             classname subprotocol subname
             dbtype dbname host port
             datasource username password user
             name environment]
      :as db-spec}
     opts]
    (cond
     (string? db-spec)
     (get-connection (URI. (strip-jdbc db-spec)) opts)

     (instance? URI db-spec)
     (get-connection (parse-properties-uri db-spec) opts)

     connection
     connection ;; do not apply opts here

     (or (and datasource username password)  ; legacy
         (and datasource user     password)) ; preferred
     (-> (.getConnection ^DataSource datasource
                         ^String (or username user)
                         ^String password)
         (modify-connection opts))

     datasource
     (-> (.getConnection ^DataSource datasource)
         (modify-connection opts))

     factory
     (-> (factory (dissoc db-spec :factory))
         (modify-connection opts))

     connection-uri
     (-> (if (and user password)
           (DriverManager/getConnection connection-uri user password)
           (DriverManager/getConnection connection-uri))
         (modify-connection opts))

     (and dbtype dbname)
     (let [;; allow aliases for dbtype
           subprotocol (subprotocols dbtype dbtype)
           host (or host "127.0.0.1")
           port (or port (ports subprotocol))
           db-sep (dbname-separators dbtype "/")
           url (cond (= "h2:mem" dbtype)
                     (str "jdbc:" subprotocol ":" dbname ";DB_CLOSE_DELAY=-1")
                     (#{"derby" "h2" "hsqldb" "sqlite"} subprotocol)
                     (str "jdbc:" subprotocol ":" dbname)
                     :else
                     (str "jdbc:" subprotocol ":"
                          (host-prefixes subprotocol "//")
                          host
                          (when port (str ":" port))
                          db-sep dbname))
           etc (dissoc db-spec :dbtype :dbname)]
       (get-driver-connection classname subprotocol db-spec
                              url etc opts
                              (str "Unknown dbtype: " dbtype)))

     (and subprotocol subname)
     (let [;; allow aliases for subprotocols
           subprotocol (subprotocols subprotocol subprotocol)
           url (format "jdbc:%s:%s" subprotocol subname)
           etc (dissoc db-spec :classname :subprotocol :subname)]
       (get-driver-connection classname subprotocol db-spec
                              url etc opts
                              (str "Unknown subprotocol: " subprotocol)))

     name
     (or (when-available javax.naming.InitialContext
           (let [env (and environment (Hashtable. ^Map environment))
                 context (javax.naming.InitialContext. env)
                 ^DataSource datasource (.lookup context ^String name)]
             (-> (.getConnection datasource)
                 (modify-connection opts))))
         (throw (ex-info (str "javax.naming.InitialContext is not available for: "
                              name)
                         db-spec)))

     ;; passing a raw Connection object to a function expecting a db-spec is
     ;; usually a confusion over how/when to use get-connection and deserves
     ;; a custom error message:
     (instance? java.sql.Connection db-spec)
     (let [^String msg (str "db-spec is a raw Connection object!\n"
                            "Did you call get-connection in the wrong context?\n"
                            "You should only call that to pass a Connection into prepare-statement.\n"
                            "(and don't forget to close it via with-open or .close)")]
       (throw (IllegalArgumentException. msg)))

     :else
     (let [^String msg (format "db-spec %s is missing a required parameter" db-spec)]
       (throw (IllegalArgumentException. msg))))))

(defn- make-cols-unique
  "A transducer that, given a collection of strings, returns a collection of
  strings that have been made unique by appending _n to duplicates."
  [xf]
  (let [seen (volatile! {})]
    (fn
      ([] (xf))
      ([result] (xf result))
      ([result input]
       (if-let [suffix (get @seen input)]
         (do
           (vswap! seen assoc input (inc suffix))
           (xf result (str input "_" suffix)))
         (do
           (vswap! seen assoc input 2)
           (xf result input)))))))

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
  (loop [ix 1 values params]
    (when (seq values)
      (set-parameter (first values) stmt ix)
      (recur (inc ix) (rest values)))))

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

(defn- make-identifier-fn
  "Given the user's identifiers function, an optional namespace qualifier, and
  a flag indicating whether to produce keywords or not, return a compound
  function that will perform the appropriate entity to identifier conversion."
  [identifiers qualifier keywordize?]
  (cond (and qualifier (not keywordize?))
        (throw (IllegalArgumentException.
                (str ":qualifier is not allowed unless "
                     ":keywordize? is true")))
        (and qualifier keywordize?)
        (comp (partial keyword qualifier) identifiers)
        keywordize?
        (comp keyword identifiers)
        :else
        identifiers))

(defn result-set-seq
  "Creates and returns a lazy sequence of maps corresponding to the rows in the
  java.sql.ResultSet rs. Loosely based on clojure.core/resultset-seq but it
  respects the specified naming strategy. Duplicate column names are made unique
  by appending _N before applying the naming strategy (where N is a unique integer),
  unless the :as-arrays? option is :cols-as-is, in which case the column names
  are untouched (the result set maintains column name/value order).
  The :identifiers option specifies how SQL column names are converted to Clojure
  keywords. The default is to convert them to lower case.
  The :keywordize? option can be specified as false to opt-out of the conversion
  to keywords.
  The :qualifier option specifies the namespace qualifier for those identifiers
  (and this may not be specified when :keywordize? is false)."
  ([rs] (result-set-seq rs {}))
  ([^ResultSet rs {:keys [as-arrays? identifiers keywordize?
                          qualifier read-columns]
                   :or {identifiers str/lower-case
                        keywordize? true
                        read-columns dft-read-columns}}]
   (let [rsmeta (.getMetaData rs)
         idxs (range 1 (inc (.getColumnCount rsmeta)))
         col-name-fn (if (= :cols-as-is as-arrays?) identity make-cols-unique)
         keys (into [] (comp (map (fn [^Integer i] (.getColumnLabel rsmeta i)))
                             col-name-fn
                             (map (make-identifier-fn identifiers
                                                      qualifier
                                                      keywordize?)))
                    idxs)
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
     :cursors     :hold | :close
     :fetch-size  n
     :max-rows    n
     :timeout     n
  Note that :result-type and :concurrency must be specified together as the
  underlying Java API expects both (or neither)."
  ([con sql] (prepare-statement con sql {}))
  ([^java.sql.Connection con ^String sql
    {:keys [return-keys result-type concurrency cursors
            fetch-size max-rows timeout]}]
   (let [^PreparedStatement
         stmt (cond
                return-keys
                (do
                  (when (or result-type concurrency cursors)
                    (throw (IllegalArgumentException.
                            (str ":concurrency, :cursors, and :result-type "
                                 "may not be specified with :return-keys."))))
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
                      (.prepareStatement con sql))))

                (and result-type concurrency)
                (if cursors
                  (.prepareStatement con sql
                                     (get result-set-type result-type result-type)
                                     (get result-set-concurrency concurrency concurrency)
                                     (get result-set-holdability cursors cursors))
                  (.prepareStatement con sql
                                     (get result-set-type result-type result-type)
                                     (get result-set-concurrency concurrency concurrency)))

                (or result-type concurrency cursors)
                (throw (IllegalArgumentException.
                        (str ":concurrency, :cursors, and :result-type "
                             "may not be specified independently.")))
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
                (try
                  (.rollback con)
                  (catch Throwable rb
                    ;; combine both exceptions
                    (throw (ex-info (str "Rollback failed handling \""
                                         (.getMessage t)
                                         "\"")
                                    {:rollback rb
                                     :handling t}))))
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
         ;; avoid confusion of read-only? TX and read-only? connection:
         (with-open [con (get-connection db (dissoc opts :read-only?))]
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
  (with-db-connection [con-db db-spec opts]
    ... con-db ...)"
  [binding & body]
  `(let [db-spec# ~(second binding) opts# ~(or (second (rest binding)) {})]
     (if (db-find-connection db-spec#)
       (let [~(first binding) db-spec#]
         ~@body)
       (with-open [con# (get-connection db-spec# opts#)]
         (let [~(first binding) (add-connection db-spec# con#)]
           ~@body)))))

(defmacro with-db-metadata
  "Evaluates body in the context of an active connection with metadata bound
   to the specified name. See also metadata-result for dealing with the results
   of operations that retrieve information from the metadata.
   (with-db-metadata [md db-spec opts]
     ... md ...)"
  [binding & body]
  `(let [db-spec# ~(second binding) opts# ~(or (second (rest binding)) {})]
     (if-let [con# (db-find-connection db-spec#)]
       (let [~(first binding) (.getMetaData con#)]
         ~@body)
       (with-open [con# (get-connection db-spec# opts#)]
         (let [~(first binding) (.getMetaData con#)]
           ~@body)))))

(defn- process-result-set
  "Given a Java ResultSet and options, produce a processed result-set-seq,
  honoring as-arrays?, result-set-fn, and row-fn from opts."
  [rset opts]
  (let [{:keys [as-arrays? result-set-fn row-fn]}
        (merge {:row-fn identity} opts)
        result-set-fn (or result-set-fn (if as-arrays? vec doall))]
    (if as-arrays?
      ((^:once fn* [rs]
         (result-set-fn (cons (first rs)
                              (map row-fn (rest rs)))))
       (result-set-seq rset opts))
      (result-set-fn (map row-fn (result-set-seq rset opts))))))

(defn metadata-result
  "If the argument is a java.sql.ResultSet, turn it into a result-set-seq,
  else return it as-is. This makes working with metadata easier.
  Also accepts an option map containing :identifiers, :keywordize?, :qualifier,
  :as-arrays?, :row-fn, and :result-set-fn to control how the ResultSet is
  transformed and returned. See query for more details."
  ([rs-or-value] (metadata-result rs-or-value {}))
  ([rs-or-value opts]
   (if (instance? java.sql.ResultSet rs-or-value)
     (process-result-set rs-or-value opts)
     rs-or-value)))

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
  in a transaction if transaction? is true. transaction? can be omitted and it
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
  return any generated keys.

  Supports :multi? which causes a full result set sequence of keys to be
  returned, and assumes the param-group is a sequence of parameter lists,
  rather than a single sequence of parameters.

  Also supports :row-fn and, if :multi? is truthy, :result-set-fn"
  [db ^PreparedStatement stmt param-group opts]
  (let [{:keys [as-arrays? multi? row-fn transaction?] :as opts}
        (merge {:row-fn identity} (when (map? db) db) opts)
        exec-and-return-keys
        (^{:once true} fn* []
         (let [counts (if multi?
                        (.executeBatch stmt)
                        (.executeUpdate stmt))]
           (try
             (let [rs (.getGeneratedKeys stmt)
                   result (cond multi?
                                (process-result-set rs opts)
                                as-arrays?
                                ((^:once fn* [rs]
                                   (list (first rs)
                                         (row-fn (second rs))))
                                 (result-set-seq rs opts))
                                :else
                                (row-fn (first (result-set-seq rs opts))))]
               ;; sqlite (and maybe others?) requires
               ;; record set to be closed
               (.close rs)
               result)
             (catch Exception _
               ;; assume generated keys is unsupported and return counts instead:
               (let [result-set-fn (or (:result-set-fn opts) doall)]
                 (result-set-fn (map row-fn counts)))))))]
    (if multi?
      (doseq [params param-group]
        ((:set-parameters opts dft-set-parameters) stmt params)
        (.addBatch stmt))
      ((:set-parameters opts dft-set-parameters) stmt param-group))
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
  the parameters. transaction? can be omitted and will default to true.
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
   (let [opts (merge (when (map? db) db) opts)
         return-keys (or (:return-keys opts) true)]
     (if-let [con (db-find-connection db)]
       (let [[sql & params] (if (sql-stmt? sql-params) (vector sql-params) (vec sql-params))]
         (if (instance? PreparedStatement sql)
           (db-do-execute-prepared-return-keys db sql params (assoc opts :transaction? transaction?))
           (with-open [^PreparedStatement stmt (prepare-statement
                                                 con sql
                                                 (assoc opts :return-keys return-keys))]
             (db-do-execute-prepared-return-keys db stmt params (assoc opts :transaction? transaction?)))))
       (with-open [con (get-connection db opts)]
         (db-do-prepared-return-keys (add-connection db con) transaction? sql-params opts))))))

(defn- db-do-execute-prepared-statement
  "Execute a PreparedStatement, optionally in a transaction."
  [db ^PreparedStatement stmt param-groups opts]
  (let [{:keys [transaction?] :as opts} (merge (when (map? db) db) opts)]
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
       (with-open [con (get-connection db opts)]
         (db-do-prepared (add-connection db con) transaction? sql-params opts))))))

(defn- execute-query-with-params
  "Given a prepared statement, a set of parameters, a parameter setting
  function, and a function to process the result set, execute the query and
  apply the processing function."
  [^PreparedStatement stmt params set-parameters func]
  (set-parameters stmt params)
  (with-open [rset (.executeQuery stmt)]
    (func rset)))

(defn- db-query-with-resultset*
  "Given a db-spec, a SQL statement (or a prepared statement), a set of
  parameters, a result set processing function and options, execute the query."
  [db sql params func opts]
  (if (instance? PreparedStatement sql)
    (let [^PreparedStatement stmt sql]
      (execute-query-with-params
        stmt
        params
        (:set-parameters opts dft-set-parameters)
        func))
    (if-let [con (db-find-connection db)]
      (with-open [^PreparedStatement stmt (prepare-statement con sql opts)]
        (execute-query-with-params
          stmt
          params
          (:set-parameters opts dft-set-parameters)
          func))
      (with-open [con (get-connection db opts)]
        (with-open [^PreparedStatement stmt (prepare-statement con sql opts)]
          (execute-query-with-params
            stmt
            params
            (:set-parameters opts dft-set-parameters)
            func))))))

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
         [sql & params] (if (sql-stmt? sql-params) (vector sql-params) (vec sql-params))]
     (when-not (sql-stmt? sql)
       (let [^Class sql-class (class sql)
             ^String msg (format "\"%s\" expected %s %s, found %s %s"
                                 "sql-params"
                                 "vector"
                                 "[sql param*]"
                                 (.getName sql-class)
                                 (pr-str sql))]
         (throw (IllegalArgumentException. msg))))
     (db-query-with-resultset* db sql params func opts))))

;; top-level API for actual SQL operations

(defn query
  "Given a database connection and a vector containing SQL and optional parameters,
  perform a simple database query. The options specify how to construct the result
  set (and are also passed to prepare-statement as needed):
    :as-arrays? - return the results as a set of arrays, default false.
    :identifiers - applied to each column name in the result set, default lower-case
    :keywordize? - defaults to true, can be false to opt-out of converting
        identifiers to keywords
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
   (let [{:keys [explain? explain-fn] :as opts}
         (merge {:explain-fn println} (when (map? db) db) opts)
         [sql & params] (if (sql-stmt? sql-params) (vector sql-params) (vec sql-params))]
     (when-not (sql-stmt? sql)
       (let [^Class sql-class (class sql)
             ^String msg (format "\"%s\" expected %s %s, found %s %s"
                                 "sql-params"
                                 "vector"
                                 "[sql param*]"
                                 (.getName sql-class)
                                 (pr-str sql))]
         (throw (IllegalArgumentException. msg))))
     (when (and explain? (string? sql))
       (query db (into [(str (if (string? explain?) explain? "EXPLAIN")
                             " "
                             sql)]
                       params)
              (-> opts
                  (dissoc :explain? :result-set-fn :row-fn)
                  (assoc :result-set-fn explain-fn))))
     (db-query-with-resultset* db sql params
                               (^:once fn* [rset]
                                 (process-result-set rset opts))
                              opts))))

(defn- get-rs-columns
  "Given a set of indices, a result set's metadata, and a function to convert
  SQL entity names to Clojure column names,
  return the unique vector of column names."
  [idxs ^ResultSetMetaData rsmeta identifier-fn]
  (into [] (comp (map (fn [^Integer i] (.getColumnLabel rsmeta i)))
                 make-cols-unique
                 (map identifier-fn))
        idxs))

(defn- init-reduce-rs
  "Given a sequence of columns, a result set, its metadata, a sequence of
  indices, a mapping function to apply, an initial value, and a function that
  can read column data from the result set, reduce the result set and
  return the result of that reduction."
  [cols ^ResultSet rs rsmeta idxs f init read-columns]
  (loop [init' init]
    (if (.next rs)
      (let [result (f init' (zipmap cols (read-columns rs rsmeta idxs)))]
        (if (reduced? result)
          @result
          (recur result)))
      init')))

(defn- reducible-result-set*
  "Given a java.sql.ResultSet, indices, metadata, column names and a reader,
  return a reducible collection.
  Compiled with Clojure 1.7 or later -- uses clojure.lang.IReduce."
  [^ResultSet rs idxs ^ResultSetMetaData rsmeta cols read-columns]
  (reify clojure.lang.IReduce
    (reduce [this f]
            (if (.next rs)
              ;; reduce init is first row of ResultSet
              (init-reduce-rs cols rs rsmeta idxs f
                              (zipmap cols (read-columns rs rsmeta idxs))
                              read-columns)
              ;; no rows so call 0-arity f to get result value
              ;; per reduce docstring contract
              (f)))
    (reduce [this f init]
            (init-reduce-rs cols rs rsmeta idxs f init read-columns))))

(defn reducible-result-set
  "Given a java.sql.ResultSet return a reducible collection.
  Compiled with Clojure 1.7 or later -- uses clojure.lang.IReduce
  Note: :as-arrays? is not accepted here."
  [^ResultSet rs {:keys [identifiers keywordize? qualifier read-columns]
                  :or {identifiers str/lower-case
                       keywordize? true
                       read-columns dft-read-columns}}]
  (let [rsmeta (.getMetaData rs)
        idxs (range 1 (inc (.getColumnCount rsmeta)))
        cols (get-rs-columns idxs rsmeta
                             (make-identifier-fn identifiers
                                                 qualifier
                                                 keywordize?))]
    (reducible-result-set* rs idxs rsmeta cols read-columns)))

(defn- query-reducer
  "Given options, return a function of f (or f and init) that accepts a
  result set and reduces it using f."
  [identifiers keywordize? qualifier read-columns]
  (let [identifier-fn (make-identifier-fn identifiers qualifier keywordize?)]
    (fn
      ([f]
       (^{:once true} fn* [^ResultSet rs]
         (let [rsmeta (.getMetaData rs)
               idxs (range 1 (inc (.getColumnCount rsmeta)))
               cols (get-rs-columns idxs rsmeta identifier-fn)]
           (reduce f (reducible-result-set* rs idxs rsmeta cols read-columns)))))
      ([f init]
       (^{:once true} fn* [^ResultSet rs]
         (let [rsmeta (.getMetaData rs)
               idxs (range 1 (inc (.getColumnCount rsmeta)))
               cols (get-rs-columns idxs rsmeta identifier-fn)]
           (reduce f init (reducible-result-set* rs idxs rsmeta cols read-columns))))))))

(defn- mapify-result-set
  "Given a result set, return an object that wraps the current row as a hash
  map. Note that a result set is mutable and the current row will change behind
  this wrapper so operations need to be eager (and fairly limited).
  Supports ILookup (keywords are treated as strings).
  Supports Associative for lookup only (again, keywords are treated as strings).
  Later we may realize a new hash map when assoc (and other, future, operations
  are performed on the result set row)."
  [^ResultSet rs]
  (reify
    clojure.lang.ILookup
    (valAt [this k]
           (try
             (.getObject rs (name k))
             (catch SQLException _)))
    (valAt [this k not-found]
           (try
             (.getObject rs (name k))
             (catch SQLException _
               not-found)))
    clojure.lang.Associative
    (containsKey [this k]
                 (try
                   (.getObject rs (name k))
                   true
                   (catch SQLException _
                     false)))
    (entryAt [this k]
             (try
               (clojure.lang.MapEntry. k (.getObject rs (name k)))
               (catch SQLException _)))
    (assoc [this _ _]
           (throw (ex-info "assoc not supported on raw result set" {})))))

(defn- raw-query-reducer
  "Given a function f and an initial value, return a function that accepts a
  result set and reduces it using no translation. The result set is extended
  to support ILookup and the readonly parts of Associative only."
  [f init]
  (^{:once true} fn* [^ResultSet rs]
    (let [rs-map (mapify-result-set rs)]
      (loop [init' init]
        (if (.next rs)
          (let [result (f init' rs-map)]
            (if (reduced? result)
              @result
              (recur result)))
          init')))))

(defn reducible-query
  "Given a database connection, a vector containing SQL and optional parameters,
  return a reducible collection. When reduced, it will start the database query
  and reduce the result set, and then close the connection:
    (transduce (map :cost) + (reducible-query db sql-params))

  The following options from query etc are not accepted here:
    :as-arrays? :explain :explain-fn :result-set-fn :row-fn
  See prepare-statement for additional options that may be passed through.

  If :raw? true is specified, the rows of the result set are not converted to
  hash maps, and it as if the following options were specified:
    :identifiers identity :keywordize? false :qualifier nil
  In addition, the rows of the result set may only be read as if they were hash
  maps (get, keyword lookup, select-keys) but the sequence representation is
  not available (so, no keys, no vals, and no seq calls). This is much faster
  than converting each row to a hash map but it is also more restrictive."
  ([db sql-params] (reducible-query db sql-params {}))
  ([db sql-params opts]
   (let [{:keys [identifiers keywordize? qualifier read-columns] :as opts}
         (merge {:identifiers str/lower-case :keywordize? true
                 :read-columns dft-read-columns}
                (when (map? db) db)
                opts)
         [sql & params] (if (sql-stmt? sql-params) (vector sql-params) (vec sql-params))
         reducing-fn (if (:raw? opts)
                       raw-query-reducer
                       (query-reducer identifiers keywordize? qualifier read-columns))]
     (when-not (sql-stmt? sql)
       (let [^Class sql-class (class sql)
             ^String msg (format "\"%s\" expected %s %s, found %s %s"
                                 "sql-params"
                                 "vector"
                                 "[sql param*]"
                                 (.getName sql-class)
                                 (pr-str sql))]
         (throw (IllegalArgumentException. msg))))
     (reify clojure.lang.IReduce
       (reduce [this f]
               (db-query-with-resultset*
                 db sql params
                 (reducing-fn f)
                 opts))
       (reduce [this f init]
               (db-query-with-resultset*
                 db sql params
                 (reducing-fn f init)
                 opts))))))

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

  An :order-by option may be supplied to sort the rows, e.g.,

      {:order-by [{:name :asc} {:age :desc} {:income :asc}]}
      ;; equivalent to:
      {:order-by [:name {:age :desc} :income]}

  The :order-by value is a sequence of column names (to sort in ascending
  order) and/or maps from column names to directions (:asc or :desc). The
  directions may be strings or keywords and are not case-sensitive. They
  are mapped to ASC or DESC in the generated SQL.

  Note: if a ordering map has more than one key, the order of the columns
  in the generated SQL ORDER BY clause is unspecified (so such maps should
  only contain one key/value pair)."
  ([db table columns] (find-by-keys db table columns {}))
  ([db table columns opts]
   (let [{:keys [entities order-by] :as opts}
         (merge {:entities identity} (when (map? db) db) opts)
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

  If :return-keys is provided, db-do-prepared-return-keys will be called
  instead of db-do-prepared, and the result will be a sequence of maps
  containing the generated keys. If present, :row-fn will be applied. If :multi?
  then :result-set-fn will also be applied if present. :as-arrays? may also be
  specified (which will affect what :result-set-fn is passed).

  If there are no parameters specified, executeUpdate will be used, otherwise
  executeBatch will be used. This may affect what SQL you can run via execute!"
  ([db sql-params] (execute! db sql-params {}))
  ([db sql-params opts]
   (let [{:keys [transaction? return-keys] :as opts}
         (merge {:transaction? true :multi? false} (when (map? db) db) opts)
         db-do-helper (if return-keys
                        db-do-prepared-return-keys
                        db-do-prepared)
         execute-helper (^{:once true} fn* [db]
                         (db-do-helper db transaction? sql-params opts))]
     (if (db-find-connection db)
       (execute-helper db)
       (with-open [con (get-connection db opts)]
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
   (let [{:keys [entities] :as opts}
         (merge {:entities identity :transaction? true} (when (map? db) db) opts)]
     (execute! db (delete-sql table where-clause entities) opts))))

(defn- multi-insert-helper
  "Given a (connected) database connection and some SQL statements (for multiple
   inserts), run a prepared statement on each and return any generated keys.
   Note: we are eager so an unrealized lazy-seq cannot escape from the connection."
  [db stmts opts]
  (let [{:keys [as-arrays? result-set-fn]} (merge (when (map? db) db) opts)
        per-statement (fn [stmt]
                        (db-do-prepared-return-keys db false stmt opts))]
    (if as-arrays?
      (let [rs (map per-statement stmts)]
        (cond (apply = (map first rs))
              ;; all the columns are the same, rearrange to cols + rows format
              ((or result-set-fn vec)
               (cons (ffirst rs)
                     (map second rs)))
              result-set-fn
              (throw (ex-info (str "Cannot apply result-set-fn to"
                                   " non-homogeneous generated keys array") rs))
              :else
              ;; non-non-homogeneous generated keys array - return as-is
              rs))
      (if result-set-fn
        (result-set-fn (map per-statement stmts))
        (seq (mapv per-statement stmts))))))

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
  (let [{:keys [entities transaction?] :as opts}
        (merge {:entities identity :identifiers str/lower-case
                :keywordize? true :transaction? true}
               (when (map? db) db)
               opts)
        sql-params (map (fn [row]
                          (when-not (map? row)
                            (throw (IllegalArgumentException. "insert! / insert-multi! called with a non-map row")))
                          (insert-single-row-sql table row entities)) rows)]
    (if (db-find-connection db)
      (insert-helper db transaction? sql-params opts)
      (with-open [con (get-connection db opts)]
        (insert-helper (add-connection db con) transaction? sql-params opts)))))

(defn- insert-cols!
  "Given a database connection, a table name, a sequence of columns names, a
  sequence of vectors of column values, one per row, and an options map,
  insert the rows into the database."
  [db table cols values opts]
  (let [{:keys [entities transaction?] :as opts}
        (merge {:entities identity :transaction? true} (when (map? db) db) opts)
        sql-params (insert-multi-row-sql table cols values entities)]
    (if (db-find-connection db)
      (db-do-prepared db transaction? sql-params (assoc opts :multi? true))
      (with-open [con (get-connection db opts)]
        (db-do-prepared (add-connection db con) transaction? sql-params
                        (assoc opts :multi? true))))))

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
  generated keys, if available (note: PostgreSQL returns the whole rows). A
  separate database operation is used for each row inserted. This may be slow
  for if a large sequence of maps is provided.

  When inserting rows as a sequence of lists of column values, the result is
  a sequence of the counts of rows affected (a sequence of 1's), if available.
  Yes, that is singularly unhelpful. Thank you getUpdateCount and executeBatch!
  A single database operation is used to insert all the rows at once. This may
  be much faster than inserting a sequence of rows (which performs an insert for
  each map in the sequence).

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
   (let [{:keys [entities] :as opts}
         (merge {:entities identity :transaction? true} (when (map? db) db) opts)]
     (execute! db (update-sql table set-map where-clause entities) opts))))

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
  :entities -- a function to specify how column names are transformed.
  :conditional? -- either a boolean, indicating whether to add 'IF NOT EXISTS',
    or a string, which is inserted literally before the table name, or a
    function of two arguments (table name and the create statement), that can
    manipulate the generated statement to better support other databases, e.g.,
    MS SQL Server which need to wrap create table in an existence query."
  ([table specs] (create-table-ddl table specs {}))
  ([table specs opts]
   (let [table-spec     (:table-spec opts)
         conditional?   (:conditional? opts)
         entities       (:entities   opts identity)
         table-name     (as-sql-name entities table)
         table-spec-str (or (and table-spec (str " " table-spec)) "")
         spec-to-string (fn [spec]
                          (try
                            (str/join " " (cons (as-sql-name entities (first spec))
                                                (map name (rest spec))))
                            (catch Exception _
                              (throw (IllegalArgumentException.
                                      "column spec is not a sequence of keywords / strings")))))]
     (cond->> (format "CREATE TABLE%s %s (%s)%s"
                      (cond (or (nil? conditional?)
                                (instance? Boolean conditional?))
                            (if conditional? " IF NOT EXISTS" "")
                            (fn? conditional?)
                            ""
                            :else
                            (str " " conditional?))
                      table-name
                      (str/join ", " (map spec-to-string specs))
                      table-spec-str)
       (fn? conditional?) (conditional? table-name)))))

(defn drop-table-ddl
  "Given a table name, return the DDL string for dropping that table.
  An options map may be provided that can contain:
  :entities -- a function to specify how column names are transformed.
  :conditional? -- either a boolean, indicating whether to add 'IF EXISTS',
    or a string, which is inserted literally before the table name, or a
    function of two arguments (table name and the create statement), that can
    manipulate the generated statement to better support other databases, e.g.,
    MS SQL Server which need to wrap create table in an existence query."
  ([table] (drop-table-ddl table {}))
  ([table {:keys [entities conditional?] :or {entities identity}}]
   (let [table-name (as-sql-name entities table)]
     (cond->> (format "DROP TABLE%s %s"
                      (cond (or (nil? conditional?)
                                (instance? Boolean conditional?))
                            (if conditional? " IF EXISTS" "")
                            (fn? conditional?)
                            ""
                            :else
                            (str " " conditional?))
                      table-name)
       (fn? conditional?) (conditional? table-name)))))
