package axle.visualize

import java.awt.{ Dimension, BasicStroke, Color, Paint, Stroke, Insets, Graphics, Graphics2D, Point }
import javax.swing.JPanel
import java.awt.event.MouseEvent
import org.joda.time.DateTime
import collection._

trait Plottable[T, DT] extends Ordering[T] {

  // TODO: There must be some existing abstraction that captures this already
  
  // Ordering needs: def compare(t1: T, t2: T): Int

  def diff(v1: T, v2: T): DT

  def div(v1: DT, v2: DT): Double
}

object Plot {

  implicit object DoublePlottable extends Plottable[Double, Double] {

    def compare(d1: Double, d2: Double) = (d1 - d2) match {
      case 0.0 => 0
      case r @ _ if r > 0.0 => 1
      case _ => -1
    }

    def diff(d0: Double, d1: Double) = (d0 - d1)

    def div(d0: Double, d1: Double) = (d0 / d1)
  }

  implicit object LongPlottable extends Plottable[Long, Long] {

    def compare(l1: Long, l2: Long) = (l1 - l2) match {
      case 0L => 0
      case r @ _ if r > 0L => 1
      case _ => -1
    }

    def diff(d0: Long, d1: Long) = (d0 - d1)

    def div(d0: Long, d1: Long) = (d0 / d1)
  }

  implicit object DateTimePlottable extends Plottable[DateTime, Long] {

    def compare(dt1: DateTime, dt2: DateTime) = dt1.compareTo(dt2)

    def diff(t0: DateTime, t1: DateTime) = (t0.getMillis - t1.getMillis)

    def div(dt0: Long, dt1: Long) = (dt0.toDouble / dt1)
  }

}

class Plot[X, DX, Y, DY](fs: Seq[SortedMap[X, Y]], connect: Boolean = true)(
  implicit xPlottable: Plottable[X, DX], yPlottable: Plottable[Y, DY]) extends JPanel {

  val PAD = 50
  val WIDTH = 600
  val HEIGHT = 600
  val DIAMETER = 4

  val colors = List(Color.blue, Color.red, Color.green, Color.orange, Color.pink, Color.yellow)

  val colorStream = Stream.continually(colors.toStream).flatten

  val minX = fs.map(_.firstKey).min(xPlottable)
  val maxX = fs.map(_.lastKey).max(xPlottable)
  val minY = fs.map(_.values.min(yPlottable)).min(yPlottable)
  val maxY = fs.map(_.values.max(yPlottable)).max(yPlottable)

  val scaledArea = new ScaledArea2D(
    WIDTH, HEIGHT, PAD,
    minX, maxX, xPlottable.diff, xPlottable.div,
    minY, maxY, yPlottable.diff, yPlottable.div)

  override def paintComponent(g: Graphics): Unit = {
    val size = getSize()
    val g2d = g.asInstanceOf[Graphics2D]
    for ((f, color) <- fs.zip(colorStream)) {
      g2d.setColor(color)
      if (connect) {
        val xsStream = f.keysIterator.toStream
        for ((x0, x1) <- xsStream.zip(xsStream.tail)) {
          scaledArea.drawLine(g2d, Point2D(x0, f(x0)), Point2D(x1, f(x1)))
        }
      }
      for (x <- f.keys) {
        scaledArea.fillOval(g2d, Point2D(x, f(x)), DIAMETER, DIAMETER)
      }
    }
  }

}
