# Mandelbrot Set

See the wikipedia page on the [Mandelbrot Set](https://en.wikipedia.org/wiki/Mandelbrot_set)

First a couple imports:

```scala mdoc:silent
import cats.implicits._
import spire.algebra.Field
import axle._
import axle.math._
```

Define a function to compute the Mandelbrot velocity at point on the plane `(x, y)`

```scala mdoc
implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra

val f = (x0: Double, x1: Double, y0: Double, y1: Double) => inMandelbrotSetAt(4d, x0, y0, 1000).getOrElse(-1)
```

Import visualization package

```scala mdoc:silent
import axle.visualize._
```

Define a "velocity to color" function

```scala mdoc
val colors = (0 to 255).map(g => Color(0, g, 255)).toArray

val v2c = (v: Int) => if( v == -1 ) Color.black else colors((v*5) % 256)
```

Define a `PixelatedColoredArea` to show a range of the Mandelbrot Set.

```scala mdoc
val pca = PixelatedColoredArea(f, v2c, 400, 400, 0.25d, 0.45, 0.50, 0.70d)
```

Create PNG

```scala mdoc
import axle.awt._
import cats.effect._

pca.png[IO]("mandelbrot.png").unsafeRunSync()
```

![Mandelbrot Set](/tutorial/images/mandelbrot.png)

Some other parts of the set to explore:

```scala
val pca = PixelatedColoredArea(f, v2c, 1600, 1600, 0d, 1d, 0d, 1d)

val pca = PixelatedColoredArea(f, v2c, 1600, 1600, 0d, 0.5, 0.5, 1d)

val pca = PixelatedColoredArea(f, v2c, 1600, 1600, 0.25d, 0.5, 0.5, 0.75d)

val pca = PixelatedColoredArea(f, v2c, 3000, 3000, 0.20d, 0.45, 0.45, 0.70d)
```
