(ns user
  (:require
   [clojure.pprint]
   [clojure.spec.alpha :as s]
   [clojure.tools.namespace.repl :as repl]
   [criterium.core :as c]
   [eugene.bakisov.scramblies.core :refer [start-app stop-app]]
   [expound.alpha :as expound]
   [integrant.core :as ig]
   [integrant.repl :refer [clear go halt init prep reset reset-all]]
   [integrant.repl.state :as state]
   [lambdaisland.classpath.watch-deps :as watch-deps]))

(watch-deps/start! {:aliases [:dev :test :shadow-cljs]})

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(add-tap (bound-fn* clojure.pprint/pprint))

(defn dev-prep!
  []
  (integrant.repl/set-prep! (fn []
                              (-> (eugene.bakisov.scramblies.config/system-config {:profile :dev})
                                  (ig/prep))))
  )

(defn test-prep!
  []
  (integrant.repl/set-prep! (fn []
                              (-> (eugene.bakisov.scramblies.config/system-config {:profile :test})
                                  (ig/prep)))))


(dev-prep!)

(repl/set-refresh-dirs "src/clj")

(def refresh repl/refresh)

(comment
  (go)
  (reset))
