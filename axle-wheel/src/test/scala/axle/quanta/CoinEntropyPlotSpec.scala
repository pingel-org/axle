package axle.quanta

import axle.visualize._

import scala.collection.immutable.TreeMap

import org.scalatest._

import axle.stats.H
import axle.stats.coin
// import axle.orderToOrdering
import spire.math.Rational
import spire.implicits._
import cats.implicits._

class CoinEntropyPlotSpec extends FunSuite with Matchers {

  test("coin entropy plot") {

    type D = TreeMap[Rational, UnittedQuantity[Information, Double]]

    import edu.uci.ics.jung.graph.DirectedSparseGraph
    import axle.jung.directedGraphJung
    import cats.kernel.Order
    import axle.quanta.unittedTics

    implicit val id =
      Information.converterGraphK2[Double, DirectedSparseGraph]

    implicit val idg = id.conversionGraph

    type DG = DirectedSparseGraph[UnitOfMeasurement[Information], Double => Double]

    implicit val or: Order[Rational] = new cats.kernel.Order[Rational] {
      val ord = Order[Double]
      def compare(x: Rational, y: Rational): Int = ord.compare(x.toDouble, y.toDouble)
    }
    implicit val bitDouble = id.bit
    import axle.stats.ConditionalProbabilityTable0

    val hm: D =
      new TreeMap[Rational, UnittedQuantity[Information, Double]]() ++
        (0 to 100).map({ i =>
          val r = Rational(i / 100d)
          r -> H[({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ, Symbol, Rational](coin(r))
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

    val plot = Plot[String, Rational, UnittedQuantity[Information, Double], D](
      () => List(("h", hm)),
      connect = true,
      colorOf = _ => Color.black,
      drawKey = false,
      xAxis = Some(0d *: bitDouble),
      xAxisLabel = Some("p(x='HEAD)"),
      yAxis = Some(Rational(0)),
      yAxisLabel = Some("H"),
      title = Some("Entropy")) // (zr, tr, er, lsrrd, zuqid, tuqid, euqid, lsuqiddd, pdv)

    import axle.web._
    val d = SVG[Plot[String, Rational, UnittedQuantity[Information, Double], D]]

    val svgName = "coinentropyplot.svg"
    svg(plot, svgName)
    new java.io.File(svgName).exists should be(true)
  }

}
