---
layout: page
title: axle._ Package Object
permalink: /tutorial/axle_package_object/
---

This page describes the axle package object.

## Enriched GenTraversable

Imports

```scala mdoc:silent
import axle._
import spire.implicits._
import spire.optional.unicode._
import spire.implicits.eqOps
```

Common aggregators ∃, ∀, Σ, Π:

```scala mdoc
∃(List(1, 2, 3)) { i: Int => i % 2 == 0 }

∀(List(1, 2, 3)) { i: Int => i % 2 == 0 }

Σ((1 to 10) map { _ * 2 })

Π((1L to 10L) map { _ * 2 })
```

Doubles, triples, and cross-products

```scala mdoc
Set(1, 2, 3).doubles

Set(1, 2, 3).triples

(List(1, 2, 3) ⨯ List(4, 5, 6)).toList
```

## Enriched Boolean

TODO: document `and`, `∧`, `or`, `∨`, `implies`

## Indexed Power Set

```scala mdoc
(0 until 4).℘

val ps = Vector("a", "b", "c").℘

ps.size

ps(7)
```

## Permutations

```scala mdoc
(0 until 4).permutations(2).toList
```

## Combinations

```scala mdoc
(0 until 4).combinations(2).toList
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
