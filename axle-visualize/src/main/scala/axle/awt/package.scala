
package axle

import spire.math.abs
import spire.math.min
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Color
import java.awt.Component
import java.awt.Graphics2D
import java.io.File
import axle.visualize.ScaledArea2D
import scala.reflect.ClassTag
import axle.algebra.DirectedGraph
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
import axle.visualize.ScatterPlot
import axle.visualize.Point2D
import axle.visualize.element.BarChartGroupedKey
import axle.visualize.element.BarChartKey
import axle.visualize.element.DataLines
import axle.visualize.element.DataPoints
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
import spire.implicits.DoubleAlgebra
import spire.implicits.eqOps
import edu.uci.ics.jung.graph.DirectedSparseGraph
import edu.uci.ics.jung.graph.UndirectedSparseGraph

import akka.stream.scaladsl.Source
import akka.stream.scaladsl.Sink
import akka.stream.ActorMaterializer
import scala.concurrent.duration.FiniteDuration
import java.util.concurrent.TimeUnit
import akka.actor.Cancellable
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props

package object awt {

  def draw2[T](x: T, f: T => T, i: Long): Unit = {

    implicit val system = ActorSystem("draw2")
    implicit val materializer = ActorMaterializer()

    val xs: Source[T, Cancellable] = {
      // apply f to x every i ms
      Source.tick(FiniteDuration(0, TimeUnit.MILLISECONDS), FiniteDuration(i, TimeUnit.MILLISECONDS), x)
    }

    // see https://groups.google.com/forum/#!topic/akka-user/swhrgX6YobM

    val rc = xs.runWith(Sink.foreach(println))
  }

  // draw2[Int](3, _ + 1, 700)

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

  def play[T: Draw, D: ClassTag](
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

  implicit def drawPlot[X, Y, D]: Draw[Plot[X, Y, D]] =
    new Draw[Plot[X, Y, D]] {
      def component(plot: Plot[X, Y, D]) = PlotComponent(plot)
    }

  implicit def drawScatterPlot[X, Y, D: ClassTag]: Draw[ScatterPlot[X, Y, D]] =
    new Draw[ScatterPlot[X, Y, D]] {
      def component(plot: ScatterPlot[X, Y, D]) = ScatterPlotComponent(plot)
    }

  implicit def drawBarChart[S, Y, D: ClassTag]: Draw[BarChart[S, Y, D]] =
    new Draw[BarChart[S, Y, D]] {
      def component(chart: BarChart[S, Y, D]) = BarChartComponent(chart)
    }

  implicit def drawBarChartGrouped[G, S, Y, D: ClassTag]: Draw[BarChartGrouped[G, S, Y, D]] =
    new Draw[BarChartGrouped[G, S, Y, D]] {
      def component(chart: BarChartGrouped[G, S, Y, D]) = BarChartGroupedComponent(chart)
    }

  implicit def drawJungUndirectedGraph[VP: Show, EP: Show]: Draw[UndirectedSparseGraph[VP, EP]] =
    new Draw[UndirectedSparseGraph[VP, EP]] {
      def component(jug: UndirectedSparseGraph[VP, EP]) =
        JungUndirectedGraphVisualization().component(jug)
    }

  implicit def drawJungDirectedGraph[VP: HtmlFrom, EP: Show]: Draw[DirectedSparseGraph[VP, EP]] =
    new Draw[DirectedSparseGraph[VP, EP]] {
      def component(jdg: DirectedSparseGraph[VP, EP]) =
        JungDirectedGraphVisualization().component(jdg)
    }

  implicit def drawBayesianNetwork[T: Manifest: Eq, N: Field: Manifest: Eq, DG](
    implicit drawDG: Draw[DG], dg: DirectedGraph[DG, BayesianNetworkNode[T, N], axle.pgm.Edge]): Draw[BayesianNetwork[T, N, DG]] = {
    new Draw[BayesianNetwork[T, N, DG]] {
      def component(bn: BayesianNetwork[T, N, DG]) =
        drawDG.component(bn.graph)
    }
  }

  implicit def drawKMeansClasifier[T, F, G, M]: Draw[KMeans[T, F, G, M]] =
    new Draw[KMeans[T, F, G, M]] {
      def component(kmc: KMeans[T, F, G, M]) = KMeansComponent(KMeansVisualization(kmc))
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

  implicit def paintDataLines[X, Y, D]: Paintable[DataLines[X, Y, D]] = new Paintable[DataLines[X, Y, D]] {

    def paint(dataLines: DataLines[X, Y, D], g2d: Graphics2D): Unit = {

      import dataLines._

      data.zip(colorStream) foreach {
        case (((label, d), color)) =>
          g2d.setColor(cachedColor(color))
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

  implicit def paintDataPoints[X, Y, D]: Paintable[DataPoints[X, Y, D]] = new Paintable[DataPoints[X, Y, D]] {

    def paint(dataPoints: DataPoints[X, Y, D], g2d: Graphics2D): Unit = {

      import dataPoints._

      val domain = dataView.dataToDomain(data)

      if (pointDiameter.toInt > 0) {
        domain foreach {
          case (x, y) => {
            g2d.setColor(cachedColor(dataView.colorOf(data, x, y)))
            fillOval(g2d, scaledArea, Point2D(x, y), pointDiameter.toInt, pointDiameter.toInt)
          }
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
          if (angle === zeroDegrees) {
            g2d.drawString(label, (bottomUnscaled.x - fontMetrics.stringWidth(label) / 2).toInt, bottomUnscaled.y.toInt + fontMetrics.getHeight)
          } else {
            drawStringAtAngle(g2d, scaledArea, fontMetrics, label, bottomScaled, angle)
          }

          g2d.drawLine(bottomUnscaled.x.toInt, (bottomUnscaled.y - 2).toInt, bottomUnscaled.x.toInt, (bottomUnscaled.y + 2).toInt)
        }
      }

    }

  }

  implicit def paintKey[X, Y, D]: Paintable[Key[X, Y, D]] = new Paintable[Key[X, Y, D]] {

    def paint(key: Key[X, Y, D], g2d: Graphics2D): Unit = {

      import key._

      g2d.setFont(cachedFont(plot.fontName, plot.fontSize, plot.bold))
      val fontMetrics = g2d.getFontMetrics

      val lineHeight = g2d.getFontMetrics.getHeight
      data.zip(colorStream).zipWithIndex foreach {
        case (((label, _), color), i) =>
          g2d.setColor(cachedColor(color))
          g2d.drawString(label, plot.width - width, topPadding + lineHeight * (i + 1))
      }
    }

  }

  implicit def paintBarChartKey[X, Y, D]: Paintable[BarChartKey[X, Y, D]] =
    new Paintable[BarChartKey[X, Y, D]] {

      def paint(key: BarChartKey[X, Y, D], g2d: Graphics2D): Unit = {

        import key._
        import chart._

        g2d.setFont(cachedFont(chart.normalFontName, chart.normalFontSize, true))
        val lineHeight = g2d.getFontMetrics.getHeight
        slices.toVector.zipWithIndex.zip(colorStream) foreach {
          case ((s, j), color) =>
            g2d.setColor(cachedColor(color))
            g2d.drawString(string(s), width - keyWidth, keyTopPadding + lineHeight * (j + 1))
        }
      }

    }

  implicit def paintBarChartGroupedKey[G, S, Y, D]: Paintable[BarChartGroupedKey[G, S, Y, D]] =
    new Paintable[BarChartGroupedKey[G, S, Y, D]] {

      def paint(key: BarChartGroupedKey[G, S, Y, D], g2d: Graphics2D): Unit = {

        import key._
        import chart._

        g2d.setFont(cachedFont(chart.normalFontName, chart.normalFontSize, true))
        val lineHeight = g2d.getFontMetrics.getHeight
        slices.toVector.zipWithIndex.zip(colorStream) foreach {
          case ((s, j), color) =>
            g2d.setColor(cachedColor(color))
            g2d.drawString(string(s), width - keyWidth, keyTopPadding + lineHeight * (j + 1))
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
