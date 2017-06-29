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
  (:require [clojure.test :refer :all]
            [clojure.java.jdbc :as sql]
            [clojure.string :as str]))

(def with-spec? (try
                  (require 'clojure.java.jdbc.spec)
                  (require 'clojure.spec.test.alpha)
                  ;; require this to workaround rebinding of report multi-fn
                  (require 'clojure.test.check.clojure-test)
                  (let [syms ((resolve 'clojure.spec.test.alpha/enumerate-namespace) 'clojure.java.jdbc)]
                    ((resolve 'clojure.spec.test.alpha/instrument) syms))
                  (println "Instrumenting clojure.java.jdbc with clojure.spec")
                  true
                  (catch Exception _
                    false)))

;; Set test-databases according to whether you have the local database available:
;; Possible values so far: [:mysql :postgres :derby :hsqldb :mysql-str :postgres-str]
;; Apache Derby and HSQLDB can run without an external setup.
(def test-databases
  (if-let [dbs (System/getenv "TEST_DBS")]
    (map keyword (.split dbs ","))
    ;; enable more by default once the build server is equipped?
    [:derby :hsqldb :h2 :sqlite]))

;; MS SQL Server requires more specialized configuration:
(def mssql-host   (or (System/getenv "TEST_MSSQL_HOST") "127.0.0.1\\SQLEXPRESS"))
(def mssql-port   (or (System/getenv "TEST_MSSQL_PORT") "1433"))
(def mssql-user   (or (System/getenv "TEST_MSSQL_USER") "sa"))
(def mssql-pass   (or (System/getenv "TEST_MSSQL_PASS") ""))
(def mssql-dbname (or (System/getenv "TEST_MSSQL_NAME") "clojure_test"))
(def jtds-host    (or (System/getenv "TEST_JTDS_HOST") mssql-host))
(def jtds-port    (or (System/getenv "TEST_JTDS_PORT") mssql-port))
(def jtds-user    (or (System/getenv "TEST_JTDS_USER") mssql-user))
(def jtds-pass    (or (System/getenv "TEST_JTDS_PASS") mssql-pass))
(def jtds-dbname  (or (System/getenv "TEST_JTDS_NAME") mssql-dbname))

;; PostgreSQL host/port
(def postgres-host (or (System/getenv "TEST_POSTGRES_HOST") "127.0.0.1"))
(def postgres-port (or (System/getenv "TEST_POSTGRES_PORT") "5432"))

;; database connections used for testing:

(def mysql-db {:dbtype   "mysql"
               :dbname   "clojure_test"
               :user     "clojure_test"
               :password "clojure_test"})

(def derby-db {:dbtype "derby"
               :dbname "clojure_test_derby"
               :create true})

(def hsqldb-db {:dbtype "hsql"
                :dbname "clojure_test_hsqldb"})

(def h2-db {:dbtype "h2"
            :dbname "./clojure_test_h2"})

(def sqlite-db {:dbtype "sqlite"
                :dbname "clojure_test_sqlite"})

(def postgres-db {:dbtype   "postgres"
                  :dbname   "clojure_test"
                  :host     postgres-host
                  :port     postgres-port
                  :user     "clojure_test"
                  :password "clojure_test"})

(def pgsql-db {:dbtype   "pgsql"
               :dbname   "clojure_test"
               :host     postgres-host
               :port     postgres-port
               :user     "clojure_test"
               :password "clojure_test"})

(def mssql-db {:dbtype   "mssql"
               :dbname   mssql-dbname
               :host     mssql-host
               :port     mssql-port
               :user     mssql-user
               :password mssql-pass})

(def jtds-db {:dbtype   "jtds"
              :dbname   jtds-dbname
              :host     jtds-host
              :port     jtds-port
              :user     jtds-user
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
    (doseq [table [:fruit :fruit2 :veggies :veggies2]]
      (try
        (sql/db-do-commands db (sql/drop-table-ddl table))
        (catch java.sql.SQLException _))))
          ;; ignore

  (t))

(use-fixtures
  :each clean-up)

;; We start with all tables dropped and each test has to create the tables
;; necessary for it to do its job, and populate it as needed...

(defn- derby? [db]
  (if (string? db)
    (re-find #"derby:" db)
    (= "derby" (or (:subprotocol db) (:dbtype db)))))

(defn- hsqldb? [db]
  (if (string? db)
    (re-find #"hsqldb:" db)
    (#{"hsql" "hsqldb"} (or (:subprotocol db) (:dbtype db)))))

(defn- mssql? [db]
  (if (string? db)
    (re-find #"sqlserver" db)
    (#{"jtds" "jtds:sqlserver" "mssql" "sqlserver"}
     (or (:subprotocol db) (:dbtype db)))))

(defn- mysql? [db]
  (if (string? db)
    (re-find #"mysql:" db)
    (= "mysql" (or (:subprotocol db) (:dbtype db)))))

(defn- postgres? [db]
  (if (string? db)
    (or (re-find #"postgres" db) (re-find #"pgsql"))
    (or (re-find #"postgres" (or (:subprotocol db) (:dbtype db)))
        (re-find #"pgsql" (or (:subprotocol db) (:dbtype db))))))

(defn- pgsql? [db]
  (if (string? db)
    (re-find #"pgsql")
    (re-find #"pgsql" (or (:subprotocol db) (:dbtype db)))))

(defmulti create-test-table
  "Create a standard test table. Uses db-do-commands.
   For MySQL, ensure table uses an engine that supports transactions!"
  (fn [table db]
    (cond
      (mysql? db) :mysql
      (postgres? db) :postgres
      :else :default)))

(defmethod create-test-table :mysql
  [table db]
  (sql/db-do-commands
   db (sql/create-table-ddl
       table
       [[:id :int "PRIMARY KEY AUTO_INCREMENT"]
        [:name "VARCHAR(32)"]
        [:appearance "VARCHAR(32)"]
        [:cost :int]
        [:grade :real]]
       {:table-spec "ENGINE=InnoDB"})))

(defmethod create-test-table :postgres
  [table db]
  (sql/db-do-commands
   db (sql/create-table-ddl
       table
       [[:id :serial "PRIMARY KEY"]
        [:name "VARCHAR(32)"]
        [:appearance "VARCHAR(32)"]
        [:cost :int]
        [:grade :real]]
       {:table-spec ""})))

(defmethod create-test-table :default
  [table db]
  (sql/db-do-commands
   db (sql/create-table-ddl
       table
       [[:id :int "DEFAULT 0"]
        [:name "VARCHAR(32)" "PRIMARY KEY"]
        [:appearance "VARCHAR(32)"]
        [:cost :int]
        [:grade :real]]
       {:table-spec ""})))

(deftest test-drop-table-ddl
  (is (= "DROP TABLE something" (sql/drop-table-ddl :something))))

(deftest test-uri-spec-parsing
  (is (= {:advanced "false" :ssl "required" :password "clojure_test"
          :user "clojure_test" :subname "//localhost/clojure_test"
          :subprotocol "postgresql"}
         (@#'sql/parse-properties-uri
          (java.net.URI.
           (str "postgres://clojure_test:clojure_test@localhost/clojure_test?"
                "ssl=required&advanced=false")))))
  (is (= {:password "clojure_test" :user "clojure_test"
          :subname "//localhost:3306/clojure_test", :subprotocol "mysql"}
         (@#'sql/parse-properties-uri
          (java.net.URI.
           "mysql://clojure_test:clojure_test@localhost:3306/clojure_test")))))

(defn- returned-key [db k]
  (case (or (:subprotocol db) (:dbtype db))
    "derby"  {(keyword "1") nil}
    ("hsql" "hsqldb") nil
    "h2"     nil
    "mysql"  {:generated_key k}
    nil      (if (mysql? db) ; string-based tests
               {:generated_key k}
               k)
    ("jtds" "jtds:sqlserver") {:id nil}
    ("mssql" "sqlserver") {:generated_keys nil}
    "sqlite" {(keyword "last_insert_rowid()") k}
    k))

(defn- select-key [db]
  (case (or (:subprotocol db) (:dbtype db))
    ("postgres" "postgresql" "pgsql") :id
    identity))

(defn- generated-key [db k]
  (case (or (:subprotocol db) (:dbtype db))
    "derby" 0
    ("hsql" "hsqldb") 0
    "h2" 0
    ("jtds" "jtds:sqlserver") 0
    ("mssql" "sqlserver") 0
    "sqlite" 0
    k))

(defn- float-or-double [db v]
  (case (or (:subprotocol db) (:dbtype db))
    "derby" (Float. v)
    "h2" (Float. v)
    ("jtds" "jtds:sqlserver") (Float. v)
    ("mssql" "sqlserver") (Float. v)
    ("postgres" "postgresql" "pgsql") (Float. v)
    v))

(deftest test-create-table
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (is (= 0 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))))

(deftest test-drop-table
  (doseq [db (test-specs)]
    (create-test-table :fruit2 db)
    (sql/db-do-commands db (sql/drop-table-ddl :fruit2))
    (is (thrown? java.sql.SQLException
                 (sql/query db ["SELECT * FROM fruit2"] {:result-set-fn count})))))

(deftest test-do-commands
  (doseq [db (test-specs)]
    (create-test-table :fruit2 db)
    (sql/db-do-commands db "DROP TABLE fruit2")
    (is (thrown? java.sql.SQLException
                 (sql/query db ["SELECT * FROM fruit2"] {:result-set-fn count})))))

(deftest test-do-commands-transaction
  (doseq [db (test-specs)]
    (create-test-table :fruit2 db)
    (sql/db-do-commands db true "DROP TABLE fruit2")
    (is (thrown? java.sql.SQLException
                 (sql/query db ["SELECT * FROM fruit2"] {:result-set-fn count})))))

(deftest test-do-commands-multi
  (doseq [db (test-specs)]
    (sql/db-do-commands db
                        [(sql/create-table-ddl :fruit3
                                               [[:name       "VARCHAR(32)"]
                                                [:appearance "VARCHAR(32)"]
                                                [:cost       :int]])
                         "DROP TABLE fruit3"])
    (is (thrown? java.sql.SQLException
                 (sql/query db ["SELECT * FROM fruit2"] {:result-set-fn count})))))

(deftest test-do-commands-multi-transaction
  (doseq [db (test-specs)]
    (sql/db-do-commands db true
                        [(sql/create-table-ddl :fruit3
                                               [[:name       "VARCHAR(32)"]
                                                [:appearance "VARCHAR(32)"]
                                                [:cost       :int]])
                         "DROP TABLE fruit3"])
    (is (thrown? java.sql.SQLException
                 (sql/query db ["SELECT * FROM fruit2"] {:result-set-fn count})))))

(deftest test-do-prepared1a
  (doseq [db (test-specs)]
    (create-test-table :fruit2 db)
    ;; single string is acceptable
    (sql/db-do-prepared db "INSERT INTO fruit2 ( name, appearance, cost, grade ) VALUES ( 'test', 'test', 1, 1.0 )")
    (is (= 1 (sql/query db ["SELECT * FROM fruit2"] {:result-set-fn count})))))

(deftest test-do-prepared1b
  (doseq [db (test-specs)]
    (create-test-table :fruit2 db)
    (with-open [con (sql/get-connection db)]
      (let [stmt (sql/prepare-statement con "INSERT INTO fruit2 ( name, appearance, cost, grade ) VALUES ( 'test', 'test', 1, 1.0 )")]
        ;; single PreparedStatement is acceptable
        (is (= [1] (sql/db-do-prepared db stmt)))))
    (is (= 1 (sql/query db ["SELECT * FROM fruit2"] {:result-set-fn count})))))

(deftest test-do-prepared1ci
  (doseq [db (test-specs)]
    (create-test-table :fruit2 db)
    (is (= (returned-key db 1)
           ((select-key db) (sql/db-do-prepared-return-keys db "INSERT INTO fruit2 ( name, appearance, cost, grade ) VALUES ( 'test', 'test', 1, 1.0 )"))))
    (is (= 1 (sql/query db ["SELECT * FROM fruit2"] {:result-set-fn count})))))

(deftest test-do-prepared1cii
  (doseq [db (test-specs)]
    (create-test-table :fruit2 db)
    (is (= (returned-key db 1)
           ((select-key db) (sql/db-do-prepared-return-keys db ["INSERT INTO fruit2 ( name, appearance, cost, grade ) VALUES ( 'test', 'test', 1, 1.0 )"]))))
    (is (= 1 (sql/query db ["SELECT * FROM fruit2"] {:result-set-fn count})))))

(deftest test-do-prepared1di
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (with-open [con (sql/get-connection db)]
      (let [stmt (sql/prepare-statement con "INSERT INTO fruit ( name, appearance, cost, grade ) VALUES ( 'test', 'test', 1, 1.0 )"
                                        {:return-keys true})]
        (is (= (returned-key db 1)
               ((select-key db) (sql/db-do-prepared-return-keys db [stmt]))))))
    (is (= 1 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))))

(deftest test-do-prepared1dii
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (with-open [con (sql/get-connection db)]
      (let [stmt (sql/prepare-statement con "INSERT INTO fruit ( name, appearance, cost, grade ) VALUES ( 'test', 'test', 1, 1.0 )"
                                        {:return-keys true})]
        (is (= (returned-key db 1)
               ((select-key db) (sql/db-do-prepared-return-keys db stmt))))))
    (is (= 1 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))))

(deftest test-do-prepared1e
  (doseq [db (test-specs)]
    ;; Derby/SQL Server does not have auto-generated id column which we're testing here
    (when-not (#{"derby" "jtds:sqlserver"} (or (:subprotocol db) (:dbtype db)))
      (create-test-table :fruit db)
      (with-open [con (sql/get-connection db)]
        (let [stmt (sql/prepare-statement con "INSERT INTO fruit ( name, appearance, cost, grade ) VALUES ( 'test', 'test', 1, 1.0 )"
                                          {:return-keys ["id"]})]
          (is (= (returned-key db 1) ((select-key db) (sql/db-do-prepared-return-keys db stmt))))))
      (is (= 1 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count}))))))

(deftest test-do-prepared2
  (doseq [db (test-specs)]
    (create-test-table :fruit2 db)
    (sql/db-do-prepared db "DROP TABLE fruit2")
    (is (thrown? java.sql.SQLException
                 (sql/query db ["SELECT * FROM fruit2"] {:result-set-fn count})))))

(deftest test-do-prepared3a
  (doseq [db (test-specs)]
    (create-test-table :fruit2 db)
    (sql/db-do-prepared db ["INSERT INTO fruit2 ( name, appearance, cost, grade ) VALUES ( ?, ?, ?, ? )" ["test" "test" 1 1.0]] {:multi? true})
    (is (= 1 (sql/query db ["SELECT * FROM fruit2"] {:result-set-fn count})))))

(deftest test-do-prepared3b
  (doseq [db (test-specs)]
    (create-test-table :fruit2 db)
    (sql/db-do-prepared db ["INSERT INTO fruit2 ( name, appearance, cost, grade ) VALUES ( ?, ?, ?, ? )" "test" "test" 1 1.0])
    (is (= 1 (sql/query db ["SELECT * FROM fruit2"] {:result-set-fn count})))))

(deftest test-do-prepared4
  (doseq [db (test-specs)]
    (create-test-table :fruit2 db)
    (sql/db-do-prepared db ["INSERT INTO fruit2 ( name, appearance, cost, grade ) VALUES ( ?, ?, ?, ? )" ["test" "test" 1 1.0] ["two" "two" 2 2.0]] {:multi? true})
    (is (= 2 (sql/query db ["SELECT * FROM fruit2"] {:result-set-fn count})))))

(deftest test-insert-rows
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (let [r (sql/insert-multi! db
                               :fruit
                               nil
                               [[1 "Apple" "red" 59 87]
                                [2 "Banana" "yellow" 29 92.2]
                                [3 "Peach" "fuzzy" 139 90.0]
                                [4 "Orange" "juicy" 139 88.6]])]
      (is (= '(1 1 1 1) r)))
    (is (= 4 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))
    (is (= 4 (sql/with-db-connection [con db]
               (sql/query con (sql/prepare-statement (sql/db-connection con) "SELECT * FROM fruit") {:result-set-fn count}))))
    (when-not (pgsql? db)
      ;; maxRows does not appear to be supported on Impossibl pgsql?
      (is (= 2 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count :max-rows 2})))
      (is (= 2 (sql/with-db-connection [con db]
                 (sql/query con [(sql/prepare-statement (sql/db-connection con) "SELECT * FROM fruit" {:max-rows 2})] {:result-set-fn count})))))
    (is (= "Apple" (sql/query db ["SELECT * FROM fruit WHERE appearance = ?" "red"] {:row-fn :name :result-set-fn first})))
    (is (= "juicy" (sql/query db ["SELECT * FROM fruit WHERE name = ?" "Orange"] {:row-fn :appearance :result-set-fn first})))
    (is (= "Apple" (:name (sql/get-by-id db :fruit 1))))
    (is (= ["Apple"] (map :name (sql/find-by-keys db :fruit {:appearance "red"}))))
    (is (= "Peach" (:name (sql/get-by-id db :fruit 3 :id))))
    (is (= ["Peach"] (map :name (sql/find-by-keys db :fruit {:id 3 :cost 139}))))
    (is (= ["Peach" "Orange"] (map :name (sql/find-by-keys db :fruit {:cost 139} {:order-by [:id]}))))
    (is (= ["Orange" "Peach"] (map :name (sql/find-by-keys db :fruit {:cost 139} {:order-by [{:appearance :desc}]}))))
    (is (= 366 (reduce (fn [n r] (+ n (:cost r))) 0
                       (sql/reducible-query db "SELECT * FROM fruit"))))
    (when-let [transduce-fn (resolve 'clojure.core/transduce)]
      (is (= 366 (transduce-fn (map :cost) +
                               (sql/reducible-query db "SELECT * FROM fruit")))))))

(deftest test-insert-values
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (let [r (sql/insert-multi! db
                               :fruit
                               [:name :cost]
                               [["Mango" 722]
                                ["Feijoa" 441]])]
      (is (= '(1 1) r)))
    (is (= 2 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))
    (is (= "Mango" (sql/query db ["SELECT * FROM fruit WHERE cost = ?" 722] {:row-fn :name :result-set-fn first})))))

(deftest test-insert-records
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (let [r (map (select-key db) (sql/insert-multi! db
                                                    :fruit
                                                    [{:name "Pomegranate" :appearance "fresh" :cost 585}
                                                     {:name "Kiwifruit" :grade 93}]))]
      (is (= (list (returned-key db 1) (returned-key db 2)) r)))
    (is (= 2 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))
    (is (= "Pomegranate" (sql/query db ["SELECT * FROM fruit WHERE cost = ?" 585] {:row-fn :name :result-set-fn first})))))

(deftest test-insert-via-execute
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (sql/execute! db [(str "INSERT INTO fruit ( name, appearance, cost ) "
                           "VALUES ( ?, ?, ? )")
                      "Apple" "Green" 75])
    (sql/execute! db [(str "INSERT INTO fruit ( name, appearance, cost ) "
                           "VALUES ( 'Pear', 'Yellow', 99 )")])
    (is (= 2 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))
    (is (= "Pear" (sql/query db ["SELECT * FROM fruit WHERE cost = ?" 99]
                             {:row-fn :name :result-set-fn first})))))

(deftest execute-with-prepared-statement
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (sql/with-db-connection [conn db]
      (let [connection (:connection conn)
            prepared-statement (sql/prepare-statement connection (str "INSERT INTO fruit ( name, appearance, cost ) "
                                                                      "VALUES ( ?, ?, ? )"))]

        (sql/execute! db [prepared-statement "Apple" "Green" 75])
        (sql/execute! db [prepared-statement "Pear" "Yellow" 99])))
    (is (= 2 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))
    (is (= "Pear" (sql/query db ["SELECT * FROM fruit WHERE cost = ?" 99]
                             {:row-fn :name :result-set-fn first})))))

(deftest execute-with-prepared-statement-return-keys
  (doseq [db (test-specs)]
    ;; Derby/SQL Server does not have auto-generated id column which we're testing here
    (when-not (#{"derby" "jtds" "jtds:sqlserver"} (or (:subprotocol db) (:dbtype db)))
      (create-test-table :fruit db)
      (sql/with-db-connection [conn db]
        (let [connection (:connection conn)
              prepared-statement (sql/prepare-statement connection (str "INSERT INTO fruit ( name, appearance, cost ) "
                                                                        "VALUES ( ?, ?, ? )")
                                                        {:return-keys ["id"]})]
          ;; what is returned is affected row counts due to how execute! works
          (is (= [1] (sql/execute! db [prepared-statement "Apple" "Green" 75])))
          (is (= [1] (sql/execute! db [prepared-statement "Pear" "Yellow" 99])))))
      (is (= 2 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))
      (is (= "Pear" (sql/query db ["SELECT * FROM fruit WHERE cost = ?" 99]
                               {:row-fn :name :result-set-fn first}))))))

(deftest test-update-values
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (let [r (sql/insert-multi! db
                               :fruit
                               nil
                               [[1 "Apple" "red" 59 87]
                                [2 "Banana" "yellow" 29 92.2]
                                [3 "Peach" "fuzzy" 139 90.0]
                                [4 "Orange" "juicy" 89 88.6]])]
      (is (= '(1 1 1 1) r)))
    (sql/update! db
                 :fruit
                 {:appearance "bruised" :cost 14}
                 ["name=?" "Banana"])
    (is (= 4 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))
    (is (= "Apple" (sql/query db ["SELECT * FROM fruit WHERE appearance = ?" "red"]
                              {:row-fn :name :result-set-fn first})))
    (is (= "Banana" (sql/query db ["SELECT * FROM fruit WHERE appearance = ?" "bruised"]
                               {:row-fn :name :result-set-fn first})))
    (is (= 14 (sql/query db ["SELECT * FROM fruit WHERE name = ?" "Banana"]
                         {:row-fn :cost :result-set-fn first})))))

(defn update-or-insert-values
  [db table row where]
  (sql/with-db-transaction [t-conn db]
    (let [result (sql/update! t-conn table row where)]
      (if (zero? (first result))
        (sql/insert! t-conn table row)
        result))))

(defn update-or-insert-values-with-isolation
  [db table row where]
  (sql/with-db-transaction [t-conn db {:isolation :read-uncommitted}]
    (let [result (sql/update! t-conn table row where)]
      (if (zero? (first result))
        (sql/insert! t-conn table row)
        result))))

(deftest test-update-or-insert-values
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (update-or-insert-values db
                             :fruit
                             {:name "Pomegranate" :appearance "fresh" :cost 585}
                             ["name=?" "Pomegranate"])
    (is (= 1 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))
    (is (= 585 (sql/query db ["SELECT * FROM fruit WHERE appearance = ?" "fresh"]
                          {:row-fn :cost :result-set-fn first})))
    (update-or-insert-values db
                             :fruit
                             {:name "Pomegranate" :appearance "ripe" :cost 565}
                             ["name=?" "Pomegranate"])
    (is (= 1 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))
    (is (= 565 (sql/query db ["SELECT * FROM fruit WHERE appearance = ?" "ripe"]
                          {:row-fn :cost :result-set-fn first})))
    (update-or-insert-values db
                             :fruit
                             {:name "Apple" :appearance "green" :cost 74}
                             ["name=?" "Apple"])
    (is (= 2 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))))

(deftest test-update-or-insert-values-with-isolation
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (update-or-insert-values-with-isolation db
                                            :fruit
                                            {:name "Pomegranate" :appearance "fresh" :cost 585}
                                            ["name=?" "Pomegranate"])
    (is (= 1 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))
    (is (= 585 (sql/query db ["SELECT * FROM fruit WHERE appearance = ?" "fresh"]
                          {:row-fn :cost :result-set-fn first})))
    (update-or-insert-values db
                             :fruit
                             {:name "Pomegranate" :appearance "ripe" :cost 565}
                             ["name=?" "Pomegranate"])
    (is (= 1 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))
    (is (= 565 (sql/query db ["SELECT * FROM fruit WHERE appearance = ?" "ripe"]
                          {:row-fn :cost :result-set-fn first})))
    (update-or-insert-values db
                             :fruit
                             {:name "Apple" :appearance "green" :cost 74}
                             ["name=?" "Apple"])
    (is (= 2 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))))

(defn- file-not-found-exception-via-reflection
  "In Clojure 1.3.0 this caused a wrapped exception and we introduced throw-non-rte
  to workaround that. This was fixed in 1.4.0 but we never removed the workaround.
  Added this hack from the mailing list specifically to test the exception handling
  so that we can verify only Clojure 1.3.0 fails the tests and drop support for it."
  [f]
  (java.io.FileReader. f))

(deftest test-partial-exception
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (try
      (sql/with-db-transaction [t-conn db]
        (sql/insert-multi! t-conn
                           :fruit
                           [:name :appearance]
                           [["Grape" "yummy"]
                            ["Pear" "bruised"]])
        (is (= 2 (sql/query t-conn ["SELECT * FROM fruit"] {:result-set-fn count})))
        (file-not-found-exception-via-reflection "/etc/password_no_such_file"))
      (catch java.io.FileNotFoundException _
        (is (= 0 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count}))))
      (catch Exception _
        (is false "Unexpected exception encountered (not wrapped?).")))))

(deftest test-partial-exception-with-isolation
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (try
      (sql/with-db-transaction [t-conn db {:isolation :serializable}]
        (sql/insert-multi! t-conn
                           :fruit
                           [:name :appearance]
                           [["Grape" "yummy"]
                            ["Pear" "bruised"]])
        (is (= 2 (sql/query t-conn ["SELECT * FROM fruit"] {:result-set-fn count})))
        (throw (Exception. "deliberate exception")))
      (catch Exception _
        (is (= 0 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))))))

(deftest test-sql-exception
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (try
      (sql/with-db-transaction [t-conn db]
        (sql/insert! t-conn
                     :fruit
                     [:name :appearance]
                     ["Apple" "strange" "whoops"]))
      (catch IllegalArgumentException _
        (is (= 0 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))))
    (is (= 0 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))))

(deftest test-sql-exception-with-isolation
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (try
      (sql/with-db-transaction [t-conn db {:isolation :read-uncommitted}]
        (sql/insert! t-conn
                     :fruit
                     [:name :appearance]
                     ["Apple" "strange" "whoops"]))
      (catch IllegalArgumentException _
        (is (= 0 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))))
    (is (= 0 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))))

(deftest test-insert-values-exception
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (is (thrown? IllegalArgumentException
                 (sql/with-db-transaction [t-conn db]
                   (sql/insert-multi! t-conn
                                      :fruit
                                      [:name :appearance]
                                      [["Grape" "yummy"]
                                       ["Pear" "bruised"]
                                       ["Apple" "strange" "whoops"]]))))
    (is (= 0 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))))

(deftest test-insert-values-exception-with-isolation
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (is (thrown? IllegalArgumentException
                 (sql/with-db-transaction [t-conn db {:isolation :read-uncommitted}]
                   (sql/insert-multi! t-conn
                                      :fruit
                                      [:name :appearance]
                                      [["Grape" "yummy"]
                                       ["Pear" "bruised"]
                                       ["Apple" "strange" "whoops"]]))))
    (is (= 0 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))))

(deftest test-rollback
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (try
      (sql/with-db-transaction [t-conn db]
        (is (not (sql/db-is-rollback-only t-conn)))
        (sql/db-set-rollback-only! t-conn)
        (is (sql/db-is-rollback-only t-conn))
        (sql/insert-multi! t-conn
                           :fruit
                           [:name :appearance]
                           [["Grape" "yummy"]
                            ["Pear" "bruised"]
                            ["Apple" "strange"]])
        (is (= 3 (sql/query t-conn ["SELECT * FROM fruit"] {:result-set-fn count}))))
      (catch java.sql.SQLException _
        (is (= 0 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))))
    (is (= 0 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))))

(deftest test-rollback-with-isolation
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (try
      (sql/with-db-transaction [t-conn db {:isolation :read-uncommitted}]
        (is (not (sql/db-is-rollback-only t-conn)))
        (sql/db-set-rollback-only! t-conn)
        (is (sql/db-is-rollback-only t-conn))
        (sql/insert-multi! t-conn
                           :fruit
                           [:name :appearance]
                           [["Grape" "yummy"]
                            ["Pear" "bruised"]
                            ["Apple" "strange"]])
        (is (= 3 (sql/query t-conn ["SELECT * FROM fruit"] {:result-set-fn count}))))
      (catch java.sql.SQLException _
        (is (= 0 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))))
    (is (= 0 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))))

(deftest test-transactions-with-possible-generated-keys-result-set
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (sql/with-db-transaction [t-conn db]
      (sql/db-set-rollback-only! t-conn)
      (sql/insert! t-conn
                   :fruit
                   [:name :appearance]
                   ["Grape" "yummy"])
      (is (= 1 (sql/query t-conn ["SELECT * FROM fruit"] {:result-set-fn count}))))
    (is (= 0 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))))

(deftest test-transactions-with-possible-generated-keys-result-set-and-isolation
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (sql/with-db-transaction [t-conn db {:isolation :read-uncommitted}]
      (sql/db-set-rollback-only! t-conn)
      (sql/insert! t-conn
                   :fruit
                   [:name :appearance]
                   ["Grape" "yummy"])
      (is (= 1 (sql/query t-conn ["SELECT * FROM fruit"] {:result-set-fn count}))))
    (is (= 0 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))))

(deftest test-nested-transactions-check-transaction-isolation-level
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (sql/with-db-transaction [t-conn db {:isolation :read-uncommitted}]
      (is (thrown? IllegalStateException
                   (sql/with-db-transaction [t-conn' t-conn {:isolation :serializable}]
                     (sql/insert! t-conn'
                                  :fruit
                                  [:name :appearance]
                                  ["Grape" "yummy"])))))
    (is (= 0 (sql/query db ["SELECT * FROM fruit"] {:result-set-fn count})))))

(deftest test-raw-metadata
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (let [table-info (with-open [conn (sql/get-connection db)]
                       (into []
                             (sql/result-set-seq
                              (-> conn
                                  (.getMetaData)
                                  (.getTables nil nil nil
                                              (into-array ["TABLE" "VIEW"]))))))]
      (is (not= [] table-info))
      (is (= "fruit" (-> table-info
                         first
                         :table_name
                         clojure.string/lower-case))))))

(deftest test-metadata-managed
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (sql/with-db-metadata [metadata db]
      (let [table-info (sql/metadata-query (.getTables metadata
                                                       nil nil nil
                                                       (into-array ["TABLE" "VIEW"])))]
        (is (not= [] table-info))
        (is (= "fruit" (-> table-info
                           first
                           :table_name
                           clojure.string/lower-case)))))))

(deftest test-metadata-managed-computed
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (is (= "fruit"
           (sql/with-db-metadata [metadata db]
             (sql/metadata-query (.getTables metadata
                                             nil nil nil
                                             (into-array ["TABLE" "VIEW"]))
                                 {:row-fn (comp clojure.string/lower-case str :table_name)
                                  :result-set-fn first}))))))

(deftest test-metadata
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (sql/with-db-metadata [metadata db]
      ;; make sure to close the ResultSet
      (with-open [table-info-result (.getTables metadata
                                                nil nil nil
                                                (into-array ["TABLE" "VIEW"]))]
        (let [table-info (sql/metadata-result table-info-result)]
          (is (not= [] table-info))
          (is (= "fruit" (-> table-info
                             first
                             :table_name
                             clojure.string/lower-case))))))))

(deftest empty-query
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (is (= [] (sql/query db ["SELECT * FROM fruit"])))))

(deftest query-with-string
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (is (= [] (sql/query db "SELECT * FROM fruit")))))

(deftest insert-one-row
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (let [new-keys (map (select-key db) (sql/insert! db :fruit {:name "Apple"}))]
      (is (= [(returned-key db 1)] new-keys)))))

(deftest insert-one-row-opts
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (let [new-keys (map (select-key db) (sql/insert! db :fruit {:name "Apple"} {}))]
      (is (= [(returned-key db 1)] new-keys)))))

(deftest insert-one-col-val
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (let [new-keys (sql/insert! db :fruit [:name] ["Apple"])]
      (is (= [1] new-keys)))))

(deftest insert-one-col-val-opts
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (let [new-keys (sql/insert! db :fruit [:name] ["Apple"] {})]
      (is (= [1] new-keys)))))

(deftest insert-query
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (let [new-keys (map (select-key db) (sql/insert! db :fruit {:name "Apple"}))]
      (is (= [(returned-key db 1)] new-keys))
      (is (= [{:id (generated-key db 1) :name "Apple" :appearance nil :grade nil :cost nil}]
             (sql/query db "SELECT * FROM fruit")))
      (is (= [{:id (generated-key db 1) :name "Apple" :appearance nil :grade nil :cost nil}]
             (sql/query db ["SELECT * FROM fruit"])))
      (is (= [{:ID (generated-key db 1) :NAME "Apple" :APPEARANCE nil :GRADE nil :COST nil}]
             (sql/query db ["SELECT * FROM fruit"] {:identifiers str/upper-case})))
      (is (= [{:ID (generated-key db 1) :NAME "Apple" :APPEARANCE nil :GRADE nil :COST nil}]
             (sql/query (assoc db :identifiers str/upper-case) ["SELECT * FROM fruit"])))
      (is (= [{:fruit/id (generated-key db 1) :fruit/name "Apple" :fruit/appearance nil
               :fruit/grade nil :fruit/cost nil}]
             (sql/query db ["SELECT * FROM fruit"] {:qualifier "fruit"})))
      (is (= [{:fruit/name "Apple"}]
             (sql/query db ["SELECT name FROM fruit"]
                        {:identifiers (comp (partial str "fruit/") str/lower-case)})))
      (is (= [{:name "Apple"}]
             (sql/query db ["SELECT name FROM fruit"]
                        {:identifiers (comp keyword str/lower-case)})))
      (is (= [{:fruit/id (generated-key db 1) :fruit/name "Apple" :fruit/appearance nil
               :fruit/grade nil :fruit/cost nil}]
             (sql/query (assoc db :qualifier "fruit") ["SELECT * FROM fruit"])))
      (is (= [{:id (generated-key db 1) :name "Apple" :appearance nil :grade nil :cost nil}]
             (with-open [con (sql/get-connection db)]
               (sql/query db [(sql/prepare-statement con "SELECT * FROM fruit")]))))
      (is (= [{:id (generated-key db 1) :name "Apple" :appearance nil :grade nil :cost nil}]
             (sql/query db ["SELECT * FROM fruit"] {:max-rows 1})))
      (cond (derby? db) nil
            (hsqldb? db) (is (seq (with-out-str
                                    (sql/query db ["SELECT * FROM fruit"]
                                               {:explain? "EXPLAIN PLAN FOR"}))))
            (mssql? db) nil
            :else (is (seq (with-out-str
                             (sql/query db ["SELECT * FROM fruit"]
                                        {:explain? true}))))))))

