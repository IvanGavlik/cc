(ns paredit)

                                        ; Practice paredit in Emacs


(defn greet [name]
  (str "Hello, " name "!"))

(defn add [a b]
  (+ a b))

(defn process [x]
  (let [doubled (* x 2)
        squared (* x x)]
    (+ doubled squared))q)

(map inc [1 2 3 4 5])

(-> {:A 1:B 2}
    (assoc :c 3)
    (dissoc :a))

(+ 1 2) 3
