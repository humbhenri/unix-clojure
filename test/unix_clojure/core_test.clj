(ns unix-clojure.core-test
  (:use midje.sweet
        unix-clojure.cat
        unix-clojure.dir
        clojure.java.io))

(def FILE "test.txt")
(def FILE2 "text2.txt")

(def TEXT
"Blah
Blah
Blah")

(def TEXT2
"skdjf skdjf skdfj
skdfj
")

(defn write-test-file []
  (spit FILE TEXT)
  (spit FILE2 TEXT2))

(defn dos2unix [s]
  (.replaceAll s "\r\n" "\n"))

(against-background [(before :facts (write-test-file))]

                    (fact "cat file should print file content"
                      (dos2unix (with-out-str (cat FILE))) => TEXT)

                    (fact "cat files should concatenate files' content to stdout"
                      (dos2unix (with-out-str (cat FILE FILE2))) => (str TEXT TEXT2)))

(fact "pwd should show current working dir"
  (.trim (with-out-str (pwd))) => (get-current-dir))

(against-background [(before :facts (cd "C:/Users"))]
  (fact "cd change pwd"
    (.trim (with-out-str (pwd))) => (canonical-path "C:\\Users")))


(fact "obvious paths should exist"
  (path-exists? ".") => true
  (path-exists? "..") => true
  (path-exists? (get-current-dir)) => true)


(against-background [(before :facts
                             (do
                               (cd "C:/Temp")
                               (mkdir "blah")))]
  (fact "mkdir should make a new directory under current working directory"
    (path-exists? "blah") => true)

  (fact "cd should accept relative paths"
    (cd "blah")
    (.trim (with-out-str (pwd))) => (canonical-path "C:/Temp/blah"))

  (fact "rmdir should remove an empty dir"
    (rmdir "blah") => true))


(against-background [(before :facts (cd "C:/Temp"))]
  (fact "touch should create a file"
    (touch "new.txt")
    (path-exists? "new.txt") => true
    (directory? "new.txt") => false)    ; TODO false doesn't work with empty files

  (fact "rm should remove a file"
    (rm "new.txt")
    (path-exists? "new.txt") => false))