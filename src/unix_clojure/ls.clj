(ns unix-clojure.ls
  (:use [unix-clojure.dir :only [list-dir-contents
                                 list-dir-contents-recursive
                                 file-name
                                 hidden?
                                 modification-time
                                 get-path]])
  (:use [clojure.tools.cli :only (cli)]))


(defn print-file-name [file]
  (println (file-name file)))


(defn sort-files [files options]
  (let [key (cond (:t options) modification-time
                  :else file-name)
        sorted-files (sort-by key files)]
    (if (:recursive options)
      files
      (if (:reverse options)
        (reverse sorted-files)
        sorted-files))))

(defn ls [& args]
  (let [[options rest help]
        (cli args
             ["-a" "--all" "do not ignore entries starting with ." :flag true :default false]
             ["-r" "--reverse" "reverse order while sorting" :flag true :default false]
             ["-R" "--recursive" "list subdirectories recursively" :flag true :default false]
             ["-t" "sort by modification time, newest first" :flag true :default false])
        path (if (empty? rest) "." (first rest))
        files (if (:recursive options)
                (list-dir-contents-recursive path)
                (list-dir-contents path))]
    (doseq [f (sort-files files options)
            :let [name (file-name f)]
            :when (if (hidden? f) (:all options) true)]
      (if (and (.isDirectory f) (:recursive options))
        (do (println)
            (println (str (.getCanonicalPath f) ":")))
        (println name)))))