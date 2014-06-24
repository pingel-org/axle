package axle.visualize

import java.awt.Color
import java.awt.Color.black
import java.awt.Color.blue
import java.awt.Color.green
import java.awt.Color.orange
import java.awt.Color.pink
import java.awt.Color.red
import java.awt.Color.yellow
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D

import scala.Stream.continually
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.ask
import axle.actor.Defaults.askTimeout
import axle.algebra.Plottable
import axle.algebra.Plottable.DoublePlottable
import axle.quanta.Angle._
import axle.visualize.element.BarChartGroupedKey
import axle.visualize.element.HorizontalLine
import axle.visualize.element.Rectangle
import axle.visualize.element.Text
import axle.visualize.element.VerticalLine
import axle.visualize.element.XTics
import axle.visualize.element.YTics
import javax.swing.JPanel
import spire.algebra.Eq
import spire.implicits.DoubleAlgebra
import spire.math.Number.apply

class BarChartGroupedView[G, S, Y: Plottable: Eq](chart: BarChartGrouped[G, S, Y], data: Map[(G, S), Y], colorStream: Stream[Color], normalFont: Font) {

  import chart._

  val minX = 0d
  val maxX = 1d
  val yAxis = minX

  val padding = 0.05 // on each side
  val widthPerGroup = (1d - (2 * padding)) / groups.size
  val whiteSpace = widthPerGroup * (1d - barWidthPercent)

  val yPlottable = implicitly[Plottable[Y]]

  val minY = List(xAxis, slices.map(s => (groups.map(data(_, s)) ++ List(yPlottable.zero)).filter(yPlottable.isPlottable).min).min).min
  val maxY = List(xAxis, slices.map(s => (groups.map(data(_, s)) ++ List(yPlottable.zero)).filter(yPlottable.isPlottable).max).max).max

  val scaledArea = new ScaledArea2D(
    width = if (drawKey) width - (keyWidth + keyLeftPadding) else width,
    height,
    border,
    minX, maxX, minY, maxY)

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

class BarChartGroupedComponent[G, S, Y: Plottable: Eq](chart: BarChartGrouped[G, S, Y])(implicit systemOpt: Option[ActorSystem])
  extends JPanel
  with Fed {

  import DataFeedProtocol._
  import chart._

  setMinimumSize(new java.awt.Dimension(width, height))

  val dataFeedActorOpt: Option[ActorRef] = chart.refresher.flatMap {
    case (fn, interval) => {
      println(s"creating dataFeedActor for BarChartComponent")
      systemOpt map { system =>
        system.actorOf(Props(new DataFeedActor(initialValue, fn, interval)))
      }
    }
  }

  def feeder: Option[ActorRef] = dataFeedActorOpt

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

    val data = feeder map { dataFeedActor =>
      val dataFuture = (dataFeedActor ? Fetch()).mapTo[Map[(G, S), Y]]
      // Getting rid of this Await is awaiting a better approach to integrating AWT and Akka
      Await.result(dataFuture, 1.seconds)
    } getOrElse (chart.initialValue)

    val view = new BarChartGroupedView(chart, data, colorStream, normalFont)

    import view._

    val g2d = g.asInstanceOf[Graphics2D]
    val fontMetrics = g2d.getFontMetrics
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
