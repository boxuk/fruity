
(ns boxuk.fruity.backend.pear
    (:use boxuk.fruity.config
          boxuk.fruity.util
          boxuk.fruity.backend
          clojure.java.shell))

;; Package the library (assumed to be checked out)

(defn- package-command
    "Returns the command to package the library"
    [library tag]                     
    (let [default "phing pear-package -Dversion=$VERSION"
          command (:packageCommand library default)]
        (.replaceAll command "\\$VERSION" tag)))

(defn- package-library
    "Build a package for a library"
    [library tag]
    (with-sh-dir "build/repo"
        (sh-str (package-command library tag))))

;; Fetch PEAR channel info

(defn- channel-info-parse
    "Parse package info from pear channel string"
    [acc string]
    (let [regexp #"^.*?/(.*?)\s+(\d+\.\d+\.\d+).*"
          [name version] (rest (re-matches regexp string))]
        (assoc acc (keyword name) version)))

(defn- channel-info
    "Fetch package information for the specified channel"
    []
    (sh-str "pear clear-cache")
    (let [command (format "pear list-all -c %s" (:alias (config :pear)))]
        (reduce channel-info-parse {}
            (drop 3 (sh-str command)))))

;; Commit built package to PEAR channel

(defn- commit-add-files
    "Add any unknown files in the channel repo"
    []
    (let [lines (sh-str "svn st build/pear")
          unknownFiles (filter #(re-matches #"^\\?(.*)" %) lines)]
        (doseq [file unknownFiles]
            (sh-str (format "svn add %s" (.substring file 2))))))

(defn- commit
    "Commit the built package to the channel"
    [library tag]
    (sh-str (format "pirum add build/pear build/repo/%s-%s.tgz" (:name library) tag))
    (commit-add-files)
    (sh-str (format "svn ci -m '%s-%s' build/pear" (:name library) tag)))

;; Checkout PEAR channel

(defn- checkout-channel
    "Checkout the PEAR channel"
    [pear]
    (sh-str "rm -rf build/pear")
    (sh-str "mkdir -p build")
    (sh-str (format "svn co %s build/pear" (:repoUrl pear))))

;; Public

(defmethod latest-version :pear
    [library]
    (get (channel-info)
         (keyword (:name library))
         "0.0.0"))

(defmethod deploy :pear
    [library tag]
    (checkout-channel)
    (package-library library tag)
    (commit library tag))

