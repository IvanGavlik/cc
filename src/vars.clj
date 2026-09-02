(ns vars
  (:import java.lang.Integer))

; *1 *2 *e

(comment
  (+ 1 2)
  ; most recent value printer
  *1
  (+ 2 2)
  ; 2th most recent value printed
  *2

  (ns-refers)
  *e
  *err*
  )

;  *assert* is a dynamic var (a global, rebindable flag) that controls whether the assert macro actually performs its
;  checks at compile time
(defn str->int [x]
  {:pre [(string? x)]
   :post [(> % 10)]}
  (Integer/valueOf x))

; TODO
; assert
;Evaluates expression x and throws an AssertionError with optional message if x does not evaluate to logical tru


(comment
  ; assertion failed
  (str->int 11)

  (str->int "12")
  (str->int "9")

  (set! *assert* true)
  )


; *command-line-args*

(defn print-args [args]
  {:pre [(not-empty args)]}
  (prn args))

(comment
  (binding [*command-line-args* ["hello" "world" "42"]]
    (print-args *command-line-args*))

  (binding [*command-line-args* nil]
    (print-args *command-line-args*))
  )


; *data-readers*
; dynamic var holding a map from tagged literal symbols to functions
; the #tag value forms you may have seen

(defn data-reader-append-test-2 [value]
  (str value " test 2"))

(comment

  (read-string "#uuid \"901982eb-7072-424c-ac61-c5f8db1769e7\"")

  (binding [*data-readers* {'t #(str % " test")
                            't2 vars/data-reader-append-test-2}]

    (prn (read-string "#t 123")
         (read-string "#t2 456")))
  )

(defn handle-user-cmd [cmd]
  (map read-string cmd))

(comment
  (binding [*command-line-args* ["#fn ivan" "#ln gavlik"]
            *data-readers* {'fn #(str "first name is " %)
                            'ln #(str "last name is " %)}]
    (prn (handle-user-cmd *command-line-args*)))
  )


(comment
  ; current namespace
  *ns*

  *print-length*
  *print-level*
  )


;; TODO
; https://clojuredocs.org/clojure.core/alter-var-root