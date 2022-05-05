(ns eugene.bakisov.scramblies.web.routes.api
  (:require
   [eugene.bakisov.scramblies.web.controllers.health :as health]
   [eugene.bakisov.scramblies.web.controllers.scramble :as scramble]
   [eugene.bakisov.scramblies.web.middleware.exception :as exception]
   [eugene.bakisov.scramblies.web.middleware.formats :as formats]
   [integrant.core :as ig]
   [malli.experimental.lite :as l]
   [reitit.coercion.malli :as malli]
   [reitit.ring.coercion :as coercion]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.ring.middleware.parameters :as parameters]
   [reitit.swagger :as swagger]))

(defn api-routes [_opts]
  [["/swagger.json"
    {:get {:no-doc  true
           :swagger {:info {:title "Scramblies API"}}
           :handler (swagger/create-swagger-handler)}}]
   ["/health"
    {:get health/healthcheck!}]
   ["/scramble"
    {:post {:handler    scramble/scramble?
            :parameters {:body {:source-string string?
                                :target-string string?}}
            :responses  {200 {:body {:result boolean?}}
                         400 {:body {:reasons (l/vector string?)}}}}}]])

(defn route-data
  [opts]
  (merge
    opts
    {:coercion   malli/coercion
     :muuntaja   formats/instance
     :swagger    {:id ::api}
     :middleware [;; query-params & form-params
                  parameters/parameters-middleware
                  ;; content-negotiation
                  muuntaja/format-negotiate-middleware
                  ;; encoding response body
                  muuntaja/format-response-middleware
                  ;; exception handling
                  coercion/coerce-exceptions-middleware
                  ;; decoding request body
                  muuntaja/format-request-middleware
                  ;; coercing response bodys
                  coercion/coerce-response-middleware
                  ;; coercing request parameters
                  coercion/coerce-request-middleware
                  ;; exception handling
                  exception/wrap-exception]}))

(derive :reitit.routes/api :reitit/routes)

(defmethod ig/init-key :reitit.routes/api
  [_ {:keys [base-path]
      :or   {base-path ""}
      :as   opts}]
  [base-path (route-data opts) (api-routes opts)])
