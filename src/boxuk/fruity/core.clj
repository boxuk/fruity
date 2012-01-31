
(ns boxuk.fruity.core
    (:gen-class)
    (:use boxuk.fruity.config
          boxuk.toolchain
          boxuk.versions)
    (:require [boxuk.fruity.backend :as backend]
              [boxuk.fruity.backend.pear]
              [boxuk.fruity.backend.clojars]
              [boxuk.fruity.library :as library]))

(def required-binaries ["svn" "pirum" "pear"])

(defn build-package
    "Builds a specified package for a library"
    [library tag]
    (println (format "Build package '%s'" tag))
    (library/checkout library tag)
    (backend/deploy library tag))

(defn unpackaged-tags
    "Fetch new tags for the specified library"
    [library]
    (filter (partial later-version? (backend/latest-version library)) 
            (library/tags library)))

(defn check-library
    "Checks a library for new tags and builds them if needed"
    [library]
    (println (format "\nChecking library '%s'" (:name library)))
    (doseq [tag (unpackaged-tags library)]
        (build-package library tag)))

(defn check-libraries
    "Checks all configured libraries for new packages"
    []
    (doseq [library (config :libraries) ]
        (check-library library)))

(defn -main[& args]
    (check-binaries required-binaries)
    (let [config-file (first args)]
        (load-file config-file)
        (check-libraries)
        (shutdown-agents)))

