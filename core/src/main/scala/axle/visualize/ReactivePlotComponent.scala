package axle.visualize

import collection._
import javax.swing.JPanel
import java.awt.Color
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Dimension
import axle.quanta._
import Angle._

import Color._

class ReactivePlotComponent[X, Y](
  plot: ReactivePlot[X, Y],
  normalFont: Font = new Font("Courier New", Font.BOLD, 12),
  titleFont: Font = new Font("Palatino", Font.BOLD, 20),
  colors: Seq[Color] = List(blue, red, green, orange, pink, yellow)) extends JPanel {

  setMinimumSize(new Dimension(plot.width, plot.height))

  val keyLeftPadding = 20
  val keyTopPadding = 50
  val keyWidth = 80

  val colorStream = Stream.continually(colors.toStream).flatten

  val key = if (plot.drawKey)
    Some(new Key(plot, colorStream, keyWidth, keyTopPadding, plot.dataB))
  else
    None

  //  def boundsAndTics(
  //    dataBehavior: Behavior[Unit, Seq[(String, SortedMap[X, Y])]]): (Point2D[X, Y], Point2D[X, Y], Seq[(X, String)], Seq[(Y, String)]) = {
  //    import plot._
  //    val data = dataBehavior.observe()
  //    val minX = List(yAxis, data.map(_._2.firstKey).min(xPlottable)).min(xPlottable)
  //    val maxX = List(yAxis, data.map(_._2.lastKey).max(xPlottable)).max(xPlottable)
  //    val minY = List(xAxis, data.map(lf => (lf._2.values ++ List(yPlottable.zero())).filter(yPlottable.isPlottable(_)).min(yPlottable)).min(yPlottable)).min(yPlottable)
  //    val maxY = List(xAxis, data.map(lf => (lf._2.values ++ List(yPlottable.zero())).filter(yPlottable.isPlottable(_)).max(yPlottable)).max(yPlottable)).max(yPlottable)
  //    val xTics = plot.xPlottable.tics(minX, maxX)
  //    val yTics = plot.yPlottable.tics(minY, maxY)
  //    (Point2D(minX, minY), Point2D(maxX, maxY), xTics, yTics)
  //  }

  // val (minPoint, maxPoint, xTics, yTics) = boundsAndTics(plot.dataB)

  val batBehavior = new Behavior[Seq[(String, SortedMap[X, Y])], (Point2D[X, Y], Point2D[X, Y], Seq[(X, String)], Seq[(Y, String)])] {
    import plot._
    def observe(input: Seq[(String, SortedMap[X, Y])]) = {
      val minX = List(yAxis, input.map(_._2.firstKey).min(xPlottable)).min(xPlottable)
      val maxX = List(yAxis, input.map(_._2.lastKey).max(xPlottable)).max(xPlottable)
      val minY = List(xAxis, input.map(lf => (lf._2.values ++ List(yPlottable.zero())).filter(yPlottable.isPlottable(_)).min(yPlottable)).min(yPlottable)).min(yPlottable)
      val maxY = List(xAxis, input.map(lf => (lf._2.values ++ List(yPlottable.zero())).filter(yPlottable.isPlottable(_)).max(yPlottable)).max(yPlottable)).max(yPlottable)
      val xTics = plot.xPlottable.tics(minX, maxX)
      val yTics = plot.yPlottable.tics(minY, maxY)
      (Point2D(minX, minY), Point2D(maxX, maxY), xTics, yTics)
    }
  }

  //  val scaledArea = new ScaledArea2D(
  //    width = if (plot.drawKey) plot.width - (keyWidth + keyLeftPadding) else plot.width,
  //    plot.height, plot.border,
  //    minPoint.x, maxPoint.x, minPoint.y, maxPoint.y
  //  )(plot.xPlottable, plot.yPlottable)

  val scaledAreaBehavior = new Behavior[(Point2D[X, Y], Point2D[X, Y], Seq[(X, String)], Seq[(Y, String)]), ScaledArea2D[X, Y]] {

    def observe(input: (Point2D[X, Y], Point2D[X, Y], Seq[(X, String)], Seq[(Y, String)])) =
      new ScaledArea2D(
        width = if (plot.drawKey) plot.width - (keyWidth + keyLeftPadding) else plot.width,
        plot.height, plot.border,
        input._1.x, input._2.x, input._1.y, input._2.y // minPoint.x, maxPoint.x, minPoint.y, maxPoint.y
      )(plot.xPlottable, plot.yPlottable)

  }

  val titleText = plot.title.map(new Text(_, titleFont, plot.width / 2, 20))
  val xAxisLabel = plot.xAxisLabel.map(new Text(_, normalFont, plot.width / 2, plot.height - plot.border / 2))
  val yAxisLabel = plot.yAxisLabel.map(new Text(_, normalFont, 20, plot.height / 2, angle = Some(90 *: °)))

  val vLineBehavior = new Behavior[ScaledArea2D[X, Y], VerticalLine[X, Y]] {
    def observe(input: ScaledArea2D[X, Y]) = new VerticalLine(input, plot.yAxis, black)
  }

  val hLineBehavior = new Behavior[ScaledArea2D[X, Y], HorizontalLine[X, Y]] {
    def observe(input: ScaledArea2D[X, Y]) = new HorizontalLine(input, plot.xAxis, black)
  }

  val xTicsBehavior = new Behavior[(ScaledArea2D[X, Y], (Point2D[X, Y], Point2D[X, Y], Seq[(X, String)], Seq[(Y, String)])), XTics[X, Y]] {
    def observe(input: (ScaledArea2D[X, Y], (Point2D[X, Y], Point2D[X, Y], Seq[(X, String)], Seq[(Y, String)]))) =
      new XTics(input._1, input._2._3, black)
  }

  val yTicsBehavior = new Behavior[(ScaledArea2D[X, Y], (Point2D[X, Y], Point2D[X, Y], Seq[(X, String)], Seq[(Y, String)])), YTics[X, Y]] {
    def observe(input: (ScaledArea2D[X, Y], (Point2D[X, Y], Point2D[X, Y], Seq[(X, String)], Seq[(Y, String)]))) =
      new YTics(input._1, input._2._4, black)
  }

  val dataLinesBehavior = new Behavior[(ScaledArea2D[X, Y], Seq[(String, SortedMap[X, Y])]), DataLines[X, Y]] {
    // TODO decide whether to get dataB from input or scope
    def observe(input: (ScaledArea2D[X, Y], Seq[(String, SortedMap[X, Y])])) = new DataLines(input._1, plot.dataB, colorStream, plot.pointDiameter, plot.connect)
  }

  override def paintComponent(g: Graphics): Unit = {

    val g2d = g.asInstanceOf[Graphics2D]

    val data = plot.dataB.observe()
    val bat = batBehavior.observe(data)
    val scaledArea = scaledAreaBehavior.observe(bat)
    vLineBehavior.observe(scaledArea).paint(g2d)
    hLineBehavior.observe(scaledArea).paint(g2d)
    xTicsBehavior.observe((scaledArea, bat)).paint(g2d)
    yTicsBehavior.observe((scaledArea, bat)).paint(g2d)
    dataLinesBehavior.observe((scaledArea, data)).paint(g2d)
    titleText.map(_.paint(g2d))
    xAxisLabel.map(_.paint(g2d))
    yAxisLabel.map(_.paint(g2d))
    key.map(_.paint(g2d))
  }

}
