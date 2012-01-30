
(ns boxuk.fruity.library
    (:use boxuk.fruity.util
          clojure.java.shell))

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
            (map parse-tag (sh-str command)))))

(defn- checkout-library
    "Checkout using the command"
    [library command]
    (sh-str "rm -rf build/repo")
    (sh-str "mkdir -p build")
    (sh-str (format command (:url library) "build/repo")))

;; Public

(defmulti tags :type)

(defmulti checkout :type)

(defn make-package
    "Build a package for a library"
    [library tag]
    (checkout library tag)
    (sh-str (package-command library tag) "build/repo"))

;; Git

(defmethod tags :git
    [library]
    (tags-from (format "git ls-remote --tags %s" (:url library))
               #".*v(\d+\.\d+.\d+)$"))

(defmethod checkout :git
    [library tag]
    (checkout-library library "git clone %s %s")
    (with-sh-dir "build/repo"
        (sh-str (str "git checkout -q v" tag))))

;; SVN

(defmethod tags :svn
    [library]
    (tags-from (format "svn ls %s/tags" (:url library))
               #"(\d+\.\d+\.\d+)/"))

(defmethod checkout :svn
    [library tag]
    (checkout-library library (str "svn co %s/tags/" tag " %s")))

