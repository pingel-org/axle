package axle.visualize

trait Plottable[T] extends Ordering[T] with Portionable[T]

object Plottable {

  import org.joda.time.DateTime
  
  implicit object DoublePlottable extends Plottable[Double] {

    def compare(d1: Double, d2: Double) = (d1 - d2) match {
      case 0.0 => 0
      case r @ _ if r > 0.0 => 1
      case _ => -1
    }

    def portion(left: Double, v: Double, right: Double) = (v - left) / (right - left)
  }

  implicit object LongPlottable extends Plottable[Long] {

    def compare(l1: Long, l2: Long) = (l1 - l2) match {
      case 0L => 0
      case r @ _ if r > 0L => 1
      case _ => -1
    }

    def portion(left: Long, v: Long, right: Long) = (v - left).toDouble / (right - left)
  }

  implicit object DateTimePlottable extends Plottable[DateTime] {

    def compare(dt1: DateTime, dt2: DateTime) = dt1.compareTo(dt2)

    def portion(left: DateTime, v: DateTime, right: DateTime) = (v.getMillis - left.getMillis).toDouble / (right.getMillis - left.getMillis)
  }

}
