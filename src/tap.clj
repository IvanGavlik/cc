(ns tap)


; register tap function
(add-tap println)
(tap> {:name "John"})


(defn my-logger [& args]
  (prn "I am logging" args))
(add-tap my-logger)
(tap> {:name "John"})

(remove-tap my-logger)