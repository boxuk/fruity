
(ns boxuk.fruity.core
    (:gen-class)
    (:use boxuk.fruity.config
          boxuk.toolchain
          boxuk.versions)
    (:require [boxuk.fruity.scm :as scm]
              [boxuk.fruity.scm.git]
              [boxuk.fruity.scm.svn]
              [boxuk.fruity.backend :as backend]
              [boxuk.fruity.backend.pear]
              [boxuk.fruity.backend.github-downloads]
              [boxuk.fruity.backend.clojars]))

;; Binary Checking

(def binaries {
    :pear       ["svn" "pirum" "pear"]
    :clojars    ["lein" "scp"]
})

(defn- required-backends
    "Returns a list of the configured backends"
    [libraries]
    (distinct (map #(:type %) libraries)))

(defn- required-binaries
    "Returns binaries required for configured libraries"
    [libraries]
    (reduce #(concat %1 (%2 binaries)) []
             (required-backends libraries)))

;; Library Checking

(defn- build-package
    "Builds a specified package for a library"
    [library tag]
    (println (format "Build package '%s'" tag))
    (scm/checkout library tag)
    (backend/deploy library tag))

(defn- unpackaged-tags
    "Fetch new tags for the specified library"
    [library]
    (filter (partial later-version? (backend/latest-version library)) 
            (scm/tags library)))

(defn- check-library
    "Checks a library for new tags and builds them if needed"
    [library]
    (println (format "\nChecking library '%s'" (:name library)))
    (doseq [tag (unpackaged-tags library)]
        (build-package library tag)))

(defn- check-libraries
    "Checks all configured libraries for new packages"
    []
    (doseq [library (config :libraries) ]
        (check-library library)))

;; Plugins

(defn- load-plugins
    "Loads any plugins via config"
    []
    (doseq [plugin-type [:backend :scm]]
        (doseq [plugin (plugin-type (config :plugins))]
            (load-file plugin))))

;; Public

(defn -main[& args]
    (let [config-file (first args)]
        (load-file config-file)
        (load-plugins)
        (check-binaries (required-binaries (config :libraries)))
        (check-libraries)
        (shutdown-agents)))

