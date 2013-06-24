;;  Copyright (c) Stephen C. Gilardi. All rights reserved.  The use and
;;  distribution terms for this software are covered by the Eclipse Public
;;  License 1.0 (http://opensource.org/licenses/eclipse-1.0.php) which can
;;  be found in the file epl-v10.html at the root of this distribution.  By
;;  using this software in any fashion, you are agreeing to be bound by the
;;  terms of this license.  You must not remove this notice, or any other,
;;  from this software.
;;
;;  test_ddl.clj
;;
;;  This namespace contains tests of all the functions within
;;  java.jdbc.ddl and does not rely on any databases.
;;
;;  scgilardi (gmail)
;;  Created 13 September 2008
;;
;;  seancorfield (gmail)
;;  Migrated from clojure.contrib.test-sql 17 April 2011
;;
;;  moquist (gmail)
;;  Migrated test-create-table from clojure.java.test-utilities, added
;;  new tests. 23 June 2013

(ns clojure.java.test-ddl
  (:use clojure.test)
  (:require [clojure.java.jdbc.ddl :as ddl]))

(deftest test-create-table
  (is (= "CREATE TABLE table (col1 int, col2 int)"
         (ddl/create-table :table ["col1 int"] [:col2 :int])))
  (is (= "CREATE TABLE table (col1 int, col2 int) ENGINE=MyISAM"
         (ddl/create-table :table [:col1 "int"] ["col2" :int] :table-spec "ENGINE=MyISAM"))))

(deftest test-drop-table
  (is (= "DROP TABLE table")
      (ddl/drop-table :table)))

(deftest test-create-index
  (is (= "CREATE  INDEX index ON table (col1, col2)"
         (ddl/create-index :index :table [:col1 "col2"])))
  (is (= "CREATE UNIQUE INDEX index ON table (col1, col2)"
         (ddl/create-index :index :table [:col1 "col2"] :unique))))

(deftest test-drop-index
  (is (= "DROP INDEX index")
      (ddl/drop-index :index)))
