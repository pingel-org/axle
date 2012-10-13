package axle.visualize

import java.awt.{ Dimension, BasicStroke, Color, Paint, Stroke, Insets, Graphics, Graphics2D, Point }
import javax.swing.JPanel
import java.awt.event.MouseEvent

import axle.ml.KMeans._
import axle.visualize.Plottable._

class KMeansVisualization[D](
  classifier: KMeansClassifier[D],
  width: Int = 600, height: Int = 600,
  border: Int = 50, pointDiameter: Int = 10) extends JPanel {

  val colors = List(Color.blue, Color.red, Color.green, Color.orange, Color.pink, Color.yellow)

  val scaledArea = new ScaledArea2D(width, height, border, 0.0, 1.0, 0.0, 1.0)

  def boundingRectangle(g2d: Graphics2D): Unit = {
    g2d.setColor(Color.black)
    scaledArea.drawRectangle(g2d, Point2D(0.0, 0.0), Point2D(1.0, 1.0))
  }

  def centroid(g2d: Graphics2D, i: Int): Unit = {
    val center = Point2D(classifier.μ(i, 0), classifier.μ(i, 1))
    g2d.setColor(Color.darkGray)
    scaledArea.fillOval(g2d, center, 3 * pointDiameter, 3 * pointDiameter)
    g2d.setColor(colors(i % colors.length))
    scaledArea.drawOval(g2d, center, 3 * pointDiameter, 3 * pointDiameter)
  }

  def cluster(g2d: Graphics2D, i: Int): Unit = {
    g2d.setColor(colors(i % colors.length))
    val nd = classifier.normalizer.normalizedData()
    for (r <- 0 until nd.rows) {
      if (classifier.A(r, 0) == i) {
        // TODO figure out what to do when N > 2
        val center = Point2D(nd(r, 0), nd(r, 1))
        scaledArea.fillOval(g2d, center, pointDiameter, pointDiameter)
        // scaledArea.drawString(g2d, r.toString + "(%.2f,%.2f)".format(center.x, center.y), center)
      }
    }
  }

  // TODO: paintComponent is executed for many kinds of events that will not change the image

  override def paintComponent(g: Graphics): Unit = {
    // super.paintComponent(g)
    val size = getSize()
    // val insets = getInsets()
    // val w = size.width - (insets.left + insets.right)
    // val h = size.height - (insets.top + insets.bottom)
    val g2d = g.asInstanceOf[Graphics2D]
    boundingRectangle(g2d)
    for (i <- 0 until classifier.K()) {
      centroid(g2d, i)
    }
    for (i <- 0 until classifier.K()) {
      // TODO: inefficient loop
      cluster(g2d, i)
    }
  }
}
