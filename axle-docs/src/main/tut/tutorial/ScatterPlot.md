---
layout: page
title: ScatterPlot
permalink: /tutorial/scatterplot/
---

ScatterPlot


```tut:silent
import axle.visualize._
```

```tut:book
val data = Map(
  (1, 1) -> 0,
  (2, 2) -> 0,
  (3, 3) -> 0,
  (2, 1) -> 1,
  (3, 2) -> 1,
  (0, 1) -> 2,
  (0, 2) -> 2,
  (1, 3) -> 2)
```

Define the ScatterPlot

```tut:silent
import axle.visualize.Color._
import cats.implicits._
```

```tut:book
val plot = ScatterPlot[String, Int, Int, Map[(Int, Int), Int]](
  data,
  colorOf = (x: Int, y: Int) => data((x, y)) match {
    case 0 => red
    case 1 => blue
    case 2 => green
  },
  labelOf = (x: Int, y: Int) => data.get((x, y)).map(s => (s.toString, false)))
```

Create the SVG

```tut:silent
import axle.web._
```

```tut:book
svg(plot, "scatter.svg")
```

<object data="/tutorial/images/scatter.svg" type="image/svg+xml" alt="scatter plot"/>
