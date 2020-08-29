package axle.quanta

import org.scalatest._

import scala.collection.immutable.TreeMap

import cats.effect._
import cats.implicits._

import spire.math.Rational
import spire.algebra._

import axle.stats.H
import axle.data.Coin
import axle.visualize._
import axle.web._

class CoinEntropyPlotSpec extends FunSuite with Matchers {

  test("coin entropy plot") {

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

    val hm: D =
      new TreeMap[Rational, UnittedQuantity[Information, Double]]() ++
        (0 to 100).map({ i =>
          val r = Rational(i / 100d)
          r -> H[Symbol, Rational](Coin.flipModel(r))
        }).toMap

    // implicit val zr = Zero[Rational]
    // implicit val tr = Tics[Rational]
    // implicit val er = Eq[Rational]
    // implicit val lsrrd = LengthSpace[Rational, Rational, Double]
    // implicit val zuqid = Zero[UnittedQuantity[Information, Double]]
    // implicit val tuqid = unittedTics[Information, Double, DG]
    // implicit val euqid = Eq[UnittedQuantity[Information, Double]]
    // implicit val lsuqiddd = LengthSpace[UnittedQuantity[Information, Double], Double, Double]
    // implicit val pdv = PlotDataView.treeMapDataView[Rational, UnittedQuantity[Information, Double]]

    implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
    val plot = Plot[String, Rational, UnittedQuantity[Information, Double], D](
      () => List(("h", hm)),
      connect = true,
      colorOf = _ => Color.black,
      drawKey = false,
      xAxisLabel = Some("p(x='HEAD)"),
      yAxisLabel = Some("H"),
      title = Some("Entropy")).zeroAxes

    SVG[Plot[String, Rational, UnittedQuantity[Information, Double], D]]

    val svgName = "coinentropyplot.svg"
    plot.svg[IO](svgName).unsafeRunSync()
    new java.io.File(svgName).exists should be(true)
  }

}
