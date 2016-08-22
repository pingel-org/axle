---
layout: page
title: Logistic Map
permalink: /tutorial/logistic_map/
---

See the wikipedia page on [Logistic Map](https://en.wikipedia.org/wiki/Logistic_map) function

Create data for a range of the logistic map function

```tut:book
import axle._
import spire.implicits.DoubleAlgebra

def doubleClose(z: Double)(y: Double) = math.abs(z - y) < 1e-6

val functions = (2.9 to 4.0 by 0.001).map(λ => λ -> logisticMap(λ))

val scatter = for {
  (λ, f) <- functions
  p <- orbit(f, applyK(f, 0.3, 100000), doubleClose)
} yield (λ, p)
```

Define a scatterplot visualization

```tut:book
import axle.visualize._

val sp = ScatterPlot(scatter.toSet, pointDiameter=1)
```

Create the SVG

```tut:book
import axle.web._
svg(sp, "logMap.svg")
```

![Logistic Map](/tutorial/images/logMap.svg)
