Changes in 0.6.1

* `insert!` and `insert-multi!` now default `:transaction?` to `true` (as they should have done in 0.6.0!) [JDBC-128](http://dev.clojure.org/jira/browse/JDBC-128). These two functions also have improved docstrings to clarify the difference in behavior between inserting rows as maps compared to inserting rows as a series of column values.
* PostgreSQL support has been improved: java.jdbc is now tested against PostgreSQL locally (as well as SQLite, Apache Derby, HSQLDB, H2, MySQL, MS SQL Server (both MS Type 4 driver and jTDS driver). [JDBC-127](http://dev.clojure.org/jira/browse/JDBC-127) and [JDBC-129](http://dev.clojure.org/jira/browse/JDBC-129).

Changes in 0.6.0

* `find-by-keys` now correctly handles `nil` values [JDBC-126](http://dev.clojure.org/jira/browse/JDBC-126).
* `find-by-keys` calls `seq` on `:order-by` to treat `[]` as no `ORDER BY` clause.

Changes in 0.6.0-rc2

* `db-query-with-resultset` now accepts an options map and passes it to `prepare-statement` [JDBC-125](http://dev.clojure.org/jira/browse/JDBC-125).
  - Passing the `prepare-statement` options map as the first element of the `[sql & params]` vector is no longer supported and will throw an `IllegalArgumentException`. It was always very poorly documented and almost never used, as far as I can tell.
* `db-query-with-resultset` no longer requires the `sql-params` argument to be a vector: a sequence is acceptable. This is in line with other functions that accept a sequence.
* `db-query-with-resultset` now accepts a bare SQL string or `PreparedStatement` as the `sql-params` argument, when there are no parameters needed. This is in line with other functions that accept SQL or a `PreparedStatement`.
* `query`'s options map now is passed to `db-query-with-resultset` and thus can contain options to be used to construct the `PreparedStatement` [JDBC-125](http://dev.clojure.org/jira/browse/JDBC-125).
* `find-by-keys` now accepts an `:order-by` option that specifies a sequence of orderings; an ordering is either a column (to sort ascending) or a map from column name to direct (`:asc` or `:desc`).

Changes in 0.6.0-rc1

* Adds `get-by-id` and `find-by-keys` convenience functions (these were easy to add after the API changes in 0.6.0 and we rely very heavily on them at World Singles so putting them in the core for everyone seemed reasonable).
* REMINDER: ALL DEPRECATED FUNCTIONALITY HAS BEEN REMOVED! [JDBC-118](http://dev.clojure.org/jira/browse/JDBC-118).
  - See alpha2 / alpha1 below for more details.

Changes in 0.6.0-alpha2

* ALL DEPRECATED FUNCTIONALITY HAS BEEN REMOVED! [JDBC-118](http://dev.clojure.org/jira/browse/JDBC-118).
  - This removes deprecated functionality from db-do-commands and db-do-prepared* which should have been removed in Alpha 1.
* Ensures SQL / params are actually vectors prior to destructuring (this addresses an interop edge case from other languages) [JDBC-124](http://dev.clojure.org/jira/browse/JDBC-124).
* Fix typo in `insert-multi!` argument validation exception [JDBC-123](http://dev.clojure.org/jira/browse/JDBC-123).

Changes in 0.6.0-alpha1

* (ALMOST) ALL DEPRECATED FUNCTIONALITY HAS BEEN REMOVED! [JDBC-118](http://dev.clojure.org/jira/browse/JDBC-118).
  - See changes described in versions 0.5.5 through 0.5.8 for what was deprecated
  - Use version 0.5.8 as a bridge to identify any deprecated API calls on which your code relies!
  - `db-transaction` (deprecated in version 0.3.0) has been removed
  - The `java.jdbc.deprecated` namespace has been removed
  
Changes in 0.5.8

* `db-do-commands` now expects multiple commands to be be wrapped in a vector [JDBC-122](http://dev.clojure.org/jira/browse/JDBC-123). The single command form is unchanged (but may be wrapped in a vector). Calling `db-do-commands` with multiple commands (not wrapped in a single vector) will produce a "DEPRECATED" warning printed to the console.
* `db-do-prepared` and `db-do-prepared-return-keys` now expect to receive a `db-spec`, an optional `transaction?` boolean, a `sql-params` argument, and an optional options map. `sql-params` is a vector containing a SQL string or `PreparedStatement` followed by parameters -- like other APIs in this library. In addition, like the `:multi? true` version of `execute!`, `db-do-prepared` can accept a vector that has parameter groups: multiple vectors containing groups of parameter values [JDBC-122](http://dev.clojure.org/jira/browse/JDBC-123). Calling `db-do-prepared` with unrolled arguments -- the SQL string / statement followed by parameter groups -- is deprecated and will produce "DEPRECATED" warnings printed to the console.

Changes in 0.5.7

* `(insert! db table [:col] ["val"] {})` syntax, introduced in 0.5.6, threw an exception [JDBC-121](http://dev.clojure.org/jira/browse/JDBC-121).

Changes in 0.5.6

* `create-table-ddl` now expects the column specs to be wrapped in a single vector and no longer needs the `:options` delimiter to specify the options map [JDBC-120](http://dev.clojure.org/jira/browse/JDBC-120). If column specs are not wrapped in a vector, you will get a "DEPRECATED" warning printed to the console.
* `insert!` now supports only single row insertion; multi-row insertion is deprecated. `insert-multi!` has been added for multi-row insertion. `:options` is no longer needed as a delimiter for the options map [JDBC-119](http://dev.clojure.org/jira/browse/JDBC-119). If `insert!` is called with multiple rows, or `:options` is specified, you will get a "DEPRECATED" warning printed to the console.
* NOTE: all deprecated functionality will go away in version 0.6.0!

Changes in 0.5.5

* Allow options map in all calls that previously took optional keyword arguments [JDBC-117](http://dev.clojure.org/jira/browse/JDBC-117). The unrolled keyword argument forms of call are deprecated -- and print a "DEPRECATED" message to the console! -- and will go away in 0.6.0.

Changes in 0.5.0

* Allow PreparedStatement in db-do-prepared-return-keys [JDBC-115](http://dev.clojure.org/jira/browse/JDBC-115).
* Remove exception wrapping [JDBC-114](http://dev.clojure.org/jira/browse/JDBC-114).
* Drop Clojure 1.3 compatibility.

Changes in 0.4.2

* Remove redundant type hints [JDBC-113](http://dev.clojure.org/jira/browse/JDBC-113) - Michael Blume.
* Avoid reflection on `.prepareStatement` [JDBC-112](http://dev.clojure.org/jira/browse/JDBC-112) - Michael Blume.
* Add `metadata-query` macro to make metadata query / results easier to work with for [JDBC-107](http://dev.clojure.org/jira/browse/JDBC-107).
* `prepare-statement` `:return-keys` may now be a vector of (auto-generated) column names to return, in addition to just being truthy or falsey. This allows keys to be returned for more databases. [JDBC-104](http://dev.clojure.org/jira/browse/JDBC-104).
* Officially support H2 (and test against it) to support [JDBC-91](http://dev.clojure.org/jira/browse/JDBC-91) and clarify docstrings to improve debugging driver-specific restrictions on SQL.

Changes in 0.4.0 / 0.4.1

* `db-do-prepared` now allows `transaction?` to be omitted when a `PreparedStatement` is passed as the second argument [JDBC-111](http://dev.clojure.org/jira/browse/JDBC-111) - Stefan Kamphausen.
* Nested transaction checks isolation level is the same [JDBC-110](http://dev.clojure.org/jira/browse/JDBC-110) - Donald Ball.
* Default PostgreSQL port; Support more dbtype/dbname variants [JDBC-109](http://dev.clojure.org/jira/browse/JDBC-109).
* Drop Clojure 1.2 compatibility.

Changes in 0.3.7

* Bump all driver versions in `project.clj` and re-test.
* Remove duplicate `count` calls in `insert-sql` [JDBC-108](http://dev.clojure.org/jira/browse/JDBC-108) - Earl St Sauver.
* Remove driver versions from README and link to Maven Central [JDBC-106](http://dev.clojure.org/jira/browse/JDBC-106).
* Fix links in CHANGES and README [JDBC-103](http://dev.clojure.org/jira/browse/JDBC-103) - John Walker.

Changes in 0.3.6

* Arbitrary values allowed for `:cursors`, `:concurrency`, `:result-type` arguments to `prepare-statement` [JDBC-102](http://dev.clojure.org/jira/browse/JDBC-102).
* Allow `:as-arrays? :cols-as-is` to omit column name uniqueness when returning result sets as arrrays [JDBC-101](http://dev.clojure.org/jira/browse/JDBC-101).
* Add `:timeout` argument to `prepare-statement` [JDBC-100](http://dev.clojure.org/jira/browse/JDBC-100).

Changes in 0.3.5

* Reflection warnings on executeUpdate addressed.
* HSQLDB and SQLite in-memory strings are now accepted [JDBC-94](http://dev.clojure.org/jira/browse/JDBC-94).
* Add support for readonly transactions via :read-only? [JDBC-93](http://dev.clojure.org/jira/browse/JDBC-93).

Changes in 0.3.4

* execute! can now accept a PreparedStatement [JDBC-96](http://dev.clojure.org/jira/browse/JDBC-96).
* Support simpler db-spec with :dbtype and :dbname (and optional :host and :port etc) [JDBC-92](http://dev.clojure.org/jire/browse/JDBC-92).
* Support oracle:oci and oracle:thin subprotocols [JDBC-90](http://dev.clojure.org/jira/browse/JDBC-90).

Changes in 0.3.3

* Prevent exception/crash when query called with bare SQL string [JDBC-89](http://dev.clojure.org/jira/browse/JDBC-89).
* Add :row-fn and :result-set-fn to metadata-result function [JDBC-87](http://dev.clojure.org/jira/browse/JDBC-87).
* Support key/value configuration from URI (Phil Hagelberg).

Changes in 0.3.2

* Add nil protocol implementation to ISQLParameter.

Changes in 0.3.1 (broken)

* Improve docstrings and add :arglists for better auto-generated documentation.
* Make insert-sql private - technically a breaking change but it should never have been public: sorry folks!
* Provide better protocol for setting parameters in prepared statements [JDBC-86](http://dev.clojure.org/jira/browse/JDBC-86).
* Fix parens in two deprecated tests [JDBC-85](http://dev.clojure.org/jira/browse/JDBC-85).
* Made create-table-ddl less aggressive about applying as-sql-name so only first name in a column spec is affected.

Changes in 0.3.0

* Ensure canonical Boolean to workaround strange behavior in some JDBC drivers [JDBC-84](http://dev.clojure.org/jira/browse/JDBC-84).
* Rename recently introduced test to ensure unique names [JDBC-83](http://dev.clojure.org/jira/browse/JDBC-83).
* Rename unused arguments in protocol implementation to support Android [JDBC-82](http://dev.clojure.org/jira/browse/JDBC-82).
* Correctly handle empty param group sequence in execute! (which only seemed to affect SQLite) [JDBC-65](http://dev.clojure.org/jira/browse/JDBC-65).

Changes in 0.3.0-rc1

* Deprecate db-transaction (new in 0.3.0) in favor of with-db-transaction [JDBC-81](http://dev.clojure.org/jira/browse/JDBC-81).
* Add with-db-metadata macro and metadata-result function to make it easier to work with SQL metadata [JDBC-80](http://dev.clojure.org/jira/browse/JDBC-80).
* Add with-db-connection macro to make it easier to run groups of operations against a single open connection [JDBC-79](http://dev.clojure.org/jira/browse/JDBC-79).
* Add ISQLValue protocol to make it easier to support custom SQL types for parameters in SQL statements [JDBC-77](http://dev.clojure.org/jira/browse/JDBC-77).
* Add support for :isolation in with-db-transaction [JDBC-75](http://dev.clojure.org/jira/browse/JDBC-75).
* Add :user as an alias for :username for DataSource connections [JDBC-74](http://dev.clojure.org/jira/browse/JDBC-74).

Changes in 0.3.0-beta2

* The DSL namespaces introduced in 0.3.0-alpha1 have been retired - see [java-jdbc/dsl](https://github.com/seancorfield/jsql) for a migration path if you wish to continue using the DSL (although it is recommended you switch to another, more expressive DSL).
* The older API (0.2.3) which was deprecated in earlier 0.3.0 builds has moved to `clojure.java.jdbc.deprecated` to help streamline the API for 0.3.0 and clean up the documentation.

Changes in 0.3.0-beta1

* query as-arrays? now allows you to leverage lazy result fetching [JDBC-72](http://dev.clojure.org/jira/browse/JDBC-72).
* "h2" is recognized as a protocol shorthand for org.h2.Driver
* Tests no longer use :1 literal [JDBC-71](http://dev.clojure.org/jira/browse/JDBC-71).
* Conditional use of javax.naming.InitialContext so it can be compiled on Android [JDBC-69](http://dev.clojure.org/jira/browse/JDBC-69).
* New db-query-with-resultset function replaces private db-with-query-results* and processes a raw ResultSet object [JDBC-63](http://dev.clojure.org/jira/browse/JDBC-63).
* Allow :set-parameters in db-spec to override set-parameters internal function to allow per-DB special handling of SQL parameters values (such as null for Teradata) [JDBC-40](http://dev.clojure.org/jira/browse/JDBC-40).

Changes in 0.3.0-alpha5

* DDL now supports entities naming strategy [JDBC-53](http://dev.clojure.org/jira/browse/JDBC-53).
* Attempt to address potential memory leaks due to closures - see [Christophe Grand's blog post on Macros, closures and unexpected object retention](http://clj-me.cgrand.net/2013/09/11/macros-closures-and-unexpected-object-retention/).
* Documentation has moved to [Using java.jdbc on Clojure-Doc.org](http://clojure-doc.org/articles/ecosystem/java_jdbc/home.html)
* Added Leiningen support for easier development/testing (Maven is still the primary build tool).
* Added create-index / drop-index DDL [JDBC-62](http://dev.clojure.org/jira/browse/JDBC-62) - moquist
* Make transaction? boolean optional in various db-do-* functions
* Create clojure.java.jdbc.ddl namespace
* Add create-table, drop-table, create-index and drop-index
* Deprecate create-table, create-table-ddl and drop-table in main namespace
* Update README to clarify PostgreSQL instructions.
* Fix test suite for PostgreSQL [JDBC-59](http://dev.clojure.org/jira/browser/JDBC-59)
* Improve hooks for Oracle data type handling [JDBC-57](http://dev.clojure.org/jira/browser/JDBC-57)
* Fix reflection warnings [JDBC-55](http://dev.clojure.org/jira/browser/JDBC-55)

* DDL now supports entities naming strategy [JDBC-53](http://dev.clojure.org/jira/browse/JDBC-53).
* Attempt to address potential memory leaks due to closures - see [Christophe Grand's blog post on Macros, closures and unexpected object retention](http://clj-me.cgrand.net/2013/09/11/macros-closures-and-unexpected-object-retention/).
* Documentation has moved to [Using java.jdbc on Clojure-Doc.org](http://clojure-doc.org/articles/ecosystem/java_jdbc/home.html)
* Added Leiningen support for easier development/testing (Maven is still the primary build tool).
* Added create-index / drop-index DDL [JDBC-62](http://dev.clojure.org/jira/browse/JDBC-62) - moquist
* Make transaction? boolean optional in various db-do-* functions
  * It will ultimately change to a function argument I think when [JDBC-37](http://dev.clojure.org/jira/browser/JDBC-37) is dealt with
* Create clojure.java.jdbc.ddl namespace
  * Add create-table and drop-table
  * Deprecate create-table, create-table-ddl and drop-table in main namespace
  * More DDL is coming soon
* Update README to clarify PostgreSQL instructions.
* Fix test suite for PostgreSQL [JDBC-59](http://dev.clojure.org/jira/browser/JDBC-59)
* Improve hooks for Oracle data type handling [JDBC-57](http://dev.clojure.org/jira/browser/JDBC-57)
* Fix reflection warnings [JDBC-55](http://dev.clojure.org/jira/browser/JDBC-55)

Changes in 0.3.0-alpha4

* Fix connection leaks [JDBC-54](http://dev.clojure.org/jira/browser/JDBC-54)
* Allow order-by to accept empty sequence (and return empty string)

Changes in 0.3.0-alpha3

* Fix macro / import interaction by fully qualifying Connection type.

Changes in 0.3.0-alpha2

* Address [JDBC-51](http://dev.clojure.org/jira/browse/JDBC-51) by declaring get-connection returns java.sql.Connection
* Add IResultSetReadColumn protocol extension point for custom read conversions [JDBC-46](http://dev.clojure.org/jira/browse/JDBC-46)
* Add :multi? to execute! so it can be used for repeated operations [JDBC-52](http://dev.clojure.org/jira/browse/JDBC-52)
* Reverted specialized handling of NULL values (reopens [JDBC-40](http://dev.clojure.org/jira/browse/JDBC-40))
* Rename :as-arrays to :as-arrays? since it is boolean
* Add curried version of clojure.java.jdbc.sql/as-quoted-str
* Officially deprecate resultset-seq

Changes in 0.3.0-alpha1

Major overhaul of the API and deprecation of most of the old API!

* Add insert!, query, update!, delete! and execute! high-level API
  [JDBC-20](http://dev.clojure.org/jira/browse/JDBC-20)
* Add optional SQL-generating DSL in clojure.java.jdbc.sql (implied by JDBC-20)
* Add db- prefixed versions of low-level API
* Add db-transaction macro:

```
  (db-transaction [t-con db-spec]
    (query t-con (select * :user (where {:id 42}))))
```

* Add result-set-seq as replacement for resultset-seq (which will be deprecated)
* Transaction now correctly rollback on non-Exception Throwables
  [JDBC-43](http://dev.clojure.org/jira/browse/JDBC-43)
* Rewrite old API functions in terms of new API, and deprecate old API
  [JDBC-43](http://dev.clojure.org/jira/browse/JDBC-43)
* Add :as-arrays to query / result-set-seq
  [JDBC-41](http://dev.clojure.org/jira/browse/JDBC-41)
* Better handling of NULL values [JDBC-40](http://dev.clojure.org/jira/browse/JDBC-40)
  and [JDBC-18](http://dev.clojure.org/jira/browse/JDBC-18)
  Note: JDBC-40 has been reverted in 0.3.0-alpha2 because it introduced regressions for PostgreSQL
* db-do-commands allows you to execute SQL without a transaction wrapping it
  [JDBC-38](http://dev.clojure.org/jira/browse/JDBC-38)
* Remove reflection warning from execute-batch
* Add notes to README about 3rd party database driver dependencies
* Add optional :identifiers argument to resultset-seq so you can explicitly pass in the naming strategy

Changes in 0.2.3:

* as-str now treats a.b as two identifiers separated by . so quoting produces [a].[b] instead of [a.b]
* Add :connection-uri option [JDBC-34](http://dev.clojure.org/jira/browse/JDBC-34)

Changes in 0.2.2:

* Handle Oracle unknown row count affected [JDBC-33](http://dev.clojure.org/jira/browse/JDBC-33)
* Handle jdbc: prefix in string db-specs [JDBC-32](http://dev.clojure.org/jira/browse/JDBC-32)
* Handle empty columns in make column unique (Juergen Hoetzel) [JDBC-31](http://dev.clojure.org/jira/browse/JDBC-31)

Changes in 0.2.1:

* Result set performance enhancement (Juergen Hoetzel) [JDBC-29](http://dev.clojure.org/jira/browse/JDBC-29)
* Make do-prepared-return-keys (for Korma team) [JDBC-30](http://dev.clojure.org/jira/browse/JDBC-30)

Changes in 0.2.0:

* Merge internal namespace into main jdbc namespace and update symbol visibility / naming.

Changes in 0.1.4:

* Unwrap RTE for nested transaction exception (we already unwrapped top-level transaction RTEs).
* Remove reflection warning unwrapping RunTimeException (Alan Malloy)

Changes in 0.1.3:

* Fix JDBC-26 (fully) by adding transaction/generated keys support for SQLite3 (based on patch from Nelson Morris)

Changes in 0.1.2:

* Fix JDBC-23 by handling prepared statement params correctly (Ghadi Shayban)
* Fix JDBC-26 by adding support for SQLite3 (based on patch from Nelson Morris)
* Fix JDBC-27 by replacing replicate with repeat (Jonas Enlund)
* Ensure MS SQL Server passes tests with both Microsoft and jTDS drivers
* Build server now tests derby, hsqldb and sqlite by default
* Update README per Stuart Sierra's outline for contrib projects

Changes in 0.1.1:

* Fix JDBC-21 by adding support for db-spec as URI (Phil Hagelberg). 
* Fix JDBC-22 by deducing driver class name from subprotocol (Phil Hagelberg).
* Add Postgres dependency so tests can be automcated (Phil Hagelberg).
* Add ability to specify test databases via TEST_DBS environment variable (Phil Hagelberg).

Changes in 0.1.0:

* Fix JDBC-15 by removing dependence on deprecated structmap.

Changes in 0.0.7:

* Fix JDBC-9 by renaming duplicate columns instead of throwing an exception.
  - thanx to Peter Siewert!
* Fix JDBC-16 by ensuring do-prepared works with no param-groups provided.
* Fix JDBC-17 by adding type hints to remove more reflection warnings.
  - thanx to Stuart Sierra!
Documentation:
* Address JDBC-4 by documenting how to do connection pooling.

Changes in 0.0.6:

* Move former tests to test-utilities namespace - these do not touch a database
* Convert old "test" examples into real tests against real databases
  - tested locally against MySQL, Apache Derby, HSQLDB
  - build system should run against Apache Derby, HSQLSB
  - will add additional databases later
* Fix JDBC-12 by removing batch when doing a single update
* Remove wrapping of exceptions in transactions to make it easier to work with SQLExceptions

Changes in 0.0.5:

* Add prepare-statement function to ease creation of PreparedStatement with common options:
  - see docstring for details
* with-query-results now allows the SQL/params vector to be:
  - a PreparedStatement object, followed by any parameters the SQL needs
  - a SQL query string, followed by any parameters it needs
  - options (for prepareStatement), a SQL query string, followed by any parameters it needs
* Add support for databases that cannot return generated keys (e.g., HSQLDB)
  - insert operations silently return the insert counts instead of generated keys
  - it is the user's responsibility to handle this if you're using such a database!
 
Changes in 0.0.4:

* Fix JDBC-2 by allowing :table-spec {string} at the end of create-table arguments:
  (sql/create-table :foo [:col1 "int"] ["col2" :int] :table-spec "ENGINE=MyISAM")
* Fix JDBC-8 by removing all reflection warnings
* Fix JDBC-11 by no longer committing the transaction when an Error occurs
* Clean up as-... functions to reduce use of (binding)
* Refactor do-prepared*, separating out return keys logic and parameter setting logic
  - in preparation for exposing more hooks in PreparedStatement creation / manipulation

Changes in 0.0.3:

* Fix JDBC-10 by using .executeUpdate when generating keys (MS SQL Server, PostgreSQL compatibility issue)

Changes in 0.0.2:

* Fix JDBC-7 Clojure 1.2 compatibility (thanx to Aaron Bedra!)

Changes in 0.0.1 (compared to clojure.contrib.sql):

* Exposed print-... functions for exception printing; no longer writes exceptions to *out*
* Add clojure.java.jdbc/resultset-seq (to replace clojure.core/resultset-seq which should be deprecated)
* Add support for naming and quoting strategies - see http://clojure.github.com/java.jdbc/doc/clojure/java/jdbc/NameMapping.html
  - The formatting is a bit borked, Tom F knows about this and is working on an enhancement to auto-doc to improve it
* Add ability to return generated keys from single insert operations, add insert-record function
* Clojure 1.3 compatibility
