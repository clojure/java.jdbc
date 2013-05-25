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
  (:require [clojure.java.jdbc :as sql]
            [clojure.java.jdbc.sql :as dsl]))

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

(defn- mysql? [db]
  (if (string? db)
    (re-find #"mysql:" db)
    (= "mysql" (:subprotocol db))))

(defn- postgres? [db]
  (if (string? db)
    (re-find #"postgres" db)
    (= "postgresql" (:subprotocol db))))

(defmulti create-test-table
  "Create a standard test table. Must be inside with-connection.
   For MySQL, ensure table uses an engine that supports transactions!"
  (fn [table db]
    (cond
     (mysql? db) :mysql
     (postgres? db) :postgres
     :else :default)))

(defmethod create-test-table :mysql
  [table db]
  (sql/create-table
   table
   [:id :int "PRIMARY KEY AUTO_INCREMENT"]
   [:name "VARCHAR(32)"]
   [:appearance "VARCHAR(32)"]
   [:cost :int]
   [:grade :real]
   :table-spec "ENGINE=InnoDB"))

(defmethod create-test-table :postgres
  [table db]
  (sql/create-table
   table
   [:id :serial "PRIMARY KEY"]
   [:name "VARCHAR(32)"]
   [:appearance "VARCHAR(32)"]
   [:cost :int]
   [:grade :real]
   :table-spec ""))

(defmethod create-test-table :default
  [table db]
  (sql/create-table
   table
   [:id :int "DEFAULT 0"]
   [:name "VARCHAR(32)" "PRIMARY KEY"]
   [:appearance "VARCHAR(32)"]
   [:cost :int]
   [:grade :real]
   :table-spec ""))

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
          nil              (when (mysql? db)
                             (is (= '({:generated_key 1} {:generated_key 2}) r)))
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
            ["Apple" "strange" "whoops"]))
        (catch IllegalArgumentException _
          (is (= 0 (sql/with-query-results res ["SELECT * FROM fruit"] (count res))))))
      (is (= 0 (sql/with-query-results res ["SELECT * FROM fruit"] (count res)))))))

(deftest test-insert-values-exception
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit db)
      (is (thrown? IllegalArgumentException
                   (sql/transaction
                    (sql/insert-values
                     :fruit
                     [:name :appearance]
                     ["Grape" "yummy"]
                     ["Pear" "bruised"]
                     ["Apple" "strange" "whoops"]))))
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
            ["Apple" "strange"])
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
                             (sql/result-set-seq
                              (-> (sql/connection)
                                  (.getMetaData)
                                  (.getTables nil nil nil (into-array ["TABLE" "VIEW"]))))))]
        (is (= [] metadata))))))

(defn- returned-key [db k]
  (condp = (:subprotocol db)
    "derby"  {:1 nil}
    "hsqldb" 1
    "mysql"  {:generated_key k}
    nil      (if (mysql? db) ; string-based tests
               {:generated_key k}
               k)
    "jtds:sqlserver" {:id nil}
    "sqlserver" {:generated_keys nil}
    "sqlite" {(keyword "last_insert_rowid()") k}
    k))

(defn- generated-key [db k]
  (condp = (:subprotocol db)
    "derby" 0
    "hsqldb" 0
    "jtds:sqlserver" 0
    "sqlserver" 0
    "sqlite" 0
    k))

(defn- float-or-double [db v]
  (condp = (:subprotocol db)
    "derby" (Float. v)
    "jtds:sqlserver" (Float. v)
    "sqlserver" (Float. v)
    "postgresql" (Float. v)
    v))

(deftest empty-query
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit db))
    (is (= [] (sql/query db (dsl/select * :fruit))))))

(deftest insert-one-row
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit db))
    (let [new-keys (sql/insert! db :fruit {:name "Apple"})
          new-keys (if (postgres? db) (map :id new-keys) new-keys)]
      (is (= [(returned-key db 1)] new-keys)))))

(deftest insert-query
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit db))
    (let [new-keys (sql/insert! db :fruit {:name "Apple"})
          new-keys (if (postgres? db) (map :id new-keys) new-keys)]
      (is (= [(returned-key db 1)] new-keys))
      (is (= [{:id (generated-key db 1) :name "Apple" :appearance nil :grade nil :cost nil}] (sql/query db (dsl/select * :fruit)))))))

