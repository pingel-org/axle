package axle.visualize

import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D

import scala.Stream.continually
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.reflect.ClassTag

import DataFeedProtocol.Fetch
import akka.pattern.ask
import axle.actor.Defaults.askTimeout
import axle.algebra.Plottable
import axle.quanta.Angle.{° => °}
import axle.visualize.element.BarChartKey
import axle.visualize.element.Text
import javax.swing.JPanel
import spire.algebra.Eq
import spire.math.Number.apply

class BarChartComponent[S, Y: Plottable: Eq, D: ClassTag](chart: BarChart[S, Y, D])
  extends JPanel
  with Fed[D] {

  import DataFeedProtocol._
  import chart._

  setMinimumSize(new java.awt.Dimension(width, height))

  def initialValue = chart.initialValue
  
  val colorStream = continually(colors.toStream).flatten
  val titleFont = new Font(titleFontName, Font.BOLD, titleFontSize)
  val normalFont = new Font(normalFontName, Font.BOLD, normalFontSize)
  val titleText = title.map(new Text(_, titleFont, width / 2, titleFontSize))
  val xAxisLabelText = xAxisLabel.map(new Text(_, normalFont, width / 2, height - border / 2))
  val yAxisLabelText = yAxisLabel.map(new Text(_, normalFont, 20, height / 2, angle = Some(90 *: °)))

  val keyOpt = if (drawKey) {
    Some(new BarChartKey(chart, normalFont, colorStream))
  } else {
    None
  }

  override def paintComponent(g: Graphics): Unit = {
    
    val data = feeder map { dataFeedActor =>
      val dataFuture = (dataFeedActor ? Fetch()).mapTo[D]
      // Getting rid of this Await is awaiting a better approach to integrating AWT and Akka
      Await.result(dataFuture, 1.seconds)
    } getOrElse (chart.initialValue)

    val view = new BarChartView(chart, data, colorStream, normalFont)

    import view._

    val g2d = g.asInstanceOf[Graphics2D]
    val fontMetrics = g2d.getFontMetrics
    titleText.foreach(_.paint(g2d))
    hLine.paint(g2d)
    vLine.paint(g2d)
    xAxisLabelText.foreach(_.paint(g2d))
    yAxisLabelText.foreach(_.paint(g2d))
    gTics.paint(g2d)
    yTics.paint(g2d)
    keyOpt.foreach(_.paint(g2d))
    bars.foreach(_.paint(g2d))

  }

}
