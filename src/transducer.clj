(ns transducer)

; cat  A transducer which concatenates the contents of each input, which must be a collection, into the red

; completing Takes a reducing function f of 2 args and returns a fn suitable for transduce by adding an arity-1 sign