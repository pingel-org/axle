---
layout: page
title: ScatterPlot
permalink: /tutorial/scatterplot/
---

ScatterPlot

```scala
import axle.visualize._
```

```scala
val data = Map(
  (1, 1) -> 0,
  (2, 2) -> 0,
  (3, 3) -> 0,
  (2, 1) -> 1,
  (3, 2) -> 1,
  (0, 1) -> 2,
  (0, 2) -> 2,
  (1, 3) -> 2)
// data: Map[(Int, Int), Int] = HashMap(
//   (2, 2) -> 0,
//   (2, 1) -> 1,
//   (3, 2) -> 1,
//   (3, 3) -> 0,
//   (1, 3) -> 2,
//   (0, 2) -> 2,
//   (0, 1) -> 2,
//   (1, 1) -> 0
// )
```

Define the ScatterPlot

```scala
import axle.visualize.Color._
import cats.implicits._
```

```scala
val plot = ScatterPlot[String, Int, Int, Map[(Int, Int), Int]](
  () => data,
  colorOf = (x: Int, y: Int) => data((x, y)) match {
    case 0 => red
    case 1 => blue
    case 2 => green
  },
  labelOf = (x: Int, y: Int) => data.get((x, y)).map(s => (s.toString, false)))
// plot: ScatterPlot[String, Int, Int, Map[(Int, Int), Int]] = ScatterPlot(
//   dataFn = <function0>,
//   width = 600.0,
//   height = 600.0,
//   border = 50.0,
//   diameterOf = axle.visualize.ScatterPlot$$$Lambda$10768/0x000000080281eb78@7acf4c09,
//   colorOf = <function2>,
//   labelOf = <function2>,
//   fontName = "Courier New",
//   fontSize = 12.0,
//   bold = false,
//   titleFontName = "Palatino",
//   titleFontSize = 20.0,
//   title = None,
//   drawXTics = true,
//   drawXTicLines = true,
//   drawYTics = true,
//   drawYTicLines = true,
//   drawBorder = true,
//   xRange = None,
//   yAxis = None,
//   yRange = None,
//   xAxis = None,
//   xAxisLabel = None,
//   yAxisLabel = None
// )
```

Create the SVG

```scala
import axle.web._
import cats.effect._
```

```scala
plot.svg[IO]("scatter.svg").unsafeRunSync()
```

<object data="/tutorial/images/scatter.svg" type="image/svg+xml" alt="scatter plot"/>
