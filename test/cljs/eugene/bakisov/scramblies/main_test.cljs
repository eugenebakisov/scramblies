(ns eugene.bakisov.scramblies.main-test
  (:require
   [cljs.test                        :refer [deftest is] :include-macros true]
   [day8.re-frame.test               :as rf.test :include-macros true]
   [eugene.bakisov.scramblies.events :as events]
   [eugene.bakisov.scramblies.subs   :as subs]
   [re-frame.core                    :as rf]
   [re-frame.db                      :as rdb]))

(defn- with-setup [f]
  (rf.test/run-test-sync
   (rf/dispatch [::events/initialize-db])
   (f)))

(cljs.test/use-fixtures :once with-setup)

(deftest main-test
  (let [valid-input? (rf/subscribe [::subs/valid-input?])
        loading?     (rf/subscribe [::subs/loading?])
        set-target   #(rf/dispatch [::events/change-by-id :target-string %])
        set-source   #(rf/dispatch [::events/change-by-id :source-string %])]
    (is @valid-input?)
    (is (not @loading?))

    (set-target "test")
    (is @valid-input?)
    (is (not @loading?))

    (set-target "testT")
    (is (not @valid-input?))
    (is (not @loading?))

    (set-target "test")
    (set-source "source")
    (let [db                    @rdb/app-db
          {db'     :db
           http-fx :http-xhrio} (events/do-submit {:db db} nil)
          _                     (reset! rdb/app-db db')]
      (is @loading?)
      (is (= (:params http-fx) {:target-string "test"
                                :source-string "source"})))))
