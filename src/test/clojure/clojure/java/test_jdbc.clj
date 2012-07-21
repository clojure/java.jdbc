;;  Copyright (c) Stephen C. Gilardi. All rights reserved.  The use and
;;  distribution terms for this software are covered by the Eclipse Public
;;  License 1.0 (http://opensource.org/licenses/eclipse-1.0.php) which can
;;  be found in the file epl-v10.html at the root of this distribution.  By
;;  using this software in any fashion, you are agreeing to be bound by the
;;  terms of this license.  You must not remove this notice, or any other,
;;  from this software.
;;
;;  test_jdbc.clj
;;
;;  This namespace contains tests that exercise the JDBC portion of java.jdbc
;;  so these tests expect databases to be available. Embedded databases can
;;  be tested without external infrastructure (Apache Derby, HSQLDB). Other
;;  databases will be available for testing in different environments. The
;;  available databases for testing can be configured below.
;;
;;  scgilardi (gmail)
;;  Created 13 September 2008
;;
;;  seancorfield (gmail)
;;  Migrated from clojure.contrib.test-sql 17 April 2011

(ns clojure.java.test-jdbc
  (:use clojure.test)
  (:require [clojure.java.jdbc :as sql]))

;; Set test-databases according to whether you have the local database available:
;; Possible values so far: [:mysql :postgres :derby :hsqldb :mysql-str :postgres-str]
;; Apache Derby and HSQLDB can run without an external setup.
(def test-databases
  (if-let [dbs (System/getenv "TEST_DBS")]
    (map keyword (.split dbs ","))
    ;; enable more by default once the build server is equipped?
    [:derby :hsqldb :sqlite]))

;; MS SQL Server requires more specialized configuration:
(def mssql-host
  (if-let [host (System/getenv "TEST_MSSQL_HOST")] host "127.0.0.1\\SQLEXPRESS"))
(def mssql-port
  (if-let [port (System/getenv "TEST_MSSQL_PORT")] port "1433"))
(def mssql-user
  (if-let [user (System/getenv "TEST_MSSQL_USER")] user "sa"))
(def mssql-pass
  (if-let [pass (System/getenv "TEST_MSSQL_PASS")] pass ""))
(def mssql-dbname
  (if-let [name (System/getenv "TEST_MSSQL_NAME")] name "clojure_test"))
(def jtds-host
  (if-let [host (System/getenv "TEST_JTDS_HOST")] host mssql-host))
(def jtds-port
  (if-let [port (System/getenv "TEST_JTDS_PORT")] port mssql-port))
(def jtds-user
  (if-let [user (System/getenv "TEST_JTDS_USER")] user mssql-user))
(def jtds-pass
  (if-let [pass (System/getenv "TEST_JTDS_PASS")] pass mssql-pass))
(def jtds-dbname
  (if-let [name (System/getenv "TEST_JTDS_NAME")] name mssql-dbname))

;; database connections used for testing:

(def mysql-db {:subprotocol "mysql"
               :subname "//127.0.0.1:3306/clojure_test"
               :user "clojure_test"
               :password "clojure_test"})

(def derby-db {:subprotocol "derby"
               :subname "clojure_test_derby"
               :create true})

(def hsqldb-db {:subprotocol "hsqldb"
                :subname "clojure_test_hsqldb"})

(def sqlite-db {:subprotocol "sqlite"
                :subname "clojure_test_sqlite"})

(def postgres-db {:subprotocol "postgresql"
                  :subname "clojure_test"
                  :user "clojure_test"
                  :password "clojure_test"})

(def mssql-db {:subprotocol "sqlserver"
               :subname (str "//" mssql-host ":" mssql-port ";DATABASENAME=" mssql-dbname)
               :user mssql-user
               :password mssql-pass})

(def jtds-db {:subprotocol "jtds:sqlserver"
              :subname (str "//" jtds-host ":" jtds-port "/" jtds-dbname)
              :user jtds-user
              :password jtds-pass})

;; To test against the stringified DB connection settings:
(def mysql-str-db
  "mysql://clojure_test:clojure_test@localhost:3306/clojure_test")

(def mysql-jdbc-str-db
  "jdbc:mysql://clojure_test:clojure_test@localhost:3306/clojure_test")

(def postgres-str-db
  "postgres://clojure_test:clojure_test@localhost/clojure_test")

(defn- test-specs
  "Return a sequence of db-spec maps that should be used for tests"
  []
  (for [db test-databases]
    @(ns-resolve 'clojure.java.test-jdbc (symbol (str (name db) "-db")))))

(defn- clean-up
  "Attempt to drop any test tables before we start a test."
  [t]
  (doseq [db (test-specs)]
    (sql/with-connection db
      (doseq [table [:fruit :fruit2 :veggies :veggies2]]
        (try
          (sql/drop-table table)
          (catch Exception _
            ;; ignore
            )))))
  (t))

(use-fixtures
  :each clean-up)

;; We start with all tables dropped and each test has to create the tables
;; necessary for it to do its job, and populate it as needed...

(defn- create-test-table
  "Create a standard test table. Must be inside with-connection.
   For MySQL, ensure table uses an engine that supports transactions!"
  [table db]
  (let [p (:subprotocol db)]
    (sql/create-table
      table
      [:id :int (if (= "mysql" p) "PRIMARY KEY AUTO_INCREMENT" "DEFAULT 0")]
      [:name "VARCHAR(32)" (if (= "mysql" p) "" "PRIMARY KEY")]
      [:appearance "VARCHAR(32)"]
      [:cost :int]
      [:grade :real]
      (if (= "postgresql" p)
        [:expiry "TIMESTAMP WITHOUT TIME ZONE"])    
      :table-spec (if (or (= "mysql" p) (and (string? db) (re-find #"mysql:" db)))
                    "ENGINE=InnoDB" ""))))

(deftest test-create-table
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit db)
      (is (= 0 (sql/with-query-results res ["SELECT * FROM fruit"] (count res)))))))

(deftest test-drop-table
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit2 db)
      (sql/drop-table :fruit2)
      (is (thrown? java.sql.SQLException (sql/with-query-results res ["SELECT * FROM fruit2"] (count res)))))))

(deftest test-do-commands
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit2 db)
      (sql/do-commands "DROP TABLE fruit2")
      (is (thrown? java.sql.SQLException (sql/with-query-results res ["SELECT * FROM fruit2"] (count res)))))))

