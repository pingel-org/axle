package axle.awt

import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D

import axle.visualize.KMeansVisualization
import axle.visualize.element.Oval
import axle.visualize.element.Rectangle
import axle.visualize.element.XTics
import axle.visualize.element.YTics
import javax.swing.JPanel

case class KMeansComponent[D, F, G, M](
  kmv: KMeansVisualization[D, F, G, M])
    extends JPanel {

  import kmv._

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
