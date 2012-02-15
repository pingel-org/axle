
Enrichments
===========

Set Enrichments
-------------------

```scala
scala> Set(1, 2, 3).∃( _ % 2 == 0 )
res9: Boolean = true

scala> Set(1, 2, 3).∀( _ % 2 == 0 )
res10: Boolean = false

scala> Set(1, 2, 3).doubles
res11: Set[(Int, Int)] = Set((1,1), (3,3), (1,3), (1,2), (2,3), (2,1), (2,2), (3,2), (3,1))

scala> Set(1, 2, 3).triples
res12: Set[(Int, Int, Int)] = Set((2,2,1), (2,1,1), (3,2,1), (1,1,3), (1,1,2), (3,1,1), (3,3,3), (3,1,2), (3,3,1), (2,3,3), (3,3,2), (1,1,1), (2,1,3), (1,2,2), (1,2,3), (1,3,1), (3,1,3), (2,2,3), (3,2,2), (2,1,2), (2,3,1), (2,3,2), (1,3,2), (3,2,3), (2,2,2), (1,3,3), (1,2,1))
```

Boolean Enrichments
---------------

```scala
scala> import org.pingel.axle.Enrichments._
import org.pingel.axle.Enrichments._

scala> true and false
res0: Boolean = false

scala> true ∧ false
res1: Boolean = false

scala> true and true
res2: Boolean = true

scala> true ∧ true
res3: Boolean = true

scala> true or false
res4: Boolean = true

scala> true ∨ false
res5: Boolean = true

scala> true implies false
res6: Boolean = false

scala> true implies true
res7: Boolean = true
```

List Enrichments
----------------

```scala
scala> import org.pingel.axle.Enrichments._
import org.pingel.axle.Enrichments._

scala> (List(1, 2, 3) ⨯ List(4, 5, 6)).toList
res23: List[List[Int]] = List(List(1, 4), List(2, 4), List(3, 4), List(1, 5), List(2, 5), List(3, 5), List(1, 6), List(2, 6), List(3, 6))
```

See the Iterators tutorial for more discussion on how Cross Product should be defined.
