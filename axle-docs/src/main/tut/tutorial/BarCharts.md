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
import axle.visualize.BarChart
import spire.implicits.DoubleAlgebra
import cats.implicits._

val chart = BarChart[String, Double, Map[String, Double], String](
  sales,
  title = Some("fruit sales"),
  hoverOf = (c: String) => Some(c)
)
```

Create the SVG

```tut:book
import axle.web._
svg(chart, "fruitsales.svg")
```

![fruit sales](/tutorial/images/fruitsales.svg)
