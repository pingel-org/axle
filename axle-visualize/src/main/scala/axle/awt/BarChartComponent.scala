package axle.awt

import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D

import scala.Stream.continually
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.reflect.ClassTag

import akka.pattern.ask
import axle.Show
import axle.actor.Defaults.askTimeout
import axle.algebra.LengthSpace
import axle.algebra.Plottable
import axle.algebra.Tics
import axle.visualize.BarChart
import axle.visualize.BarChartView
import axle.visualize.DataFeedProtocol.Fetch
import axle.visualize.Fed
import axle.visualize.angleDouble
import axle.visualize.element.BarChartKey
import axle.visualize.element.HorizontalLine
import axle.visualize.element.Rectangle
import axle.visualize.element.Text
import axle.visualize.element.VerticalLine
import axle.visualize.element.XTics
import axle.visualize.element.YTics
import javax.swing.JPanel
import spire.algebra.Eq
import spire.algebra.Order

case class BarChartComponent[S: Show, Y: Order: Tics: Eq: Plottable, D: ClassTag](
  chart: BarChart[S, Y, D])(
    implicit yls: LengthSpace[Y, _])
  extends JPanel
  with Fed[D] {

  import chart._

  setMinimumSize(new java.awt.Dimension(width, height))

  def initialValue = chart.initialValue

  val colorStream = continually(colors.toStream).flatten
  val titleFont = new Font(titleFontName, Font.BOLD, titleFontSize)
  val normalFont = new Font(normalFontName, Font.BOLD, normalFontSize)
  val titleText = title.map(Text(_, titleFont, width / 2, titleFontSize))
  val xAxisLabelText = xAxisLabel.map(Text(_, normalFont, width / 2, height - border / 2))
  val yAxisLabelText = yAxisLabel.map(Text(_, normalFont, 20, height / 2, angle = Some(90d *: angleDouble.degree)))

  val keyOpt = if (drawKey) {
    Some(BarChartKey(chart, normalFont, colorStream))
  } else {
    None
  }

  override def paintComponent(g: Graphics): Unit = {

    val data = feeder map { dataFeedActor =>
      val dataFuture = (dataFeedActor ? Fetch()).mapTo[D]
      // Getting rid of this Await is awaiting a better approach to integrating AWT and Akka
      Await.result(dataFuture, 1.seconds)
    } getOrElse (chart.initialValue)

    val view = BarChartView(chart, data, colorStream, normalFont)

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
