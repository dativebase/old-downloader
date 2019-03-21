(defproject old-downloader "0.1.0-SNAPSHOT"
  :description "Download all the data in an OLD"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/test.check "0.9.0"]
                 [cheshire "5.8.1"]
                 [clj-time "0.15.0"]
                 [inflections "0.13.2"]
                 [slingshot "0.12.2"]
                 [org.onlinelinguisticdatabase/old-client "0.1.0"]]
  :main ^:skip-aot old-downloader.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
