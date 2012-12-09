(ns unix-clojure.echo
  (:use [clojure.tools.cli :only (cli)])
  (:use [clojure.string :only [join]]))


(defn echo [& args]
  (let [[options rest help]
        (cli args
             ["-h" "--help" "Show help" :flag true :default false]
             ["-n" "Supress new line" :flag true :default false])]
    (print (join " " rest))
    (when-not (options :n)
      (println))
    (when (options :h)
      (println help))))