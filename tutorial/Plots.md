---
layout: page
title: Plots
permalink: /tutorial/plots/
---

Two-dimensional plots

## Time-series plot example

`axle.visualize.Plot`

Imports

```scala
import org.joda.time.DateTime

import scala.collection.immutable.TreeMap
import scala.math.sin

import spire.random.Generator
import spire.random.Generator.rng

import cats.implicits._

import axle._
import axle.visualize._
import axle.joda.dateTimeOrder

import axle.visualize.Color._
```

Generate the time-series to plot

```scala
val now = new DateTime()
// now: DateTime = 2022-04-22T01:11:29.628-04:00

val colors = Vector(red, blue, green, yellow, orange)
// colors: Vector[Color] = Vector(
//   Color(r = 255, g = 0, b = 0),
//   Color(r = 0, g = 0, b = 255),
//   Color(r = 0, g = 255, b = 0),
//   Color(r = 255, g = 255, b = 0),
//   Color(r = 255, g = 200, b = 0)
// )

def randomTimeSeries(i: Int, gen: Generator) = {
  val φ = gen.nextDouble()
  val A = gen.nextDouble()
  val ω = 0.1 / gen.nextDouble()
  ("series %d %1.2f %1.2f %1.2f".format(i, φ, A, ω),
    new TreeMap[DateTime, Double]() ++
      (0 to 100).map(t => (now.plusMinutes(2 * t) -> A * sin(ω * t + φ))).toMap)
}

val waves = (0 until 20).map(i => randomTimeSeries(i, rng)).toList
// waves: List[(String, TreeMap[DateTime, Double])] = List(
//   (
//     "series 0 0.42 0.36 0.19",
//     TreeMap(
//       2022-04-22T01:11:29.628-04:00 -> 0.14527672488418183,
//       2022-04-22T01:13:29.628-04:00 -> 0.20496014678021443,
//       2022-04-22T01:15:29.628-04:00 -> 0.2571458878669389,
//       2022-04-22T01:17:29.628-04:00 -> 0.2999249329742743,
//       2022-04-22T01:19:29.628-04:00 -> 0.3317323748235107,
//       2022-04-22T01:21:29.628-04:00 -> 0.35140466015762434,
//       2022-04-22T01:23:29.628-04:00 -> 0.3582221538741726,
//       2022-04-22T01:25:29.628-04:00 -> 0.35193546411523036,
//       2022-04-22T01:27:29.628-04:00 -> 0.33277456531192556,
//       2022-04-22T01:29:29.628-04:00 -> 0.3014403854519839,
//       2022-04-22T01:31:29.628-04:00 -> 0.2590791653178306,
//       2022-04-22T01:33:29.628-04:00 -> 0.20724052766417256,
//       2022-04-22T01:35:29.628-04:00 -> 0.14782079021336814,
//       2022-04-22T01:37:29.628-04:00 -> 0.08299359614521466,
//       2022-04-22T01:39:29.628-04:00 -> 0.015130399698604937,
//       2022-04-22T01:41:29.628-04:00 -> -0.05328628438567227,
//       2022-04-22T01:43:29.628-04:00 -> -0.11975369414460377,
//       2022-04-22T01:45:29.628-04:00 -> -0.1818403743395846,
//       2022-04-22T01:47:29.628-04:00 -> -0.23727512192004926,
//       2022-04-22T01:49:29.628-04:00 -> -0.2840300692754787,
//       2022-04-22T01:51:29.628-04:00 -> -0.3203948659936875,
//       2022-04-22T01:53:29.628-04:00 -> -0.34503924547030623,
//       2022-04-22T01:55:29.628-04:00 -> -0.3570616876100288,
//       2022-04-22T01:57:29.628-04:00 -> -0.35602239748136244,
//       2022-04-22T01:59:29.628-04:00 -> -0.3419593935272844,
//       2022-04-22T02:01:29.628-04:00 -> -0.3153871168063327,
//       2022-04-22T02:03:29.628-04:00 -> -0.27727761213970836,
//       2022-04-22T02:05:29.628-04:00 -> -0.22902496957994026,
//       2022-04-22T02:07:29.628-04:00 -> -0.17239432697359128,
//       2022-04-22T02:09:29.628-04:00 -> -0.1094572991636372,
//       2022-04-22T02:11:29.628-04:00 -> -0.04251619590647951,
//       2022-04-22T02:13:29.628-04:00 -> 0.025980199299579298,
//       2022-04-22T02:15:29.628-04:00 -> 0.09352620856588074,
//       2022-04-22T02:17:29.628-04:00 -> 0.1576509202280011,
//       2022-04-22T02:19:29.628-04:00 -> 0.21600857767137763,
//       2022-04-22T02:21:29.628-04:00 -> 0.26646438983948995,
//       2022-04-22T02:23:29.628-04:00 -> 0.30717262437629866,
//       2022-04-22T02:25:29.628-04:00 -> 0.3366441266648588,
//       2022-04-22T02:27:29.628-04:00 -> 0.35380079483706756,
//       2022-04-22T02:29:29.628-04:00 -> 0.3580150179952589,
//       2022-04-22T02:31:29.628-04:00 -> 0.34913263494958374,
//       2022-04-22T02:33:29.628-04:00 -> 0.32747857361382754,
//       2022-04-22T02:35:29.628-04:00 -> 0.29384496476400296,
//       2022-04-22T02:37:29.628-04:00 -> 0.24946216497226606,
//       2022-04-22T02:39:29.628-04:00 -> 0.19595374873097374,
// ...
```

