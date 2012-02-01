
(ns boxuk.fruity.scm.git
    (:use boxuk.fruity.scm
          boxuk.fruity.util
          clojure.java.shell))

(defmethod tags :git
    [library]
    (tags-from (format "git ls-remote --tags %s" (:url library))
               #".*v(\d+\.\d+.\d+)$"))

(defmethod checkout :git
    [library tag]
    (checkout-for library "git clone %s %s")
    (with-sh-dir "build/repo"
        (sh-str (str "git checkout -q v" tag))))

