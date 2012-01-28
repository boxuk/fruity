
(ns bppm.pear
    (:require [bppm.util :as util]))

(defn- package-info
    "Parse package info from pear channel string"
    [string]
    (rest (re-matches
        #"^.*?/(.*?)\s+(\d+\.\d+\.\d+).*" string)))

(defn- to-map
    "Change list of name and value into map"
    [acc e]
    (assoc acc (keyword (first e))
               (second e)))

(defn- channel-checkout
    "Checkout the PEAR channel"
    [pear]
    (util/run-commands "rm -rf build/pear"
                       "mkdir -p build"
                       (format "svn co %s build/pear" (:repoUrl pear))))

(defn- channel-add-files
    "Add any unknown files in the channel repo"
    []
    (let [lines (util/run-command "svn st build/pear")
          unknown (filter #(re-matches #"^\\?(.*)" %) lines)]
        (doseq [file unknown]
            (util/run-command (format "svn add %s"
                                      (.substring file 2))))))

(defn- channel-commit
    "Commit the built package to the channel"
    [pear library tag]
    (util/run-command (format "pirum add build/pear build/repo/%s-%s.tgz"
                           (:name library) tag))
    (channel-add-files)
    (util/run-command (format "svn ci -m '%s %s' build/pear" 
                           (:name library) tag)))

;; Public

(defn channel-info
    "Fetch package information for the specified channel"
    [channel]
    (util/run-command "pear clear-cache")
    (let [command (format "pear list-all -c %s" channel)]
        (reduce to-map {}
            (map package-info 
                (drop 3 (util/run-command command))) )))

(defn commit-package
    "Commit a package that has been built"
    [pear library tag]
    (channel-checkout pear)
    (channel-commit pear library tag))

