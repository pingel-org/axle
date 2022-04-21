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
// now: DateTime = 2022-04-21T15:13:17.520-04:00

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
//     "series 0 0.44 0.59 0.26",
//     TreeMap(
//       2022-04-21T15:13:17.520-04:00 -> 0.2507388980724336,
//       2022-04-21T15:15:17.520-04:00 -> 0.37787497128908326,
//       2022-04-21T15:17:17.520-04:00 -> 0.4805660752828017,
//       2022-04-21T15:19:17.520-04:00 -> 0.5521690580394933,
//       2022-04-21T15:21:17.520-04:00 -> 0.5880518776332635,
//       2022-04-21T15:23:17.520-04:00 -> 0.585893251927829,
//       2022-04-21T15:25:17.520-04:00 -> 0.5458328237758332,
//       2022-04-21T15:27:17.520-04:00 -> 0.4704621274350021,
//       2022-04-21T15:29:17.520-04:00 -> 0.36465694058935966,
//       2022-04-21T15:31:17.520-04:00 -> 0.23526186722854037,
//       2022-04-21T15:33:17.520-04:00 -> 0.09064755591683987,
//       2022-04-21T15:35:17.520-04:00 -> -0.05983080271972561,
//       2022-04-21T15:37:17.520-04:00 -> -0.2064386691506878,
//       2022-04-21T15:39:17.520-04:00 -> -0.33969188841728154,
//       2022-04-21T15:41:17.520-04:00 -> -0.4509702260733988,
//       2022-04-21T15:43:17.520-04:00 -> -0.5330750165659779,
//       2022-04-21T15:45:17.520-04:00 -> -0.5806948494308665,
//       2022-04-21T15:47:17.520-04:00 -> -0.5907491677716593,
//       2022-04-21T15:49:17.520-04:00 -> -0.56258755142012,
//       2022-04-21T15:51:17.520-04:00 -> -0.49803179302536954,
//       2022-04-21T15:53:17.520-04:00 -> -0.40125804514321073,
//       2022-04-21T15:55:17.520-04:00 -> -0.27852666230439455,
//       2022-04-21T15:57:17.520-04:00 -> -0.13777721474811394,
//       2022-04-21T15:59:17.520-04:00 -> 0.011885127363340275,
//       2022-04-21T16:01:17.520-04:00 -> 0.1607786131210213,
//       2022-04-21T16:03:17.520-04:00 -> 0.2992712294163992,
//       2022-04-21T16:05:17.520-04:00 -> 0.41840380191889076,
//       2022-04-21T16:07:17.520-04:00 -> 0.5104695696902476,
//       2022-04-21T16:09:17.520-04:00 -> 0.5695127403900062,
//       2022-04-21T16:11:17.520-04:00 -> 0.5917137742226196,
//       2022-04-21T16:13:17.520-04:00 -> 0.5756364723657521,
//       2022-04-21T16:15:17.520-04:00 -> 0.5223208855741234,
//       2022-04-21T16:17:17.520-04:00 -> 0.4352160326429656,
//       2022-04-21T16:19:17.520-04:00 -> 0.3199567812159504,
//       2022-04-21T16:21:17.520-04:00 -> 0.1839993246582529,
//       2022-04-21T16:23:17.520-04:00 -> 0.03613883622478143,
//       2022-04-21T16:25:17.520-04:00 -> -0.11405949622162814,
//       2022-04-21T16:27:17.520-04:00 -> -0.25687924822062375,
//       2022-04-21T16:29:17.520-04:00 -> -0.38308132031522757,
//       2022-04-21T16:31:17.520-04:00 -> -0.4845016211984714,
//       2022-04-21T16:33:17.520-04:00 -> -0.5545792079076233,
//       2022-04-21T16:35:17.520-04:00 -> -0.5887807173451519,
//       2022-04-21T16:37:17.520-04:00 -> -0.584893632384755,
//       2022-04-21T16:39:17.520-04:00 -> -0.5431694109938645,
//       2022-04-21T16:41:17.520-04:00 -> -0.46630721925980206,
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
//   yAxis = Some(value = 2022-04-21T15:13:17.520-04:00),
//   yAxisLabel = Some(value = "A·sin(ω·t + φ)")
// )
```

If instead we had supplied `(Color, String)` pairs, we would have needed something like preciding the `Plot` definition:

```scala
implicit val showCL: Show[(Color, String)] = new Show[(Color, String)] { def show(cl: (Color, String)): String = cl._2 }
// showCL: Show[(Color, String)] = repl.MdocSession$App$$anon$1@5e86c1ae
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
// timeConverter: quanta.UnitConverterGraph[Time, Double, DirectedSparseGraph[quanta.UnitOfMeasurement[Time], Double => Double]] with quanta.TimeConverter[Double] = axle.quanta.Time$$anon$1@74e95ecd
import timeConverter.millisecond

val dataUpdates: Observable[Seq[(String, TreeMap[DateTime, Double])]] =
  intervalScan(initialData, refreshFn, 500d *: millisecond)
// dataUpdates: Observable[Seq[(String, TreeMap[DateTime, Double])]] = monix.reactive.internal.operators.ScanObservable@36052610
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
