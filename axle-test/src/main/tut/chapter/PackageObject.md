
axle._ Package Object
=====================

This page describes the axle package object.

Enriched GenTraversable
-----------------------

Imports

```tut
import axle._
import spire.implicits._
import spire.optional.unicode._
import spire.implicits.eqOps
```

Common aggregators ∃, ∀, Σ, Π:

```tut
∃(List(1, 2, 3)) { i: Int => i % 2 == 0 }

∀(List(1, 2, 3)) { i: Int => i % 2 == 0 }

Σ((1 to 10) map { _ * 2 })

Π((1L to 10L) map { _ * 2 })
```

Doubles, triples, and cross-products

```tut
Set(1, 2, 3).doubles

Set(1, 2, 3).triples

(List(1, 2, 3) ⨯ List(4, 5, 6)).toList
```

Enriched Boolean
----------------

TODO: document `and`, `∧`, `or`, `∨`, `implies`

Indexed Power Set
-----------------

```tut
(0 until 4).℘

val ps = Vector("a", "b", "c").℘

ps.size

ps(7)
```

Permutations
------------

```tut
(0 until 4).permutations(2).toList
```

Combinations
------------

```tut
(0 until 4).combinations(2).toList
```

Indexed Cross Product
---------------------

```tut
val icp = IndexedCrossProduct(Vector(
  Vector("a", "b", "c"),
  Vector("d", "e"),
  Vector("f", "g", "h")))

icp.size

icp(4)
```
