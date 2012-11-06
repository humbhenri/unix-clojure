(ns unix-clojure.ls
  (:use [unix-clojure.dir :only [walk-directory file-name]]))


(defn print-file-name [file]
  (println (file-name file)))


(defn ls [& args]
  (if (empty? args)
    (walk-directory "." (print-file-name item))
    (walk-directory (first args) (print-file-name item))))