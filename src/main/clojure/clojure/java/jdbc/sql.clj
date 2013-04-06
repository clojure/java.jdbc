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

(defn- col-str
  "Transform a column spec to an entity name for SQL. The column spec may be a
  string, a keyword or a map with a single pair - column name and alias."
  [col entities]
  (if (map? col)
    (let [[k v] (first col)]
      (str (as-identifier k entities) " AS " (as-identifier v entities)))
    (as-identifier col entities)))

(defn- table-str
  "Transform a table spec to an entity name for SQL. The table spec may be a
  string, a keyword or a map with a single pair - table name and alias."
  [table entities]
  (if (map? table)
    (let [[k v] (first table)]
      (str (as-identifier k entities) " " (as-identifier v entities)))
    (as-identifier table entities)))

(def ^{:private true
       :doc "Symbols that need to be processed for entities within their forms."}
  entity-symbols
  #{"delete" "delete!"
    "insert" "insert!"
    "select" "join" "where" "order-by"
    "update" "update!"})

(def ^{:private true
       :doc "Symbols that need to be processed for identifiers within their forms."}
  identifier-symbols
  #{"query"})

(defn- order-direction
  "Transform a column order spec to an order by entity for SQL. The order spec may be a
  string, a keyword or a map with a single pair - column name and direction. If the order
  spec is not a map, the default direction is ascending."
  [col entities]
  (if (map? col)
    (str (as-identifier (first (keys col)) entities)
         " "
         (let [dir (first (vals col))]
           (get {:asc "ASC" :desc "DESC"} dir dir)))
    (str (as-identifier col entities) " ASC")))

(defn- insert-multi-row
  "Given a table and a list of columns, followed by a list of column value sequences,
  return a vector of the SQL needed for the insert followed by the list of column
  value sequences. The entities function specifies how column names are transformed."
  [table columns values entities]
  (let [nc (count columns)
        vcs (map count values)]
    (if (not (and (or (zero? nc) (= nc (first vcs))) (apply = vcs)))
      (throw (IllegalArgumentException. "insert called with inconsistent number of columns / values"))
      (into [(str "INSERT INTO " (table-str table entities)
                  (when (seq columns)
                    (str " ( "
                         (str/join ", " (map (fn [col] (col-str col entities)) columns))
                         " )"))
                  " VALUES ( "
                  (str/join ", " (repeat (first vcs) "?"))
                  " )")]
            values))))

(defn- insert-single-row
  "Given a table and a map representing a row, return a vector of the SQL needed for
  the insert followed by the list of column values. The entities function specifies
  how column names are transformed."
  [table row entities]
  (let [ks (keys row)]
    (into [(str "INSERT INTO " (table-str table entities) " ( "
                (str/join ", " (map (fn [col] (col-str col entities)) ks))
                " ) VALUES ( "
                (str/join ", " (repeat (count ks) "?"))
                " )")]
          (vals row))))

;; quoting strategy helpers

(defmacro entities
  "Given an entities function and a SQL-generating DSL form, transform the DSL form
  to inject an :entities keyword argument with the function at the end of each appropriate
  form."
  [entities sql]
  (walk/postwalk (fn [form]
                   (if (and (seq? form)
                            (symbol? (first form))
                            (entity-symbols (name (first form))))
                     (concat form [:entities entities])
                     form)) sql))

(defmacro identifiers
  "Given an identifiers function and a SQL-generating DSL form, transform the DSL form
  to inject an :identifiers keyword argument with the function at the end of each
  appropriate form."
  [identifiers sql]
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

(defn delete
  "Given a table name, a where class and its parameters and an optional entities spec,
  return a vector of the SQL for that delete operation followed by its parameters. The
  entities spec (default 'as-is') specifies how to transform column names."
  [table [where & params] & {:keys [entities] :or {entities as-is}}]
  (into [(str "DELETE FROM " (table-str table entities)
              (when where " WHERE ") where)]
        params))

(defn insert
  "Given a table name and either column names and values or maps representing rows, retun
  return a vector of the SQL for that insert operation followed by its parameters. An
  optional entities spec (default 'as-is') specifies how to transform column names."
  [table & clauses]
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

(defn join
  "Given a table name and a map of how to join it (to the existing SQL fragment),
  retun the SQL string for the JOIN clause. The optional entities spec (default 'as-is')
  specifies how to transform column names."
  [table on-map & {:keys [entities] :or {entities as-is}}]
  (str "JOIN " (table-str table entities) " ON "
       (str/join
        " AND "
        (map (fn [[k v]] (str (as-identifier k entities) " = " (as-identifier v entities))) on-map))))

(defn order-by
  "Given a sequence of column order specs, and an optional entities spec, return the
  SQL string for the ORDER BY clause. A column order spec may be a column name or a
  map of the column name to the desired order."
  [cols & {:keys [entities] :or {entities as-is}}]
  (str "ORDER BY "
       (if (or (string? cols) (keyword? cols) (map? cols))
         (order-direction cols entities)
         (str/join "," (map #(order-direction % entities) cols)))))

(defn select
  "Given a sequence of column names (or *) and a table name, followed by optional SQL
  clauses, return a vector for the SQL followed by its parameters. The general form is:
    (select [columns] table joins [where params] order-by options)
  where joins are optional strings, as is order-by, and the where clause is a vector
  of a where SQL clause followed by its parameters. The options may may include an
  entities spec to specify how column names should be transformed.
  The intent is that the joins, where clause and order by clause are generated by
  other parts of the DSL:
    (select * {:person :p}
            (join {:address :a} {:p.addressId :a.id})
            (where {:a.zip 94546})
            (order-by :p.name))"
  [col-seq table & clauses]
  (let [joins (take-while string? clauses)
        where-etc (drop (count joins) clauses)
        [where-clause & more] where-etc
        [where & params] (when-not (keyword? where-clause) where-clause)
        order-etc (if (keyword? where-clause) where-etc more)
        [order-clause & more] order-etc
        order-by (when (string? order-clause) order-clause)
        options (if order-by more order-etc)
        {:keys [entities] :or {entities as-is}} (apply hash-map options)]
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

(defn update
  "Given a table name and a map of columns to set, and optional map of columns to
  match (and an optional entities spec), return a vector of the SQL for that update
  followed by its parameters. Example:
    (update :person {:zip 94540} (where {:zip 94546}))
  returns:
    [\"UPDATE person SET zip = ? WHERE zip = ?\" 94540 94546]"
  [table set-map & where-etc]
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

(defn where
  "Given a map of columns and values, return a vector containing the where clause SQL
  followed by its parameters. Example:
    (where {:a 42 :b nil})
  returns:
    [\"a = ? AND b IS NULL\" 42]"
  [param-map & {:keys [entities] :or {entities as-is}}]
  (let [ks (keys param-map)
        vs (vals param-map)]
    (cons (str/join
           " AND "
           (map (fn [k v]
                  (str (as-identifier k entities)
                       (if (nil? v) " IS NULL" " = ?")))
                ks vs))
          (remove nil? vs))))
