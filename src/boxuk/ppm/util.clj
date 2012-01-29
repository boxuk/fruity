
(ns boxuk.ppm.util
    (:use boxuk.ppm.config)
    (:require [clojure.java.io :as io]
              [clojure.string :as string]))

(defn- split-versions
    "Split a version string into parts"
    [version]
    (if (nil? version)
        '[0 0 0]
        (string/split version #"\.")))

(defn- version-pairs
    "Breaks 2 version number strings into interleaved pairs"
    [current-version next-version]
    (map
        (fn [x y] (list (Integer/parseInt x) (Integer/parseInt y)))
        (split-versions current-version)
        (split-versions next-version)
    ))

(defn- exec
    "Execute a command and return lines of output"
    [command dir]
    (let [process (.. Runtime getRuntime (exec (str command) nil
                                         (java.io.File. dir)))]
        (line-seq (io/make-reader (.getInputStream process) {}))))

(defn- debug
    "Debug output to commands"
    [lines]
    (if (config :debug)
        (doseq [line lines]
            (println line))))

;; Public

(defn run-command 
    "Execute a command and return a sequence of lines of output"
    ([command] (run-command command "."))
    ([command dir] 
        (println "# " command " (dir: '" dir "')")
        (let [lines (exec command dir)]
            (debug lines)
            lines)))

(defn run-commands
    "Run a series of commands"
    [& commands]
    (doseq [command commands]
        (run-command command)))

(defn later-version?
    "Indicates if next-version is a greater version than current-version"
    [current-version next-version]
    (let [pairs (version-pairs current-version next-version)]
        (loop [[x y] (first pairs) more (rest pairs)]
            (cond
                (or (nil? x) (nil? y)) false
                (> y x) true
                (< y x) false
                :default (recur (first more) (rest more))))))

