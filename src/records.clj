(ns records)

(defrecord User [name age email])

(def user
  (->User "John" 30 "test@test.com"))

(map->Unser {:NAME "JOHN"
            :AGE 30
            :EMAIL "TEST@TEST2.COM"
            })





