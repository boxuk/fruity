
(ns boxuk.fruity.pear
    (use boxuk.fruity.util))

(defn- package-info
    "Parse package info from pear channel string"
    [acc string]
    (let [regexp #"^.*?/(.*?)\s+(\d+\.\d+\.\d+).*"
          [name version] (rest (re-matches regexp string))]
        (assoc acc (keyword name) version)))

(defn- channel-checkout
    "Checkout the PEAR channel"
    [pear]
    (sh-str "rm -rf build/pear")
    (sh-str "mkdir -p build")
    (sh-str (format "svn co %s build/pear" (:repoUrl pear))))

(defn- channel-add-files
    "Add any unknown files in the channel repo"
    []
    (let [lines (sh-str "svn st build/pear")
          unknownFiles (filter #(re-matches #"^\\?(.*)" %) lines)]
        (doseq [file unknownFiles]
            (sh-str (format "svn add %s" (.substring file 2))))))

(defn- channel-commit
    "Commit the built package to the channel"
    [library tag]
    (sh-str (format "pirum add build/pear build/repo/%s-%s.tgz" (:name library) tag))
    (channel-add-files)
    (sh-str (format "svn ci -m '%s-%s' build/pear" (:name library) tag)))

;; Public

(defn channel-info
    "Fetch package information for the specified channel"
    [channel]
    (sh-str "pear clear-cache")
    (let [command (format "pear list-all -c %s" channel)]
        (reduce package-info {}
            (drop 3 (sh-str command)))))

(defn commit-package
    "Commit a package that has been built"
    [pear library tag]
    (channel-checkout pear)
    (channel-commit library tag))

