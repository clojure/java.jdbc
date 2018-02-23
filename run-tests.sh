#!/bin/sh
#
# Run this shell script with a list of databases you want to test against:
#
# ./run-tests.sh mysql postgres
#
# The default list of derby h2 hsqldb sqlite are always tested against.
#
# Additionally you can specify: mssql jtds postgres pgsql mysql
#
# Alternatives to mysql (a db-spec) are mysql-str and mysql-jdbc-str which use
# connection strings instead. You may only specify one of these!
#
# Note: for mysql, postgres, and pgsql, the tests assume a database schema
# called clojure_test that is accesible by a user clojure_test with the
# password # clojure_test (currently hardcoded in the tests, sorry!).
# This will eventually change...
#
# For postgres or pgsql, you can set the following environment variables
# to override the defaults of 127.0.0.1 and 5432:
#
# TEST_POSTGRES_HOST TEST_POSTGRES_PORT
#
# Currently you may only specify one of postgres or pgsql!
#
# For mssql, you can set the following environment variables to override the
# defaults of 127.0.0.1\\SQLEXPRESS, 1433, clojure_test, sa, (empty string):
#
# TEST_MSSQL_HOST TEST_MSSQL_PORT TEST_MSSQL_NAME TEST_MSSQL_USER TEST_MSSQL_PASS
#
# For jtds, you can set the following environment variables (defaults per above):
#
# TEST_JTDS_HOST TEST_JTDS_PORT TEST_JTDS_NAME TEST_JTDS_USER TEST_JTDS_PASS
#
# For jtds you can just specify the IP address or hostname, you do not need
# the \\SQLEXPRESS part.
#
# Note: if you specify both mssql and jtds, make sure they're pointing at
# different database names or the tests will fail!
#
# Default set of databases to test:
dbs="derby h2 hsqldb sqlite"

# Start with clean databases each time to avoid slowdown
rm -rf clojure_test_*

versions="1.7 1.8 1.9 master"
for v in $versions
do
  TEST_DBS="$dbs $*" time clj -A:test -A:$v -A:runner
done
