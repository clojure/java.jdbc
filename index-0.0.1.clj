{:namespaces
 ({:source-url
   "https://github.com/clojure/java.jdbc/blob/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc/clojure.java.jdbc-api.html",
   :name "clojure.java.jdbc",
   :author "Stephen C. Gilardi, Sean Corfield",
   :doc "A Clojure interface to sql databases via jdbc."}
  {:source-url
   "https://github.com/clojure/java.jdbc/blob/72c1b8f74ab4d3810162a84fef52fa55468a5bdf/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc/clojure.java.jdbc.internal-api.html",
   :name "clojure.java.jdbc.internal",
   :doc nil}),
 :vars
 ({:arglists ([name & specs]),
   :name "create-table",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj#L103",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/create-table",
   :doc
   "Creates a table on the open database connection given a table name and\nspecs. Each spec is either a column spec: a vector containing a column\nname and optionally a type and other constraints, or a table-level\nconstraint: a vector containing words that express the constraint. All\nwords used to describe the table may be supplied as strings or keywords.",
   :var-type "function",
   :line 103,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([table where-params]),
   :name "delete-rows",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj#L170",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/delete-rows",
   :doc
   "Deletes rows from a table. where-params is a vector containing a string\nproviding the (optionally parameterized) selection criteria followed by\nvalues for any parameters.",
   :var-type "function",
   :line 170,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([& commands]),
   :name "do-commands",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj#L81",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/do-commands",
   :doc "Executes SQL commands on the open database connection.",
   :var-type "function",
   :line 81,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([sql & param-groups]),
   :name "do-prepared",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj#L90",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/do-prepared",
   :doc
   "Executes an (optionally parameterized) SQL prepared statement on the\nopen database connection. Each param-group is a seq of values for all of\nthe parameters.",
   :var-type "function",
   :line 90,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([name]),
   :name "drop-table",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj#L119",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/drop-table",
   :doc
   "Drops a table on the open database connection given its name, a string\nor keyword",
   :var-type "function",
   :line 119,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([table record]),
   :name "insert-record",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj#L162",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/insert-record",
   :doc
   "Inserts a single record into a table. A record is a map from strings or\nkeywords (identifying columns) to values.\nReturns a map of the generated keys.",
   :var-type "function",
   :line 162,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([table & records]),
   :name "insert-records",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj#L154",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/insert-records",
   :doc
   "Inserts records into a table. records are maps from strings or keywords\n(identifying columns) to values. Inserts the records one at a time.\nReturns a sequence of maps containing the generated keys for each record.",
   :var-type "function",
   :line 154,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([table & rows]),
   :name "insert-rows",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj#L147",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/insert-rows",
   :doc
   "Inserts complete rows into a table. Each row is a vector of values for\neach of the table's columns in order.\nIf a single row is inserted, returns a map of the generated keys.",
   :var-type "function",
   :line 147,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([table column-names & value-groups]),
   :name "insert-values",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj#L126",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/insert-values",
   :doc
   "Inserts rows into a table with values for specified columns only.\ncolumn-names is a vector of strings or keywords identifying columns. Each\nvalue-group is a vector containing a values for each column in\norder. When inserting complete rows (all columns), consider using\ninsert-rows instead.\nIf a single set of values is inserted, returns a map of the generated keys.",
   :var-type "function",
   :line 126,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([]),
   :name "is-rollback-only",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj#L75",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/is-rollback-only",
   :doc
   "Returns true if the outermost transaction will rollback rather than\ncommit when complete",
   :var-type "function",
   :line 75,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([]),
   :name "set-rollback-only",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj#L69",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/set-rollback-only",
   :doc
   "Marks the outermost transaction such that it will rollback rather than\ncommit when complete",
   :var-type "function",
   :line 69,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([& body]),
   :name "transaction",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj#L58",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/transaction",
   :doc
   "Evaluates body as a transaction on the open database connection. Any\nnested transactions are absorbed into the outermost transaction. By\ndefault, all database updates are committed together as a group after\nevaluating the outermost body, or rolled back on any uncaught\nexception. If set-rollback-only is called within scope of the outermost\ntransaction, the entire transaction will be rolled back rather than\ncommitted when complete.",
   :var-type "macro",
   :line 58,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([table where-params record]),
   :name "update-or-insert-values",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj#L197",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/update-or-insert-values",
   :doc
   "Updates values on selected rows in a table, or inserts a new row when no\nexisting row matches the selection criteria. where-params is a vector\ncontaining a string providing the (optionally parameterized) selection\ncriteria followed by values for any parameters. record is a map from\nstrings or keywords (identifying columns) to updated values.",
   :var-type "function",
   :line 197,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([table where-params record]),
   :name "update-values",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj#L182",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/update-values",
   :doc
   "Updates values on selected rows in a table. where-params is a vector\ncontaining a string providing the (optionally parameterized) selection\ncriteria followed by values for any parameters. record is a map from\nstrings or keywords (identifying columns) to updated values.",
   :var-type "function",
   :line 182,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([db-spec & body]),
   :name "with-connection",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj#L32",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/with-connection",
   :doc
   "Evaluates body in the context of a new connection to a database then\ncloses the connection. db-spec is a map containing values for one of the\nfollowing parameter sets:\n\nFactory:\n  :factory     (required) a function of one argument, a map of params\n  (others)     (optional) passed to the factory function in a map\n\nDriverManager:\n  :classname   (required) a String, the jdbc driver class name\n  :subprotocol (required) a String, the jdbc subprotocol\n  :subname     (required) a String, the jdbc subname\n  (others)     (optional) passed to the driver as properties.\n\nDataSource:\n  :datasource  (required) a javax.sql.DataSource\n  :username    (optional) a String\n  :password    (optional) a String, required if :username is supplied\n\nJNDI:\n  :name        (required) a String or javax.naming.Name\n  :environment (optional) a java.util.Map",
   :var-type "macro",
   :line 32,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([results sql-params & body]),
   :name "with-query-results",
   :namespace "clojure.java.jdbc",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj#L210",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/eb6ab97e9d402a1f981ea0dc21d23410fcd27f12/src/main/clojure/clojure/java/jdbc.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc/with-query-results",
   :doc
   "Executes a query, then evaluates body with results bound to a seq of the\nresults. sql-params is a vector containing a string providing\nthe (optionally parameterized) SQL query followed by values for any\nparameters.",
   :var-type "macro",
   :line 210,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc.clj"}
  {:arglists ([]),
   :name "connection*",
   :namespace "clojure.java.jdbc.internal",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/72c1b8f74ab4d3810162a84fef52fa55468a5bdf/src/main/clojure/clojure/java/jdbc/internal.clj#L36",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/72c1b8f74ab4d3810162a84fef52fa55468a5bdf/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/connection*",
   :doc
   "Returns the current database connection (or throws if there is none)",
   :var-type "function",
   :line 36,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc/internal.clj"}
  {:arglists ([return-keys sql & param-groups]),
   :name "do-prepared*",
   :namespace "clojure.java.jdbc.internal",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/72c1b8f74ab4d3810162a84fef52fa55468a5bdf/src/main/clojure/clojure/java/jdbc/internal.clj#L195",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/72c1b8f74ab4d3810162a84fef52fa55468a5bdf/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/do-prepared*",
   :doc
   "Executes an (optionally parameterized) SQL prepared statement on the\nopen database connection. Each param-group is a seq of values for all of\nthe parameters.",
   :var-type "function",
   :line 195,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc/internal.clj"}
  {:arglists ([]),
   :name "find-connection*",
   :namespace "clojure.java.jdbc.internal",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/72c1b8f74ab4d3810162a84fef52fa55468a5bdf/src/main/clojure/clojure/java/jdbc/internal.clj#L31",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/72c1b8f74ab4d3810162a84fef52fa55468a5bdf/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/find-connection*",
   :doc
   "Returns the current database connection (or nil if there is none)",
   :var-type "function",
   :line 31,
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
   "https://github.com/clojure/java.jdbc/blob/72c1b8f74ab4d3810162a84fef52fa55468a5bdf/src/main/clojure/clojure/java/jdbc/internal.clj#L65",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/72c1b8f74ab4d3810162a84fef52fa55468a5bdf/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/get-connection",
   :doc
   "Creates a connection to a database. db-spec is a map containing values\nfor one of the following parameter sets:\n\nFactory:\n  :factory     (required) a function of one argument, a map of params\n  (others)     (optional) passed to the factory function in a map\n\nDriverManager:\n  :classname   (required) a String, the jdbc driver class name\n  :subprotocol (required) a String, the jdbc subprotocol\n  :subname     (required) a String, the jdbc subname\n  (others)     (optional) passed to the driver as properties.\n\nDataSource:\n  :datasource  (required) a javax.sql.DataSource\n  :username    (optional) a String\n  :password    (optional) a String, required if :username is supplied\n\nJNDI:\n  :name        (required) a String or javax.naming.Name\n  :environment (optional) a java.util.Map",
   :var-type "function",
   :line 65,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc/internal.clj"}
  {:arglists ([stream exception]),
   :name "print-sql-exception",
   :namespace "clojure.java.jdbc.internal",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/72c1b8f74ab4d3810162a84fef52fa55468a5bdf/src/main/clojure/clojure/java/jdbc/internal.clj#L120",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/72c1b8f74ab4d3810162a84fef52fa55468a5bdf/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/print-sql-exception",
   :doc "Prints the contents of an SQLException to stream",
   :var-type "function",
   :line 120,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc/internal.clj"}
  {:arglists ([stream exception]),
   :name "print-sql-exception-chain",
   :namespace "clojure.java.jdbc.internal",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/72c1b8f74ab4d3810162a84fef52fa55468a5bdf/src/main/clojure/clojure/java/jdbc/internal.clj#L134",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/72c1b8f74ab4d3810162a84fef52fa55468a5bdf/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/print-sql-exception-chain",
   :doc "Prints a chain of SQLExceptions to stream",
   :var-type "function",
   :line 134,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc/internal.clj"}
  {:arglists ([stream exception]),
   :name "print-update-counts",
   :namespace "clojure.java.jdbc.internal",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/72c1b8f74ab4d3810162a84fef52fa55468a5bdf/src/main/clojure/clojure/java/jdbc/internal.clj#L142",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/72c1b8f74ab4d3810162a84fef52fa55468a5bdf/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/print-update-counts",
   :doc
   "Prints the update counts from a BatchUpdateException to stream",
   :var-type "function",
   :line 142,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc/internal.clj"}
  {:arglists ([] [val]),
   :name "rollback",
   :namespace "clojure.java.jdbc.internal",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/72c1b8f74ab4d3810162a84fef52fa55468a5bdf/src/main/clojure/clojure/java/jdbc/internal.clj#L42",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/72c1b8f74ab4d3810162a84fef52fa55468a5bdf/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/rollback",
   :doc "Accessor for the rollback flag on the current connection",
   :var-type "function",
   :line 42,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc/internal.clj"}
  {:arglists ([e]),
   :name "throw-rollback",
   :namespace "clojure.java.jdbc.internal",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/72c1b8f74ab4d3810162a84fef52fa55468a5bdf/src/main/clojure/clojure/java/jdbc/internal.clj#L155",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/72c1b8f74ab4d3810162a84fef52fa55468a5bdf/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/throw-rollback",
   :doc "Sets rollback and throws a wrapped exception",
   :var-type "function",
   :line 155,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc/internal.clj"}
  {:arglists ([func]),
   :name "transaction*",
   :namespace "clojure.java.jdbc.internal",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/72c1b8f74ab4d3810162a84fef52fa55468a5bdf/src/main/clojure/clojure/java/jdbc/internal.clj#L161",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/72c1b8f74ab4d3810162a84fef52fa55468a5bdf/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/transaction*",
   :doc
   "Evaluates func as a transaction on the open database connection. Any\nnested transactions are absorbed into the outermost transaction. By\ndefault, all database updates are committed together as a group after\nevaluating the outermost body, or rolled back on any uncaught\nexception. If rollback is set within scope of the outermost transaction,\nthe entire transaction will be rolled back rather than committed when\ncomplete.",
   :var-type "function",
   :line 161,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc/internal.clj"}
  {:arglists ([db-spec func]),
   :name "with-connection*",
   :namespace "clojure.java.jdbc.internal",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/72c1b8f74ab4d3810162a84fef52fa55468a5bdf/src/main/clojure/clojure/java/jdbc/internal.clj#L112",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/72c1b8f74ab4d3810162a84fef52fa55468a5bdf/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/with-connection*",
   :doc
   "Evaluates func in the context of a new connection to a database then\ncloses the connection.",
   :var-type "function",
   :line 112,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc/internal.clj"}
  {:arglists ([[sql & params :as sql-params] func]),
   :name "with-query-results*",
   :namespace "clojure.java.jdbc.internal",
   :source-url
   "https://github.com/clojure/java.jdbc/blob/72c1b8f74ab4d3810162a84fef52fa55468a5bdf/src/main/clojure/clojure/java/jdbc/internal.clj#L214",
   :raw-source-url
   "https://github.com/clojure/java.jdbc/raw/72c1b8f74ab4d3810162a84fef52fa55468a5bdf/src/main/clojure/clojure/java/jdbc/internal.clj",
   :wiki-url
   "http://clojure.github.com/java.jdbc//clojure.java.jdbc-api.html#clojure.java.jdbc.internal/with-query-results*",
   :doc
   "Executes a query, then evaluates func passing in a seq of the results as\nan argument. The first argument is a vector containing the (optionally\nparameterized) sql query string followed by values for any parameters.",
   :var-type "function",
   :line 214,
   :file
   "/home/tom/src/clj/autodoc/../autodoc-work-area/java.jdbc/src/src/main/clojure/clojure/java/jdbc/internal.clj"})}
