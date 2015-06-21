package axle.visualize

import java.awt.Color
import java.awt.Color.blue
import java.awt.Color.green
import java.awt.Color.orange
import java.awt.Color.pink
import java.awt.Color.red
import java.awt.Color.yellow
import java.awt.Font

import axle.algebra.LengthSpace
import axle.algebra.Tics
import axle.algebra.Zero
import axle.visualize.element.Text

import scala.Stream.continually
import spire.algebra.Eq

case class Plot[X, Y, D](
    initialValue: List[(String, D)],
    connect: Boolean = true,
    drawKey: Boolean = true,
    width: Int = 700,
    height: Int = 600,
    border: Int = 50,
    pointDiameter: Int = 4,
    keyLeftPadding: Int = 20,
    keyTopPadding: Int = 50,
    keyWidth: Int = 80,
    fontName: String = "Courier New",
    fontSize: Int = 12,
    titleFontName: String = "Palatino",
    titleFontSize: Int = 20,
    colors: Seq[Color] = List(blue, red, green, orange, pink, yellow),
    title: Option[String] = None,
    xAxis: Option[Y] = None,
    xAxisLabel: Option[String] = None,
    yAxis: Option[X] = None,
    yAxisLabel: Option[String] = None)(
        implicit val xZero: Zero[X],
        val xts: Tics[X],
        val xEq: Eq[X],
        val xLength: LengthSpace[X, _],
        val yZero: Zero[Y],
        val yts: Tics[Y],
        val yEq: Eq[Y],
        val yLength: LengthSpace[Y, _],
        val plotDataView: PlotDataView[X, Y, D]) {

  val colorStream = continually(colors.toStream).flatten

  val normalFont = new Font(fontName, Font.BOLD, fontSize)

  val xAxisLabelText = xAxisLabel.map(Text(_, normalFont, width / 2, height - border / 2))
  val yAxisLabelText = yAxisLabel.map(Text(_, normalFont, 20, height / 2, angle = Some(90d *: angleDouble.degree)))

  val titleFont = new Font(titleFontName, Font.BOLD, titleFontSize)
  val titleText = title.map(Text(_, titleFont, width / 2, titleFontSize))

}
