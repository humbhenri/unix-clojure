(ns unix-clojure.dir
  (:import [java.io File IOException FileNotFoundException])
  (:use [clojure.java.io :only [delete-file]]))

;;; java doesn't let you change the working directory
(def WORKING_DIR (atom (System/getProperty "user.dir")))


(defn get-user-dir []
  (System/getProperty "user.home"))


(defn get-current-dir []
  @WORKING_DIR)


(defn absolute? [path]
  (.isAbsolute (File. path)))


(defn directory? [path]
  (.isDirectory (File. path)))


(defn file? [path]
  (.isFile (File. path)))


(defn get-path [path]
  (if (absolute? path)
    (.getCanonicalPath (File. path))
    (.getCanonicalPath (File. (get-current-dir) path))))


(defn path-exists? [path]
  (.exists (File. (get-path path))))


(defn file-name [file]
  (.getName file))


(defmacro walk-directory-recursive [path & body]
  `(doseq [~'item (file-seq (File. (get-path ~path)))]
     ~@body))

(defmacro walk-directory [path & body]
  `(doseq [~'item (.listFiles (File. (get-path ~path)))]
     ~@body))

(defn pwd []
  (println (get-current-dir)))


(defn cd
  ([] (cd (get-user-dir)))
  ([dir]
     (if (path-exists? dir)
       (reset! WORKING_DIR
               (cond
                (= dir ".") (get-current-dir)
                (= dir "..") (.getCanonicalPath (File. (get-current-dir) dir))
                (absolute? dir) (.getCanonicalPath (File. dir))
                :else (.getCanonicalPath (File. @WORKING_DIR dir))))
       (println "Directory " dir " not found!"))))


(defn mkdir [dir-name]
  (try
    (.mkdirs (File. (get-path dir-name)))
    (catch IOException e
      (println "Couldn't create directory " dir-name))))


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


(defn touch [file-name]
  (try
    (.createNewFile (File. (get-path file-name)))
    (catch IOException e
      (println "Couldn't create file " file-name))
    (catch FileNotFoundException e
      (println "Couldn't create file " file-name))))


(defn rm [file-name]
  (try
    (if-not (directory? (get-path file-name))
      (delete-file (get-path file-name))
      (println "rm: " file-name ": is a directory"))
    (catch IOException e
      (println "Couldn't delete " file-name))))