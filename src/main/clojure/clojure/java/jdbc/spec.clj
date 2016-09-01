;;  Copyright (c) 2016 Sean Corfield. All rights reserved.
;;  The use and distribution terms for this software are covered by
;;  the Eclipse Public License 1.0
;;  (http://opensource.org/licenses/eclipse-1.0.php) which can be
;;  found in the file epl-v10.html at the root of this distribution.
;;  By using this software in any fashion, you are agreeing to be
;;  bound by the terms of this license.  You must not remove this
;;  notice, or any other, from this software.
;;
;;  jdbc/spec.clj
;;
;;  Optional specifications for clojure.java.jdbc

(ns ^{:author "Sean Corfield"
      :doc "Optional specifications for use with Clojure 1.9 or later."}
  clojure.java.jdbc.spec
  (:require [clojure.spec :as s]
            [clojure.java.jdbc :as sql]))

;; basic java.sql types

(s/def ::connection #(instance? java.sql.Connection %))
(s/def ::prepared-statement #(instance? java.sql.PreparedStatement %))
(s/def ::result-set #(instance? java.sql.ResultSet %))
(s/def ::result-set-metadata #(instance? java.sql.ResultSetMetaData %))

;; database specification (connection description)

(s/def ::db-spec-connection (s/keys :req-un [::connection]))
(s/def ::db-spec-friendly (s/keys :req-un [::dbtype ::dbname] :opt-un [::host ::port]))
(s/def ::db-spec-raw (s/keys :req-un [::connection-uri]))
(s/def ::db-spec-driver-manager (s/keys :req-un [::subprotocol ::subname] :opt-un [::classname]))
(s/def ::db-spec-factory (s/keys :req-un [::factory]))
(s/def ::db-spec-data-source (s/keys :req-un [::datasource] :opt-un [::username ::user ::password]))
(s/def ::db-spec-jndi (s/keys :req-un [::name] :opt-un [::environment]))
(s/def ::db-spec-string string?)
(s/def ::db-spec (s/or :connection ::db-spec-connection
                       :friendly   ::db-spec-friendly
                       :raw        ::db-spec-raw
                       :driver-mgr ::db-spec-driver-manager
                       :factory    ::db-spec-factory
                       :datasource ::db-spec-data-source
                       :jndi       ::db-spec-jndi
                       :uri        ::db-spec-string))

;; naming

(s/def ::entity string?)

(s/def ::identifier (s/or :kw keyword? :s string?))

;; SQL and parameters

(s/def ::sql-stmt (s/or :sql string? :stmt ::prepared-statement))

(s/def ::sql-value any?) ;; for now

(s/def ::sql-params (s/or :sql        ::sql-stmt
                          :sql-params (s/cat :sql ::sql-stmt :params (s/* ::sql-value))))

(s/def ::where-clause (s/cat :where string? :params (s/* ::sql-value)))

;; results

(s/def ::execute-result (s/* integer?))

;; specific options that can be passed
;; a few of them are nilable, where the functions either pass a possibly nil
;; version of the option to a called function, but most are not nilable because
;; the corresponding options must either be omitted or given valid values

(s/def ::as-arrays? (s/nilable #{:cols-as-is true false}))
(s/def ::concurrency (set (keys @#'sql/result-set-concurrency)))
(s/def ::cursors (set (keys @#'sql/result-set-holdability)))
(s/def ::fetch-size nat-int?)
;; note the asymmetry here: the identifiers function converts a SQL entity to
;; an identifier (a symbol or a string), whereas the entities function converts
;; a string (not an identifier) to a SQL entity; SQL entities are always strings
;; but whilst java.jdbc lets you produce a keyword from identifiers, it does not
;; assume that entities can accept keywords!
(s/def ::identifiers (s/fspec :args (s/cat :s ::entity)
                              :ret  ::identifier))
(s/def ::entities (s/fspec :args (s/cat :s string?)
                           :ret  ::entity))
(s/def ::max-size nat-int?)
(s/def ::multi? boolean?)
;; strictly speaking we accept any keyword or string whose upper case name
;; is either ASC or DESC so this spec is overly restrictive; the :id-dir
;; can actually be an empty map although that is not very useful
(s/def ::direction #{:asc :desc "asc" "desc" "ASC" "DESC"})
(s/def ::column-direction (s/or :id ::identifier
                                :id-dir (s/map-of ::identifier ::direction)))
(s/def ::order-by (s/coll-of ::column-direction))
(s/def ::qualifier (s/nilable string?))
;; cannot generate a result set so we can't specify this yet
#_(s/def ::read-columns (s/fspec :args (s/cat :rs     ::result-set
                                              :rsmeta ::result-set-metadata
                                              :idxs   (s/coll-of pos-int?))
                                 :ret  (s/coll-of any?)))
;; there's not much we can say about result-set-fn -- it accepts a collection of
;; transformed rows (from row-fn), and it produces whatever it wants
(s/def ::result-set-fn (s/fspec :args (s/cat :rs (s/coll-of any?))
                                :ret  any?))
(s/def ::result-type (set (keys @#'sql/result-set-type)))
;; there's not much we can say about row-fn -- it accepts a row from a ResultSet
;; which is a map of keywords to any values, and it produces whatever it wants
;; (comp clojure.string/lower-case :table_name) does not satisfy this
#_(s/def ::row-fn (s/fspec :args (s/cat :row (s/map-of keyword? any?))
                           :ret  any?))
(s/def ::row-fn ifn?)
(s/def ::return-keys (s/or :columns (s/coll-of ::entity :kind vector?)
                           :boolean boolean?))
;; ::table-spec
(s/def ::timeout nat-int?)
(s/def ::transaction? boolean?)

;; various types of options

(s/def ::create-options (s/keys :req-un [] :opt-un [::table-spec ::entities]))

(s/def ::exec-sql-options (s/keys :req-un [] :opt-un [::entities ::transaction?]))

(s/def ::execute-options (s/keys :req-un [] :opt-un [::transaction? ::multi?]))

(s/def ::find-by-keys-options (s/keys :req-un []
                                      :opt-un [::entities ::order-by
                                               ::result-set-fn ::row-fn
                                               ::identifiers ::qualifier
                                               ::as-arrays?]))

(s/def ::prepare-options (s/keys :req-un []
                                 :opt-un [::return-keys ::result-type
                                          ::concurrency ::cursors ::fetch-size
                                          ::max-rows ::timeout]))

(s/def ::query-options (s/keys :req-un []
                               :opt-un [::result-set-fn ::row-fn
                                        ::identifiers ::qualifier
                                        ::as-arrays? ::read-columns]))

;; the function API

;; as-sql-name

;; quoted

(s/fdef sql/get-connection
        :args (s/cat :db-spec ::db-spec)
        :ret  ::connection)

(s/fdef sql/result-set-seq
        :args (s/cat :rs   ::result-set
                     :opts (s/? ::query-options))
        :ret  any?)

(s/fdef sql/prepare-statement
        :args (s/cat :con  ::connection
                     :sql  string?
                     :opts (s/? ::prepare-options))
        :ret  ::prepared-statement)

(s/fdef sql/db-find-connection
        :args (s/cat :db-spec ::db-spec)
        :ret  (s/nilable ::connection))

(s/fdef sql/db-connection
        :args (s/cat :db-spec ::db-spec)
        :ret  ::connection)

;; transaction functions

(s/fdef sql/db-set-rollback-only!
        :args (s/cat :db ::db-spec))

(s/fdef sql/db-unset-rollback-only!
        :args (s/cat :db ::db-spec))

(s/fdef sql/db-is-rollback-only
        :args (s/cat :db ::db-spec)
        :ret  boolean?)

(s/fdef sql/get-isolation-level
        :args (s/cat :db ::db-spec)
        :ret  (s/nilable keyword?))

;; db-transaction*

;; with-db-transaction macro

;; with-db-connection macro

;; with-db-metadata macro

(s/fdef sql/metadata-result
        :args (s/cat :rs-or-value any?
                     :opts        (s/? ::query-options))
        :ret  any?)

;; metadata-query macro

;; db-do-commands

;; db-do-prepared-return-keys

;; db-do-prepared

;; db-query-with-resultset

(s/fdef sql/query
        :args (s/cat :db         ::db-spec
                     :sql-params ::sql-params
                     :opts       (s/? ::query-options))
        ;; because result-set-fn can return anything:
        :ret  any?)

(s/fdef sql/find-by-keys
        :args (s/cat :db      ::db-spec
                     :table   ::identifier
                     :columns (s/map-of ::identifier ::sql-value)
                     :opts    (s/? ::find-by-keys-options))
        :ret  any?)

;; get-by-id

(s/fdef sql/execute!
        :args (s/cat :db         ::db-spec
                     :sql-params ::sql-params
                     :opts       (s/? ::execute-options))
        :ret  ::execute-result)

(s/fdef sql/delete!
        :args (s/cat :db           ::db-spec
                     :table        ::identifier
                     :where-clause (s/spec ::where-clause)
                     :opts         (s/? ::exec-sql-options))
        :ret  ::execute-result)

;; insert!

;; insert-multi!

(s/fdef sql/update!
        :args (s/cat :db           ::db-spec
                     :table        ::identifier
                     :set-map      (s/map-of ::identifier ::sql-value)
                     :where-clause (s/spec ::where-clause)
                     :opts         (s/? ::exec-sql-options))
        :ret  ::execute-result)

(s/def ::column-spec (s/cat :col ::identifier :spec (s/* (s/or :kw keyword? :str string?))))

(s/fdef sql/create-table-ddl
        :args (s/cat :table ::identifier
                     :specs (s/coll-of ::column-spec)
                     :opts  (s/? ::create-options))
        :ret  string?)

(s/fdef sql/drop-table-ddl
        :args (s/cat :table ::identifier
                     :opts  (s/? (s/keys :req-un [] :opt-un [::entities])))
        :ret  string?)
