
(ns boxuk.fruity.core
    (:gen-class)
    (:use boxuk.fruity.config
          boxuk.versions)
    (:require [boxuk.fruity.pear :as pear]
              [boxuk.fruity.library :as library]))

(defn build-package
    "Builds a specified package for a library"
    [library tag]
    (println (format "Build package '%s'" tag))
    (library/make-package library tag)
    (pear/commit-package (config :pear) library tag))

(defn latest-version
    "Fetch the latest version of the library in PEAR channel (or 0.0.0)"
    [library]
    (get (pear/channel-info (:alias (config :pear)))
         (keyword (:name library))
         "0.0.0"))

(defn unpackaged-tags
    "Fetch new tags for the specified library"
    [library]
    (filter (partial later-version? (latest-version library)) 
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
    (let [config-file (first args)]
        (load-file config-file)
        (check-libraries)
        (shutdown-agents)))