(deftest test-do-prepared1
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit2 db)
      (sql/do-prepared "INSERT INTO fruit2 ( name, appearance, cost, grade ) VALUES ( 'test', 'test', 1, 1.0 )")
      (is (= 1 (sql/with-query-results res ["SELECT * FROM fruit2"] (count res)))))))

(deftest test-do-prepared2
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit2 db)
      (sql/do-prepared "DROP TABLE fruit2")
      (is (thrown? java.sql.SQLException (sql/with-query-results res ["SELECT * FROM fruit2"] (count res)))))))

(deftest test-do-prepared3
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit2 db)
      (sql/do-prepared "INSERT INTO fruit2 ( name, appearance, cost, grade ) VALUES ( ?, ?, ?, ? )" ["test" "test" 1 1.0])
      (is (= 1 (sql/with-query-results res ["SELECT * FROM fruit2"] (count res)))))))

(deftest test-do-prepared4
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit2 db)
      (sql/do-prepared "INSERT INTO fruit2 ( name, appearance, cost, grade ) VALUES ( ?, ?, ?, ? )" ["test" "test" 1 1.0] ["two" "two" 2 2.0])
      (is (= 2 (sql/with-query-results res ["SELECT * FROM fruit2"] (count res)))))))

(deftest test-timestamp
  (doseq [db (test-specs)]
    (if (= "postgresql" (:subprotocol db))
      (sql/with-connection db
        (create-test-table :fruit2 db)
        (sql/do-prepared "INSERT INTO fruit2 ( name, appearance, cost, grade, expiry ) VALUES ( 'test', 'test', 1, 1.0, '2012/07/23')")
        (is (= (.getTime (.getTime (doto (java.util.GregorianCalendar. (java.util.TimeZone/getTimeZone "UTC")) 
                                     (.set 2012 6 23 0 0 0)))) ; Month value is 0-based. e.g., 0 for January.
               (sql/with-query-results res ["SELECT * FROM fruit2"] (.getTime (:expiry (first res))))))))))

