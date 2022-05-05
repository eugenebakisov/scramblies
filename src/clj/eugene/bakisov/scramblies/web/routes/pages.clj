(ns eugene.bakisov.scramblies.web.routes.pages
  (:require
   [eugene.bakisov.scramblies.web.middleware.exception :as exception]
   [integrant.core :as ig]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.ring.middleware.parameters :as parameters]
   [ring.util.response :as response]))


(defn index [_req]
  (-> "index.html"
      (response/resource-response {:root "public"})
      (response/content-type "text/html; charset=utf-8")))

(defn page-routes [_opts]
  [["/" {:get index}]])

(defn route-data [opts]
  (merge
   opts
   {:middleware 
    [;; query-params & form-params
     parameters/parameters-middleware
     ;; encoding response body
     muuntaja/format-response-middleware
     ;; exception handling
     exception/wrap-exception]}))

(derive :reitit.routes/pages :reitit/routes)

(defmethod ig/init-key :reitit.routes/pages
  [_ {:keys [base-path]
      :or   {base-path ""}
      :as   opts}]
  [base-path (route-data opts) (page-routes opts)])
