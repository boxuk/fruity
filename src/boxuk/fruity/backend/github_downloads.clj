
(ns boxuk.fruity.backend.github-downloads
    "Backend to push build artifacts to Github downloads
        { :type :github-downloads
          :make 'ant dist'
          :archive 'package.tgz' }
    :make is the command to build the archive, and :archive
    is the name of the built archive to upload."
    (:use boxuk.fruity.backend
          boxuk.fruity.util
          clojure.java.shell
          jithub.core)
    (:require [jithub.repos.downloads :as dl]))

;; Downloads

(defn- download-version
    "Parses a version string from a download name, or nil"
    [info]
    (second (re-matches #"^.+-([\d\.]*\d+).+$" (:name info))))

(defn- download-versions
    "Return all version numbers of current downloads"
    [library]
    (filter (complement nil?)
            (map download-version (dl/all))))

(defn- jithub-repo
    "Create a jithub repo map for the library"
    [{:keys [user name password]}]
    { :user user
      :name name
      :password password })

;; Public

(defmethod latest-version :github-downloads
    [library]
    (with-repo (jithub-repo library)
        (let [versions (download-versions library)]
            (if (empty? versions) "0.0.0"
                (latest-version versions)))))

(defmethod deploy :github-downloads
    [library tag]
    (with-sh-dir
        (sh-str (:make library))))

