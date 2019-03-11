---
layout: page
title: Reservoir Sampling
permalink: /tutorial/reservoir_sampling/
---

Reservoir Sampling is the answer to a common interview question.

```scala mdoc:silent
import spire.random.Generator.rng
import spire.algebra.Field

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra

import axle.stats._
import axle.math.arithmeticMean
```

Demonstrate it uniformly sampling 15 of the first 100 integers

```scala mdoc
val sample = reservoirSampleK(15, Stream.from(1), rng).drop(100).head

arithmeticMean(sample.map(_.toDouble))
```

The mean of the sample should be in the ballpark of the mean of the entire list -- 50.
