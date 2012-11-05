(ns unix-clojure.dir
  (:import [java.io File IOException])
  (:use [clojure.java.io :only [delete-file]]))

;;; java doesn't let you change the working directory
(def WORKING_DIR (atom (System/getProperty "user.dir")))


(defn get-current-dir []
  @WORKING_DIR)


(defn absolute? [path]
  (.isAbsolute (File. path)))


(defn path-exists [path]
  (if (absolute? path)
    (.exists (File. path))
    (.exists (File. (get-current-dir) path))))


(defn pwd []
  (println (get-current-dir)))


(defn cd [dir]
  (reset! WORKING_DIR
          (cond
           (= dir ".") (get-current-dir)
           (= dir "..") (.getCanonicalPath (File. (get-current-dir) dir))
           (absolute? dir) (.getCanonicalPath (File. dir))
           :else (.getCanonicalPath (File. @WORKING_DIR dir)))))


(defn get-path [path]
  (if (absolute? path)
    (.getCanonicalPath (File. path))
    (.getCanonicalPath (File. (get-current-dir) path))))


(defn mkdir [dir-name]
  (.mkdirs (File. (get-path dir-name))))


(defn rmdir [dir-name]
  (try
    (delete-file (get-path dir-name))
    (catch IOException e
      (println "Couldn't delete " dir-name))))


(defn canonical-path [path]
  (.getCanonicalPath (File. path)))