# Multimethod

## Intro
Polymorphism based on the result of a dispatch function

`defmulti` fn that know how to choose an implementation
* it job is to decide which implementation should call
`defmethod` supplies/provides the implementation/behavior

Example
```clojure
(defmulti area :shape)

(defmethod area :circle ...)
(defmethod area :rectangle ...)
```
Explanation
Create a function called area, and when somebody calls it, 
use :shape to determine which implementation should run.
it is dispatcher

For the area multimethod, when the dispatch value is :circle, use this function.

The flow

```
{:shape :circle ...}
->
disptah fn 
->
:circle
->
(defmetho area :circle)
```

## Dispatch fn

The basic syntax is:
`(defmulti name dispatch-fn)`


You can dispatch on anything you can compute
* keyword
* values
* map
* hierarchies (::admin ::user) -> ::admin -> user
  * use `derive`
* classes
* functions (can have arguments)

Example with function

```clojure

(defmulti describe
  (fn [x]
    (cond
      (number? x) :number
      (string? x) :string
      (map? x)    :map
      :else       :unknown)))

```


## Defmethod

Basic form
`(defmethod multimethod-name dispatch-value
  [arguments]
  body)`

The dispatch value has to correspond to something the multimethod can produce.

You can provide a default implementation:

```clojure
(defmethod describe :default
  [_]
  "Unknown entity")
```

## Passing arguments

The defmethod receives the same arguments that were passed to the defmulti.

The dispatch function does not transform the arguments or pass new arguments to the method. It only looks at the arguments and returns a dispatch value.

`defmethod` receives the ORIGINAL arguments

### Option all in map

Here is piece of data figure out what it represent

```clojure

(defmulti area :shape)

(defmethod area :circle
  [{:keys [radius]}]
  (* Math/PI radius radius))

(area {:shape :circle
       :radius 10})

```

### Type and data separately

```clojure

(defmulti area
  (fn [shape _]
    (:shape shape)))

(defmethod area :circle
  [_ radius]
  (* Math/PI radius radius))

(area {:shape :circle} 10)


```

### Dispatch value itself is separate

Tell me explicitly which implementation you want, then give me its parameters

```clojure

(defmulti area identity)

(defmethod area :circle
  [_ radius]
  (* Math/PI radius radius))

(area :circle 10)

```

The dispatch function determines what information is used for dispatch.
It doesn't have to use the first argument.

## Usage

Useful when the behavior varies along some dimension of your domain,
you want to express it declaratively
Examples
Payment can be done by :credit-card :paypal :bank-transfer

                    charge
                      │
                dispatch function
                      │
          ┌───────────┼───────────┐
          │           │           │
      :credit-card  :paypal  :bank-transfer
          │           │           │
          ▼           ▼           ▼
       method       method       method

Multimethods are powerful, but that doesn't mean every if should become a multimethod.

They are open for extension

## Multimethod vs cond

This works 

```clojure

(defn area [shape]
(cond
(= (:shape shape) :circle)
; some impl

    (= (:shape shape) :rectangle)
; some impl

    (= (:shape shape) :triangle)
; some impl
    ))

```

But as the number of cases grows, the function becomes a central dispatch point that must continually be modified.

With `multmethod` we have 
* impl independently extensible
* plugin-style architecture
