{:namespaces
 ({:source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc/clojure.java.jdbc-api.html",
   :name "clojure.java.jdbc",
   :author "Stephen C. Gilardi, Sean Corfield",
   :doc
   "A Clojure interface to SQL databases via JDBC\n\nclojure.java.jdbc provides a simple abstraction for CRUD (create, read,\nupdate, delete) operations on a SQL database, along with basic transaction\nsupport. Basic DDL operations are also supported (create table, drop table,\naccess to table metadata).\n\nMaps are used to represent records, making it easy to store and retrieve\ndata. Results can be processed using any standard sequence operations.\n\nFor most operations, Java's PreparedStatement is used so your SQL and\nparameters can be represented as simple vectors where the first element\nis the SQL string, with ? for each parameter, and the remaining elements\nare the parameter values to be substituted. In general, operations return\nthe number of rows affected, except for a single record insert where any\ngenerated keys are returned (as a map)."}
  {:source-url
   "https://github.com/clojure/java.jdbc/blob/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc/clojure.java.jdbc.internal-api.html",
   :name "clojure.java.jdbc.internal",
   :doc nil}),
 :vars
 ({:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L43",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/as-identifier",
   :namespace "clojure.java.jdbc",
   :line 43,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj",
   :var-type "var",
   :doc
   "Given a string, return it as-is.  Given a keyword, return\nit as a string using the current naming strategy.",
   :name "as-identifier"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L48",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/as-keyword",
   :namespace "clojure.java.jdbc",
   :line 48,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj",
   :var-type "var",
   :doc
   "Given a string, return it as a keyword using the current\nnaming strategy.  Given a keyword, return it as-is.",
   :name "as-keyword"}
  {:arglists ([naming-strategy x]),
   :name "as-named-identifier",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L80",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/as-named-identifier",
   :doc
   "Given a naming strategy and a keyword, return the keyword as a string using the \nentity naming strategy.\nGiven a naming strategy and a string, return the string as-is.\nThe naming strategy should either be a function (the entity naming strategy) or \na map containing :entity and/or :keyword keys which provide the entity naming\nstrategy and/or keyword naming strategy respectively.",
   :var-type "function",
   :line 80,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([naming-strategy x]),
   :name "as-named-keyword",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L90",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/as-named-keyword",
   :doc
   "Given a naming strategy and a string, return the string as a keyword using the \nkeyword naming strategy.\nGiven a naming strategy and a keyword, return the keyword as-is.\nThe naming strategy should either be a function (the entity naming strategy) or \na map containing :entity and/or :keyword keys which provide the entity naming\nstrategy and/or keyword naming strategy respectively.\nNote that providing a single function will cause the default keyword naming\nstrategy to be used!",
   :var-type "function",
   :line 90,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([q x]),
   :name "as-quoted-identifier",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L102",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/as-quoted-identifier",
   :doc
   "Given a quote pattern - either a single character or a pair of characters in\na vector - and a keyword, return the keyword as a string using a simple\nquoting naming strategy.\nGiven a qote pattern and a string, return the string as-is.\n  (as-quoted-identifier X :name) will return XnameX as a string.\n  (as-quoted-identifier [A B] :name) will return AnameB as a string.",
   :var-type "function",
   :line 102,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([q x]),
   :name "as-quoted-str",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L70",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/as-quoted-str",
   :doc
   "Given a quoting pattern - either a single character or a vector pair of\ncharacters - and a string, return the quoted string:\n  (as-quoted-str X foo) will return XfooX\n  (as-quoted-str [A B] foo) will return AfooB",
   :var-type "function",
   :line 70,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L58",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/connection",
   :namespace "clojure.java.jdbc",
   :line 58,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj",
   :var-type "var",
   :doc
   "Returns the current database connection (or throws if\nthere is none)",
   :name "connection"}
  {:arglists ([name & specs]),
   :name "create-table",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L188",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/create-table",
   :doc
   "Creates a table on the open database connection given a table name and\nspecs. Each spec is either a column spec: a vector containing a column\nname and optionally a type and other constraints, or a table-level\nconstraint: a vector containing words that express the constraint. All\nwords used to describe the table may be supplied as strings or keywords.",
   :var-type "function",
   :line 188,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([table where-params]),
   :name "delete-rows",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L255",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/delete-rows",
   :doc
   "Deletes rows from a table. where-params is a vector containing a string\nproviding the (optionally parameterized) selection criteria followed by\nvalues for any parameters.",
   :var-type "function",
   :line 255,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([& commands]),
   :name "do-commands",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L172",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/do-commands",
   :doc "Executes SQL commands on the open database connection.",
   :var-type "function",
   :line 172,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([sql & param-groups]),
   :name "do-prepared",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L181",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/do-prepared",
   :doc
   "Executes an (optionally parameterized) SQL prepared statement on the\nopen database connection. Each param-group is a seq of values for all of\nthe parameters.",
   :var-type "function",
   :line 181,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([name]),
   :name "drop-table",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L204",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/drop-table",
   :doc
   "Drops a table on the open database connection given its name, a string\nor keyword",
   :var-type "function",
   :line 204,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L53",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/find-connection",
   :namespace "clojure.java.jdbc",
   :line 53,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj",
   :var-type "var",
   :doc
   "Returns the current database connection (or nil if there\nis none)",
   :name "find-connection"}
  {:arglists ([table record]),
   :name "insert-record",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L247",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/insert-record",
   :doc
   "Inserts a single record into a table. A record is a map from strings or\nkeywords (identifying columns) to values.\nReturns a map of the generated keys.",
   :var-type "function",
   :line 247,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([table & records]),
   :name "insert-records",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L239",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/insert-records",
   :doc
   "Inserts records into a table. records are maps from strings or keywords\n(identifying columns) to values. Inserts the records one at a time.\nReturns a sequence of maps containing the generated keys for each record.",
   :var-type "function",
   :line 239,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([table & rows]),
   :name "insert-rows",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L232",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/insert-rows",
   :doc
   "Inserts complete rows into a table. Each row is a vector of values for\neach of the table's columns in order.\nIf a single row is inserted, returns a map of the generated keys.",
   :var-type "function",
   :line 232,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([table column-names & value-groups]),
   :name "insert-values",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L211",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/insert-values",
   :doc
   "Inserts rows into a table with values for specified columns only.\ncolumn-names is a vector of strings or keywords identifying columns. Each\nvalue-group is a vector containing a values for each column in\norder. When inserting complete rows (all columns), consider using\ninsert-rows instead.\nIf a single set of values is inserted, returns a map of the generated keys.",
   :var-type "function",
   :line 211,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([]),
   :name "is-rollback-only",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L166",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/is-rollback-only",
   :doc
   "Returns true if the outermost transaction will rollback rather than\ncommit when complete",
   :var-type "function",
   :line 166,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([exception]),
   :name "print-sql-exception",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L303",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/print-sql-exception",
   :doc "Prints the contents of an SQLException to *out*",
   :var-type "function",
   :line 303,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([exception]),
   :name "print-sql-exception-chain",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L316",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/print-sql-exception-chain",
   :doc "Prints a chain of SQLExceptions to *out*",
   :var-type "function",
   :line 316,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([exception]),
   :name "print-update-counts",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L324",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/print-update-counts",
   :doc
   "Prints the update counts from a BatchUpdateException to *out*",
   :var-type "function",
   :line 324,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L63",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/resultset-seq",
   :namespace "clojure.java.jdbc",
   :line 63,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj",
   :var-type "var",
   :doc
   "Creates and returns a lazy sequence of structmaps\ncorresponding to the rows in the java.sql.ResultSet\nrs. Based on clojure.core/resultset-seq but it respects\nthe current naming strategy.",
   :name "resultset-seq"}
  {:arglists ([]),
   :name "set-rollback-only",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L160",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/set-rollback-only",
   :doc
   "Marks the outermost transaction such that it will rollback rather than\ncommit when complete",
   :var-type "function",
   :line 160,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([& body]),
   :name "transaction",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L149",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/transaction",
   :doc
   "Evaluates body as a transaction on the open database connection. Any\nnested transactions are absorbed into the outermost transaction. By\ndefault, all database updates are committed together as a group after\nevaluating the outermost body, or rolled back on any uncaught\nexception. If set-rollback-only is called within scope of the outermost\ntransaction, the entire transaction will be rolled back rather than\ncommitted when complete.",
   :var-type "macro",
   :line 149,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([table where-params record]),
   :name "update-or-insert-values",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L282",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/update-or-insert-values",
   :doc
   "Updates values on selected rows in a table, or inserts a new row when no\nexisting row matches the selection criteria. where-params is a vector\ncontaining a string providing the (optionally parameterized) selection\ncriteria followed by values for any parameters. record is a map from\nstrings or keywords (identifying columns) to updated values.",
   :var-type "function",
   :line 282,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([table where-params record]),
   :name "update-values",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L267",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/update-values",
   :doc
   "Updates values on selected rows in a table. where-params is a vector\ncontaining a string providing the (optionally parameterized) selection\ncriteria followed by values for any parameters. record is a map from\nstrings or keywords (identifying columns) to updated values.",
   :var-type "function",
   :line 267,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([db-spec & body]),
   :name "with-connection",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L123",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/with-connection",
   :doc
   "Evaluates body in the context of a new connection to a database then\ncloses the connection. db-spec is a map containing values for one of the\nfollowing parameter sets:\n\nFactory:\n  :factory     (required) a function of one argument, a map of params\n  (others)     (optional) passed to the factory function in a map\n\nDriverManager:\n  :classname   (required) a String, the jdbc driver class name\n  :subprotocol (required) a String, the jdbc subprotocol\n  :subname     (required) a String, the jdbc subname\n  (others)     (optional) passed to the driver as properties.\n\nDataSource:\n  :datasource  (required) a javax.sql.DataSource\n  :username    (optional) a String\n  :password    (optional) a String, required if :username is supplied\n\nJNDI:\n  :name        (required) a String or javax.naming.Name\n  :environment (optional) a java.util.Map",
   :var-type "macro",
   :line 123,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([naming-strategy & body]),
   :name "with-naming-strategy",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L112",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/with-naming-strategy",
   :doc "Evaluates body in the context of a naming strategy.",
   :var-type "macro",
   :line 112,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([results sql-params & body]),
   :name "with-query-results",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L295",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/with-query-results",
   :doc
   "Executes a query, then evaluates body with results bound to a seq of the\nresults. sql-params is a vector containing a string providing\nthe (optionally parameterized) SQL query followed by values for any\nparameters.",
   :var-type "macro",
   :line 295,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([q & body]),
   :name "with-quoted-identifiers",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj#L118",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/05207126ff17befa660bcf5cf9ad4248af32c315/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/with-quoted-identifiers",
   :doc
   "Evaluates body in the context of a simple quoting naming strategy.",
   :var-type "macro",
   :line 118,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:name "*as-key*",
   :namespace "clojure.java.jdbc.internal",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj#L55",
   :dynamic true,
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/*as-key*",
   :doc
   "The default keyword naming strategy is to lowercase the entity.",
   :var-type "var",
   :line 55,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc/internal.clj"}
  {:name "*as-str*",
   :namespace "clojure.java.jdbc.internal",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj#L50",
   :dynamic true,
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/*as-str*",
   :doc "The default entity naming strategy is to do nothing.",
   :var-type "var",
   :line 50,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc/internal.clj"}
  {:arglists ([x]),
   :name "as-identifier*",
   :namespace "clojure.java.jdbc.internal",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj#L78",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/as-identifier*",
   :doc
   "Given a keyword, convert it to a string using the current naming\nstrategy.\nGiven a string, return it as-is.",
   :var-type "function",
   :line 78,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc/internal.clj"}
  {:arglists ([f x]),
   :name "as-key",
   :namespace "clojure.java.jdbc.internal",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj#L69",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/as-key",
   :doc
   "Given a naming strategy and a string, return the string as a\nkeyword per that naming strategy. Given (a naming strategy and)\na keyword, return it as-is.",
   :var-type "function",
   :line 69,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc/internal.clj"}
  {:arglists ([x]),
   :name "as-keyword*",
   :namespace "clojure.java.jdbc.internal",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj#L85",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/as-keyword*",
   :doc
   "Given an entity name (string), convert it to a keyword using the\ncurrent naming strategy.\nGiven a keyword, return it as-is.",
   :var-type "function",
   :line 85,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc/internal.clj"}
  {:arglists ([f x]),
   :name "as-str",
   :namespace "clojure.java.jdbc.internal",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj#L60",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/as-str",
   :doc
   "Given a naming strategy and a keyword, return the keyword as a\nstring per that naming strategy. Given (a naming strategy and)\na string, return it as-is.",
   :var-type "function",
   :line 60,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc/internal.clj"}
  {:arglists ([]),
   :name "connection*",
   :namespace "clojure.java.jdbc.internal",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj#L37",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/connection*",
   :doc
   "Returns the current database connection (or throws if there is none)",
   :var-type "function",
   :line 37,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc/internal.clj"}
  {:arglists ([return-keys sql & param-groups]),
   :name "do-prepared*",
   :namespace "clojure.java.jdbc.internal",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj#L209",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/do-prepared*",
   :doc
   "Executes an (optionally parameterized) SQL prepared statement on the\nopen database connection. Each param-group is a seq of values for all of\nthe parameters.\nIf return-keys, return the generated keys for the (single) update/insert.\nelse return a seq of update counts (one count for each param-group).",
   :var-type "function",
   :line 209,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc/internal.clj"}
  {:arglists ([]),
   :name "find-connection*",
   :namespace "clojure.java.jdbc.internal",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj#L32",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/find-connection*",
   :doc
   "Returns the current database connection (or nil if there is none)",
   :var-type "function",
   :line 32,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc/internal.clj"}
  {:arglists
   ([{:keys
      [factory
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
   :namespace "clojure.java.jdbc.internal",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj#L102",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/get-connection",
   :doc
   "Creates a connection to a database. db-spec is a map containing values\nfor one of the following parameter sets:\n\nFactory:\n  :factory     (required) a function of one argument, a map of params\n  (others)     (optional) passed to the factory function in a map\n\nDriverManager:\n  :classname   (required) a String, the jdbc driver class name\n  :subprotocol (required) a String, the jdbc subprotocol\n  :subname     (required) a String, the jdbc subname\n  (others)     (optional) passed to the driver as properties.\n\nDataSource:\n  :datasource  (required) a javax.sql.DataSource\n  :username    (optional) a String\n  :password    (optional) a String, required if :username is supplied\n\nJNDI:\n  :name        (required) a String or javax.naming.Name\n  :environment (optional) a java.util.Map",
   :var-type "function",
   :line 102,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc/internal.clj"}
  {:arglists ([rs]),
   :name "resultset-seq*",
   :namespace "clojure.java.jdbc.internal",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj#L190",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/resultset-seq*",
   :doc
   "Creates and returns a lazy sequence of structmaps corresponding to\nthe rows in the java.sql.ResultSet rs. Based on clojure.core/resultset-seq\nbut it respects the current naming strategy.",
   :var-type "function",
   :line 190,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc/internal.clj"}
  {:arglists ([] [val]),
   :name "rollback",
   :namespace "clojure.java.jdbc.internal",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj#L43",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/rollback",
   :doc "Accessor for the rollback flag on the current connection",
   :var-type "function",
   :line 43,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc/internal.clj"}
  {:arglists ([e]),
   :name "throw-rollback",
   :namespace "clojure.java.jdbc.internal",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj#L157",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/throw-rollback",
   :doc "Sets rollback and throws a wrapped exception",
   :var-type "function",
   :line 157,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc/internal.clj"}
  {:arglists ([func]),
   :name "transaction*",
   :namespace "clojure.java.jdbc.internal",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj#L163",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/transaction*",
   :doc
   "Evaluates func as a transaction on the open database connection. Any\nnested transactions are absorbed into the outermost transaction. By\ndefault, all database updates are committed together as a group after\nevaluating the outermost body, or rolled back on any uncaught\nexception. If rollback is set within scope of the outermost transaction,\nthe entire transaction will be rolled back rather than committed when\ncomplete.",
   :var-type "function",
   :line 163,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc/internal.clj"}
  {:arglists ([db-spec func]),
   :name "with-connection*",
   :namespace "clojure.java.jdbc.internal",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj#L149",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/with-connection*",
   :doc
   "Evaluates func in the context of a new connection to a database then\ncloses the connection.",
   :var-type "function",
   :line 149,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc/internal.clj"}
  {:arglists ([[sql & params :as sql-params] func]),
   :name "with-query-results*",
   :namespace "clojure.java.jdbc.internal",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj#L232",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/9240d66c9124454187416861a0e2021952eb4f40/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/with-query-results*",
   :doc
   "Executes a query, then evaluates func passing in a seq of the results as\nan argument. The first argument is a vector containing the (optionally\nparameterized) sql query string followed by values for any parameters.",
   :var-type "function",
   :line 232,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc/internal.clj"})}
