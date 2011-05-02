# Manipulating Tables with DDL
Currently you can create and drop tables using clojure.java.jdbc.
## Creating a table
```clj
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
```clj
(defn drop-fruit
  "Drop a table"
  []
  (try
    (sql/drop-table :fruit)
    (catch Exception _)))
```
## Accessing table metadata
```clj
(defn db-get-tables
  "Demonstrate getting table info"
  []
  (sql/with-connection db
    (into []
          (resultset-seq
           (-> (sql/connection)
               (.getMetaData)
               (.getTables nil nil nil (into-array ["TABLE" "VIEW"])))))))
```