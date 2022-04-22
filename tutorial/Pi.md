---
layout: page
title: π
permalink: /tutorial/pi/
---

Two estimators for π

```scala
import axle.math._
```

## Wallis

The first is attributed to Englishman John Wallis (1616 - 1703) who published this
function in 1655.
It is quite slow.

```scala
wallisΠ(100).toDouble
// res0: Double = 3.1337874906281624

wallisΠ(200).toDouble
// res1: Double = 3.137677900950936

wallisΠ(400).toDouble
// res2: Double = 3.1396322219293964

wallisΠ(800).toDouble
// res3: Double = 3.1406116723489452

wallisΠ(1600).toDouble
// res4: Double = 3.1411019714193746

wallisΠ(3200).toDouble
// res5: Double = 3.141347264592393
```

## Monte Carlo

```scala
import cats.implicits._
import spire.algebra.Field

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
```

See the Wikipedia page on [Monte Carlo Methods](https://en.wikipedia.org/wiki/Monte_Carlo_method)

This particular implementation requires that the number of trials be
passed as a type `F` such that witnesses for typeclasses `Aggregatable`, `Finite`, and `Functor`
are available in implicit scope.

While this may may seem initially over-engineered, it allows `F` as varied as `List` and Spark's `RDD`
to be used to represent the number of trials and support the Monte Carlo simulation and
resulting aggregation.

```scala
monteCarloPiEstimate((1 to 10000).toList, (n: Int) => n.toDouble)
// res6: Double = 3.1204
```