(deftest insert-two-by-map-and-query
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (let [new-keys (map (select-key db) (sql/insert-multi! db :fruit [{:name "Apple"} {:name "Pear"}]))
          rows (sql/query db ["SELECT * FROM fruit ORDER BY name"])]
      (is (= [(returned-key db 1) (returned-key db 2)] new-keys))
      (is (= [{:id (generated-key db 1) :name "Apple" :appearance nil :grade nil :cost nil}
              {:id (generated-key db 2) :name "Pear" :appearance nil :grade nil :cost nil}] rows)))))

(deftest insert-identifiers-respected-1
  (doseq [db (filter postgres? (test-specs))]
    (create-test-table :fruit db)
    (let [inserted (sql/insert! db
                                :fruit
                                {:name "Apple"}
                                {:identifiers clojure.string/upper-case
                                 :qualifier "foo"})
          rows     (sql/query db ["SELECT * FROM fruit ORDER BY name"]
                              {:identifiers clojure.string/upper-case
                               :qualifier "foo"})]
      (is (= rows inserted)))))

(deftest insert-identifiers-respected-2
  (doseq [db (filter postgres? (test-specs))]
    (create-test-table :fruit db)
    (let [inserted (sql/insert-multi! db
                                      :fruit
                                      [{:name "Apple"} {:name "Pear"}]
                                      {:identifiers clojure.string/upper-case})
          rows     (sql/query db ["SELECT * FROM fruit ORDER BY name"]
                              {:identifiers clojure.string/upper-case})]
      (is (= rows inserted)))))

