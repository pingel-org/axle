package axle.web

import scala.annotation.implicitNotFound
import scala.xml.NodeSeq
import scala.xml.NodeSeq.seqToNodeSeq

import axle.visualize.Point2D

import axle.visualize.element.DataLines
import axle.visualize.element.HorizontalLine
import axle.visualize.element.Key
import axle.visualize.element.Text
import axle.visualize.element.VerticalLine
import axle.visualize.element.XTics
import axle.visualize.element.YTics

@implicitNotFound("Witness not found for SVG[${S}]")
trait SVG[S] {

  def svg(s: S): NodeSeq

}

object SVG {

  @inline final def apply[S: SVG]: SVG[S] = implicitly[SVG[S]]

  // <rect x={ s"${20 * i}" } y="20" width="20" height="100" style="fill:blue;stroke:pink;stroke-width:5;fill-opacity:0.1;stroke-opacity:0.9"/>

  implicit def svgDataLines[X, Y, D]: SVG[DataLines[X, Y, D]] =
    new SVG[DataLines[X, Y, D]] {
      def svg(dl: DataLines[X, Y, D]): NodeSeq = {
        import dl._
        dl.data.toList.zipWithIndex.flatMap {
          case ((string, d), i) => {
            val xs = orderedXs(d).toVector
            val xsStream = xs.toStream
            xsStream.zip(xsStream.tail) map {
              case (x0, x1) => {
                val p0 = Point2D(x0, x2y(d, x0))
                val p1 = Point2D(x1, x2y(d, x1))
                val fp0 = scaledArea.framePoint(p0)
                val fp1 = scaledArea.framePoint(p1)
                <line x1={ s"${fp0.x}" } y1={ s"${fp0.y}" } x2={ s"${fp1.x}" } y2={ s"${fp1.y}" } style="stroke:rgb(0,0,0);stroke-width:1"/>
              }
            }
            //              if (pointDiameter > 0) {
            //                xs foreach { x =>
            //                  fillOval(g2d, scaledArea, Point2D(x, x2y(d, x)), pointDiameter, pointDiameter)
            //                }
            //              }
          }

        }
      }
    }

  implicit def svgKey[X, Y, D]: SVG[Key[X, Y, D]] =
    new SVG[Key[X, Y, D]] {

      def svg(key: Key[X, Y, D]): NodeSeq = {
        <todo>{ "TODO" }</todo>
      }
    }

  implicit def svgText: SVG[Text] =
    new SVG[Text] {

      def svg(t: Text): NodeSeq = {

        import t._

        // TODO color
        // TODO font

        if (angle.isDefined) {
          import axle.visualize.angleDouble
          import spire.implicits._
          val twist = angle.get.in(angleDouble.degree).magnitude * -1d
          if (centered) {
            // TODO center
            <text x={ s"$x" } y={ s"$y" } transform={ s"rotate($twist $x $y)" }>{ t.text }</text>
          } else {
            <text x={ s"$x" } y={ s"$y" } transform={ s"rotate($twist $x $y)" }>{ t.text }</text>
          }
        } else {
          if (centered) {
            // TODO center
            <text x={ s"$x" } y={ s"$y" }>{ t.text }</text>
          } else {
            <text x={ s"$x" } y={ s"$y" }>{ t.text }</text>
          }
        }
      }
    }

  implicit def svgHorizontalLine[X, Y]: SVG[HorizontalLine[X, Y]] =
    new SVG[HorizontalLine[X, Y]] {

      def svg(hl: HorizontalLine[X, Y]): NodeSeq = {
        // TODO color
        import hl._
        val p0 = Point2D(scaledArea.minX, h)
        val p1 = Point2D(scaledArea.maxX, h)
        val fp0 = scaledArea.framePoint(p0)
        val fp1 = scaledArea.framePoint(p1)
        <line x1={ s"${fp0.x}" } y1={ s"${fp0.y}" } x2={ s"${fp1.x}" } y2={ s"${fp1.y}" } style="stroke:rgb(0,0,0);stroke-width:1"/>
      }
    }

  implicit def svgVerticalLine[X, Y]: SVG[VerticalLine[X, Y]] =
    new SVG[VerticalLine[X, Y]] {

      def svg(vl: VerticalLine[X, Y]): NodeSeq = {
        // TODO color
        import vl._
        val p0 = Point2D(v, scaledArea.minY)
        val p1 = Point2D(v, scaledArea.maxY)
        val fp0 = scaledArea.framePoint(p0)
        val fp1 = scaledArea.framePoint(p1)
        <line x1={ s"${fp0.x}" } y1={ s"${fp0.y}" } x2={ s"${fp1.x}" } y2={ s"${fp1.y}" } style="stroke:rgb(0,0,0);stroke-width:1"/>
      }
    }

  implicit def svgXTics[X, Y]: SVG[XTics[X, Y]] =
    new SVG[XTics[X, Y]] {

      def svg(xt: XTics[X, Y]): NodeSeq = <todo>{ xt.color }</todo>
    }

  implicit def svgYTics[X, Y]: SVG[YTics[X, Y]] =
    new SVG[YTics[X, Y]] {

      def svg(yt: YTics[X, Y]): NodeSeq = <todo>{ yt.color }</todo>
    }

}
