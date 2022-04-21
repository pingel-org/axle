---
layout: page
title: Mandelbrot Set
permalink: /tutorial/mandelbrot/
---

See the wikipedia page on the [Mandelbrot Set](https://en.wikipedia.org/wiki/Mandelbrot_set)

First a couple imports:

```scala
import cats.implicits._
import spire.algebra.Field
import axle._
import axle.math._
```

Define a function to compute the Mandelbrot velocity at point on the plane `(x, y)`

```scala
implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
// fieldDouble: Field[Double] = spire.std.DoubleAlgebra@454746e2

val f = (x0: Double, x1: Double, y0: Double, y1: Double) => inMandelbrotSetAt(4d, x0, y0, 1000).getOrElse(-1)
// f: (Double, Double, Double, Double) => Int = <function4>
```

Import visualization package

```scala
import axle.visualize._
```

Define a "velocity to color" function

```scala
val colors = (0 to 255).map(g => Color(0, g, 255)).toArray
// colors: Array[Color] = Array(
//   Color(r = 0, g = 0, b = 255),
//   Color(r = 0, g = 1, b = 255),
//   Color(r = 0, g = 2, b = 255),
//   Color(r = 0, g = 3, b = 255),
//   Color(r = 0, g = 4, b = 255),
//   Color(r = 0, g = 5, b = 255),
//   Color(r = 0, g = 6, b = 255),
//   Color(r = 0, g = 7, b = 255),
//   Color(r = 0, g = 8, b = 255),
//   Color(r = 0, g = 9, b = 255),
//   Color(r = 0, g = 10, b = 255),
//   Color(r = 0, g = 11, b = 255),
//   Color(r = 0, g = 12, b = 255),
//   Color(r = 0, g = 13, b = 255),
//   Color(r = 0, g = 14, b = 255),
//   Color(r = 0, g = 15, b = 255),
//   Color(r = 0, g = 16, b = 255),
//   Color(r = 0, g = 17, b = 255),
//   Color(r = 0, g = 18, b = 255),
//   Color(r = 0, g = 19, b = 255),
//   Color(r = 0, g = 20, b = 255),
//   Color(r = 0, g = 21, b = 255),
//   Color(r = 0, g = 22, b = 255),
//   Color(r = 0, g = 23, b = 255),
//   Color(r = 0, g = 24, b = 255),
//   Color(r = 0, g = 25, b = 255),
//   Color(r = 0, g = 26, b = 255),
//   Color(r = 0, g = 27, b = 255),
//   Color(r = 0, g = 28, b = 255),
//   Color(r = 0, g = 29, b = 255),
//   Color(r = 0, g = 30, b = 255),
//   Color(r = 0, g = 31, b = 255),
//   Color(r = 0, g = 32, b = 255),
//   Color(r = 0, g = 33, b = 255),
//   Color(r = 0, g = 34, b = 255),
//   Color(r = 0, g = 35, b = 255),
//   Color(r = 0, g = 36, b = 255),
//   Color(r = 0, g = 37, b = 255),
//   Color(r = 0, g = 38, b = 255),
//   Color(r = 0, g = 39, b = 255),
//   Color(r = 0, g = 40, b = 255),
//   Color(r = 0, g = 41, b = 255),
//   Color(r = 0, g = 42, b = 255),
//   Color(r = 0, g = 43, b = 255),
//   Color(r = 0, g = 44, b = 255),
//   Color(r = 0, g = 45, b = 255),
//   Color(r = 0, g = 46, b = 255),
//   Color(r = 0, g = 47, b = 255),
// ...

val v2c = (v: Int) => if( v == -1 ) Color.black else colors((v*5) % 256)
// v2c: Int => Color = <function1>
```

Define a `PixelatedColoredArea` to show a range of the Mandelbrot Set.

```scala
val pca = PixelatedColoredArea(f, v2c, 400, 400, 0.25d, 0.45, 0.50, 0.70d)
// pca: PixelatedColoredArea[Double, Double, Int] = PixelatedColoredArea(
//   f = <function4>,
//   c = <function1>,
//   width = 400,
//   height = 400,
//   minX = 0.25,
//   maxX = 0.45,
//   minY = 0.5,
//   maxY = 0.7
// )
```

Create PNG

```scala
import axle.awt._
import cats.effect._

pca.png[IO]("mandelbrot.png").unsafeRunSync()
// res0: Boolean = true
```

![Mandelbrot Set](/tutorial/images/mandelbrot.png)

Some other parts of the set to explore:

```scala
val pca = PixelatedColoredArea(f, v2c, 1600, 1600, 0d, 1d, 0d, 1d)

val pca = PixelatedColoredArea(f, v2c, 1600, 1600, 0d, 0.5, 0.5, 1d)

val pca = PixelatedColoredArea(f, v2c, 1600, 1600, 0.25d, 0.5, 0.5, 0.75d)

val pca = PixelatedColoredArea(f, v2c, 3000, 3000, 0.20d, 0.45, 0.45, 0.70d)
```
