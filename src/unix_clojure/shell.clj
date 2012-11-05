(ns unix-clojure.shell
  (:gen-class)
  (:use [unix-clojure.dir :only [get-current-dir pwd cd mkdir rmdir touch rm]]
        [unix-clojure.cat :only [cat]]))


(defn exit []
  (System/exit 0))


(defn print-prompt []
  (print (str (get-current-dir) "> "))
  (flush))


(defn run-command-with-args [^String command args]
  (if-let [fun (ns-resolve 'unix-clojure.shell (symbol command))]
    (apply fun args)
    (println "Command " command " not found.")))


(defn execute-command [line-input]
  (let [tokens (vec (.split (.trim line-input) "\\s+"))
        command (first tokens)
        args (rest tokens)]
    (when ( >(.length command) 0)
      (run-command-with-args command args))))


(defn run []
  (doseq [line (line-seq (java.io.BufferedReader. *in*))]
    (execute-command line)
    (print-prompt)))


(defn -main [& args]
  (print-prompt)
  (run))