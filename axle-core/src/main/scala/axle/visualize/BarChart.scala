package axle.visualize

import cats.Show
import axle.algebra.LengthSpace
import axle.algebra.Plottable
import axle.algebra.Tics
import spire.algebra.AdditiveMonoid
import axle.quanta.Angle
import axle.quanta.UnittedQuantity
import axle.visualize.element.BarChartKey
import axle.visualize.element.Text
import cats.kernel.Eq
import cats.kernel.Order

case class BarChart[C, Y, D, H](
  dataFn:          Function0[D],
  drawKey:         Boolean                                = true,
  width:           Int                                    = 700,
  height:          Int                                    = 600,
  border:          Int                                    = 50,
  barWidthPercent: Double                                 = 0.80,
  keyLeftPadding:  Int                                    = 20,
  keyTopPadding:   Int                                    = 50,
  keyWidth:        Int                                    = 80,
  title:           Option[String]                         = None,
  keyTitle:        Option[String]                         = None,
  normalFontName:  String                                 = "Courier New",
  normalFontSize:  Int                                    = 12,
  titleFontName:   String                                 = "Palatino",
  titleFontSize:   Int                                    = 20,
  xAxis:           Option[Y]                              = None,
  xAxisLabel:      Option[String]                         = None,
  yAxisLabel:      Option[String]                         = None,
  labelAngle:      Option[UnittedQuantity[Angle, Double]] = Some(36d *: angleDouble.degree),
  colorOf:         C => Color                             = (c: C) => Color.blue,
  hoverOf:         C => Option[H]                         = (c: C) => None,
  linkOf:          C => Option[(java.net.URL, Color)]     = (c: C) => None)(
  implicit
  val showC:           Show[C],
  val showH:           Show[H],
  val additiveMonoidY: AdditiveMonoid[Y],
  val orderC:          Order[C],
  val orderY:          Order[Y],
  val ticsY:           Tics[Y],
  val eqY:             Eq[Y],
  val plottableY:      Plottable[Y],
  val dataView:        DataView[C, Y, D],
  val lengthSpaceY:    LengthSpace[Y, _, Double]) {

  val titleText = title.map(Text(_, width / 2d, titleFontSize.toDouble, titleFontName, titleFontSize.toDouble, bold = true))
  val xAxisLabelText = xAxisLabel.map(Text(_, width / 2d, height - border / 2d, normalFontName, normalFontSize.toDouble, bold = true))
  val yAxisLabelText = yAxisLabel.map(Text(_, 20, height / 2d, normalFontName, normalFontSize.toDouble, bold = true, angle = Some(90d *: angleDouble.degree)))

  val keyOpt = if (drawKey) {
    Some(BarChartKey(this, keyTitle))
  } else {
    None
  }
}
