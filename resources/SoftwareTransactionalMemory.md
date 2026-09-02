ClojureDocs: alter is part of Clojure's STM (Software Transactional Memory) system. It is used to modify a Ref inside a transaction started with dosync.

The simplest mental model:

ref → a shared, coordinated mutable location
dosync → starts a transaction
alter → says "inside this transaction, update this ref using this function"
Basic example

; commute  Must be called in a transaction. Sets the in-transaction-value of ref to: (apply fun in-transaction-value