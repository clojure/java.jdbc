{:namespaces
 ({:doc
   "A Clojure interface to SQL databases via JDBC\n\nclojure.java.jdbc provides a simple abstraction for CRUD (create, read,\nupdate, delete) operations on a SQL database, along with basic transaction\nsupport. Basic DDL operations are also supported (create table, drop table,\naccess to table metadata).\n\nMaps are used to represent records, making it easy to store and retrieve\ndata. Results can be processed using any standard sequence operations.\n\nFor most operations, Java's PreparedStatement is used so your SQL and\nparameters can be represented as simple vectors where the first element\nis the SQL string, with ? for each parameter, and the remaining elements\nare the parameter values to be substituted.\n\nFor more documentation, see:\n\nhttp://clojure-doc.org/articles/ecosystem/java_jdbc/home.html",
   :author "Stephen C. Gilardi, Sean Corfield",
   :name "clojure.java.jdbc",
   :wiki-url "https://clojure.github.io/java.jdbc/index.html",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj"}
  {:doc
   "Variants of 'query' functions from clojure.java.jdbc that support\nthe new clojure.datafy functionality in Clojure 1.10.\n\nThe whole schema/column lookup piece is very likely to change!\n\nCurrently, the :schema option for a 'query' function is a mapping\nfrom column name to a tuple of table name, key column, and optionally\nthe cardinality (:one -- the default -- or :many). The cardinality\ndetermines whether navigation should produce a single row (hash map)\nor a result set.\n\nOne of the problems is that the general case -- query -- doesn't\nhave any concept of an associated table name (and may of course\njoin across multiple tables), so there's no good way to take the\ntable name into account when mapping a column to another table.\n\nFor find-by-keys and get-by-id, you do have the starting table\nname so you could map [table1 column1] to [table2 column2] and have\ntable-specific mappings.\n\nThe obvious, logical thing would be to use SQL metadata to figure\nout actual foreign key constraints but not everyone uses them, for\na variety of reasons. For folks who do use them, they can build\ntheir schema structure from the database, and pass the relevant\npart of it to the functions below (via :schema in options).",
   :author "Sean Corfield",
   :name "clojure.java.jdbc.datafy",
   :wiki-url
   "https://clojure.github.io/java.jdbc/index.html#clojure.java.jdbc.datafy",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/79ad004b42e9234d2bbf54df6a254c22b1c68c72/src/main/clojure/clojure/java/jdbc/datafy.clj"}
  {:doc "Optional specifications for use with Clojure 1.9 or later.",
   :author "Sean Corfield",
   :name "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc/index.html#clojure.java.jdbc.spec",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/2374ea2dd85b2895dd98bc1d5a04aa0699e41d8d/src/main/clojure/clojure/java/jdbc/spec.clj"}),
 :vars
 ({:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "as-sql-name",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L53",
   :line 53,
   :var-type "function",
   :arglists ([f x]),
   :doc
   "Given a naming strategy function and a keyword or string, return\na string per that naming strategy.\nA name of the form x.y is treated as multiple names, x, y, etc,\nand each are turned into strings via the naming strategy and then\njoined back together so x.y might become `x`.`y` if the naming\nstrategy quotes identifiers with `.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/as-sql-name"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "create-table-ddl",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L1670",
   :line 1670,
   :var-type "function",
   :arglists ([table specs] [table specs opts]),
   :doc
   "Given a table name and a vector of column specs, return the DDL string for\ncreating that table. Each column spec is, in turn, a vector of keywords or\nstrings that is converted to strings and concatenated with spaces to form\na single column description in DDL, e.g.,\n  [:cost :int \"not null\"]\n  [:name \"varchar(32)\"]\nThe first element of a column spec is treated as a SQL entity (so if you\nprovide the :entities option, that will be used to transform it). The\nremaining elements are left as-is when converting them to strings.\nAn options map may be provided that can contain:\n:table-spec -- a string that is appended to the DDL -- and/or\n:entities -- a function to specify how column names are transformed.\n:conditional? -- either a boolean, indicating whether to add 'IF NOT EXISTS',\n  or a string, which is inserted literally before the table name, or a\n  function of two arguments (table name and the create statement), that can\n  manipulate the generated statement to better support other databases, e.g.,\n  MS SQL Server which need to wrap create table in an existence query.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/create-table-ddl"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "db-connection",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L724",
   :line 724,
   :var-type "function",
   :arglists ([db]),
   :doc
   "Returns the current database connection (or throws if there is none)",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/db-connection"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "db-do-commands",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L924",
   :line 924,
   :var-type "function",
   :arglists ([db sql-commands] [db transaction? sql-commands]),
   :doc
   "Executes SQL commands on the specified database connection. Wraps the commands\nin a transaction if transaction? is true. transaction? can be omitted and it\ndefaults to true. Accepts a single SQL command (string) or a vector of them.\nUses executeBatch. This may affect what SQL you can run via db-do-commands.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/db-do-commands"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "db-do-prepared",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L1042",
   :line 1042,
   :var-type "function",
   :arglists
   ([db sql-params]
    [db transaction? sql-params]
    [db transaction? sql-params opts]),
   :doc
   "Executes an (optionally parameterized) SQL prepared statement on the\nopen database connection. Each param-group is a seq of values for all of\nthe parameters. transaction? can be omitted and defaults to true.\nThe sql parameter can either be a SQL string or a PreparedStatement.\nReturn a seq of update counts (one count for each param-group).",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/db-do-prepared"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "db-do-prepared-return-keys",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L997",
   :line 997,
   :var-type "function",
   :arglists
   ([db sql-params]
    [db transaction? sql-params]
    [db transaction? sql-params opts]),
   :doc
   "Executes an (optionally parameterized) SQL prepared statement on the\nopen database connection. The param-group is a seq of values for all of\nthe parameters. transaction? can be omitted and will default to true.\nReturn the generated keys for the (single) update/insert.\nA PreparedStatement may be passed in, instead of a SQL string, in which\ncase :return-keys MUST BE SET on that PreparedStatement!",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/db-do-prepared-return-keys"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "db-find-connection",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L718",
   :line 718,
   :var-type "function",
   :arglists ([db]),
   :doc
   "Returns the current database connection (or nil if there is none)",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/db-find-connection"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "db-is-rollback-only",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L741",
   :line 741,
   :var-type "function",
   :arglists ([db]),
   :doc
   "Returns true if the outermost transaction will rollback rather than\ncommit when complete",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/db-is-rollback-only"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "db-query-with-resultset",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L1101",
   :line 1101,
   :var-type "function",
   :arglists ([db sql-params func] [db sql-params func opts]),
   :doc
   "Executes a query, then evaluates func passing in the raw ResultSet as an\n argument. The second argument is a vector containing either:\n  [sql & params] - a SQL query, followed by any parameters it needs\n  [stmt & params] - a PreparedStatement, followed by any parameters it needs\n                    (the PreparedStatement already contains the SQL query)\nThe opts map is passed to prepare-statement.\nUses executeQuery. This may affect what SQL you can run via query.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/db-query-with-resultset"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "db-set-rollback-only!",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L730",
   :line 730,
   :var-type "function",
   :arglists ([db]),
   :doc
   "Marks the outermost transaction such that it will rollback rather than\ncommit when complete",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/db-set-rollback-only!"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "db-transaction*",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L769",
   :line 769,
   :var-type "function",
   :arglists ([db func] [db func opts]),
   :doc
   "Evaluates func as a transaction on the open database connection. Any\nnested transactions are absorbed into the outermost transaction. By\ndefault, all database updates are committed together as a group after\nevaluating the outermost body, or rolled back on any uncaught\nexception. If rollback is set within scope of the outermost transaction,\nthe entire transaction will be rolled back rather than committed when\ncomplete.\nThe isolation option may be :none, :read-committed, :read-uncommitted,\n:repeatable-read, or :serializable. Note that not all databases support\nall of those isolation levels, and may either throw an exception or\nsubstitute another isolation level.\nThe read-only? option puts the transaction in readonly mode (if supported).",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/db-transaction*"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "db-unset-rollback-only!",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L736",
   :line 736,
   :var-type "function",
   :arglists ([db]),
   :doc
   "Marks the outermost transaction such that it will not rollback when complete",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/db-unset-rollback-only!"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "delete!",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L1461",
   :line 1461,
   :var-type "function",
   :arglists ([db table where-clause] [db table where-clause opts]),
   :doc
   "Given a database connection, a table name and a where clause of columns to match,\nperform a delete. The options may specify how to transform column names in the\nmap (default 'as-is') and whether to run the delete in a transaction (default true).\nExample:\n  (delete! db :person [\"zip = ?\" 94546])\nis equivalent to:\n  (execute! db [\"DELETE FROM person WHERE zip = ?\" 94546])",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/delete!"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "drop-table-ddl",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L1716",
   :line 1716,
   :var-type "function",
   :arglists
   ([table]
    [table {:keys [entities conditional?], :or {entities identity}}]),
   :doc
   "Given a table name, return the DDL string for dropping that table.\nAn options map may be provided that can contain:\n:entities -- a function to specify how column names are transformed.\n:conditional? -- either a boolean, indicating whether to add 'IF EXISTS',\n  or a string, which is inserted literally before the table name, or a\n  function of two arguments (table name and the create statement), that can\n  manipulate the generated statement to better support other databases, e.g.,\n  MS SQL Server which need to wrap create table in an existence query.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/drop-table-ddl"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "execute!",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L1417",
   :line 1417,
   :var-type "function",
   :arglists ([db sql-params] [db sql-params opts]),
   :doc
   "Given a database connection and a vector containing SQL (or PreparedStatement)\nfollowed by optional parameters, perform a general (non-select) SQL operation.\n\nThe :transaction? option specifies whether to run the operation in a\ntransaction or not (default true).\n\nIf the :multi? option is false (the default), the SQL statement should be\nfollowed by the parameters for that statement.\n\nIf the :multi? option is true, the SQL statement should be followed by one or\nmore vectors of parameters, one for each application of the SQL statement.\n\nIf :return-keys is provided, db-do-prepared-return-keys will be called\ninstead of db-do-prepared, and the result will be a sequence of maps\ncontaining the generated keys. If present, :row-fn will be applied. If :multi?\nthen :result-set-fn will also be applied if present. :as-arrays? may also be\nspecified (which will affect what :result-set-fn is passed).\n\nIf there are no parameters specified, executeUpdate will be used, otherwise\nexecuteBatch will be used. This may affect what SQL you can run via execute!",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/execute!"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "find-by-keys",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L1368",
   :line 1368,
   :var-type "function",
   :arglists ([db table columns] [db table columns opts]),
   :doc
   "Given a database connection, a table name, a map of column name/value\npairs, and an optional options map, return any matching rows.\n\nAn :order-by option may be supplied to sort the rows, e.g.,\n\n    {:order-by [{:name :asc} {:age :desc} {:income :asc}]}\n    ;; equivalent to:\n    {:order-by [:name {:age :desc} :income]}\n\nThe :order-by value is a sequence of column names (to sort in ascending\norder) and/or maps from column names to directions (:asc or :desc). The\ndirections may be strings or keywords and are not case-sensitive. They\nare mapped to ASC or DESC in the generated SQL.\n\nNote: if a ordering map has more than one key, the order of the columns\nin the generated SQL ORDER BY clause is unspecified (so such maps should\nonly contain one key/value pair).",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/find-by-keys"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "get-by-id",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L1401",
   :line 1401,
   :var-type "function",
   :arglists
   ([db table pk-value]
    [db table pk-value pk-name-or-opts]
    [db table pk-value pk-name opts]),
   :doc
   "Given a database connection, a table name, a primary key value, an\noptional primary key column name, and an optional options map, return\na single matching row, or nil.\nThe primary key column name defaults to :id.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/get-by-id"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "get-connection",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L274",
   :line 274,
   :var-type "function",
   :arglists
   ([db-spec]
    [{:keys
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
      :as db-spec}
     opts]),
   :doc
   "Creates a connection to a database. db-spec is usually a map containing connection\nparameters but can also be a URI or a String.\n\nThe only time you should call this function is when you need a Connection for\nprepare-statement -- no other public functions in clojure.java.jdbc accept a\nraw Connection object: they all expect a db-spec (either a raw db-spec or one\nobtained via with-db-connection or with-db-transaction).\n\nThe correct usage of get-connection for prepare-statement is:\n\n    (with-open [conn (jdbc/get-connection db-spec)]\n      ... (jdbc/prepare-statement conn sql-statement options) ...)\n\nAny connection obtained via calling get-connection directly must be closed\nexplicitly (via with-open or a direct call to .close on the Connection object).\n\nThe various possibilities are described below:\n\nDriverManager (preferred):\n  :dbtype      (required) a String, the type of the database (the jdbc subprotocol)\n  :dbname      (required) a String, the name of the database\n  :classname   (optional) a String, the jdbc driver class name\n  :host        (optional) a String, the host name/IP of the database\n                          (defaults to 127.0.0.1)\n  :port        (optional) a Long, the port of the database\n                          (defaults to 3306 for mysql, 1433 for mssql/jtds, else nil)\n  (others)     (optional) passed to the driver as properties\n                          (may include :user and :password)\n\nRaw:\n  :connection-uri (required) a String\n               Passed directly to DriverManager/getConnection\n               (both :user and :password may be specified as well, rather\n                than passing them as part of the connection string)\n\nOther formats accepted:\n\nExisting Connection:\n  :connection  (required) an existing open connection that can be used\n               but cannot be closed (only the parent connection can be closed)\n\nDriverManager (alternative / legacy style):\n  :subprotocol (required) a String, the jdbc subprotocol\n  :subname     (required) a String, the jdbc subname\n  :classname   (optional) a String, the jdbc driver class name\n  (others)     (optional) passed to the driver as properties\n                          (may include :user and :password)\n\nFactory:\n  :factory     (required) a function of one argument, a map of params\n  (others)     (optional) passed to the factory function in a map\n\nDataSource:\n  :datasource  (required) a javax.sql.DataSource\n  :username    (optional) a String - deprecated, use :user instead\n  :user        (optional) a String - preferred\n  :password    (optional) a String, required if :user is supplied\n\nJNDI:\n  :name        (required) a String or javax.naming.Name\n  :environment (optional) a java.util.Map\n\njava.net.URI:\n  Parsed JDBC connection string (see java.lang.String format next)\n\njava.lang.String:\n  subprotocol://user:password@host:post/subname\n               An optional prefix of jdbc: is allowed.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/get-connection"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "get-isolation-level",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L760",
   :line 760,
   :var-type "function",
   :arglists ([db]),
   :doc
   "Given a db-spec (with an optional connection), return the current\ntransaction isolation level, if known. Return nil if there is no\nactive connection in the db-spec. Return :unknown if we do not\nrecognize the isolation level.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/get-isolation-level"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "insert!",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L1581",
   :line 1581,
   :var-type "function",
   :arglists
   ([db table row]
    [db table cols-or-row values-or-opts]
    [db table cols values opts]),
   :doc
   "Given a database connection, a table name and either a map representing a rows,\nor a list of column names followed by a list of column values also representing\na single row, perform an insert.\nWhen inserting a row as a map, the result is the database-specific form of the\ngenerated keys, if available (note: PostgreSQL returns the whole row).\nWhen inserting a row as a list of column values, the result is the count of\nrows affected (1), if available (from getUpdateCount after executeBatch).\nThe row map or column value vector may be followed by a map of options:\nThe :transaction? option specifies whether to run in a transaction or not.\nThe default is true (use a transaction). The :entities option specifies how\nto convert the table name and column names to SQL entities.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/insert!"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "insert-multi!",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L1601",
   :line 1601,
   :var-type "function",
   :arglists
   ([db table rows]
    [db table cols-or-rows values-or-opts]
    [db table cols values opts]),
   :doc
   "Given a database connection, a table name and either a sequence of maps (for\nrows) or a sequence of column names, followed by a sequence of vectors (for\nthe values in each row), and possibly a map of options, insert that data into\nthe database.\n\nWhen inserting rows as a sequence of maps, the result is a sequence of the\ngenerated keys, if available (note: PostgreSQL returns the whole rows). A\nseparate database operation is used for each row inserted. This may be slow\nfor if a large sequence of maps is provided.\n\nWhen inserting rows as a sequence of lists of column values, the result is\na sequence of the counts of rows affected (a sequence of 1's), if available.\nYes, that is singularly unhelpful. Thank you getUpdateCount and executeBatch!\nA single database operation should be used to insert all the rows at once.\nThis may be much faster than inserting a sequence of rows (which performs an\ninsert for each map in the sequence).\n\nNote: some database drivers need to be told to rewrite the SQL for this to\nbe performed as a single, batched operation. In particular, PostgreSQL\nrequires :reWriteBatchedInserts true and My SQL requires\n:rewriteBatchedStatement true (both non-standard JDBC options, of course!).\nThese options should be passed into the driver when the connection is\ncreated (however that is done in your program).\n\nThe :transaction? option specifies whether to run in a transaction or not.\nThe default is true (use a transaction). The :entities option specifies how\nto convert the table name and column names to SQL entities.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/insert-multi!"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "metadata-query",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L912",
   :line 912,
   :var-type "macro",
   :arglists ([meta-query & opt-args]),
   :doc
   "Given a Java expression that extracts metadata (in the context of with-db-metadata),\nand a map of options like metadata-result, manage the connection for a single\nmetadata-based query. Example usage:\n\n(with-db-metadata [meta db-spec]\n  (metadata-query (.getTables meta nil nil nil (into-array String [\"TABLE\"]))\n    {:row-fn ... :result-set-fn ...}))",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/metadata-query"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "metadata-result",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L900",
   :line 900,
   :var-type "function",
   :arglists ([rs-or-value] [rs-or-value opts]),
   :doc
   "If the argument is a java.sql.ResultSet, turn it into a result-set-seq,\nelse return it as-is. This makes working with metadata easier.\nAlso accepts an option map containing :identifiers, :keywordize?, :qualifier,\n:as-arrays?, :row-fn, and :result-set-fn to control how the ResultSet is\ntransformed and returned. See query for more details.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/metadata-result"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "prepare-statement",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L619",
   :line 619,
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
       timeout]}]),
   :doc
   "Create a prepared statement from a connection, a SQL string and a map\nof options:\n   :return-keys truthy | nil - default nil\n     for some drivers, this may be a vector of column names to identify\n     the generated keys to return, otherwise it should just be true\n   :result-type :forward-only | :scroll-insensitive | :scroll-sensitive\n   :concurrency :read-only | :updatable\n   :cursors     :hold | :close\n   :fetch-size  n\n   :max-rows    n\n   :timeout     n\nNote that :result-type and :concurrency must be specified together as the\nunderlying Java API expects both (or neither).",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/prepare-statement"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "print-sql-exception",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L678",
   :line 678,
   :var-type "function",
   :arglists ([exception]),
   :doc "Prints the contents of an SQLException to *out*",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/print-sql-exception"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "print-sql-exception-chain",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L692",
   :line 692,
   :var-type "function",
   :arglists ([exception]),
   :doc "Prints a chain of SQLExceptions to *out*",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/print-sql-exception-chain"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "print-update-counts",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L704",
   :line 704,
   :var-type "function",
   :arglists ([exception]),
   :doc
   "Prints the update counts from a BatchUpdateException to *out*",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/print-update-counts"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "query",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L1126",
   :line 1126,
   :var-type "function",
   :arglists ([db sql-params] [db sql-params opts]),
   :doc
   "Given a database connection and a vector containing SQL and optional parameters,\nperform a simple database query. The options specify how to construct the result\nset (and are also passed to prepare-statement as needed):\n  :as-arrays? - return the results as a set of arrays, default false.\n  :identifiers - applied to each column name in the result set, default lower-case\n  :keywordize? - defaults to true, can be false to opt-out of converting\n      identifiers to keywords\n  :qualifier - optionally provides the namespace qualifier for identifiers\n  :result-set-fn - applied to the entire result set, default doall / vec\n      if :as-arrays? true, :result-set-fn will default to vec\n      if :as-arrays? false, :result-set-fn will default to doall\n  :row-fn - applied to each row as the result set is constructed, default identity\nThe second argument is a vector containing a SQL string or PreparedStatement, followed\nby any parameters it needs.\nSee also prepare-statement for additional options.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/query"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "quoted",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L67",
   :line 67,
   :var-type "function",
   :arglists ([q]),
   :doc
   "Given a (vector) pair of delimiters (characters or strings), return a naming\nstrategy function that will quote SQL entities with them.\nGiven a single delimiter, treat it as a (vector) pair of that delimiter.\n  ((quoted [\\[ \\]]) \"foo\") will return \"[foo]\" -- for MS SQL Server\n  ((quoted \\`') \"foo\") will return \"`foo`\" -- for MySQL\nIntended to be used with :entities to provide a quoting (naming) strategy that\nis appropriate for your database.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/quoted"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "reducible-query",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L1295",
   :line 1295,
   :var-type "function",
   :arglists ([db sql-params] [db sql-params opts]),
   :doc
   "Given a database connection, a vector containing SQL and optional parameters,\nreturn a reducible collection. When reduced, it will start the database query\nand reduce the result set, and then close the connection:\n  (transduce (map :cost) + (reducible-query db sql-params))\n\nThe following options from query etc are not accepted here:\n  :as-arrays? :explain :explain-fn :result-set-fn :row-fn\nSee prepare-statement for additional options that may be passed through.\n\nIf :raw? true is specified, the rows of the result set are not converted to\nhash maps, and it as if the following options were specified:\n  :identifiers identity :keywordize? false :qualifier nil\nIn addition, the rows of the result set may only be read as if they were hash\nmaps (get, keyword lookup, select-keys) but the sequence representation is\nnot available (so, no keys, no vals, and no seq calls). This is much faster\nthan converting each row to a hash map but it is also more restrictive.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/reducible-query"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "reducible-result-set",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L1211",
   :line 1211,
   :var-type "function",
   :arglists
   ([rs
     {:keys [identifiers keywordize? qualifier read-columns],
      :or
      {identifiers lower-case,
       keywordize? true,
       read-columns dft-read-columns}}]),
   :doc
   "Given a java.sql.ResultSet return a reducible collection.\nCompiled with Clojure 1.7 or later -- uses clojure.lang.IReduce\nNote: :as-arrays? is not accepted here.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/reducible-result-set"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "result-set-seq",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L538",
   :line 538,
   :var-type "function",
   :arglists
   ([rs]
    [rs
     {:keys
      [as-arrays? identifiers keywordize? qualifier read-columns],
      :or
      {identifiers lower-case,
       keywordize? true,
       read-columns dft-read-columns}}]),
   :doc
   "Creates and returns a lazy sequence of maps corresponding to the rows in the\njava.sql.ResultSet rs. Loosely based on clojure.core/resultset-seq but it\nrespects the specified naming strategy. Duplicate column names are made unique\nby appending _N before applying the naming strategy (where N is a unique integer),\nunless the :as-arrays? option is :cols-as-is, in which case the column names\nare untouched (the result set maintains column name/value order).\nThe :identifiers option specifies how SQL column names are converted to Clojure\nkeywords. The default is to convert them to lower case.\nThe :keywordize? option can be specified as false to opt-out of the conversion\nto keywords.\nThe :qualifier option specifies the namespace qualifier for those identifiers\n(and this may not be specified when :keywordize? is false).",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/result-set-seq"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "update!",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L1655",
   :line 1655,
   :var-type "function",
   :arglists
   ([db table set-map where-clause]
    [db table set-map where-clause opts]),
   :doc
   "Given a database connection, a table name, a map of column values to set and a\nwhere clause of columns to match, perform an update. The options may specify\nhow column names (in the set / match maps) should be transformed (default\n'as-is') and whether to run the update in a transaction (default true).\nExample:\n  (update! db :person {:zip 94540} [\"zip = ?\" 94546])\nis equivalent to:\n  (execute! db [\"UPDATE person SET zip = ? WHERE zip = ?\" 94540 94546])",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/update!"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "with-db-connection",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L858",
   :line 858,
   :var-type "macro",
   :arglists ([binding & body]),
   :doc
   "Evaluates body in the context of an active connection to the database.\n(with-db-connection [con-db db-spec opts]\n  ... con-db ...)",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/with-db-connection"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "with-db-metadata",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L871",
   :line 871,
   :var-type "macro",
   :arglists ([binding & body]),
   :doc
   "Evaluates body in the context of an active connection with metadata bound\nto the specified name. See also metadata-result for dealing with the results\nof operations that retrieve information from the metadata.\n(with-db-metadata [md db-spec opts]\n  ... md ...)",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/with-db-metadata"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "with-db-transaction",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L844",
   :line 844,
   :var-type "macro",
   :arglists ([binding & body]),
   :doc
   "Evaluates body in the context of a transaction on the specified database connection.\nThe binding provides the database connection for the transaction and the name to which\nthat is bound for evaluation of the body. The binding may also specify the isolation\nlevel for the transaction, via the :isolation option and/or set the transaction to\nreadonly via the :read-only? option.\n(with-db-transaction [t-con db-spec {:isolation level :read-only? true}]\n  ... t-con ...)\nSee db-transaction* for more details.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/with-db-transaction"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "IResultSetReadColumn",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L498",
   :line 498,
   :var-type "protocol",
   :arglists nil,
   :doc
   "Protocol for reading objects from the java.sql.ResultSet. Default\nimplementations (for Object and nil) return the argument, and the\nBoolean implementation ensures a canonicalized true/false value,\nbut it can be extended to provide custom behavior for special types.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/IResultSetReadColumn"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "ISQLParameter",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L471",
   :line 471,
   :var-type "protocol",
   :arglists nil,
   :doc
   "Protocol for setting SQL parameters in statement objects, which\ncan convert from Clojure values. The default implementation just\ndelegates the conversion to ISQLValue's sql-value conversion and\nuses .setObject on the parameter. It can be extended to use other\nmethods of PreparedStatement to convert and set parameter values.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/ISQLParameter"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj",
   :name "ISQLValue",
   :file "src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/4138b0322715aff7ca5b8009e847d74cd3913da8/src/main/clojure/clojure/java/jdbc.clj#L457",
   :line 457,
   :var-type "protocol",
   :arglists nil,
   :doc
   "Protocol for creating SQL values from Clojure values. Default\nimplementations (for Object and nil) just return the argument,\nbut it can be extended to provide custom behavior to support\nexotic types supported by different databases.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/ISQLValue"}
  {:raw-source-url nil,
   :name "result-set-read-column",
   :file nil,
   :source-url nil,
   :var-type "function",
   :arglists ([val rsmeta idx]),
   :doc
   "Function for transforming values after reading them from the database",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/result-set-read-column"}
  {:raw-source-url nil,
   :name "set-parameter",
   :file nil,
   :source-url nil,
   :var-type "function",
   :arglists ([val stmt ix]),
   :doc
   "Convert a Clojure value into a SQL value and store it as the ix'th\nparameter in the given SQL statement object.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/set-parameter"}
  {:raw-source-url nil,
   :name "sql-value",
   :file nil,
   :source-url nil,
   :var-type "function",
   :arglists ([val]),
   :doc "Convert a Clojure value into a SQL value.",
   :namespace "clojure.java.jdbc",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc/sql-value"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/79ad004b42e9234d2bbf54df6a254c22b1c68c72/src/main/clojure/clojure/java/jdbc/datafy.clj",
   :name "find-by-keys",
   :file "src/main/clojure/clojure/java/jdbc/datafy.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/79ad004b42e9234d2bbf54df6a254c22b1c68c72/src/main/clojure/clojure/java/jdbc/datafy.clj#L119",
   :line 119,
   :var-type "function",
   :arglists ([db table columns] [db table columns opts]),
   :doc
   "Given a database connection, a table name, a map of column name/value\npairs, and an optional options map, return any matching rows.\n\nAn :order-by option may be supplied to sort the rows, e.g.,\n\n    {:order-by [{:name :asc} {:age :desc} {:income :asc}]}\n    ;; equivalent to:\n    {:order-by [:name {:age :desc} :income]}\n\nThe :order-by value is a sequence of column names (to sort in ascending\norder) and/or maps from column names to directions (:asc or :desc). The\ndirections may be strings or keywords and are not case-sensitive. They\nare mapped to ASC or DESC in the generated SQL.\n\nNote: if a ordering map has more than one key, the order of the columns\nin the generated SQL ORDER BY clause is unspecified (so such maps should\nonly contain one key/value pair).",
   :namespace "clojure.java.jdbc.datafy",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc.datafy/find-by-keys"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/79ad004b42e9234d2bbf54df6a254c22b1c68c72/src/main/clojure/clojure/java/jdbc/datafy.clj",
   :name "get-by-id",
   :file "src/main/clojure/clojure/java/jdbc/datafy.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/79ad004b42e9234d2bbf54df6a254c22b1c68c72/src/main/clojure/clojure/java/jdbc/datafy.clj#L105",
   :line 105,
   :var-type "function",
   :arglists
   ([db table pk-value]
    [db table pk-value pk-name-or-opts]
    [db table pk-value pk-name opts]),
   :doc
   "Given a database connection, a table name, a primary key value, an\noptional primary key column name, and an optional options map, return\na single matching row, or nil.\nThe primary key column name defaults to :id.",
   :namespace "clojure.java.jdbc.datafy",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc.datafy/get-by-id"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/79ad004b42e9234d2bbf54df6a254c22b1c68c72/src/main/clojure/clojure/java/jdbc/datafy.clj",
   :name "query",
   :file "src/main/clojure/clojure/java/jdbc/datafy.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/79ad004b42e9234d2bbf54df6a254c22b1c68c72/src/main/clojure/clojure/java/jdbc/datafy.clj#L142",
   :line 142,
   :var-type "function",
   :arglists ([db sql-params] [db sql-params opts]),
   :doc
   "Given a database connection and a vector containing SQL and optional parameters,\nperform a simple database query. The options specify how to construct the result\nset (and are also passed to prepare-statement as needed):\n  :as-arrays? - return the results as a set of arrays, default false.\n  :identifiers - applied to each column name in the result set, default lower-case\n  :keywordize? - defaults to true, can be false to opt-out of converting\n      identifiers to keywords\n  :qualifier - optionally provides the namespace qualifier for identifiers\n  :result-set-fn - applied to the entire result set, default doall / vec\n      if :as-arrays? true, :result-set-fn will default to vec\n      if :as-arrays? false, :result-set-fn will default to doall\n  :row-fn - applied to each row as the result set is constructed, default identity\nThe second argument is a vector containing a SQL string or PreparedStatement, followed\nby any parameters it needs.\nSee also prepare-statement for additional options.",
   :namespace "clojure.java.jdbc.datafy",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#clojure.java.jdbc.datafy/query"}
  {:keyword :clojure.java.jdbc.spec/as-arrays?,
   :spec (or :as-is #{:cols-as-is} :truthy (nilable boolean?)),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/as-arrays?"}
  {:keyword :clojure.java.jdbc.spec/auto-commit?,
   :spec boolean?,
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/auto-commit?"}
  {:keyword :clojure.java.jdbc.spec/classname,
   :spec string?,
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/classname"}
  {:keyword :clojure.java.jdbc.spec/column-direction,
   :spec
   (or
    :id
    :clojure.java.jdbc.spec/identifier
    :id-dir
    (map-of
     :clojure.java.jdbc.spec/identifier
     :clojure.java.jdbc.spec/direction)),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/column-direction"}
  {:keyword :clojure.java.jdbc.spec/column-spec,
   :spec
   (cat
    :col
    :clojure.java.jdbc.spec/identifier
    :spec
    (* (or :kw keyword? :str string? :num number?))),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/column-spec"}
  {:keyword :clojure.java.jdbc.spec/concurrency,
   :spec (set (keys (deref #'result-set-concurrency))),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/concurrency"}
  {:keyword :clojure.java.jdbc.spec/conditional?,
   :spec (or :b boolean? :s string? :f fn?),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/conditional?"}
  {:keyword :clojure.java.jdbc.spec/connection,
   :spec (instance? java.sql.Connection %),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/connection"}
  {:keyword :clojure.java.jdbc.spec/connection-binding,
   :spec (cat :con-db simple-symbol? :db-spec any? :opts (? any?)),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/connection-binding"}
  {:keyword :clojure.java.jdbc.spec/connection-options,
   :spec
   (keys
    :req-un
    []
    :opt-un
    [:clojure.java.jdbc.spec/auto-commit?
     :clojure.java.jdbc.spec/read-only?]),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/connection-options"}
  {:keyword :clojure.java.jdbc.spec/connection-uri,
   :spec string?,
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/connection-uri"}
  {:keyword :clojure.java.jdbc.spec/cursors,
   :spec (set (keys (deref #'result-set-holdability))),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/cursors"}
  {:keyword :clojure.java.jdbc.spec/datasource,
   :spec (instance? javax.sql.DataSource %),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/datasource"}
  {:keyword :clojure.java.jdbc.spec/db-spec,
   :spec
   (or
    :connection
    :clojure.java.jdbc.spec/db-spec-connection
    :friendly
    :clojure.java.jdbc.spec/db-spec-friendly
    :raw
    :clojure.java.jdbc.spec/db-spec-raw
    :driver-mgr
    :clojure.java.jdbc.spec/db-spec-driver-manager
    :factory
    :clojure.java.jdbc.spec/db-spec-factory
    :datasource
    :clojure.java.jdbc.spec/db-spec-data-source
    :jndi
    :clojure.java.jdbc.spec/db-spec-jndi
    :uri-str
    :clojure.java.jdbc.spec/db-spec-string
    :uri-obj
    :clojure.java.jdbc.spec/db-spec-uri),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/db-spec"}
  {:keyword :clojure.java.jdbc.spec/db-spec-connection,
   :spec (keys :req-un [:clojure.java.jdbc.spec/connection]),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/db-spec-connection"}
  {:keyword :clojure.java.jdbc.spec/db-spec-data-source,
   :spec
   (keys
    :req-un
    [:clojure.java.jdbc.spec/datasource]
    :opt-un
    [:clojure.java.jdbc.spec/username
     :clojure.java.jdbc.spec/user
     :clojure.java.jdbc.spec/password]),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/db-spec-data-source"}
  {:keyword :clojure.java.jdbc.spec/db-spec-driver-manager,
   :spec
   (keys
    :req-un
    [:clojure.java.jdbc.spec/subprotocol
     :clojure.java.jdbc.spec/subname]
    :opt-un
    [:clojure.java.jdbc.spec/classname
     :clojure.java.jdbc.spec/user
     :clojure.java.jdbc.spec/password]),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/db-spec-driver-manager"}
  {:keyword :clojure.java.jdbc.spec/db-spec-factory,
   :spec (keys :req-un [:clojure.java.jdbc.spec/factory]),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/db-spec-factory"}
  {:keyword :clojure.java.jdbc.spec/db-spec-friendly,
   :spec
   (keys
    :req-un
    [:clojure.java.jdbc.spec/dbtype :clojure.java.jdbc.spec/dbname]
    :opt-un
    [:clojure.java.jdbc.spec/host
     :clojure.java.jdbc.spec/port
     :clojure.java.jdbc.spec/user
     :clojure.java.jdbc.spec/password
     :clojure.java.jdbc.spec/classname]),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/db-spec-friendly"}
  {:keyword :clojure.java.jdbc.spec/db-spec-jndi,
   :spec
   (keys
    :req-un
    [:clojure.java.jdbc.spec/name]
    :opt-un
    [:clojure.java.jdbc.spec/environment]),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/db-spec-jndi"}
  {:keyword :clojure.java.jdbc.spec/db-spec-raw,
   :spec
   (keys
    :req-un
    [:clojure.java.jdbc.spec/connection-uri]
    :opt-un
    [:clojure.java.jdbc.spec/user :clojure.java.jdbc.spec/password]),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/db-spec-raw"}
  {:keyword :clojure.java.jdbc.spec/db-spec-string,
   :spec string?,
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/db-spec-string"}
  {:keyword :clojure.java.jdbc.spec/db-spec-uri,
   :spec (instance? java.net.URI %),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/db-spec-uri"}
  {:keyword :clojure.java.jdbc.spec/dbname,
   :spec string?,
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/dbname"}
  {:keyword :clojure.java.jdbc.spec/dbtype,
   :spec
   (or
    :alias
    :clojure.java.jdbc.spec/subprotocol-alias
    :name
    :clojure.java.jdbc.spec/subprotocol-base),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/dbtype"}
  {:keyword :clojure.java.jdbc.spec/delimiter,
   :spec (or :s string? :c char?),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/delimiter"}
  {:keyword :clojure.java.jdbc.spec/direction,
   :spec #{:desc "DESC" :asc "ASC" "desc" "asc"},
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/direction"}
  {:keyword :clojure.java.jdbc.spec/entities,
   :spec
   (fspec
    :args
    (cat :s string?)
    :ret
    :clojure.java.jdbc.spec/entity
    :fn
    nil),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/entities"}
  {:keyword :clojure.java.jdbc.spec/entity,
   :spec string?,
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/entity"}
  {:keyword :clojure.java.jdbc.spec/environment,
   :spec (nilable map?),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/environment"}
  {:keyword :clojure.java.jdbc.spec/exec-sql-options,
   :spec
   (keys
    :req-un
    []
    :opt-un
    [:clojure.java.jdbc.spec/entities
     :clojure.java.jdbc.spec/transaction?]),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/exec-sql-options"}
  {:keyword :clojure.java.jdbc.spec/execute-options,
   :spec
   (keys
    :req-un
    []
    :opt-un
    [:clojure.java.jdbc.spec/transaction?
     :clojure.java.jdbc.spec/multi?
     :clojure.java.jdbc.spec/return-keys]),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/execute-options"}
  {:keyword :clojure.java.jdbc.spec/execute-result,
   :spec (* integer?),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/execute-result"}
  {:keyword :clojure.java.jdbc.spec/explain-fn,
   :spec fn?,
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/explain-fn"}
  {:keyword :clojure.java.jdbc.spec/explain?,
   :spec (or :b boolean? :s string?),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/explain?"}
  {:keyword :clojure.java.jdbc.spec/factory,
   :spec
   (fspec
    :args
    (cat :db-spec :clojure.java.jdbc.spec/db-spec)
    :ret
    :clojure.java.jdbc.spec/connection
    :fn
    nil),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/factory"}
  {:keyword :clojure.java.jdbc.spec/fetch-size,
   :spec nat-int?,
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/fetch-size"}
  {:keyword :clojure.java.jdbc.spec/find-by-keys-options,
   :spec
   (keys
    :req-un
    []
    :opt-un
    [:clojure.java.jdbc.spec/entities
     :clojure.java.jdbc.spec/order-by
     :clojure.java.jdbc.spec/result-set-fn
     :clojure.java.jdbc.spec/row-fn
     :clojure.java.jdbc.spec/identifiers
     :clojure.java.jdbc.spec/qualifier
     :clojure.java.jdbc.spec/keywordize?
     :clojure.java.jdbc.spec/as-arrays?
     :clojure.java.jdbc.spec/read-columns]),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/find-by-keys-options"}
  {:keyword :clojure.java.jdbc.spec/host,
   :spec string?,
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/host"}
  {:keyword :clojure.java.jdbc.spec/identifier,
   :spec (or :kw keyword? :s string?),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/identifier"}
  {:keyword :clojure.java.jdbc.spec/identifiers,
   :spec
   (fspec
    :args
    (cat :s :clojure.java.jdbc.spec/entity)
    :ret
    :clojure.java.jdbc.spec/identifier
    :fn
    nil),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/identifiers"}
  {:keyword :clojure.java.jdbc.spec/isolation,
   :spec (set (keys (deref #'isolation-levels))),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/isolation"}
  {:keyword :clojure.java.jdbc.spec/keywordize?,
   :spec boolean?,
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/keywordize?"}
  {:keyword :clojure.java.jdbc.spec/max-size,
   :spec nat-int?,
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/max-size"}
  {:keyword :clojure.java.jdbc.spec/multi?,
   :spec boolean?,
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/multi?"}
  {:keyword :clojure.java.jdbc.spec/name,
   :spec string?,
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/name"}
  {:keyword :clojure.java.jdbc.spec/naming-strategy,
   :spec
   (fspec
    :args
    (cat :x :clojure.java.jdbc.spec/identifier)
    :ret
    :clojure.java.jdbc.spec/identifier
    :fn
    nil),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/naming-strategy"}
  {:keyword :clojure.java.jdbc.spec/order-by,
   :spec (coll-of :clojure.java.jdbc.spec/column-direction),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/order-by"}
  {:keyword :clojure.java.jdbc.spec/password,
   :spec string?,
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/password"}
  {:keyword :clojure.java.jdbc.spec/port,
   :spec (or :port pos-int? :s string?),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/port"}
  {:keyword :clojure.java.jdbc.spec/prepare-options,
   :spec
   (merge
    (keys
     :req-un
     []
     :opt-un
     [:clojure.java.jdbc.spec/return-keys
      :clojure.java.jdbc.spec/result-type
      :clojure.java.jdbc.spec/concurrency
      :clojure.java.jdbc.spec/cursors
      :clojure.java.jdbc.spec/fetch-size
      :clojure.java.jdbc.spec/max-rows
      :clojure.java.jdbc.spec/timeout])
    :clojure.java.jdbc.spec/connection-options),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/prepare-options"}
  {:keyword :clojure.java.jdbc.spec/prepared-statement,
   :spec (instance? java.sql.PreparedStatement %),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/prepared-statement"}
  {:keyword :clojure.java.jdbc.spec/qualifier,
   :spec (nilable string?),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/qualifier"}
  {:keyword :clojure.java.jdbc.spec/query-options,
   :spec
   (merge
    (keys
     :req-un
     []
     :opt-un
     [:clojure.java.jdbc.spec/result-set-fn
      :clojure.java.jdbc.spec/row-fn
      :clojure.java.jdbc.spec/identifiers
      :clojure.java.jdbc.spec/qualifier
      :clojure.java.jdbc.spec/keywordize?
      :clojure.java.jdbc.spec/as-arrays?
      :clojure.java.jdbc.spec/read-columns])
    :clojure.java.jdbc.spec/prepare-options),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/query-options"}
  {:keyword :clojure.java.jdbc.spec/read-columns,
   :spec fn?,
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/read-columns"}
  {:keyword :clojure.java.jdbc.spec/read-only?,
   :spec boolean?,
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/read-only?"}
  {:keyword :clojure.java.jdbc.spec/reducible-query-options,
   :spec
   (merge
    (keys
     :req-un
     []
     :opt-un
     [:clojure.java.jdbc.spec/identifiers
      :clojure.java.jdbc.spec/keywordize?
      :clojure.java.jdbc.spec/qualifier
      :clojure.java.jdbc.spec/read-columns])
    :clojure.java.jdbc.spec/prepare-options),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/reducible-query-options"}
  {:keyword :clojure.java.jdbc.spec/result-set,
   :spec (instance? java.sql.ResultSet %),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/result-set"}
  {:keyword :clojure.java.jdbc.spec/result-set-fn,
   :spec (fspec :args (cat :rs (coll-of any?)) :ret any? :fn nil),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/result-set-fn"}
  {:keyword :clojure.java.jdbc.spec/result-set-metadata,
   :spec (instance? java.sql.ResultSetMetaData %),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/result-set-metadata"}
  {:keyword :clojure.java.jdbc.spec/result-type,
   :spec (set (keys (deref #'result-set-type))),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/result-type"}
  {:keyword :clojure.java.jdbc.spec/return-keys,
   :spec
   (or
    :columns
    (coll-of :clojure.java.jdbc.spec/entity :kind vector?)
    :boolean
    boolean?),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/return-keys"}
  {:keyword :clojure.java.jdbc.spec/row-fn,
   :spec
   (fspec
    :args
    (cat :row (map-of keyword? :clojure.java.jdbc.spec/sql-value))
    :ret
    any?
    :fn
    nil),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/row-fn"}
  {:keyword :clojure.java.jdbc.spec/sql-params,
   :spec
   (or
    :sql
    :clojure.java.jdbc.spec/sql-stmt
    :sql-params
    (cat
     :sql
     :clojure.java.jdbc.spec/sql-stmt
     :params
     (* :clojure.java.jdbc.spec/sql-value))),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/sql-params"}
  {:keyword :clojure.java.jdbc.spec/sql-stmt,
   :spec
   (or :sql string? :stmt :clojure.java.jdbc.spec/prepared-statement),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/sql-stmt"}
  {:keyword :clojure.java.jdbc.spec/sql-value,
   :spec any?,
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/sql-value"}
  {:keyword :clojure.java.jdbc.spec/subname,
   :spec string?,
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/subname"}
  {:keyword :clojure.java.jdbc.spec/subprotocol,
   :spec string?,
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/subprotocol"}
  {:keyword :clojure.java.jdbc.spec/subprotocol-alias,
   :spec #{"mssql" "oracle" "postgres" "jtds" "hsql"},
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/subprotocol-alias"}
  {:keyword :clojure.java.jdbc.spec/subprotocol-base,
   :spec
   #{"h2:mem" "mysql" "oracle:thin" "pgsql" "oracle:oci" "postgresql"
     "sqlite" "h2" "jtds:sqlserver" "hsqldb" "redshift" "derby"
     "sqlserver"},
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/subprotocol-base"}
  {:keyword :clojure.java.jdbc.spec/table-spec,
   :spec string?,
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/table-spec"}
  {:keyword :clojure.java.jdbc.spec/timeout,
   :spec nat-int?,
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/timeout"}
  {:keyword :clojure.java.jdbc.spec/transaction-binding,
   :spec (cat :t-con simple-symbol? :db-spec any? :opts (? any?)),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/transaction-binding"}
  {:keyword :clojure.java.jdbc.spec/transaction-options,
   :spec
   (keys
    :req-un
    []
    :opt-un
    [:clojure.java.jdbc.spec/isolation
     :clojure.java.jdbc.spec/read-only?]),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/transaction-options"}
  {:keyword :clojure.java.jdbc.spec/transaction?,
   :spec boolean?,
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/transaction?"}
  {:keyword :clojure.java.jdbc.spec/uri,
   :spec (instance? java.net.URI %),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/uri"}
  {:keyword :clojure.java.jdbc.spec/user,
   :spec string?,
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/user"}
  {:keyword :clojure.java.jdbc.spec/username,
   :spec string?,
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/username"}
  {:keyword :clojure.java.jdbc.spec/where-clause,
   :spec
   (cat :where string? :params (* :clojure.java.jdbc.spec/sql-value)),
   :var-type "spec",
   :namespace "clojure.java.jdbc.spec",
   :wiki-url
   "https://clojure.github.io/java.jdbc//index.html#:clojure.java.jdbc.spec/where-clause"})}
