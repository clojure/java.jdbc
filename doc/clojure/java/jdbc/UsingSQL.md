# Manipulating Data with SQL
Here are some examples of using clojure.java.jdbc to manipulate data with SQL.
These examples assume a simple table called fruit (see [Manipulating Tables with DDL](https://github.com/clojure/java.jdbc/blob/master/doc/clojure/java/jdbc/UsingDDL.md)).
## Inserting multiple rows
If you want to insert complete rows, you can use *insert-rows* and provide the values as a simple vector for each row. Every column's value must be present in the same order the columns are declared in the table. This performs a single insert statement. If you attempt to insert a single row, a map of the generated keys will be returned.

```
(defn insert-rows-fruit
  "Insert complete rows"
  []
  (sql/insert-rows
    :fruit
    ["Apple" "red" 59 87]
    ["Banana" "yellow" 29 92.2]
    ["Peach" "fuzzy" 139 90.0]
    ["Orange" "juicy" 89 88.6]))
```
## Inserting partial rows
If you want to insert rows but only specify some columns' values, you can use *insert-values* and provide the names of the columns following by vectors containing values for those columns. This performs a single insert statement. If you attempt to insert a single row, a map of the generated keys will be returned.

```
(defn insert-values-fruit
  "Insert rows with values for only specific columns"
  []
  (sql/insert-values
    :fruit
    [:name :cost]
    ["Mango" 722]
    ["Feijoa" 441]))
```
## Inserting a record
If you want to insert a single record, you can use *insert-record* and specify the columns and their values as a map. This performs a single insert statement. A map of the generated keys will be returned.

```
(defn insert-record-fruit
  "Insert a single record, map from keys specifying columsn to values"
  []
  (sql/insert-record
    :fruit
    {:name "Pear" :appearance "green" :cost 99}))
```
## Inserting multiple records
If you want to insert multiple records, you can use *insert-records* and specify each record as a map of columns and their values. This performs a separate insert statement for each record. The generated keys are returned in a sequence of maps.

```
(defn insert-records-fruit
  "Insert records, maps from keys specifying columns to values"
  []
  (sql/insert-records
    :fruit
    {:name "Pomegranate" :appearance "fresh" :cost 585}
    {:name "Kiwifruit" :grade 93}))
```
## Using transactions
You can write multiple operations in a transaction to ensure they are either all performed, or all rolled back.

```
(defn db-write
  "Write initial values to the database as a transaction"
  []
  (sql/with-connection db
    (sql/transaction
     (drop-fruit)
     (create-fruit)
     (insert-rows-fruit)
     (insert-values-fruit)
     (insert-records-fruit)))
  nil)
```
## Reading and processing rows
To execute code against each row in a result set, use *with-query-results* with SQL.

```
(defn db-read
  "Read the entire fruit table"
  []
  (sql/with-connection db
    (sql/with-query-results res
      ["SELECT * FROM fruit"]
      (doseq [rec res]
        (println rec)))))

(defn db-read-all
  "Return all the rows of the fruit table as a vector"
  []
  (sql/with-connection db
    (sql/with-query-results res
      ["SELECT * FROM fruit"]
      (into [] res))))

(defn db-grade-range
  "Print rows describing fruit that are within a grade range"
  [min max]
  (sql/with-connection db
    (sql/with-query-results res
      [(str "SELECT name, cost, grade "
            "FROM fruit "
            "WHERE grade >= ? AND grade <= ?")
       min max]
      (doseq [rec res]
        (println rec)))))

(defn db-grade-a 
  "Print rows describing all grade a fruit (grade between 90 and 100)"
  []
  (db-grade-range 90 100))
```
## Updating values across a table
To update column values based on a SQL predicate, use *update-values* with a SQL where clause and a map of columns to new values. The result is a sequence of update counts, indicating the number of records affected by each update (in this case, a single update and therefore a single count in the sequence).

```
(defn db-update-appearance-cost
  "Update the appearance and cost of the named fruit"
  [name appearance cost]
  (sql/update-values
   :fruit
   ["name=?" name]
   {:appearance appearance :cost cost}))

(defn db-update
  "Update two fruits as a transaction"
  []
  (sql/with-connection db
    (sql/transaction
     (db-update-appearance-cost "Banana" "bruised" 14)
     (db-update-appearance-cost "Feijoa" "green" 400)))
  nil)
```
## Updating values or Inserting records conditionally
If you want to update existing records that match a SQL predicate or insert a new record if no existing records match, use *update-or-insert-values* with a SQL where clause and a map of columns to values. This calls *update-values* first and if no rows were updated, this calls *insert-values*. The result is either the sequence of update counts from the update or the sequence of generated key maps from the insert.

```
(defn db-update-or-insert
  "Updates or inserts a fruit"
  [record]
  (sql/with-connection db
    (sql/update-or-insert-values
     :fruit
     ["name=?" (:name record)]
     record)))
```
## Exception Handling and Transaction Rollback
Transactions are rolled back if an exception is thrown, as shown in these examples.

```
(defn db-exception
  "Demonstrate rolling back a partially completed transaction on exception"
  []
  (sql/with-connection db
    (sql/transaction
     (sql/insert-values
      :fruit
      [:name :appearance]
      ["Grape" "yummy"]
      ["Pear" "bruised"])
     ;; at this point the insert-values call is complete, but the transaction
     ;; is not. the exception will cause it to roll back leaving the database
     ;; untouched.
     (throw (Exception. "sql/test exception")))))

(defn db-sql-exception
  "Demonstrate an sql exception"
  []
  (sql/with-connection db
    (sql/transaction
     (sql/insert-values
      :fruit
      [:name :appearance]
      ["Grape" "yummy"]
      ["Pear" "bruised"]
      ["Apple" "strange" "whoops"]))))

(defn db-batchupdate-exception
  "Demonstrate a batch update exception"
  []
  (sql/with-connection db
    (sql/transaction
     (sql/do-commands
      "DROP TABLE fruit"
      "DROP TABLE fruit"))))

(defn db-rollback
  "Demonstrate a rollback-only trasaction"
  []
  (sql/with-connection db
    (sql/transaction
     (prn "is-rollback-only" (sql/is-rollback-only))
     (sql/set-rollback-only)
     (sql/insert-values
      :fruit
      [:name :appearance]
      ["Grape" "yummy"]
      ["Pear" "bruised"])
     (prn "is-rollback-only" (sql/is-rollback-only))
     (sql/with-query-results res
       ["SELECT * FROM fruit"]
       (doseq [rec res]
         (println rec))))
    (prn)
    (sql/with-query-results res
      ["SELECT * FROM fruit"]
      (doseq [rec res]
        (println rec)))))
```