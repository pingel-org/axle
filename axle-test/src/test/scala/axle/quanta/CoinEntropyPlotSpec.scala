package axle.quanta

import axle.visualize._

import scala.collection.immutable.TreeMap

import org.specs2.mutable.Specification

import axle.stats.H
import axle.stats.coin
import axle.orderToOrdering
import spire.math.Rational
import spire.implicits._
import cats.implicits._

class CoinEntropyPlotSpec extends Specification {

  "coin entropy" should {

    "plot" in {

      type D = TreeMap[Rational, UnittedQuantity[Information, Double]]

      import edu.uci.ics.jung.graph.DirectedSparseGraph
      import axle.jung.directedGraphJung

      implicit val id = Information.converterGraphK2[Double, DirectedSparseGraph]

      import axle._

      val hm: D =
        new TreeMap[Rational, UnittedQuantity[Information, Double]]() ++
          (0 to 100).map(i => (Rational(i / 100d), H(coin(Rational(i, 100))))).toMap

      implicit val bitDouble = id.bit
      // implicit val pdv = axle.visualize.PlotDataView.treeMapDataView[Rational, UnittedQuantity[Information, Double]]

      val plot = Plot[Rational, UnittedQuantity[Information, Double], D](
        List(("h", hm)),
        connect = true,
        drawKey = false,
        xAxis = Some(0d *: bitDouble),
        xAxisLabel = Some("p(x='HEAD)"),
        yAxis = Some(Rational(0)),
        yAxisLabel = Some("H"),
        title = Some("Entropy"))

      import axle.web._
      val d = SVG[Plot[Rational, UnittedQuantity[Information, Double], D]]

      val svgName = "coinentropyplot.svg"
      svg(plot, svgName)
      new java.io.File(svgName).exists must be equalTo true
    }
  }

}
