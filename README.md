clojure.java.jdbc
========================================

A low-level Clojure wrapper for JDBC-based access to databases.

For DSLs that are compatible with this library, consider:

* [HoneySQL](https://github.com/jkk/honeysql)
* [SQLingvo](https://github.com/r0man/sqlingvo)
* [Korma](http://sqlkorma.com)

Formerly known as `clojure.contrib.sql`.

Additional documentation can be found in the [java.jdbc section of clojure-doc.org](http://clojure-doc.org/articles/ecosystem/java_jdbc/home.html) and there is a dedicated [java.jdbc mailing list](https://groups.google.com/forum/#!forum/clojure-java-jdbc)

Releases and Dependency Information
========================================

Latest stable release: 0.3.6

* [All Released Versions](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.clojure%22%20AND%20a%3A%22java.jdbc%22)

* [Development Snapshot Versions](https://oss.sonatype.org/index.html#nexus-search;gav~org.clojure~java.jdbc~~~)

[Leiningen](https://github.com/technomancy/leiningen) dependency information:
```clojure
[org.clojure/java.jdbc "0.3.6"]
```
[Maven](http://maven.apache.org/) dependency information:
```xml
<dependency>
  <groupId>org.clojure</groupId>
  <artifactId>java.jdbc</artifactId>
  <version>0.3.6</version>
</dependency>
```
You will also need to add dependencies for the JDBC driver you intend to use. Here are links (to Maven Central) for each of the common database drivers that clojure.java.jdbc is known to be used with:

* [Apache Derby](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.apache.derby%22%20AND%20a%3A%22derby%22)
* [HSQLDB](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22hsqldb%22%20AND%20a%3A%22hsqldb%22)
* [Microsoft SQL Server jTDS](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22net.sourceforge.jtds%22%20AND%20a%3A%22jtds%22)
* [MySQL](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22mysql%22%20AND%20a%3A%22mysql-connector-java%22)
* [PostgreSQL](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.postgresql%22%20AND%20a%3A%22postgresql%22)
* [SQLite](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.xerial%22%20AND%20a%3A%22sqlite-jdbc%22)

clojure.java.jdbc is also tested against Microsoft's own JDBC4 Driver 4.0 but that
has to be [downloaded manually](https://www.microsoft.com/en-us/download/details.aspx?id=11774) and placed in a Maven repository accessible to your system. For testing, it was installed locally as:
```clojure
;; Microsoft SQL Server JDBC4 Driver 4.0
[sqljdbc4/sqljdbc4 "4.0"]
```

Example Usage
========================================
```clojure
(require '[clojure.java.jdbc :as j])

(def mysql-db {:subprotocol "mysql"
               :subname "//127.0.0.1:3306/clojure_test"
               :user "clojure_test"
               :password "clojure_test"})

(j/insert! mysql-db :fruit
  {:name "Apple" :appearance "rosy" :cost 24}
  {:name "Orange" :appearance "round" :cost 49})
;; ({:generated_key 1} {:generated_key 2})

(j/query mysql-db
  ["select * from fruit where appearance = ?" "rosy"]
  :row-fn :cost)
;; (24)
```
For more detail see the [generated documentation on github](http://clojure.github.com/java.jdbc/).

Developer Information
========================================

* [GitHub project](https://github.com/clojure/java.jdbc)

* [Bug Tracker](http://dev.clojure.org/jira/browse/JDBC)

* [Continuous Integration](http://build.clojure.org/job/java.jdbc/)

* [Compatibility Test Matrix](http://build.clojure.org/job/java.jdbc-test-matrix/)

* Testing:
  * Currently by default tests run only against Derby and HSQLDB, the in-process databases.

* To test against PostgreSQL, first create the user and database:

        $ sudo -u postgres createuser clojure_test -P clojure_test
        $ sudo -u postgres createdb clojure_test -O clojure_test

* Or similarly with MySQL:

        $ mysql -u root
        mysql> create database clojure_test;
        mysql> grant all on clojure_test.* to clojure_test identified by "clojure_test";

* Then run the tests with the <tt>TEST_DBS</tt> environment variable:

        $ TEST_DBS=mysql,postgres mvn test

Change Log
====================

* Release 0.3.6 on 2014-10-28
  * Arbitrary values allowed for `:cursors`, `:concurrency`, `:result-type` arguments to `prepare-statement` [JDBC-102](http://dev.clojure.org/jira/browse/JDBC-102).
  * Allow `:as-arrays? :cols-as-is` to omit column name uniqueness when returning result sets as arrrays [JDBC-101](http://dev.clojure.org/jira/browse/JDBC-101).
  * Add `:timeout` argument to `prepare-statement` [JDBC-100](http://dev.clojure.org/jira/browse/JDBC-100).

* Release 0.3.5 on 2014-08-01
  * Reflection warnings on executeUpdate addressed.
  * HSQLDB and SQLite in-memory strings are now accepted [JDBC-94](http://dev.clojure.org/jira/browse/JDBC-94).
  * Add support for readonly transactions via :read-only? [JDBC-93](http://dev.clojure.org/jira/browse/JDBC-93).

* Release 0.3.4 on 2014-06-30
  * execute! can now accept a PreparedStatement [JDBC-96](http://dev.clojure.org/jira/browse/JDBC-96).
  * Support simpler db-spec with :dbtype and :dbname (and optional :host and :port etc) [JDBC-92](http://dev.clojure.org/jire/browse/JDBC-92).
  * Support oracle:oci and oracle:thin subprotocols [JDBC-90](http://dev.clojure.org/jira/browse/JDBC-90).

* Release 0.3.3 on 2014-01-30
  * Prevent exception/crash when query called with bare SQL string [JDBC-89](http://dev.clojure.org/jira/browse/JDBC-89).
  * Add :row-fn and :result-set-fn to metadata-result function [JDBC-87](http://dev.clojure.org/jira/browse/JDBC-87).
  * Support key/value configuration from URI (Phil Hagelberg).

* Release 0.3.2 on 2013-12-30
  * Add nil protocol implementation to ISQLParameter

* Release 0.3.1 on 2013-12-29 (broken; use 0.3.2 instead)
  * Improve docstrings and add :arglists for better auto-generated documentation.
  * Make insert-sql private - technically a breaking change but it should never have been public: sorry folks!
  * Provide better protocol for setting parameters in prepared statements [JDBC-86](http://dev.clojure.org/jira/browse/JDBC-86).
  * Fix parens in two deprecated tests [JDBC-85](http://dev.clojure.org/jira/browse/JDBC-85).
  * Made create-table-ddl less aggressive about applying as-sql-name so only first name in a column spec is affected.

* Release 0.3.0 on 2013-12-16
  * Ensure canonical Boolean to workaround strange behavior in some JDBC drivers [JDBC-84](http://dev.clojure.org/jira/browse/JDBC-84).
  * Rename recently introduced test to ensure unique names [JDBC-83](http://dev.clojure.org/jira/browse/JDBC-83).
  * Rename unused arguments in protocol implementation to support Android [JDBC-82](http://dev.clojure.org/jira/browse/JDBC-82).
  * Correctly handle empty param group sequence in execute! (which only seemed to affect SQLite) [JDBC-65](http://dev.clojure.org/jira/browse/JDBC-65).

* Release 0.3.0-rc1 on 2013-12-12
  * Deprecate db-transaction (new in 0.3.0) in favor of with-db-transaction [JDBC-81](http://dev.clojure.org/jira/browse/JDBC-81).
  * Add with-db-metadata macro and metadata-result function to make it easier to work with SQL metadata [JDBC-80](http://dev.clojure.org/jira/browse/JDBC-80).
  * Add with-db-connection macro to make it easier to run groups of operations against a single open connection [JDBC-79](http://dev.clojure.org/jira/browse/JDBC-79).
  * Add ISQLValue protocol to make it easier to support custom SQL types for parameters in SQL statements [JDBC-77](http://dev.clojure.org/jira/browse/JDBC-77).
  * Add support for :isolation in with-db-transaction [JDBC-75](http://dev.clojure.org/jira/browse/JDBC-75).
  * Add :user as an alias for :username for DataSource connections [JDBC-74](http://dev.clojure.org/jira/browse/JDBC-74).

* Release 0.3.0-beta2 on 2013-11-24
  * **BREAKING CHANGES!**
  * The DSL namespaces introduced in 0.3.0-alpha1 have been retired - see [java-jdbc/dsl](https://github.com/seancorfield/jsql) for a migration path if you wish to continue using the DSL (although it is recommended you switch to another, more expressive DSL).
  * The older API (0.2.3) which was deprecated in earlier 0.3.0 builds has moved to `clojure.java.jdbc.deprecated` to help streamline the API for 0.3.0 and clean up the documentation.

* Release 0.3.0-beta1 on 2013-11-03
  * query as-arrays? now allows you to leverage lazy result fetching [JDBC-72](http://dev.clojure.org/jira/browse/JDBC-72).
  * "h2" is recognized as a protocol shorthand for org.h2.Driver
  * Tests no longer use :1 literal [JDBC-71](http://dev.clojure.org/jira/browse/JDBC-71).
  * Conditional use of javax.naming.InitialContext so it can be compiled on Android [JDBC-69](http://dev.clojure.org/jira/browse/JDBC-69).
  * New db-query-with-resultset function replaces private db-with-query-results* and processes a raw ResultSet object [JDBC-63](http://dev.clojure.org/jira/browse/JDBC-63).
  * Allow :set-parameters in db-spec to override set-parameters internal function to allow per-DB special handling of SQL parameters values (such as null for Teradata) [JDBC-40](http://dev.clojure.org/jira/browse/JDBC-40).

* Release 0.3.0-alpha5 on 2013-09-15
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

* Release 0.3.0-alpha4 on 2013-05-11
  * Fix connection leaks [JDBC-54](http://dev.clojure.org/jira/browser/JDBC-54)
  * Allow order-by to accept empty sequence (and return empty string)

* Release 0.3.0-alpha3 on 2013-05-04
  * Fix macro / import interaction by fully qualifying Connection type.

* Release 0.3.0-alpha2 on 2013-05-03
  * Address [JDBC-51](http://dev.clojure.org/jira/browse/JDBC-51) by declaring get-connection returns java.sql.Connection
  * Add IResultSetReadColumn protocol extension point for custom read conversions [JDBC-46](http://dev.clojure.org/jira/browse/JDBC-46)
  * Add :multi? to execute! so it can be used for repeated operations [JDBC-52](http://dev.clojure.org/jira/browse/JDBC-52)
  * Reverted specialized handling of NULL values (reopens [JDBC-40](http://dev.clojure.org/jira/browse/JDBC-40))
  * Rename :as-arrays to :as-arrays? since it is boolean
  * Add curried version of clojure.java.jdbc.sql/as-quoted-str
  * Officially deprecate resultset-seq

* Release 0.3.0-alpha1 on 2013-04-07
  * MAJOR API OVERHAUL!
  * Most of the old 0.2.x API has been deprecated and a new, more idiomatic API introduced, along with a minimal DSL to generate basic SQL
  * Specifics:
  * Add insert!, query, update!, delete! and execute! high-level API [JDBC-20](http://dev.clojure.org/jira/browse/JDBC-20)
  * Add optional SQL-generating DSL in clojure.java.jdbc.sql (implied by JDBC-20)
  * Add db- prefixed versions of low-level API
  * Add db-transaction macro
  * Add result-set-seq as replacement for resultset-seq (which will be deprecated)
  * Transaction now correctly rollback on non-Exception Throwables [JDBC-43](http://dev.clojure.org/jira/browse/JDBC-43)
  * Rewrite old API functions in terms of new API, and deprecate old API [JDBC-43](http://dev.clojure.org/jira/browse/JDBC-43)
  * Add :as-arrays to query / result-set-seq [JDBC-41](http://dev.clojure.org/jira/browse/JDBC-41)
  * Better handling of NULL values [JDBC-40](http://dev.clojure.org/jira/browse/JDBC-40) and [JDBC-18](http://dev.clojure.org/jira/browse/JDBC-18)
    Note: JDBC-40 is being reverted in 0.3.0-alpha2 because it introduces regressions in PostgreSQL
  * db-do-command allows you to execute SQL without a transaction wrapping it [JDBC-38](http://dev.clojure.org/jira/browse/JDBC-38)
  * Remove reflection warning from execute-batch
  * Add notes to README about 3rd party database driver dependencies
  * Add optional :identifiers argument to resultset-seq so you can explicitly pass in the naming strategy
* Release 0.2.3 on 2012-06-18
  * as-str now treats a.b as two identifiers separated by . so quoting produces [a].[b] instead of [a.b]
  * Add :connection-uri option [JDBC-34](http://dev.clojure.org/jira/browse/JDBC-34)
* Release 0.2.2 on 2012-06-10
  * Handle Oracle unknown row count affected [JDBC-33](http://dev.clojure.org/jira/browse/JDBC-33)
  * Handle jdbc: prefix in string db-specs [JDBC-32](http://dev.clojure.org/jira/browse/JDBC-32)
  * Handle empty columns in make column unique (Juergen Hoetzel) [JDBC-31](http://dev.clojure.org/jira/browse/JDBC-31)
* Release 0.2.1 on 2012-05-10
  * Result set performance enhancement (Juergen Hoetzel) [JDBC-29](http://dev.clojure.org/jira/browse/JDBC-29)
  * Make do-prepared-return-keys (for Korma team) [JDBC-30](http://dev.clojure.org/jira/browse/JDBC-30)
* Release 0.2.0 on 2012-04-23
  * Merge internal namespace into main jdbc namespace [JDBC-19](http://dev.clojure.org/jira/browse/JDBC-19)
* Release 0.1.4 on 2012-04-15
  * Unwrap RTE for nested transaction exceptions (we already
    unwrapped top-level transaction RTEs).
  * Remove reflection warning unwrapping RunTimeException (Alan Malloy)
* Release 0.1.3 on 2012-02-29
  * Fix generated keys inside transactions for SQLite3 [JDBC-26](http://dev.clojure.org/jira/browse/JDBC-26)
* Release 0.1.2 on 2012-02-29
  * Handle prepared statement params correctly [JDBC-23](http://dev.clojure.org/jira/browse/JDBC-23)
  * Add support for SQLite3 [JDBC-26](http://dev.clojure.org/jira/browse/JDBC-26)
  * Replace replicate (deprecated) with repeat [JDBC-27](http://dev.clojure.org/jira/browse/JDBC-27)
  * Ensure MS SQL Server passes tests with both Microsoft and jTDS drivers
  * Build server now tests derby, hsqldb and sqlite by default
  * Update README per Stuart Sierra's outline for contrib projects
* Release 0.1.1 on 2011-11-02
  * Accept string or URI in connection definition [JDBC-21](http://dev.clojure.org/jira/browse/JDBC-21)
  * Allow driver, port and subprotocol to be deduced [JDBC-22](http://dev.clojure.org/jira/browse/JDBC-22)
* Release 0.1.0 on 2011-10-16
  * Remove dependence on deprecated structmap [JDBC-15](http://dev.clojure.org/jira/browse/JDBC-15)
* Release 0.0.7 on 2011-10-11
  * Rename duplicate columns [JDBC-9](http://dev.clojure.org/jira/browse/JDBC-9)
  * Ensure do-preared traps invalid SQL [JDBC-16](http://dev.clojure.org/jira/JDBC-16)
* Release 0.0.6 on 2011-08-04
  * Improve exception handling (unwrap RTE)
  * Don't use batch for update (causes exceptions on Apache Derby) [JDBC-12](http://dev.clojure.org/jire/JDBC-12)
  * Add test suite
* Release 0.0.5 on 2011-07-18
  * Expose prepare-statement API
  * Allow with-query-results to accept a PreparedStatement or options for creating one, instead of SQL query string and parameters
  * Support databases that cannot return generated keys
* Release 0.0.4 on 2011-07-17
  * Allow :table-spec {string} in create-table  [JDBC-4](http://dev.clojure.org/jire/JDBC-4)
  * Remove reflection warnings [JDBC-8](http://dev.clojure.org/jire/JDBC-8)
  * Ensure transactions are not committed when Error occurs [JDBC-11](http://dev.clojure.org/jire/JDBC-11)
* Release 0.0.3 on 2011-07-01
  * Key generation compatibility with MS SQL Server, PostgreSQL [JDBC-10](http://dev.clojure.org/jira/browse/JDBC-10)
* Release 0.0.2 on 2011-06-07
  * Clojure 1.2 compatibility [JDBC-7](http://dev.clojure.org/jira/browse/JDBC-7)
* Release 0.0.1 on 2011-05-07
  * Initial release

* Changes from clojure.contrib.sql:
  * Expose print-... functions; no longer write exceptions to **\*out\***
  * Define resultset-seq to replace clojure.core/resultset-seq
  * Add naming / quoting strategies (see [name mapping documentation](http://clojure.github.com/java.jdbc/doc/clojure/java/jdbc/NameMapping.html)
  * Return generated keys from insert operations, where possible
  * Add insert-record function
  * Clojure 1.3 compatibility

Copyright and License
========================================

Copyright (c) Sean Corfield, Stephen Gilardi, 2011-2014. All rights reserved.  The use and
distribution terms for this software are covered by the Eclipse Public
License 1.0 (http://opensource.org/licenses/eclipse-1.0.php) which can
be found in the file epl-v10.html at the root of this distribution.
By using this software in any fashion, you are agreeing to be bound by
the terms of this license.  You must not remove this notice, or any
other, from this software.
