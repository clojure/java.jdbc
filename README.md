clojure.java.jdbc
========================================

A Clojure wrapper for JDBC-based access to databases.

Formerly known as clojure.contrib.sql.



Releases and Dependency Information
========================================

Latest stable release: 0.2.1

* [All Released Versions](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.clojure%22%20AND%20a%3A%22java.jdbc%22)

* [Development Snapshot Versions](https://oss.sonatype.org/index.html#nexus-search;gav~org.clojure~java.jdbc~~~)

[Leiningen](https://github.com/technomancy/leiningen) dependency information:

    [org.clojure/java.jdbc "0.2.1"]

[Maven](http://maven.apache.org/) dependency information:

    <dependency>
      <groupId>org.clojure</groupId>
      <artifactId>java.jdbc</artifactId>
      <version>0.2.1</version>
    </dependency>



Example Usage
========================================

    (require '[clojure.java.jdbc :as sql])
    
    (def mysql-db {:subprotocol "mysql"
        :subname "//127.0.0.1:3306/clojure_test"
        :user "clojure_test"
        :password "clojure_test"})
    
    (sql/with-connection mysql-db
        (sql/insert-records :fruit
            {:name "Apple" :appearance "rosy" :cost 24}
            {:name "Orange" :appearance "round" :cost 49}))
            
    (sql/with-connection mysql-db
        (sql/with-query-results rows
            ["SELECT * FROM fruit WHERE appearance = ?" "rosy"]
            (:cost (first rows))))

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

Copyright (c) Stephen Gilardi, Sean Corfield, 2011-2012. All rights reserved.  The use and
distribution terms for this software are covered by the Eclipse Public
License 1.0 (http://opensource.org/licenses/eclipse-1.0.php) which can
be found in the file epl-v10.html at the root of this distribution.
By using this software in any fashion, you are agreeing to be bound by
the terms of this license.  You must not remove this notice, or any
other, from this software.
