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