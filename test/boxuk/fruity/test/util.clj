
(ns boxuk.fruity.test.util
    (:use boxuk.fruity.util
          clojure.test))

(deftest test-running-command-as-string
    (is (vector? (sh-str "ls"))))

