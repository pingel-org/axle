package axle.awt

import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D

import scala.annotation.tailrec
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

import akka.pattern.ask
import axle.actor.Defaults.askTimeout
import axle.visualize.BarChart
import axle.visualize.BarChartView
import axle.visualize.DataFeedProtocol.Fetch
import axle.visualize.Fed
import axle.visualize.element.BarChartKey
import axle.visualize.element.HorizontalLine
import axle.visualize.element.Rectangle
import axle.visualize.element.Text
import axle.visualize.element.VerticalLine
import axle.visualize.element.XTics
import axle.visualize.element.YTics
import javax.swing.JPanel

case class BarChartComponent[S, Y, D](
  chart: BarChart[S, Y, D])
    extends JPanel
    with Fed[D] {

  import chart._

  setMinimumSize(new Dimension(width, height))

  def initialValue = chart.initialValue

  override def paintComponent(g: Graphics): Unit = {

    val data = feeder map { dataFeedActor =>
      val dataFuture = (dataFeedActor ? Fetch()).mapTo[D]
      // Getting rid of this Await is awaiting a better approach to integrating AWT and Akka
      Await.result(dataFuture, 1.seconds)
    } getOrElse (chart.initialValue)

    val view = BarChartView(chart, data)

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
    keyOpt.foreach(Paintable[BarChartKey[S, Y, D]].paint(_, g2d))
    bars.foreach(Paintable[Rectangle[Double, Y]].paint(_, g2d))

  }

}
