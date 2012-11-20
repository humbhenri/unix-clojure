(ns unix-clojure.core-test
  (:use midje.sweet
        unix-clojure.cat
        unix-clojure.dir
        unix-clojure.ls
        unix-clojure.grep
        unix-clojure.wc
        clojure.java.io)
  (:require clojure.string))

(def TEMPDIR (get-temp-dir))

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

(against-background [(before :facts (write-test-file)) (after :facts (do (rm FILE) (rm FILE2)))]

                    (fact "cat file should print file content"
                      (dos2unix (with-out-str (cat FILE))) => TEXT)

                    (fact "cat files should concatenate files' content to stdout"
                      (dos2unix (with-out-str (cat FILE FILE2))) => (str TEXT TEXT2)))

(fact "pwd should show current working dir"
  (.trim (with-out-str (pwd))) => (get-current-dir))

(against-background [(before :facts (cd TEMPDIR))]
  (fact "cd change pwd"
    (.trim (with-out-str (pwd))) => (canonical-path TEMPDIR)))


(fact "obvious paths should exist"
  (path-exists? ".") => true
  (path-exists? "..") => true
  (path-exists? (get-current-dir)) => true)


(against-background [(before :facts
                             (do
                               (cd TEMPDIR)
                               (mkdir "blah")))]
  (fact "mkdir should make a new directory under current working directory"
    (path-exists? "blah") => true)

  (fact "cd should accept relative paths"
    (cd "blah")
    (.trim (with-out-str (pwd))) => (canonical-path (str TEMPDIR "/blah")))

  (fact "rmdir should remove an empty dir"
    (rmdir "blah") => true))


(against-background [(before :facts (cd TEMPDIR))]
  (fact "touch should create a file"
    (touch "new.txt")
    (path-exists? "new.txt") => true
    (directory? "new.txt") => false)    ; TODO false doesn't work with empty files

  (fact "rm should remove a file"
    (rm "new.txt")
    (path-exists? "new.txt") => false))

(against-background [(before :facts (do
                                      (mkdir (str TEMPDIR "/blah"))
                                      (cd (str TEMPDIR "/blah"))
                                      (touch "A")
                                      (touch "B")
                                      (touch "C")))
                     (after :facts (do
                                     (cd (str TEMPDIR "/blah"))
                                     (rm "A")
                                     (rm "B")
                                     (rm "C")
                                     (cd "..")
                                     (rmdir "blah")))]
  (fact "ls should list files"
    (vec (clojure.string/split-lines (with-out-str (ls)))) => ["A" "B" "C"]))

(against-background [(before :facts (do
                                      (cd TEMPDIR)
                                      (touch "file")
                                      (spit (get-path "file") "sljfsdf\ns121k\nskjf\n22ol\n0\n\n\n\nabc\n")))
                     (after :facts (rm (get-path "file")))]

  (fact "wc should count number of new lines in a file"
    (dos2unix (with-out-str (wc "file"))) => "9\n")

  (fact "grep should print lines matching a pattern from a file"
    (dos2unix (with-out-str (grep "\\d+" (get-path "file")))) => "ns121k\n22ol\n"))
