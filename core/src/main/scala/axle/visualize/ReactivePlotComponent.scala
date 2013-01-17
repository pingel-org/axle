package axle.visualize

import collection._
import javax.swing.JPanel
import java.awt.Color
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Graphics
import java.awt.Graphics2D
import axle.quanta._
import Angle._

trait Behavior[T] {
  def observe(): T
}

trait Paintable {
  def paint(g2d: Graphics2D): Unit
}

class ReactiveKey[X, Y](
  plot: ReactivePlot[X, Y],
  colorStream: Stream[Color],
  width: Int,
  topPadding: Int,
  dataB: Behavior[Seq[(String, SortedMap[X, Y])]]) extends Paintable {

  def paint(g2d: Graphics2D): Unit = {

    val data = dataB.observe // TODO: this should not happen here

    val fontMetrics = g2d.getFontMetrics

    val lineHeight = g2d.getFontMetrics.getHeight
    for ((((label, f), color), i) <- data.zip(colorStream).zipWithIndex) {
      g2d.setColor(color)
      g2d.drawString(label, plot.width - width, topPadding + lineHeight * (i + 1))
    }
  }

}

class ReactiveVerticalLine[X, Y](
  scaledArea: ScaledArea2D[X, Y],
  v: X,
  color: Color = Color.black) extends Paintable {

  def paint(g2d: Graphics2D): Unit = {
    g2d.setColor(color)
    scaledArea.verticalLine(g2d, v)
  }

}

class ReactiveHorizontalLine[X, Y](
  scaledArea: ScaledArea2D[X, Y],
  h: Y,
  color: Color = Color.black) extends Paintable {

  def paint(g2d: Graphics2D): Unit = {
    g2d.setColor(color)
    scaledArea.horizontalLine(g2d, h)
  }
}

class ReactiveText(
  text: String,
  font: Font,
  x: Int,
  y: Int,
  centered: Boolean = true,
  angle: Option[Angle.Q] = None) extends Paintable {

  def paint(g2d: Graphics2D): Unit = {
    val fontMetrics = g2d.getFontMetrics
    g2d.setFont(font)

    if (angle.isDefined) {
      // TODO: handle centered, angled text
      val twist = angle.get.magnitude.doubleValue
      g2d.translate(x, y)
      g2d.rotate(twist)
      g2d.drawString(text, 0, 0)
      g2d.rotate(-1 * twist)
      g2d.translate(-x, -y)
    } else {
      if (centered)
        g2d.drawString(text, x - fontMetrics.stringWidth(text) / 2, y)
      else
        g2d.drawString(text, x, y)
    }

  }

}

class ReactiveXTics[X, Y](
  scaledArea: ScaledArea2D[X, Y],
  tics: Seq[(X, String)],
  color: Color = Color.black) extends Paintable {

  def paint(g2d: Graphics2D): Unit = {
    val fontMetrics = g2d.getFontMetrics
    scaledArea.drawXTics(g2d, fontMetrics, tics)
  }

}

class ReactiveYTics[X, Y](
  scaledArea: ScaledArea2D[X, Y],
  tics: Seq[(Y, String)],
  color: Color = Color.black) extends Paintable {

  def paint(g2d: Graphics2D): Unit = {
    val fontMetrics = g2d.getFontMetrics
    scaledArea.drawYTics(g2d, fontMetrics, tics)
  }

}

class ReactiveDataLines[X, Y](
  scaledArea: ScaledArea2D[X, Y],
  dataB: Behavior[Seq[(String, SortedMap[X, Y])]],
  colorStream: Stream[Color],
  pointDiameter: Int,
  connect: Boolean = true) extends Paintable {

  def paint(g2d: Graphics2D): Unit = {
    val data = dataB.observe
    for (((label, f), color) <- data.zip(colorStream)) {
      g2d.setColor(color)
      if (connect) {
        val xsStream = f.keysIterator.toStream
        for ((x0, x1) <- xsStream.zip(xsStream.tail)) {
          scaledArea.drawLine(g2d, Point2D(x0, f(x0)), Point2D(x1, f(x1)))
        }
      }
      for (x <- f.keys) {
        scaledArea.fillOval(g2d, Point2D(x, f(x)), pointDiameter, pointDiameter)
      }
    }
  }

}

