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
  (let [pairs (key-value-pairs table-keys table-record)]
    (->> pairs
      (apply hash-map))))

(defn data-table
  "Return list of records from csv file"
  [[scheme & values :as table]]
  (let [keys (table-keys table)]
    (for [value values]
      (data-record keys value))))

(defn str-fields-to-int
  "Return map where all values with param fields keys casted to int"
  [rec & fields]
  (apply assoc rec
    (flatten
      (for [field fields]
        (let [value (field rec)
              int-value (Integer/parseInt value)]
          [field int-value])))))

(defn table-with-casted-numbers
  "Return table where all values with param fields keys casted to int"
  [table & fields]
  (let [parsed-table (data-table table)]
    (->> parsed-table
      (map #(apply str-fields-to-int % fields)))))

(def student (table-with-casted-numbers student-tbl :id :year))
(def subject (table-with-casted-numbers subject-tbl :id))
(def student-subject
  (table-with-casted-numbers student-subject-tbl :subject_id :student_id))

(defn where*
  "Apply projection to data"
  [data condition]
  (filter condition data))

(defn limit*
  "Take first count elements"
  [data count]
  (if count
    (take count data)))

(defn order-by*
  "Sort data by column"
  [data column]
  (sort-by column data))

(where* student #(>= (:id %) 2))
(limit* student 2)
(order-by* student :year)

(defn join*
  [data1 column1 data2 column2]
  (for [value data1]
    (->> (filter #(= (column1 value) (column2 %)) data2)
      (first)
      (conj value))))

(defn perform-joins
  [data joins*]
  (loop [data1 data joins joins*]
    (if (empty? joins)
      data1
      (let [[col1 data2 col2] (first joins)]
        (recur (join* data1 col1 data2 col2)
          (next joins))))))

(perform-joins student-subject [[:student_id student :id] [:subject_id subject :id]])

(defn select
  [data & {:keys [where limit order-by joins]}]
  (-> data
    (perform-joins joins)
    (where* where)
    (order-by* order-by)
    (limit* limit)))

(select student)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
