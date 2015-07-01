package axle.visualize

import axle.algebra.LengthSpace
import axle.algebra.Tics
import axle.algebra.Zero
import axle.visualize.element.Text

import scala.Stream.continually
import spire.algebra.Eq

case class Plot[X, Y, D](
    initialValue: Seq[(String, D)],
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
    bold: Boolean = false,
    titleFontName: String = "Palatino",
    titleFontSize: Int = 20,
    colors: Seq[Color] = defaultColors,
    title: Option[String] = None,
    keyTitle: Option[String] = None,
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

  val xAxisLabelText = xAxisLabel.map(Text(_, width / 2, height - border / 2, fontName, fontSize, bold=true))

  val yAxisLabelText = yAxisLabel.map(Text(_,  20, height / 2, fontName, fontSize, bold=true, angle = Some(90d *: angleDouble.degree)))

  val titleText = title.map(Text(_, width / 2, titleFontSize, titleFontName, titleFontSize, bold=true))

}
