
(ns boxuk.fruity.config)

(def ^:dynamic data)

(defn github
    "Create a library config for Github"
    ([user project] (github user project project))
    ([user project repo]
        {:name project
         :type :git
         :url (format "https://github.com/%s/%s" user repo)
         :packageCommand "phing pear-package -Dversion=$VERSION"}))

(defn config
    "Get a configuration property"
    [name]
    (get data name))

