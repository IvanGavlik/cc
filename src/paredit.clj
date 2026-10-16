(ns paredit)
                                        ; Practice paredit in Emacs


(defn greet [name]
  (str "Hello, " name "!"))

(defn add [a b]
  (+ a b))


(comment
  (greet "name")
  )

(defn process [x]
  (let [doubled (* x 2)
        squared (* x x)]
    (+ doubled squared)))

(map inc [1 2 3 4 5])

(-> {:A 1 :B 2}
    (assoc :c 3)
    (dissoc :a))

(+ 1 2 3)



(do
(prn 1)
  (prn 1 2))

     
(prn 3)   