(deftest insert-two-by-map-and-query-as-arrays
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (let [new-keys (map (select-key db) (sql/insert-multi! db :fruit [{:name "Apple"} {:name "Pear"}]))
          rows (sql/query db ["SELECT * FROM fruit ORDER BY name"]
                          {:as-arrays? :cols-as-is})]
      (is (= [(returned-key db 1) (returned-key db 2)] new-keys))
      (is (= [[:id :name :appearance :cost :grade]
              [(generated-key db 1) "Apple" nil nil nil]
              [(generated-key db 2) "Pear" nil nil nil]] rows)))))

(deftest insert-two-by-cols-and-query
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (let [update-counts (sql/insert-multi! db :fruit [:name] [["Apple"] ["Pear"]])
          rows (sql/query db ["SELECT * FROM fruit ORDER BY name"])]
      (is (= [1 1] update-counts))
      (is (= [{:id (generated-key db 1) :name "Apple" :appearance nil :grade nil :cost nil}
              {:id (generated-key db 2) :name "Pear" :appearance nil :grade nil :cost nil}] rows)))))

(deftest insert-update-and-query
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (let [new-keys (map (select-key db) (sql/insert! db :fruit {:name "Apple"}))
          update-result (sql/update! db :fruit {:cost 12 :grade 1.2 :appearance "Green"}
                                     ["id = ?" (generated-key db 1)])
          rows (sql/query db ["SELECT * FROM fruit"])]
      (is (= [(returned-key db 1)] new-keys))
      (is (= [1] update-result))
      (is (= [{:id (generated-key db 1)
               :name "Apple" :appearance "Green"
               :grade (float-or-double db 1.2)
               :cost 12}] rows)))))

