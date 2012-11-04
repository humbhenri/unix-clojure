(ns unix-clojure.dir)

(defn get-current-dir []
  (System/getProperty "user.dir"))

(defn pwd []
  (println (get-current-dir)))
