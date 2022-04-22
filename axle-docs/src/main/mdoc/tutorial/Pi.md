# Pi

Two estimators for π

```scala mdoc:silent
import axle.math._
```

## Wallis

The first is attributed to Englishman John Wallis (1616 - 1703) who published this
function in 1655.
It is quite slow.

```scala mdoc
wallisΠ(100).toDouble

wallisΠ(200).toDouble

wallisΠ(400).toDouble

wallisΠ(800).toDouble

wallisΠ(1600).toDouble

wallisΠ(3200).toDouble
```

## Monte Carlo

```scala mdoc:silent
import cats.implicits._
import spire.algebra.Field

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
```

See the Wikipedia page on [Monte Carlo Methods](https://en.wikipedia.org/wiki/Monte_Carlo_method)

This particular implementation requires that the number of trials be
passed as a type `F` such that witnesses for typeclasses `Aggregatable`, `Finite`, and `Functor`
are available in implicit scope.

While this may may seem initially over-engineered, it allows `F` as varied as `List` and Spark's `RDD`
to be used to represent the number of trials and support the Monte Carlo simulation and
resulting aggregation.

```scala mdoc
monteCarloPiEstimate((1 to 10000).toList, (n: Int) => n.toDouble)
```
