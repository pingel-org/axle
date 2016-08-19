
Entropy of a Biased Coin
========================

Visualize the relationship of a coin's bias to its entropy with this code snippet.

Imports and implicits:

```scala
import axle._
import axle.stats._
import spire.math._
import spire.algebra._
import axle.quanta.Information
import scala.collection.immutable.TreeMap
import axle.quanta.UnittedQuantity
import spire.math.Rational

type D = TreeMap[Rational, UnittedQuantity[Information, Double]]

import spire.implicits.DoubleAlgebra
import axle.jung.directedGraphJung
import edu.uci.ics.jung.graph.DirectedSparseGraph
import axle.quanta.UnitOfMeasurement

implicit val id = Information.converterGraphK2[Double, DirectedSparseGraph]

import axle.stats.H
import axle.stats.coin
```

Create dataset

```scala
scala> val hm: D = new TreeMap[Rational, UnittedQuantity[Information, Double]]() ++ (0 to 100).map(i => (Rational(i / 100d), H(coin(Rational(i, 100))))).toMap
hm: D = Map(0 -> UnittedQuantity(0.0,UnitOfMeasurement(bit,b,None)), 5764607523034235/576460752303423488 -> UnittedQuantity(0.08079313589591118,UnitOfMeasurement(bit,b,None)), 5764607523034235/288230376151711744 -> UnittedQuantity(0.14144054254182067,UnitOfMeasurement(bit,b,None)), 1080863910568919/36028797018963968 -> UnittedQuantity(0.19439185783157623,UnitOfMeasurement(bit,b,None)), 5764607523034235/144115188075855872 -> UnittedQuantity(0.24229218908241482,UnitOfMeasurement(bit,b,None)), 3602879701896397/72057594037927936 -> UnittedQuantity(0.28639695711595625,UnitOfMeasurement(bit,b,None)), 1080863910568919/18014398509481984 -> UnittedQuantity(0.32744491915447627,UnitOfMeasurement(bit,b,None)), 1261007895663739/18014398509481984 -> UnittedQuantity(0.36592365090022316,UnitOfMeasureme...
```

Define visualization

```scala
scala> import axle.visualize._
import axle.visualize._

scala> implicit val bitDouble = id.bit
bitDouble: axle.quanta.UnitOfMeasurement[axle.quanta.Information] = UnitOfMeasurement(bit,b,None)

scala> implicit val ut = axle.quanta.unittedTicsGraphK2[Information, Double, DirectedSparseGraph]
ut: axle.algebra.Tics[axle.quanta.UnittedQuantity[axle.quanta.Information,Double]] = axle.quanta.package$$anon$6@16779030

scala> val plot = new Plot[Rational, UnittedQuantity[Information, Double], D](
     |   List(("h", hm)),
     |   connect = true,
     |   drawKey = false,
     |   xAxis = Some(0d *: bitDouble),
     |   xAxisLabel = Some("p(x='HEAD)"),
     |   yAxis = Some(Rational(0)),
     |   yAxisLabel = Some("H"),
     |   title = Some("Entropy"))
plot: axle.visualize.Plot[spire.math.Rational,axle.quanta.UnittedQuantity[axle.quanta.Information,Double],D] = Plot(List((h,Map(0 -> UnittedQuantity(0.0,UnitOfMeasurement(bit,b,None)), 5764607523034235/576460752303423488 -> UnittedQuantity(0.08079313589591118,UnitOfMeasurement(bit,b,None)), 5764607523034235/288230376151711744 -> UnittedQuantity(0.14144054254182067,UnitOfMeasurement(bit,b,None)), 1080863910568919/36028797018963968 -> UnittedQuantity(0.19439185783157623,UnitOfMeasurement(bit,b,None)), 5764607523034235/144115188075855872 -> UnittedQuantity(0.24229218908241482,UnitOfMeasurement(bit,b,None)), 3602879701896397/72057594037927936 -> UnittedQuantity(0.28639695711595625,UnitOfMeasurement(bit,b,None)), 1080863910568919/18014398509481984 -> UnittedQuantity(0.32744491915447627,UnitO...
```

Export to SVG

```scala
scala> import axle.web._
import axle.web._

scala> svg(plot, "coinentropy.svg")
```

The result is the classic Claude Shannon graph

![coin entropy](../images/coinentropy.svg)
