# Connection Pooling
clojure.java.jdbc does not provide connection pooling directly but it is relatively easy to add to your project. The following example shows how to configure connection pooling use c3p0.
## Add the c3p0 dependency
If you're using Leiningen, you can just add the following to your dependencies:

```
[c3p0/c3p0 "0.9.1.2"]
```
In Maven, it would be:

```
<dependency>
  <groupId>c3p0</groupId>
  <artifactId>c3p0</artifactId>
  <version>0.9.1.2</version>
</dependency>
```
## Create the pooled datasource from your db-spec
Define your db-spec as usual, for example (for MySQL):

```
(def db-spec 
  {:classname "com.mysql.jdbc.Driver"
   :subprotocol "mysql"
   :subname "//127.0.0.1:3306/mydb"
   :user "myaccount"
   :password "secret"})
```
Import the c3p0 class as part of your namespace declaration, for example:

```
(ns example.db
  (:import com.mchange.v2.c3p0.ComboPooledDataSource))
```
Define a function that creates a pooled datasource:

```
(defn pool
  [spec]
  (let [cpds (doto (ComboPooledDataSource.)
               (.setDriverClass (:classname spec)) 
               (.setJdbcUrl (str "jdbc:" (:subprotocol spec) ":" (:subname spec)))
               (.setUser (:user spec))
               (.setPassword (:password spec))
               ;; expire excess connections after 30 minutes of inactivity:
               (.setMaxIdleTimeExcessConnections (* 30 60))
               ;; expire connections after 3 hours of inactivity:
               (.setMaxIdleTime (* 3 60 60)))] 
    {:datasource cpds}))
```
Now you can create a single connection pool:

```
(defn db-connection []
  (let [pooled-db (delay (pool db-spec))]
    @pooled-db))
```
And then call (db-connection) wherever you need access to it.