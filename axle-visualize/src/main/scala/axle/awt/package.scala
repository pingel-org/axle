
package axle

import scala.math.abs
import scala.math.min

import java.awt.FontMetrics
import java.awt.Color
import java.awt.Component
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import axle.visualize.ScaledArea2D

import scala.annotation.implicitNotFound
import scala.reflect.ClassTag

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import axle.algebra.DirectedGraph
import axle.algebra.LengthSpace
import axle.algebra.LinearAlgebra
import axle.algebra.Plottable
import axle.algebra.Tics
import axle.algebra.Zero
import axle.awt.Paintable
import axle.awt.BarChartComponent
import axle.awt.BarChartGroupedComponent
import axle.awt.PlotComponent
import axle.awt.JungUndirectedGraphVisualization
import axle.awt.JungDirectedGraphVisualization
import axle.awt.KMeansComponent
import axle.jung.JungDirectedGraph
import axle.jung.JungUndirectedGraph
import axle.ml.KMeans
import axle.pgm.BayesianNetwork
import axle.pgm.BayesianNetworkNode
import axle.quanta.Angle
import axle.quanta.Time
import axle.quanta.TimeConverter
import axle.quanta.UnittedQuantity
import axle.visualize.angleDouble
import axle.visualize.BarChart
import axle.visualize.BarChartGrouped
import axle.visualize.Fed
import axle.visualize.FrameRepaintingActor
import axle.visualize.KMeansVisualization
import axle.visualize.Plot
import axle.visualize.Point2D
import axle.visualize.element.BarChartGroupedKey
import axle.visualize.element.BarChartKey
import axle.visualize.element.DataLines
import axle.visualize.element.HorizontalLine
import axle.visualize.element.Key
import axle.visualize.element.Oval
import axle.visualize.element.Rectangle
import axle.visualize.element.Text
import axle.visualize.element.VerticalLine
import axle.visualize.element.XTics
import axle.visualize.element.YTics
import javax.imageio.ImageIO
import spire.algebra.Eq
import spire.algebra.Field
import spire.algebra.Order
import spire.implicits.DoubleAlgebra
import spire.implicits.eqOps

