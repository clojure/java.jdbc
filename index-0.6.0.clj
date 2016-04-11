{:namespaces
 ({:doc
   "A Clojure interface to SQL databases via JDBC\n\nclojure.java.jdbc provides a simple abstraction for CRUD (create, read,\nupdate, delete) operations on a SQL database, along with basic transaction\nsupport. Basic DDL operations are also supported (create table, drop table,\naccess to table metadata).\n\nMaps are used to represent records, making it easy to store and retrieve\ndata. Results can be processed using any standard sequence operations.\n\nFor most operations, Java's PreparedStatement is used so your SQL and\nparameters can be represented as simple vectors where the first element\nis the SQL string, with ? for each parameter, and the remaining elements\nare the parameter values to be substituted. In general, operations return\nthe number of rows affected, except for a single record insert where any\ngenerated keys are returned (as a map).\n\nFor more documentation, see:\n\nhttp://clojure-doc.org/articles/ecosystem/java_jdbc/home.html\n\nAs of release 0.3.0, the API has undergone a major overhaul and most of the\noriginal API has been deprecated in favor of a more idiomatic API. The\noriginal API has been moved to java.jdbc.deprecated for backward\ncompatibility but it will be removed before a 1.0.0 release.",
   :author "Stephen C. Gilardi, Sean Corfield",
   :name "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc/clojure.java.jdbc-api.html",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj"}
  {:doc
   "A Clojure interface to SQL databases via JDBC\n\nThis namespace contains the old API (0.2.3) which was deprecated in the 0.3.0\nrelease and is provided for backward compatibility. This API will be removed\ncompletely before a 1.0.0 release so will need to migrate code to the new API\nbefore that release.\n\nclojure.java.jdbc provides a simple abstraction for CRUD (create, read,\nupdate, delete) operations on a SQL database, along with basic transaction\nsupport. Basic DDL operations are also supported (create table, drop table,\naccess to table metadata).\n\nMaps are used to represent records, making it easy to store and retrieve\ndata. Results can be processed using any standard sequence operations.\n\nFor most operations, Java's PreparedStatement is used so your SQL and\nparameters can be represented as simple vectors where the first element\nis the SQL string, with ? for each parameter, and the remaining elements\nare the parameter values to be substituted. In general, operations return\nthe number of rows affected, except for a single record insert where any\ngenerated keys are returned (as a map).",
   :author "Stephen C. Gilardi, Sean Corfield",
   :name "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc/clojure.java.jdbc.deprecated-api.html",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj"}),
 :vars
 ({:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "as-sql-name",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L56",
   :line 56,
   :var-type "function",
   :arglists ([f] [f x]),
   :doc
   "Given a naming strategy function and a keyword or string, return\na string per that naming strategy.\nA name of the form x.y is treated as multiple names, x, y, etc,\nand each are turned into strings via the naming strategy and then\njoined back together so x.y might become `x`.`y` if the naming\nstrategy quotes identifiers with `.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/as-sql-name"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "create-table-ddl",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L1233",
   :line 1233,
   :var-type "function",
   :arglists
   ([table specs]
    [table specs opts]
    [table spec1 spec2 spec3 & specs]),
   :doc
   "Given a table name and column specs return the DDL string for creating that\ntable. An options map may be provided that can contain:\n:table-spec -- a string that is appended to the DDL -- and/or\n:entities -- a function to specify how column names are transformed.\nFor backward compatibility, those options may be specified inline or via a\nmap introduced with the delimiting keyword :options (but that is deprecated).\nIn addition, also for backward compatibility, the column specs may be\nspecified as individual arguments rather than collected into a vector (but\nthat is also deprecated).",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/create-table-ddl"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "db-connection",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L543",
   :line 543,
   :var-type "function",
   :arglists ([db]),
   :doc
   "Returns the current database connection (or throws if there is none)",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-connection"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "db-do-commands",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L700",
   :line 700,
   :var-type "function",
   :arglists
   ([db sql-commands]
    [db transaction? sql-commands]
    [db t-or-cmd cmd-1 cmd-2 & cmds]),
   :doc
   "Executes SQL commands on the specified database connection. Wraps the commands\nin a transaction if transaction? is true. transaction? can be ommitted and it\ndefaults to true. Accepts a single SQL command (string) or a vector of them.\nFor backward compatibility, also accepts multiple string arguments but that\nis deprecated and will be removed in 0.6.0.\nUses executeBatch. This may affect what SQL you can run via db-do-commands.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-do-commands"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "db-do-prepared",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L801",
   :line 801,
   :var-type "function",
   :arglists
   ([db sql-param-groups]
    [db transaction? sql-param-groups]
    [db t-or-sql-p-g sql-pg-1 pg-2 & pgs]),
   :doc
   "Executes an (optionally parameterized) SQL prepared statement on the\nopen database connection. Each param-group is a seq of values for all of\nthe parameters. transaction? can be omitted and defaults to true.\nThe sql parameter can either be a SQL string or a PreparedStatement.\nReturn a seq of update counts (one count for each param-group).",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-do-prepared"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "db-do-prepared-return-keys",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L758",
   :line 758,
   :var-type "function",
   :arglists
   ([db sql] [db sql param-group] [db transaction? sql param-group]),
   :doc
   "Executes an (optionally parameterized) SQL prepared statement on the\nopen database connection. The param-group is a seq of values for all of\nthe parameters. transaction? can be ommitted and will default to true.\nReturn the generated keys for the (single) update/insert.\nA PreparedStatement may be passed in, instead of a SQL string, in which\ncase :return-keys MUST BE SET on that PreparedStatement!",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-do-prepared-return-keys"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "db-find-connection",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L537",
   :line 537,
   :var-type "function",
   :arglists ([db]),
   :doc
   "Returns the current database connection (or nil if there is none)",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-find-connection"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "db-is-rollback-only",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L560",
   :line 560,
   :var-type "function",
   :arglists ([db]),
   :doc
   "Returns true if the outermost transaction will rollback rather than\ncommit when complete",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-is-rollback-only"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "db-query-with-resultset",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L832",
   :line 832,
   :var-type "function",
   :arglists
   ([db-spec [sql-string & params] func]
    [db-spec [stmt & params] func]
    [db-spec [options-map sql-string & params] func]),
   :doc
   "Executes a query, then evaluates func passing in the raw ResultSet as an\n argument. The second argument is a vector containing either:\n  [sql & params] - a SQL query, followed by any parameters it needs\n  [stmt & params] - a PreparedStatement, followed by any parameters it needs\n                    (the PreparedStatement already contains the SQL query)\n  [options sql & params] - options and a SQL query for creating a\n                    PreparedStatement, followed by any parameters it needs\nSee prepare-statement for supported options.\nUses executeQuery. This may affect what SQL you can run via query.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-query-with-resultset"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "db-set-rollback-only!",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L549",
   :line 549,
   :var-type "function",
   :arglists ([db]),
   :doc
   "Marks the outermost transaction such that it will rollback rather than\ncommit when complete",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-set-rollback-only!"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "db-transaction",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L1283",
   :line 1283,
   :deprecated "0.3.0",
   :var-type "macro",
   :arglists ([binding & body]),
   :doc "Original name for with-db-transaction. Use that instead.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-transaction"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "db-transaction*",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L574",
   :line 574,
   :var-type "function",
   :arglists
   ([db func]
    [db func {:keys [isolation read-only?], :as opts}]
    [db func k v & kvs]),
   :doc
   "Evaluates func as a transaction on the open database connection. Any\nnested transactions are absorbed into the outermost transaction. By\ndefault, all database updates are committed together as a group after\nevaluating the outermost body, or rolled back on any uncaught\nexception. If rollback is set within scope of the outermost transaction,\nthe entire transaction will be rolled back rather than committed when\ncomplete.\nThe isolation option may be :none, :read-committed, :read-uncommitted,\n:repeatable-read, or :serializable. Note that not all databases support\nall of those isolation levels, and may either throw an exception or\nsubstitute another isolation level.\nThe read-only? option puts the transaction in readonly mode (if supported).",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-transaction*"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "db-unset-rollback-only!",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L555",
   :line 555,
   :var-type "function",
   :arglists ([db]),
   :doc
   "Marks the outermost transaction such that it will not rollback when complete",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/db-unset-rollback-only!"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "delete!",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L964",
   :line 964,
   :var-type "function",
   :arglists
   ([db table where-clause]
    [db
     table
     where-clause
     {:keys [entities transaction?],
      :or {entities identity, transaction? true}}]
    [db table where-clause k v & kvs]),
   :doc
   "Given a database connection, a table name and a where clause of columns to match,\nperform a delete. The options may specify how to transform column names in the\nmap (default 'as-is') and whether to run the delete in a transaction (default true).\nExample:\n  (delete! db :person [\"zip = ?\" 94546])\nis equivalent to:\n  (execute! db [\"DELETE FROM person WHERE zip = ?\" 94546])",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/delete!"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "drop-table-ddl",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L1274",
   :line 1274,
   :var-type "function",
   :arglists
   ([name]
    [name {:keys [entities], :or {entities identity}}]
    [name k v & kvs]),
   :doc
   "Given a table name, return the DDL string for dropping that table.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/drop-table-ddl"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "execute!",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L913",
   :line 913,
   :var-type "function",
   :arglists
   ([db sql-params]
    [db
     sql-params
     {:keys [transaction? multi?],
      :or {transaction? true, multi? false}}]
    [db sql-params k v & kvs]),
   :doc
   "Given a database connection and a vector containing SQL (or PreparedStatement)\nfollowed by optional parameters, perform a general (non-select) SQL operation.\nThe :transaction? option specifies whether to run the operation in a\ntransaction or not (default true).\nIf the :multi? option is false (the default), the SQL statement should be\nfollowed by the parameters for that statement.\nIf the :multi? option is true, the SQL statement should be followed by one or\nmore vectors of parameters, one for each application of the SQL statement.\nIf there are no parameters specified, executeUpdate will be used, otherwise\nexecuteBatch will be used. This may affect what SQL you can run via execute!",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/execute!"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "get-connection",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L176",
   :line 176,
   :var-type "function",
   :arglists
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
   :doc
   "Creates a connection to a database. db-spec is usually a map containing connection\nparameters but can also be a URI or a String. The various possibilities are described\nbelow:\n\nExisting Connection:\n  :connection  (required) an existing open connection that can be used\n               but cannot be closed (only the parent connection can be closed)\n\nFactory:\n  :factory     (required) a function of one argument, a map of params\n  (others)     (optional) passed to the factory function in a map\n\nDriverManager:\n  :subprotocol (required) a String, the jdbc subprotocol\n  :subname     (required) a String, the jdbc subname\n  :classname   (optional) a String, the jdbc driver class name\n  (others)     (optional) passed to the driver as properties.\n\nDriverManager (alternative):\n  :dbtype      (required) a String, the type of the database (the jdbc subprotocol)\n  :dbname      (required) a String, the name of the database\n  :host        (optional) a String, the host name/IP of the database\n                          (defaults to 127.0.0.1)\n  :port        (optional) a Long, the port of the database\n                          (defaults to 3306 for mysql, 1433 for mssql/jtds, else nil)\n  (others)     (optional) passed to the driver as properties.\n\nDataSource:\n  :datasource  (required) a javax.sql.DataSource\n  :username    (optional) a String\n  :user        (optional) a String - an alternate alias for :username\n                          (added after 0.3.0-beta2 for consistency JDBC-74)\n  :password    (optional) a String, required if :username is supplied\n\nJNDI:\n  :name        (required) a String or javax.naming.Name\n  :environment (optional) a java.util.Map\n\nRaw:\n  :connection-uri (required) a String\n               Passed directly to DriverManager/getConnection\n\nURI:\n  Parsed JDBC connection string - see below\n\nString:\n  subprotocol://user:password@host:post/subname\n               An optional prefix of jdbc: is allowed.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/get-connection"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "insert!",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L1114",
   :line 1114,
   :var-type "function",
   :arglists
   ([db-spec table row-map & row-maps]
    [db-spec table col-name-vec col-val-vec & col-val-vecs]
    [db-spec table rows-or-col-name-val-vecs :options options]),
   :doc
   "Given a database connection, a table name and either maps representing rows or\na list of column names followed by lists of column values, perform an insert.\nThe rows or columns may be followed by :options and a map of options (or, for\nbackward compatibility, just the unrolled options instead, but that is deprecated).\nThe :transaction? option specifies whether to run in a transaction or not.\nThe default is true (use a transaction). The :entities option specifies how\nto convert the table name and column names to SQL entities.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/insert!"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "insert-multi!",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L1174",
   :line 1174,
   :var-type "function",
   :arglists
   ([db table rows]
    [db table cols-or-rows values-or-opts]
    [db table cols values opts]),
   :doc
   "Given a database connection, a table name and either a sequence of maps (for\nrows) or a sequence of column names, followed by a sequence of vectors (for\nthe values in each row), and possibly a map of options, insert that data into\nthe database.\nThe :transaction? option specifies whether to run in a transaction or not.\nThe default is true (use a transaction). The :entities option specifies how\nto convert the table name and column names to SQL entities.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/insert-multi!"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "legacy-create-table-ddl",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L1266",
   :line 1266,
   :var-type "function",
   :arglists ([table & specs]),
   :doc "Deprecated version of create-table-ddl.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/legacy-create-table-ddl"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "metadata-query",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L688",
   :line 688,
   :var-type "macro",
   :arglists ([meta-query & opt-args]),
   :doc
   "Given a Java expression that extracts metadata (in the context of with-db-metadata),\nand a map of options like metadata-result, manage the connection for a single\nmetadata-based query. Example usage:\n\n(with-db-metadata [meta db-spec]\n  (metadata-query (.getTables meta nil nil nil (into-array String [\"TABLE\"]))\n    {:row-fn ... :result-set-fn ...}))",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/metadata-query"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "metadata-result",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L666",
   :line 666,
   :var-type "function",
   :arglists
   ([rs-or-value]
    [rs-or-value
     {:keys [identifiers as-arrays? row-fn result-set-fn],
      :or {identifiers lower-case, row-fn identity}}]
    [rs-or-value k v & kvs]),
   :doc
   "If the argument is a java.sql.ResultSet, turn it into a result-set-seq,\nelse return it as-is. This makes working with metadata easier.\nAlso accepts an option map containing :identifiers, :as-arrays?, :row-fn,\nand :result-set-fn to control how the ResultSet is transformed and returned.\nSee query for more details.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/metadata-result"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "prepare-statement",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L440",
   :line 440,
   :var-type "function",
   :arglists
   ([con sql]
    [con
     sql
     {:keys
      [return-keys
       result-type
       concurrency
       cursors
       fetch-size
       max-rows
       timeout]}]
    [con sql k v & kvs]),
   :doc
   "Create a prepared statement from a connection, a SQL string and a map\nof options:\n   :return-keys truthy | nil - default nil\n     for some drivers, this may be a vector of column names to identify\n     the generated keys to return, otherwise it should just be true\n   :result-type :forward-only | :scroll-insensitive | :scroll-sensitive\n   :concurrency :read-only | :updatable\n   :cursors\n   :fetch-size n\n   :max-rows n\n   :timeout n",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/prepare-statement"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "print-sql-exception",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L497",
   :line 497,
   :var-type "function",
   :arglists ([exception]),
   :doc "Prints the contents of an SQLException to *out*",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/print-sql-exception"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "print-sql-exception-chain",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L511",
   :line 511,
   :var-type "function",
   :arglists ([exception]),
   :doc "Prints a chain of SQLExceptions to *out*",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/print-sql-exception-chain"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "print-update-counts",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L523",
   :line 523,
   :var-type "function",
   :arglists ([exception]),
   :doc
   "Prints the update counts from a BatchUpdateException to *out*",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/print-update-counts"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "query",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L880",
   :line 880,
   :var-type "function",
   :arglists
   ([db sql-params]
    [db
     sql-params
     {:keys [result-set-fn row-fn identifiers as-arrays?],
      :or {row-fn identity, identifiers lower-case}}]
    [db sql-params k v & kvs]),
   :doc
   "Given a database connection and a vector containing SQL and optional parameters,\nperform a simple database query. The options specify how to construct the result\nset:\n  :result-set-fn - applied to the entire result set, default doall / vec\n      if :as-arrays? true, :result-set-fn will default to vec\n      if :as-arrays? false, :result-set-fn will default to doall\n  :row-fn - applied to each row as the result set is constructed, default identity\n  :identifiers - applied to each column name in the result set, default lower-case\n  :as-arrays? - return the results as a set of arrays, default false.\nThe second argument is a vector containing a SQL string or PreparedStatement, followed\nby any parameters it needs. It may optionally include an options map before the SQL\nstring or PreparedStatement. See db-query-with-resultset for details.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/query"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "quoted",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L73",
   :line 73,
   :var-type "function",
   :arglists ([q] [q x]),
   :doc
   "With a single argument, returns a naming strategy function that quotes\nnames. The single argument can either be a single character or a vector\npair of characters.\nCan also be called with two arguments - a quoting argument and a name -\nand returns the fully quoted string:\n  (quoted \\` \"foo\") will return \"`foo`\"\n  (quoted [\\[ \\]] \"foo\") will return \"[foo]\"",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/quoted"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "result-set-seq",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L365",
   :line 365,
   :var-type "function",
   :arglists
   ([rs]
    [rs {:keys [identifiers as-arrays?], :or {identifiers lower-case}}]
    [rs k v & kvs]),
   :doc
   "Creates and returns a lazy sequence of maps corresponding to the rows in the\njava.sql.ResultSet rs. Loosely based on clojure.core/resultset-seq but it\nrespects the specified naming strategy. Duplicate column names are made unique\nby appending _N before applying the naming strategy (where N is a unique integer),\nunless the :as-arrays? option is :cols-as-is, in which case the column names\nare untouched (the result set maintains column name/value order).\nThe :identifiers option specifies how SQL column names are converted to Clojure\nkeywords. The default is to convert them to lower case.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/result-set-seq"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "update!",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L1212",
   :line 1212,
   :var-type "function",
   :arglists
   ([db table set-map where-clause]
    [db
     table
     set-map
     where-clause
     {:keys [entities transaction?],
      :or {entities identity, transaction? true}}]
    [db table set-map where-clause k v & kvs]),
   :doc
   "Given a database connection, a table name, a map of column values to set and a\nwhere clause of columns to match, perform an update. The options may specify\nhow column names (in the set / match maps) should be transformed (default\n'as-is') and whether to run the update in a transaction (default true).\nExample:\n  (update! db :person {:zip 94540} [\"zip = ?\" 94546])\nis equivalent to:\n  (execute! db [\"UPDATE person SET zip = ? WHERE zip = ?\" 94540 94546])",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/update!"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "with-db-connection",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L645",
   :line 645,
   :var-type "macro",
   :arglists ([binding & body]),
   :doc
   "Evaluates body in the context of an active connection to the database.\n(with-db-connection [con-db db-spec]\n  ... con-db ...)",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/with-db-connection"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "with-db-metadata",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L655",
   :line 655,
   :var-type "macro",
   :arglists ([binding & body]),
   :doc
   "Evaluates body in the context of an active connection with metadata bound\nto the specified name. See also metadata-result for dealing with the results\nof operations that retrieve information from the metadata.\n(with-db-metadata [md db-spec]\n  ... md ...)",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/with-db-metadata"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "with-db-transaction",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L631",
   :line 631,
   :var-type "macro",
   :arglists ([binding & body]),
   :doc
   "Evaluates body in the context of a transaction on the specified database connection.\nThe binding provides the database connection for the transaction and the name to which\nthat is bound for evaluation of the body. The binding may also specify the isolation\nlevel for the transaction, via the :isolation option and/or set the transaction to\nreadonly via the :read-only? option.\n(with-db-transaction [t-con db-spec {:isolation level :read-only? true}]\n  ... t-con ...)\nSee db-transaction* for more details.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/with-db-transaction"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "IResultSetReadColumn",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L347",
   :line 347,
   :var-type "protocol",
   :arglists nil,
   :doc
   "Protocol for reading objects from the java.sql.ResultSet. Default\nimplementations (for Object and nil) return the argument, and the\nBoolean implementation ensures a canonicalized true/false value,\nbut it can be extended to provide custom behavior for special types.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/IResultSetReadColumn"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "ISQLParameter",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L328",
   :line 328,
   :var-type "protocol",
   :arglists nil,
   :doc
   "Protocol for setting SQL parameters in statement objects, which\ncan convert from Clojure values. The default implementation just\ndelegates the conversion to ISQLValue's sql-value conversion and\nuses .setObject on the parameter. It can be extended to use other\nmethods of PreparedStatement to convert and set parameter values.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/ISQLParameter"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj",
   :name "ISQLValue",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/441e32a4440f572b64e8fb93b713a62525c5e610/src/main/clojure/clojure/java/jdbc.clj#L314",
   :line 314,
   :var-type "protocol",
   :arglists nil,
   :doc
   "Protocol for creating SQL values from Clojure values. Default\nimplementations (for Object and nil) just return the argument,\nbut it can be extended to provide custom behavior to support\nexotic types supported by different databases.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/ISQLValue"}
  {:name "result-set-read-column",
   :doc
   "Function for transforming values after reading them from the database",
   :var-type "function",
   :namespace "clojure.java.jdbc",
   :arglists ([val rsmeta idx]),
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/result-set-read-column",
   :source-url nil,
   :raw-source-url nil,
   :file nil}
  {:name "set-parameter",
   :doc
   "Convert a Clojure value into a SQL value and store it as the ix'th\nparameter in the given SQL statement object.",
   :var-type "function",
   :namespace "clojure.java.jdbc",
   :arglists ([val stmt ix]),
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/set-parameter",
   :source-url nil,
   :raw-source-url nil,
   :file nil}
  {:name "sql-value",
   :doc "Convert a Clojure value into a SQL value.",
   :var-type "function",
   :namespace "clojure.java.jdbc",
   :arglists ([val]),
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/sql-value",
   :source-url nil,
   :raw-source-url nil,
   :file nil}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "as-identifier",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L88",
   :line 88,
   :var-type "function",
   :arglists ([x] [x f-entity]),
   :doc
   "Given a keyword, convert it to a string using the current naming\nstrategy.\nGiven a string, return it as-is.",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/as-identifier"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "as-key",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L79",
   :line 79,
   :var-type "function",
   :arglists ([f x]),
   :doc
   "Given a naming strategy and a string, return the string as a\nkeyword per that naming strategy. Given (a naming strategy and)\na keyword, return it as-is.",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/as-key"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "as-keyword",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L95",
   :line 95,
   :var-type "function",
   :arglists ([x] [x f-keyword]),
   :doc
   "Given an entity name (string), convert it to a keyword using the\ncurrent naming strategy.\nGiven a keyword, return it as-is.",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/as-keyword"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "as-named-identifier",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L255",
   :line 255,
   :var-type "function",
   :arglists ([naming-strategy x]),
   :doc
   "Given a naming strategy and a keyword, return the keyword as a string using the\nentity naming strategy.\nGiven a naming strategy and a string, return the string as-is.\nThe naming strategy should either be a function (the entity naming strategy) or\na map containing :entity and/or :keyword keys which provide the entity naming\nstrategy and/or keyword naming strategy respectively.",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/as-named-identifier"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "as-named-keyword",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L265",
   :line 265,
   :var-type "function",
   :arglists ([naming-strategy x]),
   :doc
   "Given a naming strategy and a string, return the string as a keyword using the\nkeyword naming strategy.\nGiven a naming strategy and a keyword, return the keyword as-is.\nThe naming strategy should either be a function (the entity naming strategy) or\na map containing :entity and/or :keyword keys which provide the entity naming\nstrategy and/or keyword naming strategy respectively.\nNote that providing a single function will cause the default keyword naming\nstrategy to be used!",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/as-named-keyword"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "as-quoted-identifier",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L277",
   :line 277,
   :var-type "function",
   :arglists ([q x]),
   :doc
   "Given a quote pattern - either a single character or a pair of characters in\na vector - and a keyword, return the keyword as a string using a simple\nquoting naming strategy.\nGiven a qote pattern and a string, return the string as-is.\n  (as-quoted-identifier X :name) will return XnameX as a string.\n  (as-quoted-identifier [A B] :name) will return AnameB as a string.",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/as-quoted-identifier"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "as-quoted-str",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L245",
   :line 245,
   :var-type "function",
   :arglists ([q x]),
   :doc
   "Given a quoting pattern - either a single character or a vector pair of\ncharacters - and a string, return the quoted string:\n  (as-quoted-str X foo) will return XfooX\n  (as-quoted-str [A B] foo) will return AfooB",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/as-quoted-str"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "as-str",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L62",
   :line 62,
   :var-type "function",
   :arglists ([f x]),
   :doc
   "Given a naming strategy and a keyword, return the keyword as a\nstring per that naming strategy. Given (a naming strategy and)\na string, return it as-is.\nA keyword of the form :x.y is treated as keywords :x and :y,\nboth are turned into strings via the naming strategy and then\njoined back together so :x.y might become `x`.`y` if the naming\nstrategy quotes identifiers with `.",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/as-str"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "connection",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L131",
   :line 131,
   :var-type "function",
   :arglists ([]),
   :doc
   "Returns the current database connection (or throws if there is none)",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/connection"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "create-table",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L528",
   :line 528,
   :var-type "function",
   :arglists ([name & specs]),
   :doc
   "Creates a table on the open database connection given a table name and\nspecs. Each spec is either a column spec: a vector containing a column\nname and optionally a type and other constraints, or a table-level\nconstraint: a vector containing words that express the constraint. An\noptional suffix to the CREATE TABLE DDL describing table attributes may\nby provided as :table-spec {table-attributes-string}. All words used to\ndescribe the table may be supplied as strings or keywords.",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/create-table"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "create-table-ddl",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L509",
   :line 509,
   :var-type "function",
   :arglists ([name & specs]),
   :doc
   "Given a table name and column specs with an optional table-spec\nreturn the DDL string for creating a table based on that.",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/create-table-ddl"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "delete-rows",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L610",
   :line 610,
   :var-type "function",
   :arglists ([table where-params]),
   :doc
   "Deletes rows from a table. where-params is a vector containing a string\nproviding the (optionally parameterized) selection criteria followed by\nvalues for any parameters.",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/delete-rows"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "do-commands",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L429",
   :line 429,
   :var-type "function",
   :arglists ([& commands]),
   :doc "Executes SQL commands on the open database connection.",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/do-commands"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "do-prepared",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L494",
   :line 494,
   :var-type "function",
   :arglists ([sql & param-groups]),
   :doc
   "Executes an (optionally parameterized) SQL prepared statement on the\nopen database connection. Each param-group is a seq of values for all of\nthe parameters.\nReturn a seq of update counts (one count for each param-group).",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/do-prepared"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "do-prepared-return-keys",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L546",
   :line 546,
   :var-type "function",
   :arglists ([sql param-group]),
   :doc
   "Executes an (optionally parameterized) SQL prepared statement on the\nopen database connection. The param-group is a seq of values for all of\nthe parameters.\nReturn the generated keys for the (single) update/insert.",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/do-prepared-return-keys"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "drop-table",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L539",
   :line 539,
   :var-type "function",
   :arglists ([name]),
   :doc
   "Drops a table on the open database connection given its name, a string\nor keyword",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/drop-table"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "find-connection",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L126",
   :line 126,
   :var-type "function",
   :arglists ([]),
   :doc
   "Returns the current database connection (or nil if there is none)",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/find-connection"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "insert-record",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L602",
   :line 602,
   :var-type "function",
   :arglists ([table record]),
   :doc
   "Inserts a single record into a table. A record is a map from strings or\nkeywords (identifying columns) to values.\nReturns a map of the generated keys.",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/insert-record"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "insert-records",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L594",
   :line 594,
   :var-type "function",
   :arglists ([table & records]),
   :doc
   "Inserts records into a table. records are maps from strings or keywords\n(identifying columns) to values. Inserts the records one at a time.\nReturns a sequence of maps containing the generated keys for each record.",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/insert-records"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "insert-rows",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L587",
   :line 587,
   :var-type "function",
   :arglists ([table & rows]),
   :doc
   "Inserts complete rows into a table. Each row is a vector of values for\neach of the table's columns in order.\nIf a single row is inserted, returns a map of the generated keys.",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/insert-rows"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "insert-values",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L566",
   :line 566,
   :var-type "function",
   :arglists ([table column-names & value-groups]),
   :doc
   "Inserts rows into a table with values for specified columns only.\ncolumn-names is a vector of strings or keywords identifying columns. Each\nvalue-group is a vector containing a value for each column in\norder. When inserting complete rows (all columns), consider using\ninsert-rows instead.\nIf a single set of values is inserted, returns a map of the generated keys.",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/insert-values"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "is-rollback-only",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L411",
   :line 411,
   :var-type "function",
   :arglists ([]),
   :doc
   "Returns true if the outermost transaction will rollback rather than\ncommit when complete",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/is-rollback-only"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "prepare-statement",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L457",
   :line 457,
   :var-type "function",
   :arglists
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
   :doc
   "Create a prepared statement from a connection, a SQL string and an\noptional list of parameters:\n  :return-keys true | false - default false\n  :result-type :forward-only | :scroll-insensitive | :scroll-sensitive\n  :concurrency :read-only | :updatable\n  :fetch-size n\n  :max-rows n",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/prepare-statement"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "print-sql-exception",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L693",
   :line 693,
   :var-type "function",
   :arglists ([exception]),
   :doc "Prints the contents of an SQLException to *out*",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/print-sql-exception"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "print-sql-exception-chain",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L707",
   :line 707,
   :var-type "function",
   :arglists ([exception]),
   :doc "Prints a chain of SQLExceptions to *out*",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/print-sql-exception-chain"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "print-update-counts",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L719",
   :line 719,
   :var-type "function",
   :arglists ([exception]),
   :doc
   "Prints the update counts from a BatchUpdateException to *out*",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/print-update-counts"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "resultset-seq",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L219",
   :line 219,
   :var-type "function",
   :arglists ([rs]),
   :doc
   "Creates and returns a lazy sequence of maps corresponding to\nthe rows in the java.sql.ResultSet rs. Based on clojure.core/resultset-seq\nbut it respects the current naming strategy. Duplicate column names are\nmade unique by appending _N before applying the naming strategy (where\nN is a unique integer).",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/resultset-seq"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "set-rollback-only",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L405",
   :line 405,
   :var-type "function",
   :arglists ([]),
   :doc
   "Marks the outermost transaction such that it will rollback rather than\ncommit when complete",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/set-rollback-only"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "transaction",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L394",
   :line 394,
   :var-type "macro",
   :arglists ([& body]),
   :doc
   "Evaluates body as a transaction on the open database connection. Any\nnested transactions are absorbed into the outermost transaction. By\ndefault, all database updates are committed together as a group after\nevaluating the outermost body, or rolled back on any uncaught\nexception. If set-rollback-only is called within scope of the outermost\ntransaction, the entire transaction will be rolled back rather than\ncommitted when complete.",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/transaction"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "transaction*",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L354",
   :line 354,
   :var-type "function",
   :arglists ([func]),
   :doc
   "Evaluates func as a transaction on the open database connection. Any\nnested transactions are absorbed into the outermost transaction. By\ndefault, all database updates are committed together as a group after\nevaluating the outermost body, or rolled back on any uncaught\nexception. If rollback is set within scope of the outermost transaction,\nthe entire transaction will be rolled back rather than committed when\ncomplete.",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/transaction*"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "update-or-insert-values",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L635",
   :line 635,
   :var-type "function",
   :arglists ([table where-params record]),
   :doc
   "Updates values on selected rows in a table, or inserts a new row when no\nexisting row matches the selection criteria. where-params is a vector\ncontaining a string providing the (optionally parameterized) selection\ncriteria followed by values for any parameters. record is a map from\nstrings or keywords (identifying columns) to updated values.",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/update-or-insert-values"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "update-values",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L621",
   :line 621,
   :var-type "function",
   :arglists ([table where-params record]),
   :doc
   "Updates values on selected rows in a table. where-params is a vector\ncontaining a string providing the (optionally parameterized) selection\ncriteria followed by values for any parameters. record is a map from\nstrings or keywords (identifying columns) to updated values.",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/update-values"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "with-connection",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L310",
   :line 310,
   :var-type "macro",
   :arglists ([db-spec & body]),
   :doc
   "Evaluates body in the context of a new connection to a database then\ncloses the connection. db-spec is a map containing values for one of the\nfollowing parameter sets:\n\nFactory:\n  :factory     (required) a function of one argument, a map of params\n  (others)     (optional) passed to the factory function in a map\n\nDriverManager:\n  :subprotocol (required) a String, the jdbc subprotocol\n  :subname     (required) a String, the jdbc subname\n  :classname   (optional) a String, the jdbc driver class name\n  (others)     (optional) passed to the driver as properties.\n\nDataSource:\n  :datasource  (required) a javax.sql.DataSource\n  :username    (optional) a String\n  :password    (optional) a String, required if :username is supplied\n\nJNDI:\n  :name        (required) a String or javax.naming.Name\n  :environment (optional) a java.util.Map\n\nRaw:\n  :connection-uri (required) a String\n               Passed directly to DriverManager/getConnection\n\nURI:\n  Parsed JDBC connection string - see below\n\nString:\n  subprotocol://user:password@host:post/subname\n               An optional prefix of jdbc: is allowed.",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/with-connection"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "with-connection*",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L302",
   :line 302,
   :var-type "function",
   :arglists ([db-spec func]),
   :doc
   "Evaluates func in the context of a new connection to a database then\ncloses the connection.",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/with-connection*"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "with-naming-strategy",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L287",
   :line 287,
   :var-type "macro",
   :arglists ([naming-strategy & body]),
   :doc
   "Evaluates body in the context of a naming strategy.\nThe naming strategy is either a function - the entity naming strategy - or\na map containing :entity and/or :keyword keys which provide the entity naming\nstrategy and/or the keyword naming strategy respectively. The default entity\nnaming strategy is identity; the default keyword naming strategy is lower-case.",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/with-naming-strategy"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "with-query-results",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L681",
   :line 681,
   :var-type "macro",
   :arglists ([results sql-params & body]),
   :doc
   "Executes a query, then evaluates body with results bound to a seq of the\nresults. sql-params is a vector containing either:\n  [sql & params] - a SQL query, followed by any parameters it needs\n  [stmt & params] - a PreparedStatement, followed by any parameters it needs\n                    (the PreparedStatement already contains the SQL query)\n  [options sql & params] - options and a SQL query for creating a\n                    PreparedStatement, follwed by any parameters it needs\nSee prepare-statement for supported options.",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/with-query-results"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "with-query-results*",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L648",
   :line 648,
   :var-type "function",
   :arglists ([sql-params func]),
   :doc
   "Executes a query, then evaluates func passing in a seq of the results as\nan argument. The first argument is a vector containing either:\n  [sql & params] - a SQL query, followed by any parameters it needs\n  [stmt & params] - a PreparedStatement, followed by any parameters it needs\n                    (the PreparedStatement already contains the SQL query)\n  [options sql & params] - options and a SQL query for creating a\n                    PreparedStatement, follwed by any parameters it needs\nSee prepare-statement for supported options.",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/with-query-results*"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :name "with-quoted-identifiers",
   :file "src/main/clojure/clojure/java/jdbc/deprecated.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/59872ac51fd9f9167e7fd8fdb3663facee9bd336/src/main/clojure/clojure/java/jdbc/deprecated.clj#L297",
   :line 297,
   :var-type "macro",
   :arglists ([q & body]),
   :doc
   "Evaluates body in the context of a simple quoting naming strategy.",
   :namespace "clojure.java.jdbc.deprecated",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.deprecated/with-quoted-identifiers"})}
