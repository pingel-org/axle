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
  kmv: KMeansVisualization[D, F, M])
    extends JPanel {

  import kmv._
  import classifier._

  setMinimumSize(new Dimension(width + border, height + border))

  override def paintComponent(g: Graphics): Unit = {

    val g2d = g.asInstanceOf[Graphics2D]
    val fontMetrics = g2d.getFontMetrics

    Paintable[Rectangle[Double, Double]].paint(boundingRectangle, g2d)
    Paintable[XTics[Double, Double]].paint(xTics, g2d)
    Paintable[YTics[Double, Double]].paint(yTics, g2d)
    centroidOvals foreach { Paintable[Oval[Double, Double]].paint(_, g2d) }
    points foreach { Paintable[Oval[Double, Double]].paint(_, g2d)}
  }

}