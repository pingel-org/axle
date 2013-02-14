package axle.visualize

import java.awt.Dimension
import java.awt.BasicStroke
import java.awt.Color._
import java.awt.Paint
import java.awt.Stroke
import java.awt.Insets
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import javax.swing.JPanel
import java.awt.event.MouseEvent
import java.awt.Font

import axle.ml.KMeansModule
import axle.matrix._

trait KmeansVisualizationModule extends KMeansModule {

  def visualize[D](classifier: KMeansClassifier[D],
    width: Int = 600, height: Int = 600,
    border: Int = 50, pointDiameter: Int = 10) =
    new KMeansVisualization[D](classifier, width, height, border, pointDiameter)

  class KMeansVisualization[D](
    classifier: KMeansClassifier[D],
    width: Int = 600, height: Int = 600,
    border: Int = 50, pointDiameter: Int = 10) extends JPanel {

    setMinimumSize(new Dimension(width + border, height + border))

    val features = classifier.features

    val colors = List(blue, red, green, orange, pink, yellow)

    val maxs = features.columnMaxs
    val mins = features.columnMins

    val minX = mins(0, 0)
    val maxX = maxs(0, 0)
    val minY = mins(0, 1)
    val maxY = maxs(0, 1)

    implicit val dp = axle.visualize.Plottable.DoublePlottable
    val xTics = dp.tics(minX, maxX)
    val yTics = dp.tics(minY, maxY)

    val scaledArea = new ScaledArea2D(width, height, border, minX, maxX, minY, maxY)

    def boundingRectangle(g2d: Graphics2D): Unit = {
      g2d.setColor(black)
      scaledArea.drawRectangle(g2d, Point2D(minX, minY), Point2D(maxX, maxY))
    }

    def centroid(g2d: Graphics2D, i: Int): Unit = {
      // TODO asInstanceOF
      val denormalized = classifier.normalizer.denormalize(classifier.μ.row(i))
      val center = Point2D(denormalized(0), denormalized(1))
      g2d.setColor(darkGray)
      scaledArea.fillOval(g2d, center, 3 * pointDiameter, 3 * pointDiameter)
      g2d.setColor(colors(i % colors.length))
      scaledArea.drawOval(g2d, center, 3 * pointDiameter, 3 * pointDiameter)
    }

    def cluster(g2d: Graphics2D, i: Int): Unit = {
      g2d.setColor(colors(i % colors.length))
      for (r <- 0 until features.rows) {
        if (classifier.a(r, 0) == i) {
          // TODO figure out what to do when N > 2
          val center = Point2D(features(r, 0), features(r, 1))
          scaledArea.fillOval(g2d, center, pointDiameter, pointDiameter)
          // scaledArea.drawString(g2d, r.toString + "(%.2f,%.2f)".format(center.x, center.y), center)
        }
      }
    }

    // TODO: paintComponent is executed for many kinds of events that will not change the image

    val normalFont = new Font("Courier New", Font.BOLD, 12)

    override def paintComponent(g: Graphics): Unit = {
      // super.paintComponent(g)
      val size = getSize()
      // val insets = getInsets()
      // val w = size.width - (insets.left + insets.right)
      // val h = size.height - (insets.top + insets.bottom)
      val g2d = g.asInstanceOf[Graphics2D]
      val fontMetrics = g2d.getFontMetrics
      g2d.setFont(normalFont)
      boundingRectangle(g2d)
      scaledArea.drawXTics(g2d, fontMetrics, xTics)
      scaledArea.drawYTics(g2d, fontMetrics, yTics)
      for (i <- 0 until classifier.K) {
        centroid(g2d, i)
      }
      for (i <- 0 until classifier.K) {
        // TODO: inefficient loop
        cluster(g2d, i)
      }
    }
  }

}