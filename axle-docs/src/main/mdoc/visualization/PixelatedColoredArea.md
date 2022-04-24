# Pixelated Colored Area

This visualization shows the composition of a function `f: (X, Y) => V` with a
colorizing function `c: V => Color`
over a rectangular range on the `(X, Y)` plane.
`LengthSpace[X, X, Double]` and
`LengthSpace[Y, Y, Double]` must be implicitly in scope.

## Example

A few imports:

```scala mdoc:silent
import cats.implicits._

import axle._
import axle.visualize._
```

Define a function to compute an `Double` for each point on the plane `(x, y): (Double, Double)`

```scala mdoc
def f(x0: Double, x1: Double, y0: Double, y1: Double) = x0 + y0
```

Define a `toColor` function.
Here we first prepare an array of colors to avoid creating the objects during rendering.

```scala mdoc
val n = 100

// red to orange to yellow
val roy = (0 until n).map(i => Color(255, ((i / n.toDouble) * 255).toInt, 0)).toArray

def toColor(v: Double) = roy(v.toInt % n)
```

Define a `PixelatedColoredArea` to show `toColor ∘ f` over the range `(0,0)` to `(1000,1000)`
represented as a 400 pixel square.

```scala mdoc
val pca = PixelatedColoredArea(f, toColor, 400, 400, 0d, 1000d, 0d, 1000d)
```

Create PNG

```scala mdoc
import axle.awt._
import cats.effect._

pca.png[IO]("@DOCWD@/images/roy_diagonal.png").unsafeRunSync()
```

![ROY Diagonal](/images/roy_diagonal.png)

## Second example

More compactly:

```scala mdoc
import spire.math.sqrt

val m = 200

val greens = (0 until m).map(i => Color(0, ((i / m.toDouble) * 255).toInt, 0)).toArray

val gpPca = PixelatedColoredArea(
    (x0: Double, x1: Double, y0: Double, y1: Double) => sqrt(x0*x0 + y0*y0),
    (v: Double) => greens(v.toInt % m),
    400, 400,
    0d, 1000d,
    0d, 1000d)

import axle.awt._
import cats.effect._

gpPca.png[IO]("@DOCWD@/images/green_polar.png").unsafeRunSync()
```

![Green Polar](/images/green_polar.png)
