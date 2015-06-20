package axle.awt

import java.awt.Dimension
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

import akka.pattern.ask
import axle.actor.Defaults.askTimeout
import axle.algebra.LengthSpace
import axle.algebra.Tics
import axle.algebra.Zero
import axle.visualize.DataFeedProtocol.Fetch
import axle.visualize.Fed
import axle.visualize.Plot
import axle.visualize.PlotView
import axle.visualize.angleDouble
import axle.visualize.element.DataLines
import axle.visualize.element.HorizontalLine
import axle.visualize.element.Key
import axle.visualize.element.Text
import axle.visualize.element.VerticalLine
import axle.visualize.element.XTics
import axle.visualize.element.YTics
import javax.swing.JPanel
import spire.algebra.Eq

case class PlotComponent[X: Zero: Tics: Eq, Y: Zero: Tics: Eq, D](
  plot: Plot[X, Y, D])(
    implicit xls: LengthSpace[X, _], yls: LengthSpace[Y, _])
    extends JPanel
    with Fed[List[(String, D)]] {

  def initialValue = plot.initialValue

  import plot._

  setMinimumSize(new Dimension(width, height))

  val normalFont = new Font(fontName, Font.BOLD, fontSize)
  val xAxisLabelText = xAxisLabel.map(Text(_, normalFont, width / 2, height - border / 2))
  val yAxisLabelText = yAxisLabel.map(Text(_, normalFont, 20, height / 2, angle = Some(90d *: angleDouble.degree)))
  val titleFont = new Font(titleFontName, Font.BOLD, titleFontSize)
  val titleText = title.map(Text(_, titleFont, width / 2, titleFontSize))

  override def paintComponent(g: Graphics): Unit = {

    val data = feeder map { dataFeedActor =>
      val dataFuture = (dataFeedActor ? Fetch()).mapTo[List[(String, D)]]
      // Getting rid of this Await is awaiting a better approach to integrating AWT and Akka
      Await.result(dataFuture, 1.seconds)
    } getOrElse (plot.initialValue)

    val g2d = g.asInstanceOf[Graphics2D]

    val view = PlotView(plot, data, normalFont)
    import view._

    Paintable[HorizontalLine[X, Y]].paint(hLine, g2d)
    Paintable[VerticalLine[X, Y]].paint(vLine, g2d)
    Paintable[XTics[X, Y]].paint(xTics, g2d)
    Paintable[YTics[X, Y]].paint(yTics, g2d)
    Paintable[DataLines[X, Y, D]].paint(dataLines, g2d)

    titleText.foreach(Paintable[Text].paint(_, g2d))
    xAxisLabelText.foreach(Paintable[Text].paint(_, g2d))
    yAxisLabelText.foreach(Paintable[Text].paint(_, g2d))
    view.keyOpt.foreach(Paintable[Key[X, Y, D]].paint(_, g2d))

  }

}