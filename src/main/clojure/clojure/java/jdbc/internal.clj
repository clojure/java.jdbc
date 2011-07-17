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
  (:require clojure.string)
  (:import
    (clojure.lang RT)
    (java.sql BatchUpdateException DriverManager SQLException Statement)
    (java.util Hashtable Map Properties)
    (javax.naming InitialContext Name)
    (javax.sql DataSource)))

(def ^{:dynamic true} *db* {:connection nil :level 0})

(def special-counts
  {Statement/EXECUTE_FAILED "EXECUTE_FAILED"
   Statement/SUCCESS_NO_INFO "SUCCESS_NO_INFO"})

(defn find-connection*
  "Returns the current database connection (or nil if there is none)"
  []
  (:connection *db*))

(defn connection*
  "Returns the current database connection (or throws if there is none)"
  []
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

(defn get-connection
  "Creates a connection to a database. db-spec is a map containing values
  for one of the following parameter sets:

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
  [{:keys [factory
           classname subprotocol subname
           datasource username password
           name environment]
    :as db-spec}]
  (cond
    factory
    (factory (dissoc db-spec :factory))
    (and classname subprotocol subname)
    (let [url (format "jdbc:%s:%s" subprotocol subname)
          etc (dissoc db-spec :classname :subprotocol :subname)]
      (RT/loadClassForName classname)
      (DriverManager/getConnection url (as-properties etc)))
    (and datasource username password)
    (.getConnection datasource username password)
    datasource
    (.getConnection datasource)
    name
    (let [env (and environment (Hashtable. environment))
          context (InitialContext. env)
          datasource (.lookup context name)]
      (.getConnection datasource))
    :else
    (throw (IllegalArgumentException. (format "db-spec %s is missing a required parameter" db-spec)))))

(defn with-connection*
  "Evaluates func in the context of a new connection to a database then
  closes the connection."
  [db-spec func]
  (with-open [con (get-connection db-spec)]
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
      (let [con (connection*)
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
              (throw e))
            (finally
              (rollback false)
              (.setAutoCommit con auto-commit)))))
      (func))))

(defn resultset-seq*
  "Creates and returns a lazy sequence of structmaps corresponding to
   the rows in the java.sql.ResultSet rs. Based on clojure.core/resultset-seq
   but it respects the current naming strategy."
  [^java.sql.ResultSet rs]
    (let [rsmeta (.getMetaData rs)
          idxs (range 1 (inc (.getColumnCount rsmeta)))
          keys (map (comp keyword *as-key*)
                    (map (fn [^Integer i] (.getColumnLabel rsmeta i)) idxs))
          check-keys
                (or (apply distinct? keys)
                    (throw (Exception. "ResultSet must have unique column labels")))
          row-struct (apply create-struct keys)
          row-values (fn [] (map (fn [^Integer i] (.getObject rs i)) idxs))
          rows (fn thisfn []
                 (when (.next rs)
                   (cons (apply struct row-struct (row-values)) (lazy-seq (thisfn)))))]
      (rows)))

(defn- set-parameters
  "Add the parameters to the given statement."
  [stmt params]
  (dorun
    (map-indexed
      (fn [ix value]
        (.setObject stmt (inc ix) value))
      params)))

(defn do-prepared-return-keys*
  "Executes an (optionally parameterized) SQL prepared statement on the
  open database connection. Each param-group is a seq of values for all of
  the parameters.
  Return the generated keys for the (single) update/insert."
  [sql & param-groups]
  (with-open [stmt (.prepareStatement (connection*) sql java.sql.Statement/RETURN_GENERATED_KEYS)]
    (doseq [param-group param-groups]
      (set-parameters stmt param-group)
      (.addBatch stmt))
    (transaction* (fn [] (do (.executeUpdate stmt)
                           (first (resultset-seq* (.getGeneratedKeys stmt))))))))

(defn do-prepared*
  "Executes an (optionally parameterized) SQL prepared statement on the
  open database connection. Each param-group is a seq of values for all of
  the parameters.
  Return a seq of update counts (one count for each param-group)."
  [sql & param-groups]
  (with-open [stmt (.prepareStatement (connection*) sql)]
    (doseq [param-group param-groups]
      (set-parameters stmt param-group)
      (.addBatch stmt))
    (transaction* (fn [] (seq (.executeBatch stmt))))))

(defn with-query-results*
  "Executes a query, then evaluates func passing in a seq of the results as
  an argument. The first argument is a vector containing the (optionally
  parameterized) sql query string followed by values for any parameters."
  [[sql & params :as sql-params] func]
  (when-not (vector? sql-params)
    (throw (IllegalArgumentException. (format "\"%s\" expected %s %s, found %s %s"
                                              "sql-params"
                                              "vector"
                                              "[sql param*]"
                                              (.getName (class sql-params))
                                              (pr-str sql-params)))))
  (with-open [stmt (.prepareStatement (connection*) sql)]
    (set-parameters stmt params)
    (with-open [rset (.executeQuery stmt)]
      (func (resultset-seq* rset)))))
