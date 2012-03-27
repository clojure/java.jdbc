;;  Copyright (c) Stephen C. Gilardi. All rights reserved.  The use and
;;  distribution terms for this software are covered by the Eclipse Public
;;  License 1.0 (http://opensource.org/licenses/eclipse-1.0.php) which can
;;  be found in the file epl-v10.html at the root of this distribution.  By
;;  using this software in any fashion, you are agreeing to be bound by the
;;  terms of this license.  You must not remove this notice, or any other,
;;  from this software.
;;
;;  internal definitions for clojure.java.jdbc
;;
;;  scgilardi (gmail)
;;  Created 3 October 2008
;;
;;  seancorfield (gmail)
;;  Migrated from clojure.contrib.sql.internal 17 April 2011

(ns clojure.java.jdbc.internal
  (:require [clojure.string :as str])
  (:import
    (clojure.lang RT)
    (java.net URI)
    (java.sql BatchUpdateException Connection DriverManager PreparedStatement ResultSet SQLException Statement)
    (java.util Hashtable Map Properties)
    (javax.naming InitialContext Name)
    (javax.sql DataSource)))

(def ^{:dynamic true} *db* {:connection nil :level 0})

(def ^{:private true :doc "Map of classnames to subprotocols"} classnames
  {"postgresql"     "org.postgresql.Driver"
   "mysql"          "com.mysql.jdbc.Driver"
   "sqlserver"      "com.microsoft.sqlserver.jdbc.SQLServerDriver"
   "jtds:sqlserver" "net.sourceforge.jtds.jdbc.Driver"
   "derby"          "org.apache.derby.jdbc.EmbeddedDriver"
   "hsqldb"         "org.hsqldb.jdbcDriver"
   "sqlite"         "org.sqlite.JDBC"})

(def ^{:private true :doc "Map of schemes to subprotocols"} subprotocols
  {"postgres" "postgresql"})

(def special-counts
  {Statement/EXECUTE_FAILED "EXECUTE_FAILED"
   Statement/SUCCESS_NO_INFO "SUCCESS_NO_INFO"})

(defn find-connection*
  "Returns the current database connection (or nil if there is none)"
  []
  (:connection *db*))

(defn connection*
  "Returns the current database connection (or throws if there is none)"
  ^Connection []
  (or (find-connection*)
      (throw (Exception. "no current database connection"))))

(defn rollback
  "Accessor for the rollback flag on the current connection"
  ([]
    (deref (:rollback *db*)))
  ([val]
    (swap! (:rollback *db*) (fn [_] val))))

(def ^{:dynamic true
       :doc "The default entity naming strategy is to do nothing."}
  *as-str* 
  identity)

(def ^{:dynamic true
       :doc "The default keyword naming strategy is to lowercase the entity."}
  *as-key*
  clojure.string/lower-case)

(defn as-str
  "Given a naming strategy and a keyword, return the keyword as a
   string per that naming strategy. Given (a naming strategy and)
   a string, return it as-is."
  [f x]
  (if (instance? clojure.lang.Named x)
    (f (name x))
    (str x)))

(defn as-key
  "Given a naming strategy and a string, return the string as a
   keyword per that naming strategy. Given (a naming strategy and)
   a keyword, return it as-is."
  [f x]
  (if (instance? clojure.lang.Named x)
    x
    (keyword (f (str x)))))

(defn as-identifier*
  "Given a keyword, convert it to a string using the current naming
   strategy.
   Given a string, return it as-is."
  ([x] (as-identifier* x *as-str*))
  ([x f-entity] (as-str f-entity x)))

(defn as-keyword*
  "Given an entity name (string), convert it to a keyword using the
   current naming strategy.
   Given a keyword, return it as-is."
  ([x] (as-keyword* x *as-key*))
  ([x f-keyword] (as-key f-keyword x)))

(defn- ^Properties as-properties
  "Convert any seq of pairs to a java.utils.Properties instance.
   Uses as-str to convert both keys and values into strings."
  { :tag Properties }
  [m]
  (let [p (Properties.)]
    (doseq [[k v] m]
      (.setProperty p (as-str identity k) (as-str identity v)))
    p))

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

(defn get-connection
  "Creates a connection to a database. db-spec is a map containing values
  for one of the following parameter sets:

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
  [{:keys [factory
           classname subprotocol subname
           datasource username password
           name environment]
    :as db-spec}]
  (cond
    (instance? URI db-spec)
    (get-connection (parse-properties-uri db-spec))
    (string? db-spec)
    (get-connection (URI. db-spec))
    factory
    (factory (dissoc db-spec :factory))
    (and subprotocol subname)
    (let [url (format "jdbc:%s:%s" subprotocol subname)
          etc (dissoc db-spec :classname :subprotocol :subname)
          classname (or classname (classnames subprotocol))]
      (RT/loadClassForName classname)
      (DriverManager/getConnection url (as-properties etc)))
    (and datasource username password)
    (.getConnection ^DataSource datasource ^String username ^String password)
    datasource
    (.getConnection ^DataSource datasource)
    name
    (let [env (and environment (Hashtable. ^Map environment))
          context (InitialContext. env)
          ^DataSource datasource (.lookup context ^String name)]
      (.getConnection datasource))
    :else
    (let [^String msg (format "db-spec %s is missing a required parameter" db-spec)]
      (throw (IllegalArgumentException. msg)))))

(defn with-connection*
  "Evaluates func in the context of a new connection to a database then
  closes the connection."
  [db-spec func]
  (with-open [^Connection con (get-connection db-spec)]
    (binding [*db* (assoc *db* :connection con :level 0 :rollback (atom false))]
      (func))))

(defn transaction*
  "Evaluates func as a transaction on the open database connection. Any
  nested transactions are absorbed into the outermost transaction. By
  default, all database updates are committed together as a group after
  evaluating the outermost body, or rolled back on any uncaught
  exception. If rollback is set within scope of the outermost transaction,
  the entire transaction will be rolled back rather than committed when
  complete."
  [func]
  (binding [*db* (update-in *db* [:level] inc)]
    (if (= (:level *db*) 1)
      (let [^Connection con (connection*)
            auto-commit (.getAutoCommit con)]
        (io!
          (.setAutoCommit con false)
          (try
            (let [result (func)]
              (if (rollback)
                (.rollback con)
                (.commit con))
              result)
            (catch Exception e
              (.rollback con)
              ;; This ugliness makes it easier to catch SQLException objects
              ;; rather than something wrapped in a RuntimeException which
              ;; can really obscure your code when working with JDBC from
              ;; Clojure... :(
              (letfn [(throw-non-rte [^Throwable ex]
                        (cond (instance? java.sql.SQLException ex) (throw ex)
                              (and (instance? RuntimeException ex) (.getCause ex)) (throw-non-rte (.getCause ex))
                              :else (throw ex)))]
                     (throw-non-rte e)))
            (finally
              (rollback false)
              (.setAutoCommit con auto-commit)))))
      (func))))

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
  (if (apply distinct? cols)
    cols
    (loop [[col-name :as new-cols] (seq cols)
           unique-cols []]
      (if (seq new-cols)
        (recur (rest new-cols) (conj unique-cols (make-name-unique unique-cols col-name 1)))
        unique-cols))))

(defn resultset-seq*
  "Creates and returns a lazy sequence of maps corresponding to
   the rows in the java.sql.ResultSet rs. Based on clojure.core/resultset-seq
   but it respects the current naming strategy. Duplicate column names are
   made unique by appending _N before applying the naming strategy (where
   N is a unique integer)."
  [^ResultSet rs]
    (let [rsmeta (.getMetaData rs)
          idxs (range 1 (inc (.getColumnCount rsmeta)))
          keys (->> idxs
                 (map (fn [^Integer i] (.getColumnLabel rsmeta i)))
                 make-cols-unique
                 (map (comp keyword *as-key*)))
          row-values (fn [] (map (fn [^Integer i] (.getObject rs i)) idxs))
          ;; This used to use create-struct (on keys) and then struct to populate each row.
          ;; That had the side effect of preserving the order of columns in each row. As
          ;; part of JDBC-15, this was changed because structmaps are deprecated. We don't
          ;; want to switch to records so we're using regular maps instead. We no longer
          ;; guarantee column order in rows but using into {} should preserve order for up
          ;; to 16 columns (because it will use a PersistentArrayMap). If someone is relying
          ;; on the order-preserving behavior of structmaps, we can reconsider...
          rows (fn thisfn []
                 (when (.next rs)
                   (cons (into {} (map vector keys (row-values))) (lazy-seq (thisfn)))))]
      (rows)))

(defn- set-parameters
  "Add the parameters to the given statement."
  [^PreparedStatement stmt params]
  (dorun
    (map-indexed
      (fn [ix value]
        (.setObject stmt (inc ix) value))
      params)))

(def ^{:private true
       :doc "Map friendly :type values to ResultSet constants."} 
  result-set-type
  {:forward-only ResultSet/TYPE_FORWARD_ONLY
   :scroll-insensitive ResultSet/TYPE_SCROLL_INSENSITIVE
   :scroll-sensitive ResultSet/TYPE_SCROLL_SENSITIVE})

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

(defn prepare-statement*
  "Create a prepared statement from a connection, a SQL string and an
   optional list of parameters:
     :return-keys true | false - default false
     :result-type :forward-only | :scroll-insensitive | :scroll-sensitive
     :concurrency :read-only | :updatable
     :fetch-size n
     :max-rows n"
  [^Connection con ^String sql & {:keys [return-keys result-type concurrency cursors fetch-size max-rows]}]
  (let [^PreparedStatement stmt (cond
                                  return-keys (try
                                                (.prepareStatement con sql java.sql.Statement/RETURN_GENERATED_KEYS)
                                                (catch Exception _
                                                  ;; assume it is unsupported and try basic PreparedStatement:
                                                  (.prepareStatement con sql)))
                                  (and result-type concurrency) (if cursors
                                                                  (.prepareStatement con sql 
                                                                                     (result-type result-set-type)
                                                                                     (concurrency result-set-concurrency)
                                                                                     (cursors result-set-holdability))
                                                                  (.prepareStatement con sql 
                                                                                     (result-type result-set-type)
                                                                                     (concurrency result-set-concurrency)))
                                  :else (.prepareStatement con sql))]
    (when fetch-size (.setFetchSize stmt fetch-size))
    (when max-rows (.setMaxRows stmt max-rows))
    stmt))

(defn do-prepared-return-keys*
  "Executes an (optionally parameterized) SQL prepared statement on the
  open database connection. The param-group is a seq of values for all of
  the parameters.
  Return the generated keys for the (single) update/insert."
  [sql param-group]
  (with-open [^PreparedStatement stmt (prepare-statement* (connection*) sql :return-keys true)]
    (set-parameters stmt param-group)
    (transaction* (fn [] (let [counts (.executeUpdate stmt)]
                          (try
                            (let [rs (.getGeneratedKeys stmt)
                                  result (first (resultset-seq* rs))]
                              ;; sqlite (and maybe others?) requires
                              ;; record set to be closed
                              (.close rs)
                              result)
                            (catch Exception _
                              ;; assume generated keys is unsupported and return counts instead: 
                              counts)))))))

(defn do-prepared*
  "Executes an (optionally parameterized) SQL prepared statement on the
  open database connection. Each param-group is a seq of values for all of
  the parameters.
  Return a seq of update counts (one count for each param-group)."
  [sql & param-groups]
  (with-open [^PreparedStatement stmt (prepare-statement* (connection*) sql)]
    (if (empty? param-groups)
      (transaction* (fn [] (vector (.executeUpdate stmt))))
      (do
        (doseq [param-group param-groups]
          (set-parameters stmt param-group)
          (.addBatch stmt))
        (transaction* (fn [] (seq (.executeBatch stmt))))))))

(defn with-query-results*
  "Executes a query, then evaluates func passing in a seq of the results as
  an argument. The first argument is a vector containing either:
    [sql & params] - a SQL query, followed by any parameters it needs
    [stmt & params] - a PreparedStatement, followed by any parameters it needs
                      (the PreparedStatement already contains the SQL query)
    [options sql & params] - options and a SQL query for creating a
                      PreparedStatement, follwed by any parameters it needs
  See prepare-statement* for supported options."
  [sql-params func]
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
        prepare-args (when (map? special) (flatten (seq special)))]
    (with-open [^PreparedStatement stmt (if (instance? PreparedStatement special) special (apply prepare-statement* (connection*) sql prepare-args))]
      (set-parameters stmt params)
      (with-open [rset (.executeQuery stmt)]
        (func (resultset-seq* rset))))))
