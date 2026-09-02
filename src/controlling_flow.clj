(ns controlling-flow)

; case  Takes an expression, and a set of clauses. Each clause can take the form of either: test-constant res
; if
; cond  Takes a set of test/expr pairs. It evaluates each test one at a time. If a test returns logical true, cond