package object awt {

  def draw[T: Draw](t: T): Unit = {
    val draw = Draw[T]
    val component = draw.component(t)
    val minSize = component.getMinimumSize
    val frame = AxleFrame(minSize.width, minSize.height)
    frame.initialize()
    val rc = frame.add(component)
    rc.setVisible(true)
    frame.setVisible(true)
  }

  def play[T: Draw, D](
    t: T,
    f: D => D,
    interval: UnittedQuantity[Time, Double])(
      implicit system: ActorSystem,
      tc: TimeConverter[Double]): ActorRef = {

    val draw = Draw[T]
    draw.component(t) match {
      case fed: Component with Fed[D] => {
        val minSize = fed.getMinimumSize
        val frame = AxleFrame(minSize.width, minSize.height)
        val feeder = fed.setFeeder(f, interval, system)
        system.actorOf(Props(classOf[FrameRepaintingActor], frame, fed.feeder.get))
        frame.initialize()
        val rc = frame.add(fed)
        rc.setVisible(true)
        frame.setVisible(true)
        feeder
      }
      case _ => null
    }
  }

  implicit def drawPlot[X: Zero: Tics: Eq, Y: Zero: Tics: Eq, D](
    implicit xls: LengthSpace[X, _], yls: LengthSpace[Y, _]): Draw[Plot[X, Y, D]] =
    new Draw[Plot[X, Y, D]] {

      def component(plot: Plot[X, Y, D]) = PlotComponent(plot)
    }

  implicit def drawBarChart[S: Show, Y: Plottable: Order: Tics: Eq, D: ClassTag](
    implicit yls: LengthSpace[Y, _]): Draw[BarChart[S, Y, D]] =
    new Draw[BarChart[S, Y, D]] {
      def component(barChart: BarChart[S, Y, D]) = BarChartComponent(barChart)
    }

  implicit def drawBarChartGrouped[G: Show, S: Show, Y: Plottable: Tics: Order: Eq, D: ClassTag](
    implicit yls: LengthSpace[Y, _]): Draw[BarChartGrouped[G, S, Y, D]] =
    new Draw[BarChartGrouped[G, S, Y, D]] {
      def component(barChart: BarChartGrouped[G, S, Y, D]) = BarChartGroupedComponent(barChart)
    }

  implicit def drawJungUndirectedGraph[VP: Show, EP: Show]: Draw[JungUndirectedGraph[VP, EP]] =
    new Draw[JungUndirectedGraph[VP, EP]] {
      def component(jug: JungUndirectedGraph[VP, EP]) =
        JungUndirectedGraphVisualization().component(jug)
    }

  implicit def drawJungDirectedGraph[VP: HtmlFrom, EP: Show]: Draw[JungDirectedGraph[VP, EP]] =
    new Draw[JungDirectedGraph[VP, EP]] {
      def component(jdg: JungDirectedGraph[VP, EP]) =
        JungDirectedGraphVisualization().component(jdg)
    }

  implicit def drawBayesianNetwork[T: Manifest: Eq, N: Field: Manifest: Eq, DG[_, _]: DirectedGraph](implicit drawDG: Draw[DG[BayesianNetworkNode[T, N], String]]): Draw[BayesianNetwork[T, N, DG]] = {
    new Draw[BayesianNetwork[T, N, DG]] {
      def component(bn: BayesianNetwork[T, N, DG]) =
        drawDG.component(bn.graph)
    }
  }

  implicit def drawKMeansClasifier[T, F[_], M](implicit la: LinearAlgebra[M, Int, Int, Double]): Draw[KMeans[T, F, M]] =
    new Draw[KMeans[T, F, M]] {
      def component(kmc: KMeans[T, F, M]) = KMeansComponent(KMeansVisualization(kmc))
    }

  /**
   * component2file
   *
   * encoding: PNG, JPEG, gif, BMP
   *
   * http://stackoverflow.com/questions/4028898/create-an-image-from-a-non-visible-awt-component
   */

  def draw2file[T: Draw](t: T, filename: String, encoding: String): Unit = {

    val component = Draw[T].component(t)

    val minSize = component.getMinimumSize
    val frame = AxleFrame(minSize.width, minSize.height)
    frame.setUndecorated(true)
    frame.initialize()
    val rc = frame.add(component)
    // rc.setVisible(true)
    frame.setVisible(true)

    val img = new BufferedImage(frame.getWidth, frame.getHeight, BufferedImage.TYPE_INT_RGB) // ARGB
    val g = img.createGraphics()
    frame.paintAll(g)

    ImageIO.write(img, encoding, new File(filename))

    g.dispose()
  }

  def png[T: Draw](t: T, filename: String): Unit = draw2file(t, filename, "PNG")

  def jpeg[T: Draw](t: T, filename: String): Unit = draw2file(t, filename, "JPEG")

  def gif[T: Draw](t: T, filename: String): Unit = draw2file(t, filename, "gif")

  def bmp[T: Draw](t: T, filename: String): Unit = draw2file(t, filename, "BMP")

  implicit def paintDataLines[X, Y, D]: Paintable[DataLines[X, Y, D]] = new Paintable[DataLines[X, Y, D]] {

    def paint(dataLines: DataLines[X, Y, D], g2d: Graphics2D): Unit = {

      import dataLines._

      data.zip(colorStream) foreach {
        case (((label, d), color)) =>
          g2d.setColor(color)
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

  implicit def paintHorizontalLine[X, Y]: Paintable[HorizontalLine[X, Y]] = new Paintable[HorizontalLine[X, Y]] {

    def paint(hLine: HorizontalLine[X, Y], g2d: Graphics2D): Unit = {

      import hLine._
      import scaledArea._

      g2d.setColor(color)
      drawLine(g2d, scaledArea, Point2D(minX, h), Point2D(maxX, h))
    }

  }

  implicit def paintVerticalLine[X, Y]: Paintable[VerticalLine[X, Y]] = new Paintable[VerticalLine[X, Y]] {

    def paint(vLine: VerticalLine[X, Y], g2d: Graphics2D): Unit = {

      import vLine._
      import scaledArea._

      g2d.setColor(color)
      drawLine(g2d, scaledArea, Point2D(v, minY), Point2D(v, maxY))
    }

  }

  implicit def painText: Paintable[Text] = new Paintable[Text] {

    def paint(t: Text, g2d: Graphics2D): Unit = {

      import t._

      g2d.setColor(color)
      g2d.setFont(font)

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
          g2d.drawString(text, x - fontMetrics.stringWidth(text) / 2, y)
        } else {
          g2d.drawString(text, x, y)
        }
      }

    }

  }

  implicit def paintOval[X, Y]: Paintable[Oval[X, Y]] = new Paintable[Oval[X, Y]] {

    def paint(oval: Oval[X, Y], g2d: Graphics2D): Unit = {

      import oval._

      g2d.setColor(borderColor)
      fillOval(g2d, scaledArea, center, width, height)

      g2d.setColor(color)
      drawOval(g2d, scaledArea, center, width, height)
    }

  }

  implicit def paintRectangle[X, Y]: Paintable[Rectangle[X, Y]] = new Paintable[Rectangle[X, Y]] {

    def paint(r: Rectangle[X, Y], g2d: Graphics2D): Unit = {

      import r._

      fillColor.map(color => {
        g2d.setColor(color)
        fillRectangle(
          g2d,
          scaledArea,
          Point2D(lowerLeft.x, lowerLeft.y),
          Point2D(upperRight.x, upperRight.y))
      })
      borderColor.map(color => {
        g2d.setColor(color)
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
      g2d.setColor(color)

      val fontMetrics = g2d.getFontMetrics
      import scaledArea._
      import java.awt.Color

      tics.map({
        case (y, label) => {
          val leftScaled = Point2D(minX, y)
          val leftUnscaled = framePoint(leftScaled)
          g2d.setColor(Color.lightGray)
          drawLine(g2d, scaledArea, leftScaled, Point2D(maxX, y))
          g2d.setColor(Color.black)
          g2d.drawString(label, leftUnscaled.x - fontMetrics.stringWidth(label) - 5, leftUnscaled.y + fontMetrics.getHeight / 2)
          g2d.drawLine(leftUnscaled.x - 2, leftUnscaled.y, leftUnscaled.x + 2, leftUnscaled.y)
        }
      })
    }

  }

  implicit def paintXTics[X, Y]: Paintable[XTics[X, Y]] = new Paintable[XTics[X, Y]] {

    def paint(xt: XTics[X, Y], g2d: Graphics2D): Unit = {

      import xt._
      import scaledArea._

      g2d.setColor(color)
      g2d.setFont(font)

      val fontMetrics = g2d.getFontMetrics

      tics map {
        case (x, label) => {
          if (fDrawLines) {
            g2d.setColor(Color.lightGray)
            drawLine(g2d, scaledArea, Point2D(x, minY), Point2D(x, maxY))
          }
          val bottomScaled = Point2D(x, minY)
          val bottomUnscaled = framePoint(bottomScaled)
          g2d.setColor(Color.black)

          // TODO: angle xtics?
          if (angle === zeroDegrees) {
            g2d.drawString(label, bottomUnscaled.x - fontMetrics.stringWidth(label) / 2, bottomUnscaled.y + fontMetrics.getHeight)
          } else {
            drawStringAtAngle(g2d, scaledArea, fontMetrics, label, bottomScaled, angle)
          }

          g2d.drawLine(bottomUnscaled.x, bottomUnscaled.y - 2, bottomUnscaled.x, bottomUnscaled.y + 2)
        }
      }

    }

  }

  implicit def paintKey[X, Y, D]: Paintable[Key[X, Y, D]] = new Paintable[Key[X, Y, D]] {

    def paint(key: Key[X, Y, D], g2d: Graphics2D): Unit = {

      import key._

      g2d.setFont(font)
      val fontMetrics = g2d.getFontMetrics

      val lineHeight = g2d.getFontMetrics.getHeight
      data.zip(colorStream).zipWithIndex foreach {
        case (((label, _), color), i) =>
          g2d.setColor(color)
          g2d.drawString(label, plot.width - width, topPadding + lineHeight * (i + 1))
      }
    }

  }

  implicit def paintBarChartKey[X: Show, Y, D]: Paintable[BarChartKey[X, Y, D]] = new Paintable[BarChartKey[X, Y, D]] {

    def paint(key: BarChartKey[X, Y, D], g2d: Graphics2D): Unit = {

      import key._
      import chart._

      g2d.setFont(font)
      val lineHeight = g2d.getFontMetrics.getHeight
      slices.toVector.zipWithIndex.zip(colorStream) foreach {
        case ((s, j), color) =>
          g2d.setColor(color)
          g2d.drawString(string(s), width - keyWidth, keyTopPadding + lineHeight * (j + 1))
      }
    }

  }

  implicit def paintBarChartGroupedKey[G: Show, S: Show, Y, D]: Paintable[BarChartGroupedKey[G, S, Y, D]] = new Paintable[BarChartGroupedKey[G, S, Y, D]] {

    def paint(key: BarChartGroupedKey[G, S, Y, D], g2d: Graphics2D): Unit = {

      import key._
      import chart._

      g2d.setFont(font)
      val lineHeight = g2d.getFontMetrics.getHeight
      slices.toVector.zipWithIndex.zip(colorStream) foreach {
        case ((s, j), color) =>
          g2d.setColor(color)
          g2d.drawString(string(s), width - keyWidth, keyTopPadding + lineHeight * (j + 1))
      }
    }

  }

  def fillOval[X, Y](g2d: Graphics2D, scaledArea2D: ScaledArea2D[X, Y], p: Point2D[X, Y], width: Int, height: Int): Unit = {
    if (scaledArea2D.nonZeroArea) {
      val fp = scaledArea2D.framePoint(p)
      g2d.fillOval(fp.x - width / 2, fp.y - height / 2, width, height)
    }
  }

  def drawOval[X, Y](g2d: Graphics2D, scaledArea2D: ScaledArea2D[X, Y], p: Point2D[X, Y], width: Int, height: Int): Unit = {
    if (scaledArea2D.nonZeroArea) {
      val fp = scaledArea2D.framePoint(p)
      g2d.drawOval(fp.x - width / 2, fp.y - height / 2, width, height)
    }
  }

  def drawLine[X, Y](g2d: Graphics2D, scaledArea: ScaledArea2D[X, Y], p0: Point2D[X, Y], p1: Point2D[X, Y]): Unit = {
    if (scaledArea.nonZeroArea) {
      val fp0 = scaledArea.framePoint(p0)
      val fp1 = scaledArea.framePoint(p1)
      g2d.drawLine(fp0.x, fp0.y, fp1.x, fp1.y)
    }
  }

  def fillRectangle[X, Y](g2d: Graphics2D, scaledArea: ScaledArea2D[X, Y], p0: Point2D[X, Y], p1: Point2D[X, Y]): Unit = {
    if (scaledArea.nonZeroArea) {
      val fp0 = scaledArea.framePoint(p0)
      val fp1 = scaledArea.framePoint(p1)
      g2d.fillRect(min(fp0.x, fp1.x), min(fp0.y, fp1.y), abs(fp0.x - fp1.x), abs(fp0.y - fp1.y))
    }
  }

  def drawRectangle[X, Y](g2d: Graphics2D, scaledArea: ScaledArea2D[X, Y], p0: Point2D[X, Y], p1: Point2D[X, Y]): Unit = {
    if (scaledArea.nonZeroArea) {
      val fp0 = scaledArea.framePoint(p0)
      val fp1 = scaledArea.framePoint(p1)
      g2d.drawRect(min(fp0.x, fp1.x), min(fp0.y, fp1.y), abs(fp0.x - fp1.x), abs(fp0.y - fp1.y))
    }
  }

  def drawString[X, Y](g2d: Graphics2D, scaledArea: ScaledArea2D[X, Y], s: String, p: Point2D[X, Y]): Unit = {
    if (scaledArea.nonZeroArea) {
      val fp = scaledArea.framePoint(p)
      g2d.drawString(s, fp.x, fp.y)
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
