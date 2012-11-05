(ns unix-clojure.dir
  (:import [java.io File]))

;;; java doesn't let you change the working directory
(def WORKING_DIR (atom (System/getProperty "user.dir")))


(defn get-current-dir []
  @WORKING_DIR)


(defn isAbsolute [path]
  (.isAbsolute (File. path)))


(defn path-exists [path]
  (if (isAbsolute path)
    (.exists (File. path))
    (.exists (File. (get-current-dir) path))))


(defn pwd []
  (println (get-current-dir)))


(defn cd [dir]
  (reset! WORKING_DIR
          (cond
           (= dir ".") (get-current-dir)
           (= dir "..") (.getCanonicalPath (File. (get-current-dir) dir))
           :else (.getCanonicalPath (File. dir)))))


(defn get-file [file-name]
  (.getCanonicalPath (File. (get-current-dir) file-name)))


(defn mkdir [dir-name]
  (.mkdirs (File. (get-current-dir) dir-name)))