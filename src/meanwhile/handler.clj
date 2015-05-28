(ns meanwhile.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clj-http.client :as client]
            [clojure-csv.core :refer [parse-csv]]
            [clojure.data.json :as json]
            [clojure.walk :as cw]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(def csv (parse-csv (slurp "/home/leena/github/meanwhile/resources/mediashort.csv")))

(def csv-map (reduce conj {} csv))

(def csv-keywordized (cw/keywordize-keys csv-map))

(defn get-video-id [article-id]
  ((keyword article-id) csv-keywordized))

(defn- parse-int [s]
  (Integer. (re-find  #"\d+" s )))

(defn- get-decade [decade-start]
  (str decade-start "/" (+ (parse-int decade-start) 9)))

(defn get-haku-data [decade]
  (let [haku-data (:body (client/get "http://haku.yle.fi/api/search" {:query-params {:category "elavaarkisto" :UILanguage "fi"
                                                              :decade (get-decade decade) :media "video" :page "1"}}))
        haku-data-kw (cw/keywordize-keys (json/read-str haku-data))
        results (second (rest haku-data-kw))]
    (println results)))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/archive/:decade" [decade] (get-haku-data decade))
  (GET "/video" [] (client/get "http://yle.fi/aihe/artikkeli/2015/04/23/holmbergin-jaakarin-morsian-kohahdutti-maltillisuudellaan"))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
