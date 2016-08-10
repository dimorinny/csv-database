# Сsv-database

A test project in clojure language whitch is simple api for csv files similar to sql syntax.

# Sample

### Data structure

**Student**

```
id,surname,year,group_id
1,Ivanov,1998
2,Petrov,1997
3,Sidorov,1996
```

**Subject**

```
id,subject
1,Math
2,CS
```

**Student-Subject**

> It's many-to-many relation table

```
student_id,subject_id
1,1
2,1
2,2
3,2
```

### Queries and responses

```
(db/select student)
({:surname Ivanov, :year 1998, :id 1} {:surname Petrov, :year 1997, :id 2} {:surname Sidorov, :year 1996, :id 3})

(db/select student :order-by :year)
({:surname Sidorov, :year 1996, :id 3} {:surname Petrov, :year 1997, :id 2} {:surname Ivanov, :year 1998, :id 1})

(db/select student :where #(> (:id %) 1))
({:surname Petrov, :year 1997, :id 2} {:surname Sidorov, :year 1996, :id 3})

(db/select student :limit 2)
({:surname Ivanov, :year 1998, :id 1} {:surname Petrov, :year 1997, :id 2})

(db/select student :where #(> (:id %) 1) :limit 1)
({:surname Petrov, :year 1997, :id 2})

(db/select student :where #(> (:id %) 1) :order-by :year :limit 2)
({:surname Sidorov, :year 1996, :id 3} {:surname Petrov, :year 1997, :id 2})

(db/select student-subject :limit 2 :joins [[:student_id student :id] [:subject_id subject :id]])
({:surname Ivanov, :year 1998, :id 1, :subject_id 1, :student_id 1, :subject Math} {:surname Petrov, :year 1997, :id 1, :subject_id 1, :student_id 2, :subject Math})
```
