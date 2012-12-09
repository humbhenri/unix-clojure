(ns unix-clojure.ls
  (:use [unix-clojure.dir :only [list-dir-contents
                                 list-dir-contents-recursive
                                 file-name
                                 hidden?
                                 modification-time
                                 get-path
                                 owner
                                 permissions
                                 file-type
                                 group
                                 size
                                 number-hard-links]])
  (:use [clojure.tools.cli :only (cli)])
  (:import [java.util Date]
           [java.text SimpleDateFormat]))


(defn print-file-name [file]
  (println (file-name file)))


(defn sort-files [files options]
  (let [key (cond (:t options) modification-time
                  (:S options) size
                  :else file-name)
        sorted-files (sort-by key files)]
    (if (:recursive options)
      files
      (if (:reverse options)
        (reverse sorted-files)
        sorted-files))))

(defn print-date [time]
  (let [date (Date. time)]
    (-> (SimpleDateFormat. "yyyy-MM-dd HH:mm")
        (.format date))))

(defn get-file-stats [file]
  (let [type (file-type file)
        rwx (permissions file)
        links (number-hard-links file)
        owner (owner file)
        group (group file)
        size (size file)
        date (print-date (modification-time file))
        name (file-name file)]
    (format "%s%s %4d %10s %10s %7d %s %s"
            type
            rwx
            links
            owner
            group
            size
            date
            name)))

(defn ls [& args]
  (let [[options rest help]
        (cli args
             ["-a" "--all" "do not ignore entries starting with ." :flag true :default false]
             ["-r" "--reverse" "reverse order while sorting" :flag true :default false]
             ["-R" "--recursive" "list subdirectories recursively" :flag true :default false]
             ["-t" "sort by modification time, newest first" :flag true :default false]
             ["-l" "use a long list formatting" :flag true :default false]
             ["-S" "sort by file size" :flag true :default false])
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
        (if (:l options)
          (println (get-file-stats f))
          (println name))))))