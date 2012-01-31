
(ns boxuk.fruity.config)

(def ^:dynamic data)

(defn github
    "Create a library config for Github"
    ([type user project] (github user project project))
    ([type user project repo]
        {:name project
         :scm :git
         :type type
         :url (format "https://github.com/%s/%s" user repo)
         :packageCommand "phing pear-package -Dversion=$VERSION"}))

(defn config
    "Get a configuration property"
    [name]
    (get data name))

