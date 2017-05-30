package axle.awt

import javax.swing.JPanel
import java.awt._
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D

import axle.visualize._
import axle.visualize.element._

case class ScatterPlotComponent[S, X, Y, D](plot: ScatterPlot[S, X, Y, D])
    extends JPanel {

  import plot._

  setMinimumSize(new Dimension(width.toInt, height.toInt))

  override def paintComponent(g: Graphics): Unit = {

    Option(dataFn.apply()) foreach { data =>

      val g2d = g.asInstanceOf[Graphics2D]

      Paintable[HorizontalLine[X, Y]].paint(hLine, g2d)
      Paintable[VerticalLine[X, Y]].paint(vLine, g2d)
      Paintable[XTics[X, Y]].paint(xTics, g2d)
      Paintable[YTics[X, Y]].paint(yTics, g2d)
      Paintable[DataPoints[S, X, Y, D]].paint(dataPoints, g2d)

      titleText.foreach(Paintable[Text].paint(_, g2d))
      xAxisLabelText.foreach(Paintable[Text].paint(_, g2d))
      yAxisLabelText.foreach(Paintable[Text].paint(_, g2d))
    }
  }

}
