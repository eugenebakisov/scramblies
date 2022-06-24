(ns eugene.bakisov.scramblies.domain.final
  "Final implementation.
  If we can be 100% sure we are not going to support other characters
  except for mentioned in the task description, we can use vector-based distributions instead of map-based
  N'th element would represent occurrence of N'th character after `a`"
  (:require
   [eugene.bakisov.scramblies.domain.common :as common]))

(set! *warn-on-reflection* true)

(def amount-of-chars (inc (count (range (int \a) (int \z)))))
(def empty-distribution (vec (take amount-of-chars (repeat 0))))

(defn chars-distribution
  [str]
  (->> (reduce (fn [acc char]
                 (let [index (- (int char) (int \a))]
                   (assoc! acc index (inc (get acc index)))))
               (transient empty-distribution)
               str)
       persistent!))

(defn distribution-includes?
  "Returns true if first distribution includes second one.
  In other words: if every key in second distribution had less or equal occurrences than in first distribution."
  [distr other-distr]
  (->> (map < distr other-distr)
       (filter true?)
       empty?))

(defn scramble?
  "Returns true if a portion of 'source' characters can be rearranged to match 'target'"
  [source target]
  (and (>= (count source) (count target))
       (common/scramble-fn chars-distribution distribution-includes? source target)))
