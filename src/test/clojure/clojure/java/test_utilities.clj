;;  Copyright (c) 2008-2016 Sean Corfield, Stephen C. Gilardi.
;;                                       All rights reserved.  The use and
;;  distribution terms for this software are covered by the Eclipse Public
;;  License 1.0 (http://opensource.org/licenses/eclipse-1.0.php) which can
;;  be found in the file epl-v10.html at the root of this distribution.  By
;;  using this software in any fashion, you are agreeing to be bound by the
;;  terms of this license.  You must not remove this notice, or any other,
;;  from this software.
;;
;;  test_utilities.clj
;;
;;  This namespace contains tests of all the utility functions within java.jdbc
;;  and does not rely on any databases.
;;
;;  scgilardi (gmail)
;;  Created 13 September 2008
;;
;;  seancorfield (gmail)
;;  Migrated from clojure.contrib.test-sql 17 April 2011

(ns clojure.java.test-utilities
  (:use clojure.test)
  (:require [clojure.java.jdbc :as sql]))

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

(deftest test-make-name-unique
  (let [make-name-unique @#'sql/make-name-unique]
    (is (= "a" (make-name-unique '() "a" 1)))
    (is (= "a_2" (make-name-unique '("a") "a" 1)))
    (is (= "a_3" (make-name-unique '("a" "b" "a_2") "a" 1)))))

(deftest test-make-cols-unique
  (let [make-cols-unique @#'sql/make-cols-unique]
    (is (= '() (make-cols-unique '())))
    (is (= '("a") (make-cols-unique '("a"))))
    (is (= '("a" "a_2") (make-cols-unique '("a" "a"))))
    (is (= '("a" "b" "a_2" "a_3") (make-cols-unique '("a" "b" "a" "a"))))
    (is (= '("a" "b" "a_2" "b_2" "a_3" "b_3") (make-cols-unique '("a" "b" "a" "b" "a" "b"))))))

;; DDL tests

(deftest test-create-table-ddl
  (is (= "CREATE TABLE THING (COL1 INT, COL2 int)"
         (sql/create-table-ddl :thing [["col1 int"] [:col2 :int]]
                               {:entities clojure.string/upper-case})))
  (is (= "CREATE TABLE THING (COL1 int, COL2 int) ENGINE=MyISAM"
         (sql/create-table-ddl :thing [[:col1 "int"] ["col2" :int]]
                               {:table-spec "ENGINE=MyISAM"
                                :entities clojure.string/upper-case}))))

;; since we have clojure.spec instrumentation enabled for Clojure 1.9.0
;; we need to account for the fact that we'll get different exceptions
;; for Clojure < 1.9.0 since the spec will trigger first and obscure
;; our own argument checking on 1.9.0+

(defn argument-exception?
  "Given a thunk, try to execute it and return true if it throws
  either an IllegalArgumentException or a Clojure exception from
  clojure.spec."
  [thunk]
  (try
    (thunk)
    false
    (catch IllegalArgumentException _ true)
    (catch clojure.lang.ExceptionInfo e
      (re-find #"did not conform to spec" (.getMessage e)))))

(deftest test-invalid-create-table-ddl
  (is (argument-exception? (fn [] (sql/create-table-ddl :thing [[]]))))
  (is (argument-exception? (fn [] (sql/create-table-ddl :thing [[:col1 "int"] []]))))
  (is (argument-exception? (fn [] (sql/create-table-ddl :thing [:col1 "int"])))))
