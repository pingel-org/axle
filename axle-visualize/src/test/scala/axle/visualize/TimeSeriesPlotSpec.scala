package axle.visualize

import scala.Vector
import scala.collection.immutable.TreeMap
import scala.math.sin
import scala.util.Random

import org.joda.time.DateTime
import org.specs2.mutable.Specification

import axle.algebra.Plottable.DateTimePlottable
import axle.algebra.Plottable.DoublePlottable
import axle.quanta2.Information
import axle.quanta2.Information.bit
import axle.quanta2.Information.cgIDouble
import axle.quanta2.Information.cgIReal
import axle.quanta2.Quantity
import axle.quanta2.UnitPlottable
import axle.quanta2.doubleDoubleMetricSpace
import axle.quanta2.modulize
import axle.quanta2.realDoubleMetricSpace
import axle.stats.H
import axle.stats.coin
import spire.algebra.Eq
import spire.compat.ordering
import spire.implicits.DoubleAlgebra
import spire.implicits.eqOps
import spire.implicits.moduleOps
import spire.math.Rational
import spire.math.Real
import spire.implicits.StringAlgebra 
import spire.implicits._

class TimeSeriesPlotSpec extends Specification {

  //  implicit def eqVector[T: Eq] = new Eq[Vector[T]] {
  //    def eqv(x: Vector[T], y: Vector[T]): Boolean = x.length == y.length && (x.zip(y).forall({ case (a, b) => a === b}))
  //  }

  implicit def eqTuple2[T: Eq, U: Eq] = new Eq[(T, U)] {
    def eqv(x: (T, U), y: (T, U)): Boolean = x._1 === y._1 && x._2 === y._2
  }

  "Tics for units" should {
    "work" in {

      val plottable = UnitPlottable(bit[Double])

      val tics = plottable.tics(0d *: bit[Double], 1d *: bit[Double]).toVector

      val expected = Vector(
        (0.0 *: bit[Double], "0.0"),
        (0.1 *: bit[Double], "0.1"),
        (0.2 *: bit[Double], "0.2"),
        (0.3 *: bit[Double], "0.3"),
        (0.4 *: bit[Double], "0.4"),
        (0.5 *: bit[Double], "0.5"),
        (0.6 *: bit[Double], "0.6"),
        (0.7 *: bit[Double], "0.7"),
        (0.8 *: bit[Double], "0.8"),
        (0.9 *: bit[Double], "0.9"),
        (1.0 *: bit[Double], "1.0"))

      //implicit val eqqid = axle.quanta2.Quantity.eqqqn[Information, Double]
      val vieq = implicitly[Eq[Vector[(Quantity[Information, Double], String)]]]

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
      (d: TreeMap[DateTime, Double]) => d.keys,
      (d: TreeMap[DateTime, Double], dt: DateTime) => d(dt),
      connect = true,
      drawKey = true,
      xAxis = Some(0d),
      yAxis = Some(now))

    // show(plot)
  }

  def t2(): Unit = {

    implicit val plottable = UnitPlottable(bit[Real])

    type D = TreeMap[Real, Quantity[Information, Real]]
    val hm: D = new TreeMap[Real, Quantity[Information, Real]]() ++ (0 to 100).map(i => (Real(i / 100d), H(coin(Rational(i, 100))))).toMap

    val plot = new Plot[Real, Quantity[Information, Real], D](
      List(("h", hm)),
      (d: TreeMap[Real, Quantity[Information, Real]]) => d.keys,
      (d: TreeMap[Real, Quantity[Information, Real]], x: Real) => d(x),
      connect = true,
      drawKey = false,
      xAxis = Some(Real(0) *: bit[Real]),
      xAxisLabel = Some("p(x='HEAD)"),
      yAxis = Some(Real(0)),
      yAxisLabel = Some("H"),
      title = Some("Entropy"))

    // show(plot)

  }

}
