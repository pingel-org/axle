package axle.visualize

import java.awt.Color
import java.awt.Color.blue
import java.awt.Color.green
import java.awt.Color.orange
import java.awt.Color.pink
import java.awt.Color.red
import java.awt.Color.yellow
import java.awt.Font

import scala.Stream.continually
import scala.reflect.ClassTag

import axle.Show
import axle.algebra.LengthSpace
import axle.algebra.Plottable
import axle.algebra.Tics
import axle.algebra.Zero
import axle.quanta.Angle
import axle.quanta.UnittedQuantity
import axle.visualize.element.BarChartKey
import axle.visualize.element.Text
import spire.algebra.Eq
import spire.algebra.Order

case class BarChart[S, Y, D](
    initialValue: D,
    drawKey: Boolean = true,
    width: Int = 700,
    height: Int = 600,
    border: Int = 50,
    barWidthPercent: Double = 0.80,
    keyLeftPadding: Int = 20,
    keyTopPadding: Int = 50,
    keyWidth: Int = 80,
    title: Option[String] = None,
    keyTitle: Option[String] = None,
    normalFontName: String = "Courier New",
    normalFontSize: Int = 12,
    titleFontName: String = "Palatino",
    titleFontSize: Int = 20,
    xAxis: Option[Y] = None,
    xAxisLabel: Option[String] = None,
    yAxisLabel: Option[String] = None,
    labelAngle: UnittedQuantity[Angle, Double] = 36d *: angleDouble.degree,
    colors: Seq[Color] = List(blue, red, green, orange, pink, yellow))(
        implicit val showS: Show[S],
        val zeroY: Zero[Y],
        val orderY: Order[Y],
        val ticsY: Tics[Y],
        val eqY: Eq[Y],
        val plottableY: Plottable[Y],
        val classTagD: ClassTag[D],
        val dataView: DataView[S, Y, D],
        val lengthSpaceY: LengthSpace[Y, _]) {

  val colorStream = continually(colors.toStream).flatten

  val titleFont = new Font(titleFontName, Font.BOLD, titleFontSize)
  val normalFont = new Font(normalFontName, Font.BOLD, normalFontSize)

  val titleText = title.map(Text(_, titleFont, width / 2, titleFontSize))
  val xAxisLabelText = xAxisLabel.map(Text(_, normalFont, width / 2, height - border / 2))
  val yAxisLabelText = yAxisLabel.map(Text(_, normalFont, 20, height / 2, angle = Some(90d *: angleDouble.degree)))

  val keyOpt = if (drawKey) {
    Some(BarChartKey(this, keyTitle, normalFont))
  } else {
    None
  }
}
