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
// now: DateTime = 2022-04-22T00:28:40.852-04:00

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
//     "series 0 0.67 0.58 0.25",
//     TreeMap(
//       2022-04-22T00:28:40.852-04:00 -> 0.3579385419529296,
//       2022-04-22T00:30:40.852-04:00 -> 0.4597979105595591,
//       2022-04-22T00:32:40.852-04:00 -> 0.5320649313336848,
//       2022-04-22T00:34:40.852-04:00 -> 0.5700885374338671,
//       2022-04-22T00:36:40.852-04:00 -> 0.5714215498892549,
//       2022-04-22T00:38:40.852-04:00 -> 0.5359781767305088,
//       2022-04-22T00:40:40.852-04:00 -> 0.4660395345151063,
//       2022-04-22T00:42:40.852-04:00 -> 0.3661068368846069,
//       2022-04-22T00:44:40.852-04:00 -> 0.24261169885988634,
//       2022-04-22T00:46:40.852-04:00 -> 0.10350220153510008,
//       2022-04-22T00:48:40.852-04:00 -> -0.042268642174512504,
//       2022-04-22T00:50:40.852-04:00 -> -0.18531909865984736,
//       2022-04-22T00:52:40.852-04:00 -> -0.31644251697992754,
//       2022-04-22T00:54:40.852-04:00 -> -0.42719986399692783,
//       2022-04-22T00:56:40.852-04:00 -> -0.510462856039564,
//       2022-04-22T00:58:40.852-04:00 -> -0.5608727314312071,
//       2022-04-22T01:00:40.852-04:00 -> -0.5751851375279589,
//       2022-04-22T01:02:40.852-04:00 -> -0.5524789355212074,
//       2022-04-22T01:04:40.852-04:00 -> -0.4942154844391427,
//       2022-04-22T01:06:40.852-04:00 -> -0.40414458886057175,
//       2022-04-22T01:08:40.852-04:00 -> -0.2880631634959217,
//       2022-04-22T01:10:40.852-04:00 -> -0.15344214685403254,
//       2022-04-22T01:12:40.852-04:00 -> -0.008945675631794556,
//       2022-04-22T01:14:40.852-04:00 -> 0.13612653449402196,
//       2022-04-22T01:16:40.852-04:00 -> 0.2724377135984532,
//       2022-04-22T01:18:40.852-04:00 -> 0.39121494703165194,
//       2022-04-22T01:20:40.852-04:00 -> 0.48481379551798026,
//       2022-04-22T01:22:40.852-04:00 -> 0.5472102871830149,
//       2022-04-22T01:24:40.852-04:00 -> 0.5743886171645018,
//       2022-04-22T01:26:40.852-04:00 -> 0.5645996026674602,
//       2022-04-22T01:28:40.852-04:00 -> 0.5184732594342004,
//       2022-04-22T01:30:40.852-04:00 -> 0.4389782542677949,
//       2022-04-22T01:32:40.852-04:00 -> 0.3312308432229259,
//       2022-04-22T01:34:40.852-04:00 -> 0.2021655921000586,
//       2022-04-22T01:36:40.852-04:00 -> 0.06008907149597843,
//       2022-04-22T01:38:40.852-04:00 -> -0.0858547496412354,
//       2022-04-22T01:40:40.852-04:00 -> -0.22627300496767022,
//       2022-04-22T01:42:40.852-04:00 -> -0.3521284506024289,
//       2022-04-22T01:44:40.852-04:00 -> -0.45532109731479453,
//       2022-04-22T01:46:40.852-04:00 -> -0.5292095215029222,
//       2022-04-22T01:48:40.852-04:00 -> -0.5690383036680385,
//       2022-04-22T01:50:40.852-04:00 -> -0.5722440846723408,
//       2022-04-22T01:52:40.852-04:00 -> -0.538620542163388,
//       2022-04-22T01:54:40.852-04:00 -> -0.47033166937045534,
//       2022-04-22T01:56:40.852-04:00 -> -0.3717725016566208,
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
//   yAxis = Some(value = 2022-04-22T00:28:40.852-04:00),
//   yAxisLabel = Some(value = "A·sin(ω·t + φ)")
// )
```

If instead we had supplied `(Color, String)` pairs, we would have needed something like preciding the `Plot` definition:

```scala
implicit val showCL: Show[(Color, String)] = new Show[(Color, String)] { def show(cl: (Color, String)): String = cl._2 }
// showCL: Show[(Color, String)] = repl.MdocSession$App$$anon$1@37f287f7
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
// timeConverter: quanta.UnitConverterGraph[Time, Double, DirectedSparseGraph[quanta.UnitOfMeasurement[Time], Double => Double]] with quanta.TimeConverter[Double] = axle.quanta.Time$$anon$1@230a7034
import timeConverter.millisecond

val dataUpdates: Observable[Seq[(String, TreeMap[DateTime, Double])]] =
  intervalScan(initialData, refreshFn, 500d *: millisecond)
// dataUpdates: Observable[Seq[(String, TreeMap[DateTime, Double])]] = monix.reactive.internal.operators.ScanObservable@27edb1db
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
