(ns unix-clojure.date
  (:use [clj-time.local :only [local-now]]
        [clj-time.format :only [unparse formatter]]))


(def default-format (formatter "E MMM dd HH:mm:ss yyyy"))


(defn date [& args]
  (println (unparse default-format (local-now))))