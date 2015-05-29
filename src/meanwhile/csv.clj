(ns meanwhile.csv
  (:require   [clojure-csv.core :refer [parse-csv]]
              [clojure.walk :as cw]))

(def csv (parse-csv (slurp "resources/mediashort.csv")))

(def csv-map (reduce conj {} csv))

(def csv-keywordized (cw/keywordize-keys csv-map))