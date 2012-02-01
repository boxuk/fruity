
(ns boxuk.fruity.library.git
    (:use boxuk.fruity.library
          boxuk.fruity.util
          clojure.java.shell))

(defmethod tags :git
    [library]
    (tags-from (format "git ls-remote --tags %s" (:url library))
               #".*v(\d+\.\d+.\d+)$"))

(defmethod checkout :git
    [library tag]
    (checkout-library library "git clone %s %s")
    (with-sh-dir "build/repo"
        (sh-str (str "git checkout -q v" tag))))

