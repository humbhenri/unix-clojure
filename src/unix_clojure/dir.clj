(ns unix-clojure.dir
  (:import [java.io File]))

;;; java doesn't let you change the working directory
(def WORKING_DIR (atom (System/getProperty "user.dir")))

(defn get-current-dir []
  @WORKING_DIR)

(defn pwd []
  (println (get-current-dir)))

(defn cd [dir]
  (reset! WORKING_DIR
          (cond
           (= dir ".") @WORKING_DIR
           (= dir "..") (.getCanonicalPath (File. @WORKING_DIR dir))
           :else (.getCanonicalPath (File. dir)))))

(defn get-file [file-name]
  (.getCanonicalPath (File. @WORKING_DIR file-name)))