(deftest insert-delete-and-query
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (let [new-keys (map (select-key db) (sql/insert! db :fruit {:name "Apple"}))
          delete-result (sql/delete! db :fruit
                                     ["id = ?" (generated-key db 1)])
          rows (sql/query db ["SELECT * FROM fruit"])]
      (is (= [(returned-key db 1)] new-keys))
      (is (= [1] delete-result))
      (is (= [] rows)))))

(deftest insert-delete-and-query-in-connection
  (doseq [db (test-specs)]
    (sql/with-db-connection [con-db db]
      (create-test-table :fruit con-db)
      (let [new-keys (map (select-key db) (sql/insert! con-db :fruit {:name "Apple"}))
            delete-result (sql/delete! con-db :fruit
                                       ["id = ?" (generated-key con-db 1)])
            rows (sql/query con-db ["SELECT * FROM fruit"])]
        (is (= [(returned-key con-db 1)] new-keys))
        (is (= [1] delete-result))
        (is (= [] rows))))))

(deftest illegal-insert-arguments
  (doseq [db (test-specs)]
    (is (thrown? IllegalArgumentException (sql/insert! db)))
    (is (thrown? IllegalArgumentException (sql/insert! db {:name "Apple"} [:name])))
    (is (thrown? IllegalArgumentException (sql/insert! db {:name "Apple"} [:name] {:entities identity})))
    (is (thrown? IllegalArgumentException (sql/insert! db [:name])))
    (if with-spec? ; clojure.spec catches this differently
      (is (thrown? clojure.lang.ExceptionInfo (sql/insert! db [:name] {:entities identity})))
      (is (thrown? ClassCastException (sql/insert! db [:name] {:entities identity}))))))

