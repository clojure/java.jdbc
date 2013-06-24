*Note: this documentation will soon move to [clojure-doc.org](http://clojure-doc.org) where it can be more easily maintained and accept community contributions.*

# Mapping Keywords to/from Entity Names
Entity names in SQL may be specified as strings or as keywords. It's convenient to represent records as Clojure maps with keywords for the keys but this means that a mapping is required when moving from Clojure to SQL and back again. Historically, clojure.contrib.sql simply called (name) on keywords passed in and used clojure.core/resultset-seq to convert Java ResultSet objects back to Clojure maps, which had the side-effect of lowercasing all entity names as they became keywords. Whilst that is still the default behavior of clojure.java.jdbc, it is now possible to override this behavior in a couple of ways.
## Quoted Entities
The first problem that the old approach exposed was when table names or column names were the same as a SQL keyword. Databases provide a way to quote identifier names so that entity names do not get treated as SQL keywords (often referred to as 'stropping'). Unfortunately, the way quoting works tends to be vendor-specific so that Microsoft SQL Server accepts \[name\] and "name" whilst MySQL traditionally uses \`name\` (although recent versions also accept "name"). In order to support multiple approaches to quoting, clojure.java.jdbc supports the concept of identifier functions and entity functions. The former are used to convert entity names in result sets into Clojure identifiers. The latter are used to convert Clojure identifiers (table names, keys in maps) into SQL entity names.

The function clojure.java.jdbc.sql/quoted can be used to create an entity function. It takes a quote spec and returns a function that will quote its argument appropriately. Other entity functions are clojure.java.jdbc.sql/as-is which leaves names untouched (and is the default) and clojure.java.jdbc.sql/lower-case which lowercases name (and is the default identifier function for result-set-seq.

A quote spec is either a single character or a pair of characters in a vector. For the former, the entity name is wrapped in a pair of that character. For the latter, the entity name is wrapped in the specified pair:

    ((s/quoted \`) "name") ;; produces "`name`"
    ((s/quoted [\[ \]]) "name") ;; produces "[name]"

Code can either be passed an entity function or be wrapped in the *entities* macro to influence how keywords are mapped to entity names:

    (j/insert! mysql-db :fruit
      {:name "Pear" :appearance "green" :cost 99}
      :entities (s/quoted \`))
      
    (s/entities (s/quoted \`)
      (j/insert! mysql-db :fruit
        {:name "Pear" :appearance "green" :cost 99}))

    ;; INSERT INTO `fruit` ( `name`, `appearance`, `cost` )
    ;; VALUES ( 'Pear', 'green', 99 )

Note that if a string is used for an entity name, rather than a keyword, it is passed through unchanged, on the assumption that if you're explicitly passing strings, you want to control exactly what goes into the SQL.
## Entity to Identifier
The second problem with the old approach was that in returned results, all entity names were mapped to lowercase so any case sensitivity in the original entity names was lost. In addition to the option to determine a quoting strategy, clojure.java.jdbc support identifier functions for *query* and *result-set-seq*.

    (j/query mysql-db
      (s/select * :fruit)
      :identifiers clojure.string/upper-case)
    
    (s/identifiers clojure.string/upper-case
      (j/query mysql-db
        (s/select * :fruit)))
    
    ;; ({:GRADE nil, :COST 24, :APPEARANCE "rosy", :NAME "Apple", :ID 1}
    ;;  ...)
