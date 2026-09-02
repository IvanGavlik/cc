# Atoms

Atoms are simples concurrency primitive. They manage independent synchronous shared state without transactions

## Into

Multiple threads might try to update variable simultaneously
Clojure ensures that even if 100 threads call swap! at the same time evey increment is counted correctly
* todo how this works

Primitive is basic building block. Clojure has several primitives for managing shared state
* atom - simples one
* ref
* agent
* volatile! 

Atom is thread safe box that stores one value

Shared state
* variable belong to one fn 
* for atom every part of your program can access it

Synchronous
* when `swap!` returns the update has already happened

Transactions are needed when multiple values must change as one unit
(both updated happen or neither update happens)
Atom protect one value at a time - so no transaction

## Basic operations

Creating
`(def counter (atom 0))`

Dereference 
```clojure
@counter
; or
(deref counter)
```

New value - provide fn 
```clojure
(swap! counter inc)
(swap! counter + 5) ; send arguments
```

With collections
```clojure
(def user
  (atom {:name "Alice"
         :age 30}))

(swap! user update :age inc)
(swap! user assoc :city "Paris")
```

Compare and set
* update if the current value is exactly what you expect
```clojure
(def a (atom 10))

(compare-and-set! a 10 20)
;; => true

(compare-and-set! a 10 30)
;; => false
```

Reset value
```clojure
(reset! counter 100)

@counter
;; => 100
```


## Internally `swap!` (changing value)

compare-and-swap (CAS) loop:
* Read current value.
* Compute new value.
* Try to install it.
* If another thread changed the atom first, retry automatically.

Because the update function may be retried, it must be free of side effects.

## When to use it 
* Caches
* Counters
* Configuration
* Application state (for many applications)
* Statistics
* Any single piece of shared state

So One independent value (it can be map)? Use an atom.

## Implementation
* atom is implemented as `AtomicReference` in java
  * which holds value as `private volatile V value;`
* uses atomic compare-and-set (CAS) before updating value 
  * If another thread changes the atom before my update succeeds it calls the fn again
  * the fn should be ideally pure

## TODO
* how they are implemented look into implementatoin