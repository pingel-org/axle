---
layout: page
title: Reservoir Sampling
permalink: /tutorial/reservoir_sampling/
---

Reservoir Sampling is the answer to a common interview question.

```scala
import spire.random.Generator.rng
import spire.algebra.Field

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra

import axle.stats._
import axle.math.arithmeticMean
```

Demonstrate it uniformly sampling 15 of the first 100 integers

```scala
val sample = reservoirSampleK(15, LazyList.from(1), rng).drop(100).head
// sample: List[Int] = List(
//   93,
//   92,
//   86,
//   81,
//   80,
//   50,
//   46,
//   45,
//   43,
//   42,
//   37,
//   34,
//   27,
//   25,
//   12
// )

arithmeticMean(sample.map(_.toDouble))
// res0: Double = 52.86666666666667
```

The mean of the sample should be in the ballpark of the mean of the entire list -- 50.
