
(defproject boxuk.fruity "0.2.0"
    :description "Pear Package Management Tool"
    :dependencies [[org.clojure/clojure "1.3.0"]
                   [cheshire "2.1.0"]
                   [clj-http "0.3.1"]
                   [jithub "0.0.1"]
                   [boxuk.versions "0.1.0"]
                   [boxuk.toolchain "0.0.2"]]
    :main boxuk.fruity.core
    :plugins [[lein-bin "0.1.2"]])