(deftest insert-two-by-map-and-query
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit db))
    (let [new-keys (sql/insert! db :fruit {:name "Apple"} {:name "Pear"})
          new-keys (if (postgres? db) (map :id new-keys) new-keys)
          rows (sql/query db (dsl/select * :fruit (dsl/order-by :name)))]
      (is (= [(returned-key db 1) (returned-key db 2)] new-keys))
      (is (= [{:id (generated-key db 1) :name "Apple" :appearance nil :grade nil :cost nil}
              {:id (generated-key db 2) :name "Pear" :appearance nil :grade nil :cost nil}] rows)))))

(deftest insert-two-by-map-and-query-as-arrays
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit db))
    (let [new-keys (sql/insert! db :fruit {:name "Apple"} {:name "Pear"})
          new-keys (if (postgres? db) (map :id new-keys) new-keys)
          rows (sql/query db (dsl/select * :fruit (dsl/order-by :name))
                          :as-arrays? true)]
      (is (= [(returned-key db 1) (returned-key db 2)] new-keys))
      (is (= [[:id :name :appearance :cost :grade]
              [(generated-key db 1) "Apple" nil nil nil]
              [(generated-key db 2) "Pear" nil nil nil]] rows)))))

(deftest insert-two-by-cols-and-query
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit db))
    (let [update-counts (sql/insert! db :fruit [:name] ["Apple"] ["Pear"])
          rows (sql/query db (dsl/select * :fruit (dsl/order-by :name)))]
      (is (= [1 1] update-counts))
      (is (= [{:id (generated-key db 1) :name "Apple" :appearance nil :grade nil :cost nil}
              {:id (generated-key db 2) :name "Pear" :appearance nil :grade nil :cost nil}] rows)))))

(deftest insert-update-and-query
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit db))
    (let [new-keys (sql/insert! db :fruit {:name "Apple"})
          new-keys (if (postgres? db) (map :id new-keys) new-keys)
          update-result (sql/update! db :fruit {:cost 12 :grade 1.2 :appearance "Green"}
                                     (dsl/where {:id (generated-key db 1)}))
          rows (sql/query db (dsl/select * :fruit))]
      (is (= [(returned-key db 1)] new-keys))
      (is (= [1] update-result))
      (is (= [{:id (generated-key db 1)
               :name "Apple" :appearance "Green"
               :grade (float-or-double db 1.2)
               :cost 12}] rows)))))

(deftest insert-delete-and-query
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit db))
    (let [new-keys (sql/insert! db :fruit {:name "Apple"})
          new-keys (if (postgres? db) (map :id new-keys) new-keys)
          delete-result (sql/delete! db :fruit
                                     (dsl/where {:id (generated-key db 1)}))
          rows (sql/query db (dsl/select * :fruit))]
      (is (= [(returned-key db 1)] new-keys))
      (is (= [1] delete-result))
      (is (= [] rows)))))

(deftest illegal-insert-arguments
  (doseq [db (test-specs)]
    (is (thrown? IllegalArgumentException (sql/insert! db)))
    (is (thrown? IllegalArgumentException (sql/insert! db :entities dsl/as-is)))
    (is (thrown? IllegalArgumentException (sql/insert! db {:name "Apple"} [:name])))
    (is (thrown? IllegalArgumentException (sql/insert! db {:name "Apple"} [:name] :entities dsl/as-is)))
    (is (thrown? IllegalArgumentException (sql/insert! db [:name])))
    (is (thrown? IllegalArgumentException (sql/insert! db [:name] :entities dsl/as-is)))))

(deftest test-partial-exception-with-db
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit db))
    (try
      (sql/db-transaction [t-db db]
       (sql/insert! t-db
        :fruit
        [:name :appearance]
        ["Grape" "yummy"]
        ["Pear" "bruised"])
       (is (= 2 (sql/query t-db ["SELECT * FROM fruit"] :result-set-fn count)))
       (throw (Exception. "deliberate exception")))
      (catch Exception _
        (is (= 0 (sql/query db ["SELECT * FROM fruit"] :result-set-fn count)))))))

