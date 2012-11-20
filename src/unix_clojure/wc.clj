(ns unix-clojure.wc
  (:use [unix-clojure.dir :only [read-file]]))

(defn count-lines [text]
  (count (for [c text :when (= c \newline)] c)))

(defn count-words [text]
  (count (.split text "\\s+")))

(defn count-chars [text]
  (.length text))

(defn wc [file]
  (let [text (read-file file)]
    (println (count-lines text) (count-words text) (count-chars text) file)))