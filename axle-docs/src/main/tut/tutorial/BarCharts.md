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

```tut:silent
import axle.visualize.BarChart
import axle.visualize.Color.lightGray
import spire.implicits.DoubleAlgebra
import cats.implicits._
```

```tut:book
val chart = BarChart[String, Double, Map[String, Double], String](
  () => sales,
  title = Some("fruit sales"),
  hoverOf = (c: String) => Some(c),
  linkOf = (c: String) => Some((new java.net.URL(s"http://wikipedia.org/wiki/$c"), lightGray))
)
```

Create the SVG

```tut:silent
import axle.web._
```

```tut:book
svg(chart, "fruitsales.svg")
```

<object data="/tutorial/images/fruitsales.svg" type="image/svg+xml" alt="fruit sales"/>
