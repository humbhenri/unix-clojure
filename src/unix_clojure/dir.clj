(ns unix-clojure.dir)

(defn get-current-dir []
  (System/getProperty "user.dir"))
