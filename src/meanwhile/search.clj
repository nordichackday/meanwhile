(ns meanwhile.search
  (:require   [meanwhile.csv :as csv]
              [clojure.data.json :as json]
              [clojure.walk :as cw]
              [clj-http.client :as client]))

(defn get-video-id [article-id]
  ((keyword article-id) csv/csv-keywordized))

(defn- parse-int [s]
  (Integer. (re-find  #"\d+" s )))

(defn- get-decade [decade-start]
  (str decade-start "/" (+ (parse-int decade-start) 9)))

(defn add-media-id [result]
  (let [video-id (get-video-id (:articleId result))
        result-with-media (assoc result :mediaId video-id)]
    (println result-with-media)
    result-with-media))

(defn get-haku-data [decade genre]
  (let [haku-data (:body (client/get "http://haku.yle.fi/api/search" {:query-params {:category "elavaarkisto" :UILanguage "fi"
                                                                                     :decade (get-decade decade) :media "video" :page "1" :keyword genre}}))
        haku-data-kw (cw/keywordize-keys (json/read-str haku-data))
        results (second (rest haku-data-kw))
        results-with-article-ids (second results)
        new-results (assoc {} :newresult (map #(add-media-id %) results-with-article-ids))]
    (println new-results)
    new-results))