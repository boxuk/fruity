
(ns boxuk.fruity.backend.clojars
    (:use boxuk.fruity.backend
          boxuk.fruity.util
          clojure.java.shell
          cheshire.core)
    (:require [clojure.java.io :as io]))

(defn- search-json
    "Perform a search of clojars for the specified library"
    [name]
    (let [url (format "http://clojars.org/search?q=%s&format=json" name)]
        (with-open [in (io/reader url)]
            (:results (parse-stream in true)))))

;; Public

(defmethod latest-version :clojars
    [{:keys [name]}]
    (:version (first (filter #(= name (:jar_name %)) 
                              (search-json name)))
              "0.0.0"))

(defmethod deploy :clojars
    [{:keys [name]} tag]
    (let [jar-name (format "%s-%s.jar" name tag)]
        (with-sh-dir "build/repo"
            (sh-str "lein pom")
            (sh-str "lein jar")
            (sh-str (format "scp pom.xml %s clojars@clojars.org:" jar-name)))))

