;;  Copyright (c) Sean Corfield. All rights reserved. The use and
;;  distribution terms for this software are covered by the Eclipse Public
;;  License 1.0 (http://opensource.org/licenses/eclipse-1.0.php) which can
;;  be found in the file epl-v10.html at the root of this distribution.  By
;;  using this software in any fashion, you are agreeing to be bound by the
;;  terms of this license.  You must not remove this notice, or any other,
;;  from this software.
;;
;;  sql.clj
;;
;;  A basic SQL DSL for use with clojure.java.jdbc (or you can use any
;;  other SQL DSL you want to...)
;;
;;  seancorfield (gmail)
;;  December 2012

(ns
  ^{:author "Sean Corfield",
    :doc "An optional DSL for generating SQL.

Intended to be used with clojure.java.jdbc, this provides a simple DSL -
Domain Specific Language - that generates raw SQL strings. Any other DSL
can be used instead. This DSL is entirely optional and is deliberately
not very sophisticated. It is sufficient to support the delete!, insert!
and update! high-level operations within clojure.java.jdbc directly." }
  clojure.java.jdbc.sql
  (:require [clojure.string :as str]
            [clojure.walk :as walk]))

;; implementation utilities

(defn- as-str
  "Given a naming strategy and a keyword, return the keyword as a
   string per that naming strategy. Given (a naming strategy and)
   a string, return it as-is.
   A keyword of the form :x.y is treated as keywords :x and :y,
   both are turned into strings via the naming strategy and then
   joined back together so :x.y might become `x`.`y` if the naming
   strategy quotes identifiers with `."
  [f x]
  (if (instance? clojure.lang.Named x)
    (let [n (name x)
          i (.indexOf n (int \.))]
      (if (= -1 i)
        (f n)
        (str/join "." (map f (.split n "\\.")))))
    (str x)))

(defn- as-identifier
  "Given a keyword, convert it to a string using the current naming
   strategy.
   Given a string, return it as-is."
  [x f-entity]
  (as-str f-entity x))

(defn- as-quoted-str
  "Given a quoting pattern - either a single character or a vector pair of
   characters - and a string, return the quoted string:
     (as-quoted-str X foo) will return XfooX
     (as-quoted-str [A B] foo) will return AfooB"
  [q x]
  (if (vector? q)
    (str (first q) x (last q))
    (str q x q)))

(defn- col-str [col entities]
  (if (map? col)
    (let [[k v] (first col)]
      (str (as-identifier k entities) " AS " (as-identifier v entities)))
    (as-identifier col entities)))

(defn- table-str [table entities]
  (if (map? table)
    (let [[k v] (first table)]
      (str (as-identifier k entities) " " (as-identifier v entities)))
    (as-identifier table entities)))

(def ^:private entity-symbols
  #{"delete" "delete!"
    "insert" "insert!"
    "select" "join" "where" "order-by"
    "update" "update!"})

(def ^:private identifier-symbols
  #{"query"})

(defn- order-direction [col entities]
  (if (map? col)
    (str (as-identifier (first (keys col)) entities)
         " "
         (let [dir (first (vals col))]
           (get {:asc "ASC" :desc "DESC"} dir dir)))
    (str (as-identifier col entities) " ASC")))

(defn- insert-multi-row [table columns values entities]
  (let [nc (count columns)
        vcs (map count values)]
    (if (not (apply = nc vcs))
      (throw (IllegalArgumentException. "insert called with inconsistent number of columns / values"))
      (into [(str "INSERT INTO " (table-str table entities) " ( "
                  (str/join ", " (map (fn [col] (col-str col entities)) columns))
                  " ) VALUES "
                  (str/join ", "
                            (repeat (count values)
                                    (str "( "
                                         (str/join ", " (repeat nc "?"))
                                         " )"))))]
            (apply concat values)))))

(defn- insert-single-row [table row entities]
  (let [ks (keys row)]
    (into [(str "INSERT INTO " (table-str table entities) " ( "
                (str/join ", " (map (fn [col] (col-str col entities)) ks))
                " ) VALUES ( "
                (str/join ", " (repeat (count ks) "?"))
                " )")]
          (vals row))))

;; quoting strategy helpers

(defmacro entities [entities sql]
  (walk/postwalk (fn [form]
                   (if (and (seq? form)
                            (symbol? (first form))
                            (entity-symbols (name (first form))))
                     (concat form [:entities entities])
                     form)) sql))

(defmacro identifiers [identifiers sql]
  (walk/postwalk (fn [form]
                   (if (and (seq? form)
                            (symbol? (first form))
                            (identifier-symbols (name (first form))))
                     (concat form [:identifiers identifiers])
                     form)) sql))

;; some common quoting strategies

(def as-is identity)
(def lower-case str/lower-case)
(defn quoted [q] (partial as-quoted-str q))

;; SQL generation functions

(defn delete [table [where & params] & {:keys [entities] :or {entities as-is}}]
  (into [(str "DELETE FROM " (table-str table entities)
              (when where " WHERE ") where)]
        params))

(defn insert [table & clauses]
  (let [rows (take-while map? clauses)
        n-rows (count rows)
        cols-and-vals-etc (drop n-rows clauses)
        cols-and-vals (take-while (comp not keyword?) cols-and-vals-etc)
        n-cols-and-vals (count cols-and-vals)
        no-cols-and-vals (zero? n-cols-and-vals)
        options (drop (+ (count rows) (count cols-and-vals)) clauses)
        {:keys [entities] :or {entities as-is}} (apply hash-map options)]
    (if (zero? n-rows)
      (if no-cols-and-vals
        (throw (IllegalArgumentException. "insert called without data to insert"))
        (if (< n-cols-and-vals 2)
          (throw (IllegalArgumentException. "insert called with columns but no values"))
          (insert-multi-row table (first cols-and-vals) (rest cols-and-vals) entities)))
      (if no-cols-and-vals
        (map (fn [row] (insert-single-row table row entities)) rows)
        (throw (IllegalArgumentException. "insert may take records or columns and values, not both"))))))

(defn join [table on-map & {:keys [entities] :or {entities as-is}}]
  (str "JOIN " (table-str table entities) " ON "
       (str/join
        " AND "
        (map (fn [[k v]] (str (as-identifier k entities) " = " (as-identifier v entities))) on-map))))

(defn order-by [cols & {:keys [entities] :or {entities as-is}}]
  (str "ORDER BY "
       (if (or (string? cols) (keyword? cols) (map? cols))
         (order-direction cols entities)
         (str/join "," (map #(order-direction % entities) cols)))))

(defn select [col-seq table & clauses]
  (let [joins (take-while string? clauses)
        where-etc (drop (count joins) clauses)
        [where-clause & more] where-etc
        [where & params] (when-not (keyword? where-clause) where-clause)
        order-etc (if (keyword? where-clause) where-etc more)
        [order-clause & more] order-etc
        order-by (when (string? order-clause) order-clause)
        options (if order-by more order-etc)
        {:keys [entities] :or {entities as-is}} (apply hash-map  options)]
    (cons (str "SELECT "
               (cond
                (= * col-seq) "*"
                (or (string? col-seq)
                    (keyword? col-seq)
                    (map? col-seq)) (col-str col-seq entities)
                    :else (str/join "," (map #(col-str % entities) col-seq)))
               " FROM " (table-str table entities)
               (when (seq joins) (str/join " " (cons "" joins)))
               (when where " WHERE ")
               where
               (when order-by " ")
               order-by)
          params)))

(defn update [table set-map & where-etc]
  (let [[where-clause & options] (when-not (keyword? (first where-etc)) where-etc)
        [where & params] where-clause
        {:keys [entities] :or {entities as-is}} (if (keyword? (first where-etc)) where-etc options)
        ks (keys set-map)
        vs (vals set-map)]
    (cons (str "UPDATE " (table-str table entities)
               " SET " (str/join
                        ","
                        (map (fn [k v]
                               (str (as-identifier k entities)
                                    " = "
                                    (if (nil? v) "NULL" "?")))
                             ks vs))
               (when where " WHERE ")
               where)
          (concat (remove nil? vs) params))))

(defn where [param-map & {:keys [entities] :or {entities as-is}}]
  (let [ks (keys param-map)
        vs (vals param-map)]
    (cons (str/join
           " AND "
           (map (fn [k v]
                  (str (as-identifier k entities)
                       (if (nil? v) " IS NULL" " = ?")))
                ks vs))
          (remove nil? vs))))
