;;  Copyright (c) Sean Corfield. All rights reserved.  The use and
;;  distribution terms for this software are covered by the Eclipse Public
;;  License 1.0 (http://opensource.org/licenses/eclipse-1.0.php) which can
;;  be found in the file epl-v10.html at the root of this distribution.  By
;;  using this software in any fashion, you are agreeing to be bound by the
;;  terms of this license.  You must not remove this notice, or any other,
;;  from this software.
;;
;;  test_sql.clj
;;
;;  This namespace contains tests that exercise the optional DSL
;;  portion of java.jdbc.
;;
;;  seancorfield (gmail)
;;  Constructured from tests for jsql DSL December 2012

(ns clojure.java.test-sql
  (:use clojure.test
        clojure.java.jdbc.sql))

(deftest select-dsl
  (is (= ["SELECT * FROM table"] (select * :table)))
  (is (= ["SELECT id FROM table"] (select :id :table)))
  (is (= ["SELECT id,name FROM table"] (select [:id :name] :table)))
  (is (= ["SELECT id AS foo,name FROM table"] (select [{:id :foo} :name] :table)))
  (is (= ["SELECT t.id FROM table t"] (select :t.id {:table :t}))))

(deftest quoting-fns
  (is (= "`a`" ((quoted \`) "a")))
  (is (= "`A`" ((quoted \`) "A")))
  (is (= "a" (as-is "a")))
  (is (= "A" (as-is "A")))
  (is (= "a" (lower-case "a")))
  (is (= "a" (lower-case "A"))))

(deftest select-entities
  (is (= ["SELECT * FROM `table`"] (entities (quoted \`) (select * :table))))
  (is (= ["SELECT * FROM table"] (entities as-is (select * :table))))
  (is (= ["SELECT `t`.`id` FROM `table` `t`"] (entities (quoted \`) (select :t.id {:table :t})))))

(deftest join-dsl
  (is (= "JOIN t ON a.id = t.id" (join :t {:a.id :t.id}))))

(deftest select-join-dsl
  (is (= ["SELECT * FROM a JOIN b ON a.id = b.id"] (select * :a (join :b {:a.id :b.id}))))
  (is (= ["SELECT * FROM a JOIN b ON a.id = b.id JOIN c ON c.id = b.id"]
         (select * :a (join :b {:a.id :b.id}) (join :c {:c.id :b.id})))))

(deftest where-dsl
  (is (= ["id = ?" 42] (where {:id 42})))
  (is (= ["id IS NULL"] (where {:id nil}))))

(deftest select-where-dsl
  (is (#{["SELECT * FROM a WHERE c = ? AND b = ?" 3 2]
         ["SELECT * FROM a WHERE b = ? AND c = ?" 2 3]}
         (select * :a (where {:b 2 :c 3}))))
  (is (#{["SELECT * FROM a WHERE c IS NULL AND b = ?" 2]
         ["SELECT * FROM a WHERE b = ? AND c IS NULL" 2]}
         (select * :a (where {:b 2 :c nil})))))

(deftest order-by-dsl
  (is (= "ORDER BY a ASC" (order-by :a)))
  (is (= "ORDER BY a ASC" (order-by [:a])))
  (is (= "ORDER BY a DESC" (order-by {:a :desc})))
  (is (= "ORDER BY a ASC,b ASC" (order-by [:a :b])))
  (is (= "ORDER BY a DESC,b ASC" (order-by [{:a :desc} :b])))
  (is (= "ORDER BY a DESC,b DESC" (order-by [{:a :desc} {:b :desc}])))
  (is (= "ORDER BY `a` ASC,`b` DESC" (entities (quoted \`) (order-by [{:a :asc} {:b :desc}]))))
  (is (= "ORDER BY `a` ASC,`b` DESC" (order-by [{:a :asc} {:b :desc}] :entities (quoted \`)))))

(deftest select-order-dsl
  (is (= ["SELECT id FROM person ORDER BY name ASC"] (select :id :person (order-by :name))))
  (is (= ["SELECT a FROM b JOIN c ON b.id = c.id ORDER BY d ASC"
          (select :a :b (join :c {:b.id :c.id}) (order-by :d))]))
  (is (= ["SELECT a FROM b WHERE c = ? ORDER BY d ASC" 3]
         (select :a :b (where {:c 3}) (order-by :d))))
  (is (= ["SELECT a FROM b JOIN c ON b.id = c.id WHERE d = ? ORDER BY e ASC" 4]
         (select :a :b (join :c {:b.id :c.id}) (where {:d 4}) (order-by :e)))))

(deftest select-join-alias-dsl
  (is (= ["SELECT a.id,b.name FROM aa a JOIN bb b ON a.id = b.id WHERE b.test = ?" 42]
         (select [:a.id :b.name] {:aa :a}
                 (join {:bb :b} {:a.id :b.id})
                 (where {:b.test 42}))))
  (is (= ["SELECT `a`.`id`,`b`.`name` FROM `aa` `a` JOIN `bb` `b` ON `a`.`id` = `b`.`id` WHERE `b`.`test` = ?" 42]
         (entities (quoted \`)
                   (select [:a.id :b.name] {:aa :a}
                           (join {:bb :b} {:a.id :b.id})
                           (where {:b.test 42}))))))

(deftest update-dsl
  (is (= ["UPDATE a SET b = ?" 2] (update :a {:b 2})))
  (is (= ["UPDATE a SET b = ? WHERE c = ?" 2 3] (update :a {:b 2} (where {:c 3}))))
  (is (= ["UPDATE `a` SET `b` = ? WHERE `c` = ?" 2 3]
         (entities (quoted \`) (update :a {:b 2} (where {:c 3}))))))

(deftest delete-dsl
  (is (= ["DELETE FROM a WHERE b = ?" 2] (delete :a (where {:b 2}))))
  (is (#{["DELETE FROM a WHERE c IS NULL AND b = ?" 2]
         ["DELETE FROM a WHERE b = ? AND c IS NULL" 2]}
         (delete :a (where {:b 2 :c nil}))))
  (is (= ["DELETE FROM `a` WHERE `b` = ?" 2]
         (entities (quoted \`) (delete :a (where {:b 2}))))))

(deftest insert-dsl
  (is (= ["INSERT INTO a ( b ) VALUES ( ? )" [2]] (insert :a [:b] [2])))
  (is (= ["INSERT INTO a VALUES ( ? )" [2]] (insert :a nil [2])))
  (is (= ["INSERT INTO a VALUES ( ? )" [2]] (insert :a [] [2])))
  (is (= [["INSERT INTO a ( b ) VALUES ( ? )" 2]] (insert :a {:b 2})))
  (is (= ["INSERT INTO a ( b ) VALUES ( ? )" [2] [3]] (insert :a [:b] [2] [3])))
  (is (= ["INSERT INTO a VALUES ( ? )" [2] [3]] (insert :a nil [2] [3])))
  (is (= ["INSERT INTO a VALUES ( ? )" [2] [3]] (insert :a [] [2] [3])))
  (is (= ["INSERT INTO a ( b, c, d ) VALUES ( ?, ?, ? )" [2 3 4] [3 4 5]]
         (insert :a [:b :c :d] [2 3 4] [3 4 5])))
  (is (= ["INSERT INTO a VALUES ( ?, ?, ? )" [2 3 4] [3 4 5]]
         (insert :a nil [2 3 4] [3 4 5])))
  (is (= ["INSERT INTO a VALUES ( ?, ?, ? )" [2 3 4] [3 4 5]]
         (insert :a [] [2 3 4] [3 4 5])))
  (is (= [["INSERT INTO a ( b ) VALUES ( ? )" 2]
          ["INSERT INTO a ( b ) VALUES ( ? )" 3]]
         (insert :a {:b 2} {:b 3})))
  (is (= ["INSERT INTO `a` ( `b` ) VALUES ( ? )" [2]]
         (entities (quoted \`) (insert :a [:b] [2]))))
  (is (= ["INSERT INTO `a` VALUES ( ? )" [2]]
         (entities (quoted \`) (insert :a nil [2]))))
  (is (= ["INSERT INTO `a` VALUES ( ? )" [2]]
         (entities (quoted \`) (insert :a [] [2])))))

(deftest bad-insert-args
  (is (thrown? IllegalArgumentException (insert)))
  (is (thrown? IllegalArgumentException (insert :a)))
  (is (thrown? IllegalArgumentException (insert :a [:b])))
  (is (thrown? IllegalArgumentException (insert :a {:b 1} [:c] [2])))
  (is (thrown? IllegalArgumentException (insert :a [:b] [2 3]))))