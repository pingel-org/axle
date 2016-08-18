Plots
=====

Two-dimensional plots

Time-series plot example
------------------------

`axle.visualize.Plot`

```scala
scala> import axle._
import axle._

scala> import axle.visualize._
import axle.visualize._

scala> import org.joda.time.DateTime
import org.joda.time.DateTime

scala> import spire.compat.ordering
import spire.compat.ordering

scala> import scala.collection.immutable.TreeMap
import scala.collection.immutable.TreeMap

scala> import scala.math.sin
import scala.math.sin

scala> import scala.util.Random.nextDouble
import scala.util.Random.nextDouble

scala> import axle.joda.dateTimeOrder
import axle.joda.dateTimeOrder

scala> val now = new DateTime()
now: org.joda.time.DateTime = 2016-08-18T12:14:08.600-07:00

scala> def randomTimeSeries(i: Int) = {
     |   val φ = nextDouble
     |   val A = nextDouble
     |   val ω = 0.1 / nextDouble
     |   ("%1.2f %1.2f %1.2f".format(φ, A, ω),
     |     new TreeMap[DateTime, Double]() ++
     |     (0 to 100).map(t => (now.plusMinutes(2 * t) -> A * sin(ω*t + φ))).toMap)
     | }
randomTimeSeries: (i: Int)(String, scala.collection.immutable.TreeMap[org.joda.time.DateTime,Double])

scala> val waves = (0 until 20).map(randomTimeSeries)
waves: scala.collection.immutable.IndexedSeq[(String, scala.collection.immutable.TreeMap[org.joda.time.DateTime,Double])] = Vector((0.03 0.16 0.11,Map(2016-08-18T12:14:08.600-07:00 -> 0.004847360123426411, 2016-08-18T12:16:08.600-07:00 -> 0.022425504420866254, 2016-08-18T12:18:08.600-07:00 -> 0.03973992517070553, 2016-08-18T12:20:08.600-07:00 -> 0.05658700508170737, 2016-08-18T12:22:08.600-07:00 -> 0.07276862278288915, 2016-08-18T12:24:08.600-07:00 -> 0.08809448272744917, 2016-08-18T12:26:08.600-07:00 -> 0.10238435306513538, 2016-08-18T12:28:08.600-07:00 -> 0.1154701851657149, 2016-08-18T12:30:08.600-07:00 -> 0.12719808986798528, 2016-08-18T12:32:08.600-07:00 -> 0.13743014721367225, 2016-08-18T12:34:08.600-07:00 -> 0.1460460283837752, 2016-08-18T12:36:08.600-07:00 -> 0.15294441076341586...

scala> import axle.joda.dateTimeZero
import axle.joda.dateTimeZero

scala> implicit val zeroDT = dateTimeZero(now)
zeroDT: axle.algebra.Zero[org.joda.time.DateTime] = axle.joda.package$$anon$3@fae35b4

scala> import axle.visualize.Plot
import axle.visualize.Plot

scala> import spire.implicits.DoubleAlgebra
import spire.implicits.DoubleAlgebra

scala> import axle.algebra.Plottable.doublePlottable
import axle.algebra.Plottable.doublePlottable

scala> import axle.joda.dateTimeOrder
import axle.joda.dateTimeOrder

scala> import axle.joda.dateTimePlottable
import axle.joda.dateTimePlottable

scala> import axle.joda.dateTimeTics
import axle.joda.dateTimeTics

scala> import axle.joda.dateTimeDurationLengthSpace
import axle.joda.dateTimeDurationLengthSpace

scala> val plot = Plot(
     |   waves,
     |   title = Some("Random Waves"),
     |   xAxis = Some(0d),
     |   xAxisLabel = Some("time (t)"),
     |   yAxisLabel = Some("A sin(ωt + φ)"))
plot: axle.visualize.Plot[org.joda.time.DateTime,Double,scala.collection.immutable.TreeMap[org.joda.time.DateTime,Double]] = Plot(Vector((0.03 0.16 0.11,Map(2016-08-18T12:14:08.600-07:00 -> 0.004847360123426411, 2016-08-18T12:16:08.600-07:00 -> 0.022425504420866254, 2016-08-18T12:18:08.600-07:00 -> 0.03973992517070553, 2016-08-18T12:20:08.600-07:00 -> 0.05658700508170737, 2016-08-18T12:22:08.600-07:00 -> 0.07276862278288915, 2016-08-18T12:24:08.600-07:00 -> 0.08809448272744917, 2016-08-18T12:26:08.600-07:00 -> 0.10238435306513538, 2016-08-18T12:28:08.600-07:00 -> 0.1154701851657149, 2016-08-18T12:30:08.600-07:00 -> 0.12719808986798528, 2016-08-18T12:32:08.600-07:00 -> 0.13743014721367225, 2016-08-18T12:34:08.600-07:00 -> 0.1460460283837752, 2016-08-18T12:36:08.600-07:00 -> 0.15294441076...

scala> import axle.web._
import axle.web._

scala> svg(plot, "waves.svg")
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
