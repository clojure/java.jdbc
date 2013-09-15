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
  (:require [clojure.java.jdbc.ddl :as ddl]
            [clojure.java.jdbc.sql :as sql]
            [clojure.string :as str]))

(deftest test-create-table
  (is (= "CREATE TABLE table (col1 int, col2 int)"
         (ddl/create-table :table ["col1 int"] [:col2 :int])))
  (is (= "CREATE TABLE TABLE (col1 int, COL2 INT)"
         (ddl/create-table :table ["col1 int"] [:col2 :int] :entities str/upper-case)))
  (is (= "CREATE TABLE TABLE (col1 int, COL2 INT)"
         (sql/entities str/upper-case
                       (ddl/create-table :table ["col1 int"] [:col2 :int]))))
  (is (= "CREATE TABLE table (col1 int, col2 int) ENGINE=MyISAM"
         (ddl/create-table :table [:col1 "int"] ["col2" :int] :table-spec "ENGINE=MyISAM")))
  (is (= "CREATE TABLE TABLE (COL1 int, col2 INT) ENGINE=MyISAM"
         (ddl/create-table :table [:col1 "int"] ["col2" :int] :entities str/upper-case :table-spec "ENGINE=MyISAM")))
  (is (= "CREATE TABLE TABLE (COL1 int, col2 INT) ENGINE=MyISAM"
         (ddl/create-table :table [:col1 "int"] ["col2" :int] :table-spec "ENGINE=MyISAM" :entities str/upper-case))))

(deftest test-drop-table
  (is (= "DROP TABLE table")
      (ddl/drop-table :table))
  (is (= "DROP TABLE TABLE")
      (ddl/drop-table :table :entities str/upper-case))
  (is (= "DROP TABLE TABLE")
      (sql/entities str/upper-case
                    (ddl/drop-table :table))))

(deftest test-create-index
  (is (= "CREATE INDEX index ON table (col1, col2)"
         (ddl/create-index :index :table [:col1 "col2"])))
  (is (= "CREATE INDEX INDEX ON TABLE (COL1, col2)"
         (ddl/create-index :index :table [:col1 "col2"] :entities str/upper-case)))
  (is (= "CREATE INDEX INDEX ON TABLE (COL1, col2)"
         (sql/entities str/upper-case
                       (ddl/create-index :index :table [:col1 "col2"]))))
  (is (= "CREATE UNIQUE INDEX index ON table (col1, col2)"
         (ddl/create-index :index :table [:col1 "col2"] :unique)))
  (is (= "CREATE UNIQUE INDEX INDEX ON TABLE (COL1, col2)"
         (ddl/create-index :index :table [:col1 "col2"] :unique :entities str/upper-case)))
  (is (= "CREATE UNIQUE INDEX INDEX ON TABLE (COL1, col2)"
         (ddl/create-index :index :table [:col1 "col2"] :entities str/upper-case :unique))))

(deftest test-drop-index
  (is (= "DROP INDEX index")
      (ddl/drop-index :index))
  (is (= "DROP INDEX INDEX")
      (ddl/drop-index :index :entities str/upper-case))
  (is (= "DROP INDEX INDEX")
      (sql/entities str/upper-case
                    (ddl/drop-index :index))))
