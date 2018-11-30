;;  Copyright (c) 2008-2018 Sean Corfield, Stephen C. Gilardi. All rights reserved.
;;  The use and distribution terms for this software are covered by
;;  the Eclipse Public License 1.0
;;  (http://opensource.org/licenses/eclipse-1.0.php) which can be
;;  found in the file epl-v10.html at the root of this distribution.
;;  By using this software in any fashion, you are agreeing to be
;;  bound by the terms of this license.  You must not remove this
;;  notice, or any other, from this software.
;;
;;  datafy.clj
;;
;;  Exploring datafy and nav for JDBC databases

(ns
    ^{:author "Sean Corfield",
      :doc "Variants of 'query' functions from clojure.java.jdbc that support
           the new clojure.datafy functionality in Clojure 1.10."}
  clojure.java.jdbc.datafy
  (:require [clojure.core.protocols :as p]
            [clojure.datafy :as d]
            [clojure.java.jdbc :as jdbc]))

(declare datafy-result-set)
(declare datafy-row)

(defn- default-schema
  "The default schema lookup rule for column names.

  If a column name ends with _id or id, it is assumed to be a foreign key
  into the table identified by the first part of the column name."
  [col]
  (let [[_ table] (re-find #"^(.*)_?id$" (name col))]
    (when table
      [(keyword table) :id])))

(defn- schema-opt
  "Returns the schema 'option'.

  As with other clojure.java.jdbc options, it can be provided in the db-spec
  hash map or in the options for a particular function call.

  A schema can a simple map from column names to pairs of table name and
  the key column to be used in that table. A schema can also be a function
  that is called with column names and should return nil if the column is
  not to be treated as a foreign key, or a pair of table name and the key
  column within that table."
  [db-spec opts]
  (:schema (merge {:schema default-schema}
                  (when (map? db-spec) db-spec)
                  opts)))

(defn- navize-row [db-spec opts row]
  "Given a db-spec, a map of options, and a row -- a hash map -- return the
  row with metadata that provides navigation via foreign keys."
  (let [schema (schema-opt db-spec opts)]
    (with-meta row
         {`p/nav (fn [coll k v]
                   (let [[table fk cardinality] (schema k)]
                     (if fk
                       (if (= :many cardinality)
                         (datafy-result-set db-spec opts
                                            (jdbc/find-by-keys db-spec table {fk v}))
                         (datafy-row db-spec opts
                                     (jdbc/get-by-id db-spec table v fk)))
                       v)))})))

(defn- datafy-row [db-spec opts row]
  "Given a db-spec, a map of options, and a row -- a hash map -- return the
  row with metadata that provides datafication (which in turn provides).
  navigation)."
  (with-meta row {`p/datafy (partial navize-row db-spec opts)}))

(defn- datafy-result-set [db-spec opts rs]
  "Given a db-spec, a map of options, and a result set -- a sequence of hash
  maps that represent rows -- return a sequence of datafiable rows."
  (mapv (partial datafy-row db-spec opts) rs))

(defn get-by-id
  "Given a database connection, a table name, a primary key value, an
  optional primary key column name, and an optional options map, return
  a single matching row, or nil.
  The primary key column name defaults to :id."
  ([db table pk-value] (get-by-id db table pk-value :id {}))
  ([db table pk-value pk-name-or-opts]
   (if (map? pk-name-or-opts)
     (get-by-id db table pk-value :id pk-name-or-opts)
     (get-by-id db table pk-value pk-name-or-opts {})))
  ([db table pk-value pk-name opts]
   (datafy-row db opts
               (jdbc/get-by-id db table pk-value pk-name opts))))

(defn find-by-keys
  "Given a database connection, a table name, a map of column name/value
  pairs, and an optional options map, return any matching rows.

  An :order-by option may be supplied to sort the rows, e.g.,

      {:order-by [{:name :asc} {:age :desc} {:income :asc}]}
      ;; equivalent to:
      {:order-by [:name {:age :desc} :income]}

  The :order-by value is a sequence of column names (to sort in ascending
  order) and/or maps from column names to directions (:asc or :desc). The
  directions may be strings or keywords and are not case-sensitive. They
  are mapped to ASC or DESC in the generated SQL.

  Note: if a ordering map has more than one key, the order of the columns
  in the generated SQL ORDER BY clause is unspecified (so such maps should
  only contain one key/value pair)."
  ([db table columns] (find-by-keys db table columns {}))
  ([db table columns opts]
   (datafy-result-set db opts
                      (jdbc/find-by-keys db table columns opts))))

(defn query
  "Given a database connection and a vector containing SQL and optional parameters,
  perform a simple database query. The options specify how to construct the result
  set (and are also passed to prepare-statement as needed):
    :as-arrays? - return the results as a set of arrays, default false.
    :identifiers - applied to each column name in the result set, default lower-case
    :keywordize? - defaults to true, can be false to opt-out of converting
        identifiers to keywords
    :qualifier - optionally provides the namespace qualifier for identifiers
    :result-set-fn - applied to the entire result set, default doall / vec
        if :as-arrays? true, :result-set-fn will default to vec
        if :as-arrays? false, :result-set-fn will default to doall
    :row-fn - applied to each row as the result set is constructed, default identity
  The second argument is a vector containing a SQL string or PreparedStatement, followed
  by any parameters it needs.
  See also prepare-statement for additional options."
  ([db sql-params] (query db sql-params {}))
  ([db sql-params opts]
   (datafy-result-set db opts
                      (jdbc/query db sql-params opts))))

(comment
  (def db-spec {:dbtype "derby" :dbname "datafy" :create true})
  (jdbc/db-do-commands db-spec (jdbc/create-table-ddl :fruit [[:id :int] [:name "varchar(256)"]]))
  (jdbc/db-do-commands db-spec (jdbc/create-table-ddl :fruit2 [[:fruitid :int] [:name "varchar(256)"]]))
  (jdbc/insert! db-spec :fruit {:id 1 :name "First fruit"})
  (jdbc/insert! db-spec :fruit2 {:fruitid 1 :name "First fruit"})
  (jdbc/insert! db-spec :fruit {:id 2 :name "Second fruit"})
  (jdbc/insert! db-spec :fruit2 {:fruitid 2 :name "More fruit"}))
