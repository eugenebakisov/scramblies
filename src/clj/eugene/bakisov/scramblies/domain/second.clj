(ns eugene.bakisov.scramblies.domain.second
  "Second implementation.
  We can use transient map for the distribution counting"
  (:require
   [eugene.bakisov.scramblies.domain.common :as common]
   [eugene.bakisov.scramblies.domain.first :as first]))

(set! *warn-on-reflection* true)

(defn scramble?
  "Returns true if a portion of 'source' characters can be rearranged to match 'target'"
  [source target]
  (and (>= (count source) (count target))
       (common/scramble-fn frequencies first/distribution-includes? source target))) ;; hurray! he remember `frequencies`...
