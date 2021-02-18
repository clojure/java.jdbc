Changes in 0.7.12

* Make the protocols `ISQLValue`, `ISQLParameter`, and `IResultSetReadColumn` extensible via metadata.

Changes in 0.7.11

* Address edge case in transaction rollback failure [JDBC-179](https://clojure.atlassian.net/browse/JDBC-179).

Changes in 0.7.10

* Use a US-locale `lower-case` function to avoid problems in certain locales (e.g., Turkish). A similar issue has been fixed recently in both HoneySQL and `next.jdbc`.
* Clean up `db-spec` options that are passed to the JDBC connection manager as properties [JDBC-178](https://clojure.atlassian.net/browse/JDBC-178).
* Relax restriction on `create-table-ddl` column specs to allow numbers (as well as keywords and strings) [JDBC-177](https://clojure.atlassian.net/browse/JDBC-177).

Changes in 0.7.9

* Fix behavior of multi-inserts when database does not support generated keys [JDBC-176](https://clojure.atlassian.net/browse/JDBC-176).
* Added _highly experimental_ support for `datafy`/`nav` (in `clojure.java.jdbc.datafy` namespace). This includes a convention-based approach to foreign keys with some assistance from a `:schema` option. This is subject to change and is provided mostly for informational purposes, as an example of the new functionality in Clojure 1.10. This includes a fix for the conventions from [JDBC-175](https://clojure.atlassian.net/browse/JDBC-175).
* Add note about rewriting batched operations to `insert-multi!` for some drivers [JDBC-174](https://clojure.atlassian.net/browse/JDBC-174).
* Support Oracle SID style URLs (`dbtype` can be `oracle:sid` which maps to `oracle:thin` and uses `:` as the separator before the `dbname` value) [JDBC-173](https://clojure.atlassian.net/browse/JDBC-173).

Changes in 0.7.8

* Support multiple JDBC driver class names (MySQL introduced a new driver class name with its 6.x connector) [JDBC-172](https://clojure.atlassian.net/browse/JDBC-172).
* Allow `with-db-connection` and `with-db-metadata` to nest [JDBC-171](https://clojure.atlassian.net/browse/JDBC-171).

Changes in 0.7.7

* Support `:as-arrays?`, `:result-set-fn`, and `:row-fn` in operations that return generated keys as a result set (`execute!`, `insert!`, and `insert-multi!`) [JDBC-169](https://clojure.atlassian.net/browse/JDBC-169).
* `get-connection` provides much better feedback if you accidentally call a function that expects a `db-spec` but pass a `java.sql.Connection` object instead (which is only required for `prepare-statement`).

Changes in 0.7.6

* `execute!` now supports `:return-keys` as a vector of column names, rather than just a simple Boolean value, for drivers that support that [JDBC-166](https://clojure.atlassian.net/browse/JDBC-166).
* Add built-in support for H2 in-memory database (`:dbtype "h2:mem"`).
* Add missing spec for `db-spec` being a `java.net.URI` object.
* Fix `add-connection` handling of string `db-spec` (becomes `:connection-uri`, not `:connection-string`).
* Fix specs for `with-db-*` functions, to support options in the binding form [JDBC-165](https://clojure.atlassian.net/browse/JDBC-165).
* Update tests so they work properly with string `db-spec` test databases.
* Ensure no reflection warnings are present.
* Switched local test infrastructure over to CLI and `deps.edn` (from Leiningen) as an example of multi-version testing without a "build tool".

Changes in 0.7.5

* Add support for `:return-keys` in `execute!` and `:multi?` in `db-do-prepared-return-keys` [JDBC-163](https://clojure.atlassian.net/browse/JDBC-163).

Changes in 0.7.4

* Improved discoverability of other `java.jdbc` documentation [JDBC-160](https://clojure.atlassian.net/browse/JDBC-160).
* Optional specs updated with `:keywordize?` and `:connection-uri` changes from 0.7.2 and 0.7.3 releases.
* Performance improvements, primarily in `query` and `reducible-query`.
* Experimental `:raw?` result set handling in `reducible-query`.
* `modify-connection` is more robust in the face of `null` connections and bad option values.

Changes in 0.7.3

* Added `:keywordize?` option alongside `:identifiers` that defaults to `true` but can be set to `false` to opt-out of converting identifiers to keywords (so column names etc will only be processed by the function passed as `:identifiers`) [JDBC-159](https://clojure.atlassian.net/browse/JDBC-159).
* If an exception occurs during a transaction, and then rollback fails with another exception, both exceptions will now be combined into an `ex-info`. Previously the rollback exception obscured the transaction exception [JDBC-158](https://clojure.atlassian.net/browse/JDBC-158).

Changes in 0.7.2

* `connection-uri` was incorrectly spec'd as a `java.net.URI` but should be `string?` [JDBC-156](https://clojure.atlassian.net/browse/JDBC-156).
* Allow for `:user` and `:password` to be passed with `:connection-uri`, so credentials can be omitted from the connection string.
* Clarified docstring for `get-connection` to show where `:user` and `:password` can be passed.

Changes in 0.7.1

* Connection strings with empty values were not parsed correctly [JDBC-155](https://clojure.atlassian.net/browse/JDBC-155).

Changes in 0.7.0

* `:conditional?` option for `create-table-ddl` and `drop-table-ddl` to provide for existence check (or a function to manipulate the generated DDL).
* Add better support for Oracle connections (default port to `1521`, support `:dbtype "oracle"` -- as `"oracle:thin"` -- and `:dbtype "oracle:oci"`, with `@` instead of `//` before host).

Changes in 0.7.0-beta5

* `get-connection` now accepts an `opts` map with `:auto-commit?` and `:read-only?` options. If present, the appropriate methods will be called on the connection obtained. These options are valid in any function call that may call `get-connection` under the hood. This should allow for streaming results in a query for most databases [JDBC-153](https://clojure.atlassian.net/browse/JDBC-153).
* Additional validation of options is performed in `prepared-statement` to avoid silently ignoring invalid combinations of `:concurrency`, `:cursors`, `:result-type`, and `:return-keys`.

Changes in 0.7.0-beta4

* `opts` are now correctly passed from `reducible-query` to `db-query-with-resultset`.
* Updated the `::query-options` spec to make it clear that `::prepare-options` are also acceptable there.

Changes in 0.7.0-beta3

* Reflection warnings removed in `reducible-result-set` [JDBC-152](https://clojure.atlassian.net/browse/JDBC-152).

Changes in 0.7.0-beta2

* Support for Clojure 1.6.0 and earlier has been dropped -- breaking change.
* Or, put another way, `clojure.java.jdbc` now requires Clojure 1.7 or later!
* All public functions now have specs in the optional `clojure.java.jdbc.spec` namespace (requires `clojure.spec.alpha`).
* `reducible-query` and `reducible-result-set` use `IReduce` and correctly support the no-`init` arity of `reduce` by using the first row of the `ResultSet`, if present, as the (missing) `init` value, and only calling `f` with no arguments if the `ResultSet` is empty. The `init` arity of `reduce` only ever calls `f` with two arguments.

Changes in 0.7.0-beta1

* Support for Clojure 1.4.0 has been dropped -- breaking change.
* Optional spec support now uses `clojure.spec.alpha`.
* `reducible-query` accepts a `db-spec` and a SQL/parameters vector and returns a reducible (`IReduce` on Clojure 1.7 or later; `CollReduce` on Clojure 1.5/1.6): when reduced, it runs the query, obtains a reducible result set, and then reduces that. A reducible query will run the query each time it is reduced. The helper function `reducible-result-set` is public: it accepts a `ResultSet` and produces a reducible that offers a single pass reduce over the rows. Both functions honor `reduced` values to short-circuit the process [JDBC-99](https://clojure.atlassian.net/browse/JDBC-99).

Changes in 0.7.0-alpha3

* `classname` is now accepted with `dbtype` / `dbname` so you can easily specify a JDBC driver class name for a database type that is not known [JDBC-151](https://clojure.atlassian.net/browse/JDBC-151).
* `redshift` has been added as a `dbtype` with `com.amazon.redshift.jdbc.Driver` as the driver name.

Changes in 0.7.0-alpha2

* `pgsql` and the Impossibl PostgresSQL 'NG' driver are now supported (note that `:max-rows` does not work with this driver!); also, providing unknown `dbtype` or `subprotocol` in a `db-spec` should now throw a better exception [JDBC-150](https://clojure.atlassian.net/browse/JDBC-150).
* `quoted` now accepts keywords for database / dialect (`:ansi` (including PostgresSQL), `:mysql`, `:oracle`, `:sqlserver` -- these match the keywords used in HoneySQL which is the recommended third party SQL DSL for java.jdbc) [JDBC-149](https://clojure.atlassian.net/browse/JDBC-149).
* Reorder `get-connection` clauses to make it easier to combine keys in a `db-spec` [JDBC-148](https://clojure.atlassian.net/browse/JDBC-148).
* Force load `DriverManager` before `classForName` call on drivers to avoid potential race condition on initialization [JDBC-145](https://clojure.atlassian.net/browse/JDBC-145).

Changes in 0.7.0-alpha1 -- potentially breaking changes

* The signatures of `as-sql-name` and `quoted` have changed slightly: the former no longer has the curried (single argument) version, and the latter no longer has the two argument version. This change came out of a discussion on Slack which indicated curried functions are non-idiomatic. If you relied on the curried version of `as-sql-name`, you will not need to use `partial`. If you relied on the two argument version of `quoted`, you will need to add an extra `( )` for the one argument call. I'd be fairly surprised if anyone is using `as-sql-name` at all since it is really an implementation detail. I'd also be surprised if anyone was using the two argument version of `quoted` since the natural usage is `:entities (quoted [\[ \]])` to create a naming strategy (that provides SQL entity quoting).
* Clarified that `insert-multi!` with a sequence of row maps may be substantially slower than with a sequence of row value vectors (the former performs an insert for each row, the latter performs a single insert for all the data together) [JDBC-147](https://clojure.atlassian.net/browse/JDBC-147).
* All options are passed through all function calls, expanding the range of options you can pass into high-level functions such as `insert!` and `update!` [JDBC-144](https://clojure.atlassian.net/browse/JDBC-144).
* Added `get-isolation-level` to return the current transaction's isolation level, if any [JDBC-141](https://clojure.atlassian.net/browse/JDBC-141).
* Added support for `read-columns` option to allow more flexible customization of reading column values from a result set (particularly in a multi-database application). Also expands `set-parameters` support to options (previously it was just part of the db-spec) [JDBC-137](https://clojure.atlassian.net/browse/JDBC-137).
* Expanded optional `clojure.spec` coverage to almost the whole library API.

Changes in 0.6.2-alpha3

* Fixed bad interaction between `:qualifier` and existing `:identifiers` functionality [JDBC-140](https://clojure.atlassian.net/browse/JDBC-140).
* Updated the README and docstrings to reflect that `:dbtype` is the easiest / preferred way to write `db-spec` maps [JDBC-139](https://clojure.atlassian.net/browse/JDBC-139).
* Fixed postgres / postgresql alias support [JDBC-138](https://clojure.atlassian.net/browse/JDBC-138).
  This also adds aliases for mssql (sqlserver), jtds (jtds:sqlserver), oracle (oracle:thin), and hsql (hsqldb).

Changes in 0.6.2-alpha2

* Updates to `clojure.spec` support to work properly with Clojure 1.9.0 Alpha 10.

Changes in 0.6.2-alpha1

* Experimental support for `clojure.spec` via the new `clojure.java.jdbc.spec` namespace. Requires Clojure 1.9.0 Alpha 8 (or later).
* All options to all functions can now have defaults within the `db-spec` itself [JDBC-136](https://clojure.atlassian.net/browse/JDBC-136).
* `query` (and by extension `find-by-keys` and `get-by-id`) now support `:explain?` and `:explain-fn` options to help support basic performance analysis [JDBC-135](https://clojure.atlassian.net/browse/JDBC-135).
* `insert!` and `insert-multi!` now respect `:identifiers` and `:qualifier` because inserting rows on PostgreSQL returns full rows, not just the newly inserted keys [JDBC-134](https://clojure.atlassian.net/browse/JDBC-134).
* In addition to the `:identifiers` option, you can now use `:qualifier` to specify a namespace qualifier (string) to be used when constructing keywords from SQL column names [JDBC-133](https://clojure.atlassian.net/browse/JDBC-133).

Changes in 0.6.1

* `insert!` and `insert-multi!` now default `:transaction?` to `true` (as they should have done in 0.6.0!) [JDBC-128](https://clojure.atlassian.net/browse/JDBC-128). These two functions also have improved docstrings to clarify the difference in behavior between inserting rows as maps compared to inserting rows as a series of column values.
* PostgreSQL support has been improved: java.jdbc is now tested against PostgreSQL locally (as well as SQLite, Apache Derby, HSQLDB, H2, MySQL, MS SQL Server (both MS Type 4 driver and jTDS driver). [JDBC-127](https://clojure.atlassian.net/browse/JDBC-127) and [JDBC-129](https://clojure.atlassian.net/browse/JDBC-129).

Changes in 0.6.0

* `find-by-keys` now correctly handles `nil` values [JDBC-126](https://clojure.atlassian.net/browse/JDBC-126).
* `find-by-keys` calls `seq` on `:order-by` to treat `[]` as no `ORDER BY` clause.

Changes in 0.6.0-rc2

* `db-query-with-resultset` now accepts an options map and passes it to `prepare-statement` [JDBC-125](https://clojure.atlassian.net/browse/JDBC-125).
  - Passing the `prepare-statement` options map as the first element of the `[sql & params]` vector is no longer supported and will throw an `IllegalArgumentException`. It was always very poorly documented and almost never used, as far as I can tell.
* `db-query-with-resultset` no longer requires the `sql-params` argument to be a vector: a sequence is acceptable. This is in line with other functions that accept a sequence.
* `db-query-with-resultset` now accepts a bare SQL string or `PreparedStatement` as the `sql-params` argument, when there are no parameters needed. This is in line with other functions that accept SQL or a `PreparedStatement`.
* `query`'s options map now is passed to `db-query-with-resultset` and thus can contain options to be used to construct the `PreparedStatement` [JDBC-125](https://clojure.atlassian.net/browse/JDBC-125).
* `find-by-keys` now accepts an `:order-by` option that specifies a sequence of orderings; an ordering is either a column (to sort ascending) or a map from column name to direct (`:asc` or `:desc`).

Changes in 0.6.0-rc1

* Adds `get-by-id` and `find-by-keys` convenience functions (these were easy to add after the API changes in 0.6.0 and we rely very heavily on them at World Singles so putting them in the core for everyone seemed reasonable).
* REMINDER: ALL DEPRECATED FUNCTIONALITY HAS BEEN REMOVED! [JDBC-118](https://clojure.atlassian.net/browse/JDBC-118).
  - See alpha2 / alpha1 below for more details.

Changes in 0.6.0-alpha2

* ALL DEPRECATED FUNCTIONALITY HAS BEEN REMOVED! [JDBC-118](https://clojure.atlassian.net/browse/JDBC-118).
  - This removes deprecated functionality from db-do-commands and `db-do-prepared*` which should have been removed in Alpha 1.
* Ensures SQL / params are actually vectors prior to destructuring (this addresses an interop edge case from other languages) [JDBC-124](https://clojure.atlassian.net/browse/JDBC-124).
* Fix typo in `insert-multi!` argument validation exception [JDBC-123](https://clojure.atlassian.net/browse/JDBC-123).

Changes in 0.6.0-alpha1

* (ALMOST) ALL DEPRECATED FUNCTIONALITY HAS BEEN REMOVED! [JDBC-118](https://clojure.atlassian.net/browse/JDBC-118).
  - See changes described in versions 0.5.5 through 0.5.8 for what was deprecated
  - Use version 0.5.8 as a bridge to identify any deprecated API calls on which your code relies!
  - `db-transaction` (deprecated in version 0.3.0) has been removed
  - The `java.jdbc.deprecated` namespace has been removed

Changes in 0.5.8

* `db-do-commands` now expects multiple commands to be be wrapped in a vector [JDBC-122](https://clojure.atlassian.net/browse/JDBC-123). The single command form is unchanged (but may be wrapped in a vector). Calling `db-do-commands` with multiple commands (not wrapped in a single vector) will produce a "DEPRECATED" warning printed to the console.
* `db-do-prepared` and `db-do-prepared-return-keys` now expect to receive a `db-spec`, an optional `transaction?` boolean, a `sql-params` argument, and an optional options map. `sql-params` is a vector containing a SQL string or `PreparedStatement` followed by parameters -- like other APIs in this library. In addition, like the `:multi? true` version of `execute!`, `db-do-prepared` can accept a vector that has parameter groups: multiple vectors containing groups of parameter values [JDBC-122](https://clojure.atlassian.net/browse/JDBC-123). Calling `db-do-prepared` with unrolled arguments -- the SQL string / statement followed by parameter groups -- is deprecated and will produce "DEPRECATED" warnings printed to the console.

Changes in 0.5.7

* `(insert! db table [:col] ["val"] {})` syntax, introduced in 0.5.6, threw an exception [JDBC-121](https://clojure.atlassian.net/browse/JDBC-121).

Changes in 0.5.6

* `create-table-ddl` now expects the column specs to be wrapped in a single vector and no longer needs the `:options` delimiter to specify the options map [JDBC-120](https://clojure.atlassian.net/browse/JDBC-120). If column specs are not wrapped in a vector, you will get a "DEPRECATED" warning printed to the console.
* `insert!` now supports only single row insertion; multi-row insertion is deprecated. `insert-multi!` has been added for multi-row insertion. `:options` is no longer needed as a delimiter for the options map [JDBC-119](https://clojure.atlassian.net/browse/JDBC-119). If `insert!` is called with multiple rows, or `:options` is specified, you will get a "DEPRECATED" warning printed to the console.
* NOTE: all deprecated functionality will go away in version 0.6.0!

Changes in 0.5.5

* Allow options map in all calls that previously took optional keyword arguments [JDBC-117](https://clojure.atlassian.net/browse/JDBC-117). The unrolled keyword argument forms of call are deprecated -- and print a "DEPRECATED" message to the console! -- and will go away in 0.6.0.

Changes in 0.5.0

* Allow PreparedStatement in db-do-prepared-return-keys [JDBC-115](https://clojure.atlassian.net/browse/JDBC-115).
* Remove exception wrapping [JDBC-114](https://clojure.atlassian.net/browse/JDBC-114).
* Drop Clojure 1.3 compatibility.

Changes in 0.4.2

* Remove redundant type hints [JDBC-113](https://clojure.atlassian.net/browse/JDBC-113) - Michael Blume.
* Avoid reflection on `.prepareStatement` [JDBC-112](https://clojure.atlassian.net/browse/JDBC-112) - Michael Blume.
* Add `metadata-query` macro to make metadata query / results easier to work with for [JDBC-107](https://clojure.atlassian.net/browse/JDBC-107).
* `prepare-statement` `:return-keys` may now be a vector of (auto-generated) column names to return, in addition to just being truthy or falsey. This allows keys to be returned for more databases. [JDBC-104](https://clojure.atlassian.net/browse/JDBC-104).
* Officially support H2 (and test against it) to support [JDBC-91](https://clojure.atlassian.net/browse/JDBC-91) and clarify docstrings to improve debugging driver-specific restrictions on SQL.

Changes in 0.4.0 / 0.4.1

* `db-do-prepared` now allows `transaction?` to be omitted when a `PreparedStatement` is passed as the second argument [JDBC-111](https://clojure.atlassian.net/browse/JDBC-111) - Stefan Kamphausen.
* Nested transaction checks isolation level is the same [JDBC-110](https://clojure.atlassian.net/browse/JDBC-110) - Donald Ball.
* Default PostgreSQL port; Support more dbtype/dbname variants [JDBC-109](https://clojure.atlassian.net/browse/JDBC-109).
* Drop Clojure 1.2 compatibility.

Changes in 0.3.7

* Bump all driver versions in `project.clj` and re-test.
* Remove duplicate `count` calls in `insert-sql` [JDBC-108](https://clojure.atlassian.net/browse/JDBC-108) - Earl St Sauver.
* Remove driver versions from README and link to Maven Central [JDBC-106](https://clojure.atlassian.net/browse/JDBC-106).
* Fix links in CHANGES and README [JDBC-103](https://clojure.atlassian.net/browse/JDBC-103) - John Walker.

Changes in 0.3.6

* Arbitrary values allowed for `:cursors`, `:concurrency`, `:result-type` arguments to `prepare-statement` [JDBC-102](https://clojure.atlassian.net/browse/JDBC-102).
* Allow `:as-arrays? :cols-as-is` to omit column name uniqueness when returning result sets as arrrays [JDBC-101](https://clojure.atlassian.net/browse/JDBC-101).
* Add `:timeout` argument to `prepare-statement` [JDBC-100](https://clojure.atlassian.net/browse/JDBC-100).

Changes in 0.3.5

* Reflection warnings on executeUpdate addressed.
* HSQLDB and SQLite in-memory strings are now accepted [JDBC-94](https://clojure.atlassian.net/browse/JDBC-94).
* Add support for readonly transactions via :read-only? [JDBC-93](https://clojure.atlassian.net/browse/JDBC-93).

Changes in 0.3.4

* execute! can now accept a PreparedStatement [JDBC-96](https://clojure.atlassian.net/browse/JDBC-96).
* Support simpler db-spec with :dbtype and :dbname (and optional :host and :port etc) [JDBC-92](https://clojure.atlassian.net/browse/JDBC-92).
* Support oracle:oci and oracle:thin subprotocols [JDBC-90](https://clojure.atlassian.net/browse/JDBC-90).

Changes in 0.3.3

* Prevent exception/crash when query called with bare SQL string [JDBC-89](https://clojure.atlassian.net/browse/JDBC-89).
* Add :row-fn and :result-set-fn to metadata-result function [JDBC-87](https://clojure.atlassian.net/browse/JDBC-87).
* Support key/value configuration from URI (Phil Hagelberg).

Changes in 0.3.2

* Add nil protocol implementation to ISQLParameter.

Changes in 0.3.1 (broken)

* Improve docstrings and add :arglists for better auto-generated documentation.
* Make insert-sql private - technically a breaking change but it should never have been public: sorry folks!
* Provide better protocol for setting parameters in prepared statements [JDBC-86](https://clojure.atlassian.net/browse/JDBC-86).
* Fix parens in two deprecated tests [JDBC-85](https://clojure.atlassian.net/browse/JDBC-85).
* Made create-table-ddl less aggressive about applying as-sql-name so only first name in a column spec is affected.

Changes in 0.3.0

* Ensure canonical Boolean to workaround strange behavior in some JDBC drivers [JDBC-84](https://clojure.atlassian.net/browse/JDBC-84).
* Rename recently introduced test to ensure unique names [JDBC-83](https://clojure.atlassian.net/browse/JDBC-83).
* Rename unused arguments in protocol implementation to support Android [JDBC-82](https://clojure.atlassian.net/browse/JDBC-82).
* Correctly handle empty param group sequence in execute! (which only seemed to affect SQLite) [JDBC-65](https://clojure.atlassian.net/browse/JDBC-65).

Changes in 0.3.0-rc1

* Deprecate db-transaction (new in 0.3.0) in favor of with-db-transaction [JDBC-81](https://clojure.atlassian.net/browse/JDBC-81).
* Add with-db-metadata macro and metadata-result function to make it easier to work with SQL metadata [JDBC-80](https://clojure.atlassian.net/browse/JDBC-80).
* Add with-db-connection macro to make it easier to run groups of operations against a single open connection [JDBC-79](https://clojure.atlassian.net/browse/JDBC-79).
* Add ISQLValue protocol to make it easier to support custom SQL types for parameters in SQL statements [JDBC-77](https://clojure.atlassian.net/browse/JDBC-77).
* Add support for :isolation in with-db-transaction [JDBC-75](https://clojure.atlassian.net/browse/JDBC-75).
* Add :user as an alias for :username for DataSource connections [JDBC-74](https://clojure.atlassian.net/browse/JDBC-74).

Changes in 0.3.0-beta2

* The DSL namespaces introduced in 0.3.0-alpha1 have been retired - see [java-jdbc/dsl](https://github.com/seancorfield/jsql) for a migration path if you wish to continue using the DSL (although it is recommended you switch to another, more expressive DSL).
* The older API (0.2.3) which was deprecated in earlier 0.3.0 builds has moved to `clojure.java.jdbc.deprecated` to help streamline the API for 0.3.0 and clean up the documentation.

Changes in 0.3.0-beta1

* query as-arrays? now allows you to leverage lazy result fetching [JDBC-72](https://clojure.atlassian.net/browse/JDBC-72).
* "h2" is recognized as a protocol shorthand for org.h2.Driver
* Tests no longer use :1 literal [JDBC-71](https://clojure.atlassian.net/browse/JDBC-71).
* Conditional use of javax.naming.InitialContext so it can be compiled on Android [JDBC-69](https://clojure.atlassian.net/browse/JDBC-69).
* New db-query-with-resultset function replaces private `db-with-query-results*` and processes a raw ResultSet object [JDBC-63](https://clojure.atlassian.net/browse/JDBC-63).
* Allow :set-parameters in db-spec to override set-parameters internal function to allow per-DB special handling of SQL parameters values (such as null for Teradata) [JDBC-40](https://clojure.atlassian.net/browse/JDBC-40).

Changes in 0.3.0-alpha5

* DDL now supports entities naming strategy [JDBC-53](https://clojure.atlassian.net/browse/JDBC-53).
* Attempt to address potential memory leaks due to closures - see [Christophe Grand's blog post on Macros, closures and unexpected object retention](http://clj-me.cgrand.net/2013/09/11/macros-closures-and-unexpected-object-retention/).
* Documentation has moved to [Using java.jdbc on Clojure-Doc.org](http://clojure-doc.org/articles/ecosystem/java_jdbc/home.html)
* Added Leiningen support for easier development/testing (Maven is still the primary build tool).
* Added create-index / drop-index DDL [JDBC-62](https://clojure.atlassian.net/browse/JDBC-62) - moquist
* Make transaction? boolean optional in various `db-do-*` functions
* Create clojure.java.jdbc.ddl namespace
* Add create-table, drop-table, create-index and drop-index
* Deprecate create-table, create-table-ddl and drop-table in main namespace
* Update README to clarify PostgreSQL instructions.
* Fix test suite for PostgreSQL [JDBC-59](https://clojure.atlassian.net/browser/JDBC-59)
* Improve hooks for Oracle data type handling [JDBC-57](https://clojure.atlassian.net/browser/JDBC-57)
* Fix reflection warnings [JDBC-55](https://clojure.atlassian.net/browser/JDBC-55)

* DDL now supports entities naming strategy [JDBC-53](https://clojure.atlassian.net/browse/JDBC-53).
* Attempt to address potential memory leaks due to closures - see [Christophe Grand's blog post on Macros, closures and unexpected object retention](http://clj-me.cgrand.net/2013/09/11/macros-closures-and-unexpected-object-retention/).
* Documentation has moved to [Using java.jdbc on Clojure-Doc.org](http://clojure-doc.org/articles/ecosystem/java_jdbc/home.html)
* Added Leiningen support for easier development/testing (Maven is still the primary build tool).
* Added create-index / drop-index DDL [JDBC-62](https://clojure.atlassian.net/browse/JDBC-62) - moquist
* Make transaction? boolean optional in various `db-do-*` functions
  * It will ultimately change to a function argument I think when [JDBC-37](https://clojure.atlassian.net/browser/JDBC-37) is dealt with
* Create clojure.java.jdbc.ddl namespace
  * Add create-table and drop-table
  * Deprecate create-table, create-table-ddl and drop-table in main namespace
  * More DDL is coming soon
* Update README to clarify PostgreSQL instructions.
* Fix test suite for PostgreSQL [JDBC-59](https://clojure.atlassian.net/browser/JDBC-59)
* Improve hooks for Oracle data type handling [JDBC-57](https://clojure.atlassian.net/browser/JDBC-57)
* Fix reflection warnings [JDBC-55](https://clojure.atlassian.net/browser/JDBC-55)

Changes in 0.3.0-alpha4

* Fix connection leaks [JDBC-54](https://clojure.atlassian.net/browser/JDBC-54)
* Allow order-by to accept empty sequence (and return empty string)

Changes in 0.3.0-alpha3

* Fix macro / import interaction by fully qualifying Connection type.

Changes in 0.3.0-alpha2

* Address [JDBC-51](https://clojure.atlassian.net/browse/JDBC-51) by declaring get-connection returns java.sql.Connection
* Add IResultSetReadColumn protocol extension point for custom read conversions [JDBC-46](https://clojure.atlassian.net/browse/JDBC-46)
* Add :multi? to execute! so it can be used for repeated operations [JDBC-52](https://clojure.atlassian.net/browse/JDBC-52)
* Reverted specialized handling of NULL values (reopens [JDBC-40](https://clojure.atlassian.net/browse/JDBC-40))
* Rename :as-arrays to :as-arrays? since it is boolean
* Add curried version of clojure.java.jdbc.sql/as-quoted-str
* Officially deprecate resultset-seq

Changes in 0.3.0-alpha1

Major overhaul of the API and deprecation of most of the old API!

* Add insert!, query, update!, delete! and execute! high-level API
  [JDBC-20](https://clojure.atlassian.net/browse/JDBC-20)
* Add optional SQL-generating DSL in clojure.java.jdbc.sql (implied by JDBC-20)
* Add db- prefixed versions of low-level API
* Add db-transaction macro:

```
  (db-transaction [t-con db-spec]
    (query t-con (select * :user (where {:id 42}))))
```

* Add result-set-seq as replacement for resultset-seq (which will be deprecated)
* Transaction now correctly rollback on non-Exception Throwables
  [JDBC-43](https://clojure.atlassian.net/browse/JDBC-43)
* Rewrite old API functions in terms of new API, and deprecate old API
  [JDBC-43](https://clojure.atlassian.net/browse/JDBC-43)
* Add :as-arrays to query / result-set-seq
  [JDBC-41](https://clojure.atlassian.net/browse/JDBC-41)
* Better handling of NULL values [JDBC-40](https://clojure.atlassian.net/browse/JDBC-40)
  and [JDBC-18](https://clojure.atlassian.net/browse/JDBC-18)
  Note: JDBC-40 has been reverted in 0.3.0-alpha2 because it introduced regressions for PostgreSQL
* db-do-commands allows you to execute SQL without a transaction wrapping it
  [JDBC-38](https://clojure.atlassian.net/browse/JDBC-38)
* Remove reflection warning from execute-batch
* Add notes to README about 3rd party database driver dependencies
* Add optional :identifiers argument to resultset-seq so you can explicitly pass in the naming strategy

Changes in 0.2.3:

* as-str now treats a.b as two identifiers separated by . so quoting produces [a].[b] instead of [a.b]
* Add :connection-uri option [JDBC-34](https://clojure.atlassian.net/browse/JDBC-34)

Changes in 0.2.2:

* Handle Oracle unknown row count affected [JDBC-33](https://clojure.atlassian.net/browse/JDBC-33)
* Handle jdbc: prefix in string db-specs [JDBC-32](https://clojure.atlassian.net/browse/JDBC-32)
* Handle empty columns in make column unique (Juergen Hoetzel) [JDBC-31](https://clojure.atlassian.net/browse/JDBC-31)

Changes in 0.2.1:

* Result set performance enhancement (Juergen Hoetzel) [JDBC-29](https://clojure.atlassian.net/browse/JDBC-29)
* Make do-prepared-return-keys (for Korma team) [JDBC-30](https://clojure.atlassian.net/browse/JDBC-30)

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
* Refactor `do-prepared*`, separating out return keys logic and parameter setting logic
  - in preparation for exposing more hooks in PreparedStatement creation / manipulation

Changes in 0.0.3:

* Fix JDBC-10 by using .executeUpdate when generating keys (MS SQL Server, PostgreSQL compatibility issue)

Changes in 0.0.2:

* Fix JDBC-7 Clojure 1.2 compatibility (thanx to Aaron Bedra!)

Changes in 0.0.1 (compared to clojure.contrib.sql):

* Exposed print-... functions for exception printing; no longer writes exceptions to `*out*`
* Add clojure.java.jdbc/resultset-seq (to replace clojure.core/resultset-seq which should be deprecated)
* Add support for naming and quoting strategies - see https://clojure.github.io/java.jdbc/doc/clojure/java/jdbc/NameMapping.html
  - The formatting is a bit borked, Tom F knows about this and is working on an enhancement to auto-doc to improve it
* Add ability to return generated keys from single insert operations, add insert-record function
* Clojure 1.3 compatibility
