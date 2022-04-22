---
layout: page
title: Grouped Bar Charts
permalink: /tutorial/grouped_bar_charts/
---

Two-dimensional grouped bar charts

## Example

The following example dataset:

```scala
val sales = Map(
  ("apple", 2011) -> 43.0,
  ("apple", 2012) -> 83.8,
  ("banana", 2011) -> 11.3,
  ("banana", 2012) -> 77.9,
  ("coconut", 2011) -> 88.0,
  ("coconut", 2012) -> 10.1
)
// sales: Map[(String, Int), Double] = HashMap(
//   ("banana", 2011) -> 11.3,
//   ("apple", 2012) -> 83.8,
//   ("banana", 2012) -> 77.9,
//   ("coconut", 2012) -> 10.1,
//   ("apple", 2011) -> 43.0,
//   ("coconut", 2011) -> 88.0
// )
```

Shared imports

```scala
import cats.implicits._
import spire.algebra.Field
import axle.visualize.BarChartGrouped
import axle.visualize.Color._

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
```

The data can be grouped in two ways to produce bar charts:

```scala
val chart = BarChartGrouped[String, Int, Double, Map[(String, Int), Double], String](
  () => sales,
  title = Some("fruit sales"),
  colorOf = (label: String, year: Int) => year match {
    case 2011 => red
    case 2012 => blue
  }
)
// chart: BarChartGrouped[String, Int, Double, Map[(String, Int), Double], String] = BarChartGrouped(
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
//   colorOf = <function2>,
//   hoverOf = axle.visualize.BarChartGrouped$$$Lambda$10433/0x000000080270e000@2eef234d,
//   linkOf = axle.visualize.BarChartGrouped$$$Lambda$10434/0x000000080270e5a8@2b2049d8
// )
```

Create the SVG

```scala
import axle.web._
import cats.effect._

chart.svg[IO]("barchart1.svg").unsafeRunSync()
```

![barchart1](/tutorial/images/barchart1.svg)

Or alternatively

```scala
val chart2 = BarChartGrouped[Int, String, Double, Map[(Int, String), Double], String](
  () => sales map { case (k, v) => (k._2, k._1) -> v},
  colorOf = (year: Int, label: String) => label match {
    case "apple" => red
    case "banana" => yellow
    case "coconut" => brown
  },
  title = Some("fruit sales")
)
// chart2: BarChartGrouped[Int, String, Double, Map[(Int, String), Double], String] = BarChartGrouped(
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
//   colorOf = <function2>,
//   hoverOf = axle.visualize.BarChartGrouped$$$Lambda$10433/0x000000080270e000@2eef234d,
//   linkOf = axle.visualize.BarChartGrouped$$$Lambda$10434/0x000000080270e5a8@2b2049d8
// )
```

Create the second SVG

```scala
import axle.web._
import cats.effect._

chart.svg[IO]("barchart2.svg").unsafeRunSync()
```

![barchart2](/tutorial/images/barchart2.svg)

## Animation

This example keeps the "bar" value steady at 1.0 while assigning a new random Double (between 0 and 1) to "foo" every second.

Imports

```scala
import scala.util.Random.nextDouble
import axle.jung._
import axle.quanta.Time
import edu.uci.ics.jung.graph.DirectedSparseGraph
import monix.reactive._
import axle.reactive.intervalScan
```

Define stream of data updates

```scala
val groups = Vector("foo", "bar")
// groups: Vector[String] = Vector("foo", "bar")
val initial = Map("foo" -> 1d, "bar" -> 1d)
// initial: Map[String, Double] = Map("foo" -> 1.0, "bar" -> 1.0)

val tick = (previous: Map[String, Double]) => previous + ("foo" -> nextDouble())
// tick: Map[String, Double] => Map[String, Double] = <function1>

implicit val timeConverter = {
  import axle.algebra.modules.doubleRationalModule
  Time.converterGraphK2[Double, DirectedSparseGraph]
}
// timeConverter: axle.quanta.UnitConverterGraph[Time, Double, DirectedSparseGraph[axle.quanta.UnitOfMeasurement[Time], Double => Double]] with axle.quanta.TimeConverter[Double] = axle.quanta.Time$$anon$1@c6e8e58
import timeConverter.second

val dataUpdates: Observable[Map[String, Double]] = intervalScan(initial, tick, 1d *: second)
// dataUpdates: Observable[Map[String, Double]] = monix.reactive.internal.operators.ScanObservable@45c951
```

Create `CurrentValueSubscriber`, which will be used by the `BarChart` to get the latest value

```scala
import axle.reactive.CurrentValueSubscriber
import monix.execution.Scheduler.Implicits.global

val cvSub = new CurrentValueSubscriber[Map[String, Double]]()
val cvCancellable = dataUpdates.subscribe(cvSub)

import axle.visualize.BarChart

val chart = BarChart[String, Double, Map[String, Double], String](
  () => cvSub.currentValue.getOrElse(initial),
  title = Some("random")
)
```

Animate

```scala
import axle.awt.play

val (frame, paintCancellable) = play(chart, dataUpdates)
```

Tear down the resources

```scala
paintCancellable.cancel()
cvCancellable.cancel()
frame.dispose()
```
