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
        (print (slurp (get-file file)))
        (catch FileNotFoundException e
          (println (str "File " file " not found.")))))))
