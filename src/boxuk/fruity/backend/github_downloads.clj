
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
          cheshire.core)
    (:require [clojure.java.io :as io]
              [clj-http.client :as http])
    (:import [java.io File]
             [java.net URLConnection]))

;; Downloads

(defn- api-url
    [{:keys [user name]}]
    (format "https://api.github.com/repos/%s/%s/downloads" user name))

(defn- downloads
    "Returns downloads for the specified library"
    [library]
    (parse-stream (io/reader (api-url library)) true))

(defn- download-version
    "Parses a version string from a download name, or nil"
    [info]
    (second (re-matches #"^.+-([\d\.]*\d+).+$" (:name info))))

(defn- download-versions
    "Return all version numbers of current downloads"
    [library]
    (filter (complement nil?)
            (map download-version (downloads library))))

;; Uploads

(defn- upload-request-params
    "Parameters for the first step in file upload"
    [library file]
    (let [name (.getName file)]
        { :basic-auth (:basic-auth library)
          :body (generate-string {
              :name name
              :size (.length file)
              :description name
              :content_type (URLConnection/guessContentTypeFromName name)
          })
        }))

(defn- upload-request
    "Fetches upload request parameters, for saving to S3"
    [library file]
    (http/post (api-url library)
        (upload-request-params library file)))

(defn- upload-s3-params
    "Parameters for uploading to S3"
    [file req]
    (let [name (.getName file)]
        (into (sorted-map) {
          :key (:path req)
          :acl (:acl req)
          :success_action_status "201"
          :Filename (:name req)
          :AWSAccessKeyId (:accesskeyid req)
          :Policy (:policy req)
          :Signature (:signature req)
          :Content-Type (:mime_type req)
          :file (slurp file)
        })))

(defn- upload-s3
    "Upload a file to S3"
    [file req]
    (println (str "S3: " (upload-s3-params file req)))
    (http/post "https://github.s3.amazonaws.com"
        { :form-params (upload-s3-params file req) }))

(defn- upload
    "Upload a file to Github"
    [library]
    (let [file (File. (:archive library))
          req (upload-request library file)
          params (parse-string (:body req) true)]
              (upload-s3 file params)))

;; Public

(defmethod latest-version :github-downloads
    [library]
    (let [versions (download-versions library)]
        (if (empty? versions) "0.0.0"
            (latest-version versions))))

(defmethod deploy :github-downloads
    [library tag]
    (with-sh-dir
        (sh-str (:make library))))

