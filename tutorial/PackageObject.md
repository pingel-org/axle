---
layout: page
title: axle.math and axle.logic Package Objects
permalink: /tutorial/axle_package_object/
---

This page describes functions in `axle.logic` and `axle.math` package objects.

Imports

```scala
import cats.implicits._

import spire.algebra._

import axle.logic._
import axle.math._

implicit val rngInt: Rng[Int] = spire.implicits.IntAlgebra
implicit val ringLong: Ring[Long] = spire.implicits.LongAlgebra
implicit val boolBoolean: Bool[Boolean] = spire.implicits.BooleanStructure
```

Logic aggregators `∃` and `∀`:

```scala
∃(List(1, 2, 3)) { i: Int => i % 2 == 0 }
// res0: Boolean = true

∀(List(1, 2, 3)) { i: Int => i % 2 == 0 }
// res1: Boolean = false
```

Sum and multiply aggregators `Σ` and `Π`.
Note that `Σ` and `Π` are also available in `spire.optional.unicode._`.

```scala
Σ((1 to 10) map { _ * 2 })
// res2: Int = 110

Π((1L to 10L) map { _ * 2 })
// res3: Long = 3715891200L
```

Doubles, triples, and cross-products

```scala
doubles(Set(1, 2, 3))
// res4: Seq[(Int, Int)] = List((1, 2), (1, 3), (2, 1), (2, 3), (3, 1), (3, 2))

triples(Set(1, 2, 3))
// res5: Seq[(Int, Int, Int)] = List(
//   (1, 2, 3),
//   (1, 3, 2),
//   (2, 1, 3),
//   (2, 3, 1),
//   (3, 1, 2),
//   (3, 2, 1)
// )

⨯(List(1, 2, 3))(List(4, 5, 6)).toList
// res6: List[(Int, Int)] = List(
//   (1, 4),
//   (1, 5),
//   (1, 6),
//   (2, 4),
//   (2, 5),
//   (2, 6),
//   (3, 4),
//   (3, 5),
//   (3, 6)
// )
```

Powerset

```scala
℘(0 until 4)
// res7: IndexedPowerSet[Int] = Iterable(
//   Set(),
//   Set(0),
//   Set(1),
//   Set(0, 1),
//   Set(2),
//   Set(0, 2),
//   Set(1, 2),
//   Set(0, 1, 2),
//   Set(3),
//   Set(0, 3),
//   Set(1, 3),
//   Set(0, 1, 3),
//   Set(2, 3),
//   Set(0, 2, 3),
//   Set(1, 2, 3),
//   Set(0, 1, 2, 3)
// )

val ps = ℘(Vector("a", "b", "c"))
// ps: IndexedPowerSet[String] = Iterable(
//   Set(),
//   Set("a"),
//   Set("b"),
//   Set("a", "b"),
//   Set("c"),
//   Set("a", "c"),
//   Set("b", "c"),
//   Set("a", "b", "c")
// )

ps.size
// res8: Int = 8

ps(7)
// res9: Set[String] = Set("a", "b", "c")
```

## Permutations

```scala
permutations(0 until 4)(2).toList
// res10: List[IndexedSeq[Int]] = List(
//   Vector(0, 1),
//   Vector(0, 2),
//   Vector(0, 3),
//   Vector(1, 0),
//   Vector(1, 2),
//   Vector(1, 3),
//   Vector(2, 0),
//   Vector(2, 1),
//   Vector(2, 3),
//   Vector(3, 0),
//   Vector(3, 1),
//   Vector(3, 2)
// )
```

## Combinations

```scala
combinations(0 until 4)(2).toList
// res11: List[IndexedSeq[Int]] = List(
//   Vector(0, 1),
//   Vector(0, 2),
//   Vector(0, 3),
//   Vector(1, 2),
//   Vector(1, 3),
//   Vector(2, 3)
// )
```

## Indexed Cross Product

```scala
val icp = IndexedCrossProduct(Vector(
  Vector("a", "b", "c"),
  Vector("d", "e"),
  Vector("f", "g", "h")))
// icp: IndexedCrossProduct[String] = Iterable(
//   List("a", "d", "f"),
//   List("a", "d", "g"),
//   List("a", "d", "h"),
//   List("a", "e", "f"),
//   List("a", "e", "g"),
//   List("a", "e", "h"),
//   List("b", "d", "f"),
//   List("b", "d", "g"),
//   List("b", "d", "h"),
//   List("b", "e", "f"),
//   List("b", "e", "g"),
//   List("b", "e", "h"),
//   List("c", "d", "f"),
//   List("c", "d", "g"),
//   List("c", "d", "h"),
//   List("c", "e", "f"),
//   List("c", "e", "g"),
//   List("c", "e", "h")
// )

icp.size
// res12: Int = 18

icp(4)
// res13: Seq[String] = List("a", "e", "g")
```
