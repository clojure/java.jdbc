{:namespaces
 ({:source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc/clojure.java.jdbc-api.html",
   :name "clojure.java.jdbc",
   :author "Stephen C. Gilardi, Sean Corfield",
   :doc
   "A Clojure interface to SQL databases via JDBC\n\nclojure.java.jdbc provides a simple abstraction for CRUD (create, read,\nupdate, delete) operations on a SQL database, along with basic transaction\nsupport. Basic DDL operations are also supported (create table, drop table,\naccess to table metadata).\n\nMaps are used to represent records, making it easy to store and retrieve\ndata. Results can be processed using any standard sequence operations.\n\nFor most operations, Java's PreparedStatement is used so your SQL and\nparameters can be represented as simple vectors where the first element\nis the SQL string, with ? for each parameter, and the remaining elements\nare the parameter values to be substituted. In general, operations return\nthe number of rows affected, except for a single record insert where any\ngenerated keys are returned (as a map).\n\nFor more documentation, see:\n\nhttp://clojure-doc.org/articles/ecosystem/java_jdbc/home.html\n\nAs of release 0.3.0, the API has undergone a major overhaul and most of the\noriginal API has been deprecated in favor of a more idiomatic API. The\noriginal API has been moved to java.jdbc.deprecated for backward\ncompatibility but it will be removed before a 1.0.0 release."}
  {:source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc/clojure.java.jdbc.deprecated-api.html",
   :name "clojure.java.jdbc.deprecated",
   :author "Stephen C. Gilardi, Sean Corfield",
   :doc
   "A Clojure interface to SQL databases via JDBC\n\nThis namespace contains the old API (0.2.3) which was deprecated in the 0.3.0\nrelease and is provided for backward compatibility. This API will be removed\ncompletely before a 1.0.0 release so will need to migrate code to the new API\nbefore that release.\n\nclojure.java.jdbc provides a simple abstraction for CRUD (create, read,\nupdate, delete) operations on a SQL database, along with basic transaction\nsupport. Basic DDL operations are also supported (create table, drop table,\naccess to table metadata).\n\nMaps are used to represent records, making it easy to store and retrieve\ndata. Results can be processed using any standard sequence operations.\n\nFor most operations, Java's PreparedStatement is used so your SQL and\nparameters can be represented as simple vectors where the first element\nis the SQL string, with ? for each parameter, and the remaining elements\nare the parameter values to be substituted. In general, operations return\nthe number of rows affected, except for a single record insert where any\ngenerated keys are returned (as a map)."}),
 :vars
 ({:arglists ([f] [f x]),
   :name "as-sql-name",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L56",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/as-sql-name",
   :doc
   "Given a naming strategy function and a keyword or string, return\na string per that naming strategy.\nA name of the form x.y is treated as multiple names, x, y, etc,\nand each are turned into strings via the naming strategy and then\njoined back together so x.y might become `x`.`y` if the naming\nstrategy quotes identifiers with `.",
   :var-type "function",
   :line 56,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([table & specs]),
   :name "create-table-ddl",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L1073",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/create-table-ddl",
   :doc
   "Given a table name and column specs with an optional table-spec\nreturn the DDL string for creating that table.",
   :var-type "function",
   :line 1073,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([db]),
   :name "db-connection",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L523",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-connection",
   :doc
   "Returns the current database connection (or throws if there is none)",
   :var-type "function",
   :line 523,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists
   ([db-spec sql-command & sql-commands]
    [db-spec transaction? sql-command & sql-commands]),
   :name "db-do-commands",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L686",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-do-commands",
   :doc
   "Executes SQL commands on the specified database connection. Wraps the commands\nin a transaction if transaction? is true. transaction? can be ommitted and it\ndefaults to true.",
   :var-type "function",
   :line 686,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists
   ([db-spec sql & param-groups]
    [db-spec transaction? sql & param-groups]),
   :name "db-do-prepared",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L765",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-do-prepared",
   :doc
   "Executes an (optionally parameterized) SQL prepared statement on the\nopen database connection. Each param-group is a seq of values for all of\nthe parameters. transaction? can be omitted and defaults to true.\nThe sql parameter can either be a SQL string or a PreparedStatement.\nReturn a seq of update counts (one count for each param-group).",
   :var-type "function",
   :line 765,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([db sql param-group] [db transaction? sql param-group]),
   :name "db-do-prepared-return-keys",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L709",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-do-prepared-return-keys",
   :doc
   "Executes an (optionally parameterized) SQL prepared statement on the\nopen database connection. The param-group is a seq of values for all of\nthe parameters. transaction? can be ommitted and will default to true.\nReturn the generated keys for the (single) update/insert.",
   :var-type "function",
   :line 709,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([db]),
   :name "db-find-connection",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L517",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-find-connection",
   :doc
   "Returns the current database connection (or nil if there is none)",
   :var-type "function",
   :line 517,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([db]),
   :name "db-is-rollback-only",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L550",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-is-rollback-only",
   :doc
   "Returns true if the outermost transaction will rollback rather than\ncommit when complete",
   :var-type "function",
   :line 550,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists
   ([db-spec [sql-string & params] func]
    [db-spec [stmt & params] func]
    [db-spec [options-map sql-string & params] func]),
   :name "db-query-with-resultset",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L785",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-query-with-resultset",
   :doc
   "Executes a query, then evaluates func passing in the raw ResultSet as an\n argument. The second argument is a vector containing either:\n  [sql & params] - a SQL query, followed by any parameters it needs\n  [stmt & params] - a PreparedStatement, followed by any parameters it needs\n                    (the PreparedStatement already contains the SQL query)\n  [options sql & params] - options and a SQL query for creating a\n                    PreparedStatement, followed by any parameters it needs\nSee prepare-statement for supported options.",
   :var-type "function",
   :line 785,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([db]),
   :name "db-set-rollback-only!",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L539",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-set-rollback-only!",
   :doc
   "Marks the outermost transaction such that it will rollback rather than\ncommit when complete",
   :var-type "function",
   :line 539,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([binding & body]),
   :name "db-transaction",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L1096",
   :deprecated "0.3.0",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-transaction",
   :doc "Original name for with-db-transaction. Use that instead.",
   :var-type "macro",
   :line 1096,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([db func & {:keys [isolation read-only?]}]),
   :name "db-transaction*",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L564",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-transaction*",
   :doc
   "Evaluates func as a transaction on the open database connection. Any\nnested transactions are absorbed into the outermost transaction. By\ndefault, all database updates are committed together as a group after\nevaluating the outermost body, or rolled back on any uncaught\nexception. If rollback is set within scope of the outermost transaction,\nthe entire transaction will be rolled back rather than committed when\ncomplete.\nThe isolation option may be :none, :read-committed, :read-uncommitted,\n:repeatable-read, or :serializable. Note that not all databases support\nall of those isolation levels, and may either throw an exception or\nsubstitute another isolation level.\nThe read-only? option puts the transaction in readonly mode (if supported).",
   :var-type "function",
   :line 564,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([db]),
   :name "db-unset-rollback-only!",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L545",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-unset-rollback-only!",
   :doc
   "Marks the outermost transaction such that it will not rollback when complete",
   :var-type "function",
   :line 545,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists
   ([db
     table
     where-clause
     &
     {:keys [entities transaction?],
      :or {entities identity, transaction? true}}]),
   :name "delete!",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L908",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/delete!",
   :doc
   "Given a database connection, a table name and a where clause of columns to match,\nperform a delete. The optional keyword arguments specify how to transform\ncolumn names in the map (default 'as-is') and whether to run the delete in\na transaction (default true).\nExample:\n  (delete! db :person [\"zip = ?\" 94546])\nis equivalent to:\n  (execute! db [\"DELETE FROM person WHERE zip = ?\" 94546])",
   :var-type "function",
   :line 908,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([name & {:keys [entities], :or {entities identity}}]),
   :name "drop-table-ddl",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L1091",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/drop-table-ddl",
   :doc
   "Given a table name, return the DDL string for dropping that table.",
   :var-type "function",
   :line 1091,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists
   ([db-spec [sql & params] :multi? false :transaction? true]
    [db-spec [sql & param-groups] :multi? true :transaction? true]),
   :name "execute!",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L869",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/execute!",
   :doc
   "Given a database connection and a vector containing SQL and optional parameters,\nperform a general (non-select) SQL operation. The optional keyword argument specifies\nwhether to run the operation in a transaction or not (default true).",
   :var-type "function",
   :line 869,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists
   ([{:keys
      [connection
       factory
       connection-uri
       classname
       subprotocol
       subname
       dbtype
       dbname
       host
       port
       datasource
       username
       password
       user
       name
       environment],
      :as db-spec}]),
   :name "get-connection",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L176",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/get-connection",
   :doc
   "Creates a connection to a database. db-spec is usually a map containing connection\nparameters but can also be a URI or a String. The various possibilities are described\nbelow:\n\nExisting Connection:\n  :connection  (required) an existing open connection that can be used\n               but cannot be closed (only the parent connection can be closed)\n\nFactory:\n  :factory     (required) a function of one argument, a map of params\n  (others)     (optional) passed to the factory function in a map\n\nDriverManager:\n  :subprotocol (required) a String, the jdbc subprotocol\n  :subname     (required) a String, the jdbc subname\n  :classname   (optional) a String, the jdbc driver class name\n  (others)     (optional) passed to the driver as properties.\n\nDriverManager (alternative):\n  :dbtype      (required) a String, the type of the database (the jdbc subprotocol)\n  :dbname      (required) a String, the name of the database\n  :host        (optional) a String, the host name/IP of the database\n                          (defaults to 127.0.0.1)\n  :port        (optional) a Long, the port of the database\n                          (defaults to 3306 for mysql, 1433 for mssql/jtds, else nil)\n  (others)     (optional) passed to the driver as properties.\n\nDataSource:\n  :datasource  (required) a javax.sql.DataSource\n  :username    (optional) a String\n  :user        (optional) a String - an alternate alias for :username\n                          (added after 0.3.0-beta2 for consistency JDBC-74)\n  :password    (optional) a String, required if :username is supplied\n\nJNDI:\n  :name        (required) a String or javax.naming.Name\n  :environment (optional) a java.util.Map\n\nRaw:\n  :connection-uri (required) a String\n               Passed directly to DriverManager/getConnection\n\nURI:\n  Parsed JDBC connection string - see below\n\nString:\n  subprotocol://user:password@host:post/subname\n               An optional prefix of jdbc: is allowed.",
   :var-type "function",
   :line 176,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists
   ([db-spec table row-map :transaction? true :entities identity]
    [db-spec
     table
     row-map
     &
     row-maps
     :transaction?
     true
     :entities
     identity]
    [db-spec
     table
     col-name-vec
     col-val-vec
     &
     col-val-vecs
     :transaction?
     true
     :entities
     identity]),
   :name "insert!",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L1016",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/insert!",
   :doc
   "Given a database connection, a table name and either maps representing rows or\na list of column names followed by lists of column values, perform an insert.\nUse :transaction? argument to specify whether to run in a transaction or not.\nThe default is true (use a transaction). Use :entities to specify how to convert\nthe table name and column names to SQL entities.",
   :var-type "function",
   :line 1016,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([meta-query & opt-args]),
   :name "metadata-query",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L673",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/metadata-query",
   :doc
   "Given a Java expression that extracts metadata (in the context of with-db-metadata),\nand additional optional arguments like metadata-result, manage the connection for a\nsingle metadata-based query. Example usage:\n\n(with-db-metadata [meta db-spec]\n  (metadata-query (.getTables meta nil nil nil (into-array String [\"TABLE\"]))\n    :row-fn ...\n    :result-set-fn ...))",
   :var-type "macro",
   :line 673,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists
   ([rs-or-value
     &
     {:keys [identifiers as-arrays? row-fn result-set-fn],
      :or {identifiers str/lower-case, row-fn identity}}]),
   :name "metadata-result",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L655",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/metadata-result",
   :doc
   "If the argument is a java.sql.ResultSet, turn it into a result-set-seq,\nelse return it as-is. This makes working with metadata easier.\nAlso accepts :identifiers, :as-arrays?, :row-fn, and :result-set-fn\nto control how the ResultSet is transformed and returned.\nSee query for more details.",
   :var-type "function",
   :line 655,
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
       max-rows
       timeout]}]),
   :name "prepare-statement",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L428",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/prepare-statement",
   :doc
   "Create a prepared statement from a connection, a SQL string and an\noptional list of parameters:\n  :return-keys truthy | nil - default nil\n    for some drivers, this may be a vector of column names to identify\n    the generated keys to return, otherwise it should just be true\n  :result-type :forward-only | :scroll-insensitive | :scroll-sensitive\n  :concurrency :read-only | :updatable\n  :cursors\n  :fetch-size n\n  :max-rows n\n  :timeout n",
   :var-type "function",
   :line 428,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([exception]),
   :name "print-sql-exception",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L477",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/print-sql-exception",
   :doc "Prints the contents of an SQLException to *out*",
   :var-type "function",
   :line 477,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([exception]),
   :name "print-sql-exception-chain",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L491",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/print-sql-exception-chain",
   :doc "Prints a chain of SQLExceptions to *out*",
   :var-type "function",
   :line 491,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([exception]),
   :name "print-update-counts",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L503",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/print-update-counts",
   :doc
   "Prints the update counts from a BatchUpdateException to *out*",
   :var-type "function",
   :line 503,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists
   ([db-spec
     sql-and-params
     :as-arrays?
     false
     :identifiers
     clojure.string/lower-case
     :result-set-fn
     doall
     :row-fn
     identity]
    [db-spec
     sql-and-params
     :as-arrays?
     true
     :identifiers
     clojure.string/lower-case
     :result-set-fn
     vec
     :row-fn
     identity]
    [db-spec [sql-string & params]]
    [db-spec [stmt & params]]
    [db-spec [option-map sql-string & params]]),
   :name "query",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L832",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/query",
   :doc
   "Given a database connection and a vector containing SQL and optional parameters,\nperform a simple database query. The optional keyword arguments specify how to\nconstruct the result set:\n  :result-set-fn - applied to the entire result set, default doall / vec\n      if :as-arrays? true, :result-set-fn will default to vec\n      if :as-arrays? false, :result-set-fn will default to doall\n  :row-fn - applied to each row as the result set is constructed, default identity\n  :identifiers - applied to each column name in the result set, default lower-case\n  :as-arrays? - return the results as a set of arrays, default false.\nThe second argument is a vector containing a SQL string or PreparedStatement, followed\nby any parameters it needs. See db-query-with-resultset for details.",
   :var-type "function",
   :line 832,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([q] [q x]),
   :name "quoted",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L73",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/quoted",
   :doc
   "With a single argument, returns a naming strategy function that quotes\nnames. The single argument can either be a single character or a vector\npair of characters.\nCan also be called with two arguments - a quoting argument and a name -\nand returns the fully quoted string:\n  (quoted \\` \"foo\") will return \"`foo`\"\n  (quoted [\\[ \\]] \"foo\") will return \"[foo]\"",
   :var-type "function",
   :line 73,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists
   ([rs
     &
     {:keys [identifiers as-arrays?],
      :or {identifiers str/lower-case}}]),
   :name "result-set-seq",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L365",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/result-set-seq",
   :doc
   "Creates and returns a lazy sequence of maps corresponding to the rows in the\njava.sql.ResultSet rs. Loosely based on clojure.core/resultset-seq but it\nrespects the specified naming strategy. Duplicate column names are made unique\nby appending _N before applying the naming strategy (where N is a unique integer).",
   :var-type "function",
   :line 365,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists
   ([db
     table
     set-map
     where-clause
     &
     {:keys [entities transaction?],
      :or {entities identity, transaction? true}}]),
   :name "update!",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L1058",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/update!",
   :doc
   "Given a database connection, a table name, a map of column values to set and a\nwhere clause of columns to match, perform an update. The optional keyword arguments\nspecify how column names (in the set / match maps) should be transformed (default\n'as-is') and whether to run the update in a transaction (default true).\nExample:\n  (update! db :person {:zip 94540} [\"zip = ?\" 94546])\nis equivalent to:\n  (execute! db [\"UPDATE person SET zip = ? WHERE zip = ?\" 94540 94546])",
   :var-type "function",
   :line 1058,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([binding & body]),
   :name "with-db-connection",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L634",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/with-db-connection",
   :doc
   "Evaluates body in the context of an active connection to the database.\n(with-db-connection [con-db db-spec]\n  ... con-db ...)",
   :var-type "macro",
   :line 634,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([binding & body]),
   :name "with-db-metadata",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L644",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/with-db-metadata",
   :doc
   "Evaluates body in the context of an active connection with metadata bound\nto the specified name. See also metadata-result for dealing with the results\nof operations that retrieve information from the metadata.\n(with-db-metadata [md db-spec]\n  ... md ...)",
   :var-type "macro",
   :line 644,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([binding & body]),
   :name "with-db-transaction",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L620",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/with-db-transaction",
   :doc
   "Evaluates body in the context of a transaction on the specified database connection.\nThe binding provides the database connection for the transaction and the name to which\nthat is bound for evaluation of the body. The binding may also specify the isolation\nlevel for the transaction, via the :isolation option and/or set the transaction to\nreadonly via the :read-only? option.\n(with-db-transaction [t-con db-spec :isolation level :read-only? true]\n  ... t-con ...)\nSee db-transaction* for more details.",
   :var-type "macro",
   :line 620,
   :file "src/main/clojure/clojure/java/jdbc.clj"}
  {:file "src/main/clojure/clojure/java/jdbc.clj",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L347",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/IResultSetReadColumn",
   :namespace "clojure.java.jdbc",
   :line 347,
   :var-type "protocol",
   :doc
   "Protocol for reading objects from the java.sql.ResultSet. Default\nimplementations (for Object and nil) return the argument, and the\nBoolean implementation ensures a canonicalized true/false value,\nbut it can be extended to provide custom behavior for special types.",
   :name "IResultSetReadColumn"}
  {:file "src/main/clojure/clojure/java/jdbc.clj",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L328",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/ISQLParameter",
   :namespace "clojure.java.jdbc",
   :line 328,
   :var-type "protocol",
   :doc
   "Protocol for setting SQL parameters in statement objects, which\ncan convert from Clojure values. The default implementation just\ndelegates the conversion to ISQLValue's sql-value conversion and\nuses .setObject on the parameter. It can be extended to use other\nmethods of PreparedStatement to convert and set parameter values.",
   :name "ISQLParameter"}
  {:file "src/main/clojure/clojure/java/jdbc.clj",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/410cbdea52bec5ec7e7145f4cd2e51351ab468f6/src/main/clojure/clojure/java/jdbc.clj#L314",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/ISQLValue",
   :namespace "clojure.java.jdbc",
   :line 314,
   :var-type "protocol",
   :doc
   "Protocol for creating SQL values from Clojure values. Default\nimplementations (for Object and nil) just return the argument,\nbut it can be extended to provide custom behavior to support\nexotic types supported by different databases.",
   :name "ISQLValue"}
  {:file nil,
   :raw-source-url nil,
   :source-url nil,
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/result-set-read-column",
   :namespace "clojure.java.jdbc",
   :var-type "function",
   :arglists ([val rsmeta idx]),
   :doc
   "Function for transforming values after reading them from the database",
   :name "result-set-read-column"}
  {:file nil,
   :raw-source-url nil,
   :source-url nil,
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/set-parameter",
   :namespace "clojure.java.jdbc",
   :var-type "function",
   :arglists ([val stmt ix]),
   :doc
   "Convert a Clojure value into a SQL value and store it as the ix'th\nparameter in the given SQL statement object.",
   :name "set-parameter"}
  {:file nil,
   :raw-source-url nil,
   :source-url nil,
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/sql-value",
   :namespace "clojure.java.jdbc",
   :var-type "function",
   :arglists ([val]),
   :doc "Convert a Clojure value into a SQL value.",
   :name "sql-value"}
  {:arglists ([x] [x f-entity]),
   :name "as-identifier",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L88",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/as-identifier",
   :doc
   "Given a keyword, convert it to a string using the current naming\nstrategy.\nGiven a string, return it as-is.",
   :var-type "function",
   :line 88,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([f x]),
   :name "as-key",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L79",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/as-key",
   :doc
   "Given a naming strategy and a string, return the string as a\nkeyword per that naming strategy. Given (a naming strategy and)\na keyword, return it as-is.",
   :var-type "function",
   :line 79,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([x] [x f-keyword]),
   :name "as-keyword",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L95",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/as-keyword",
   :doc
   "Given an entity name (string), convert it to a keyword using the\ncurrent naming strategy.\nGiven a keyword, return it as-is.",
   :var-type "function",
   :line 95,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([naming-strategy x]),
   :name "as-named-identifier",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L254",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/as-named-identifier",
   :doc
   "Given a naming strategy and a keyword, return the keyword as a string using the \nentity naming strategy.\nGiven a naming strategy and a string, return the string as-is.\nThe naming strategy should either be a function (the entity naming strategy) or \na map containing :entity and/or :keyword keys which provide the entity naming\nstrategy and/or keyword naming strategy respectively.",
   :var-type "function",
   :line 254,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([naming-strategy x]),
   :name "as-named-keyword",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L264",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/as-named-keyword",
   :doc
   "Given a naming strategy and a string, return the string as a keyword using the \nkeyword naming strategy.\nGiven a naming strategy and a keyword, return the keyword as-is.\nThe naming strategy should either be a function (the entity naming strategy) or \na map containing :entity and/or :keyword keys which provide the entity naming\nstrategy and/or keyword naming strategy respectively.\nNote that providing a single function will cause the default keyword naming\nstrategy to be used!",
   :var-type "function",
   :line 264,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([q x]),
   :name "as-quoted-identifier",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L276",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/as-quoted-identifier",
   :doc
   "Given a quote pattern - either a single character or a pair of characters in\na vector - and a keyword, return the keyword as a string using a simple\nquoting naming strategy.\nGiven a qote pattern and a string, return the string as-is.\n  (as-quoted-identifier X :name) will return XnameX as a string.\n  (as-quoted-identifier [A B] :name) will return AnameB as a string.",
   :var-type "function",
   :line 276,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([q x]),
   :name "as-quoted-str",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L244",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/as-quoted-str",
   :doc
   "Given a quoting pattern - either a single character or a vector pair of\ncharacters - and a string, return the quoted string:\n  (as-quoted-str X foo) will return XfooX\n  (as-quoted-str [A B] foo) will return AfooB",
   :var-type "function",
   :line 244,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([f x]),
   :name "as-str",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L62",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/as-str",
   :doc
   "Given a naming strategy and a keyword, return the keyword as a\nstring per that naming strategy. Given (a naming strategy and)\na string, return it as-is.\nA keyword of the form :x.y is treated as keywords :x and :y,\nboth are turned into strings via the naming strategy and then\njoined back together so :x.y might become `x`.`y` if the naming\nstrategy quotes identifiers with `.",
   :var-type "function",
   :line 62,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([]),
   :name "connection",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L130",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/connection",
   :doc
   "Returns the current database connection (or throws if there is none)",
   :var-type "function",
   :line 130,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([name & specs]),
   :name "create-table",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L527",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/create-table",
   :doc
   "Creates a table on the open database connection given a table name and\nspecs. Each spec is either a column spec: a vector containing a column\nname and optionally a type and other constraints, or a table-level\nconstraint: a vector containing words that express the constraint. An\noptional suffix to the CREATE TABLE DDL describing table attributes may\nby provided as :table-spec {table-attributes-string}. All words used to\ndescribe the table may be supplied as strings or keywords.",
   :var-type "function",
   :line 527,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([name & specs]),
   :name "create-table-ddl",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L508",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/create-table-ddl",
   :doc
   "Given a table name and column specs with an optional table-spec\nreturn the DDL string for creating a table based on that.",
   :var-type "function",
   :line 508,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([table where-params]),
   :name "delete-rows",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L609",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/delete-rows",
   :doc
   "Deletes rows from a table. where-params is a vector containing a string\nproviding the (optionally parameterized) selection criteria followed by\nvalues for any parameters.",
   :var-type "function",
   :line 609,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([& commands]),
   :name "do-commands",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L428",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/do-commands",
   :doc "Executes SQL commands on the open database connection.",
   :var-type "function",
   :line 428,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([sql & param-groups]),
   :name "do-prepared",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L493",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/do-prepared",
   :doc
   "Executes an (optionally parameterized) SQL prepared statement on the\nopen database connection. Each param-group is a seq of values for all of\nthe parameters.\nReturn a seq of update counts (one count for each param-group).",
   :var-type "function",
   :line 493,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([sql param-group]),
   :name "do-prepared-return-keys",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L545",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/do-prepared-return-keys",
   :doc
   "Executes an (optionally parameterized) SQL prepared statement on the\nopen database connection. The param-group is a seq of values for all of\nthe parameters.\nReturn the generated keys for the (single) update/insert.",
   :var-type "function",
   :line 545,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([name]),
   :name "drop-table",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L538",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/drop-table",
   :doc
   "Drops a table on the open database connection given its name, a string\nor keyword",
   :var-type "function",
   :line 538,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([]),
   :name "find-connection",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L125",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/find-connection",
   :doc
   "Returns the current database connection (or nil if there is none)",
   :var-type "function",
   :line 125,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([table record]),
   :name "insert-record",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L601",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/insert-record",
   :doc
   "Inserts a single record into a table. A record is a map from strings or\nkeywords (identifying columns) to values.\nReturns a map of the generated keys.",
   :var-type "function",
   :line 601,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([table & records]),
   :name "insert-records",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L593",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/insert-records",
   :doc
   "Inserts records into a table. records are maps from strings or keywords\n(identifying columns) to values. Inserts the records one at a time.\nReturns a sequence of maps containing the generated keys for each record.",
   :var-type "function",
   :line 593,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([table & rows]),
   :name "insert-rows",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L586",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/insert-rows",
   :doc
   "Inserts complete rows into a table. Each row is a vector of values for\neach of the table's columns in order.\nIf a single row is inserted, returns a map of the generated keys.",
   :var-type "function",
   :line 586,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([table column-names & value-groups]),
   :name "insert-values",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L565",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/insert-values",
   :doc
   "Inserts rows into a table with values for specified columns only.\ncolumn-names is a vector of strings or keywords identifying columns. Each\nvalue-group is a vector containing a value for each column in\norder. When inserting complete rows (all columns), consider using\ninsert-rows instead.\nIf a single set of values is inserted, returns a map of the generated keys.",
   :var-type "function",
   :line 565,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([]),
   :name "is-rollback-only",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L410",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/is-rollback-only",
   :doc
   "Returns true if the outermost transaction will rollback rather than\ncommit when complete",
   :var-type "function",
   :line 410,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
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
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L456",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/prepare-statement",
   :doc
   "Create a prepared statement from a connection, a SQL string and an\noptional list of parameters:\n  :return-keys true | false - default false\n  :result-type :forward-only | :scroll-insensitive | :scroll-sensitive\n  :concurrency :read-only | :updatable\n  :fetch-size n\n  :max-rows n",
   :var-type "function",
   :line 456,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([exception]),
   :name "print-sql-exception",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L692",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/print-sql-exception",
   :doc "Prints the contents of an SQLException to *out*",
   :var-type "function",
   :line 692,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([exception]),
   :name "print-sql-exception-chain",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L706",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/print-sql-exception-chain",
   :doc "Prints a chain of SQLExceptions to *out*",
   :var-type "function",
   :line 706,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([exception]),
   :name "print-update-counts",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L718",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/print-update-counts",
   :doc
   "Prints the update counts from a BatchUpdateException to *out*",
   :var-type "function",
   :line 718,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([rs]),
   :name "resultset-seq",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L218",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/resultset-seq",
   :doc
   "Creates and returns a lazy sequence of maps corresponding to\nthe rows in the java.sql.ResultSet rs. Based on clojure.core/resultset-seq\nbut it respects the current naming strategy. Duplicate column names are\nmade unique by appending _N before applying the naming strategy (where\nN is a unique integer).",
   :var-type "function",
   :line 218,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([]),
   :name "set-rollback-only",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L404",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/set-rollback-only",
   :doc
   "Marks the outermost transaction such that it will rollback rather than\ncommit when complete",
   :var-type "function",
   :line 404,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([& body]),
   :name "transaction",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L393",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/transaction",
   :doc
   "Evaluates body as a transaction on the open database connection. Any\nnested transactions are absorbed into the outermost transaction. By\ndefault, all database updates are committed together as a group after\nevaluating the outermost body, or rolled back on any uncaught\nexception. If set-rollback-only is called within scope of the outermost\ntransaction, the entire transaction will be rolled back rather than\ncommitted when complete.",
   :var-type "macro",
   :line 393,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([func]),
   :name "transaction*",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L353",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/transaction*",
   :doc
   "Evaluates func as a transaction on the open database connection. Any\nnested transactions are absorbed into the outermost transaction. By\ndefault, all database updates are committed together as a group after\nevaluating the outermost body, or rolled back on any uncaught\nexception. If rollback is set within scope of the outermost transaction,\nthe entire transaction will be rolled back rather than committed when\ncomplete.",
   :var-type "function",
   :line 353,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([table where-params record]),
   :name "update-or-insert-values",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L634",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/update-or-insert-values",
   :doc
   "Updates values on selected rows in a table, or inserts a new row when no\nexisting row matches the selection criteria. where-params is a vector\ncontaining a string providing the (optionally parameterized) selection\ncriteria followed by values for any parameters. record is a map from\nstrings or keywords (identifying columns) to updated values.",
   :var-type "function",
   :line 634,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([table where-params record]),
   :name "update-values",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L620",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/update-values",
   :doc
   "Updates values on selected rows in a table. where-params is a vector\ncontaining a string providing the (optionally parameterized) selection\ncriteria followed by values for any parameters. record is a map from\nstrings or keywords (identifying columns) to updated values.",
   :var-type "function",
   :line 620,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([db-spec & body]),
   :name "with-connection",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L309",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/with-connection",
   :doc
   "Evaluates body in the context of a new connection to a database then\ncloses the connection. db-spec is a map containing values for one of the\nfollowing parameter sets:\n\nFactory:\n  :factory     (required) a function of one argument, a map of params\n  (others)     (optional) passed to the factory function in a map\n\nDriverManager:\n  :subprotocol (required) a String, the jdbc subprotocol\n  :subname     (required) a String, the jdbc subname\n  :classname   (optional) a String, the jdbc driver class name\n  (others)     (optional) passed to the driver as properties.\n\nDataSource:\n  :datasource  (required) a javax.sql.DataSource\n  :username    (optional) a String\n  :password    (optional) a String, required if :username is supplied\n\nJNDI:\n  :name        (required) a String or javax.naming.Name\n  :environment (optional) a java.util.Map\n\nRaw:\n  :connection-uri (required) a String\n               Passed directly to DriverManager/getConnection\n\nURI:\n  Parsed JDBC connection string - see below\n\nString:\n  subprotocol://user:password@host:post/subname\n               An optional prefix of jdbc: is allowed.",
   :var-type "macro",
   :line 309,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([db-spec func]),
   :name "with-connection*",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L301",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/with-connection*",
   :doc
   "Evaluates func in the context of a new connection to a database then\ncloses the connection.",
   :var-type "function",
   :line 301,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([naming-strategy & body]),
   :name "with-naming-strategy",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L286",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/with-naming-strategy",
   :doc
   "Evaluates body in the context of a naming strategy.\nThe naming strategy is either a function - the entity naming strategy - or\na map containing :entity and/or :keyword keys which provide the entity naming\nstrategy and/or the keyword naming strategy respectively. The default entity\nnaming strategy is identity; the default keyword naming strategy is lower-case.",
   :var-type "macro",
   :line 286,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([results sql-params & body]),
   :name "with-query-results",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L680",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/with-query-results",
   :doc
   "Executes a query, then evaluates body with results bound to a seq of the\nresults. sql-params is a vector containing either:\n  [sql & params] - a SQL query, followed by any parameters it needs\n  [stmt & params] - a PreparedStatement, followed by any parameters it needs\n                    (the PreparedStatement already contains the SQL query)\n  [options sql & params] - options and a SQL query for creating a\n                    PreparedStatement, follwed by any parameters it needs\nSee prepare-statement for supported options.",
   :var-type "macro",
   :line 680,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([sql-params func]),
   :name "with-query-results*",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L647",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/with-query-results*",
   :doc
   "Executes a query, then evaluates func passing in a seq of the results as\nan argument. The first argument is a vector containing either:\n  [sql & params] - a SQL query, followed by any parameters it needs\n  [stmt & params] - a PreparedStatement, followed by any parameters it needs\n                    (the PreparedStatement already contains the SQL query)\n  [options sql & params] - options and a SQL query for creating a\n                    PreparedStatement, follwed by any parameters it needs\nSee prepare-statement for supported options.",
   :var-type "function",
   :line 647,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"}
  {:arglists ([q & body]),
   :name "with-quoted-identifiers",
   :namespace "clojure.java.jdbc.deprecated",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj#L296",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/954f33c70ac64ed139377973141ff86867e60e8e/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/with-quoted-identifiers",
   :doc
   "Evaluates body in the context of a simple quoting naming strategy.",
   :var-type "macro",
   :line 296,
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj"})}
