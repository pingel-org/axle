
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

import axle.graph._
import axle.visualize._
import axle.ml._
import axle.stats._
import axle.pgm._
import axle.algebra.Plottable
import axle.algebra.Tics
import axle.algebra.LengthSpace
import axle.algebra.Zero

package object visualize {

  // default width/height was 1100/800

  def newFrame(width: Int, height: Int): AxleFrame =
    new AxleFrame(width, height, Color.white, "αχλε")

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

  def play[T: Draw: Fed](t: T, refreshFn: T => T, interval: UnittedQuantity[Time, Double])(implicit system: ActorSystem): ActorRef = {

    val drawer = implicitly[Draw[T]]

    drawer.component(t) match {
      // TODO reorganize this
      case fedComponent: Component with Fed[T] => {
        val minSize = fedComponent.getMinimumSize
        val frame = newFrame(minSize.width, minSize.height)
        val feeder = fedComponent.setFeeder(refreshFn, interval, system)
        //system.actorOf(Props(classOf[FrameRepaintingActor], frame, component.feeder.get))
        frame.initialize()
        val rc = frame.add(fedComponent)
        rc.setVisible(true)
        frame.setVisible(true)
        feeder
      }
      case _ => {
        draw(t)
        null // TODO re-org
      }
    }
  }

  implicit def drawUndirectedGraph[VP: Manifest: Eq, EP: Eq]: Draw[UndirectedGraph[VP, EP]] =
    new Draw[UndirectedGraph[VP, EP]] {

      def component(ug: UndirectedGraph[VP, EP]) = ug match {
        case jug: JungUndirectedGraph[VP, EP] => new JungUndirectedGraphVisualization().component(jug)
        case _ => new JungUndirectedGraphVisualization().component(JungUndirectedGraph(ug.vertexPayloads, ug.edgeFunction))
      }
    }

  implicit def drawDirectedGraph[VP: Manifest: Eq, EP: Eq]: Draw[DirectedGraph[VP, EP]] =
    new Draw[DirectedGraph[VP, EP]] {

      def component(dg: DirectedGraph[VP, EP]) = dg match {
        case jdg: JungDirectedGraph[VP, EP] => new JungDirectedGraphVisualization().component(jdg)
        case _ => new JungDirectedGraphVisualization().component(JungDirectedGraph(dg.vertexPayloads, dg.edgeFunction))
      }
    }

  trait BayesianNetworkVisualizationModule extends BayesianNetworkModule {

    implicit def drawBayesianNetwork[T: Manifest: Eq, N: Field: Manifest: Eq]: Draw[BayesianNetwork[T, N]] = {
      new Draw[BayesianNetwork[T, N]] {
        def component(bn: BayesianNetwork[T, N]) =
          drawDirectedGraph[BayesianNetworkNode[T, N], String].component(bn.graph)
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
