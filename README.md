clojure.java.jdbc
========================================

A Clojure wrapper for JDBC-based access to databases.

Formerly known as clojure.contrib.sql.

Releases and Dependency Information
========================================

Latest stable release: 0.3.0-alpha3

* [All Released Versions](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.clojure%22%20AND%20a%3A%22java.jdbc%22)

* [Development Snapshot Versions](https://oss.sonatype.org/index.html#nexus-search;gav~org.clojure~java.jdbc~~~)

[Leiningen](https://github.com/technomancy/leiningen) dependency information:
```clojure
[org.clojure/java.jdbc "0.3.0-alpha3"]
```
[Maven](http://maven.apache.org/) dependency information:
```xml
<dependency>
  <groupId>org.clojure</groupId>
  <artifactId>java.jdbc</artifactId>
  <version>0.3.0-alpha3</version>
</dependency>
```
You will also need to add dependencies for the JDBC driver you intend to use. Here are examples of the drivers currently used for testing,
shown as Leiningen dependencies:
```clojure
;; Apache Derby
[org.apache.derby/derby "10.8.1.2"]
;; HSQLDB
[hsqldb/hsqldb "1.8.0.10"]
;; Microsoft SQL Server using the jTDS driver
[net.sourceforge.jtds/jtds "1.2.4"]
;; MySQL
[mysql/mysql-connector-java "5.1.6"]
;; PostgreSQL
[postgresql/postgresql "8.4-702.jdbc4"]
;; SQLite
[org.xerial/sqlite-jdbc "3.7.2"]
```
For the latest versions, consult the vendor or project websites. clojure.java.jdbc is also tested against Microsoft's own JDBC4 Driver 3.0 but that
has to be downloaded manually and placed in a Maven repository accessible to your system. For testing, it was installed locally as:
```clojure
;; Microsoft SQL Server JDBC4 Driver 3.0
[sqljdbc4/sqljdbc4 "3.0"]
```

Example Usage
========================================
```clojure
(require '[clojure.java.jdbc :as j]
         '[clojure.java.jdbc.sql :as s])

(def mysql-db {:subprotocol "mysql"
               :subname "//127.0.0.1:3306/clojure_test"
               :user "clojure_test"
               :password "clojure_test"})

(j/insert! mysql-db :fruit
  {:name "Apple" :appearance "rosy" :cost 24}
  {:name "Orange" :appearance "round" :cost 49})
;; ({:generated_key 1} {:generated_key 2})

(j/query mysql-db
  (s/select * :fruit (s/where {:appearance "rosy"}))
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

        $ sudo -u postgres createuser clojure_test -P # password: clojure_test
        $ sudo -u postgres createdb clojure_test -O clojure_test

* Or similarly with MySQL:

        $ mysql -u root
        mysql> create database clojure_test;
        mysql> grant all on clojure_test.* to clojure_test identified by "clojure_test";

* Then run the tests with the <tt>TEST_DBS</tt> environment variable:

        $ TEST_DBS=mysql,postgres mvn test

Change Log
====================

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

Copyright (c) Sean Corfield, Stephen Gilardi, 2011-2013. All rights reserved.  The use and
distribution terms for this software are covered by the Eclipse Public
License 1.0 (http://opensource.org/licenses/eclipse-1.0.php) which can
be found in the file epl-v10.html at the root of this distribution.
By using this software in any fashion, you are agreeing to be bound by
the terms of this license.  You must not remove this notice, or any
other, from this software.
