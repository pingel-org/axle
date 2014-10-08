package axle.visualize

import scala.Vector
import scala.collection.immutable.TreeMap
import scala.math.sin
import scala.util.Random

import org.joda.time.DateTime
import org.specs2.mutable.Specification

import axle.algebra.Plottable.DateTimePlottable
import axle.algebra.Plottable.DoublePlottable
import axle.quanta.Information
import axle.quanta.Information.bit
import axle.quanta.UnittedQuantity
import axle.quanta.UnitPlottable
import axle.quanta.doubleDoubleMetricSpace
import axle.quanta.modulize
import axle.quanta.realDoubleMetricSpace
import axle.stats.H
import axle.stats.coin
import spire.algebra.Eq
import spire.compat.ordering
import spire.implicits.DoubleAlgebra
import spire.implicits.SeqOrder
import spire.implicits.moduleOps
import spire.implicits.StringAlgebra
import spire.implicits._
import spire.math.Rational
import spire.math.Real
import spire.compat.ordering

class TimeSeriesPlotSpec extends Specification {

  "Tics for units" should {
    "work" in {

      import axle.quanta.Information._

      val plottable = UnitPlottable[Information, Double](bit[Double])

      val tics = plottable.tics(0d *: bit[Double], 1d *: bit[Double]).toVector

      val expected = Vector(
        (0.0 *: bit[Double], "0.0"),
        //        (0.1 *: bit[Double], "0.1"),
        //        (0.2 *: bit[Double], "0.2"),
        //        (0.3 *: bit[Double], "0.3"),
        //        (0.4 *: bit[Double], "0.4"),
        //        (0.5 *: bit[Double], "0.5"),
        //        (0.6 *: bit[Double], "0.6"),
        //        (0.7 *: bit[Double], "0.7"),
        //        (0.8 *: bit[Double], "0.8"),
        //        (0.9 *: bit[Double], "0.9"),
        (1.0 *: bit[Double], "1.0"))

      val vieq = implicitly[Eq[Vector[(UnittedQuantity[Information, Double], String)]]]

      // tics must be equalTo expected
      true must be equalTo (vieq.eqv(tics, expected))
    }
  }

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

    // show(plot)
  }

  def t2(): Unit = {

    implicit val plottable = UnitPlottable[Information, Double](bit[Double])

    type D = TreeMap[Rational, UnittedQuantity[Information, Double]]
    val hm: D = new TreeMap[Rational, UnittedQuantity[Information, Double]]() ++ (0 to 100).map(i => (Rational(i / 100d), H(coin(Rational(i, 100))))).toMap

    import Plot._ // TODO find implicits without this

    val plot = new Plot[Rational, UnittedQuantity[Information, Double], D](
      List(("h", hm)),
      connect = true,
      drawKey = false,
      xAxis = Some(0d *: bit[Double]),
      xAxisLabel = Some("p(x='HEAD)"),
      yAxis = Some(Rational(0)),
      yAxisLabel = Some("H"),
      title = Some("Entropy"))

    // show(plot)

  }

}
