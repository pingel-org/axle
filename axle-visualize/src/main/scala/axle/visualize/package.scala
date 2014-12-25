
package axle

import scala.reflect.ClassTag

import akka.actor.ActorSystem

import java.awt.Color
import java.awt.Component
import java.awt.Font
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JPanel
import javax.swing.CellRendererPane
import axle.quanta.Time
import axle.quanta.UnittedQuantity

import spire.algebra._

import akka.actor.ActorRef
import akka.actor.Props

import axle.visualize._
import axle.ml._
import axle.stats._
import axle.pgm._
import axle.jung._
import axle.algebra.Plottable
import axle.algebra.Tics
import axle.algebra.LengthSpace
import axle.algebra.Zero
import axle.algebra.DirectedGraph

package object visualize {

  // default width/height was 1100/800

  def newFrame(width: Int, height: Int): AxleFrame =
    AxleFrame(width, height, Color.white, "αχλε")

  def draw[T: Draw](t: T): Unit = {
    val draw = implicitly[Draw[T]]
    val component = draw.component(t)
    val minSize = component.getMinimumSize
    val frame = newFrame(minSize.width, minSize.height)
    frame.initialize()
    val rc = frame.add(component)
    rc.setVisible(true)
    frame.setVisible(true)
  }

  def play[T: Draw, D](
    t: T,
    f: D => D,
    interval: UnittedQuantity[Time.type, Double])(implicit system: ActorSystem): ActorRef = {

    val draw = implicitly[Draw[T]]
    draw.component(t) match {
      case fed: Component with Fed[D] => {
        val minSize = fed.getMinimumSize
        val frame = newFrame(minSize.width, minSize.height)
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

  implicit def drawBayesianNetwork[T: Manifest: Eq, N: Field: Manifest: Eq, DG[_, _]: DirectedGraph]: Draw[BayesianNetwork[T, N, DG]] = {
    new Draw[BayesianNetwork[T, N, DG]] {
      def component(bn: BayesianNetwork[T, N, DG]) = {
        // TODO this should be easier
        val jdg = implicitly[DirectedGraph[JungDirectedGraph]].make[BayesianNetworkNode[T, N], String](???, ???) // bn.graph.v.vertexPayloads, bn.graph.edgeFunction)
        drawJungDirectedGraph[BayesianNetworkNode[T, N], String].component(jdg)
      }
    }
  }

  /**
   * component2file
   *
   * encoding: PNG, JPEG, gif, BMP
   *
   * http://stackoverflow.com/questions/4028898/create-an-image-from-a-non-visible-awt-component
   */

  def draw2file[T: Draw](t: T, filename: String, encoding: String): Unit = {

    val component = implicitly[Draw[T]].component(t)

    val minSize = component.getMinimumSize
    val frame = newFrame(minSize.width, minSize.height)
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

}
