package axle.visualize

import java.awt.Color
import java.awt.Color.blue
import java.awt.Color.green
import java.awt.Color.orange
import java.awt.Color.pink
import java.awt.Color.red
import java.awt.Color.yellow

import scala.reflect.ClassTag

import axle.algebra.LengthSpace
import axle.algebra.Plottable
import axle.algebra.Tics
import axle.quanta.Angle
import axle.quanta.Angle.{° => °}
import axle.quanta.UnittedQuantity
import axle.Show
import axle.string
import spire.algebra.Eq
import spire.algebra.Order
import spire.implicits.DoubleAlgebra

object BarChart {

  implicit def drawBarChart[S: Show, Y: Plottable: Order: Tics: Eq, D: ClassTag](implicit yls: LengthSpace[Y, _]): Draw[BarChart[S, Y, D]] =
    new Draw[BarChart[S, Y, D]] {
      def component(barChart: BarChart[S, Y, D]) = new BarChartComponent(barChart)
    }

}

case class BarChart[S: Show, Y: Plottable, D](
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
  normalFontName: String = "Courier New",
  normalFontSize: Int = 12,
  titleFontName: String = "Palatino",
  titleFontSize: Int = 20,
  xAxis: Y,
  xAxisLabel: Option[String] = None,
  yAxisLabel: Option[String] = None,
  labelAngle: UnittedQuantity[Angle, Double] = 36d *: °[Double],
  colors: Seq[Color] = List(blue, red, green, orange, pink, yellow))(
    implicit val dataView: DataView[S, Y, D])
