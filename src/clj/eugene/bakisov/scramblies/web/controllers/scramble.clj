(ns eugene.bakisov.scramblies.web.controllers.scramble
  (:require
   [eugene.bakisov.scramblies.domain.final :as scramble]
   [ring.util.http-response :as http-response]))

(defn valid-input-string?
  [s]
  (re-matches #"[a-z]*" s))

(defn scramble?
  [{{:keys [source-string target-string]} :body-params :as _req}]
  (if (and (valid-input-string? source-string)
           (valid-input-string? target-string))
    (http-response/ok {:result (scramble/scramble? source-string target-string)})
    (http-response/bad-request {:reasons ["found illegal character in provided string, legal characters are: [a-z]"]})))
