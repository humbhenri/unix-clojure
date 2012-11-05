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

(fact "cd change pwd"
  (cd "C:/Users")
  (with-out-str (pwd) => "C:\\Users"))