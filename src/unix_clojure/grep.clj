(ns unix-clojure.grep
  (:use [unix-clojure.dir :only [process-file-contents get-path]])
  (:use [clojure.tools.cli :only (cli)]))


(defn grep [& args]
  (let [[options [pattern file & rest] help]
        (cli args
             ["-v" "--invert-match" "Invert the sense  of  matching" :flag true :default false]
             ["-c" "--count" "Suppress  normal  output;  instead print a count of matching lines" :flag true :default false]
             ["-o" "--only-matching" "Print only the matched (non-empty) parts of a matching line" :flag true :default false]
             ["-n" "--line-number" "Prefix each line of output with the 1-based line number" :flag true :default false]
             ["-r" "--recursive" "Read all files under each directory, recursively" :flag true :default false])]
    (process-file-contents line file
                           (if (and (:invert-match options)
                                    (-> (re-pattern pattern) (re-find line) (not)))
                             (println line)
                             (when (and (not (:invert-match options))
                                      (-> (re-pattern pattern) (re-find line)))
                               (println line))))))