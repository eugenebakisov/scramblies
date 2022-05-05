(ns eugene.bakisov.scramblies.domain.common)

(defn scramble-fn
  "Higher order function for building scramblies solutions"
  [distr-count-fn distr-cmp-fn source target]
  (->> [source target]
       (map distr-count-fn)
       (apply distr-cmp-fn)))
