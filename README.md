# java.jdbc

The library formerly known as clojure.contrib.sql.

## Depending on java.jdbc

In Maven:

```xml
<dependency>
  <groupId>org.clojure</groupId>
  <artifactId>java.jdbc</artifactId>
  <version>0.1.0</version>
</dependency>
```

In Leiningen:

```clj
  :dependencies [...
                 [org.clojure/java.jdbc "0.1.0"]
                 ...]
```

## Testing

Currently by default tests run only against Derby and HSQLDB, the
in-process databases. 

To test against PostgreSQL, first create the user and database:

    $ sudo -u postgres createuser clojure_test -P # password: clojure_test
    $ sudo -u postgres createdb clojure_test -O clojure_test

Or similarly with MySQL:

    $ mysql -u root
    mysql> create database clojure_test;
    mysql> grant all on clojure_test.* to clojure_test identified by "clojure_test";

Then run the tests with the <tt>TEST_DBS</tt> environment variable.

    $ TEST_DBS=mysql,postgres mvn test

## License

Copyright (c) Rich Hickey and contributors. All rights reserved.

The use and distribution terms for this software are covered by the
Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
which can be found in the file epl.html at the root of this distribution.
By using this software in any fashion, you are agreeing to be bound by
the terms of this license.
You must not remove this notice, or any other, from this software.


