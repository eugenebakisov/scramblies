(ns eugene.bakisov.scramblies.core
  (:require
   [eugene.bakisov.scramblies.events :as events]
   [eugene.bakisov.scramblies.views :as views]
   [re-frame.core :as rf]
   [reagent.dom :as d]))

(defn ^:after-load mount-root
  []
  (rf/clear-subscription-cache!)
  (d/render [views/main]
            (.getElementById js/document "app")))

(defn ^:export init!
  []
  (rf/dispatch-sync [::events/initialize-db])
  (mount-root))
