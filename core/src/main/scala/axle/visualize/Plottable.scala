package axle.visualize

trait Plottable[T] extends Ordering[T] with Portionable[T]

object Plottable {

  import org.joda.time.DateTime

  implicit object DoublePlottable extends Plottable[Double] {

    import math.{ pow, abs, log, floor, ceil, BigDecimal }

    def compare(d1: Double, d2: Double) = (d1 - d2) match {
      case 0.0 => 0
      case r @ _ if r > 0.0 => 1
      case _ => -1
    }

    def portion(left: Double, v: Double, right: Double) = (v - left) / (right - left)

    def step(from: Double, to: Double): Double = pow(10, floor(log(abs(to - from))) - 1)

    def tics(from: Double, to: Double): Seq[(Double, String)] = {
      val s = step(from, to)
      val n = ceil((to - from) / s).toInt
      val start = BigDecimal.valueOf(s * floor(from / s))
      (0 to n).map(i => {
        val v = start + BigDecimal(s) * i
        (v.toDouble, v.toString)
      }).filter(vs => (vs._1 >= from && vs._1 <= to))
    }
  }

  implicit object LongPlottable extends Plottable[Long] {

    def compare(l1: Long, l2: Long) = (l1 - l2) match {
      case 0L => 0
      case r @ _ if r > 0L => 1
      case _ => -1
    }

    def portion(left: Long, v: Long, right: Long) = (v - left).toDouble / (right - left)

    def tics(from: Long, to: Long): Seq[(Long, String)] = List()
  }

  implicit object IntPlottable extends Plottable[Int] {

    def compare(i1: Int, i2: Int) = (i1 - i2) match {
      case 0 => 0
      case r @ _ if r > 0 => 1
      case _ => -1
    }

    def portion(left: Int, v: Int, right: Int) = (v - left).toDouble / (right - left)

    def tics(from: Int, to: Int): Seq[(Int, String)] = List()
  }

  implicit object DateTimePlottable extends Plottable[DateTime] {

    def compare(dt1: DateTime, dt2: DateTime) = dt1.compareTo(dt2)

    def portion(left: DateTime, v: DateTime, right: DateTime) = (v.getMillis - left.getMillis).toDouble / (right.getMillis - left.getMillis)

    def tics(from: DateTime, to: DateTime): Seq[(DateTime, String)] = List()
  }

}
