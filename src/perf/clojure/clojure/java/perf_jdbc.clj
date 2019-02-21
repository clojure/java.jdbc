;;  Copyright (c) 2017 Tommmi Reiman, Sean Corfield. All rights reserved.
;;  The use and distribution terms for this software are covered by
;;  the Eclipse Public License 1.0
;;  (http://opensource.org/licenses/eclipse-1.0.php) which can be
;;  found in the file epl-v10.html at the root of this distribution.
;;  By using this software in any fashion, you are agreeing to be
;;  bound by the terms of this license.  You must not remove this
;;  notice, or any other, from this software.

(ns clojure.java.perf-jdbc
  "Performance tests for parts of clojure.java.jdbc.

  Here's how to run these tests:

  $ clj -A:test:perf
  Clojure 1.9.0
  user=> (require '[clojure.java.perf-jdbc :as p])
  nil
  user=> (p/calibrate)
  ...
  nil
  user=> (p/test-dummy)
  ...
  nil
  user=> (p/test-h2)
  ...
  nil
  user=>

  These test compare the raw performance (against an in-memory H2 database)
  for hand-crafted Java JDBC calls and various `query` and `reducible-query`
  calls."
  (:require [criterium.core :as cc]
            [clojure.java.jdbc :as sql])
  (:import (java.sql Connection PreparedStatement ResultSet Statement ResultSetMetaData)))

(defn calibrate []
  ;; 840ms
  (cc/quick-bench (reduce + (take 10e6 (range)))))

(def db
  "Note: loading this namespace creates a connection to the H2 database!"
  {:connection (sql/get-connection "jdbc:h2:mem:test_mem")})

(defn create-table! [db]
  (sql/db-do-commands
    db (sql/create-table-ddl
         :fruit
         [[:id :int "DEFAULT 0"]
          [:name "VARCHAR(32)" "PRIMARY KEY"]
          [:appearance "VARCHAR(32)"]
          [:cost :int]
          [:grade :real]]
         {:table-spec ""})))

(defn- drop-table! [db]
  (doseq [table [:fruit :fruit2 :veggies :veggies2]]
    (try
      (sql/db-do-commands db (sql/drop-table-ddl table))
      (catch java.sql.SQLException _))))

(defn add-stuff! [db]
  (sql/insert-multi! db
                     :fruit
                     nil
                     [[1 "Apple" "red" 59 87]
                      [2 "Banana" "yellow" 29 92.2]
                      [3 "Peach" "fuzzy" 139 90.0]
                      [4 "Orange" "juicy" 89 88.6]]))

(def dummy-con
  (reify
    Connection
    (createStatement [_]
      (reify
        Statement
        (addBatch [_ _])))
    (prepareStatement [_ _]
      (reify
        PreparedStatement
        (setObject [_ _ _])
        (setString [_ _ _])
        (close [_])
        (executeQuery [_]
          (reify
            ResultSet
            (getMetaData [_]
              (reify
                ResultSetMetaData
                (getColumnCount [_] 1)
                (getColumnLabel [_ _] "name")))
            (next [_] true)
            (close [_])
            (^Object getObject [_ ^int s]
              "Apple")
            (^Object getObject [_ ^String s]
              "Apple")
            (^String getString [_ ^String s]
              "Apple")))))))

(defn select [db]
  (sql/query db ["SELECT * FROM fruit WHERE appearance = ?" "red"]
             {:row-fn :name :result-set-fn first}))

(defn select-p [db ps]
  (sql/query db [ps "red"]
             {:row-fn :name :result-set-fn first}))

(defn select* [^Connection con]
  (let [ps (doto (.prepareStatement con "SELECT * FROM fruit WHERE appearance = ?")
             (.setObject 1 "red"))
        rs (.executeQuery ps)
        _ (.next rs)
        value (.getObject rs "name")]
    (.close ps)
    value))

(defn test-dummy []
  (do
    (let [db {:connection dummy-con}]
      (assert (= "Apple" (select db)))
      ;(time (dotimes [_ 100000] (select db)))

      ; 3.029268 ms (3030 ns)
      (cc/quick-bench (dotimes [_ 1000] (select db))))

    (let [con dummy-con]
      (assert (= "Apple" (select* con)))
      ;(time (dotimes [_ 100000] (select* con)))

      ;  716.661522 ns (0.7ns) -> 4300x faster
      (cc/quick-bench (dotimes [_ 1000] (select* con))))))

(defn test-h2 []
  (do
    (drop-table! db)
    (create-table! db)
    (add-stuff! db)

    (println "Basic select...")
    (let [db db]
      (assert (= "Apple" (select db)))
      (cc/quick-bench (select db)))

    (println "Basic select first rs...")
    (let [db db]
      (cc/quick-bench (sql/query db ["SELECT * FROM fruit WHERE appearance = ?" "red"]
                                 {:result-set-fn first :qualifier "fruit"})))

    (println "Select with prepared statement...")
    (let [con (:connection db)]
      (with-open [ps (sql/prepare-statement con "SELECT * FROM fruit WHERE appearance = ?")]
        (assert (= "Apple" (select-p db ps)))
        (cc/quick-bench (select-p db ps))))

    (println "Reducible query...")
    (let [db db
          rq (sql/reducible-query db ["SELECT * FROM fruit WHERE appearance = ?" "red"])]
      (assert (= "Apple" (reduce (fn [_ row] (reduced (:name row)))
                                 nil rq)))
      (cc/quick-bench (reduce (fn [_ row] (reduced (:name row)))
                              nil rq)))

    (println "Reducible query with prepared statement...")
    (let [con (:connection db)]
      (with-open [ps (sql/prepare-statement con "SELECT * FROM fruit WHERE appearance = ?")]
        (let [rq (sql/reducible-query db [ps "red"])]
          (assert (= "Apple" (reduce (fn [_ row] (reduced (:name row)))
                                     nil rq)))
          (cc/quick-bench (reduce (fn [_ row] (reduced (:name row)))
                                  nil rq)))))

    (println "Reducible query with prepared statement and simple identifiers...")
    (let [con (:connection db)]
      (with-open [ps (sql/prepare-statement con "SELECT * FROM fruit WHERE appearance = ?")]
        (let [rq (sql/reducible-query db [ps "red"]
                                      {:keywordize? false :identifiers identity})]
          (assert (= "Apple" (reduce (fn [_ row] (reduced (get row "NAME")))
                                     nil rq)))
          (cc/quick-bench (reduce (fn [_ row] (reduced (get row "NAME")))
                                  nil rq)))))

    (println "Reducible query with prepared statement and raw result set...")
    (let [con (:connection db)]
      (with-open [ps (sql/prepare-statement con "SELECT * FROM fruit WHERE appearance = ?")]
        (let [rq (sql/reducible-query db [ps "red"] {:raw? true})]
          (assert (= "Apple" (reduce (fn [_ row] (reduced (:name row)))
                                     nil rq)))
          (cc/quick-bench (reduce (fn [_ row] (reduced (:name row)))
                                  nil rq)))))

    (println "Reducible query with raw result set...")
    (let [db db
          rq (sql/reducible-query
              db
              ["SELECT * FROM fruit WHERE appearance = ?" "red"]
              {:raw? true})]
      (assert (= "Apple" (reduce (fn [_ row] (reduced (:name row)))
                                 nil rq)))
      (cc/quick-bench (reduce (fn [_ row] (reduced (:name row)))
                              nil rq)))

    (println "Repeated reducible query with raw result set...")
    (let [db db]
      (assert (= "Apple" (reduce (fn [_ row] (reduced (:name row)))
                                 nil (sql/reducible-query
                                      db
                                      ["SELECT * FROM fruit WHERE appearance = ?" "red"]
                                      {:raw? true}))))
      (cc/quick-bench (reduce (fn [_ row] (reduced (:name row)))
                              nil (sql/reducible-query
                                   db
                                   ["SELECT * FROM fruit WHERE appearance = ?" "red"]
                                   {:raw? true}))))

    (println "Raw Java...")
    (let [con (:connection db)]
      (assert (= "Apple" (select* con)))
      (cc/quick-bench (select* con)))))

(comment
  (calibrate)
  (test-dummy)
  (test-h2)
  ;; The following are just some things I was double-checking while adding
  ;; to Tommi's original tests -- Sean.
  ;; Shows the row is a reify instance, but you can select by key:
  (reduce (fn [_ row] (println row) (reduced (:name row)))
          nil (sql/reducible-query
               db
               ["SELECT * FROM fruit WHERE appearance = ?" "red"]
               {:raw? true}))
  ;; Shows you can construct a map result using select-keys:
  (reduce (fn [_ row] (reduced (select-keys row [:cost])))
          nil (sql/reducible-query
               db
               ["SELECT * FROM fruit WHERE appearance = ?" "red"]
               {:raw? true}))
  ;; Shows you can reconstruct an entire result set (with very little overhead):
  (transduce (map #(select-keys % [:id :name :cost :appearance :grade]))
             conj [] (sql/reducible-query
                      db
                      "SELECT * FROM fruit"
                      {:raw? true})))
