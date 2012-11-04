(ns unix-clojure.cat
  (:use [clojure.java.io])
  (:import [java.io FileNotFoundException]))

(defn cat [& files]
  (if (empty? files)
    (doseq [line (line-seq (java.io.BufferedReader. *in*))]
      (println line))
    (doseq [file files]
      (try
        (print (slurp file))
        (catch FileNotFoundException e
          (println (str "File " file " not found.")))))))
