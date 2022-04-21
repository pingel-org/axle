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
//   100,
//   94,
//   93,
//   88,
//   67,
//   57,
//   48,
//   45,
//   44,
//   43,
//   40,
//   25,
//   18,
//   16,
//   11
// )

arithmeticMean(sample.map(_.toDouble))
// res0: Double = 52.6
```

The mean of the sample should be in the ballpark of the mean of the entire list -- 50.
