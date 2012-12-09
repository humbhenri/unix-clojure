(ns unix-clojure.dir
  (:import [java.io File IOException FileNotFoundException]
           [java.nio.file Files Path LinkOption]
           [java.nio.file.attribute PosixFilePermissions PosixFileAttributes])
  (:use [clojure.java.io :only [delete-file reader]]))

;;; java doesn't let you change the working directory
(def WORKING_DIR (atom (System/getProperty "user.dir")))


(defn get-user-dir []
  (System/getProperty "user.home"))


(defn get-current-dir []
  @WORKING_DIR)


(defn get-temp-dir []
  (System/getProperty "java.io.tmpdir"))


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


(defmacro walk-directory-recursive [binding path & body]
  `(doseq [~binding (file-seq (File. (get-path ~path)))]
     ~@body))

(defmacro walk-directory [binding path & body]
  `(doseq [~binding (sort-by file-name (.listFiles (File. (get-path ~path))))]
     ~@body))

(defn list-dir-contents [path]
  (.listFiles (File. (get-path path))))

(defn list-dir-contents-recursive [path]
  (file-seq (File. (get-path path))))

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
      (println "Couldn't delete " dir-name))
    (catch FileNotFoundException e
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
      (println "Couldn't delete " file-name))
    (catch FileNotFoundException e
      (println "Couldn't delete " file-name))))


(defmacro process-file-contents [binding path body]
  `(with-open [rdr# (reader (get-path ~path))]
     (doseq [~binding (line-seq rdr#)]
       ~body)))


(defn read-file [path]
  (try
    (slurp (get-path path))
    (catch FileNotFoundException e
      (println "File" path" not found."))
    (catch IOException e
      (println "Couldn't read" path "."))))


(defn hidden? [file]
  (let [name (file-name file)]
    (.startsWith name ".")))

(defn modification-time [file]
  (.lastModified file))

(def no-follow-links
  (into-array [LinkOption/NOFOLLOW_LINKS]))

(defn owner [file]
  (-> (.toPath file) (Files/getOwner no-follow-links) (.getName)))

(defn size [file]
  (-> (.toPath file) (Files/size)))

(defn permissions [file]
  (-> (.toPath file)
      (Files/getPosixFilePermissions no-follow-links)
      PosixFilePermissions/toString))

(defn file-type [file]
  (let [path (.toPath file)]
    (cond (Files/isRegularFile path no-follow-links) "-"
          (Files/isDirectory path no-follow-links) "d"
          (Files/isSymbolicLink path) "l"
          :else "-")))

(defn group [file]
  (-> (.toPath file)
      (Files/readAttributes PosixFileAttributes no-follow-links)
      (.group)
      (.getName)))

(defn number-hard-links [file]
  (-> (.toPath file)
      (Files/getAttribute "unix:nlink" no-follow-links)))