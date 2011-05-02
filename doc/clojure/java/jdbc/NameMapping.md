# Mapping Keywords to/from Entity Names
Entity names in SQL may be specified as strings or as keywords. It's convenient to represent records as Clojure maps with keywords for the keys but this means that a mapping is required when moving from Clojure to SQL and back again. Historically, clojure.contrib.sql simply called (name) on keywords passed in and used clojure.core/resultset-seq to convert Java ResultSet objects back to Clojure maps, which had the side-effect of lowercasing all entity names as they became keywords. Whilst that is still the default behavior of clojure.java.jdbc, it is now possible to override this behavior in a couple of ways.
## Quoted Identifiers
The first problem that the old approach exposed was when table names or column names were the same as a SQL keyword. Databases provide a way to quote identifier names so that entity names do not get treated as SQL keywords (often referred to as 'stropping'). Unfortunately, the way quoting works tends to be vendor-specific so that Microsoft SQL Server accepts \[name\] and "name" whilst MySQL traditionally uses \`name\` (although recent versions also accept "name"). In order to support multiple approaches to quoted, clojure.java.jdbc now has a function *as-quoted-identifier* and a macro *with-quoted-identifiers* to allow you to specify how quoting should be handled.

A quote spec is either a single character or a pair of characters in a vector. For the former, the entity name is wrapped in a pair of that character. For the latter, the entity name is wrapped in the specified pair:

```clj
(as-quoted-identifier \` :name) ;; produces "`name`"
(as-quoted-identifier [[ ]] :name) ;; produces "[name]"
```
Any code can be wrapped in the *with-quoted-identifiers* macro to influence how keywords are mapped to entity names:

```clj
(sql/with-quoted-identifiers \`
  (sql/insert-record
    :fruit
    { :name "Pear" :appearance "green" :cost 99}))
;; INSERT INTO `fruit` ( `name`, `appearance`, `cost` )
;; VALUES ( 'Pear', 'green', 99 )
```
Note that if a string is used for an entity name, rather than a keyword, it is passed through unchanged, on the assumption that if you're explicitly passing strings, you want to control exactly what goes into the SQL.
## Naming Strategies
The second problem with the old approach was that in returned results, all entity names were mapped to lowercase so any case sensitivity in the original entity names was lost. In addition to the option to determine a quoting strategy, clojure.java.jdbc now has functions *as-named-identifier*, *as-named-keyword* and a macro *with-naming-strategy* to allow you to specify how entity name / keyword mapping should be performed in both directions.

A naming strategy may be a single function or a map containing the keys `:entity` and/or `:keyword`, whose values are functions. If a single function `f` is provided, it is treated as `{ :entity f }`, i.e., a mapping from entity names to keywords. The `:entity` mapping is used on keywords that need to be converted to entity names. The `:keyword` mapping is used on entity names that need to be converted to keywords.

```clj
(as-named-identifier clojure.string/upper-case :name) ;; produces "NAME"
(def quote-dash
  { :entity (partial as-quoted-str \`) :keyword #(.replace % "_" "-") })
(as-named-identifier quote-dash :name) ;; produces "`name`"
(as-named-keyword quote-dash "item_id") ;; produces :item-id
```

The default naming strategy is `{ :entity identity :keyword clojure.string/lower-case }`. clojure.java.jdbc uses its own version of *resultset-seq* that respects the current naming strategy (for entity names mapped to keywords). That is not currently exposed as a public function. You cannot specify a naming strategy that produces strings instead of keywords (but you could use a keyword naming strategy of *identity* to preserve case and spelling).
## Convenience Functions
In addition to *as-quoted-identifier*, *as-named-identifier* and *as-named-keyword* described above, clojure.java.jdbc exposes *as-identifier* which maps strings/keywords to entity names under the current naming strategy and *as-keyword* which maps strings/keywords to keywords under the current naming strategy.

The core of the quoting strategy is also exposed as *as-quoted-str* (shown in the `quote-dash` example above).