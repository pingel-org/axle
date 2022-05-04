# Reservoir Sampling

Reservoir Sampling is the answer to a common interview question.

```scala mdoc:silent
import spire.random.Generator.rng
import spire.algebra.Field

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra

import axle.stats._
```

Demonstrate it uniformly sampling 15 of the first 100 integers

```scala mdoc
val sample = reservoirSampleK(15, LazyList.from(1), rng).drop(100).head
```

The mean of the sample should be in the ballpark of the mean of the entire list (50.5):

```scala mdoc
import axle.math.arithmeticMean

arithmeticMean(sample.map(_.toDouble))
```

Indeed it is.