import Color._

class ReactivePlotComponent[X, Y](
  plot: ReactivePlot[X, Y],
  normalFont: Font = new Font("Courier New", Font.BOLD, 12),
  titleFont: Font = new Font("Palatino", Font.BOLD, 20),
  colors: Seq[Color] = List(blue, red, green, orange, pink, yellow)) extends JPanel {

  setMinimumSize(new java.awt.Dimension(plot.width, plot.height))

  val keyLeftPadding = 20
  val keyTopPadding = 50
  val keyWidth = 80

  val colorStream = Stream.continually(colors.toStream).flatten

  val key = if (plot.drawKey)
    Some(new ReactiveKey(plot, colorStream, keyWidth, keyTopPadding, plot.dataB))
  else
    None

  def boundsAndTics(
    data: Seq[(String, SortedMap[X, Y])]): (Point2D[X, Y], Point2D[X, Y], Seq[(X, String)], Seq[(Y, String)]) = {
    import plot._
    val minX = List(yAxis, data.map(_._2.firstKey).min(xPlottable)).min(xPlottable)
    val maxX = List(yAxis, data.map(_._2.lastKey).max(xPlottable)).max(xPlottable)
    val minY = List(xAxis, data.map(lf => (lf._2.values ++ List(yPlottable.zero())).filter(yPlottable.isPlottable(_)).min(yPlottable)).min(yPlottable)).min(yPlottable)
    val maxY = List(xAxis, data.map(lf => (lf._2.values ++ List(yPlottable.zero())).filter(yPlottable.isPlottable(_)).max(yPlottable)).max(yPlottable)).max(yPlottable)
    val xTics = plot.xPlottable.tics(minX, maxX)
    val yTics = plot.yPlottable.tics(minY, maxY)
    (Point2D(minX, minY), Point2D(maxX, maxY), xTics, yTics)
  }

  val (minPoint, maxPoint, xTics, yTics) = boundsAndTics(plot.dataB.observe) // TODO: this should not happen here

  val scaledArea = new ScaledArea2D(
    width = if (plot.drawKey) plot.width - (keyWidth + keyLeftPadding) else plot.width,
    plot.height, plot.border,
    minPoint.x, maxPoint.x, minPoint.y, maxPoint.y
  )(plot.xPlottable, plot.yPlottable)

  val titleText = plot.title.map(new ReactiveText(_, titleFont, plot.width / 2, 20))
  val xAxisLabel = plot.xAxisLabel.map(new ReactiveText(_, normalFont, plot.width / 2, plot.height - plot.border / 2))
  val yAxisLabel = plot.yAxisLabel.map(new ReactiveText(_, normalFont, 20, plot.height / 2, angle = Some(90 *: °)))
  val vLine = new ReactiveVerticalLine(scaledArea, plot.yAxis)
  val hLine = new ReactiveHorizontalLine(scaledArea, plot.xAxis)
  val xs = new ReactiveXTics(scaledArea, xTics)
  val ys = new ReactiveYTics(scaledArea, yTics)
  val dataLines = new ReactiveDataLines(scaledArea, plot.dataB, colorStream, plot.pointDiameter, plot.connect)

  override def paintComponent(g: Graphics): Unit = {

    val g2d = g.asInstanceOf[Graphics2D]

    vLine.paint(g2d)
    hLine.paint(g2d)
    xs.paint(g2d)
    ys.paint(g2d)
    dataLines.paint(g2d)
    titleText.map(_.paint(g2d))
    xAxisLabel.map(_.paint(g2d))
    yAxisLabel.map(_.paint(g2d))
    key.map(_.paint(g2d))
  }

}