(deftest test-sql-exception-with-db
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit db))
    (try
      (sql/db-transaction [t-db db]
       (sql/insert! t-db
        :fruit
        [:name :appearance]
        ["Grape" "yummy"]
        ["Pear" "bruised"]
        ["Apple" "strange" "whoops"])
       ;; sqlite does not throw exception for too many items
       (throw (java.sql.SQLException.)))
      (catch Exception _ ;; insert! throws the exception, not JDBC
        (is (= 0 (sql/query db ["SELECT * FROM fruit"] :result-set-fn count)))))
    (is (= 0 (sql/query db ["SELECT * FROM fruit"] :result-set-fn count)))))

(deftest test-rollback-with-db
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit db))
    (try
      (sql/db-transaction [t-db db]
       (is (not (sql/db-is-rollback-only t-db)))
       (sql/db-set-rollback-only! t-db)
       (is (sql/db-is-rollback-only t-db))
       (sql/insert! t-db
        :fruit
        [:name :appearance]
        ["Grape" "yummy"]
        ["Pear" "bruised"]
        ["Apple" "strange" "whoops"])
       (is (= 3 (sql/query t-db ["SELECT * FROM fruit"] :result-set-fn count))))
      (catch Exception _ ;; insert! throws the exception, not JDBC
        (is (= 0 (sql/query db ["SELECT * FROM fruit"] :result-set-fn count)))))
    (is (= 0 (sql/query db ["SELECT * FROM fruit"] :result-set-fn count)))))

(deftest test-transactions-with-possible-generated-keys-result-set-with-db
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit db))
    (sql/db-transaction [t-db db]
     (sql/db-set-rollback-only! t-db)
     (sql/insert! t-db
                  :fruit
                  [:name :appearance]
                  ["Grape" "yummy"])
     (is (= 1 (sql/query t-db ["SELECT * FROM fruit"] :result-set-fn count))))
    (is (= 0 (sql/query db ["SELECT * FROM fruit"] :result-set-fn count)))))

(deftest test-execute!-fails-with-multi-param-groups
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit db))
    ;; RuntimeException -> SQLException -> ArrayIndexOutOfBoundsException
    (is (thrown? Exception
                 (sql/execute!
                  db
                  ["INSERT INTO fruit (name,appearance) VALUES (?,?)"
                   ["Apple" "rosy"]
                   ["Pear" "yellow"]
                   ["Orange" "round"]])))))

(deftest test-execute!-with-multi?-true-param-groups
  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit db))
    ;; RuntimeException -> SQLException -> ArrayIndexOutOfBoundsException
    (let [counts (sql/execute!
                  db
                  ["INSERT INTO fruit (name,appearance) VALUES (?,?)"
                   ["Apple" "rosy"]
                   ["Pear" "yellow"]
                   ["Orange" "round"]]
                  :multi? true)
          rows (sql/query db (dsl/select * :fruit (dsl/order-by :name)))]
      (is (= [1 1 1] counts))
      (is (= [{:id (generated-key db 1) :name "Apple" :appearance "rosy" :cost nil :grade nil}
              {:id (generated-key db 3) :name "Orange" :appearance "round" :cost nil :grade nil}
              {:id (generated-key db 2) :name "Pear" :appearance "yellow" :cost nil :grade nil}] rows)))))

(deftest test-resultset-read-column
  (extend-protocol sql/IResultSetReadColumn
    String
    (result-set-read-column [s _ _] ::FOO))

  (doseq [db (test-specs)]
    (sql/with-connection db
      (create-test-table :fruit db)
      (sql/insert! db
                   :fruit
                   [:name :cost]
                   ["Crepes" 12]
                   ["Vegetables" -88]
                   ["Teenage Mutant Ninja Turtles" 0])
      (is (= {:name ::FOO, :cost -88}
             (sql/with-query-results res ["SELECT name, cost FROM fruit WHERE name = ?"
                                          "Vegetables"]
               (first res))))))

  ;; somewhat "undo" the first extension
  (extend-protocol sql/IResultSetReadColumn
    String
    (result-set-read-column [s _ _] s)))
