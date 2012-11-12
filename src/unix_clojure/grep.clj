(ns unix-clojure.grep
  (:use [unix-clojure.dir :only [process-file-contents get-path]]))

(defn grep [pattern file]
  (process-file-contents line file
                         (when-not (nil? (re-find (re-pattern pattern) (get-path file)))
                           (println line))))