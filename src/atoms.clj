(ns atoms)

; atom  Creates and returns an Atom with an initial value of x and zero or more options (in any order): :meta meta

(def counter (atom 0))

; The callbacks run as part of the update operation, so a slow watch makes swap! or reset! slowe
(add-watch counter :logger
           (fn [key ref old-state new-state]
             (println "Change from" old-state "to " new-state)))

(comment
  (swap! counter inc))


; counter task
(def my-counter (atom 0))
(add-watch my-counter :logger
           (fn [key ref old-state new-state]
             (println "Change from" old-state "to " new-state)))

(comment
  (swap! my-counter inc)
  (swap! my-counter dec)
  (reset! my-counter 100))


; Shopping Cart
(defn add-to [cart item]
  (swap! cart conj item))

(defn remove-from [cart item]
  (let [remove-fn (fn [chart item]
                    (vec (remove #(= % item) chart)))]
    (swap! cart remove-fn item)))

(defn clear-cart [cart]
  (reset! cart []))

(defn count-cart [cart]
  (count @cart))

(comment
  (def cart (atom ["apple" "banana" "milk"]))
  (add-to cart "water")
  (remove-from cart "apple")
  (clear-cart cart)
  (count-cart cart)
  (reduce (fn [s i] (if (= 3 i) (conj s i) s)) [] [1 2 3 4])
  )

; User Profile

(def user (atom {:name       "Alice"
                 :age        30
                 :logged-in? false}))

; Practice updating nested values using swap!:
; increment age
; log the user in
; change the name

(defn swap-fn [map key value]
  (assoc map key value))

(defn increment-age [user]
  (swap! user (fn [value]
                (prn "increment")
                (assoc value :age (inc (:age value))))))


(defn update-map []
  (fn [map key value] (assoc map key value)))

(defn log-user [user logged?]
  (swap! user (update-map) :logged-in? logged?
         #_#(assoc % :logged-in? logged?)
         #_(fn [value] (assoc value :logged-in? logged?))))


(defn change-name [user name]
  (swap! user (fn [value] (assoc value :name name))))

(comment
  (increment-age user)

  (log-user user true)
  (log-user user false)
  (change-name user "Bob")

  ; swap! atomically applies my update function.
  ; If another thread changes the atom before my update succeeds,
  ; swap! retries my function against the latest value.
  (do
    (future (increment-age user))
    (future (increment-age user))
    (prn user))

  ;You might see "increment!" more than twice under contention because the function can be retried.
  ;Therefore, the function you give to swap! should ideally be pure:
  ; rather than something with side effects:
  ; CAS + retry will make the implementation of Atom
  )


; Todo List

; Maintain:
{:next-id 2
 :todos {1 {:id 1
            :title "Learn atoms"
            :completed? false}}}

; Each todo has:
; id
; title
; completed?

; Implement operations:
; add todo
; complete todo
; delete todo
; list completed todos

; Question Which pieces of state must change together to preserve an invariant?

(def todos (atom {:next-id 0
                  :todos {}}))

(defn new-todo [todo title]
  (let [id      (:next-id todo)]
    (-> todo
        (assoc-in [:todos id] {:title               title
                               :completed? false})
        (update :next-id inc))))

(comment (-> (new-todo {:next-id 1} "test 1")
             (new-todo "test 2")))

(defn complete-todo [todo id]
  (if (contains? (:todos todo) id)
    (assoc-in todo [:todos id :completed?] true)
    todo))

(comment (->
          {:next-id 3, :todos {0 {:title "write code 1", :completed? false}, 1 {:title "write code 2", :completed? false}, 2 {:title "write code 2", :completed? false}}}
          (complete-todo 1)
          (complete-todo 5)))

(defn delete-todo [todo id]
  (update todo :todos dissoc id))

(comment(->
         {:next-id 3, :todos {0 {:title "write code 1", :completed? false}, 1 {:title "write code 2", :completed? false}, 2 {:title "write code 2", :completed? false}}}
         (delete-todo 0)
         (delete-todo 1)
         (delete-todo 2)
         (delete-todo 3)))

(defn add-todo! [todos title]
  (swap! todos new-todo title))

(defn complete-todo! [todos id]
  (swap! todos complete-todo id))

(defn delete-todo! [todos id]
  (swap! todos delete-todo id))

(comment
  (add-todo! todos "write code 2")
  (add-todo! todos "write code 2")
  (add-todo! todos "write code 2")
  (complete-todo! todos 0)
  (complete-todo! todos 1)
  (delete-todo! todos 0)
  ())


; Create an in-memory cache.
;State:
;{:users {}
; :products {} }
;
;Implement:
;
;cache lookup
;cache insert
;cache eviction
;cache clear

(def cache (atom {}))

(defn cache-lookup [cache]
  (fn [key]
    (get @cache key)))

(def lookup (cache-lookup cache))

(comment
  (reset! cache {:test 123})
  ((cache-lookup cache) :test)
  (lookup :test)
  )

(defn insert-cache [cache]
  (fn [key value]
    (swap! cache assoc key value)
    ; why not use reset
    #_(rest! cache ...)))

(comment
  (reset! cache {:test 123})
  (def insert (insert-cache cache))
  (insert :test-2 2)
  )

(defn clear-cache [cache]
  (fn [] (reset! cache {})))

(comment
  (def cache (atom {}))
  (insert :test-2 2)
  (def clear-c (clear-cache cache))
  (clear-c)
  (lookup :test-2)
  )


; Inventory
; Maintain product quantities.

; Example:

{"Laptop" 10
 "Mouse" 35
 "Keyboard" 12}

; Implement:
;   stock arrival
;   purchase
;   prevent negative inventory

(def inventory (atom {"Laptop"   10
                      "Mouse"    35
                      "Keyboard" 12}
                     :validator (fn [inventory-state]
                                  (every? #(>= % 0) (vals inventory-state)))
                     ))

(defn inventory-add [inventory key value]
  (let [add-to-inventory (fn [inventory key value]
                           (when (>= value 0)
                             (let [new-value (+ value (get inventory key 0))]
                               (assoc inventory key new-value))))]
    (swap! inventory add-to-inventory key value)))

(defn inventory-remove [inventory key value]
  (let [remove-from-inventory (fn [inventory key value]
                                (let [new-value (- (get inventory key 0) value)]
                                  (assoc inventory key new-value)))]
    (swap! inventory remove-from-inventory key value)))

; what to do when no key for adding and removing
; what to do when negative is input for adding and removing
; todo conclusion validator can only check is "END" state ok
; business operation have to define validation
(comment
  (inventory-add inventory "Disk" 1)
  (inventory-add inventory "Laptop" -1)
  (inventory-remove inventory "Disk" 2)
  (inventory-remove inventory "Laptop" 1))


;; todo CAN I create validator that can check input
(comment
  )


; 10. State History
;
;Every change should also be stored in a history vector.
;
;Example:
;
;{:current 42
; :history [0 5 12 42]}
;
;Challenge yourself to keep the history consistent.

(def state-history (atom {:current  1
                          :history [0]}))

(defn change [state value]
  (swap! state (fn [state new-value]
                 (let [current (:current state)]
                   (if (= current new-value)
                     state
                     (-> state
                         (assoc :current new-value)
                         (update :history conj new-value)))))
         value))

(comment
  (change state-history 12)
  (change state-history 13)
  )

;11. Watchers
;
;Add one or more watches to an atom.
;
;The watches should:
;
;log every state change
;count how many updates occurred
;detect when a specific value appears
;

(def my-state (atom 0))
(def my-state-counter (atom 0))

(add-watch my-state
           :logger (fn [key ref old-state new-state]
                     (println "Change from" old-state "to " new-state)))

(add-watch my-state
           :counter (fn [key ref old-state new-state]
                      (swap! my-state-counter inc)))

(add-watch my-state
           :detect-12 (fn [key ref old-state new-state]
                        (when (= 12 new-state)
                          (println "Detected 12"))))

(comment
  (swap! my-state inc)
  (prn @my-state-counter))


;13. Rate Limiter
;
;Store:
;
;{:requests [...]
; :blocked? false}
;
;Design a rate limiter that:
;
;records request timestamps
;blocks after N requests within one minute
;automatically allows requests again after the window expires

(def rate-limiter (atom {:requests [{:id 1
                                     :timestamp (java.time.LocalTime/now)}
                                    {:id 2
                                     :timestamp (java.time.LocalTime/now)}
                                    {:id 3
                                     :timestamp (java.time.LocalTime/now)}]
                         :blocked? false}))

(defn record-request [rate-limiter request]
  (swap! rate-limiter
         (fn [rate-limiter request]
           (let [t1       (:timestamp (first (:requests rate-limiter)))
                 tn       (:timestamp request)
                 blocked? (> 60 (.getSeconds (java.time.Duration/between t1 tn)))]
             (if blocked?
               (assoc rate-limiter :blocked? true)
               (-> rate-limiter
                   (update :requests
                           (fn [old new]
                             (vec (rest (conj old new))))
                           request)
                   (assoc :blocked? false))))) request))


(comment
  (record-request rate-limiter {:id 4
                                :timestamp (java.time.LocalTime/now)})
  )


;14. In-Memory Session Store
;
;Maintain sessions:
;
;{session-id
; {:user-id 123
;  :expires-at ...}}
;
;Implement:
;
;create session
;refresh expiration
;delete session
;remove expired sessions


;15. Event Store
;
;Every update generates an immutable event.
;
;Example:
;
;{:state ...
; :events [...]}
;
;Design operations that append events while updating the current state.
;
;Think about event ordering and immutability.
;
;16. Concurrent Leaderboard
;
;Hundreds of threads update player scores simultaneously.
;
;Requirements:
;
;no lost updates
;efficient reads
;ability to retrieve the top 10 players
;
;Consider how your data structure affects performance.
;
;17. Publish/Subscribe System
;
;Use an atom to manage subscribers.
;
;Subscribers should be functions.
;
;Implement:
;
;subscribe
;unsubscribe
;publish an event to all subscribers
;
;Bonus: think about what happens if a subscriber throws an exception.