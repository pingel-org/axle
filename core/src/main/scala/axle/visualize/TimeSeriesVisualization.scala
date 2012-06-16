package axle.visualize

import java.awt.{ Dimension, BasicStroke, Color, Paint, Stroke, Insets, Graphics, Graphics2D, Point }
import javax.swing.JPanel
import java.awt.event.MouseEvent

// TODO: define TimeSeries type Time -> Double

class TimeSeriesVisualization(ts: List[Map[Double, Double]]) extends JPanel {

  val PAD = 50
  val WIDTH = 600
  val HEIGHT = 600
  val DIAMETER = 4

  val colors = List(Color.blue, Color.red, Color.green, Color.orange, Color.pink, Color.yellow)

  val scaledArea = new ScaledArea2D(WIDTH, HEIGHT, PAD, 0.0, 1.0, 0.0, 1.0)

  override def paintComponent(g: Graphics): Unit = {
    val size = getSize()
    val g2d = g.asInstanceOf[Graphics2D]
    for (i <- 0 until ts.length) {
      g2d.setColor(colors(i % colors.length))
      val timeSeries = ts(i)
      val times = timeSeries.keys.toList.sortWith(_ < _) // TODO: very inefficient:
      for ( (t0, t1) <- times.zip(times.tail)) {
        scaledArea.drawLine(g2d, PointDouble(t0, timeSeries(t0)), PointDouble(t1, timeSeries(t1)))
      }
      for( t <- times ) {
        scaledArea.fillOval(g2d, PointDouble(t, timeSeries(t)), DIAMETER, DIAMETER)
      }
    }
  }

}
