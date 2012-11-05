(ns unix-clojure.cat
  (:use [clojure.java.io]
        [unix-clojure.dir])
  (:import [java.io FileNotFoundException]))

(defn cat [& files]
  (if (empty? files)
    (doseq [line (line-seq (java.io.BufferedReader. *in*))]
      (println line))
    (doseq [file files]
      (try
        (print (slurp (get-path file)))
        (catch FileNotFoundException e
          (println "File " file " not found."))))))
