(ns csv-database.sample
  (:require [clojure-csv.core :as csv]
            [csv-database.core :as db])
  (:gen-class))

(defn -main
  "Its a main sample function."
  [& _]
  (let
    [student-tbl (csv/parse-csv (slurp "student.csv"))
     subject-tbl (csv/parse-csv (slurp "subject.csv"))
     student-subject-tbl (csv/parse-csv (slurp "student_subject.csv"))
     student (db/table-with-casted-numbers student-tbl :id :year)
     subject (db/table-with-casted-numbers subject-tbl :id)
     student-subject (db/table-with-casted-numbers student-subject-tbl :subject_id :student_id)]

   (println "(db/select student)")
   (println (db/select student))

   (println "(db/select student :order-by :year)")
   (println (db/select student :order-by :year))

   (println "(db/select student :where #(> (:id %) 1))")
   (println (db/select student :where #(> (:id %) 1)))

   (println "(db/select student :limit 2)")
   (println (db/select student :limit 2))

   (println "(db/select student :where #(> (:id %) 1) :limit 1)")
   (println (db/select student :where #(> (:id %) 1) :limit 1))

   (println "(db/select student :where #(> (:id %) 1) :order-by :year :limit 2)")
   (println (db/select student :where #(> (:id %) 1) :order-by :year :limit 2))

   (println "(db/select student-subject :limit 2 :joins [[:student_id student :id] [:subject_id subject :id]])")
   (println (db/select student-subject :limit 2 :joins [[:student_id student :id] [:subject_id subject :id]]))))