Imports for visualization

```scala
import cats.Show

import spire.algebra._

import axle.visualize.Plot
import axle.algebra.Plottable.doublePlottable
import axle.joda.dateTimeOrder
import axle.joda.dateTimePlottable
import axle.joda.dateTimeTics
import axle.joda.dateTimeDurationLengthSpace

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
```

Define the visualization

```scala
val plot = Plot[String, DateTime, Double, TreeMap[DateTime, Double]](
  () => waves,
  connect = true,
  colorOf = s => colors(s.hash.abs % colors.length),
  title = Some("Random Waves"),
  xAxisLabel = Some("time (t)"),
  yAxis = Some(now),
  yAxisLabel = Some("A·sin(ω·t + φ)")).zeroXAxis
// plot: Plot[String, DateTime, Double, TreeMap[DateTime, Double]] = Plot(
//   dataFn = <function0>,
//   connect = true,
//   drawKey = true,
//   width = 700,
//   height = 600,
//   border = 50,
//   pointDiameter = 4,
//   keyLeftPadding = 20,
//   keyTopPadding = 50,
//   keyWidth = 80,
//   fontName = "Courier New",
//   fontSize = 12,
//   bold = false,
//   titleFontName = "Palatino",
//   titleFontSize = 20,
//   colorOf = <function1>,
//   title = Some(value = "Random Waves"),
//   keyTitle = None,
//   xAxis = Some(value = 0.0),
//   xAxisLabel = Some(value = "time (t)"),
//   yAxis = Some(value = 2022-04-22T01:11:29.628-04:00),
//   yAxisLabel = Some(value = "A·sin(ω·t + φ)")
// )
```

If instead we had supplied `(Color, String)` pairs, we would have needed something like preciding the `Plot` definition:

```scala
implicit val showCL: Show[(Color, String)] = new Show[(Color, String)] { def show(cl: (Color, String)): String = cl._2 }
// showCL: Show[(Color, String)] = repl.MdocSession$App$$anon$1@14531801
```

Create the SVG

```scala
import axle.web._
import cats.effect._

plot.svg[IO]("random_waves.svg").unsafeRunSync()
```

![waves](/tutorial/images/random_waves.svg)

## Animation

This example traces two "saw" functions vs time:

Imports

```scala
import org.joda.time.DateTime
import edu.uci.ics.jung.graph.DirectedSparseGraph
import collection.immutable.TreeMap

import cats.implicits._

import monix.reactive._

import spire.algebra.Field

import axle.jung._
import axle.quanta.Time
import axle.visualize._
import axle.reactive.intervalScan
```

Define stream of data updates refreshing every 500 milliseconds

```scala
val initialData = List(
  ("saw 1", new TreeMap[DateTime, Double]()),
  ("saw 2", new TreeMap[DateTime, Double]())
)
// initialData: List[(String, TreeMap[DateTime, Double])] = List(
//   ("saw 1", TreeMap()),
//   ("saw 2", TreeMap())
// )

// Note: uses zeroDT defined above

val saw1 = (t: Long) => (t % 10000) / 10000d
// saw1: Long => Double = <function1>
val saw2 = (t: Long) => (t % 100000) / 50000d
// saw2: Long => Double = <function1>

val fs = List(saw1, saw2)
// fs: List[Long => Double] = List(<function1>, <function1>)

val refreshFn = (previous: List[(String, TreeMap[DateTime, Double])]) => {
  val now = new DateTime()
  previous.zip(fs).map({ case (old, f) => (old._1, old._2 ++ Vector(now -> f(now.getMillis))) })
}
// refreshFn: List[(String, TreeMap[DateTime, Double])] => List[(String, TreeMap[DateTime, Double])] = <function1>

implicit val timeConverter = {
  import axle.algebra.modules.doubleRationalModule
  Time.converterGraphK2[Double, DirectedSparseGraph]
}
// timeConverter: quanta.UnitConverterGraph[Time, Double, DirectedSparseGraph[quanta.UnitOfMeasurement[Time], Double => Double]] with quanta.TimeConverter[Double] = axle.quanta.Time$$anon$1@2f0cf152
import timeConverter.millisecond

val dataUpdates: Observable[Seq[(String, TreeMap[DateTime, Double])]] =
  intervalScan(initialData, refreshFn, 500d *: millisecond)
// dataUpdates: Observable[Seq[(String, TreeMap[DateTime, Double])]] = monix.reactive.internal.operators.ScanObservable@488207f6
```

Create `CurrentValueSubscriber`, which will be used by the `Plot` to get the latest values

```scala
import monix.execution.Scheduler.Implicits.global
import axle.reactive.CurrentValueSubscriber

val cvSub = new CurrentValueSubscriber[Seq[(String, TreeMap[DateTime, Double])]]()
val cvCancellable = dataUpdates.subscribe(cvSub)

// [DateTime, Double, TreeMap[DateTime, Double]]

val plot = Plot(
  () => cvSub.currentValue.getOrElse(initialData),
  connect = true,
  colorOf = (label: String) => Color.black,
  title = Some("Saws"),
  xAxis = Some(0d),
  xAxisLabel = Some("time (t)"),
  yAxisLabel = Some("y")
)
```

Animate

```scala
import axle.awt._

val (frame, paintCancellable) = play(plot, dataUpdates)
```

Tear down resources

```scala
paintCancellable.cancel()
cvCancellable.cancel()
frame.dispose()
```
