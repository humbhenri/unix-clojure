(ns unix-clojure.grep
  (:use [unix-clojure.dir :only [read-lines]]
        [clojure.tools.cli :only (cli)]
        [clojure.string :only [join]]))


(defn prepend-pos [lines text]
  (->>
   (for [[i l] (map list (iterate inc 1) text)]
     (for [k lines]
       (when (= k l) (str i ": " k))))
   (flatten)
   (filter (comp not nil?))))


(defn grep [& args]
  (let [[options [regex file & rest] help]
        (cli args
             ["-v" "--invert-match" "Invert the sense  of  matching" :flag true :default false]
             ["-c" "--count" "Suppress  normal  output;  instead print a count of matching lines" :flag true :default false]
             ["-o" "--only-matching" "Print only the matched (non-empty) parts of a matching line" :flag true :default false]
             ["-n" "--line-number" "Prefix each line of output with the 1-based line number" :flag true :default false]
             ["-r" "--recursive" "Read all files under each directory, recursively" :flag true :default false])
        pattern (re-pattern regex)
        lines (read-lines file)
        matched (for [line lines
                      :let [matches (re-seq pattern line)]
                      :when (if (:invert-match options) (not matches) matches)]
                  (cond (:only-matching options) (join " " matches)
                        (:line-number options) (prepend-pos matches lines)
                        :else line))]
    (if (:count options)
      (println (count matched))
      (doseq [line matched]
        (println line)))))