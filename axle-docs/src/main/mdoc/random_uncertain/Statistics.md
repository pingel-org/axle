# Statistics

Common imports and implicits

```scala mdoc:silent
import cats.implicits._
import spire.algebra._
import axle.probability._

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
```

## Uniform Distribution

Example

```scala mdoc
val X = uniformDistribution(List(2d, 4d, 4d, 4d, 5d, 5d, 7d, 9d))
```

## Standard Deviation

Example

```scala mdoc:silent
import axle.stats._

implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra
```

```scala mdoc
standardDeviation(X)
```

See also [Probability Model](ProbabilityModel.md)
