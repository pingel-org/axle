package axle.awt

import javax.swing.JPanel
import java.awt._
import axle.visualize._
import axle.visualize.element._
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D

import scala.reflect.ClassTag
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

import akka.pattern.ask
import axle.actor.Defaults.askTimeout
import axle.visualize.DataFeedProtocol.Fetch

case class ScatterPlotComponent[S, X, Y, D: ClassTag](
  plot: ScatterPlot[S, X, Y, D])
    extends JPanel
    with Fed[D] {

  def initialValue = plot.data

  import plot._

  setMinimumSize(new Dimension(width.toInt, height.toInt))

  override def paintComponent(g: Graphics): Unit = {

    val data = feeder map { dataFeedActor =>
      val dataFuture = (dataFeedActor ? Fetch()).mapTo[D]
      // Getting rid of this Await is awaiting a better approach to integrating AWT and Akka
      Await.result(dataFuture, 1.seconds)
    } getOrElse (plot.data)

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
