(ns eugene.bakisov.scramblies.subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub
 ::result
 (fn [db]
   (:result db)))

(rf/reg-sub
 ::loading?
 (fn [db]
   (:loading? db)))

(rf/reg-sub
 ::error
 (fn [db]
   (:error db)))

(rf/reg-sub
 ::value-by-id
 (fn [db [_ id]]
   (get db id)))

(defn valid-string?
  [s]
  (and (string? s) (re-matches #"[a-z]*" s)))

(rf/reg-sub
 ::valid-by-id?
 (fn [[_ id] _]
   (rf/subscribe [::value-by-id id]))
 valid-string?)

(rf/reg-sub
 ::valid-input?
 :<- [::valid-by-id? :target-string]
 :<- [::valid-by-id? :source-string]
 (fn [[t s]]
   (and t s)))
