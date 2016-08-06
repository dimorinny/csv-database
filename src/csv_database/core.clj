(ns csv-database.core
  (:require [clojure-csv.core :as csv]))

(def student-tbl (csv/parse-csv (slurp "student.csv")))
(def subject-tbl (csv/parse-csv (slurp "subject.csv")))
(def student-subject-tbl (csv/parse-csv (slurp "student_subject.csv")))

(defn table-keys
  "Return vector of table scheme keys"
  [[scheme & _]]
  (->> scheme
    (map keyword)
    (vec)))

(defn key-value-pairs
  "Return list with keys and values"
  [table-keys table-record]
  (->> (map list table-keys table-record)
    (flatten)))

(defn data-record
  "Return hash map from keys and values"
  [table-keys table-record]
  (->> (key-value-pairs table-keys table-record)
    (apply hash-map)))

; (defn data-table
;   [[scheme & values :as table]]
;   (apply map data-record
;     (table-keys table)
;     (data-record values)))
;
; (data-table student-tbl)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
