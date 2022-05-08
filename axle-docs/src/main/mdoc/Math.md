# Math

## Pythagorean Means

Arithmetic, Geometric, and Harmonic Means are all 'Pythagorean'.

See the wikipedia page on [Pythagorean Means](https://en.wikipedia.org/wiki/Pythagorean_means)
for more.

### Arithmetic, Geometric, and Harmonic Mean Examples

Imports

```scala mdoc:silent
import cats.implicits._

import spire.math.Real
import spire.algebra.Field
import spire.algebra.NRoot

import axle.math._

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra
```

Examples

Arithmetic mean

```scala mdoc
arithmeticMean(List(2d, 3d, 4d, 5d))
```

Geometric mean

```scala mdoc
geometricMean[Real, List](List(1d, 5d, 25d))
```

Harmonic mean

```scala mdoc
harmonicMean(List(2d, 3d, 4d, 5d))
```

### Generalized Mean

See the wikipedia page on [Generalized Mean](https://en.wikipedia.org/wiki/Generalized_mean).

When the parameter `p` is 1, it is the arithmetic mean.

```scala mdoc
generalizedMean[Double, List](1d, List(2d, 3d, 4d, 5d))
```

As `p` approaches 0, it is the geometric mean.

```scala mdoc
generalizedMean[Double, List](0.0001, List(1d, 5d, 25d))
```

At -1 it is the harmonic mean.

```scala mdoc
generalizedMean[Double, List](-1d, List(2d, 3d, 4d, 5d))
```

### Moving means

```scala mdoc:silent
import spire.math._
```

Moving arithmetic mean

```scala mdoc:height=15
movingArithmeticMean[List, Int, Double](
    (1 to 100).toList.map(_.toDouble),
    5)
```

Moving geometric mean

```scala mdoc:height=15
movingGeometricMean[List, Int, Real](
    List(1d, 5d, 25d, 125d, 625d),
    3)
```

Moving harmonic mean

```scala mdoc:height=15
movingHarmonicMean[List, Int, Real](
    (1 to 5).toList.map(v => Real(v)),
    3)
```

## Mean Average Precision at K

See the page on [mean average precision](https://www.kaggle.com/wiki/MeanAveragePrecision) at Kaggle

```scala mdoc:silent:reset
import spire.math.Rational
import axle.ml.RankedClassifierPerformance._
```

Examples (from [benhamner/Metrics](https://github.com/benhamner/Metrics))

```scala mdoc
meanAveragePrecisionAtK[Int, Rational](List(1 until 5), List(1 until 5), 3)

// should be Rational.one
```

```scala mdoc
meanAveragePrecisionAtK[Int, Rational](List(List(1, 3, 4), List(1, 2, 4), List(1, 3)), List(1 until 6, 1 until 6, 1 until 6), 3)

// should be Rational(37, 54) aka 0.6851851851851851
```

```scala mdoc
meanAveragePrecisionAtK[Int, Rational](List(1 until 6, 1 until 6), List(List(6, 4, 7, 1, 2), List(1, 1, 1, 1, 1)), 5)

// should be Rational(13, 50)
```

```scala mdoc
meanAveragePrecisionAtK[Int, Rational](List(List(1, 3), List(1, 2, 3), List(1, 2, 3)), List(1 until 6, List(1, 1, 1), List(1, 2, 1)), 3)

// should be Rational(11, 18) aka 0.611111111111111
```

## Pi

Two estimators for π

```scala mdoc:silent:reset
import axle.math._
```

### Wallis

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

### Monte Carlo

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

## Fibonacci

```scala mdoc:silent:reset
import axle.math._
```

### Linear using `foldLeft`

```scala mdoc
fibonacciByFold(10)
```

### Recursive

```scala mdoc
fibonacciRecursively(10)
```

Some alternatives that are not in Axle include

### Recursive with memoization

```scala mdoc:silent
val memo = collection.mutable.Map(0 -> 0L, 1 -> 1L)

def fibonacciRecursivelyWithMemo(n: Int): Long = {
  if (memo.contains(n)) {
    memo(n)
  } else {
    val result = fibonacciRecursivelyWithMemo(n - 2) + fibonacciRecursivelyWithMemo(n - 1)
    memo += n -> result
    result
  }
}
```

```scala mdoc
fibonacciRecursivelyWithMemo(10)
```

### Recursive squaring

Imports

```scala mdoc:silent
import org.jblas.DoubleMatrix

import cats.implicits._

import spire.algebra.EuclideanRing
import spire.algebra.NRoot
import spire.algebra.Rng

import axle._
import axle.jblas._

implicit val eucRingInt: EuclideanRing[Int] = spire.implicits.IntAlgebra
implicit val rngDouble: Rng[Double] = spire.implicits.DoubleAlgebra
implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra
implicit val laJblasDouble = axle.jblas.linearAlgebraDoubleMatrix[Double]
import laJblasDouble._
```

The fibonacci sequence at N can be generated by taking the Nth power of a special 2x2 matrix.
By employing the general-purpose strategy for exponentiation called "recursive squaring",
we can achieve sub-linear time.

```scala mdoc:silent
val base = fromColumnMajorArray(2, 2, List(1d, 1d, 1d, 0d).toArray)

def fibonacciSubLinear(n: Int): Long = n match {
  case 0 => 0L
  case _ => exponentiateByRecursiveSquaring(base, n).get(0, 1).toLong
}
```

Demo:

```scala mdoc
fibonacciSubLinear(78)
```

Note: Beyond 78 inaccuracies creep in due to the limitations of the `Double` number type.

## Ackermann

See the Wikipedia page on the [Ackermann function](http://en.wikipedia.org/wiki/Ackermann_function)

```scala mdoc:silent:reset
import axle.math._
```

The computational complexity is enormous.
Only for very small `m` and `n` can the function complete:

```scala mdoc
ackermann(1, 1)

ackermann(3, 3)
```

## Future Work

* Collatz Conjecture [vis](https://en.wikipedia.org/wiki/Collatz_conjecture#/media/File:Collatz-stopping-time.svg)
* Demo Mandelbrot with Rational
* Scrutinize `axle.math` and move out less reusable functions
* Complex Analysis

* Topoi
* N Queens
* Connection between dynamic programming and semiring
* Fourier transformations
* Blockchain
* Rainbow Tables
