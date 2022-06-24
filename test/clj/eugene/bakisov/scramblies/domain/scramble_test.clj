(ns eugene.bakisov.scramblies.domain.scramble-test
  (:require [eugene.bakisov.scramblies.domain.final :as sut3]
            [eugene.bakisov.scramblies.domain.first :as sut1]
            [eugene.bakisov.scramblies.domain.second :as sut2]
            [clojure.test :refer [are deftest]]))

(deftest scramble?
  (are [scramble-fn]
      (do (are [s t]
              (scramble-fn s t)
            "" ""
            "a" "a"
            "aa" "a"
            "aab" "ab"
            "abccca" "aba"
            "abccca" "aab")
          (are [s t]
              (not (scramble-fn s t))
            ""   "a"
            "b"  "a"
            "a"  "aa"
            "ab" "abc"))
    sut1/scramble?
    sut2/scramble?
    sut3/scramble?))
