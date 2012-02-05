
(ns boxuk.fruity.backend.github
    "Backend to push build artifacts to Github downloads
        { :type :github-downloads
          :make 'ant dist'
          :archive 'package.tgz' }
    :make is the command to build the archive, and :archive
    is the name of the built archive to upload."
    (:use boxuk.fruity.backend
          cheshire.core)
    (:require [clojure.java.io :as io]))

(defn- downloads
    "Returns downloads for the specified library"
    [{:keys [user name]}]
    (let [url (format "https://api.github.com/repos/%s/%s/downloads" user name)]
        (parse-stream (io/reader url) true)))

(defn- parse-version
    [info]
    (second (re-matches #"^.+-([\d\.]*\d+).+$" (:name info))))

(defn- download-versions
    "Return all version numbers of current downloads"
    [library]
    (filter (complement nil?)
            (map parse-version (downloads library))))

;; Public

(defmethod latest-version :github-downloads

