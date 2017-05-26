package axle.awt

import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D

import javax.swing.JPanel

import monix.reactive._

import axle.visualize.BarChartGrouped
import axle.visualize.BarChartGroupedView
import axle.visualize.element._

case class BarChartGroupedComponent[G, S, Y, D, H](
  chart: BarChartGrouped[G, S, Y, D, H],
  dataUpdatesOpt: Option[Observable[D]] = None)
    extends JPanel {

  import chart._

  setMinimumSize(new Dimension(width, height))

  def initialValue = chart.initialValue

  override def paintComponent(g: Graphics): Unit = {

    val data: D = chart.initialValue // TODO refresh

    val view = BarChartGroupedView(chart, data)

    import view._

    val g2d = g.asInstanceOf[Graphics2D]
    val fontMetrics = g2d.getFontMetrics
    titleText.foreach(Paintable[Text].paint(_, g2d))
    Paintable[HorizontalLine[Double, Y]].paint(hLine, g2d)
    Paintable[VerticalLine[Double, Y]].paint(vLine, g2d)
    xAxisLabelText.foreach(Paintable[Text].paint(_, g2d))
    yAxisLabelText.foreach(Paintable[Text].paint(_, g2d))
    Paintable[XTics[Double, Y]].paint(gTics, g2d)
    Paintable[YTics[Double, Y]].paint(yTics, g2d)
    keyOpt.foreach(Paintable[BarChartGroupedKey[G, S, Y, D, H]].paint(_, g2d))
    bars.foreach(Paintable[Rectangle[Double, Y]].paint(_, g2d))
  }

}
