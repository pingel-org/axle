---
layout: page
title: Root-mean-square deviation
permalink: /tutorial/rmsd/
---

See the Wikipedia page on
[Root-mean-square deviation](https://en.wikipedia.org/wiki/Root-mean-square_deviation).

```scala mdoc:silent
import spire.implicits.DoubleAlgebra
import axle.stats._
```

Given four numbers and an estimator function, compute the RMSD:

```scala mdoc
val data = List(1d, 2d, 3d, 4d)

def estimator(x: Double) = x + 0.2

rootMeanSquareDeviation(data, estimator)
```
