(ns data-types-map)

; assoc  When applied to a map, returns a new map of the same (hashed/sorted) type, that contains the mapping of key(s) to val(s). When applied to a vector, returns a new vector that contains val at index. Note - index must be <= (count vector).

;assoc!  When applied to a transient map, adds mapping of key(s) to val(s). When applied to a transient vector, sets the val at index. Note - index must be <= (count vector). Returns coll.

;assoc-in
;Associates a value in a nested associative structure, where ks is a sequence of keys and v is the new
