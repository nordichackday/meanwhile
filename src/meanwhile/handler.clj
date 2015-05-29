(ns meanwhile.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [cheshire.core :as ch]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [compojure.handler :as handler] ; form, query params decode; cookie; session, etc
            [ring.middleware.json :as middleware]
            [ring.middleware.jsonp :refer [wrap-json-with-padding]]
            [meanwhile.search :as search]))

(defn- json-response
  [data]
  {:status  200
   :headers {}
   :body    (ch/generate-string data)})

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/archive/:decade/:genre" [decade genre] (json-response (search/get-haku-data decade genre)))
  (GET "/archive/:decade" [decade] (json-response (search/get-haku-data decade "")))
  (route/not-found "Not Found"))

(defn middleware [routes]
  (-> routes
    wrap-json-with-padding
    handler/site))

(def app
  (middleware app-routes))

