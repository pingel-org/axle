package axle.awt

import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D

import axle.algebra.LinearAlgebra
import axle.syntax.linearalgebra.matrixOps
import axle.visualize.KMeansVisualization
import axle.visualize.Point2D
import axle.visualize.element.Oval
import axle.visualize.element.Rectangle
import axle.visualize.element.XTics
import axle.visualize.element.YTics
import javax.swing.JPanel
import spire.implicits.DoubleAlgebra
import spire.implicits.eqOps

case class KMeansComponent[D, F[_], M](
  kmv: KMeansVisualization[D, F, M])(
    implicit la: LinearAlgebra[M, Int, Int, Double])
    extends JPanel {

  import kmv._
  import classifier._

  setMinimumSize(new Dimension(w + border, h + border))

  def cluster(g2d: Graphics2D, i: Int): Unit = {
    g2d.setColor(colors(i % colors.length))
    (0 until featureMatrix.rows) foreach { r =>
      if (classifier.a.get(r, 0) === i) {
        // TODO figure out what to do when N > 2
        val center = Point2D(featureMatrix.get(r, 0), featureMatrix.get(r, 1))
        fillOval(g2d, scaledArea, center, pointDiameter, pointDiameter)
        // scaledArea.drawString(g2d, r.toString + "(%.2f,%.2f)".format(center.x, center.y), center)
      }
    }
  }

  override def paintComponent(g: Graphics): Unit = {

    val g2d = g.asInstanceOf[Graphics2D]
    val fontMetrics = g2d.getFontMetrics

    import axle.awt.Paintable // TODO move this !!
    Paintable[Rectangle[Double, Double]].paint(boundingRectangle, g2d)
    Paintable[XTics[Double, Double]].paint(xTics, g2d)
    Paintable[YTics[Double, Double]].paint(yTics, g2d)
    centroidOvals foreach { Paintable[Oval[Double, Double]].paint(_, g2d) }
    (0 until classifier.K) foreach { cluster(g2d, _) }
  }

}