(ns boolean-logic)

;  and  Evaluates exprs one at a time, from left to right. If a form returns logical false (nil or false), and returns that value and doesn't evaluate any of the other expressions, otherwise it returns the value of the last expr. (and) returns true.
; any?  Returns true given any argument.


; <  Returns non-nil if nums are in monotonically increasing order, otherwise false.
; <=  Returns non-nil if nums are in monotonically non-decreasing order, otherwise false.
; =  Equality. Returns true if x equals y, false if not. Same as Java x.equals(y) except it also works for nil, and compares numbers and collections in a type-independent manner. Clojure's immutable data structures define equals() (and thus =) as a value, not an identity, comparison.

;==  Returns non-nil if nums all have the equivalent value (type-independent), otherwise false
;>  Returns non-nil if nums are in monotonically decreasing order, otherwise false.
; >=  Returns non-nil if nums are in monotonically non-increasing order, otherwise false.


; boolean  Coerce to boolean
; boolean?  Return true if x is a Boolean


;case  Takes an expression, and a set of clauses. Each clause can take the form of either: test-constant re