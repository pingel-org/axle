package axle.awt

import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

import akka.pattern.ask
import axle.actor.Defaults.askTimeout
import axle.visualize.DataFeedProtocol.Fetch
import axle.visualize.Fed
import axle.visualize.Plot
import axle.visualize.PlotView
import axle.visualize.element.DataLines
import axle.visualize.element.HorizontalLine
import axle.visualize.element.Key
import axle.visualize.element.Text
import axle.visualize.element.VerticalLine
import axle.visualize.element.XTics
import axle.visualize.element.YTics
import javax.swing.JPanel

case class PlotComponent[S, X, Y, D](
  plot: Plot[S, X, Y, D])
    extends JPanel
    with Fed[Seq[(S, D)]] {

  def initialValue = plot.initialValue

  import plot._

  setMinimumSize(new Dimension(width, height))

  override def paintComponent(g: Graphics): Unit = {

    val data = feeder map { dataFeedActor =>
      val dataFuture = (dataFeedActor ? Fetch()).mapTo[List[(S, D)]]
      // Getting rid of this Await is awaiting a better approach to integrating AWT and Akka
      Await.result(dataFuture, 1.seconds)
    } getOrElse (plot.initialValue)

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
