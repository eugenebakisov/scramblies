(ns eugene.bakisov.scramblies.events
  (:require
   [ajax.core :as ajax]
   [day8.re-frame.http-fx]
   [re-frame.core :as rf]))

(rf/reg-event-db
 ::initialize-db
 (constantly {:source-string ""
              :target-string ""
              :result "(submit two strings to get the result)"
              :error "Warning! This is a toy side project."}))

(rf/reg-event-db
 ::change-by-id
 (fn [db [_ id value]]
   (-> db
       (dissoc :result)
       (assoc id value))))

(rf/reg-event-db
 ::clear-error
 (fn [db _]
   (dissoc db :error)))

(rf/reg-event-db
 ::on-submit-success
 (fn [db [_ {:keys [result] :as _response}]]
   (-> db
       (dissoc :loading?)
       (assoc :result (if result "Yes!" "No.")))))

(rf/reg-event-db
 ::on-submit-failure
 (fn [db [_ failure]]
   ((.-error js/console) failure)
   (-> db
       (dissoc :loading?)
       (assoc :error (:status-text failure)))))

;; could've used re-frame-http-fx-alpha or ingesolvoll/glimt for much smoother http fx:
(defn do-submit
  [{:keys [db]} _]
  {:db         (assoc db :loading? true)
   :http-xhrio {:method          :post
                :params          (select-keys db [:target-string :source-string])
                :uri             "/api/scramble"
                :timeout         5000
                :format          (ajax/json-request-format)
                :response-format (ajax/json-response-format {:keywords? true})
                :on-success      [::on-submit-success]
                :on-failure      [::on-submit-failure]}})

(rf/reg-event-fx
 ::submit
 do-submit)
