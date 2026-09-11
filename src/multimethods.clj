(ns multimethods)

(defmulti area :shape)
; keywords can be used as fn

(defmethod area :circle [{:keys [radius]}]
  (* Math/PI radius radius))

(defmethod area :rectangle [{:keys [width height]}]
  (* width height))

(comment
  (area {:shape :circle :radius 10})
  (area {:shape :rectangle :width 10 :height 10})
  )


(defmulti area2 (fn [shape & data] (:shape shape)))

(defmethod area2 :circle [shape radius]
  (prn "shape param" shape) ; have the original params not the result of dispatch fn
  (* Math/PI radius radius))

(defmethod area2 :rectangle [shape width height]
  (* width height))

(comment
  (area2 {:shape :circle} 10)
  (area2 {:shape :rectangle} 10  10)
  )


;Task 1 — Animal Sounds
;
;Create a multimethod called speak.
;
;Your system should support:
;
;:dog
;:cat
;:cow
;
;The caller should provide the animal type and the multimethod should return the appropriate sound.

(defmulti speak :animal)

(defmethod speak :dog [par]
  (prn "par " par)
  (prn "vau vau"))

(defmethod speak :cat [_]
  (prn "mau mau"))

(defmethod speak :cow [_]
  (prn "muu muu"))

(defmethod speak :default [_]
  (prn "can not speak"))

(comment
  (speak {:animal :dog :test 123})
  (speak {:animal :cat})
  (speak {:animal :mouse})
  ())

;Task 7 — User Permissions
;
;Create a multimethod called permissions.
;
;The function receives a user map containing :roles.
;
;A user may have one or multiple roles.
;
;Support:
;
;:guest
;:user
;:moderator
;:admin
;
;Each role has its own set of permissions.
;
;The system should determine the user's effective permissions based on all of their roles.
;
;Your implementation should handle:
;
;a user with one role
;a user with multiple roles
;a user with no roles
;an unknown role


(def role-permission {:guest [:read]
                      :user [:read :comment]
                      :moderator [:read :write :comment]
                      :admin [:read :write :comment :stats]})

(defmulti permissions
          (fn [user _]
            (let [count-roles (count (:roles user))]
              (cond
                (= count-roles 1) :one
                (> count-roles 1) :many
                :default :empty
                ))))

(defmethod permissions :empty [user role-permission]
  #{})

(defmethod permissions :one [user role-permission]
  (-> user
      :roles
      first
      role-permission
      set))

(defmethod permissions :many [user role-permission]
  (->> (:roles user)
       (mapcat #(% role-permission))
       set))

(comment

  (permissions {:user "123"
                :roles [:guest]} role-permission)

  (permissions {:user "123"
                :roles [:guest :user]} role-permission)

  (permissions {:user "123"
                :roles [:guest :user :admin]} role-permission)

  (permissions {:user "123"} role-permission)

  (permissions {:user "123"
                :roles [:not-existing-role]} role-permission)
  ; problem "this role is invalid" but my solution is "this user has no permissions"
  )


;Task 8 — Dispatch Based on Multiple Properties
;
;Create a multimethod called calculate-shipping.
;
;The dispatch decision should depend on two properties:
;
;customer type
;shipping method
;
;For example, customer types might include:
;
;:regular
;:premium
;
;Shipping methods might include:
;
;:standard
;:express
;
;Your dispatch value should represent the combination of these properties.
;
;Create implementations for different combinations.
;
;The goal is to practice creating a dispatch function that returns something more complex than a single keyword.



;Task 17 — User Role Hierarchy
;
;Create a hierarchy such as:
;
;user
;├── moderator
;└── admin
;
;Create a multimethod called can-access?.
;
;Different resources should require different levels of authorization.
;
;Use Clojure's hierarchy mechanism so that more specific roles can inherit behavior from broader roles.
;
;Test:
;
;regular users
;moderators
;administrators
;unknown roles



;Task 25 — Rate Limiter Strategy
;
;Design a rate-limiting system using multimethods.
;
;The system should support different strategies:
;
;fixed window
;sliding window
;token bucket
;leaky bucket
;
;The rate limiter should receive a request and configuration.
;
;The dispatch mechanism should select the appropriate strategy.