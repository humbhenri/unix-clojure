(ns unix-clojure.dir
  (:import [java.io File IOException])
  (:use [clojure.java.io :only [delete-file]]))

;;; java doesn't let you change the working directory
(def WORKING_DIR (atom (System/getProperty "user.dir")))


(defn get-current-dir []
  @WORKING_DIR)


(defn absolute? [path]
  (.isAbsolute (File. path)))


(defn directory? [path]
  (.isDirectory (File. path)))


(defn get-path [path]
  (if (absolute? path)
    (.getCanonicalPath (File. path))
    (.getCanonicalPath (File. (get-current-dir) path))))


(defn path-exists? [path]
  (.exists (File. (get-path path))))


(defn pwd []
  (println (get-current-dir)))


(defn cd [dir]
  (if (path-exists? dir)
    (reset! WORKING_DIR
            (cond
             (= dir ".") (get-current-dir)
             (= dir "..") (.getCanonicalPath (File. (get-current-dir) dir))
             (absolute? dir) (.getCanonicalPath (File. dir))
             :else (.getCanonicalPath (File. @WORKING_DIR dir))))
    (println "Directory " dir " not found!")))


(defn mkdir [dir-name]
  (.mkdirs (File. (get-path dir-name))))


(defn rmdir [dir-name]
  (try
    (let [dir (get-path dir-name)]
      (if (directory? dir)
        (delete-file dir)
        (println dir-name " is not a directory!")))
    (catch IOException e
      (println "Couldn't delete " dir-name))))


(defn canonical-path [path]
  (.getCanonicalPath (File. path)))