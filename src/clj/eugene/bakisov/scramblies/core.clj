(ns eugene.bakisov.scramblies.core
  (:require
   [clojure.tools.logging :as log]
   [eugene.bakisov.scramblies.config :as config]
   [eugene.bakisov.scramblies.env :refer [defaults]]
   [eugene.bakisov.scramblies.web.handler]
   [eugene.bakisov.scramblies.web.routes.api]
   [eugene.bakisov.scramblies.web.routes.pages]
   [eugene.bakisov.scramblies.web.server]
   [integrant.core :as ig])
  (:gen-class))

;; log uncaught exceptions in threads
(Thread/setDefaultUncaughtExceptionHandler
  (reify Thread$UncaughtExceptionHandler
    (uncaughtException [_ thread ex]
      (log/error {:what :uncaught-exception
                  :exception ex
                  :where (str "Uncaught exception on" (.getName thread))}))))

(defonce system (atom nil))

(defn stop-app []
  ((or (:stop defaults) (fn [])))
  (some-> (deref system) (ig/halt!))
  (shutdown-agents))

(defn start-app [& [params]]
  ((or (:start params) (:start defaults) (fn [])))
  (->> (config/system-config (or (:opts params) (:opts defaults) {}))
       (ig/prep)
       (ig/init)
       (reset! system))
  (.addShutdownHook (Runtime/getRuntime) (Thread. ^java.lang.Runnable stop-app)))

(defn -main [& _]
  (start-app))
