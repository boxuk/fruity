
(defproject boxuk/fruity "0.3.0"
    :description "Pear Package Management Tool"
    :dependencies [[org.clojure/clojure "1.3.0"]
                   [cheshire "2.1.0"]
                   [boxuk/versions "0.6.0"]
                   [boxuk/toolchain "0.0.3"]]
    :main boxuk.fruity.core
    :plugins [[lein-bin "0.1.2"]])

