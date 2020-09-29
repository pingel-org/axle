---
layout: page
title: axle.math and axle.logic Package Objects
permalink: /tutorial/axle_package_object/
---

This page describes functions in `axle.logic` and `axle.math` package objects.

Imports

```scala mdoc:silent
import cats.implicits._

import spire.algebra._

import axle.logic._
import axle.math._

implicit val rngInt: Rng[Int] = spire.implicits.IntAlgebra
implicit val ringLong: Ring[Long] = spire.implicits.LongAlgebra
implicit val boolBoolean: Bool[Boolean] = spire.implicits.BooleanStructure
```

Logic aggregators `∃` and `∀`:

```scala mdoc
∃(List(1, 2, 3)) { i: Int => i % 2 == 0 }

∀(List(1, 2, 3)) { i: Int => i % 2 == 0 }
```

Sum and multiply aggregators `Σ` and `Π`.
Note that `Σ` and `Π` are also available in `spire.optional.unicode._`.

```scala mdoc
Σ((1 to 10) map { _ * 2 })

Π((1L to 10L) map { _ * 2 })
```

Doubles, triples, and cross-products

```scala mdoc
doubles(Set(1, 2, 3))

triples(Set(1, 2, 3))

⨯(List(1, 2, 3))(List(4, 5, 6)).toList
```

Powerset

```scala mdoc
℘(0 until 4)

val ps = ℘(Vector("a", "b", "c"))

ps.size

ps(7)
```

## Permutations

```scala mdoc
permutations(0 until 4)(2).toList
```

## Combinations

```scala mdoc
combinations(0 until 4)(2).toList
```

## Indexed Cross Product

```scala mdoc
val icp = IndexedCrossProduct(Vector(
  Vector("a", "b", "c"),
  Vector("d", "e"),
  Vector("f", "g", "h")))

icp.size

icp(4)
```
