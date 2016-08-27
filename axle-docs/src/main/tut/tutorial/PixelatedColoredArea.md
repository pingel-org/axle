---
layout: page
title: Pixelated Colored Area
permalink: /tutorial/pixelated_colored_area/
---

A few imports:

```tut:silent
import axle._
import spire.implicits.DoubleAlgebra
import axle.visualize._
```

Define a function to compute an `Double` for each point on the plane `(x, y): (Double, Double)`

```tut:book
val n = 100

def f(x: Double, y: Double) = (x + y) % n
```

Define a `toColor` function.
Here we first prepare an array of colors to avoid creating the objects during rendering.

```tut:book
// red to orange to yellow
val roy = (0 until n).map(i => Color(255, ((i / n.toDouble) * 255).toInt, 0)).toArray

def toColor(v: Double) = roy(v.toInt)
```

Define a `PixelatedColoredArea` to show a range of `toColor ∘ f` (`(0,0)` to `(1000,1000)`)
represented as a 400 pixel square.

```tut:book
val pca = PixelatedColoredArea(f, toColor, 400, 400, 0d, 1000d, 0d, 1000d)
```

Create PNG

```tut:book
import axle.awt._
png(pca, "roy_diagonal.png")
```

![ROY Diagonal](/tutorial/images/roy_diagonal.png)

Another (more compact) example

```tut:book
import spire.math.sqrt

val m = 200

val greens = (0 until m).map(i => Color(0, ((i / m.toDouble) * 255).toInt, 0)).toArray

png(
  PixelatedColoredArea(
    (x: Double, y: Double) => sqrt(x*x + y*y),
    (v: Double) => greens(v.toInt % m),
    400, 400,
    0d, 1000d,
    0d, 1000d),
  "green_polar.png"
)
```

![Green Polar](/tutorial/images/green_polar.png)
