;;  Copyright (c) Sean Corfield. All rights reserved. The use and
;;  distribution terms for this software are covered by the Eclipse Public
;;  License 1.0 (http://opensource.org/licenses/eclipse-1.0.php) which can
;;  be found in the file epl-v10.html at the root of this distribution.  By
;;  using this software in any fashion, you are agreeing to be bound by the
;;  terms of this license.  You must not remove this notice, or any other,
;;  from this software.
;;
;;  ddl.clj
;;
;;  A basic DDL DSL for use with clojure.java.jdbc (or you can use any
;;  other DDL DSL you want to...)
;;
;;  seancorfield (gmail)
;;  December 2013

(ns
  ^{:author "Sean Corfield",
    :doc "An optional DSL for generating DDL.

Intended to be used with clojure.java.jdbc, this provides a simple DSL -
Domain Specific Language - that generates raw DDL strings. Any other DSL
can be used instead. This DSL is entirely optional and is deliberately
not very sophisticated." }
  clojure.java.jdbc.ddl
  (:require [clojure.java.jdbc.sql :as sql]))

(defn create-table
  "Given a table name and column specs with an optional table-spec
   return the DDL string for creating that table."
  [name & specs]
  (let [split-specs (partition-by #(= :table-spec %) specs)
        col-specs (first split-specs)
        table-spec (first (second (rest split-specs)))
        table-spec-str (or (and table-spec (str " " table-spec)) "")
        specs-to-string (fn [specs]
                          (apply str
                                 (map (sql/as-str identity)
                                      (apply concat
                                             (interpose [", "]
                                                        (map (partial interpose " ") specs))))))]
    (format "CREATE TABLE %s (%s)%s"
            (sql/as-str identity name)
            (specs-to-string col-specs)
            table-spec-str)))

(defn drop-table
  "Given a table name, return the DDL string for dropping that table."
  [name]
  (format "DROP TABLE %s" (sql/as-str identity name)))

(defn create-index
  "Given an index name, table name, vector of column names, and
  (optional) is-unique, return the DDL string for creating an index.

   Examples:
   (create-index :indexname :tablename [:field1 :field2] :unique)
   \"CREATE UNIQUE INDEX indexname ON tablename (field1, field2)\"

   (create-index :indexname :tablename [:field1 :field2])
   \"CREATE INDEX indexname ON tablename (field1, field2)\""
  [index-name table-name cols & is-unique]
  (let [cols-string (apply str
                           (interpose ", "
                                      (map (sql/as-str identity)
                                           cols)))
        is-unique (if is-unique "UNIQUE " "")]
    (format "CREATE %sINDEX %s ON %s (%s)"
            is-unique
            (sql/as-str identity index-name)
            (sql/as-str identity table-name)
            cols-string)))

(defn drop-index
  "Given an index name, return the DDL string for dropping that index."
  [name]
  (format "DROP INDEX %s" (sql/as-str identity name)))

