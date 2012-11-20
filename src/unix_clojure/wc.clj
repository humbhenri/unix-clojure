(ns unix-clojure.wc
  (:use [unix-clojure.dir :only [read-file]]))

(defn wc [file]
  (println (count (for [c (read-file file) :when (= c \newline)] c))))