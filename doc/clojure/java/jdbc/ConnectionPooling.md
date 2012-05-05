# Connection Pooling
clojure.java.jdbc does not provide connection pooling directly but it is relatively easy to add to your project. The following example shows how to configure connection pooling use c3p0.
## Add the c3p0 dependency
If you're using Leiningen, you can just add the following to your dependencies:

    [c3p0/c3p0 "0.9.1.2"]

In Maven, it would be:

    <dependency>
      <groupId>c3p0</groupId>
      <artifactId>c3p0</artifactId>
      <version>0.9.1.2</version>
    </dependency>

Import the c3p0 class as part of your namespace declaration, for example  (for postgresql):

    (ns example.db
      (:import com.mchange.v2.c3p0.ComboPooledDataSource))

    (def db (delay {:datasource (doto (ComboPooledDataSource.)
                                  (.setDriverClass "org.postgresql.Driver")
                                  (.setJdbcUrl "jdbc:postgresql://DB-HOST:DB-PORT/DB-NAME")
                                  (.setUser "DB-USER")
                                  (.setPassword "DB-PASS")
                                  ;; expire excess connections after 30 minutes of inactivity:
                                  (.setMaxIdleTimeExcessConnections (* 30 60))
                                  ;; expire connections after 3 hours of inactivity:
                                  (.setMaxIdleTime (* 3 60 60)))}))

And then call `@db` wherever you need access to it.