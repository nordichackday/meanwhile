(ns meanwhile.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [cheshire.core :as ch]
            [clj-http.client :as client]
            [clojure.data.json :as json]
            [clojure.walk :as cw]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [compojure.handler :as handler] ; form, query params decode; cookie; session, etc
            [ring.middleware.json :as middleware]
            [ring.middleware.jsonp :refer [wrap-json-with-padding]]
            [meanwhile.csv :as csv]))

(defn- json-response
  [data]
  {:status  200
   :headers {}
   :body    (ch/generate-string data)})

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

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/archive/:decade/:genre" [decade genre] (json-response (get-haku-data decade genre)))
  (GET "/archive/:decade" [decade] (json-response (get-haku-data decade "")))
  (GET "/video" [] (client/get "http://yle.fi/aihe/artikkeli/2015/04/23/holmbergin-jaakarin-morsian-kohahdutti-maltillisuudellaan"))
  (route/not-found "Not Found"))

(defn middleware [routes]
  (-> routes
    wrap-json-with-padding
    handler/site))

(def app
  (middleware app-routes))

