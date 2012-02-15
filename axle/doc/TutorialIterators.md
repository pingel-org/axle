
Iterators
=========

PowerSet
--------

```scala
scala> import org.pingel.axle.iterator.PowerSet
import org.pingel.axle.iterator.PowerSet

scala> ℘(List("a", "b")).toList
res10: List[scala.collection.Set[java.lang.String]] = List(Set(), Set(a), Set(b), Set(a, b))
```

Permuter
--------

```scala
scala> import org.pingel.axle.iterator.Permuter
import org.pingel.axle.iterator.Permuter

scala> new Permuter(List("a", "b", "c"), 2)
res17: org.pingel.axle.iterator.Permuter[java.lang.String] = Permuter(List(b, a), List(b, c), List(a, b), List(a, c), List(c, b), List(c, a))
```

Cross Product
-------------

```scala
scala> import org.pingel.axle.iterator.CrossProduct
import org.pingel.axle.iterator.CrossProduct

scala> new CrossProduct(List(List("a", "b"), List(1, 2)))
res21: org.pingel.axle.iterator.CrossProduct[Any] = CrossProduct(List(a, 1), List(b, 1), List(a, 2), List(b, 2))
```
