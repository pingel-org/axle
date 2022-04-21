---
layout: page
title: Entropy of a Biased Coin
permalink: /tutorial/entropy_biased_coin/
---

Visualize the relationship of a coin's bias to its entropy with this code snippet.

Imports and implicits:

```scala
import scala.collection.immutable.TreeMap
import cats.implicits._
import spire.math.Rational
import spire.algebra._
import axle.stats.H
import axle.data.Coin
import axle.quanta.UnittedQuantity
import axle.quanta.Information

type D = TreeMap[Rational, UnittedQuantity[Information, Double]]

import edu.uci.ics.jung.graph.DirectedSparseGraph
import axle.jung.directedGraphJung
import cats.kernel.Order
import axle.quanta.unittedTics

implicit val id = {
  implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
  Information.converterGraphK2[Double, DirectedSparseGraph]
}

implicit val or: Order[Rational] = new cats.kernel.Order[Rational] {
  implicit val doubleOrder = Order.fromOrdering[Double]
  def compare(x: Rational, y: Rational): Int = doubleOrder.compare(x.toDouble, y.toDouble)
}
implicit val bitDouble = id.bit
```

Create dataset

```scala
val hm: D =
  new TreeMap[Rational, UnittedQuantity[Information, Double]]() ++
    (0 to 100).map({ i =>
      val r = Rational(i.toLong, 100L)
      r -> H[Symbol, Rational](Coin.flipModel(r))
    }).toMap
// hm: D = TreeMap(
//   0 -> UnittedQuantity(
//     magnitude = 0.0,
//     unit = UnitOfMeasurement(name = "bit", symbol = "b", wikipediaUrl = None)
//   ),
//   1/100 -> UnittedQuantity(
//     magnitude = 0.08079313589591118,
//     unit = UnitOfMeasurement(name = "bit", symbol = "b", wikipediaUrl = None)
//   ),
//   1/50 -> UnittedQuantity(
//     magnitude = 0.14144054254182067,
//     unit = UnitOfMeasurement(name = "bit", symbol = "b", wikipediaUrl = None)
//   ),
//   3/100 -> UnittedQuantity(
//     magnitude = 0.19439185783157623,
//     unit = UnitOfMeasurement(name = "bit", symbol = "b", wikipediaUrl = None)
//   ),
//   1/25 -> UnittedQuantity(
//     magnitude = 0.24229218908241482,
//     unit = UnitOfMeasurement(name = "bit", symbol = "b", wikipediaUrl = None)
//   ),
//   1/20 -> UnittedQuantity(
//     magnitude = 0.28639695711595625,
//     unit = UnitOfMeasurement(name = "bit", symbol = "b", wikipediaUrl = None)
//   ),
//   3/50 -> UnittedQuantity(
//     magnitude = 0.32744491915447627,
//     unit = UnitOfMeasurement(name = "bit", symbol = "b", wikipediaUrl = None)
//   ),
//   7/100 -> UnittedQuantity(
//     magnitude = 0.36592365090022316,
//     unit = UnitOfMeasurement(name = "bit", symbol = "b", wikipediaUrl = None)
//   ),
//   2/25 -> UnittedQuantity(
//     magnitude = 0.4021791902022729,
//     unit = UnitOfMeasurement(name = "bit", symbol = "b", wikipediaUrl = None)
//   ),
//   9/100 -> UnittedQuantity(
//     magnitude = 0.4364698170641029,
//     unit = UnitOfMeasurement(name = "bit", symbol = "b", wikipediaUrl = None)
//   ),
//   1/10 -> UnittedQuantity(
//     magnitude = 0.4689955935892812,
//     unit = UnitOfMeasurement(name = "bit", symbol = "b", wikipediaUrl = None)
//   ),
//   11/100 -> UnittedQuantity(
//     magnitude = 0.499915958164528,
//     unit = UnitOfMeasurement(name = "bit", symbol = "b", wikipediaUrl = None)
//   ),
// ...
```

Define visualization

```scala
import axle.visualize._

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
// fieldDouble: Field[Double] = spire.std.DoubleAlgebra@454746e2

val plot = Plot[String, Rational, UnittedQuantity[Information, Double], D](
  () => List(("h", hm)),
  connect = true,
  colorOf = _ => Color.black,
  drawKey = false,
  xAxisLabel = Some("p(x='HEAD)"),
  yAxisLabel = Some("H"),
  title = Some("Entropy")).zeroAxes
// plot: Plot[String, Rational, UnittedQuantity[Information, Double], D] = Plot(
//   dataFn = <function0>,
//   connect = true,
//   drawKey = false,
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
//   title = Some(value = "Entropy"),
//   keyTitle = None,
//   xAxis = Some(
//     value = UnittedQuantity(
//       magnitude = 0.0,
//       unit = UnitOfMeasurement(
//         name = "byte",
//         symbol = "B",
//         wikipediaUrl = Some(value = "http://en.wikipedia.org/wiki/Byte")
//       )
//     )
//   ),
//   xAxisLabel = Some(value = "p(x='HEAD)"),
//   yAxis = Some(value = 0),
//   yAxisLabel = Some(value = "H")
// )
```

Create the SVG

```scala
import axle.web._
import cats.effect._

plot.svg[IO]("coinentropy.svg").unsafeRunSync()
```

The result is the classic Claude Shannon graph

![coin entropy](/tutorial/images/coinentropy.svg)
