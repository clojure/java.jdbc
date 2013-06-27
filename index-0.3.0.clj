{:namespaces
 ({:source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc/clojure.java.jdbc-api.html",
   :name "clojure.java.jdbc",
   :author "Stephen C. Gilardi, Sean Corfield",
   :doc
   "A Clojure interface to SQL databases via JDBC\n\nclojure.java.jdbc provides a simple abstraction for CRUD (create, read,\nupdate, delete) operations on a SQL database, along with basic transaction\nsupport. Basic DDL operations are also supported (create table, drop table,\naccess to table metadata).\n\nMaps are used to represent records, making it easy to store and retrieve\ndata. Results can be processed using any standard sequence operations.\n\nFor most operations, Java's PreparedStatement is used so your SQL and\nparameters can be represented as simple vectors where the first element\nis the SQL string, with ? for each parameter, and the remaining elements\nare the parameter values to be substituted. In general, operations return\nthe number of rows affected, except for a single record insert where any\ngenerated keys are returned (as a map).\n\nAs of release 0.3.0, the API has undergone a major overhaul and most of the\noriginal API has been deprecated in favor of a more idiomatic API, and a\nminimal DSL for generating SQL has been added as an option. The original\nAPI is still supported but will be deprecated before a 1.0.0 release is\nmade at some future date."}
  {:source-url
   "https://github.com/clojure/java.jdbc/blob/45f3d990040a5b8e6ec8bd322fe8c04cab4bd933/src/main/clojure/clojure/java/jdbc/ddl.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc/clojure.java.jdbc.ddl-api.html",
   :name "clojure.java.jdbc.ddl",
   :author "Sean Corfield",
   :doc
   "An optional DSL for generating DDL.\n\nIntended to be used with clojure.java.jdbc, this provides a simple DSL -\nDomain Specific Language - that generates raw DDL strings. Any other DSL\ncan be used instead. This DSL is entirely optional and is deliberately\nnot very sophisticated."}
  {:source-url
   "https://github.com/clojure/java.jdbc/blob/f9ecadd03c1c2e01f107155b03061ac0b20f976c/src/main/clojure/clojure/java/jdbc/sql.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc/clojure.java.jdbc.sql-api.html",
   :name "clojure.java.jdbc.sql",
   :author "Sean Corfield",
   :doc
   "An optional DSL for generating SQL.\n\nIntended to be used with clojure.java.jdbc, this provides a simple DSL -\nDomain Specific Language - that generates raw SQL strings. Any other DSL\ncan be used instead. This DSL is entirely optional and is deliberately\nnot very sophisticated. It is sufficient to support the delete!, insert!\nand update! high-level operations within clojure.java.jdbc directly."}),
 :vars
 ({:arglists ([x] [x f-entity]),
   :name "as-identifier",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L1049",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/as-identifier",
   :doc
   "Given a keyword, convert it to a string using the current naming\nstrategy.\nGiven a string, return it as-is.",
   :var-type "function",
   :line 1049,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([f x]),
   :name "as-key",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L1002",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/as-key",
   :doc
   "Given a naming strategy and a string, return the string as a\nkeyword per that naming strategy. Given (a naming strategy and)\na keyword, return it as-is.",
   :var-type "function",
   :line 1002,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([x] [x f-keyword]),
   :name "as-keyword",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L1013",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/as-keyword",
   :doc
   "Given an entity name (string), convert it to a keyword using the\ncurrent naming strategy.\nGiven a keyword, return it as-is.",
   :var-type "function",
   :line 1013,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([naming-strategy x]),
   :name "as-named-identifier",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L1068",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/as-named-identifier",
   :doc
   "Given a naming strategy and a keyword, return the keyword as a string using\nthe entity naming strategy.\nGiven a naming strategy and a string, return the string as-is.\nThe naming strategy should either be a function (the entity naming strategy)\nor a map containing :entity and/or :keyword keys which provide the entity\nnaming strategy and/or keyword naming strategy respectively.",
   :var-type "function",
   :line 1068,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([naming-strategy x]),
   :name "as-named-keyword",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L1022",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/as-named-keyword",
   :doc
   "Given a naming strategy and a string, return the string as a keyword using\nthe keyword naming strategy.\nGiven a naming strategy and a keyword, return the keyword as-is.\nThe naming strategy should either be a function (the entity naming strategy)\nor a map containing :entity and/or :keyword keys which provide the entity\nnaming strategy and/or keyword naming strategy respectively.\nNote that providing a single function will cause the default keyword naming\nstrategy to be used!",
   :var-type "function",
   :line 1022,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([q x]),
   :name "as-quoted-identifier",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L1080",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/as-quoted-identifier",
   :doc
   "Given a quote pattern - either a single character or a pair of characters in\na vector - and a keyword, return the keyword as a string using a simple\nquoting naming strategy.\nGiven a quote pattern and a string, return the string as-is.\n  (as-quoted-identifier X :name) will return XnameX as a string.\n  (as-quoted-identifier [A B] :name) will return AnameB as a string.",
   :var-type "function",
   :line 1080,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([q x]),
   :name "as-quoted-str",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L1058",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/as-quoted-str",
   :doc
   "Given a quoting pattern - either a single character or a vector pair of\ncharacters - and a string, return the quoted string:\n  (as-quoted-str X foo) will return XfooX\n  (as-quoted-str [A B] foo) will return AfooB",
   :var-type "function",
   :line 1058,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([f x]),
   :name "as-str",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L1036",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/as-str",
   :doc
   "Given a naming strategy and a keyword, return the keyword as a\nstring per that naming strategy. Given (a naming strategy and)\na string, return it as-is.\nA keyword of the form :x.y is treated as keywords :x and :y,\nboth are turned into strings via the naming strategy and then\njoined back together so :x.y might become `x`.`y` if the naming\nstrategy quotes identifiers with `.",
   :var-type "function",
   :line 1036,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([]),
   :name "connection",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L741",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/connection",
   :doc
   "Returns the current database connection (or throws if there is none)",
   :var-type "function",
   :line 741,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([name & specs]),
   :name "create-table",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L850",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/create-table",
   :doc
   "Creates a table on the open database connection given a table name and\nspecs. Each spec is either a column spec: a vector containing a column\nname and optionally a type and other constraints, or a table-level\nconstraint: a vector containing words that express the constraint. An\noptional suffix to the CREATE TABLE DDL describing table attributes may\nby provided as :table-spec {table-attributes-string}. All words used to\ndescribe the table may be supplied as strings or keywords.",
   :var-type "function",
   :line 850,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([name & specs]),
   :name "create-table-ddl",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L843",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/create-table-ddl",
   :doc
   "See clojure.java.jdbc.ddl/create-table for details.\nThis version is deprecated in favor of the version in the DDL namespace.",
   :var-type "function",
   :line 843,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([db]),
   :name "db-connection",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L416",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-connection",
   :doc
   "Returns the current database connection (or throws if there is none)",
   :var-type "function",
   :line 416,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([db transaction? & commands]),
   :name "db-do-commands",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L492",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-do-commands",
   :doc
   "Executes SQL commands on the specified database connection. Wraps the commands\nin a transaction if transaction? is true. transaction? can be ommitted and it\ndefaults to true.",
   :var-type "function",
   :line 492,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([db transaction? & [sql & param-groups :as opts]]),
   :name "db-do-prepared",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L546",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-do-prepared",
   :doc
   "Executes an (optionally parameterized) SQL prepared statement on the\nopen database connection. Each param-group is a seq of values for all of\nthe parameters. transaction? can be omitted and defaults to true.\nReturn a seq of update counts (one count for each param-group).",
   :var-type "function",
   :line 546,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([db sql param-group] [db transaction? sql param-group]),
   :name "db-do-prepared-return-keys",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L513",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-do-prepared-return-keys",
   :doc
   "Executes an (optionally parameterized) SQL prepared statement on the\nopen database connection. The param-group is a seq of values for all of\nthe parameters. transaction? can be ommitted and will default to true.\nReturn the generated keys for the (single) update/insert.",
   :var-type "function",
   :line 513,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([db]),
   :name "db-find-connection",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L410",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-find-connection",
   :doc
   "Returns the current database connection (or nil if there is none)",
   :var-type "function",
   :line 410,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([db]),
   :name "db-is-rollback-only",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L443",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-is-rollback-only",
   :doc
   "Returns true if the outermost transaction will rollback rather than\ncommit when complete",
   :var-type "function",
   :line 443,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([db]),
   :name "db-set-rollback-only!",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L432",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-set-rollback-only!",
   :doc
   "Marks the outermost transaction such that it will rollback rather than\ncommit when complete",
   :var-type "function",
   :line 432,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([binding & body]),
   :name "db-transaction",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L483",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-transaction",
   :doc
   "Evaluates body in the context of a transaction on the specified database connection.\nThe binding provides the database connection for the transaction and the name to which\nthat is bound for evaluation of the body.\nSee db-transaction* for more details.",
   :var-type "macro",
   :line 483,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([db func]),
   :name "db-transaction*",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L449",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-transaction*",
   :doc
   "Evaluates func as a transaction on the open database connection. Any\nnested transactions are absorbed into the outermost transaction. By\ndefault, all database updates are committed together as a group after\nevaluating the outermost body, or rolled back on any uncaught\nexception. If rollback is set within scope of the outermost transaction,\nthe entire transaction will be rolled back rather than committed when\ncomplete.",
   :var-type "function",
   :line 449,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([db]),
   :name "db-unset-rollback-only!",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L438",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-unset-rollback-only!",
   :doc
   "Marks the outermost transaction such that it will not rollback when complete",
   :var-type "function",
   :line 438,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists
   ([db
     table
     where-clause
     &
     {:keys [entities transaction?],
      :or {entities sql/as-is, transaction? true}}]),
   :name "delete!",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L661",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/delete!",
   :doc
   "Given a database connection, a table name and a where clause of columns to match,\nperform a delete. The optional keyword arguments specify how to transform\ncolumn names in the map (default 'as-is') and whether to run the delete in\na transaction (default true).\nExample:\n  (delete! db :person (where {:zip 94546}))\nis equivalent to:\n  (execute! db [\"DELETE FROM person WHERE zip = ?\" 94546])",
   :var-type "function",
   :line 661,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([table where-params]),
   :name "delete-rows",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L920",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/delete-rows",
   :doc
   "Deletes rows from a table. where-params is a vector containing a string\nproviding the (optionally parameterized) selection criteria followed by\nvalues for any parameters.",
   :var-type "function",
   :line 920,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([& commands]),
   :name "do-commands",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L826",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/do-commands",
   :doc "Executes SQL commands on the open database connection.",
   :var-type "function",
   :line 826,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([sql & param-groups]),
   :name "do-prepared",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L833",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/do-prepared",
   :doc
   "Executes an (optionally parameterized) SQL prepared statement on the\nopen database connection. Each param-group is a seq of values for all of\nthe parameters.\nReturn a seq of update counts (one count for each param-group).",
   :var-type "function",
   :line 833,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([sql param-group]),
   :name "do-prepared-return-keys",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L871",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/do-prepared-return-keys",
   :doc
   "Executes an (optionally parameterized) SQL prepared statement on the\nopen database connection. The param-group is a seq of values for all of\nthe parameters.\nReturn the generated keys for the (single) update/insert.",
   :var-type "function",
   :line 871,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([name]),
   :name "drop-table",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L863",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/drop-table",
   :doc
   "Drops a table on the open database connection given its name, a string\nor keyword",
   :var-type "function",
   :line 863,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists
   ([db
     sql-params
     &
     {:keys [transaction? multi?],
      :or {transaction? true, multi? false}}]),
   :name "execute!",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L645",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/execute!",
   :doc
   "Given a database connection and a vector containing SQL and optional parameters,\nperform a general (non-select) SQL operation. The optional keyword argument specifies\nwhether to run the operation in a transaction or not (default true).",
   :var-type "function",
   :line 645,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([]),
   :name "find-connection",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L735",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/find-connection",
   :doc
   "Returns the current database connection (or nil if there is none)",
   :var-type "function",
   :line 735,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists
   ([{:keys
      [connection
       factory
       connection-uri
       classname
       subprotocol
       subname
       datasource
       username
       password
       name
       environment],
      :as db-spec}]),
   :name "get-connection",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L140",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/get-connection",
   :doc
   "Creates a connection to a database. db-spec is a map containing connection\nparameters. db-spec is a map containing values for one of the following\nparameter sets:\n\nExisting Connection:\n  :connection  (required) an existing open connection that can be used\n               but cannot be closed (only the parent connection can be closed)\n\nFactory:\n  :factory     (required) a function of one argument, a map of params\n  (others)     (optional) passed to the factory function in a map\n\nDriverManager:\n  :subprotocol (required) a String, the jdbc subprotocol\n  :subname     (required) a String, the jdbc subname\n  :classname   (optional) a String, the jdbc driver class name\n  (others)     (optional) passed to the driver as properties.\n\nDataSource:\n  :datasource  (required) a javax.sql.DataSource\n  :username    (optional) a String\n  :password    (optional) a String, required if :username is supplied\n\nJNDI:\n  :name        (required) a String or javax.naming.Name\n  :environment (optional) a java.util.Map\n\nRaw:\n  :connection-uri (required) a String\n               Passed directly to DriverManager/getConnection\n\nURI:\n  Parsed JDBC connection string - see below\n\nString:\n  subprotocol://user:password@host:post/subname\n               An optional prefix of jdbc: is allowed.",
   :var-type "function",
   :line 140,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([db table & options]),
   :name "insert!",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L705",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/insert!",
   :doc
   "Given a database connection, a table name and either maps representing rows or\na list of column names followed by lists of column values, perform an insert.\nUse :transaction? argument to specify whether to run in a transaction or not.\nThe default is true (use a transaction).",
   :var-type "function",
   :line 705,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([table record]),
   :name "insert-record",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L911",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/insert-record",
   :doc
   "Inserts a single record into a table. A record is a map from strings or\nkeywords (identifying columns) to values.\nReturns a map of the generated keys.",
   :var-type "function",
   :line 911,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([table & records]),
   :name "insert-records",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L902",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/insert-records",
   :doc
   "Inserts records into a table. records are maps from strings or keywords\n(identifying columns) to values. Inserts the records one at a time.\nReturns a sequence of maps containing the generated keys for each record.",
   :var-type "function",
   :line 902,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([table & rows]),
   :name "insert-rows",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L893",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/insert-rows",
   :doc
   "Inserts complete rows into a table. Each row is a vector of values for\neach of the table's columns in order.\nIf a single row is inserted, returns a map of the generated keys.",
   :var-type "function",
   :line 893,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([table column-names & value-groups]),
   :name "insert-values",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L881",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/insert-values",
   :doc
   "Inserts rows into a table with values for specified columns only.\ncolumn-names is a vector of strings or keywords identifying columns. Each\nvalue-group is a vector containing a values for each column in\norder. When inserting complete rows (all columns), consider using\ninsert-rows instead.\nIf a single set of values is inserted, returns a map of the generated keys.",
   :var-type "function",
   :line 881,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([]),
   :name "is-rollback-only",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L818",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/is-rollback-only",
   :doc
   "Returns true if the outermost transaction will rollback rather than\ncommit when complete",
   :var-type "function",
   :line 818,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists
   ([con
     sql
     &
     {:keys
      [return-keys
       result-type
       concurrency
       cursors
       fetch-size
       max-rows]}]),
   :name "prepare-statement",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L328",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/prepare-statement",
   :doc
   "Create a prepared statement from a connection, a SQL string and an\noptional list of parameters:\n  :return-keys true | false - default false\n  :result-type :forward-only | :scroll-insensitive | :scroll-sensitive\n  :concurrency :read-only | :updatable\n  :cursors\n  :fetch-size n\n  :max-rows n",
   :var-type "function",
   :line 328,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([exception]),
   :name "print-sql-exception",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L370",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/print-sql-exception",
   :doc "Prints the contents of an SQLException to *out*",
   :var-type "function",
   :line 370,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([exception]),
   :name "print-sql-exception-chain",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L384",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/print-sql-exception-chain",
   :doc "Prints a chain of SQLExceptions to *out*",
   :var-type "function",
   :line 384,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([exception]),
   :name "print-update-counts",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L396",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/print-update-counts",
   :doc
   "Prints the update counts from a BatchUpdateException to *out*",
   :var-type "function",
   :line 396,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists
   ([db
     sql-params
     &
     {:keys [result-set-fn row-fn identifiers as-arrays?],
      :or
      {result-set-fn doall,
       row-fn identity,
       identifiers sql/lower-case}}]),
   :name "query",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L624",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/query",
   :doc
   "Given a database connection and a vector containing SQL and optional parameters,\nperform a simple database query. The optional keyword arguments specify how to\nconstruct the result set:\n  :result-set-fn - applied to the entire result set, default doall\n  :row-fn - applied to each row as the result set is constructed, default identity\n  :identifiers - applied to each column name in the result set, default lower-case\n  :as-arrays? - return the results as a set of arrays, default false.",
   :var-type "function",
   :line 624,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists
   ([rs
     &
     {:keys [identifiers as-arrays?],
      :or {identifiers str/lower-case}}]),
   :name "result-set-seq",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L257",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/result-set-seq",
   :doc
   "Creates and returns a lazy sequence of maps corresponding to the rows in the\njava.sql.ResultSet rs. Loosely based on clojure.core/resultset-seq but it\nrespects the specified naming strategy. Duplicate column names are made unique\nby appending _N before applying the naming strategy (where N is a unique integer).",
   :var-type "function",
   :line 257,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists
   ([rs & {:keys [identifiers], :or {identifiers *as-key*}}]),
   :name "resultset-seq",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L288",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/resultset-seq",
   :doc
   "A deprecated version of result-set-seq that uses the\ndynamic *as-key* variable.",
   :var-type "function",
   :line 288,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([]),
   :name "set-rollback-only",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L810",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/set-rollback-only",
   :doc
   "Marks the outermost transaction such that it will rollback rather than\ncommit when complete",
   :var-type "function",
   :line 810,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([& body]),
   :name "transaction",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L797",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/transaction",
   :doc
   "Evaluates body as a transaction on the open database connection. Any\nnested transactions are absorbed into the outermost transaction. By\ndefault, all database updates are committed together as a group after\nevaluating the outermost body, or rolled back on any uncaught\nexception. If set-rollback-only is called within scope of the outermost\ntransaction, the entire transaction will be rolled back rather than\ncommitted when complete.",
   :var-type "macro",
   :line 797,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([func]),
   :name "transaction*",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L763",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/transaction*",
   :doc
   "Evaluates func as a transaction on the open database connection. Any\nnested transactions are absorbed into the outermost transaction. By\ndefault, all database updates are committed together as a group after\nevaluating the outermost body, or rolled back on any uncaught\nexception. If rollback is set within scope of the outermost transaction,\nthe entire transaction will be rolled back rather than committed when\ncomplete.",
   :var-type "function",
   :line 763,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists
   ([db
     table
     set-map
     where-clause
     &
     {:keys [entities transaction?],
      :or {entities sql/as-is, transaction? true}}]),
   :name "update!",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L718",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/update!",
   :doc
   "Given a database connection, a table name, a map of column values to set and a\nwhere clause of columns to match, perform an update. The optional keyword arguments\nspecify how column names (in the set / match maps) should be transformed (default\n'as-is') and whether to run the update in a transaction (default true).\nExample:\n  (update! db :person {:zip 94540} (where {:zip 94546}))\nis equivalent to:\n  (execute! db [\"UPDATE person SET zip = ? WHERE zip = ?\" 94540 94546])",
   :var-type "function",
   :line 718,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([table where-params record]),
   :name "update-or-insert-values",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L939",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/update-or-insert-values",
   :doc
   "Updates values on selected rows in a table, or inserts a new row when no\nexisting row matches the selection criteria. where-params is a vector\ncontaining a string providing the (optionally parameterized) selection\ncriteria followed by values for any parameters. record is a map from\nstrings or keywords (identifying columns) to updated values.",
   :var-type "function",
   :line 939,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([table where-params record]),
   :name "update-values",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L929",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/update-values",
   :doc
   "Updates values on selected rows in a table. where-params is a vector\ncontaining a string providing the (optionally parameterized) selection\ncriteria followed by values for any parameters. record is a map from\nstrings or keywords (identifying columns) to updated values.",
   :var-type "function",
   :line 929,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([db-spec & body]),
   :name "with-connection",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L756",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/with-connection",
   :doc
   "Evaluates body in the context of a new connection to a database then\ncloses the connection.",
   :var-type "macro",
   :line 756,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([db-spec func]),
   :name "with-connection*",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L747",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/with-connection*",
   :doc
   "Evaluates func in the context of a new connection to a database then\ncloses the connection.",
   :var-type "function",
   :line 747,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([naming-strategy & body]),
   :name "with-naming-strategy",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L1099",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/with-naming-strategy",
   :doc
   "Evaluates body in the context of a naming strategy.\nThe naming strategy is either a function - the entity naming strategy - or\na map containing :entity and/or :keyword keys which provide the entity naming\nstrategy and/or the keyword naming strategy respectively. The default entity\nnaming strategy is identity; the default keyword naming strategy is\nlower-case.",
   :var-type "macro",
   :line 1099,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([results sql-params & body]),
   :name "with-query-results",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L988",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/with-query-results",
   :doc
   "Executes a query, then evaluates body with results bound to a seq of the\nresults. sql-params is a vector containing either:\n  [sql & params] - a SQL query, followed by any parameters it needs\n  [stmt & params] - a PreparedStatement, followed by any parameters it needs\n                    (the PreparedStatement already contains the SQL query)\n  [options sql & params] - options and a SQL query for creating a\n                    PreparedStatement, followed by any parameters it needs\nSee prepare-statement for supported options.",
   :var-type "macro",
   :line 988,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([sql-params func]),
   :name "with-query-results*",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L952",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/with-query-results*",
   :doc
   "Executes a query, then evaluates func passing in a seq of the results as\nan argument. The first argument is a vector containing either:\n  [sql & params] - a SQL query, followed by any parameters it needs\n  [stmt & params] - a PreparedStatement, followed by any parameters it needs\n                   (the PreparedStatement already contains the SQL query)\n  [options sql & params] - options and a SQL query for creating a\n                   PreparedStatement, followed by any parameters it needs\nSee prepare-statement for supported options.",
   :var-type "function",
   :line 952,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([q & body]),
   :name "with-quoted-identifiers",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L1092",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/with-quoted-identifiers",
   :doc
   "Evaluates body in the context of a simple quoting naming strategy.",
   :var-type "macro",
   :line 1092,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:file "src/main/clojure/clojure/java/jdbc.clj",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/dd3c05b940b9a9c7a739247e2508ea6a5d55df65/src/main/clojure/clojure/java/jdbc.clj#L243",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/IResultSetReadColumn",
   :namespace "clojure.java.jdbc",
   :line 243,
   :var-type "protocol",
   :doc
   "Protocol for reading objects from the java.sql.ResultSet. Default\nimplementations (for Object and nil) return the argument, but it can\nbe extended to provide custom behavior for special types.",
   :name "IResultSetReadColumn"}
  {:file nil,
   :raw-source-url nil,
   :source-url nil,
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/result-set-read-column",
   :namespace "clojure.java.jdbc",
   :var-type "function",
   :arglists ([val rsmeta idx]),
   :doc
   "Function for transforming values after reading them\nfrom the database",
   :name "result-set-read-column"}
  {:arglists ([index-name table-name cols & is-unique]),
   :name "create-index",
   :namespace "clojure.java.jdbc.ddl",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/45f3d990040a5b8e6ec8bd322fe8c04cab4bd933/src/main/clojure/clojure/java/jdbc/ddl.clj#L52",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/45f3d990040a5b8e6ec8bd322fe8c04cab4bd933/src/main/clojure/clojure/java/jdbc/ddl.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.ddl/create-index",
   :doc
   "Given an index name, table name, vector of column names, and\n(optional) is-unique, return the DDL string for creating an index.\n\n Examples:\n (create-index :indexname :tablename [:field1 :field2] :unique)\n \"CREATE UNIQUE INDEX indexname ON tablename (field1, field2)\"\n\n (create-index :indexname :tablename [:field1 :field2])\n \"CREATE INDEX indexname ON tablename (field1, field2)\"",
   :var-type "function",
   :line 52,
   :file "src/main/clojure/clojure/java/jdbc/ddl.clj"}
  {:arglists ([name & specs]),
   :name "create-table",
   :namespace "clojure.java.jdbc.ddl",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/45f3d990040a5b8e6ec8bd322fe8c04cab4bd933/src/main/clojure/clojure/java/jdbc/ddl.clj#L28",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/45f3d990040a5b8e6ec8bd322fe8c04cab4bd933/src/main/clojure/clojure/java/jdbc/ddl.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.ddl/create-table",
   :doc
   "Given a table name and column specs with an optional table-spec\nreturn the DDL string for creating that table.",
   :var-type "function",
   :line 28,
   :file "src/main/clojure/clojure/java/jdbc/ddl.clj"}
  {:arglists ([name]),
   :name "drop-index",
   :namespace "clojure.java.jdbc.ddl",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/45f3d990040a5b8e6ec8bd322fe8c04cab4bd933/src/main/clojure/clojure/java/jdbc/ddl.clj#L74",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/45f3d990040a5b8e6ec8bd322fe8c04cab4bd933/src/main/clojure/clojure/java/jdbc/ddl.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.ddl/drop-index",
   :doc
   "Given an index name, return the DDL string for dropping that index.",
   :var-type "function",
   :line 74,
   :file "src/main/clojure/clojure/java/jdbc/ddl.clj"}
  {:arglists ([name]),
   :name "drop-table",
   :namespace "clojure.java.jdbc.ddl",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/45f3d990040a5b8e6ec8bd322fe8c04cab4bd933/src/main/clojure/clojure/java/jdbc/ddl.clj#L47",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/45f3d990040a5b8e6ec8bd322fe8c04cab4bd933/src/main/clojure/clojure/java/jdbc/ddl.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.ddl/drop-table",
   :doc
   "Given a table name, return the DDL string for dropping that table.",
   :var-type "function",
   :line 47,
   :file "src/main/clojure/clojure/java/jdbc/ddl.clj"}
  {:arglists ([q] [q x]),
   :name "as-quoted-str",
   :namespace "clojure.java.jdbc.sql",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/f9ecadd03c1c2e01f107155b03061ac0b20f976c/src/main/clojure/clojure/java/jdbc/sql.clj#L52",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/f9ecadd03c1c2e01f107155b03061ac0b20f976c/src/main/clojure/clojure/java/jdbc/sql.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.sql/as-quoted-str",
   :doc
   "Given a quoting pattern - either a single character or a vector pair of\ncharacters - and a string, return the quoted string:\n  (as-quoted-str X foo) will return XfooX\n  (as-quoted-str [A B] foo) will return AfooB",
   :var-type "function",
   :line 52,
   :file "src/main/clojure/clojure/java/jdbc/sql.clj"}
  {:arglists ([f] [f x]),
   :name "as-str",
   :namespace "clojure.java.jdbc.sql",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/f9ecadd03c1c2e01f107155b03061ac0b20f976c/src/main/clojure/clojure/java/jdbc/sql.clj#L32",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/f9ecadd03c1c2e01f107155b03061ac0b20f976c/src/main/clojure/clojure/java/jdbc/sql.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.sql/as-str",
   :doc
   "Given a naming strategy and a keyword, return the keyword as a\nstring per that naming strategy. Given (a naming strategy and)\na string, return it as-is.\nA keyword of the form :x.y is treated as keywords :x and :y,\nboth are turned into strings via the naming strategy and then\njoined back together so :x.y might become `x`.`y` if the naming\nstrategy quotes identifiers with `.",
   :var-type "function",
   :line 32,
   :file "src/main/clojure/clojure/java/jdbc/sql.clj"}
  {:arglists
   ([table
     [where & params]
     &
     {:keys [entities], :or {entities as-is}}]),
   :name "delete",
   :namespace "clojure.java.jdbc.sql",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/f9ecadd03c1c2e01f107155b03061ac0b20f976c/src/main/clojure/clojure/java/jdbc/sql.clj#L174",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/f9ecadd03c1c2e01f107155b03061ac0b20f976c/src/main/clojure/clojure/java/jdbc/sql.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.sql/delete",
   :doc
   "Given a table name, a where class and its parameters and an optional entities spec,\nreturn a vector of the SQL for that delete operation followed by its parameters. The\nentities spec (default 'as-is') specifies how to transform column names.",
   :var-type "function",
   :line 174,
   :file "src/main/clojure/clojure/java/jdbc/sql.clj"}
  {:arglists ([entities sql]),
   :name "entities",
   :namespace "clojure.java.jdbc.sql",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/f9ecadd03c1c2e01f107155b03061ac0b20f976c/src/main/clojure/clojure/java/jdbc/sql.clj#L142",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/f9ecadd03c1c2e01f107155b03061ac0b20f976c/src/main/clojure/clojure/java/jdbc/sql.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.sql/entities",
   :doc
   "Given an entities function and a SQL-generating DSL form, transform the DSL form\nto inject an :entities keyword argument with the function at the end of each appropriate\nform.",
   :var-type "macro",
   :line 142,
   :file "src/main/clojure/clojure/java/jdbc/sql.clj"}
  {:arglists ([identifiers sql]),
   :name "identifiers",
   :namespace "clojure.java.jdbc.sql",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/f9ecadd03c1c2e01f107155b03061ac0b20f976c/src/main/clojure/clojure/java/jdbc/sql.clj#L154",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/f9ecadd03c1c2e01f107155b03061ac0b20f976c/src/main/clojure/clojure/java/jdbc/sql.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.sql/identifiers",
   :doc
   "Given an identifiers function and a SQL-generating DSL form, transform the DSL form\nto inject an :identifiers keyword argument with the function at the end of each\nappropriate form.",
   :var-type "macro",
   :line 154,
   :file "src/main/clojure/clojure/java/jdbc/sql.clj"}
  {:arglists ([table & clauses]),
   :name "insert",
   :namespace "clojure.java.jdbc.sql",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/f9ecadd03c1c2e01f107155b03061ac0b20f976c/src/main/clojure/clojure/java/jdbc/sql.clj#L183",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/f9ecadd03c1c2e01f107155b03061ac0b20f976c/src/main/clojure/clojure/java/jdbc/sql.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.sql/insert",
   :doc
   "Given a table name and either column names and values or maps representing rows, retun\nreturn a vector of the SQL for that insert operation followed by its parameters. An\noptional entities spec (default 'as-is') specifies how to transform column names.",
   :var-type "function",
   :line 183,
   :file "src/main/clojure/clojure/java/jdbc/sql.clj"}
  {:arglists
   ([table on-map & {:keys [entities], :or {entities as-is}}]),
   :name "join",
   :namespace "clojure.java.jdbc.sql",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/f9ecadd03c1c2e01f107155b03061ac0b20f976c/src/main/clojure/clojure/java/jdbc/sql.clj#L206",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/f9ecadd03c1c2e01f107155b03061ac0b20f976c/src/main/clojure/clojure/java/jdbc/sql.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.sql/join",
   :doc
   "Given a table name and a map of how to join it (to the existing SQL fragment),\nretun the SQL string for the JOIN clause. The optional entities spec (default 'as-is')\nspecifies how to transform column names.",
   :var-type "function",
   :line 206,
   :file "src/main/clojure/clojure/java/jdbc/sql.clj"}
  {:arglists ([cols & {:keys [entities], :or {entities as-is}}]),
   :name "order-by",
   :namespace "clojure.java.jdbc.sql",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/f9ecadd03c1c2e01f107155b03061ac0b20f976c/src/main/clojure/clojure/java/jdbc/sql.clj#L216",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/f9ecadd03c1c2e01f107155b03061ac0b20f976c/src/main/clojure/clojure/java/jdbc/sql.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.sql/order-by",
   :doc
   "Given a sequence of column order specs, and an optional entities spec, return the\nSQL string for the ORDER BY clause. A column order spec may be a column name or a\nmap of the column name to the desired order.",
   :var-type "function",
   :line 216,
   :file "src/main/clojure/clojure/java/jdbc/sql.clj"}
  {:arglists ([col-seq table & clauses]),
   :name "select",
   :namespace "clojure.java.jdbc.sql",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/f9ecadd03c1c2e01f107155b03061ac0b20f976c/src/main/clojure/clojure/java/jdbc/sql.clj#L229",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/f9ecadd03c1c2e01f107155b03061ac0b20f976c/src/main/clojure/clojure/java/jdbc/sql.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.sql/select",
   :doc
   "Given a sequence of column names (or *) and a table name, followed by optional SQL\nclauses, return a vector for the SQL followed by its parameters. The general form is:\n  (select [columns] table joins [where params] order-by options)\nwhere joins are optional strings, as is order-by, and the where clause is a vector\nof a where SQL clause followed by its parameters. The options may may include an\nentities spec to specify how column names should be transformed.\nThe intent is that the joins, where clause and order by clause are generated by\nother parts of the DSL:\n  (select * {:person :p}\n          (join {:address :a} {:p.addressId :a.id})\n          (where {:a.zip 94546})\n          (order-by :p.name))",
   :var-type "function",
   :line 229,
   :file "src/main/clojure/clojure/java/jdbc/sql.clj"}
  {:arglists ([table set-map & where-etc]),
   :name "update",
   :namespace "clojure.java.jdbc.sql",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/f9ecadd03c1c2e01f107155b03061ac0b20f976c/src/main/clojure/clojure/java/jdbc/sql.clj#L267",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/f9ecadd03c1c2e01f107155b03061ac0b20f976c/src/main/clojure/clojure/java/jdbc/sql.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.sql/update",
   :doc
   "Given a table name and a map of columns to set, and optional map of columns to\nmatch (and an optional entities spec), return a vector of the SQL for that update\nfollowed by its parameters. Example:\n  (update :person {:zip 94540} (where {:zip 94546}))\nreturns:\n  [\"UPDATE person SET zip = ? WHERE zip = ?\" 94540 94546]",
   :var-type "function",
   :line 267,
   :file "src/main/clojure/clojure/java/jdbc/sql.clj"}
  {:arglists ([param-map & {:keys [entities], :or {entities as-is}}]),
   :name "where",
   :namespace "clojure.java.jdbc.sql",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/f9ecadd03c1c2e01f107155b03061ac0b20f976c/src/main/clojure/clojure/java/jdbc/sql.clj#L292",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/f9ecadd03c1c2e01f107155b03061ac0b20f976c/src/main/clojure/clojure/java/jdbc/sql.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.sql/where",
   :doc
   "Given a map of columns and values, return a vector containing the where clause SQL\nfollowed by its parameters. Example:\n  (where {:a 42 :b nil})\nreturns:\n  [\"a = ? AND b IS NULL\" 42]",
   :var-type "function",
   :line 292,
   :file "src/main/clojure/clojure/java/jdbc/sql.clj"})}
