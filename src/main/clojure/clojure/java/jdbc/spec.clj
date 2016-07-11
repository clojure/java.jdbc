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
            [clojure.java.jdbc :refer :all]))

;; basic java.sql types

(s/def ::connection #(instance? java.sql.Connection %))
(s/def ::prepared-statement #(instance? java.sql.PreparedStatement %))

;; database specification (connection description)

(s/def ::db-spec-connection (s/keys :req-un [::connection]))
(s/def ::db-spec-factory (s/keys :req-un [::factory]))
(s/def ::db-spec-driver-manager (s/keys :req-un [::subprotocol ::subname] :opt-un [::classname]))
(s/def ::db-spec-driver-manager-alt (s/keys :req-un [::dbtype ::dbname] :opt-un [::host ::port]))
(s/def ::db-spec-data-source (s/keys :req-un [::datasource] :opt-un [::username ::user ::password]))
(s/def ::db-spec-jndi (s/keys :req-un [::name] :opt-un [::environment]))
(s/def ::db-spec-raw (s/keys :req-un [::connection-uri]))
(s/def ::db-spec-string string?)
(s/def ::db-spec (s/or :connection ::db-spec-connection
                       :factory    ::db-spec-factory
                       :driver-mgr ::db-spec-driver-manager
                       :friendly   ::db-spec-driver-manager-alt
                       :datasource ::db-spec-data-source
                       :jndi       ::db-spec-jndi
                       :raw        ::db-spec-raw
                       :uri        ::db-spec-string))

;; naming

(s/def ::entity string?)

(s/def ::identifier (s/or :kw keyword? :str string?))

;; SQL and parameters

(s/def ::sql-stmt (s/or :sql string? :stmt ::prepared-statement))

(s/def ::sql-value any?) ;; for now

(s/def ::sql-params (s/or :sql        ::sql-stmt
                          :sql-params (s/cat :sql ::sql-stmt :params (s/* ::sql-value))))

(s/def ::where-clause (s/cat :where string? :params (s/* ::sql-value)))

;; results

(s/def ::execute-result (s/* integer?))

;; various types of options

(s/def ::create-options (s/keys :req-un [] :opt-un [::table-spec ::entities]))

(s/def ::exec-sql-options (s/keys :req-un [] :opt-un [::entities ::transaction?]))

(s/def ::execute-options (s/keys :req-un [] :opt-un [::transaction? ::multi?]))

(s/def ::find-by-keys-options (s/keys :req-un [] :opt-un [::entities ::order-by ::result-set-fn ::row-fn ::identifiers ::as-arrays?]))

(s/def ::prepare-options (s/keys :req-un [] :opt-un [::return-keys ::result-type ::concurrency ::cursors ::fetch-size ::max-rows ::timeout]))

(s/def ::query-options (s/keys :req-un [] :opt-un [::result-set-fn ::row-fn ::identifiers ::as-arrays?]))

;; the function API

;; as-sql-name

;; quoted

(s/fdef get-connection
        :args (s/cat :db-spec ::db-spec)
        :ret  ::connection)

;; result-set-seq

(s/fdef prepare-statement
        :args (s/cat :con  ::connection
                     :sql  string?
                     :opts (s/? ::prepare-options))
        :ret  ::prepared-statement)

(s/fdef db-find-connection
        :args (s/cat :db-spec ::db-spec)
        :ret  (s/nilable ::connection))

(s/fdef db-connection
        :args (s/cat :db-spec ::db-spec)
        :ret  ::connection)

;; transaction functions

;; metadata-result

;; db-do-commands

;; db-do-prepared-return-keys

;; db-do-prepared

;; db-query-with-resultset

(s/fdef query
        :args (s/cat :db         ::db-spec
                     :sql-params ::sql-params
                     :opts       (s/? ::query-options))
        ;; because result-set-fn can return anything:
        :ret  any?)

(s/fdef find-by-keys
        :args (s/cat :db      ::db-spec
                     :table   ::identifier
                     :columns (s/map-of ::identifier ::sql-value)
                     :opts    (s/? ::find-by-keys-options))
        :ret  any?)

;; get-by-id

(s/fdef execute!
        :args (s/cat :db         ::db-spec
                     :sql-params ::sql-params
                     :opts       (s/? ::execute-options))
        :ret  ::execute-result)

(s/fdef delete!
        :args (s/cat :db           ::db-spec
                     :table        ::identifier
                     :where-clause (s/spec ::where-clause)
                     :opts         (s/? ::exec-sql-options))
        :ret  ::execute-result)

;; insert!

;; insert-multi!

(s/fdef update!
        :args (s/cat :db           ::db-spec
                     :table        ::identifier
                     :set-map      (s/map-of ::identifier ::sql-value)
                     :where-clause (s/spec ::where-clause)
                     :opts         (s/? ::exec-sql-options))
        :ret  ::execute-result)

(s/def ::column-spec (s/cat :col ::identifier :spec (s/* (s/or :kw keyword? :str string?))))

(s/fdef create-table-ddl
        :args (s/cat :table ::identifier
                     :specs (s/coll-of ::column-spec)
                     :opts  (s/? ::create-options))
        :ret  string?)

(s/fdef drop-table-ddl
        :args (s/cat :table ::identifier
                     :opts  (s/? (s/keys :req-un [] :opt-un [::entities])))
        :ret  string?)
