
(ns boxuk.fruity.test.config
    (:use boxuk.fruity.config
          clojure.test))

(deftest test-github-project
    (let [project (github "boxuk" "inject")]
        (is (= "inject" (:name project)))
        (is (= :git (:scm project)))
        (is (= :pear (:type project)))
        (is (= "https://github.com/boxuk/inject" (:url project)))))

(deftest test-github-project-with-different-repo
    (let [project (github "boxuk" "inject" {:url "https://github.com/boxuk/boxuk-di"})]
        (is (= "inject" (:name project)))
        (is (= "https://github.com/boxuk/boxuk-di" (:url project)))))

