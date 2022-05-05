(ns eugene.bakisov.scramblies.domain.first
  "Most naive and straigforward first implementation"
  (:require
   [eugene.bakisov.scramblies.domain.common :as common]))

(set! *warn-on-reflection* true)

(defn chars-distribution
  "Awesome function to count character distribution from a given string s
  (This is silly because I for some reason forgot about `frequencies` at that point in time)"
  [s]
  (reduce (fn [acc char]
            (update acc char (fnil inc 0)))
          {}
          s))

(defn distribution-includes?
  "Returns true if first distribution includes second one.
  In other words: if every key in second distribution had less or equal occurences than in first distribution."
  [distr other-distr]
  (let [char-has-more-occurences? (fn [[k freq]]
                                    (let [char-distr (get distr k 0)]
                                      (> freq char-distr)))]
    (->> other-distr
         (filter char-has-more-occurences?)
         some?)))

(defn scramble?
  "Returns true if a portion of 'source' characters can be rearranged to match 'target'"
  [source target]
  (and (>= (count source) (count target))
       (common/scramble-fn chars-distribution distribution-includes? source target)))
