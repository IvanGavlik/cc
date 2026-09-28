(ns protocol)

(defprotocol MyProtocol
  "doc string"
  (bar [this a b] "bar docs")
  (baz [this a] "baz docs")
  )

(defrecord Test [name]
  MyProtocol
  (bar [this a b] (prn "bar " this (:name this) a b))
  (baz [this a] (prn "baz " this a))
  )

(comment
  (extends? MyProtocol Test)
  (def my-test (Test. "some name"))
  (bar my-test "a" "b")
  ())

(defprotocol Printable
  (print-it [value]))

(defprotocol Persistable
  (save [value]))


(extend-type clojure.lang.IPersistentMap
  Printable
  (print-it [value]
    (println "I am printing it " value))

  Persistable
  (save [value]
    (println "I am saving it")
    (assoc value :id 1))
  )

(comment
  (print-it {:a "aaa"})
  (save {:b "bbb"})
  )

; Task 7 — Protocol + Multimethod
;
;Build a system where a protocol abstracts the component, while a multimethod dispatches the operation.
;
;For example:
;Storage protocol
;       │
;       ├── PostgreSQL
;       └── Redis
;
;operation multimethod
;       │
;       ├── :read
;       ├── :write
;       └── :delete
;
;This is an excellent exercise because it makes the distinction between the two mechanisms concrete.

(defprotocol Storage
  (read [this value])
  (write [this value])
  (delete [this value]))

(defrecord PostgreSQL []
  Storage
  (read [_ value]
   (prn "PostgreSQL value is" (:id value)))

  (write [_ value]
    (prn "PostgreSQL value is" (:id value)))

  (delete [_ value]
    (prn "PostgreSQL value is" (:id value)))
  )

(defrecord Redis []
  Storage
  (read [this value]
    (prn "Redis value is " value))

  (write [_ value]
    (prn "Redis value is" (:id value)))

  (delete [_ value]
    (prn "Redis value is" (:id value)))
  )

(defmulti operation :type)

(defmethod operation :read [{:keys [value storage]}]
  (prn "I am reading from")
  (read storage value))

(defmethod operation :write [{:keys [value storage]}]
  (prn "I am write to" value)
  (write storage value))

(defmethod operation :delete [{:keys [value storage]}]
  (prn "I am delete" value)
  (delete storage value))


(comment
  ; The multimethod doesn't know whether storage is Redis or Postgres
  (def some-storage (->Redis))
  (operation {:type :read
              :value {:id 10}
              :storage some-storage})

  (def some-other-storage (->PostgreSQL))
  (operation {:type :write
              :value {:id 10}
              :storage some-other-storage})
  )