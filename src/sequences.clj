(ns sequences)

; butlast  Return a seq of all but the last item in coll, in linear time

; concat  Returns a lazy seq representing the concatenation of the elements in the supplied colls.

;coll?  Returns true if x implements IPersistentCollection


;conj  conj[oin]. Returns a new collection with the xs 'added'. (conj nil item) returns (item). (conj coll) returns coll. (conj) returns []. The 'addition' may happen at different 'places' depending on the concrete type.
;conj!  Adds x to the transient collection, and return coll. The 'addition' may happen at different 'places' depending on the concrete type.
;cons  Returns a new seq where x is the first element and seq is the rest.

; contains?  Returns true if key is present in the given collection, otherwise returns false. Note that for numeric

; todo maybe split sequences and collections


;count  Returns the number of items in the collection. (count nil) returns 0. Also works on strings, arrays, and Java Collections and Maps
; counted?  Returns true if coll implements count in constant time

; cycle  Returns a lazy (infinite!) sequence of repetitions of the items in coll.