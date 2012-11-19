(defproject unix-clojure "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [midje "1.3.0"]
                 [clargon "1.0.0"]
                 [clj-time "0.4.4"]]
  :profiles {:dev {:plugins [[lein-midje "2.0.0-SNAPSHOT"]]}}
  :main unix-clojure.shell
)
