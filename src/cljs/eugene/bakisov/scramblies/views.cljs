(ns eugene.bakisov.scramblies.views
  (:require
   [eugene.bakisov.scramblies.events :as events]
   [eugene.bakisov.scramblies.subs :as subs]
   [re-frame.core :as rf]))

(defn string-input
  [id name disabled?]
  (let [value  @(rf/subscribe [::subs/value-by-id id])
        valid? @(rf/subscribe [::subs/valid-by-id? id])]
    [:div
     [:label {:for id :class "block text-sm font-medium text-gray-700"} name]
     [:div {:class "mt-1"}
      [:input {:id        (str id)
               :name      (str id)
               :type      "text"
               :required  true
               :class     (str (if valid?
                                 "focus:border-gray-300 focus:ring-gray-300 border-gray-300"
                                 "focus:border-red-400 focus:ring-red-400 border-red-400")
                               " appearance-none block w-full px-3 py-2 rounded-md shadow-sm placeholder-gray-400 focus:outline-none sm:text-sm")
               :value     value
               :disabled  disabled?
               :on-change (fn [e]
                            (let [value (-> e (.-target) (.-value))]
                              (rf/dispatch [::events/change-by-id id value])))}]
      (when-not valid?
        [:label {:class "alert text-sm block text-red-700"} "only lowercase letters allowed"])]]))

(defn error-banner
  [error]
  [:div {:class "bg-red-100 border text-center border-red-400 text-red-700 px-4 py-3 rounded relative" :role "alert"}
   [:span {:class "block sm:inline"} error]
   [:span {:class "absolute top-0 bottom-0 right-0 px-4 py-3"}
    [:svg {:class "fill-current h-6 w-6 text-red-500"
           :role :button
           :xmlns "http://www.w3.org/2000/svg"
           :viewBox "0 0 20 20"
           :on-click #(rf/dispatch [::events/clear-error])}
     ;; x cross:
     [:path {:d "M14.348 14.849a1.2 1.2 0 0 1-1.697 0L10 11.819l-2.651 3.029a1.2 1.2 0 1 1-1.697-1.697l2.758-3.15-2.759-3.152a1.2 1.2 0 1 1 1.697-1.697L10 8.183l2.651-3.031a1.2 1.2 0 1 1 1.697 1.697l-2.758 3.152 2.758 3.15a1.2 1.2 0 0 1 0 1.698z"}]]]])

(defn submitt-btn []
  (let [valid-input? @(rf/subscribe [::subs/valid-input?])
        loading?     @(rf/subscribe [::subs/loading?])]
    [:div
     [:button
      {:type     "submit"
       :on-click #(rf/dispatch [::events/submit])
       :disabled (not valid-input?)
       :class
       (str (if (or (not valid-input?) loading?)
              "bg-gray-300"
              "bg-green-600 hover:bg-green-700 focus:ring-2 focus:ring-offset-2 focus:ring-green-500")
            " w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white focus:outline-none")}
      (if loading?
        "Waiting..."
        "Submit")]]))

(defn result []
  (let [result @(rf/subscribe [::subs/result])]
    [:div
     [:label {:id "result-label" :class "text-center block text-sm font-medium text-gray-700"}
      "Can target string be formed out of source's letters?"]
     [:label {:id "result" :class "text-center block text-sm font-medium text-gray-700"}
      result]]))

(defn main []
  []
  (let [loading?     @(rf/subscribe [::subs/loading?])
        error        @(rf/subscribe [::subs/error])]
    [:div {:class "min-h-screen bg-gray-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8"}
     [:div {:class "sm:mx-auto sm:w-full sm:max-w-md"}
      [:h2 {:class "mt-6 text-center text-3xl font-extrabold text-gray-900"} "Scramblies"]
     (when error [error-banner error])]
     [:div {:class "mt-8 sm:mx-auto sm:w-full sm:max-w-md"}
      [:div {:class "bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10"}
       [:div {:class "space-y-6"}
       [string-input :source-string "Source string"  loading?]
       [string-input :target-string "Target string" loading?]
        [result]
        [submitt-btn]]]]]))
