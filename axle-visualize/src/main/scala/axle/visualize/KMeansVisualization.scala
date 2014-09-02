package axle.visualize

import java.awt.Color.black
import java.awt.Color.blue
import java.awt.Color.darkGray
import java.awt.Color.green
import java.awt.Color.orange
import java.awt.Color.pink
import java.awt.Color.red
import java.awt.Color.yellow
import java.awt.Dimension
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D

import axle.algebra.Plottable
import axle.ml.KMeansModule.KMeansClassifier
import axle.quanta2.Angle.{° => °}
import axle.quanta2.UnittedQuantity
import axle.visualize.element.Oval
import axle.visualize.element.Rectangle
import axle.visualize.element.XTics
import axle.visualize.element.YTics
import javax.swing.JPanel
import spire.implicits.DoubleAlgebra
import spire.implicits.IntAlgebra
import spire.implicits.eqOps
import spire.math.Number.apply
import spire.implicits.moduleOps

object KMeansVisualizationModule extends KMeansVisualizationModule

trait KMeansVisualizationModule {

  //  def visualize[D](classifier: KMeansClassifier[D],
  //    width: Int = 600,
  //    height: Int = 600,
  //    border: Int = 50,
  //    pointDiameter: Int = 10,
  //    fontName: String = "Courier New",
  //    fontSize: Int = 12) =
  //    new KMeansVisualization[D](classifier, width, height, border, pointDiameter, fontName, fontSize)

  case class KMeansVisualization[D](
    classifier: KMeansClassifier[D],
    w: Int = 600,
    h: Int = 600,
    border: Int = 50,
    pointDiameter: Int = 10,
    fontName: String = "Courier New",
    fontSize: Int = 12) extends JPanel {

    setMinimumSize(new Dimension(w + border, h + border))

    val features = classifier.features

    val colors = List(blue, red, green, orange, pink, yellow)

    val maxs = features.columnMaxs
    val mins = features.columnMins

    val minX = mins(0, 0)
    val maxX = maxs(0, 0)
    val minY = mins(0, 1)
    val maxY = maxs(0, 1)

    implicit val dp = Plottable.DoublePlottable
    val scaledArea = new ScaledArea2D(w, h, border, minX, maxX, minY, maxY)

    val normalFont = new Font(fontName, Font.BOLD, fontSize)

    val xTics = new XTics(scaledArea, dp.tics(minX, maxX), normalFont, true, 0 *: °, black)
    val yTics = new YTics(scaledArea, dp.tics(minY, maxY), normalFont, black)

    val boundingRectangle = new Rectangle(scaledArea, Point2D(minX, minY), Point2D(maxX, maxY), borderColor = Some(black))

    def centroidOval(i: Int): Oval[Double, Double] = {
      val denormalized = classifier.normalizer.denormalize(classifier.μ.row(i))
      val center = Point2D(denormalized(0), denormalized(1))
      Oval(scaledArea, center, 3 * pointDiameter, 3 * pointDiameter, colors(i % colors.length), darkGray)
    }

    def cluster(g2d: Graphics2D, i: Int): Unit = {
      g2d.setColor(colors(i % colors.length))
      (0 until features.rows) foreach { r =>
        if (classifier.a(r, 0) === i) {
          // TODO figure out what to do when N > 2
          val center = Point2D(features(r, 0), features(r, 1))
          scaledArea.fillOval(g2d, center, pointDiameter, pointDiameter)
          // scaledArea.drawString(g2d, r.toString + "(%.2f,%.2f)".format(center.x, center.y), center)
        }
      }
    }

    val centroidOvals = (0 until classifier.K).map(centroidOval)

    override def paintComponent(g: Graphics): Unit = {

      val g2d = g.asInstanceOf[Graphics2D]
      val fontMetrics = g2d.getFontMetrics

      boundingRectangle.paint(g2d)
      xTics.paint(g2d)
      yTics.paint(g2d)
      centroidOvals.map(_.paint(g2d))
      (0 until classifier.K) foreach { i =>
        cluster(g2d, i)
      }
    }
  }

}
