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
import DataFeed._
import akka.pattern.ask
import axle.akka.Defaults._
import Stream.continually
import axle.quanta._
import Angle._
import Plottable._

class BarChartView[X, S, Y: Plottable](chart: BarChart[X, S, Y], data: Map[(X, S), Y], colorStream: Stream[Color]) {

  import chart._

  val minX = 0.0
  val maxX = 1.0
  val yAxis = minX

  val padding = 0.05 // on each side
  val widthPerX = (1.0 - (2 * padding)) / xs.size
  val whiteSpace = widthPerX * (1.0 - barWidthPercent)

  val yPlottable = implicitly[Plottable[Y]]

  val minY = List(xAxis, ss.map(s => (xs.map(y(_, s)) ++ List(yPlottable.zero())).filter(yPlottable.isPlottable(_)).min(yPlottable)).min(yPlottable)).min(yPlottable)
  val maxY = List(xAxis, ss.map(s => (xs.map(y(_, s)) ++ List(yPlottable.zero())).filter(yPlottable.isPlottable(_)).max(yPlottable)).max(yPlottable)).max(yPlottable)

  val scaledArea = new ScaledArea2D(
    width = if (drawKey) width - (keyWidth + keyLeftPadding) else width,
    height,
    border,
    minX, maxX, minY, maxY
  )

  val vLine = new VerticalLine(scaledArea, yAxis, black)
  val hLine = new HorizontalLine(scaledArea, xAxis, black)

  val xTics = new XTics(
    scaledArea,
    xs.zipWithIndex.map({ case (x, i) => (padding + (i + 0.5) * widthPerX, xLabeller(x)) }).toList,
    false,
    36 *: °,
    black)

  val yTics = new YTics(scaledArea, yPlottable.tics(minY, maxY), black)

  val barSliceWidth = (widthPerX - (whiteSpace / 2d)) / ss.size.toDouble

  val bars = for {
    ((s, j), color) <- ss.zipWithIndex.zip(colorStream)
    (x, i) <- xs.zipWithIndex
  } yield {
    val leftX = padding + (whiteSpace / 2d) + i * widthPerX + j * barSliceWidth
    val rightX = leftX + barSliceWidth
    Rectangle(scaledArea, Point2D(leftX, minY), Point2D(rightX, y(x, s)), color)
  }

}

class BarChartComponent[X, S, Y: Plottable](chart: BarChart[X, S, Y]) extends JPanel {

  import chart._

  setMinimumSize(new java.awt.Dimension(width, height))

  val colors = List(blue, red, green, orange, pink, yellow)
  val colorStream = continually(colors.toStream).flatten
  val titleFont = new Font(titleFontName, Font.BOLD, titleFontSize)
  val normalFont = new Font(normalFontName, Font.BOLD, normalFontSize)
  val titleText = title.map(new Text(_, titleFont, width / 2, titleFontSize))
  val xAxisLabelText = xAxisLabel.map(new Text(_, normalFont, width / 2, height - border / 2))
  val yAxisLabelText = yAxisLabel.map(new Text(_, normalFont, 20, height / 2, angle = Some(90 *: °)))

  val keyOpt = if (drawKey)
    Some(new BarChartKey(chart, colorStream))
  else
    None

  var timestamp = 0L

  override def paintComponent(g: Graphics): Unit = {

    val g2d = g.asInstanceOf[Graphics2D]
    val fontMetrics = g2d.getFontMetrics

    val dataOptFuture = (dataFeedActor ? Fetch(timestamp)).mapTo[Option[Map[(X, S), Y]]]

    Await.result(dataOptFuture, 1.seconds).map(data => {

      timestamp = System.currentTimeMillis

      val view = new BarChartView(chart, data, colorStream)

      import view._

      titleText.map(_.paint(g2d))
      hLine.paint(g2d)
      vLine.paint(g2d)
      xAxisLabelText.map(_.paint(g2d))
      yAxisLabelText.map(_.paint(g2d))
      xTics.paint(g2d)
      yTics.paint(g2d)
      keyOpt.map(_.paint(g2d))
      bars.map(_.paint(g2d))
    })

  }

}
