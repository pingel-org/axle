
package axle

import java.awt.Font
import java.awt.FontMetrics
import java.awt.Color
import java.awt.Component
import java.awt.Graphics2D
import java.io.File

import javax.imageio.ImageIO

import edu.uci.ics.jung.graph.DirectedSparseGraph
import edu.uci.ics.jung.graph.UndirectedSparseGraph

import scala.concurrent.duration._
import scala.reflect.ClassTag

import cats.kernel.Eq
import cats.implicits._
import cats.Show

import monix.execution.Cancelable
import monix.execution.Scheduler
import monix.reactive.Observable

import spire.math.abs
import spire.math.min
import spire.algebra.Field
import spire.implicits.DoubleAlgebra
import spire.implicits.eqOps

import axle.algebra.DirectedGraph
import axle.pgm.BayesianNetwork
import axle.pgm.BayesianNetworkNode
import axle.quanta.Angle
import axle.quanta.UnittedQuantity
import axle.visualize._
import axle.visualize.element._

package object awt {

  def draw[T: Draw](t: T): AxleFrame = {

    val draw = Draw[T]
    val component = draw.component(t)
    val minSize = component.getMinimumSize
    val frame = AxleFrame(minSize.width, minSize.height)
    frame.initialize()
    val rc = frame.add(component)
    rc.setVisible(true)
    frame.setVisible(true)
    frame
  }

  def play[T: Draw, D](t: T, dataStream: Observable[D])(implicit scheduler: Scheduler): (AxleFrame, Cancelable) = {

    val draw = Draw[T]
    val component: Component = draw.component(t)
    val minSize = component.getMinimumSize
    val frame = AxleFrame(minSize.width, minSize.height)

    frame.initialize()
    val rc = frame.add(component)
    rc.setVisible(true)
    frame.setVisible(true)
    frame.repaint()

    val frameTic = Observable.interval(42.milliseconds)

    val cancelPainting =
      Observable.zipMap2(dataStream, frameTic)({ case (d, t) => {} }).foreach(u => {
        frame.repaint()
      })

    (frame, cancelPainting)
  }

  val fontMemo = scala.collection.mutable.Map.empty[(String, Int, Boolean), Font]

  def cachedFont(name: String, size: Int, bold: Boolean): Font = {
    val fontKey = (name, size, bold)
    if (fontMemo.contains(fontKey)) {
      fontMemo(fontKey)
    } else {
      val effect = if (bold) Font.BOLD else Font.PLAIN
      val font = new Font(name, effect, size)
      fontMemo += fontKey -> font
      font
    }
  }

  val colorMemo = scala.collection.mutable.Map.empty[axle.visualize.Color, java.awt.Color]
  def cachedColor(axc: axle.visualize.Color): java.awt.Color = {
    if (colorMemo.contains(axc)) {
      colorMemo(axc)
    } else {
      val jc = new java.awt.Color(axc.r, axc.g, axc.b)
      colorMemo += axc -> jc
      jc
    }
  }

  implicit def drawPlot[S, X, Y, D]: Draw[Plot[S, X, Y, D]] =
    new Draw[Plot[S, X, Y, D]] {
      def component(plot: Plot[S, X, Y, D]) = PlotComponent(plot)
    }

  implicit def drawScatterPlot[S, X, Y, D: ClassTag]: Draw[ScatterPlot[S, X, Y, D]] =
    new Draw[ScatterPlot[S, X, Y, D]] {
      def component(plot: ScatterPlot[S, X, Y, D]) = ScatterPlotComponent(plot)
    }

  implicit def drawBarChart[C, Y, D: ClassTag, H]: Draw[BarChart[C, Y, D, H]] =
    new Draw[BarChart[C, Y, D, H]] {
      def component(chart: BarChart[C, Y, D, H]) = BarChartComponent(chart)
    }

  implicit def drawBarChartGrouped[G, S, Y, D: ClassTag, H]: Draw[BarChartGrouped[G, S, Y, D, H]] =
    new Draw[BarChartGrouped[G, S, Y, D, H]] {
      def component(chart: BarChartGrouped[G, S, Y, D, H]) = BarChartGroupedComponent(chart)
    }

  implicit def drawJungUndirectedGraph[VP: Show, EP: Show]: Draw[UndirectedSparseGraph[VP, EP]] =
    new Draw[UndirectedSparseGraph[VP, EP]] {
      def component(jug: UndirectedSparseGraph[VP, EP]) =
        JungUndirectedGraphVisualization(700, 700, 50).component(jug)
    }

  implicit def drawJungDirectedGraph[VP: HtmlFrom, EP: Show]: Draw[DirectedSparseGraph[VP, EP]] =
    new Draw[DirectedSparseGraph[VP, EP]] {
      def component(jdg: DirectedSparseGraph[VP, EP]) =
        JungDirectedGraphVisualization(700, 700, 50).component(jdg)
    }

  implicit def drawBayesianNetwork[T: Manifest: Eq, N: Field: Manifest: Eq, DG](
    implicit drawDG: Draw[DG], dg: DirectedGraph[DG, BayesianNetworkNode[T, N], axle.pgm.Edge]): Draw[BayesianNetwork[T, N, DG]] = {
    new Draw[BayesianNetwork[T, N, DG]] {
      def component(bn: BayesianNetwork[T, N, DG]) =
        drawDG.component(bn.graph)
    }
  }

  implicit def drawKMeansVisualization[T, F, G, M]: Draw[KMeansVisualization[T, F, G, M]] =
    new Draw[KMeansVisualization[T, F, G, M]] {
      def component(kmv: KMeansVisualization[T, F, G, M]) = KMeansComponent(kmv)
    }

  /**
   * image2file
   *
   * encoding: PNG, JPEG, gif, BMP
   *
   */

  def image2file[T: Image](t: T, filename: String, encoding: String): Unit = {
    val image = Image[T].image(t)
    val rc = ImageIO.write(image, encoding, new File(filename))
  }

  def png[T: Image](t: T, filename: String): Unit = image2file(t, filename, "PNG")

  def jpeg[T: Image](t: T, filename: String): Unit = image2file(t, filename, "JPEG")

  def gif[T: Image](t: T, filename: String): Unit = image2file(t, filename, "gif")

  def bmp[T: Image](t: T, filename: String): Unit = image2file(t, filename, "BMP")

  implicit def paintDataLines[S, X, Y, D]: Paintable[DataLines[S, X, Y, D]] = new Paintable[DataLines[S, X, Y, D]] {

    def paint(dataLines: DataLines[S, X, Y, D], g2d: Graphics2D): Unit = {

      import dataLines._

      data foreach {
        case (label, d) =>
          g2d.setColor(cachedColor(colorOf(label)))
          val xs = orderedXs(d).toVector
          if (connect && xs.size > 1) {
            val xsStream = xs.toStream
            xsStream.zip(xsStream.tail) foreach {
              case (x0, x1) =>
                drawLine(g2d, scaledArea, Point2D(x0, x2y(d, x0)), Point2D(x1, x2y(d, x1)))
            }
          }
          if (pointDiameter > 0) {
            xs foreach { x =>
              fillOval(g2d, scaledArea, Point2D(x, x2y(d, x)), pointDiameter, pointDiameter)
            }
          }
      }
    }

  }

  implicit def paintDataPoints[S, X, Y, D]: Paintable[DataPoints[S, X, Y, D]] = new Paintable[DataPoints[S, X, Y, D]] {

    def paint(dataPoints: DataPoints[S, X, Y, D], g2d: Graphics2D): Unit = {

      import dataPoints._

      val domain = dataView.dataToDomain(data)

      domain foreach {
        case (x, y) => {
          val pointDiameter = diameterOf(x, y)
          g2d.setColor(cachedColor(dataPoints.colorOf(x, y)))
          fillOval(g2d, scaledArea, Point2D(x, y), pointDiameter.toInt, pointDiameter.toInt)
        }
      }
    }

  }

  implicit def paintHorizontalLine[X, Y]: Paintable[HorizontalLine[X, Y]] = new Paintable[HorizontalLine[X, Y]] {

    def paint(hLine: HorizontalLine[X, Y], g2d: Graphics2D): Unit = {

      import hLine._
      import scaledArea._

      g2d.setColor(cachedColor(color))
      drawLine(g2d, scaledArea, Point2D(minX, h), Point2D(maxX, h))
    }

  }

  implicit def paintVerticalLine[X, Y]: Paintable[VerticalLine[X, Y]] = new Paintable[VerticalLine[X, Y]] {

    def paint(vLine: VerticalLine[X, Y], g2d: Graphics2D): Unit = {

      import vLine._
      import scaledArea._

      g2d.setColor(cachedColor(color))
      drawLine(g2d, scaledArea, Point2D(v, minY), Point2D(v, maxY))
    }

  }

  implicit def painText: Paintable[Text] = new Paintable[Text] {

    def paint(t: Text, g2d: Graphics2D): Unit = {

      import t._

      g2d.setColor(cachedColor(color))
      g2d.setFont(cachedFont(fontName, fontSize.toInt, bold))

      val fontMetrics = g2d.getFontMetrics

      if (angleRadOpt.isDefined) {
        val twist = angleRadOpt.get
        g2d.translate(x, y)
        g2d.rotate(twist * -1)
        if (centered) {
          g2d.drawString(text, -fontMetrics.stringWidth(text) / 2, 0)
        } else {
          g2d.drawString(text, 0, 0)
        }
        g2d.rotate(twist)
        g2d.translate(-x, -y)
      } else {
        if (centered) {
          g2d.drawString(text, (x - fontMetrics.stringWidth(text) / 2).toInt, y.toInt)
        } else {
          g2d.drawString(text, x.toInt, y.toInt)
        }
      }

    }

  }

  implicit def paintOval[X, Y]: Paintable[Oval[X, Y]] = new Paintable[Oval[X, Y]] {

    def paint(oval: Oval[X, Y], g2d: Graphics2D): Unit = {

      import oval._

      g2d.setColor(cachedColor(borderColor))
      fillOval(g2d, scaledArea, center, width, height)

      g2d.setColor(cachedColor(color))
      drawOval(g2d, scaledArea, center, width, height)
    }

  }

  implicit def paintRectangle[X, Y]: Paintable[Rectangle[X, Y]] = new Paintable[Rectangle[X, Y]] {

    def paint(r: Rectangle[X, Y], g2d: Graphics2D): Unit = {

      import r._

      fillColor.foreach(color => {
        g2d.setColor(cachedColor(color))
        fillRectangle(
          g2d,
          scaledArea,
          Point2D(lowerLeft.x, lowerLeft.y),
          Point2D(upperRight.x, upperRight.y))
      })
      borderColor.foreach(color => {
        g2d.setColor(cachedColor(color))
        drawRectangle(
          g2d,
          scaledArea,
          Point2D(lowerLeft.x, lowerLeft.y),
          Point2D(upperRight.x, upperRight.y))
      })
    }

  }

  implicit def paintYTics[X, Y]: Paintable[YTics[X, Y]] = new Paintable[YTics[X, Y]] {

    def paint(yt: YTics[X, Y], g2d: Graphics2D): Unit = {

      import yt._
      g2d.setColor(cachedColor(color))

      val fontMetrics = g2d.getFontMetrics
      import scaledArea._
      import java.awt.Color

      tics foreach {
        case (y, label) => {
          val leftScaled = Point2D(minX, y)
          val leftUnscaled = framePoint(leftScaled)
          g2d.setColor(Color.lightGray)
          drawLine(g2d, scaledArea, leftScaled, Point2D(maxX, y))
          g2d.setColor(Color.black)
          g2d.drawString(label, (leftUnscaled.x - fontMetrics.stringWidth(label) - 5).toInt, (leftUnscaled.y + fontMetrics.getHeight / 2).toInt)
          g2d.drawLine((leftUnscaled.x - 2).toInt, leftUnscaled.y.toInt, (leftUnscaled.x + 2).toInt, leftUnscaled.y.toInt)
        }
      }
    }

  }

  implicit def paintXTics[X, Y]: Paintable[XTics[X, Y]] = new Paintable[XTics[X, Y]] {

    def paint(xt: XTics[X, Y], g2d: Graphics2D): Unit = {

      import xt._
      import scaledArea._

      g2d.setColor(cachedColor(color))
      g2d.setFont(cachedFont(fontName, fontSize.toInt, bold))

      val fontMetrics = g2d.getFontMetrics

      tics foreach {
        case (x, label) => {
          if (drawLines) {
            g2d.setColor(Color.lightGray)
            drawLine(g2d, scaledArea, Point2D(x, minY), Point2D(x, maxY))
          }
          val bottomScaled = Point2D(x, minY)
          val bottomUnscaled = framePoint(bottomScaled)
          g2d.setColor(Color.black)

          // TODO: angle xtics?
          angle foreach { a =>
            if (a === zeroDegrees) {
              g2d.drawString(label, (bottomUnscaled.x - fontMetrics.stringWidth(label) / 2).toInt, bottomUnscaled.y.toInt + fontMetrics.getHeight)
            } else {
              drawStringAtAngle(g2d, scaledArea, fontMetrics, label, bottomScaled, a)
            }
          }

          g2d.drawLine(bottomUnscaled.x.toInt, (bottomUnscaled.y - 2).toInt, bottomUnscaled.x.toInt, (bottomUnscaled.y + 2).toInt)
        }
      }

    }

  }

  implicit def paintKey[S: Show, X, Y, D]: Paintable[Key[S, X, Y, D]] = new Paintable[Key[S, X, Y, D]] {

    def paint(key: Key[S, X, Y, D], g2d: Graphics2D): Unit = {

      import key._

      g2d.setFont(cachedFont(plot.fontName, plot.fontSize, plot.bold))
      val fontMetrics = g2d.getFontMetrics

      val lineHeight = g2d.getFontMetrics.getHeight
      data.zipWithIndex foreach {
        case ((label, d), i) =>
          val color = colorOf(label)
          g2d.setColor(cachedColor(color))
          g2d.drawString(string(label), plot.width - width, topPadding + lineHeight * (i + 1))
      }
    }

  }

  implicit def paintBarChartKey[X, Y, D, H]: Paintable[BarChartKey[X, Y, D, H]] =
    new Paintable[BarChartKey[X, Y, D, H]] {

      def paint(key: BarChartKey[X, Y, D, H], g2d: Graphics2D): Unit = {

        import key._
        import chart._

        g2d.setFont(cachedFont(chart.normalFontName, chart.normalFontSize, true))
        val lineHeight = g2d.getFontMetrics.getHeight
        slices.toVector.zipWithIndex foreach {
          case (s, j) =>
            val color = colorOf(s)
            g2d.setColor(cachedColor(color))
            g2d.drawString(string(s), width - keyWidth, keyTopPadding + lineHeight * (j + 1))
        }
      }

    }

  implicit def paintBarChartGroupedKey[G, S, Y, D, H]: Paintable[BarChartGroupedKey[G, S, Y, D, H]] =
    new Paintable[BarChartGroupedKey[G, S, Y, D, H]] {

      def paint(key: BarChartGroupedKey[G, S, Y, D, H], g2d: Graphics2D): Unit = {

        import key._
        import chart._

        g2d.setFont(cachedFont(chart.normalFontName, chart.normalFontSize, true))
        val lineHeight = g2d.getFontMetrics.getHeight
        slices.toVector.zipWithIndex foreach {
          case (s, i) =>
            groups.toVector.zipWithIndex foreach {
              case (g, j) =>
                val r = i * groups.size + j
                g2d.setColor(cachedColor(colorOf(g, s)))
                g2d.drawString(string(s), width - keyWidth, keyTopPadding + lineHeight * (r + 1))
            }
        }
      }

    }

  def fillOval[X, Y](g2d: Graphics2D, scaledArea2D: ScaledArea2D[X, Y], p: Point2D[X, Y], width: Int, height: Int): Unit = {
    if (scaledArea2D.nonZeroArea) {
      val fp = scaledArea2D.framePoint(p)
      g2d.fillOval((fp.x - width / 2).toInt, (fp.y - height / 2).toInt, width, height)
    }
  }

  def drawOval[X, Y](g2d: Graphics2D, scaledArea2D: ScaledArea2D[X, Y], p: Point2D[X, Y], width: Int, height: Int): Unit = {
    if (scaledArea2D.nonZeroArea) {
      val fp = scaledArea2D.framePoint(p)
      g2d.drawOval((fp.x - width / 2).toInt, (fp.y - height / 2).toInt, width, height)
    }
  }

  def drawLine[X, Y](g2d: Graphics2D, scaledArea: ScaledArea2D[X, Y], p0: Point2D[X, Y], p1: Point2D[X, Y]): Unit = {
    if (scaledArea.nonZeroArea) {
      val fp0 = scaledArea.framePoint(p0)
      val fp1 = scaledArea.framePoint(p1)
      g2d.drawLine(fp0.x.toInt, fp0.y.toInt, fp1.x.toInt, fp1.y.toInt)
    }
  }

  def fillRectangle[X, Y](g2d: Graphics2D, scaledArea: ScaledArea2D[X, Y], p0: Point2D[X, Y], p1: Point2D[X, Y]): Unit = {
    if (scaledArea.nonZeroArea) {
      val fp0 = scaledArea.framePoint(p0)
      val fp1 = scaledArea.framePoint(p1)
      g2d.fillRect(min(fp0.x.toInt, fp1.x.toInt), min(fp0.y.toInt, fp1.y.toInt), abs(fp0.x.toInt - fp1.x.toInt), abs(fp0.y.toInt - fp1.y.toInt))
    }
  }

  def drawRectangle[X, Y](g2d: Graphics2D, scaledArea: ScaledArea2D[X, Y], p0: Point2D[X, Y], p1: Point2D[X, Y]): Unit = {
    if (scaledArea.nonZeroArea) {
      val fp0 = scaledArea.framePoint(p0)
      val fp1 = scaledArea.framePoint(p1)
      g2d.drawRect(min(fp0.x.toInt, fp1.x.toInt), min(fp0.y.toInt, fp1.y.toInt), abs(fp0.x.toInt - fp1.x.toInt), abs(fp0.y.toInt - fp1.y.toInt))
    }
  }

  def drawString[X, Y](g2d: Graphics2D, scaledArea: ScaledArea2D[X, Y], s: String, p: Point2D[X, Y]): Unit = {
    if (scaledArea.nonZeroArea) {
      val fp = scaledArea.framePoint(p)
      g2d.drawString(s, fp.x.toInt, fp.y.toInt)
    }
  }

  def drawStringAtAngle[X, Y](
    g2d: Graphics2D,
    scaledArea: ScaledArea2D[X, Y],
    fontMetrics: FontMetrics,
    s: String,
    p: Point2D[X, Y],
    angle: UnittedQuantity[Angle, Double]): Unit = {
    if (scaledArea.nonZeroArea) {
      val fp = scaledArea.framePoint(p)
      val a = (angle in angleDouble.radian).magnitude
      g2d.translate(fp.x, fp.y + fontMetrics.getHeight)
      g2d.rotate(a)
      g2d.drawString(s, 0, 0)
      g2d.rotate(-1 * a)
      g2d.translate(-fp.x, -fp.y - fontMetrics.getHeight)
    }
  }

}
