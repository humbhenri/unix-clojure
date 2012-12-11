(ns unix-clojure.grep
  (:use [unix-clojure.dir :only [process-file-contents get-path]]))

(defn grep [pattern file]
  (process-file-contents line file
                         (when (re-find (re-pattern pattern) line)
                           (println line))))