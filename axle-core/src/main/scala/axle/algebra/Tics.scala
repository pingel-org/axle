package axle.algebra

import java.lang.Double.{ isInfinite, isNaN }
import scala.annotation.implicitNotFound
import cats.implicits._
import spire.math._
import spire.implicits.literalLongOps
import axle.showRational

@implicitNotFound("Witness not found for Tics[${T}]")
trait Tics[T] {

  def tics(from: T, to: T): Seq[(T, String)]

}

object Tics {

  final def apply[T: Tics]: Tics[T] = implicitly[Tics[T]]

  implicit def doubleTics: Tics[Double] = new Tics[Double] {

    def step(from: Double, to: Double): Double =
      pow(10, ceil(log10(abs(to - from)) - 0.3) - 1)

    def tics(from: Double, to: Double): Seq[(Double, String)] = {
      if ((from == to) || from.isNaN || from.isInfinity || to.isNaN || to.isInfinity) {
        List((0d, "0.0"), (1d, "1.0"))
      } else {
        val s = step(from, to)
        val n = ceil((to - from) / s).toInt
        val w = s * floor(from / s)
        val start = BigDecimal.valueOf(w)
        (0 to n).map(i => {
          val v = start + BigDecimal(s) * i
          (v.toDouble, v.show)
        }).filter({ case (d, _) => (d >= from && d <= to) })
      }
    }

  }

  implicit def longTics: Tics[Long] = new Tics[Long] {

    def step(from: Long, to: Long): Long = {
      val n = floor(spire.math.ceil(spire.math.log10(abs(to - from).toDouble)) - 1d)
      max(1, pow(10, n).toLong)
    }

    def tics(from: Long, to: Long): Seq[(Long, String)] = {
      val s = step(from, to)
      val n = (to - from) / s
      val start = (s * (from / s))
      (0L to n).map(i => {
        val v = start + s * i
        (v, v.show)
      }).filter(vs => (vs._1 >= from && vs._1 <= to))
    }
  }

  implicit def intTics: Tics[Int] = new Tics[Int] {

    def step(from: Int, to: Int): Int = {
      val n = floor(spire.math.ceil(spire.math.log10(abs(to - from).toDouble)) - 1d)
      max(1, pow(10, n).toInt)
    }

    def tics(from: Int, to: Int): Seq[(Int, String)] = {
      val s = step(from, to)
      val n = (to - from) / s
      val start = (s * (from / s))
      (0 to n).map(i => {
        val v = start + s * i
        (v, v.show)
      }).filter(vs => (vs._1 >= from && vs._1 <= to))
    }

  }

  implicit def rationalTics: Tics[Rational] = new Tics[Rational] {

    def step(from: Rational, to: Rational): Rational = {
      val power = ceil(log10((to - from).abs.toDouble)).toLong - 1L
      if (power >= 0d) {
        Rational(10L ** power, 1L)
      } else {
        // spire doesn't like negative arguments to **
        Rational(1L, 10L ** power.abs)
      }
    }

    def tics(from: Rational, to: Rational): Seq[(Rational, String)] = {
      val fromDouble = from.toDouble
      val toDouble = to.toDouble
      if (isNaN(fromDouble) || isInfinite(fromDouble) || isNaN(toDouble) || isInfinite(toDouble)) {
        List((Rational.zero, "0"), (Rational(1), "1"))
      } else {
        val s = step(from, to)
        val start = (from / s).floor * s
        val n = ((to - from) / s).ceil.toInt
        (0 to n).map(i => {
          val v = start + s * i
          (v, v.show)
        }).filter({ case (d, _) => (d >= from && d <= to) })
      }
    }

  }

}
