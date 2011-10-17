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
;; Possible values so far: [:mysql :derby :hsqldb]
;; Apache Derby and HSQLDB can run without an external setup.

;; The build system does not yet have MySQL available :(
(def test-databases [:derby :hsqldb])

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

(defn- test-specs
  "Return a sequence of db-spec maps that should be used for tests"
  []
  (map (fn [db] 
         (deref (resolve (symbol (str "clojure.java.test-jdbc/" (name db) "-db"))))) 
       test-databases))

(defn- clean-up
  "Attempt to drop any test tables before we start a test."
  [t]
  (doseq [db (test-specs)]
    (sql/with-connection
      db
      (doseq [table [:fruit :fruit2]]
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
      :table-spec (if (= "mysql" p) "ENGINE=InnoDB" ""))))

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
          "mysql" (is (= '({:generated_key 1} {:generated_key 2}) r))
          "hsqldb" (is (= '(1 1) r))
          "derby" (is (= '({:1 nil} {:1 nil}) r))))
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
            ["Apple" "strange" "whoops"]))
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

(deftest test-metadata
  (doseq [db (test-specs)]
    (let [metadata (sql/with-connection
                     db
                     (into []
                           (sql/resultset-seq
                             (-> (sql/connection)
                               (.getMetaData)
                               (.getTables nil nil nil (into-array ["TABLE" "VIEW"]))))))]
      (is (= [] metadata)))))
