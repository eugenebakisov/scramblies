(ns eugene.bakisov.scramblies.domain.scramble-test
  (:require [eugene.bakisov.scramblies.domain.final :as sut3]
            [eugene.bakisov.scramblies.domain.first :as sut1]
            [eugene.bakisov.scramblies.domain.second :as sut2]
            [clojure.test :refer [are deftest is]]
            [clojure.string :as str]))

(def amount-of-chars (inc (count (range (int \a) (int \z)))))

(defn rand-str
  [len]
  (let [rand-char #(char (+ (int \a) (rand amount-of-chars)))]
    (->> rand-char
         repeatedly
         (take len)
         (apply str))))

(defn next-char
  [c]
  (-> c
      int
      inc
      (mod amount-of-chars)
      (+ (int \a))
      char))

(defn generate-possible-target
  [s]
  (apply str (shuffle (seq s))))

(defn genetate-impossible-target
  [s]
  (let [first-char (get s 0)
        another-char (next-char first-char)]
    (str/replace-first s first-char another-char)))

(deftest scramble?
  (doseq [random-string (take 100 (repeatedly #(rand-str 10000)))]
    (let [possible-target (generate-possible-target random-string)
          impossible-target (genetate-impossible-target random-string)]
      (are [scramble-fn]
         (do (are [s t]
                 (scramble-fn s t)
               "" ""
               "a" "a"
               "aa" "a"
               "aab" "ab"
               "abccca" "aba"
               "abccca" "aab"
               "abcdef" "abcdef"
               random-string possible-target)
             (are [s t]
                 (not (scramble-fn s t))
               ""   "a"
               "b"  "a"
               "a"  "aa"
               "ab" "abc"
               "xxabcxx" "zzabczz"
               random-string impossible-target))
       sut1/scramble?
       sut2/scramble?
       sut3/scramble?))))
