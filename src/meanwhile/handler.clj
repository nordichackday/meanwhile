(ns meanwhile.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clj-http.client :as client]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/archive" [] (client/get " http://haku.yle.fi/api/search" {:query-params {:category "elavaarkisto" :UILanguage "fi"
                                                                       :decade "1970/1979" :media "video" :page "1"}}))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
