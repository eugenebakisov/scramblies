(ns eugene.bakisov.scramblies.test-utils
  (:require [cheshire.core :as cheshire]
            [eugene.bakisov.scramblies.core :as core]
            [integrant.repl.state :as state]
            [ring.mock.request :as mock.req]))

(defn system-state
  []
  (or @core/system state/system))

(defn system-fixture
  []
  (fn [f]
    (when (nil? (system-state))
      (core/start-app {:opts {:profile :test}}))
    (f)))

(defn provide-handler []
  (:handler/ring (system-state)))

(defmulti decode-response-body (fn [body] (type body)))
(defmethod decode-response-body :default [body] body)
(defmethod decode-response-body java.io.ByteArrayInputStream
  [^java.io.ByteArrayInputStream bais]
  (-> bais
      java.io.InputStreamReader.
      cheshire/decode-stream))

(defn make-request
  ([method uri]
   (-> (mock.req/request method uri)
       ((provide-handler))
       (update :body decode-response-body)))
  ([method uri params]
   (-> (mock.req/request method uri)
       (mock.req/json-body params)
       ((provide-handler))
       (update :body decode-response-body))))
