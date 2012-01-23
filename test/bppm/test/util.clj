
(ns bppm.test.util
    (:use [bppm.util]
          [clojure.test]))
    
(deftest test-later-version?
    (is (later-version? "1.0.0" "1.0.1"))
    (is (later-version? "1.0.0" "2.0.1"))
    (is (later-version? "1.1.0" "1.2.1"))
    (is (not (later-version? "1.0.1" "1.0.0")))
    (is (not (later-version? "2.0.1" "2.0.0")))
    (is (not (later-version? "1.2.1" "1.1.0"))))

