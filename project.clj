;; NOTE: This project.clj file exists purely to make it easier to
;; develop and test java.jdbc locally. The pom.xml file is the
;; "system of record" as far as the project version is concerned.

(defproject org.clojure/java.jdbc "0.6.0-SNAPSHOT"
  :description "A low-level Clojure wrapper for JDBC-based access to databases."
  :parent [org.clojure/pom.contrib "0.1.2"]
  :url "https://github.com/clojure/java.jdbc"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :source-paths ["src/main/clojure"]
  :test-paths ["src/test/clojure"]
  :repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 ;; These are just the versions most recently test against
                 ;; for your own projects, use whatever version is most
                 ;; appropriate for you. Again, note that this project.clj
                 ;; file exists for convenience -- the pom.xml file is the
                 ;; "system of record" as far as dependencies go!
                 [org.apache.derby/derby "10.11.1.1"]
                 [org.hsqldb/hsqldb "2.3.3"]
                 [com.h2database/h2 "1.4.188"]
                 [net.sourceforge.jtds/jtds "1.3.1"]
                 [mysql/mysql-connector-java "5.1.36"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [org.xerial/sqlite-jdbc "3.8.11.1"]
                 ;; if you have the MS driver in your local repo
                 [sqljdbc4 "4.0"]
                 ]
  :profiles {:1.4 {:dependencies [[org.clojure/clojure "1.4.0"]]}
             :1.5 {:dependencies [[org.clojure/clojure "1.5.1"]]}
             :1.6 {:dependencies [[org.clojure/clojure "1.6.0"]]}
             :1.7 {:dependencies [[org.clojure/clojure "1.7.0"]]}
             :1.8 {:dependencies [[org.clojure/clojure "1.8.0"]]}
             :1.9 {:dependencies [[org.clojure/clojure "1.9.0-master-SNAPSHOT"]]}
             }
  :aliases {"test-all" ["with-profile" "test,1.4:test,1.5:test,1.6:test,1.7:test,1.8:test,1.9" "test"]
            "check-all" ["with-profile" "1.4:1.5:1.6:1.7:1.8:1.9" "check"]}
  :min-lein-version "2.0.0")
