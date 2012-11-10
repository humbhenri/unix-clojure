(ns unix-clojure.ls
  (:use [unix-clojure.dir :only [walk-directory file-name]]))


(defn print-file-name [file]
  (println (file-name file)))


(defn ls [& args]
  (if (empty? args)
    (walk-directory file "." (print-file-name file))
    (walk-directory file (first args) (print-file-name file))))