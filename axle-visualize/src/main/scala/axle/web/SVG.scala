package axle.web

import java.awt.Color
import java.awt.Color.lightGray
import java.awt.Font
import java.awt.Font.BOLD

import scala.annotation.implicitNotFound
import scala.xml.NodeSeq
import scala.xml.NodeSeq.seqToNodeSeq

import axle.Show
import axle.algebra.LengthSpace
import axle.algebra.Tics
import axle.algebra.Zero
import axle.algebra.Plottable
import axle.visualize.BarChart
import axle.visualize.BarChartView
import axle.visualize.DataView
import axle.visualize.Plot
import axle.visualize.PlotDataView
import axle.visualize.PlotView
import axle.visualize.Point2D
import axle.visualize.angleDouble
import axle.visualize.element.DataLines
import axle.visualize.element.HorizontalLine
import axle.visualize.element.Key
import axle.visualize.element.Rectangle
import axle.visualize.element.Text
import axle.visualize.element.VerticalLine
import axle.visualize.element.XTics
import axle.visualize.element.YTics
import spire.algebra.Order
import spire.algebra.Eq
import spire.implicits.DoubleAlgebra

@implicitNotFound("Witness not found for SVG[${S}]")
trait SVG[S] {

  def svg(s: S): NodeSeq

}

object SVG {

  @inline final def apply[S: SVG]: SVG[S] = implicitly[SVG[S]]

  def rgb(color: Color): String = s"rgb(${color.getRed},${color.getGreen},${color.getBlue})"

  implicit def svgDataLines[X, Y, D]: SVG[DataLines[X, Y, D]] =
    new SVG[DataLines[X, Y, D]] {
      def svg(dl: DataLines[X, Y, D]): NodeSeq = {

        import dl._

        val pointRadius = pointDiameter / 2

        data.zip(colorStream).flatMap {
          case ((_, d), color) => {
            val xs = orderedXs(d).toList
            val centers = xs.map(x => scaledArea.framePoint(Point2D(x, x2y(d, x))))
            val points = (centers map { c => s"${c.x},${c.y}" }).mkString(" ")
            val polyline = <polyline points={ s"$points" } fill="none" stroke={ s"${rgb(color)}" } stroke-width="1"/>
            val pointCircles =
              if (pointRadius > 0) {
                centers map { c =>
                  <circle cx={ s"${c.x}" } cy={ s"${c.y}" } r={ s"${pointRadius}" } fill={ s"${rgb(color)}" }/>
                }
              } else {
                List.empty
              }
            polyline :: pointCircles
          }
        }
      }
    }

  implicit def svgKey[X, Y, D]: SVG[Key[X, Y, D]] =
    new SVG[Key[X, Y, D]] {
      def svg(key: Key[X, Y, D]): NodeSeq = {
        import key._
        data.zip(colorStream).zipWithIndex map {
          case (((label, _), color), i) => {
            <text x={ s"${plot.width - width}" } y={ s"${topPadding + font.getSize * (i + 1)}" } fill={ s"${rgb(color)}" } font-size={ s"${font.getSize}" }>{ label }</text>
          }
        }
      }
    }

  implicit def svgText: SVG[Text] =
    new SVG[Text] {

      def svg(t: Text): NodeSeq = {

        import t._

        if (angle.isDefined) {
          import axle.visualize.angleDouble
          import spire.implicits._
          val twist = angle.get.in(angleDouble.degree).magnitude * -1d
          if (centered) {
            <text text-anchor="middle" x={ s"$x" } y={ s"$y" } transform={ s"rotate($twist $x $y)" } fill={ s"${rgb(color)}" } font-size={ s"${font.getSize}" }>{ t.text }</text>
          } else {
            <text text-anchor="left" x={ s"$x" } y={ s"$y" } transform={ s"rotate($twist $x $y)" } fill={ s"${rgb(color)}" } font-size={ s"${font.getSize}" }>{ t.text }</text>
          }
        } else {
          if (centered) {
            <text text-anchor="middle" x={ s"$x" } y={ s"$y" } fill={ s"${rgb(color)}" } font-size={ s"${font.getSize}" }>{ t.text }</text>
          } else {
            <text text-anchor="left" x={ s"$x" } y={ s"$y" } fill={ s"${rgb(color)}" } font-size={ s"${font.getSize}" }>{ t.text }</text>
          }
        }
      }
    }

  implicit def svgHorizontalLine[X, Y]: SVG[HorizontalLine[X, Y]] =
    new SVG[HorizontalLine[X, Y]] {

      def svg(hl: HorizontalLine[X, Y]): NodeSeq = {
        import hl._
        val left = scaledArea.framePoint(Point2D(scaledArea.minX, h))
        val right = scaledArea.framePoint(Point2D(scaledArea.maxX, h))
        <line x1={ s"${left.x}" } y1={ s"${left.y}" } x2={ s"${right.x}" } y2={ s"${right.y}" } stroke={ s"${rgb(color)}" } stroke-width="1"/>
      }
    }

  implicit def svgVerticalLine[X, Y]: SVG[VerticalLine[X, Y]] =
    new SVG[VerticalLine[X, Y]] {

      def svg(vl: VerticalLine[X, Y]): NodeSeq = {
        import vl._
        val bottom = scaledArea.framePoint(Point2D(v, scaledArea.minY))
        val top = scaledArea.framePoint(Point2D(v, scaledArea.maxY))
        <line x1={ s"${bottom.x}" } y1={ s"${bottom.y}" } x2={ s"${top.x}" } y2={ s"${top.y}" } stroke={ s"${rgb(color)}" } stroke-width="1"/>
      }
    }

