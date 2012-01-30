
(ns boxuk.fruity.config)

(def ^:dynamic data)

(defn github
    "Create a library config for Github"
    ([user project] (github user project project))
    ([user project name]
        {:name name
         :type :git
         :url (format "https://github.com/%s/%s" user project)
         :packageCommand "phing pear-package -Dversion=$VERSION"}))

(defn config
    "Get a configuration property"
    [name]
    (get data name))

