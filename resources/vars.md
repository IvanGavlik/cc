# Clojure vars

* named references (symbol) to values of fn 
* they live in a namespace
* mutable reference to an immutable value

Note symbol is just name

Functions are actually stored in vars 
```clojure
(defn greet [name] (str "hello" name))
; is equal to 
(def greet (fn [name] (str "hello" name)))
; because defn is macro
```

Var is separate from the value so you can redefine it 

## Dynamic vars

Allows temporary thread-local bindings
```clojure
(def ^:dynamic *debug* false)

; temporarily change it
(binding [*debug* true]
 (prinlnt *debug*))
```

## Metadata

```clojure
(def ^{:doc "A counter"} counter 0)
```

## Symbol vs Var

### Symbol

Pice of code that names something
```clojure
(type 'x)
;; => clojure.lang.Symbol
```

Writen as `'x`
Exists in code and data

### Var 
Runtime object that point to immutable value
Written as #'x or (var x)
Exists at runtime


Symbol      Var          Value
------   ----------   -------
x  ──▶ #'user/x ──▶ 42


Example explained
```clojure
(def x 43)
x
; 43
```
In the code 
* find the symbol
* resolve it to var `#'user/x`
* dereference var
* produce 42

Note symbol can refere to *local binding* (not a Var) or local variable
```clojure
; local binding
(let [x 10]
  x)

; local variable
(fn [x] x) 
```

## Immutability

Vars are mutable 
The values they point are (usually immutable)

The var can change what it points to the data/vale cannot change

Imagine a program without vars 
we have immutable values but no way to give them names 
## Vars and namespaces

Namespace is mapping from symbols to vars
Namespace is object that contains vars

```clojure
(ns math)
; internaly
; pi --> var
; inc --> var
```

### Referring to another namespace

```clojure
(ns animals)
(def dog "Labrador")

(ns zoo
  (:require [animals]))

animals/dog ;; TODO why not (animals/dog) 


(ns zoo-2
  (:require [animals :as a])) ; aliases

a/dog ; shorthand for animals/dog


(ns zoo-2
  (:require [animals :refer [dog]])) ; refering vars

dog ; shorthland for a/dog
```


#### Dereferencing

Var fully qualified name includes namespace: math/pi or user/x

Example
```clojure
(def x 42)
```
When evaluating steps
1. find var
2. deref var
3. 42

you can also do dereferencing by: 
```clojure
@#'x 
; or
(deref #'x)
```

## Evaluation pipeline simplify

Examples
```clojure
(double-it x)
```

1. Soruce code contains symbols (double-it x)
Symbols is name ('x, it has type clojure.lang.Symbol)

2. Compiler resolve namespace
What does the symbol x refer to in the current namespace
So we get namespace-name/x var
Note: namespace dosen't store values it stores vars

3. Var is runtime object so it dereferences it to get values

4. Invocation (of the function or the value)

Why we have vars, what not directly have values, because with vars we have
* redefintion (REPL)
* metadata
* dynaming binding

## Special Symbols

### ' — Quote

Dont evaluate this treat it as data

Example
```clojure
(def x 42)
```
This means 
1. Resolve symbol
2. Find the var
3. Return it value 

```clojure
'x
; => x
```
This means 
1. Return symbol

### #' — Var Quote

Resolve this symbol to its Var

Example
```clojure
(def x 42)
#'x
;; => #'user/x
```
The result is not 42
the result is the Var object
To get a value you can dereference it using `@`
```clojure
@#'x
;; => 42
; or

(deref #'x)
```

When to use it 
* Metadata is attached to the Var
* Testing and mocking
* Implementing macros
* Dynamic Vars and advanced runtime features


## TODO
* what is runtime object in clojure 
* what is (deref #'x)
* referencing in clojure 

symbols vs vars in clojure explain this
Exists in code and data
Exists at runtime
with code examples 
* implementation of def and ns
* Var  fully-qualified names
* clojure when not to use paranthences when refercing/using variable and why 