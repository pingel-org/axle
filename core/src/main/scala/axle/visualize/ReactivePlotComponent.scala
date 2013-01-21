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

class ReactivePlotComponent[X, Y](plot: ReactivePlot[X, Y]) extends JPanel {

  setMinimumSize(new Dimension(plot.width, plot.height))

  val colorStream = Stream.continually(plot.colors.toStream).flatten

  val keyBehavior = new Behavior[Seq[(String, SortedMap[X, Y])], Key[X, Y]] {

    def observe(input: Seq[(String, SortedMap[X, Y])]) =
      new Key(plot, colorStream, plot.keyWidth, plot.keyTopPadding, input)
  }

  val keyBehaviorOpt = if (plot.drawKey)
    Some(keyBehavior)
  else
    None

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

  val scaledAreaBehavior = new Behavior[(Point2D[X, Y], Point2D[X, Y], Seq[(X, String)], Seq[(Y, String)]), ScaledArea2D[X, Y]] {

    def observe(input: (Point2D[X, Y], Point2D[X, Y], Seq[(X, String)], Seq[(Y, String)])) =
      new ScaledArea2D(
        width = if (plot.drawKey) plot.width - (plot.keyWidth + plot.keyLeftPadding) else plot.width,
        plot.height, plot.border,
        input._1.x, input._2.x, input._1.y, input._2.y // minPoint.x, maxPoint.x, minPoint.y, maxPoint.y
      )(plot.xPlottable, plot.yPlottable)

  }

  val normalFont = new Font(plot.fontName, Font.BOLD, plot.fontSize)
  val xAxisLabel = plot.xAxisLabel.map(new Text(_, normalFont, plot.width / 2, plot.height - plot.border / 2))
  val yAxisLabel = plot.yAxisLabel.map(new Text(_, normalFont, 20, plot.height / 2, angle = Some(90 *: °)))
  
  val titleFont = new Font(plot.titleFontName, Font.BOLD, plot.titleFontSize)
  val titleText = plot.title.map(new Text(_, titleFont, plot.width / 2, 20))

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

    def observe(input: (ScaledArea2D[X, Y], Seq[(String, SortedMap[X, Y])])) =
      new DataLines(input._1, input._2, colorStream, plot.pointDiameter, plot.connect)
  }

  override def paintComponent(g: Graphics): Unit = {

    val g2d = g.asInstanceOf[Graphics2D]

    val data = plot.dataB.observe()
    val bat = batBehavior.observe(data)
    val scaledArea = scaledAreaBehavior.observe(bat)

    val paintables = Vector(
      vLineBehavior.observe(scaledArea),
      hLineBehavior.observe(scaledArea),
      xTicsBehavior.observe((scaledArea, bat)),
      yTicsBehavior.observe((scaledArea, bat)),
      dataLinesBehavior.observe((scaledArea, data))
    ) ++ Vector(
        titleText,
        xAxisLabel,
        yAxisLabel,
        keyBehaviorOpt.map(_.observe(data))
      ).flatMap(i => i)

    for (paintable <- paintables) {
      paintable.paint(g2d)
    }

  }

}
