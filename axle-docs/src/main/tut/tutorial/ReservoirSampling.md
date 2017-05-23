---
layout: page
title: Reservoir Sampling
permalink: /tutorial/reservoir_sampling/
---

Reservoir Sampling is the answer to a common interview question.

```tut:silent
import axle.stats._
import spire.math.Rational
import axle.math.arithmeticMean
import spire.implicits.DoubleAlgebra
```

Demonstrate it uniformly sampling 15 of the first 100 integers

```tut:book
val sample = reservoirSampleK(15, Stream.from(1)).drop(100).head

val mean = arithmeticMean(sample.map(_.toDouble))
```

The mean of the sample should be in the ballpark of the mean of the entire list -- 50.

