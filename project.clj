
(defproject boxuk.fruity "0.2.0"
    :description "Pear Package Management Tool"
    :dependencies [[org.clojure/clojure "1.3.0"]
                   [cheshire "2.1.0"]
                   [clj-http "0.3.1"]
                   [boxuk.versions "0.0.5"]
                   [boxuk.toolchain "0.0.2"]]
    :main boxuk.fruity.core
    :uberjar-name "fruity.jar")