(deftest test-insert-rows
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit db)
      (let [r (sql/insert-rows
                :fruit
                [1 "Apple" "red" 59 87]
                [2 "Banana" "yellow" 29 92.2]
                [3 "Peach" "fuzzy" 139 90.0]
                [4 "Orange" "juicy" 89 88.6])]
        (is (= '(1 1 1 1) r)))
      (is (= 4 (sql/with-query-results res ["SELECT * FROM fruit"] (count res))))
      (is (= "Apple" (sql/with-query-results res ["SELECT * FROM fruit WHERE appearance = ?" "red"] (:name (first res)))))
      (is (= "juicy" (sql/with-query-results res ["SELECT * FROM fruit WHERE name = ?" "Orange"] (:appearance (first res))))))))

(deftest test-insert-values
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit db)
      (let [r (sql/insert-values
                :fruit
                [:name :cost]
                ["Mango" 722]
                ["Feijoa" 441])]
        (is (= '(1 1) r)))
      (is (= 2 (sql/with-query-results res ["SELECT * FROM fruit"] (count res))))
      (is (= "Mango" (sql/with-query-results res ["SELECT * FROM fruit WHERE cost = ?" 722] (:name (first res))))))))

(deftest test-insert-records
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit db)
      (let [r (sql/insert-records
                :fruit
                {:name "Pomegranate" :appearance "fresh" :cost 585}
                {:name "Kiwifruit" :grade 93})]
        (condp = (:subprotocol db)
          nil nil ; for the string connection args
          "postgresql"     (is (= 2 (count r)))
          "mysql"          (is (= '({:generated_key 1} {:generated_key 2}) r))
          "sqlserver"      (is (= '({:generated_keys nil} {:generated_keys nil}) r))
          "jtds:sqlserver" (is (= '({:id nil} {:id nil}) r))
          "hsqldb"         (is (= '(1 1) r))
          "sqlite"         (is (= (list {(keyword "last_insert_rowid()") 1}
                                        {(keyword "last_insert_rowid()") 2}) r))
          "derby"          (is (= '({:1 nil} {:1 nil}) r))))
      (is (= 2 (sql/with-query-results res ["SELECT * FROM fruit"] (count res))))
      (is (= "Pomegranate" (sql/with-query-results res ["SELECT * FROM fruit WHERE cost = ?" 585] (:name (first res))))))))

(deftest test-update-values
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit db)
      (let [r (sql/insert-rows
                :fruit
                [1 "Apple" "red" 59 87]
                [2 "Banana" "yellow" 29 92.2]
                [3 "Peach" "fuzzy" 139 90.0]
                [4 "Orange" "juicy" 89 88.6])]
        (is (= '(1 1 1 1) r)))
      (sql/update-values
        :fruit
        ["name=?" "Banana"]
        {:appearance "bruised" :cost 14})
      (is (= 4 (sql/with-query-results res ["SELECT * FROM fruit"] (count res))))
      (is (= "Apple" (sql/with-query-results res ["SELECT * FROM fruit WHERE appearance = ?" "red"] (:name (first res)))))
      (is (= "Banana" (sql/with-query-results res ["SELECT * FROM fruit WHERE appearance = ?" "bruised"] (:name (first res)))))
      (is (= 14 (sql/with-query-results res ["SELECT * FROM fruit WHERE name = ?" "Banana"] (:cost (first res))))))))

(deftest test-update-or-insert-values
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit db)
      (sql/update-or-insert-values
        :fruit
        ["name=?" "Pomegranate"]
        {:name "Pomegranate" :appearance "fresh" :cost 585})
      (is (= 1 (sql/with-query-results res ["SELECT * FROM fruit"] (count res))))
      (is (= 585 (sql/with-query-results res ["SELECT * FROM fruit WHERE appearance = ?" "fresh"] (:cost (first res)))))
      (sql/update-or-insert-values
        :fruit
        ["name=?" "Pomegranate"]
        {:name "Pomegranate" :appearance "ripe" :cost 565})
      (is (= 1 (sql/with-query-results res ["SELECT * FROM fruit"] (count res))))
      (is (= 565 (sql/with-query-results res ["SELECT * FROM fruit WHERE appearance = ?" "ripe"] (:cost (first res)))))
      (sql/update-or-insert-values
        :fruit
        ["name=?" "Apple"]
        {:name "Apple" :appearance "green" :cost 74})
      (is (= 2 (sql/with-query-results res ["SELECT * FROM fruit"] (count res)))))))

(deftest test-partial-exception
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit db)
      (try
        (sql/transaction
          (sql/insert-values
            :fruit
            [:name :appearance]
            ["Grape" "yummy"]
            ["Pear" "bruised"])
          (is (= 2 (sql/with-query-results res ["SELECT * FROM fruit"] (count res))))
          (throw (Exception. "deliberate exception")))
        (catch Exception _
          (is (= 0 (sql/with-query-results res ["SELECT * FROM fruit"] (count res)))))))))

(deftest test-sql-exception
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit db)
      (try
        (sql/transaction
          (sql/insert-values
            :fruit
            [:name :appearance]
            ["Grape" "yummy"]
            ["Pear" "bruised"]
            ["Apple" "strange" "whoops"])
          ;; sqlite does not throw exception for too many items
          (throw (java.sql.SQLException.)))
        (catch java.sql.SQLException _
          (is (= 0 (sql/with-query-results res ["SELECT * FROM fruit"] (count res))))))
      (is (= 0 (sql/with-query-results res ["SELECT * FROM fruit"] (count res)))))))

