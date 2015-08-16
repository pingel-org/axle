package axle.visualize

import scala.collection.immutable.TreeMap
import spire.math.sin
import scala.util.Random

import org.joda.time.DateTime
import org.specs2.mutable.Specification

import axle.joda.dateTimeOrder
import axle.joda.dateTimePlottable
import axle.joda.dateTimeZero
import axle.joda.dateTimeTics
import axle.joda.dateTimeDurationLengthSpace
import axle.quanta.Information
import axle.quanta.UnittedQuantity
import axle.stats.H
import axle.stats.coin

import spire.compat.ordering
import spire.implicits.DoubleAlgebra
import spire.math.Rational

class TimeSeriesPlotSpec extends Specification {

  def t1(): Unit = {

    val now = new DateTime()

    def randomTimeSeries(i: Int) = {
      val phase = Random.nextDouble
      val amp = Random.nextDouble
      val f = Random.nextDouble
      ("series " + i,
        new TreeMap[DateTime, Double]() ++
        (0 to 100).map(j => (now.plusMinutes(2 * j) -> amp * sin(phase + (j / (10 * f))))).toMap)
    }

    val lfs = (0 until 20).map(randomTimeSeries)

    implicit val dateZero = dateTimeZero(now)

    val plot = Plot[DateTime, Double, TreeMap[DateTime, Double]](
      lfs,
      connect = true,
      drawKey = true,
      xAxis = Some(0d),
      yAxis = Some(now))

  }

  def t2(): Unit = {

    type D = TreeMap[Rational, UnittedQuantity[Information, Double]]

    import edu.uci.ics.jung.graph.DirectedSparseGraph
    import axle.jung.directedGraphJung
    import axle.quanta.UnitOfMeasurement
    implicit val id = Information.converterGraphK2[Double, DirectedSparseGraph]

    val hm: D =
      new TreeMap[Rational, UnittedQuantity[Information, Double]]() ++
        (0 to 100).map(i => (Rational(i / 100d), H(coin(Rational(i, 100))))).toMap

    implicit val bitDouble = id.bit
    //implicit val pdv = axle.visualize.PlotDataView.treeMapDataView[Rational, UnittedQuantity4[Information[Double], Double]]

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

    // svg(plot, "")
  }

}
