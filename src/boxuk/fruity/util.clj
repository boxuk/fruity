
(ns boxuk.fruity.util
    (:use boxuk.fruity.config
          clojure.java.shell)
    (:require [clojure.string :as string]))

;; Public

(defn sh-seq
    "Run the command and return seq of lines"
    [args]
    (let [output (:out (apply sh args))
          lines (string/split output #"\n")]
        (if (config :debug) (println output))
        lines))

(defn sh-str
    "Run the given command string, and return seq of lines"
    [cmd]
    (println cmd)
    (apply sh-seq [(string/split cmd #"\s+")]))

