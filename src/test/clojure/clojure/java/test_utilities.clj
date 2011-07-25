;;  Copyright (c) Stephen C. Gilardi. All rights reserved.  The use and
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