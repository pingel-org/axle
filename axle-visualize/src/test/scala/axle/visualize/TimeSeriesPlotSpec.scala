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
import axle.quanta.Information.Q
import axle.quanta.Information.bit
import axle.quanta.Information.eqTypeclass
import axle.stats.H
import axle.stats.coin
import spire.algebra.Eq
import spire.implicits.SeqEq
import spire.implicits.StringOrder
import spire.implicits.eqOps
import spire.math.Number.apply
import spire.math.Rational

class TimeSeriesPlotSpec extends Specification {

  //  implicit def eqVector[T: Eq] = new Eq[Vector[T]] {
  //    def eqv(x: Vector[T], y: Vector[T]): Boolean = x.length == y.length && (x.zip(y).forall({ case (a, b) => a === b}))
  //  }

  implicit def eqTuple2[T: Eq, U: Eq] = new Eq[(T, U)] {
    def eqv(x: (T, U), y: (T, U)): Boolean = x._1 === y._1 && x._2 === y._2
  }

  "Tics for units" should {
    "work" in {

      val tics = bit.plottable.tics(0 *: bit, 1 *: bit).toVector

      val expected = Vector(
        (0.0 *: bit, "0.0"),
        (0.1 *: bit, "0.1"),
        (0.2 *: bit, "0.2"),
        (0.3 *: bit, "0.3"),
        (0.4 *: bit, "0.4"),
        (0.5 *: bit, "0.5"),
        (0.6 *: bit, "0.6"),
        (0.7 *: bit, "0.7"),
        (0.8 *: bit, "0.8"),
        (0.9 *: bit, "0.9"),
        (1.0 *: bit, "1.0"))

      // implicit val ieq = implicitly[Eq[Information.Q]]
      implicit val ieqx = implicitly[Eq[(Information.Q, String)]](eqTuple2[Information.Q, String])
      implicit val vieq = implicitly[Eq[Vector[(Information.Q, String)]]]

      //      tics must be equalTo expected
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

    val lfs = (0 until 20).map(i => randomTimeSeries(i)).toList

    val plot = new Plot(
      lfs,
      (d: TreeMap[DateTime, Double]) => d.keysIterator.toVector,
      (d: TreeMap[DateTime, Double], dt: DateTime) => d(dt),
      connect = true,
      drawKey = true,
      xAxis = Some(0d),
      yAxis = Some(now))

    // show(plot)
  }

  def t2(): Unit = {

    val hm = new TreeMap[Double, Q]() ++ (0 to 100).map(i => (i / 100.0, H(coin(Rational(i, 100))))).toMap

    val plot = new Plot(
      List(("h", hm)),
      (d: TreeMap[Double, Q]) => d.keysIterator.toVector,
      (d: TreeMap[Double, Q], x: Double) => d(x),
      connect = true,
      drawKey = false,
      xAxis = Some(0.0 *: bit),
      xAxisLabel = Some("p(x='HEAD)"),
      yAxis = Some(0.0),
      yAxisLabel = Some("H"),
      title = Some("Entropy"))(DoublePlottable, Information.UnitPlottable(bit))

    // show(plot)

  }

}
