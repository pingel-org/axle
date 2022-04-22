---
layout: page
title: Pixelated Colored Area
permalink: /tutorial/pixelated_colored_area/
---

This visualization shows the composition of a function `f: (X, Y) => V` with a
colorizing function `c: V => Color`
over a rectangular range on the `(X, Y)` plane.
`LengthSpace[X, X, Double]` and
`LengthSpace[Y, Y, Double]` must be implicitly in scope.

## Example

A few imports:

```scala
import cats.implicits._

import axle._
import axle.visualize._
```

Define a function to compute an `Double` for each point on the plane `(x, y): (Double, Double)`

```scala
def f(x0: Double, x1: Double, y0: Double, y1: Double) = x0 + y0
```

Define a `toColor` function.
Here we first prepare an array of colors to avoid creating the objects during rendering.

```scala
val n = 100
// n: Int = 100

// red to orange to yellow
val roy = (0 until n).map(i => Color(255, ((i / n.toDouble) * 255).toInt, 0)).toArray
// roy: Array[Color] = Array(
//   Color(r = 255, g = 0, b = 0),
//   Color(r = 255, g = 2, b = 0),
//   Color(r = 255, g = 5, b = 0),
//   Color(r = 255, g = 7, b = 0),
//   Color(r = 255, g = 10, b = 0),
//   Color(r = 255, g = 12, b = 0),
//   Color(r = 255, g = 15, b = 0),
//   Color(r = 255, g = 17, b = 0),
//   Color(r = 255, g = 20, b = 0),
//   Color(r = 255, g = 22, b = 0),
//   Color(r = 255, g = 25, b = 0),
//   Color(r = 255, g = 28, b = 0),
//   Color(r = 255, g = 30, b = 0),
//   Color(r = 255, g = 33, b = 0),
//   Color(r = 255, g = 35, b = 0),
//   Color(r = 255, g = 38, b = 0),
//   Color(r = 255, g = 40, b = 0),
//   Color(r = 255, g = 43, b = 0),
//   Color(r = 255, g = 45, b = 0),
//   Color(r = 255, g = 48, b = 0),
//   Color(r = 255, g = 51, b = 0),
//   Color(r = 255, g = 53, b = 0),
//   Color(r = 255, g = 56, b = 0),
//   Color(r = 255, g = 58, b = 0),
//   Color(r = 255, g = 61, b = 0),
//   Color(r = 255, g = 63, b = 0),
//   Color(r = 255, g = 66, b = 0),
//   Color(r = 255, g = 68, b = 0),
//   Color(r = 255, g = 71, b = 0),
//   Color(r = 255, g = 73, b = 0),
//   Color(r = 255, g = 76, b = 0),
//   Color(r = 255, g = 79, b = 0),
//   Color(r = 255, g = 81, b = 0),
//   Color(r = 255, g = 84, b = 0),
//   Color(r = 255, g = 86, b = 0),
//   Color(r = 255, g = 89, b = 0),
//   Color(r = 255, g = 91, b = 0),
//   Color(r = 255, g = 94, b = 0),
//   Color(r = 255, g = 96, b = 0),
//   Color(r = 255, g = 99, b = 0),
//   Color(r = 255, g = 102, b = 0),
//   Color(r = 255, g = 104, b = 0),
//   Color(r = 255, g = 107, b = 0),
//   Color(r = 255, g = 109, b = 0),
//   Color(r = 255, g = 112, b = 0),
//   Color(r = 255, g = 114, b = 0),
//   Color(r = 255, g = 117, b = 0),
//   Color(r = 255, g = 119, b = 0),
// ...

def toColor(v: Double) = roy(v.toInt % n)
```

Define a `PixelatedColoredArea` to show `toColor ∘ f` over the range `(0,0)` to `(1000,1000)`
represented as a 400 pixel square.

```scala
val pca = PixelatedColoredArea(f, toColor, 400, 400, 0d, 1000d, 0d, 1000d)
// pca: PixelatedColoredArea[Double, Double, Double] = PixelatedColoredArea(
//   f = <function4>,
//   c = <function1>,
//   width = 400,
//   height = 400,
//   minX = 0.0,
//   maxX = 1000.0,
//   minY = 0.0,
//   maxY = 1000.0
// )
```

