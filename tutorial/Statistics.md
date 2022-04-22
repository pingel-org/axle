---
layout: page
title: Statistics
permalink: /tutorial/statistics/
---

## Uniform Distribution

Example

```scala
import cats.implicits._
import spire.algebra._
import axle.probability._

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
// fieldDouble: Field[Double] = spire.std.DoubleAlgebra@38677cfc

val X = uniformDistribution(List(2d, 4d, 4d, 4d, 5d, 5d, 7d, 9d))
// X: ConditionalProbabilityTable[Double, spire.math.Rational] = ConditionalProbabilityTable(
//   p = HashMap(5.0 -> 1/4, 9.0 -> 1/8, 2.0 -> 1/8, 7.0 -> 1/8, 4.0 -> 3/8)
// )
```

## Standard Deviation

Example

```scala
import axle.stats._

implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra
// nrootDouble: NRoot[Double] = spire.std.DoubleAlgebra@38677cfc

standardDeviation(X)
// res0: Double = 2.0
```

See also [Probability Model](/tutorial/probability_model/).
