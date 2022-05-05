(ns eugene.bakisov.scramblies.env
  (:require
    [clojure.tools.logging :as log]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[ starting using the test profile ]=-"))
   :started    (fn []
                 (log/info "\n-=[ started successfully using the test profile ]=-"))
   :stop       (fn []
                 (log/info "\n-=[ has shut down successfully ]=-"))
   :middleware (fn [handler _] handler)
   :opts       {:profile       :test}})
