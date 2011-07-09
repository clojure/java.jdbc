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
;;  test/example for clojure.java.jdbc
;;
;;  most of this file is still example code (which has been duplicated to
;;  the documentation in doc/clojure/java/jdbc/) but actual tests are being
;;  added near the top of the file
;;
;;  scgilardi (gmail)
;;  Created 13 September 2008
;;
;;  seancorfield (gmail)
;;  Migrated from clojure.contrib.test-sql 17 April 2011

(ns clojure.java.test-jdbc
  (:use clojure.test)
  (:require [clojure.java.jdbc :as sql]))

;; basic tests for keyword / entity conversion

(deftest test-as-identifier
  (is (= "kw" (sql/as-identifier "kw")))
  (is (= "kw" (sql/as-identifier :kw)))
  (is (= "KW" (sql/as-identifier "KW")))
  (is (= "KW" (sql/as-identifier :KW))))

(deftest test-as-keyword
  (is (= :kw (sql/as-keyword "kw")))
  (is (= :kw (sql/as-keyword :kw)))
  (is (= :kw (sql/as-keyword "KW")))
  (is (= :KW (sql/as-keyword :KW))))

(deftest test-quoted
  (is (= "kw" (sql/as-quoted-identifier [ \[ \] ] "kw")))
  (is (= "[kw]" (sql/as-quoted-identifier [ \[ \] ] :kw)))
  (is (= "KW" (sql/as-quoted-identifier \` "KW")))
  (is (= "`KW`" (sql/as-quoted-identifier \` :KW))))

(def quote-dash { :entity (partial sql/as-quoted-str \`) :keyword #(.replace % "_" "-") })

(deftest test-named
  (is (= "kw" (sql/as-named-identifier quote-dash "kw")))
  (is (= "`kw`" (sql/as-named-identifier quote-dash :kw)))
  (is (= :K-W (sql/as-named-keyword quote-dash "K_W")))
  (is (= :K_W (sql/as-named-keyword quote-dash :K_W))))

(deftest test-with-quote
  (sql/with-quoted-identifiers [ \[ \] ]
    (is (= "kw" (sql/as-identifier "kw")))
    (is (= "[kw]" (sql/as-identifier :kw)))
    (is (= "KW" (sql/as-identifier "KW")))
    (is (= "[KW]" (sql/as-identifier :KW)))))

(deftest test-with-naming
  (sql/with-naming-strategy quote-dash
    (is (= "kw" (sql/as-identifier "kw")))
    (is (= "`kw`" (sql/as-identifier :kw)))
    (is (= :K-W (sql/as-keyword "K_W")))
    (is (= :K_W) (sql/as-keyword :K_W))))

(deftest test-print-update-counts
  (let [bu-ex (java.sql.BatchUpdateException. (int-array [1 2 3]))]
    (let [e (is (thrown? java.sql.BatchUpdateException (throw bu-ex)))
          counts-str (with-out-str (sql/print-update-counts e))]
      (is (re-find #"^Update counts" counts-str))
      (is (re-find #"Statement 0: 1" counts-str))
      (is (re-find #"Statement 2: 3" counts-str)))))

(deftest test-print-exception-chain
  (let [base-ex (java.sql.SQLException. "Base Message" "Base State")
        test-ex (java.sql.BatchUpdateException. "Test Message" "Test State" (int-array [1 2 3]))]
    (.setNextException test-ex base-ex)
    (let [e (is (thrown? java.sql.BatchUpdateException (throw test-ex)))
          except-str (with-out-str (sql/print-sql-exception-chain e))
          pattern (fn [s] (java.util.regex.Pattern/compile s java.util.regex.Pattern/DOTALL))]
      (is (re-find (pattern "^BatchUpdateException:.*SQLException:") except-str))
      (is (re-find (pattern "Message: Test Message.*Message: Base Message") except-str))
      (is (re-find (pattern "SQLState: Test State.*SQLState: Base State") except-str)))))

;; DDL tests

(deftest test-create-table-ddl
  (is (= "CREATE TABLE table (col1 int, col2 int)"
         (sql/create-table-ddl :table ["col1 int"] [:col2 :int])))
  (is (= "CREATE TABLE table (col1 int, col2 int) ENGINE=MyISAM"
         (sql/create-table-ddl :table [:col1 "int"] ["col2" :int] :table-spec "ENGINE=MyISAM"))))

;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; old example code below here - will eventually be removed once proper tests are written!

(def db {:classname "org.apache.derby.jdbc.EmbeddedDriver"
         :subprotocol "derby"
         :subname "/tmp/clojure.java.test-jdbc.db"
         :create true})

(defn create-fruit
  "Create a table"
  []
  (sql/create-table
   :fruit
   [:name "varchar(32)" "PRIMARY KEY"]
   [:appearance "varchar(32)"]
   [:cost :int]
   [:grade :real]))

(defn drop-fruit
  "Drop a table"
  []
  (try
   (sql/drop-table :fruit)
   (catch Exception _)))

(defn insert-rows-fruit
  "Insert complete rows"
  []
  (sql/insert-rows
   :fruit
   ["Apple" "red" 59 87]
   ["Banana" "yellow" 29 92.2]
   ["Peach" "fuzzy" 139 90.0]
   ["Orange" "juicy" 89 88.6]))

(defn insert-values-fruit
  "Insert rows with values for only specific columns"
  []
  (sql/insert-values
   :fruit
   [:name :cost]
   ["Mango" 722]
   ["Feijoa" 441]))

(defn insert-records-fruit
  "Insert records, maps from keys specifying columns to values"
  []
  (sql/insert-records
   :fruit
   {:name "Pomegranate" :appearance "fresh" :cost 585}
   {:name "Kiwifruit" :grade 93}))

(defn db-write
  "Write initial values to the database as a transaction"
  []
  (sql/with-connection db
    (sql/transaction
     (drop-fruit)
     (create-fruit)
     (insert-rows-fruit)
     (insert-values-fruit)
     (insert-records-fruit)))
  nil)

(defn db-read
  "Read the entire fruit table"
  []
  (sql/with-connection db
    (sql/with-query-results res
      ["SELECT * FROM fruit"]
      (doseq [rec res]
        (println rec)))))

(defn db-update-appearance-cost
  "Update the appearance and cost of the named fruit"
  [name appearance cost]
  (sql/update-values
   :fruit
   ["name=?" name]
   {:appearance appearance :cost cost}))

(defn db-update
  "Update two fruits as a transaction"
  []
  (sql/with-connection db
    (sql/transaction
     (db-update-appearance-cost "Banana" "bruised" 14)
     (db-update-appearance-cost "Feijoa" "green" 400)))
  nil)

(defn db-update-or-insert
  "Updates or inserts a fruit"
  [record]
  (sql/with-connection db
    (sql/update-or-insert-values
     :fruit
     ["name=?" (:name record)]
     record)))

(defn db-read-all
  "Return all the rows of the fruit table as a vector"
  []
  (sql/with-connection db
    (sql/with-query-results res
      ["SELECT * FROM fruit"]
      (into [] res))))

(defn db-grade-range
  "Print rows describing fruit that are within a grade range"
  [min max]
  (sql/with-connection db
    (sql/with-query-results res
      [(str "SELECT name, cost, grade "
            "FROM fruit "
            "WHERE grade >= ? AND grade <= ?")
       min max]
      (doseq [rec res]
        (println rec)))))

(defn db-grade-a 
  "Print rows describing all grade a fruit (grade between 90 and 100)"
  []
  (db-grade-range 90 100))

(defn db-get-tables
  "Demonstrate getting table info"
  []
  (sql/with-connection db
    (into []
          (sql/resultset-seq
           (-> (sql/connection)
               (.getMetaData)
               (.getTables nil nil nil (into-array ["TABLE" "VIEW"])))))))

(defn db-exception
  "Demonstrate rolling back a partially completed transaction on exception"
  []
  (sql/with-connection db
    (sql/transaction
     (sql/insert-values
      :fruit
      [:name :appearance]
      ["Grape" "yummy"]
      ["Pear" "bruised"])
     ;; at this point the insert-values call is complete, but the transaction
     ;; is not. the exception will cause it to roll back leaving the database
     ;; untouched.
     (throw (Exception. "sql/test exception")))))

(defn db-sql-exception
  "Demonstrate an sql exception"
  []
  (sql/with-connection db
    (sql/transaction
     (sql/insert-values
      :fruit
      [:name :appearance]
      ["Grape" "yummy"]
      ["Pear" "bruised"]
      ["Apple" "strange" "whoops"]))))

(defn db-batchupdate-exception
  "Demonstrate a batch update exception"
  []
  (sql/with-connection db
    (sql/transaction
     (sql/do-commands
      "DROP TABLE fruit"
      "DROP TABLE fruit"))))

(defn db-rollback
  "Demonstrate a rollback-only trasaction"
  []
  (sql/with-connection db
    (sql/transaction
     (prn "is-rollback-only" (sql/is-rollback-only))
     (sql/set-rollback-only)
     (sql/insert-values
      :fruit
      [:name :appearance]
      ["Grape" "yummy"]
      ["Pear" "bruised"])
     (prn "is-rollback-only" (sql/is-rollback-only))
     (sql/with-query-results res
       ["SELECT * FROM fruit"]
       (doseq [rec res]
         (println rec))))
    (prn)
    (sql/with-query-results res
      ["SELECT * FROM fruit"]
      (doseq [rec res]
        (println rec)))))