(deftest test-execute!-fails-with-multi-param-groups
  (doseq [db (test-specs)]
    (create-test-table :fruit db)
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
    (create-test-table :fruit db)
    ;; RuntimeException -> SQLException -> ArrayIndexOutOfBoundsException
    (let [counts (sql/execute!
                  db
                  ["INSERT INTO fruit (name,appearance) VALUES (?,?)"
                   ["Apple" "rosy"]
                   ["Pear" "yellow"]
                   ["Orange" "round"]]
                  {:multi? true})
          rows (sql/query db ["SELECT * FROM fruit ORDER BY name"])]
      (is (= [1 1 1] counts))
      (is (= [{:id (generated-key db 1) :name "Apple" :appearance "rosy" :cost nil :grade nil}
              {:id (generated-key db 3) :name "Orange" :appearance "round" :cost nil :grade nil}
              {:id (generated-key db 2) :name "Pear" :appearance "yellow" :cost nil :grade nil}] rows)))))

(deftest test-resultset-read-column
  (extend-protocol sql/IResultSetReadColumn
    String
    (result-set-read-column [s _ _] ::FOO))

  (try
    (doseq [db (test-specs)]
      (create-test-table :fruit db)
      (sql/insert-multi! db
                         :fruit
                         [:name :cost :grade]
                         [["Crepes" 12 87.7]
                          ["Vegetables" -88 nil]
                          ["Teenage Mutant Ninja Turtles" 0 100.0]])
      (is (= {:name ::FOO, :cost -88, :grade nil}
             (sql/query db ["SELECT name, cost, grade FROM fruit WHERE name = ?"
                            "Vegetables"]
                        {:result-set-fn first}))))

    ;; somewhat "undo" the first extension
    (finally
      (extend-protocol sql/IResultSetReadColumn
        String
        (result-set-read-column [s _ _] s)))))

