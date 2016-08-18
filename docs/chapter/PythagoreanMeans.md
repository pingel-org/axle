Pythagorean Means
=================

Arithmetic, Geometric, and Harmonic Means are all 'Pythagorean'.

See the wikipedia page on <a href="https://en.wikipedia.org/wiki/Pythagorean_means">Pythagorean Means</a>
for more.

Arithmetic, Geometric, and Harmonic Mean Examples
-------------------------------------------------

Imports

```scala
scala> import axle._
import axle._

scala> import axle.algebra._
import axle.algebra._
```

Examples

```scala
scala> import spire.implicits.DoubleAlgebra
import spire.implicits.DoubleAlgebra

scala> arithmeticMean(List(2d, 3d, 4d, 5d))
res0: Double = 3.5

scala> import spire.math.Real
import spire.math.Real

scala> geometricMean[Real, List[Real]](List(1d, 5d, 25d))
res1: spire.math.Real = 5

scala> harmonicMean(List(2d, 3d, 4d, 5d))
res2: Double = 3.116883116883117
```

Generalized Mean
----------------

See the wikipedia page on <a href="https://en.wikipedia.org/wiki/Generalized_mean">Generalized Mean</a>.

When the parameter `p` is 1, it is the arithmetic mean.
At -1 it is the harmonic mean.
As `p` approaches 0, it is the geometric mean.

```scala
scala> generalizedMean[Double, List[Double]](1d, List(2d, 3d, 4d, 5d))
res3: Double = 3.5

scala> generalizedMean[Double, List[Double]](-1d, List(2d, 3d, 4d, 5d))
res4: Double = 3.116883116883117

scala> generalizedMean[Double, List[Double]](0.0001, List(1d, 5d, 25d))
res5: Double = 5.00043173370165
```
