# Manipulating Tables with DDL
Currently you can create and drop tables using clojure.java.jdbc. To see how to manipulate data with SQL, see [Manipulating Data with SQL](https://github.com/clojure/java.jdbc/blob/master/doc/clojure/java/jdbc/UsingSQL.md).
## Creating a table
To create a table, use *create-table* with the table name and a vector for each column spec. Currently, table-level specifications are not supported.

```
(defn create-fruit
  "Create a table"
  []
  (sql/create-table
    :fruit
    [:name "varchar(32)" "PRIMARY KEY"]
    [:appearance "varchar(32)"]
    [:cost :int]
    [:grade :real]))
```
## Dropping a table
To drop a table, use *drop-table* with the table name.

```
(defn drop-fruit
  "Drop a table"
  []
  (try
    (sql/drop-table :fruit)
    (catch Exception _)))
```
## Accessing table metadata
To retrieve the metadata for a table, you can operate on the connection itself. In future, functions may be added to make this easier.

```
(defn db-get-tables
  "Demonstrate getting table info"
  []
  (sql/with-connection db
    (into []
          (sql/resultset-seq
           (-> (sql/connection)
               (.getMetaData)
               (.getTables nil nil nil (into-array ["TABLE" "VIEW"])))))))
```