(deftest test-rollback
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit db)
      (try
        (sql/transaction
          (is (not (sql/is-rollback-only)))
          (sql/set-rollback-only)
          (is (sql/is-rollback-only))
          (sql/insert-values
            :fruit
            [:name :appearance]
            ["Grape" "yummy"]
            ["Pear" "bruised"]
            ["Apple" "strange" "whoops"])
          (is (= 3 (sql/with-query-results res ["SELECT * FROM fruit"] (count res)))))
        (catch java.sql.SQLException _
          (is (= 0 (sql/with-query-results res ["SELECT * FROM fruit"] (count res))))))
      (is (= 0 (sql/with-query-results res ["SELECT * FROM fruit"] (count res)))))))

(deftest test-transactions-with-possible-generated-keys-result-set
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit db)
      (try
        (sql/transaction
         (sql/set-rollback-only)
         (sql/insert-values
          :fruit
          [:name :appearance]
          ["Grape" "yummy"])
         (is (= 1 (sql/with-query-results res ["SELECT * FROM fruit"] (count res))))))
      (is (= 0 (sql/with-query-results res ["SELECT * FROM fruit"] (count res)))))))

(deftest test-metadata
  (doseq [db (test-specs)]
    (when-not (and (map? db) (.endsWith ^String (:subprotocol db) "sqlserver"))
      (let [metadata (sql/with-connection
                       db
                       (into []
                             (sql/resultset-seq
                              (-> (sql/connection)
                                  (.getMetaData)
                                  (.getTables nil nil nil (into-array ["TABLE" "VIEW"]))))))]
        (is (= [] metadata))))))
