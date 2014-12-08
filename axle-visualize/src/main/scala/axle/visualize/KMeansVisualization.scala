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
import axle.quanta.Angle3.{ ° => ° }
import axle.quanta.UnittedQuantity
import axle.visualize.element.Oval
import axle.visualize.element.Rectangle
import axle.visualize.element.XTics
import axle.visualize.element.YTics
import axle.algebra.Matrix
import axle.algebra.Tics
import axle.ml.KMeans
import javax.swing.JPanel
import java.awt.Component
import spire.implicits.DoubleAlgebra
import spire.implicits.IntAlgebra
import spire.implicits.eqOps
import spire.math.Number.apply
import spire.implicits.moduleOps
import axle.syntax.matrix._

//  implicit def enComponentKMeansClassifier[T, F[_], M[_]](classifier: KMeans[T, F, M]): Component =
//    new KMeansVisualization[T, F, M](classifier)

case class KMeansVisualization[D, F[_], M[_]: Matrix](
  classifier: KMeans[D, F, M],
  w: Int = 600,
  h: Int = 600,
  border: Int = 50,
  pointDiameter: Int = 10,
  fontName: String = "Courier New",
  fontSize: Int = 12) extends JPanel {

  setMinimumSize(new Dimension(w + border, h + border))

  import classifier.featureMatrix

  val colors = List(blue, red, green, orange, pink, yellow)

  val maxs = featureMatrix.columnMaxs
  val mins = featureMatrix.columnMins

  val minX = mins.get(0, 0)
  val maxX = maxs.get(0, 0)
  val minY = mins.get(0, 1)
  val maxY = maxs.get(0, 1)

  implicit val ddls = new axle.algebra.DoubleDoubleLengthSpace {}
  val scaledArea = new ScaledArea2D(w, h, border, minX, maxX, minY, maxY)

  val normalFont = new Font(fontName, Font.BOLD, fontSize)

  implicit val doubleTics = implicitly[Tics[Double]]
  val xTics = new XTics(scaledArea, doubleTics.tics(minX, maxX), normalFont, true, 0 *: °, black)
  val yTics = new YTics(scaledArea, doubleTics.tics(minY, maxY), normalFont, black)

  val boundingRectangle = new Rectangle(scaledArea, Point2D(minX, minY), Point2D(maxX, maxY), borderColor = Some(black))

  def centroidOval(i: Int): Oval[Double, Double] = {
    val denormalized = classifier.normalizer.unapply(classifier.μ.row(i))
    val center = Point2D(denormalized(0), denormalized(1))
    Oval(scaledArea, center, 3 * pointDiameter, 3 * pointDiameter, colors(i % colors.length), darkGray)
  }

  def cluster(g2d: Graphics2D, i: Int): Unit = {
    g2d.setColor(colors(i % colors.length))
    (0 until featureMatrix.rows) foreach { r =>
      if (classifier.a.get(r, 0) === i) {
        // TODO figure out what to do when N > 2
        val center = Point2D(featureMatrix.get(r, 0), featureMatrix.get(r, 1))
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
