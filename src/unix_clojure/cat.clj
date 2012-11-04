(ns unix-clojure.cat
  (:use [clojure.java.io]))

(defn cat [& files]
  (if (empty? files)
    (doseq [line (line-seq (java.io.BufferedReader. *in*))]
      (println line))
    (doseq [file files]
      (print (slurp file)))))
