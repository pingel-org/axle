package axle.awt

import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D

import javax.swing.JPanel

import axle.visualize.Plot
import axle.visualize.PlotView
import axle.visualize.element._

case class PlotComponent[S, X, Y, D](plot: Plot[S, X, Y, D])
    extends JPanel {

  var drawnData: Option[Seq[(S, D)]] = None

  import plot._

  setMinimumSize(new Dimension(width, height))

  override def paintComponent(g: Graphics): Unit = {

    Option(dataFn.apply) foreach { data =>

      val g2d = g.asInstanceOf[Graphics2D]

      val view = PlotView(plot, data)
      import view._

      Paintable[HorizontalLine[X, Y]].paint(hLine, g2d)
      Paintable[VerticalLine[X, Y]].paint(vLine, g2d)
      Paintable[XTics[X, Y]].paint(xTics, g2d)
      Paintable[YTics[X, Y]].paint(yTics, g2d)
      Paintable[DataLines[S, X, Y, D]].paint(dataLines, g2d)

      titleText.foreach(Paintable[Text].paint(_, g2d))
      xAxisLabelText.foreach(Paintable[Text].paint(_, g2d))
      yAxisLabelText.foreach(Paintable[Text].paint(_, g2d))
      view.keyOpt.foreach(Paintable[Key[S, X, Y, D]].paint(_, g2d))
    }
  }

}
