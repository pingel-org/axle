---
layout: page
title: Statistics
permalink: /tutorial/statistics/
---

## Uniform Distribution

Example

```scala mdoc
import cats.implicits._
import spire.algebra._
import axle.stats._

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra

val X = uniformDistribution(List(2d, 4d, 4d, 4d, 5d, 5d, 7d, 9d))
```

## Standard Deviation

Example

```scala mdoc
implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra

standardDeviation(X)
```

See also [Probability Model](/tutorial/probability_model/).
