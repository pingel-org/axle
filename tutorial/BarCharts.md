---
layout: page
title: Bar Charts
permalink: /tutorial/bar_charts/
---

Two-dimensional bar charts.

## Example

The dataset:

```scala
val sales = Map(
  "apple" -> 83.8,
  "banana" -> 77.9,
  "coconut" -> 10.1
)
// sales: Map[String, Double] = Map(
//   "apple" -> 83.8,
//   "banana" -> 77.9,
//   "coconut" -> 10.1
// )
```

Define a bar chart visualization

```scala
import spire.algebra.Field
import cats.implicits._
import axle.visualize.BarChart
import axle.visualize.Color.lightGray
```

```scala
implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
// fieldDouble: Field[Double] = spire.std.DoubleAlgebra@454746e2

val chart = BarChart[String, Double, Map[String, Double], String](
  () => sales,
  title = Some("fruit sales"),
  hoverOf = (c: String) => Some(c),
  linkOf = (c: String) => Some((new java.net.URL(s"http://wikipedia.org/wiki/$c"), lightGray))
)
// chart: BarChart[String, Double, Map[String, Double], String] = BarChart(
//   dataFn = <function0>,
//   drawKey = true,
//   width = 700,
//   height = 600,
//   border = 50,
//   barWidthPercent = 0.8,
//   keyLeftPadding = 20,
//   keyTopPadding = 50,
//   keyWidth = 80,
//   title = Some(value = "fruit sales"),
//   keyTitle = None,
//   normalFontName = "Courier New",
//   normalFontSize = 12,
//   titleFontName = "Palatino",
//   titleFontSize = 20,
//   xAxis = None,
//   xAxisLabel = None,
//   yAxisLabel = None,
//   labelAngle = Some(
//     value = UnittedQuantity(
//       magnitude = 36.0,
//       unit = UnitOfMeasurement(
//         name = "degree",
//         symbol = "°",
//         wikipediaUrl = Some(
//           value = "http://en.wikipedia.org/wiki/Degree_(angle)"
//         )
//       )
//     )
//   ),
//   colorOf = axle.visualize.BarChart$$$Lambda$6496/0x00000008020a2530@5bbb5523,
//   hoverOf = <function1>,
//   linkOf = <function1>
// )
```

Create the SVG

```scala
import axle.web._
import cats.effect._
```

```scala
chart.svg[IO]("fruitsales.svg").unsafeRunSync()
```

<object data="/tutorial/images/fruitsales.svg" type="image/svg+xml" alt="fruit sales"/>
