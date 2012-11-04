(ns unix-clojure.shell
  (:gen-class)
  (:use [unix-clojure.dir :only [get-current-dir]]
        [unix-clojure.cat :only [cat]]))


(defn print-prompt []
  (print (str (get-current-dir) "> "))
  (flush))


(defn execute-command [line-input]
  (let [tokens (vec (.split (.trim line-input) "\\s+"))
        command (first tokens)
        args (rest tokens)]
    (cond
     (= command "cat") (apply cat args)
     (= command "exit") (System/exit 0)
     :else (println (str "Command " command " not recognized")))))


(defn run []
  (doseq [line (line-seq (java.io.BufferedReader. *in*))]
    (execute-command line)
    (print-prompt)))


(defn -main [& args]
  (print-prompt)
  (run))