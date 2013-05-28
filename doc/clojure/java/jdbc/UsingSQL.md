# Manipulating Data with SQL
Here are some examples of using clojure.java.jdbc to manipulate data with SQL.
These examples assume a simple table called fruit (see [Manipulating Tables with DDL](https://github.com/clojure/java.jdbc/blob/master/doc/clojure/java/jdbc/UsingDDL.md)).
## Inserting multiple rows
If you want to insert complete rows, you can use *insert!* and provide the values as a simple vector for each row. Every column's value must be present in the same order the columns are declared in the table. This performs a series of insert statement. If you attempt to insert a single row, a map of the generated keys will be returned.

    (defn insert-rows-fruit
      "Insert complete rows"
      [db]
      (j/insert! db
        :fruit
        nil ; column names not supplied
        [1 "Apple" "red" 59 87]
        [2 "Banana" "yellow" 29 92.2]
        [3 "Peach" "fuzzy" 139 90.0]
        [4 "Orange" "juicy" 89 88.6]))
    ;; (1 1 1 1) - row counts modified

## Inserting partial rows
If you want to insert rows but only specify some columns' values, you can use *insert!* and provide the names of the columns following by vectors containing values for those columns. This performs a single insert statement. If you attempt to insert a single row, a map of the generated keys will be returned.

    (defn insert-values-fruit
      "Insert rows with values for only specific columns"
      [db]
      (j/insert! db
        :fruit
        [:name :cost]
        ["Mango" 722]
        ["Feijoa" 441]))

## Inserting a record
If you want to insert a single record, you can use *insert!* and specify the columns and their values as a map. This performs a single insert statement. A map of the generated keys will be returned.

    (defn insert-record-fruit
      "Insert a single record, map from keys specifying columsn to values"
      [db]
      (j/insert! db
        :fruit
        {:name "Pear" :appearance "green" :cost 99}))

## Inserting multiple records
If you want to insert multiple records, you can use *insert!* and specify each record as a map of columns and their values. This performs a separate insert statement for each record. The generated keys are returned in a sequence of maps.

    (defn insert-records-fruit
      "Insert records, maps from keys specifying columns to values"
      [db]
      (j/insert! db
        :fruit
        {:name "Pomegranate" :appearance "fresh" :cost 585}
        {:name "Kiwifruit" :grade 93}))

## Using transactions
You can write multiple operations in a transaction to ensure they are either all performed, or all rolled back.

    (defn db-write
      "Write initial values to the database as a transaction"
      [db]
      (j/db-transaction [t-con db]
        (insert-rows-fruit t-con)
        (insert-values-fruit t-con)
        (insert-records-fruit t-con)))

The *db-transaction* macro creates a transaction-aware connection from the database specification and that should be used in the body of the transaction code.

## Reading and processing rows
To execute code against each row in a result set, use *query* with SQL.

    (defn db-read
      "Read the entire fruit table"
      [db]
      (j/query db
        ["SELECT * FROM fruit"]
        :row-fn println))
    
    (defn db-read-all
      "Return all the rows of the fruit table as a vector"
      [db]
      (j/query db
        ["SELECT * FROM fruit"]
        :as-arrays? true))
    
    ;; ([:id :name :appearance :cost :grade]
    ;;  [1 "Apple" "red" 59 87.0]
    ;;  [2 "Banana" "yellow" 29 92.2]
    ;;  ...)
    
    (defn db-grade-range
      "Print rows describing fruit that are within a grade range"
      [db min max]
      (j/query db
        [(str "SELECT name, cost, grade "
              "FROM fruit "
              "WHERE grade >= ? AND grade <= ?")
         min max]
        :row-fn println))
    
    (defn db-grade-a 
      "Print rows describing all grade a fruit (grade between 90 and 100)"
      [db]
      (db-grade-range db 90 100))

    (defn db-group-by-alias
      "Transform the rows to group the result per alias. SQL alias like 'f_name' is split info :f alias and :name column alias
      Result: [{:bike {:name \"abc\" :price 22} :category {:name \"Mountain\"}}]"
      [db]
      (j/query db ["select b.name as bike_name, b.price as bike_price, c.name :as category_name from bike b join category c on b.cat_id = c.id"]
                        :row-fn sql/group-by-alias))




## Updating values across a table
To update column values based on a SQL predicate, use *update!* with a SQL where clause and a map of columns to new values. The result is a sequence of update counts, indicating the number of records affected by each update (in this case, a single update and therefore a single count in the sequence).

    (defn db-update-appearance-cost
      "Update the appearance and cost of the named fruit"
      [db name appearance cost]
      (j/update! mysql-db
       :fruit
       {:appearance appearance :cost cost}
       ["name=?" name]))
    
    (defn db-update
      "Update two fruits as a transaction"
      [db]
      (j/db-transaction [t-con db]
        (db-update-appearance-cost t-con "Banana" "bruised" 14)
        (db-update-appearance-cost t-con "Feijoa" "green" 400)))

## Updating values or Inserting records conditionally
If you want to update existing records that match a SQL predicate or insert a new record if no existing records match, you can use a transaction wrapped around an *update!* and a conditional *insert!*.
use *update-or-insert-values* with a SQL where clause and a map of columns to values. This calls *update-values* first and if no rows were updated, this calls *insert-values*. The result is either the sequence of update counts from the update or the sequence of generated key maps from the insert.

    (defn db-update-or-insert
      "Updates or inserts a fruit"
      [db record where-clause]
      (j/db-transaction [t-con db]
        (let [result (j/update! t-con :fruit record where-clause)]
          (if (zero? (first result))
            (j/insert! t-con :fruit record)
            result))))
    
    (db-update-or-insert mysql-db
      {:name "Cactus" :appearance "Spiky" :cost 2000}
      ["name = ?" "Cactus"])
    ;; inserts Cactus
    (db-update-or-insert mysql-db
      {:name "Cactus" :appearance "Spiky" :cost 2500}
      ["name = ?" "Cactus"])
    ;; updates the Cactus we just inserted

## Exception Handling and Transaction Rollback
Transactions are rolled back if an exception is thrown, as shown in these examples.

    (defn db-exception
      "Demonstrate rolling back a partially completed transaction on exception"
      [db]
      (j/db-transaction [t-con db]
        (j/insert! t-con
          :fruit
          [:name :appearance]
          ["Grape" "yummy"]
          ["Pear" "bruised"])
        ;; at this point the insert-values call is complete, but the transaction
        ;; is not. the exception will cause it to roll back leaving the database
        ;; untouched.
        (throw (Exception. "sql/test exception")))))

Transactions can also be set explicitly to rollback instead of commit:

    (defn db-rollback
      "Demonstrate a rollback-only trasaction"
      [db]
      (j/db-transaction [t-con db]
        (prn "is-rollback-only" (j/db-is-rollback-only t-con))
        (j/db-set-rollback-only! t-con)
        (j/insert! t-con
          :fruit
          [:name :appearance]
          ["Grape" "yummy"]
          ["Pear" "bruised"])
         (prn "is-rollback-only" (j/db-is-rollback-only t-con))
         (j/query t-con
           ["SELECT * FROM fruit"]
           :row-fn println))
        (prn)
        (j/query db
          ["SELECT * FROM fruit"]
          :row-fn println))
