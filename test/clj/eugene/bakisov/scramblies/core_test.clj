(ns eugene.bakisov.scramblies.core-test
  (:require [clojure.test :refer [are deftest is testing use-fixtures]]
            [eugene.bakisov.scramblies.test-utils :as utils]
            [matcho.core :as m]))

(use-fixtures
  :once
  (utils/system-fixture))

(deftest health-endpoint
  (testing "is working"
    (m/assert {:status 200} (utils/make-request :get "/api/health"))))

(deftest scramble-endpoint
  (let [req (partial utils/make-request :post "/api/scramble")]
    (testing "is working"
      (m/assert {:status 200} (req {:source-string "" :target-string ""})))

    (testing "is not accepting a non-string inputs"
      (are [incorrect]
          (are [f s]
              (let [payload {:source-string f :target-string s}]
                (testing payload (m/assert {:status 400} (req payload))))
            "" incorrect
            incorrect ""
            incorrect incorrect)
        nil 1 [] {:a :b}))

    (testing "is providing a reasons for a bad request"
      (is (m/valid? {:status 400
                     :body   {"reasons" (partial every? string?)}}
                    (req {:source-string "A" :target-string ""}))))))
