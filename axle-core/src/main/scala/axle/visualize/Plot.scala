package axle.visualize

import cats.Show
import cats.kernel.Eq

import spire.algebra.AdditiveMonoid

import axle.algebra.LengthSpace
import axle.algebra.Tics
import axle.visualize.element.Text

case class Plot[S, X, Y, D](
  dataFn:         Function0[Seq[(S, D)]],
  connect:        Boolean                = true,
  drawKey:        Boolean                = true,
  width:          Int                    = 700,
  height:         Int                    = 600,
  border:         Int                    = 50,
  pointDiameter:  Int                    = 4,
  keyLeftPadding: Int                    = 20,
  keyTopPadding:  Int                    = 50,
  keyWidth:       Int                    = 80,
  fontName:       String                 = "Courier New",
  fontSize:       Int                    = 12,
  bold:           Boolean                = false,
  titleFontName:  String                 = "Palatino",
  titleFontSize:  Int                    = 20,
  colorOf:        S => Color,
  title:          Option[String]         = None,
  keyTitle:       Option[String]         = None,
  xAxis:          Option[Y]              = None,
  xAxisLabel:     Option[String]         = None,
  yAxis:          Option[X]              = None,
  yAxisLabel:     Option[String]         = None)(
  implicit
  val sShow:        Show[S],
  val xts:          Tics[X],
  val xEq:          Eq[X],
  val xLength:      LengthSpace[X, _, Double],
  val yts:          Tics[Y],
  val yEq:          Eq[Y],
  val yLength:      LengthSpace[Y, _, Double],
  val plotDataView: PlotDataView[S, X, Y, D]) {

  val xAxisLabelText = xAxisLabel.map(Text(_, width / 2d, height - border / 2d, fontName, fontSize.toDouble, bold = true))

  val yAxisLabelText = yAxisLabel.map(Text(_, 20, height / 2d, fontName, fontSize.toDouble, bold = true, angle = Some(90d *: angleDouble.degree)))

  val titleText = title.map(Text(_, width / 2d, titleFontSize.toDouble, titleFontName, titleFontSize.toDouble, bold = true))

  def zeroXAxis(implicit yam: AdditiveMonoid[Y]): Plot[S, X, Y, D] =
    this.copy(xAxis = Some(yam.zero))

  def zeroYAxis(implicit xam: AdditiveMonoid[X]): Plot[S, X, Y, D] =
    this.copy(yAxis = Some(xam.zero))

  def zeroAxes(implicit xam: AdditiveMonoid[X], yam: AdditiveMonoid[Y]): Plot[S, X, Y, D] =
    this.zeroXAxis.zeroYAxis

}