  implicit def svgRectangle[X, Y]: SVG[Rectangle[X, Y]] =
    new SVG[Rectangle[X, Y]] {

      def svg(rectangle: Rectangle[X, Y]): NodeSeq = {
        import rectangle.scaledArea
        val ll = scaledArea.framePoint(rectangle.lowerLeft)
        val ur = scaledArea.framePoint(rectangle.upperRight)
        val width = ur.x - ll.x
        val height = ll.y - ur.y
        <rect x={s"${ll.x}"} y={s"${ur.y}"} width={s"$width"} height={s"$height"} fill={ s"${rgb(rectangle.fillColor.getOrElse(Color.blue))}" } stroke={ s"${rgb(rectangle.borderColor.getOrElse(Color.blue))}" } stroke-width="1"/>
      }
    }

  implicit def svgPlot[X, Y, D]: SVG[Plot[X, Y, D]] = new SVG[Plot[X, Y, D]] {

    def svg(plot: Plot[X, Y, D]): NodeSeq = {

      import plot._

      val view = PlotView(plot, plot.initialValue)

      import view._

      val nodes =
        SVG[HorizontalLine[X, Y]].svg(hLine) ::
          SVG[VerticalLine[X, Y]].svg(vLine) ::
          SVG[XTics[X, Y]].svg(xTics) ::
          SVG[YTics[X, Y]].svg(yTics) ::
          SVG[DataLines[X, Y, D]].svg(dataLines) ::
          List(
            titleText.map(SVG[Text].svg),
            xAxisLabelText.map(SVG[Text].svg),
            yAxisLabelText.map(SVG[Text].svg),
            view.keyOpt.map(SVG[Key[X, Y, D]].svg)).flatten

      svgFrame(nodes.reduce(_ ++ _), width, height)
    }
  }

  implicit def svgXTics[X, Y]: SVG[XTics[X, Y]] =
    new SVG[XTics[X, Y]] {

      def svg(xt: XTics[X, Y]): NodeSeq = {

        import xt._
        import scaledArea._

        tics flatMap {
          case (x, label) => {
            val pre = if (fDrawLines) {
              val bottom = scaledArea.framePoint(Point2D(x, minY))
              val top = scaledArea.framePoint(Point2D(x, maxY))
              List(<line x1={ s"${bottom.x}" } y1={ s"${bottom.y}" } x2={ s"${top.x}" } y2={ s"${top.y}" } stroke={ s"${rgb(lightGray)}" } stroke-width="1"/>)
            } else {
              Nil
            }
            val bottom = framePoint(Point2D(x, minY))
            // TODO: if (angle === zeroDegrees)
            pre ++ List(
              <text text-anchor="middle" alignment-baseline="hanging" x={ s"${bottom.x}" } y={ s"${bottom.y}" } fill={ s"${rgb(color)}" } font-size={ s"${font.getSize}" }>{ label }</text>,
              <line x1={ s"${bottom.x}" } y1={ s"${bottom.y - 2}" } x2={ s"${bottom.x}" } y2={ s"${bottom.y + 2}" } stroke={ s"${rgb(lightGray)}" } stroke-width="1"/>)
          }
        }
      }
    }

  implicit def svgYTics[X, Y]: SVG[YTics[X, Y]] =
    new SVG[YTics[X, Y]] {

      def svg(yt: YTics[X, Y]): NodeSeq = {

        import yt._
        import scaledArea._

        tics.flatMap({
          case (y, label) => {

            val left = framePoint(Point2D(minX, y))
            val right = framePoint(Point2D(maxX, y))

            List(
              <line x1={ s"${left.x}" } y1={ s"${left.y}" } x2={ s"${right.x}" } y2={ s"${right.y}" } stroke={ s"${rgb(lightGray)}" } stroke-width="1"/>,
              <text text-anchor="end" alignment-baseline="middle" x={ s"${left.x - 5}" } y={ s"${left.y}" } font-size={ s"${font.getSize}" }>{ label }</text>,
              <line x1={ s"${left.x - 2}" } y1={ s"${left.y}" } x2={ s"${left.x + 2}" } y2={ s"${left.y}" } stroke={ s"${rgb(lightGray)}" } stroke-width="1"/>)
          }
        })
      }
    }

  implicit def svgBarChart[S, Y, D]: SVG[BarChart[S, Y, D]] =
    new SVG[BarChart[S, Y, D]] {

      def svg(chart: BarChart[S, Y, D]): NodeSeq = {

        import chart._

        val view = BarChartView(chart, chart.initialValue)

        import view._

        val nodes =
          SVG[HorizontalLine[Double, Y]].svg(hLine) ::
            SVG[VerticalLine[Double, Y]].svg(vLine) ::
            SVG[XTics[Double, Y]].svg(gTics) ::
            SVG[YTics[Double, Y]].svg(yTics) ::
            bars.map(SVG[Rectangle[Double, Y]].svg).flatten ::
            List(
              titleText.map(SVG[Text].svg),
              xAxisLabelText.map(SVG[Text].svg),
              yAxisLabelText.map(SVG[Text].svg)).flatten

        svgFrame(nodes.reduce(_ ++ _), width, height)
      }
    }

}
