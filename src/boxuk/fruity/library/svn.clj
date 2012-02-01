
(ns boxuk.fruity.library.svn
    (:use boxuk.fruity.library
          boxuk.fruity.util
          clojure.java.shell))

(defmethod tags :svn
    [library]
    (tags-from (format "svn ls %s/tags" (:url library))
               #"(\d+\.\d+\.\d+)/"))

(defmethod checkout :svn
    [library tag]
    (checkout-library library (str "svn co %s/tags/" tag " %s")))

