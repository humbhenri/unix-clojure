(ns unix-clojure.cat
  (:use [unix-clojure.dir :only [read-file process-input]]))

(defn cat [& files]
  (if (empty? files)
    (process-input line (println line))
    (doseq [file files]
      (print (read-file file)))))
