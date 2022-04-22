# Grouped Bar Charts

Two-dimensional grouped bar charts

## Example

The following example dataset:

```scala mdoc
val sales = Map(
  ("apple", 2011) -> 43.0,
  ("apple", 2012) -> 83.8,
  ("banana", 2011) -> 11.3,
  ("banana", 2012) -> 77.9,
  ("coconut", 2011) -> 88.0,
  ("coconut", 2012) -> 10.1
)
```

Shared imports

```scala mdoc:silent
import cats.implicits._
import spire.algebra.Field
import axle.visualize.BarChartGrouped
import axle.visualize.Color._

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
```

The data can be grouped in two ways to produce bar charts:

```scala mdoc
val chart = BarChartGrouped[String, Int, Double, Map[(String, Int), Double], String](
  () => sales,
  title = Some("fruit sales"),
  colorOf = (label: String, year: Int) => year match {
    case 2011 => red
    case 2012 => blue
  }
)
```

Create the SVG

```scala mdoc
import axle.web._
import cats.effect._

chart.svg[IO]("barchart1.svg").unsafeRunSync()
```

![barchart1](/tutorial/images/barchart1.svg)

Or alternatively

```scala mdoc
val chart2 = BarChartGrouped[Int, String, Double, Map[(Int, String), Double], String](
  () => sales map { case (k, v) => (k._2, k._1) -> v},
  colorOf = (year: Int, label: String) => label match {
    case "apple" => red
    case "banana" => yellow
    case "coconut" => brown
  },
  title = Some("fruit sales")
)
```

Create the second SVG

```scala mdoc
import axle.web._
import cats.effect._

chart.svg[IO]("barchart2.svg").unsafeRunSync()
```

![barchart2](/tutorial/images/barchart2.svg)

## Animation

This example keeps the "bar" value steady at 1.0 while assigning a new random Double (between 0 and 1) to "foo" every second.

Imports

```scala mdoc:silent
import scala.util.Random.nextDouble
import axle.jung._
import axle.quanta.Time
import edu.uci.ics.jung.graph.DirectedSparseGraph
import monix.reactive._
import axle.reactive.intervalScan
```

Define stream of data updates

```scala mdoc
val groups = Vector("foo", "bar")
val initial = Map("foo" -> 1d, "bar" -> 1d)

val tick = (previous: Map[String, Double]) => previous + ("foo" -> nextDouble())

implicit val timeConverter = {
  import axle.algebra.modules.doubleRationalModule
  Time.converterGraphK2[Double, DirectedSparseGraph]
}
import timeConverter.second

val dataUpdates: Observable[Map[String, Double]] = intervalScan(initial, tick, 1d *: second)
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
