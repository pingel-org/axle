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
// now: DateTime = 2022-04-22T01:17:26.425-04:00

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
//     "series 0 0.76 0.23 0.16",
//     TreeMap(
//       2022-04-22T01:17:26.425-04:00 -> 0.15863242848845288,
//       2022-04-22T01:19:26.425-04:00 -> 0.18320136036737594,
//       2022-04-22T01:21:26.425-04:00 -> 0.20310762704979765,
//       2022-04-22T01:23:26.425-04:00 -> 0.21784459334116327,
//       2022-04-22T01:25:26.425-04:00 -> 0.22703718811923349,
//       2022-04-22T01:27:26.425-04:00 -> 0.2304514502843129,
//       2022-04-22T01:29:26.425-04:00 -> 0.22800048331261522,
//       2022-04-22T01:31:26.425-04:00 -> 0.21974666686318764,
//       2022-04-22T01:33:26.425-04:00 -> 0.20590006915069783,
//       2022-04-22T01:35:26.425-04:00 -> 0.18681310049084715,
//       2022-04-22T01:37:26.425-04:00 -> 0.16297154409124134,
//       2022-04-22T01:39:26.425-04:00 -> 0.13498219236342102,
//       2022-04-22T01:41:26.425-04:00 -> 0.10355740342477283,
//       2022-04-22T01:43:26.425-04:00 -> 0.06949697084341105,
//       2022-04-22T01:45:26.425-04:00 -> 0.033667768059884695,
//       2022-04-22T01:47:26.425-04:00 -> -0.0030183144436497908,
//       2022-04-22T01:49:26.425-04:00 -> -0.03962757770479284,
//       2022-04-22T01:51:26.425-04:00 -> -0.07522827789077492,
//       2022-04-22T01:53:26.425-04:00 -> -0.10891434014824676,
//       2022-04-22T01:55:26.425-04:00 -> -0.1398284191508411,
//       2022-04-22T01:57:26.425-04:00 -> -0.1671837194295819,
//       2022-04-22T01:59:26.425-04:00 -> -0.19028402013603368,
//       2022-04-22T02:01:26.425-04:00 -> -0.2085413945871373,
//       2022-04-22T02:03:26.425-04:00 -> -0.22149117361088477,
//       2022-04-22T02:05:26.425-04:00 -> -0.22880377186011858,
//       2022-04-22T02:07:26.425-04:00 -> -0.23029307610246794,
//       2022-04-22T02:09:26.425-04:00 -> -0.22592118199571679,
//       2022-04-22T02:11:26.425-04:00 -> -0.2157993587927433,
//       2022-04-22T02:13:26.425-04:00 -> -0.20018521742328627,
//       2022-04-22T02:15:26.425-04:00 -> -0.17947615402780348,
//       2022-04-22T02:17:26.425-04:00 -> -0.15419923581230605,
//       2022-04-22T02:19:26.425-04:00 -> -0.12499778663968375,
//       2022-04-22T02:21:26.425-04:00 -> -0.09261501376817723,
//       2022-04-22T02:23:26.425-04:00 -> -0.0578750924535346,
//       2022-04-22T02:25:26.425-04:00 -> -0.02166218983140465,
//       2022-04-22T02:27:26.425-04:00 -> 0.015102038056020034,
//       2022-04-22T02:29:26.425-04:00 -> 0.05148190336502914,
//       2022-04-22T02:31:26.425-04:00 -> 0.08655150067936612,
//       2022-04-22T02:33:26.425-04:00 -> 0.1194182722662446,
//       2022-04-22T02:35:26.425-04:00 -> 0.14924572459913082,
//       2022-04-22T02:37:26.425-04:00 -> 0.1752747179881558,
//       2022-04-22T02:39:26.425-04:00 -> 0.19684278747491765,
//       2022-04-22T02:41:26.425-04:00 -> 0.21340100325480119,
//       2022-04-22T02:43:26.425-04:00 -> 0.22452794151153033,
//       2022-04-22T02:45:26.425-04:00 -> 0.22994041009167762,
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
//   yAxis = Some(value = 2022-04-22T01:17:26.425-04:00),
//   yAxisLabel = Some(value = "A·sin(ω·t + φ)")
// )
```

If instead we had supplied `(Color, String)` pairs, we would have needed something like preciding the `Plot` definition:

```scala
implicit val showCL: Show[(Color, String)] = new Show[(Color, String)] { def show(cl: (Color, String)): String = cl._2 }
// showCL: Show[(Color, String)] = repl.MdocSession$App$$anon$1@e5ba6d5
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
// timeConverter: quanta.UnitConverterGraph[Time, Double, DirectedSparseGraph[quanta.UnitOfMeasurement[Time], Double => Double]] with quanta.TimeConverter[Double] = axle.quanta.Time$$anon$1@1f4a9539
import timeConverter.millisecond

val dataUpdates: Observable[Seq[(String, TreeMap[DateTime, Double])]] =
  intervalScan(initialData, refreshFn, 500d *: millisecond)
// dataUpdates: Observable[Seq[(String, TreeMap[DateTime, Double])]] = monix.reactive.internal.operators.ScanObservable@2e629c06
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
