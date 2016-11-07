---
layout: page
title: Bar Charts
permalink: /tutorial/bar_charts/
---

Two-dimensional bar charts.

Example
-------

The dataset:

```tut:book
val sales = Map(
  "apple" -> 83.8,
  "banana" -> 77.9,
  "coconut" -> 10.1
)
```

Define a bar chart visualization

```tut:book
import spire.implicits.DoubleAlgebra
import spire.implicits.StringOrder
import axle.visualize.BarChart
import axle.showString

val chart = BarChart[String, Double, Map[String, Double]](
  sales,
  title = Some("fruit sales")
)
```

Create the SVG

```tut:book
import axle.web._
svg(chart, "fruitsales.svg")
```

![fruit sales](/tutorial/images/fruitsales.svg)
