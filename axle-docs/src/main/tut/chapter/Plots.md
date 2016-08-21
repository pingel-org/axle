Plots
=====

Two-dimensional plots

Time-series plot example
------------------------

`axle.visualize.Plot`

```tut:book
import axle._
import axle.visualize._
import org.joda.time.DateTime
import spire.compat.ordering
import scala.collection.immutable.TreeMap
import scala.math.sin
import scala.util.Random.nextDouble
import axle.joda.dateTimeOrder

val now = new DateTime()

def randomTimeSeries(i: Int) = {
  val φ = nextDouble
  val A = nextDouble
  val ω = 0.1 / nextDouble
  ("%1.2f %1.2f %1.2f".format(φ, A, ω),
    new TreeMap[DateTime, Double]() ++
    (0 to 100).map(t => (now.plusMinutes(2 * t) -> A * sin(ω*t + φ))).toMap)
}

val waves = (0 until 20).map(randomTimeSeries)

import axle.joda.dateTimeZero
implicit val zeroDT = dateTimeZero(now)

import axle.visualize.Plot
import spire.implicits.DoubleAlgebra
import axle.algebra.Plottable.doublePlottable
import axle.joda.dateTimeOrder
import axle.joda.dateTimePlottable
import axle.joda.dateTimeTics
import axle.joda.dateTimeDurationLengthSpace

val plot = Plot(
  waves,
  title = Some("Random Waves"),
  xAxis = Some(0d),
  xAxisLabel = Some("time (t)"),
  yAxisLabel = Some("A sin(ωt + φ)"))

import axle.web._
svg(plot, "waves.svg")
```

![waves](../images/waves.svg)

Animation
---------

This example traces two "saw" functions vs time:

```scala
import collection.immutable.TreeMap
import org.joda.time.DateTime
import axle.joda._
import spire.compat.ordering

val initialData = List(
  ("saw 1", new TreeMap[DateTime, Double]()),
  ("saw 2", new TreeMap[DateTime, Double]())
)

import spire.implicits.DoubleAlgebra
import axle.visualize._

val now = new DateTime()
implicit val dtz = dateTimeZero(now)

val plot = Plot[DateTime, Double, TreeMap[DateTime, Double]](
  initialData,
  connect = true,
  title = Some("Saws"),
  xAxis = Some(0d),
  xAxisLabel = Some("time (t)"),
  yAxisLabel = Some("y")
)

val saw1 = (t: Long) => (t % 10000) / 10000d
val saw2 = (t: Long) => (t % 100000) / 50000d

val fs = List(saw1, saw2)

val refreshFn = (previous: List[(String, TreeMap[DateTime, Double])]) => {
  val now = new DateTime()
  previous.zip(fs).map({ case (old, f) => (old._1, old._2 ++ Vector(now -> f(now.getMillis))) })
}

import akka.actor.ActorSystem
implicit val system = ActorSystem("Animator")

import axle.jung._
import axle.quanta.Time
import edu.uci.ics.jung.graph.DirectedSparseGraph

implicit val timeConverter = {
  import axle.algebra.modules.doubleRationalModule
  Time.converterGraphK2[Double, DirectedSparseGraph]
}
import timeConverter.millisecond

play(plot, refreshFn, 500 *: millisecond)
```
