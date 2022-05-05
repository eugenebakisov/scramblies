(ns eugene.bakisov.scramblies.env
  (:require
    [clojure.tools.logging :as log]
    [eugene.bakisov.scramblies.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[ starting using the development profile ]=-"))
   :started    (fn []
                 (log/info "\n-=[ started successfully using the development profile ]=-"))
   :stop       (fn []
                 (log/info "\n-=[ has shut down successfully ]=-"))
   :middleware wrap-dev
   :opts       {:profile       :dev
                :persist-data? true}})
