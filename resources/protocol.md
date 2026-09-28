# Protocol 

Set of non implements methods (only names) and their signatures with name

Implementation of the methods using extend

Implementation check order (BASED on type)
* direct definitions `defrecord` `deftype` `reify`
* metadata definitions
* external extensions `extend` `extend-type` `extend-protocol`

Conceptually similar to an interface in Java
* define behavior for common abstraction

## Extend existing types

* add fn to work with string object
* use `extend-type` `extend-protocol` `extend`
* can extend protocols to maps
  * can separate data representation from behavior
* can work with java classes