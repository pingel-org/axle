---
layout: page
title: Plots
permalink: /tutorial/plots/
---

Two-dimensional plots

Time-series plot example
------------------------

`axle.visualize.Plot`

Imports

```tut:silent
import org.joda.time.DateTime

import scala.collection.immutable.TreeMap
import scala.math.sin
import scala.util.Random.nextDouble

import cats.implicits._
import cats.Order.catsKernelOrderingForOrder

import axle._
import axle.visualize._
import axle.joda.dateTimeOrder

import axle.visualize.Color._
```

Generate the time-series to plot

```tut:book
val now = new DateTime()

val colors = Vector(red, blue, green, yellow, orange)

def randomTimeSeries(i: Int) = {

  val φ = nextDouble
  val A = nextDouble
  val ω = 0.1 / nextDouble
  val color = colors.random

  val data = new TreeMap[DateTime, Double]() ++ (0 to 100).map(t => (now.plusMinutes(2 * t) -> A * sin(ω*t + φ))).toMap

  val label = "%1.2f %1.2f %1.2f".format(φ, A, ω)

  (color, label) -> data
}

val waves = (0 until 20).map(randomTimeSeries)

import axle.joda.dateTimeZero

implicit val zeroDT = dateTimeZero(now)
```

Imports

```tut:silent
import cats.Show
import spire.implicits.DoubleAlgebra
import axle.visualize.Plot
import axle.algebra.Plottable.doublePlottable
import axle.joda.dateTimeOrder
import axle.joda.dateTimePlottable
import axle.joda.dateTimeTics
import axle.joda.dateTimeDurationLengthSpace
```

Define the visualization

```tut:book
implicit val showCL: Show[(Color, String)] = new Show[(Color, String)] { def show(cl: (Color, String)): String = cl._2 }

val plot = Plot(
  () => waves,
  colorOf = (cl: (Color, String)) => cl._1,
  title = Some("Random Waves"),
  xAxis = Some(0d),
  xAxisLabel = Some("time (t)"),
  yAxisLabel = Some("A sin(ωt + φ)"))
```

Create the SVG

```tut:book
import axle.web._

svg(plot, "waves.svg")
```

![waves](/tutorial/images/waves.svg)

Animation
---------

This example traces two "saw" functions vs time:

Imports

```tut:silent
import org.joda.time.DateTime
import edu.uci.ics.jung.graph.DirectedSparseGraph
import collection.immutable.TreeMap
import cats.implicits._
import monix.reactive._
import monix.execution.Scheduler.Implicits.global
import spire.implicits.DoubleAlgebra
import axle.joda._
import axle.jung._
import axle.quanta.Time
import axle.visualize._
import axle.reactive.intervalScan
import axle.reactive.CurrentValueSubscriber
import axle.awt.play
```

Define stream of data updates refreshing every 500 milliseconds

```tut:book
val initialData = List(
  ("saw 1", new TreeMap[DateTime, Double]()),
  ("saw 2", new TreeMap[DateTime, Double]())
)

// Note: uses zeroDT defined above

val saw1 = (t: Long) => (t % 10000) / 10000d
val saw2 = (t: Long) => (t % 100000) / 50000d

val fs = List(saw1, saw2)

val refreshFn = (previous: List[(String, TreeMap[DateTime, Double])]) => {
  val now = new DateTime()
  previous.zip(fs).map({ case (old, f) => (old._1, old._2 ++ Vector(now -> f(now.getMillis))) })
}

implicit val timeConverter = {
  import axle.algebra.modules.doubleRationalModule
  Time.converterGraphK2[Double, DirectedSparseGraph]
}
import timeConverter.millisecond

val dataUpdates: Observable[Seq[(String, TreeMap[DateTime, Double])]] =
  intervalScan(initialData, refreshFn, 500d *: millisecond)
```

Create `CurrentValueSubscriber`, which will be used by the `Plot` to get the latest values

```tut:book
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

```tut:silent
val (frame, paintCancellable) = play(plot, dataUpdates)
```

Tear down resources

```tut:silent
paintCancellable.cancel()
frame.setVisible(false)
cvCancellable.cancel()
```