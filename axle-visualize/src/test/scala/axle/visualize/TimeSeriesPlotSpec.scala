package axle.visualize

import scala.collection.immutable.TreeMap
import scala.math.sin
import scala.util.Random

import org.joda.time.DateTime
import org.specs2.mutable.Specification

import axle.algebra.Plottable
import axle.joda._
import axle.quanta.Information
import axle.quanta.Information.bit
import axle.quanta.UnittedQuantity
import axle.quanta.UnitOfMeasurement
import axle.quanta.modulize3
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

    val lfs = (0 until 20).map(randomTimeSeries).toList

    val plot = new Plot[DateTime, Double, TreeMap[DateTime, Double]](
      lfs,
      connect = true,
      drawKey = true,
      xAxis = Some(0d),
      yAxis = Some(now))

  }

  def t2(): Unit = {

    import axle.jung.JungDirectedGraph
    import axle.jung.JungDirectedGraph.directedGraphJung // conversion graph

    type D = TreeMap[Rational, UnittedQuantity[Information, Double]]
    val hm: D = new TreeMap[Rational, UnittedQuantity[Information, Double]]() ++ (0 to 100).map(i => (Rational(i / 100d), H(coin(Rational(i, 100))))).toMap

    implicit val base = bit[Double]
    //implicit val cg = axle.quanta.Information.conversionGraph[Double, JungDirectedGraph]
    implicit val orderThem = axle.quanta.unit3Order[Information, Double, JungDirectedGraph]
    //implicit val pdv = axle.visualize.PlotDataView.treeMapDataView[Rational, UnittedQuantity3[Information, Double]]

    val plot = new Plot[Rational, UnittedQuantity[Information, Double], D](
      List(("h", hm)),
      connect = true,
      drawKey = false,
      xAxis = Some(0d *: bit[Double]),
      xAxisLabel = Some("p(x='HEAD)"),
      yAxis = Some(Rational(0)),
      yAxisLabel = Some("H"),
      title = Some("Entropy"))

  }

}
