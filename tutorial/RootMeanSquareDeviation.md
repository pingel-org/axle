---
layout: page
title: Root-mean-square deviation
permalink: /tutorial/rmsd/
---

See the Wikipedia page on
[Root-mean-square deviation](https://en.wikipedia.org/wiki/Root-mean-square_deviation).

```scala
import cats.implicits._

import spire.algebra.Field
import spire.algebra.NRoot

import axle.stats._

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra
```

Given four numbers and an estimator function, compute the RMSD:

```scala
val data = List(1d, 2d, 3d, 4d)
// data: List[Double] = List(1.0, 2.0, 3.0, 4.0)

def estimator(x: Double) = x + 0.2

rootMeanSquareDeviation(data, estimator)
// res0: Double = 0.4000000000000002
```
