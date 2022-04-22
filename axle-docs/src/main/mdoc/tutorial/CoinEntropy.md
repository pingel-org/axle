# Entropy of a Biased Coin

Visualize the relationship of a coin's bias to its entropy with this code snippet.

Imports and implicits:

```scala mdoc:silent
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

```scala mdoc
val hm: D =
  new TreeMap[Rational, UnittedQuantity[Information, Double]]() ++
    (0 to 100).map({ i =>
      val r = Rational(i.toLong, 100L)
      r -> H[Symbol, Rational](Coin.flipModel(r))
    }).toMap
```

Define visualization

```scala mdoc
import axle.visualize._

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra

val plot = Plot[String, Rational, UnittedQuantity[Information, Double], D](
  () => List(("h", hm)),
  connect = true,
  colorOf = _ => Color.black,
  drawKey = false,
  xAxisLabel = Some("p(x='HEAD)"),
  yAxisLabel = Some("H"),
  title = Some("Entropy")).zeroAxes
```

Create the SVG

```scala mdoc
import axle.web._
import cats.effect._

plot.svg[IO]("coinentropy.svg").unsafeRunSync()
```

The result is the classic Claude Shannon graph

![coin entropy](/tutorial/images/coinentropy.svg)
