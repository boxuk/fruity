
(ns bppm.library
    (:require [bppm.util :as util]))

(defn- package-command
    "Returns the command to package the library"
    [library tag]                     
    (let [default "phing pear-package -Dversion=$VERSION"
          command (:packageCommand library default)]
        (.replaceAll command "\\$VERSION" tag)))

(defn- tags-from
    "Fetch tags using the specified command and regexp"
    [command regexp]
    (let [parse-tag #(second (re-matches regexp %))]
        (remove nil? 
            (map parse-tag (util/run-command command)))))

(defn- checkout-library
    "Checkout using the command"
    [library command]
    (util/run-commands "rm -rf build/repo"
                       "mkdir -p build"
                       (format command (:url library) "build/repo")))

;; Public

(defmulti tags :type)

(defmulti checkout :type)

(defn make-package
    "Build a package for a library"
    [library tag]
    (do (checkout library tag)
        (util/run-command (package-command library tag) "build/repo")))

;; Git

(defmethod tags :git
    [library]
    (tags-from (format "git ls-remote --tags %s" (:url library))
               #".*v(\d+\.\d+.\d+)$"))

(defmethod checkout :git
    [library tag]
    (do (checkout-library library "git clone %s %s")
        (util/run-command (str "git checkout -q v" tag) "build/repo")))

;; SVN

(defmethod tags :svn
    [library]
    (tags-from (format "svn ls %s/tags" (:url library))
               #"(\d+\.\d+\.\d+)/"))

(defmethod checkout :svn
    [library tag]
    (checkout-library library (str "svn co %s/tags/" tag " %s")))

