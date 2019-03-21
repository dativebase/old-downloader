(ns old-downloader.core
  "Downloads all of the data from an OLD using its HTTP API."
  (:gen-class)
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [cheshire.core :refer :all]
   [inflections.core :refer [plural]]
   [old-client.core :refer [make-old-client oc-get path-to-url]]
   [old-client.resources :refer [rsrc-kw->plrl]]
   [old-downloader.utils :refer [strip]]))

(defn get-next-paginator
  [{:keys [items_per_page page count]}]
  (let [next-index (inc (* items_per_page page))]
    (when (< next-index count)
      {:items_per_page items_per_page :page (inc page) :count count})))

(defn get-old-resource-string
  [resource-kw]
  (let [ret (rsrc-kw->plrl resource-kw)]
    (if (= ret "corpus")
      "corpora"
      ret)))

(defn fetch-resources
  "Fetch all resources at subpath ``resource``, making multiple requests if needed."
  [old-client resource & {:keys [ipp p items] :or {ipp 100 p 1 items []}}]
  (let [{:keys [paginator items]}
        (oc-get
         old-client
         resource
         {:items_per_page ipp :page p})]
    (if-let [{:keys [items_per_page page]} (get-next-paginator paginator)]
      (concat items
              (fetch-resources old-client resource
                               :ipp items_per_page
                               :p page
                               :items items))
      items)))

(defn mkdir
  [dir-path]
  (.mkdir (java.io.File. dir-path)))

(def root "olds")

(defn mkdir-olds [] (mkdir root))

(def good-chars
  "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789./")

(defn timestamp
  []
  (quot (System/currentTimeMillis) 1000))

(defn get-download-dir-name
  "Given https://some.domain.edu/olds/someold/, returns
  https-some-domain-edu-olds-someold"
  [url]
  (let [cleaned
        (->> url
             (filter (fn [c] (some #{c} good-chars)))
             (apply str))
        prefix
        (-> cleaned
            (string/replace #"[/\.]+" "-")
            (strip "-"))]
    (str prefix "-" (timestamp))))

(defn get-download-dir-path
  [url]
  (str (io/file root (get-download-dir-name url))))

(defn mkdir-download
  [url]
  (let [path (get-download-dir-path url)]
    (mkdir path)
    path))

(def resource-kws
  [
   ;; :applicationsetting
   :collectionbackup
   :collection
   :corpus
   :corpusbackup
   :elicitationmethod
   :file
   :formbackup
   :form
   :formsearch
   :keyboard
   :language
   :morphemelanguagemodelbackup
   :morphemelanguagemodel
   :morphologicalparserbackup
   :morphologicalparser
   :morphology
   :morphologybackup
   :orthography
   :page
   :phonology
   :phonologybackup
   :source
   :speaker
   :syntacticcategory
   :tag
   :user])

(defn get-resource-json-path
  [resource dir-path]
  (str (io/file dir-path (str (-> resource name plural) ".json"))))

(defn fetch-persist
  [resource old-client path]
  (let [rsrc-plrl (get-old-resource-string resource)
        resources (seq (fetch-resources old-client rsrc-plrl))
        path (get-resource-json-path resource path)]
    (if resources
      (do
        (generate-stream resources (clojure.java.io/writer path) {:pretty true})
        (format "%s %s downloaded and stored at %s."
                (count resources) rsrc-plrl path))
      (format "There are no %s." rsrc-plrl))))

(defn download-old
  [url username password]
  (mkdir-olds)
  (let [old-client
        (make-old-client {:url url
                          :username username
                          :password password})
        path (mkdir-download url)]
    (for [resource resource-kws]
      (fetch-persist resource old-client path))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
