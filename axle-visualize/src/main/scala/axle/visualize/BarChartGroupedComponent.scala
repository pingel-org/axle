package axle.visualize

import javax.swing.JPanel
import java.awt.Color
import Color._
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Graphics
import java.awt.Graphics2D
import scala.concurrent.Await
import scala.concurrent.duration._
import DataFeedProtocol._
import akka.pattern.ask
import akka.actor.ActorRef
import axle.actor.Defaults._
import Stream.continually
import axle.quanta._
import Angle._
import axle.algebra.Plottable
import Plottable._
import axle.visualize.element._

class BarChartGroupedView[G, S, Y: Plottable](chart: BarChartGrouped[G, S, Y], data: Map[(G, S), Y], colorStream: Stream[Color], normalFont: Font) {

  import chart._

  val minX = 0.0
  val maxX = 1.0
  val yAxis = minX

  val padding = 0.05 // on each side
  val widthPerGroup = (1.0 - (2 * padding)) / groups.size
  val whiteSpace = widthPerGroup * (1.0 - barWidthPercent)

  val yPlottable = implicitly[Plottable[Y]]

  val minY = List(xAxis, slices.map(s => (groups.map(data(_, s)) ++ List(yPlottable.zero)).filter(yPlottable.isPlottable).min).min).min
  val maxY = List(xAxis, slices.map(s => (groups.map(data(_, s)) ++ List(yPlottable.zero)).filter(yPlottable.isPlottable).max).max).max

  val scaledArea = new ScaledArea2D(
    width = if (drawKey) width - (keyWidth + keyLeftPadding) else width,
    height,
    border,
    minX, maxX, minY, maxY
  )

  val vLine = new VerticalLine(scaledArea, yAxis, black)
  val hLine = new HorizontalLine(scaledArea, xAxis, black)

  val gTics = new XTics(
    scaledArea,
    groups.zipWithIndex.map({ case (g, i) => (padding + (i + 0.5) * widthPerGroup, gLabeller(g)) }).toList,
    normalFont,
    false,
    36 *: °,
    black)

  val yTics = new YTics(scaledArea, yPlottable.tics(minY, maxY), normalFont, black)

  val barSliceWidth = (widthPerGroup - (whiteSpace / 2d)) / slices.size.toDouble

  val bars = for {
    ((s, j), color) <- slices.zipWithIndex.zip(colorStream)
    (g, i) <- groups.zipWithIndex
  } yield {
    val leftX = padding + (whiteSpace / 2d) + i * widthPerGroup + j * barSliceWidth
    val rightX = leftX + barSliceWidth
    Rectangle(scaledArea, Point2D(leftX, minY), Point2D(rightX, data(g, s)), fillColor = Some(color))
  }

}

class BarChartGroupedComponent[G, S, Y: Plottable](chart: BarChartGrouped[G, S, Y]) extends JPanel with Fed {

  import chart._

  setMinimumSize(new java.awt.Dimension(width, height))

  def feeder: ActorRef = dataFeedActor

  val colors = List(blue, red, green, orange, pink, yellow)
  val colorStream = continually(colors.toStream).flatten
  val titleFont = new Font(titleFontName, Font.BOLD, titleFontSize)
  val normalFont = new Font(normalFontName, Font.BOLD, normalFontSize)
  val titleText = title.map(new Text(_, titleFont, width / 2, titleFontSize))
  val xAxisLabelText = xAxisLabel.map(new Text(_, normalFont, width / 2, height - border / 2))
  val yAxisLabelText = yAxisLabel.map(new Text(_, normalFont, 20, height / 2, angle = Some(90 *: °)))

  val keyOpt = if (drawKey) {
    Some(new BarChartGroupedKey(chart, normalFont, colorStream))
  } else {
    None
  }

  override def paintComponent(g: Graphics): Unit = {

    val g2d = g.asInstanceOf[Graphics2D]
    val fontMetrics = g2d.getFontMetrics

    val dataFuture = (dataFeedActor ? Fetch()).mapTo[Map[(G, S), Y]]

    // Getting rid of this Await is awaiting a better approach to integrating AWT and Akka
    val data = Await.result(dataFuture, 1.seconds)

    val view = new BarChartGroupedView(chart, data, colorStream, normalFont)

    import view._

    titleText.map(_.paint(g2d))
    hLine.paint(g2d)
    vLine.paint(g2d)
    xAxisLabelText.map(_.paint(g2d))
    yAxisLabelText.map(_.paint(g2d))
    gTics.paint(g2d)
    yTics.paint(g2d)
    keyOpt.map(_.paint(g2d))
    bars.map(_.paint(g2d))

  }

}
