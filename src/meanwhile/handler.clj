(ns meanwhile.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clj-http.client :as client]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn- parse-int [s]
  (Integer. (re-find  #"\d+" s )))

(defn- get-decade [decade-start]
  (str decade-start "/" (+ (parse-int decade-start) 9)))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/archive/:decade" [decade] (client/get "http://haku.yle.fi/api/search" {:query-params {:category "elavaarkisto" :UILanguage "fi"
                                                                       :decade (get-decade decade) :media "video" :page "1"}}))
  (GET "/video" [] (client/get "http://yle.fi/aihe/artikkeli/2015/04/23/holmbergin-jaakarin-morsian-kohahdutti-maltillisuudellaan"))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
