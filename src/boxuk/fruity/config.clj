
(ns boxuk.fruity.config)

(def ^:dynamic data)

(defn github
    "Create a library config for Github"
    ([user project] (github user project {}))
    ([user project config]
        (let [defaults { :name project
                         :scm :git
                         :type :pear
                         :url (format "https://github.com/%s/%s" user project) }]
              (merge defaults config))))

(defn config
    "Get a configuration property"
    [name]
    (get data name))