Create PNG

```scala
import axle.awt._
import cats.effect._

pca.png[IO]("roy_diagonal.png").unsafeRunSync()
// res0: Boolean = true
```

![ROY Diagonal](/tutorial/images/roy_diagonal.png)

## Second example

More compactly:

```scala
import spire.math.sqrt

val m = 200
// m: Int = 200

val greens = (0 until m).map(i => Color(0, ((i / m.toDouble) * 255).toInt, 0)).toArray
// greens: Array[Color] = Array(
//   Color(r = 0, g = 0, b = 0),
//   Color(r = 0, g = 1, b = 0),
//   Color(r = 0, g = 2, b = 0),
//   Color(r = 0, g = 3, b = 0),
//   Color(r = 0, g = 5, b = 0),
//   Color(r = 0, g = 6, b = 0),
//   Color(r = 0, g = 7, b = 0),
//   Color(r = 0, g = 8, b = 0),
//   Color(r = 0, g = 10, b = 0),
//   Color(r = 0, g = 11, b = 0),
//   Color(r = 0, g = 12, b = 0),
//   Color(r = 0, g = 14, b = 0),
//   Color(r = 0, g = 15, b = 0),
//   Color(r = 0, g = 16, b = 0),
//   Color(r = 0, g = 17, b = 0),
//   Color(r = 0, g = 19, b = 0),
//   Color(r = 0, g = 20, b = 0),
//   Color(r = 0, g = 21, b = 0),
//   Color(r = 0, g = 22, b = 0),
//   Color(r = 0, g = 24, b = 0),
//   Color(r = 0, g = 25, b = 0),
//   Color(r = 0, g = 26, b = 0),
//   Color(r = 0, g = 28, b = 0),
//   Color(r = 0, g = 29, b = 0),
//   Color(r = 0, g = 30, b = 0),
//   Color(r = 0, g = 31, b = 0),
//   Color(r = 0, g = 33, b = 0),
//   Color(r = 0, g = 34, b = 0),
//   Color(r = 0, g = 35, b = 0),
//   Color(r = 0, g = 36, b = 0),
//   Color(r = 0, g = 38, b = 0),
//   Color(r = 0, g = 39, b = 0),
//   Color(r = 0, g = 40, b = 0),
//   Color(r = 0, g = 42, b = 0),
//   Color(r = 0, g = 43, b = 0),
//   Color(r = 0, g = 44, b = 0),
//   Color(r = 0, g = 45, b = 0),
//   Color(r = 0, g = 47, b = 0),
//   Color(r = 0, g = 48, b = 0),
//   Color(r = 0, g = 49, b = 0),
//   Color(r = 0, g = 51, b = 0),
//   Color(r = 0, g = 52, b = 0),
//   Color(r = 0, g = 53, b = 0),
//   Color(r = 0, g = 54, b = 0),
//   Color(r = 0, g = 56, b = 0),
//   Color(r = 0, g = 57, b = 0),
//   Color(r = 0, g = 58, b = 0),
//   Color(r = 0, g = 59, b = 0),
// ...

val gpPca = PixelatedColoredArea(
    (x0: Double, x1: Double, y0: Double, y1: Double) => sqrt(x0*x0 + y0*y0),
    (v: Double) => greens(v.toInt % m),
    400, 400,
    0d, 1000d,
    0d, 1000d)
// gpPca: PixelatedColoredArea[Double, Double, Double] = PixelatedColoredArea(
//   f = <function4>,
//   c = <function1>,
//   width = 400,
//   height = 400,
//   minX = 0.0,
//   maxX = 1000.0,
//   minY = 0.0,
//   maxY = 1000.0
// )

import axle.awt._
import cats.effect._

gpPca.png[IO]("green_polar.png").unsafeRunSync()
// res1: Boolean = true
```

![Green Polar](/tutorial/images/green_polar.png)
