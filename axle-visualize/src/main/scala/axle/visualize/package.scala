
package axle

import java.awt.Color
import java.awt.Component
import java.awt.Font
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.swing.JPanel
import javax.swing.CellRendererPane
import java.io.File
import spire.algebra._
import akka.actor.ActorRef
import axle.graph._
import axle.visualize._
import axle.ml._
import axle.stats._
import axle.pgm._
import axle.algebra.Plottable

package object visualize {

  // default width/height was 1100/800

  def newFrame(width: Int, height: Int, dataFeedActorOpt: Option[ActorRef]): AxleFrame =
    new AxleFrame(width, height, Color.white, "αχλε", dataFeedActorOpt)

  def show(component: Component): Unit = {
    val minSize = component.getMinimumSize
    val dataFeedActorOpt = component match {
      case f: Fed => Some(f.feeder)
      case _ => None
    }
    val frame = newFrame(minSize.width, minSize.height, dataFeedActorOpt)
    frame.initialize()
    val rc = frame.add(component)
    rc.setVisible(true)
    frame.setVisible(true)
  }

  implicit def enComponentPlot[X: Plottable, Y: Plottable](plot: Plot[X, Y]): Component = new PlotComponent(plot)

  implicit def enComponentBarChart[S, Y: Plottable](barChart: BarChart[S, Y]): Component = new BarChartComponent(barChart)

  implicit def enComponentBarChartGrouped[G, S, Y: Plottable](barChart: BarChartGrouped[G, S, Y]): Component = new BarChartGroupedComponent(barChart)

  implicit def enComponentUndirectedGraph[VP: Manifest: Eq, EP: Eq](ug: UndirectedGraph[VP, EP]): Component = ug match {
    case jug: JungUndirectedGraph[VP, EP] => new JungUndirectedGraphVisualization().component(jug)
    case _ => new JungUndirectedGraphVisualization().component(JungUndirectedGraph(ug.vertexPayloads, ug.edgeFunction))
  }

  implicit def enComponentDirectedGraph[VP: Manifest: Eq, EP: Eq](dg: DirectedGraph[VP, EP]): Component = dg match {
    case jdg: JungDirectedGraph[VP, EP] => new JungDirectedGraphVisualization().component(jdg)
    case _ => new JungDirectedGraphVisualization().component(JungDirectedGraph(dg.vertexPayloads, dg.edgeFunction))
  }

  import BayesianNetworkModule._

  implicit def enComponentBayesianNetwork[T: Manifest: Eq](bn: BayesianNetworkModule.BayesianNetwork[T]): Component =
    enComponentDirectedGraph(bn.graph)

  implicit def enComponentKMeansClassifier[T](classifier: KMeansModule.KMeansClassifier[T]): Component =
    new KMeansVisualizationModule.KMeansVisualization[T](classifier)

  /**
   * component2file
   *
   * encoding: PNG, JPEG, gif, BMP
   *
   * http://stackoverflow.com/questions/4028898/create-an-image-from-a-non-visible-awt-component
   */

  def component2file(component: Component, filename: String, encoding: String): Unit = {

    val minSize = component.getMinimumSize
    val frame = newFrame(minSize.width, minSize.height, None)
    frame.setUndecorated(true)
    frame.initialize()
    val rc = frame.add(component)
    // rc.setVisible(true)
    frame.setVisible(true)

    val img = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB) // ARGB
    val g = img.createGraphics()
    frame.paintAll(g)

    ImageIO.write(img, encoding, new File(filename))

    g.dispose()
  }

  def png(component: Component, filename: String): Unit = component2file(component, filename, "PNG")

  def jpeg(component: Component, filename: String): Unit = component2file(component, filename, "JPEG")

  def gif(component: Component, filename: String): Unit = component2file(component, filename, "gif")

  def bmp(component: Component, filename: String): Unit = component2file(component, filename, "BMP")

}
