(ns thread-expression)


(defn add-and-print-first [n1 n2]
  (prn n1)
  (+ n1 n2))

(defn add-and-print-last [n1 n2]
  (prn n2)
  (+ n1 n2))


(def person
  {:name "Mark Volkmann"
   :address {:street "644 Glen Summit"
             :city "St. Charles"
             :state "Missouri"
             :zip 63304}
   :employer {:name "Object Computing, Inc."
              :address {:street "12140 Woodcrest Dr."
                        :city "Creve Coeur"
                        :state "Missouri"
                        :zip 63141}}})

(comment
  ; thread expression first
  (-> 1
      (add-and-print-first 2)
      (add-and-print-first 4))

  (-> person
      :employer
      :address
      :city)
  (:city (:address (:employer person)))

  (-> "foo"
      (str "bar")
      (str "zar"))
  (str (str "foo" "bar") "zar")

  (-> 10
      ; anonymous fn has to be wrapped
      (#(/ % 2))
      )

  (inc (/ 10 2))

  (-> 10
      inc
      (/ 2))

  ; thread expression last
  (->> 1
       (add-and-print-last 2)
       (add-and-print-last 4))
  )


; TODO
; as->  Binds name to expr, evaluates the first form in the lexical context of that binding, then binds name to


; cond->  Takes an expression and a set of test/form pairs. Threads expr (via ->) through each form for which the corresponding test expression is true. Note that, unlike cond branching, cond-> threading does not short circuit after the first true test expression.
; cond->>  Takes an expression and a set of test/form pairs. Threads expr (via ->>) through each form for which the c