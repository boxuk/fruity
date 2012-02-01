
(ns boxuk.fruity.scm.svn
    (:use boxuk.fruity.scm
          boxuk.fruity.util
          clojure.java.shell))

(defmethod tags :svn
    [library]
    (tags-from (format "svn ls %s/tags" (:url library))
               #"(\d+\.\d+\.\d+)/"))

(defmethod checkout :svn
    [library tag]
    (checkout-for library (str "svn co %s/tags/" tag " %s")))

