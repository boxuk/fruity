
(ns boxuk.fruity.backend.clojars
    (:use boxuk.fruity.backend
          boxuk.fruity.util
          clojure.java.shell
          cheshire.core)
    (:require [clojure.java.io :as io]))

(defn- search-json
    "Perform a search of clojars for the specified library"
    [library]
    (let [url (format "http://clojars.org/search?q=%s&format=json" (:name library))]
        (parse-stream (io/reader url) true)))

;; Public

(defmethod latest-version :clojars
    [library]
    (let [json (search-json library)]
        (if (= 1 (:count json))
            (:version (first (:results json)))
            "0.0.0")))

(defmethod deploy :clojars
    [library tag]
    (let [jar-name (format "%s-%s.jar" (:name library) tag)]
        (with-sh-dir "build/repo"
            (sh-str "lein pom")
            (sh-str "lein jar")
            (sh-str (format "scp pom.xml %s clojars@clojars.org:" jar-name)))))

