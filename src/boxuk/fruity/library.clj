
(ns boxuk.fruity.library
    (:use boxuk.fruity.util
          clojure.java.shell))

;; Public

(defn tags-from
    "Fetch tags using the specified command and regexp"
    [command regexp]
    (let [parse-tag #(second (re-matches regexp %))]
        (remove nil? 
            (map parse-tag (sh-str command)))))

(defn checkout-library
    "Checkout using the command"
    [library command]
    (sh-str "rm -rf build/repo")
    (sh-str "mkdir -p build")
    (sh-str (format command (:url library) "build/repo")))

(defmulti tags :scm)

(defmulti checkout :scm)

