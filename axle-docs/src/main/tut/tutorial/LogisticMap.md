---
layout: page
title: Logistic Map
permalink: /tutorial/logistic_map/
---

See the wikipedia page on [Logistic Map](https://en.wikipedia.org/wiki/Logistic_map) function

Create data for a range of the logistic map function

```tut:book
import math.abs
import spire.implicits.DoubleAlgebra
import axle.{logisticMap, orbit, applyK}

def doubleClose(z: Double)(y: Double) = abs(z - y) < 1e-6

val functions = (2.9 to 4.0 by 0.001).map(λ => λ -> logisticMap(λ))

val scatter = for {
  (λ, f) <- functions
  p <- orbit(f, applyK(f, 0.3, 100000), doubleClose)
} yield (λ, p)
```

Define a scatterplot visualization

```tut:book
import cats.implicits._
import axle.visualize._

val sp = ScatterPlot[String, Double, Double, Set[(Double, Double)]](
  scatter.toSet,
  diameterOf = (d: Set[(Double, Double)], x: Double, y: Double) => 1d
)
```

Create the SVG

```tut:book
import axle.web._
svg(sp, "logMap.svg")
```

![Logistic Map](/tutorial/images/logMap.svg)
