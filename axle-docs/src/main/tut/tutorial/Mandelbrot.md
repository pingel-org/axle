---
layout: page
title: Mandelbrot Set
permalink: /tutorial/mandelbrot/
---

See the wikipedia page on the [Mandelbrot Set](https://en.wikipedia.org/wiki/Mandelbrot_set)

Define a function to compute the Mandelbrot velocity at point on the plane `(x, y)`

```tut:book
import axle._
import spire.implicits.DoubleAlgebra

val f = (x: Double, y: Double) => inMandelbrotSetAt(4d, x, y, 1000).getOrElse(-1)
```

Define a "velocity to color" function

```tut:book
import axle.visualize._

val colors = (0 to 255).map(g => Color(0, g, 255)).toArray

val v2c = (v: Int) => if( v == -1 ) Color.black else colors((v*5) % 256)
```

Define a "pixelated colored area" to show a range of the Mandelbrot Set.

```tut:book
// val pca = PixelatedColoredArea(f, v2c, 1600, 1600, 0d, 1d, 0d, 1d)
// val pca = PixelatedColoredArea(f, v2c, 1600, 1600, 0d, 0.5, 0.5, 1d)
// val pca = PixelatedColoredArea(f, v2c, 1600, 1600, 0.25d, 0.5, 0.5, 0.75d)
// val pca = PixelatedColoredArea(f, v2c, 3000, 3000, 0.20d, 0.45, 0.45, 0.70d)
val pca = PixelatedColoredArea(f, v2c, 400, 400, 0.25d, 0.45, 0.50, 0.70d)
```

Create PNG

```tut:book
import axle.awt._
png(pca, "mandelbrot.png")
```

![Mandelbrot Set](/tutorial/images/mandelbrot.png)