(deftest test-sql-value
  (extend-protocol sql/ISQLValue
    clojure.lang.Keyword
    (sql-value [_] "KW"))

  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (sql/insert! db
                 :fruit
                 [:name :cost :grade]
                 [:test 12 nil])
    (is (= {:name "KW", :cost 12, :grade nil}
           (sql/query db ["SELECT name, cost, grade FROM fruit"]
                      {:result-set-fn first}))))

  ;; somewhat "undo" the first extension
  (extend-protocol sql/ISQLValue
    clojure.lang.Keyword
    (sql-value [k] k)))

(deftest test-sql-parameter
  (extend-protocol sql/ISQLParameter
    clojure.lang.Keyword
    (set-parameter [v ^java.sql.PreparedStatement s ^long i]
      (if (= :twelve v)
        (.setLong   s i 12)
        (.setString s i (str (name v) i)))))

  (doseq [db (test-specs)]
    (create-test-table :fruit db)
    (sql/insert! db
                 :fruit
                 [:name :cost :grade]
                 [:test :twelve nil])
    (is (= {:name "test1", :cost 12, :grade nil}
           (sql/query db ["SELECT name, cost, grade FROM fruit"]
                      {:result-set-fn first}))))

  ;; somewhat "undo" the first extension
  (extend-protocol sql/ISQLParameter
    clojure.lang.Keyword
    (set-parameter [v ^java.sql.PreparedStatement s ^long i]
      (.setObject s i (sql/sql-value v)))))
