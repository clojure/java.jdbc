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
* db-do-command allows you to execute SQL without a transaction wrapping it
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
