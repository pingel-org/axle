package axle

import java.awt.Color
import java.awt.Component
import java.io.File
import axle.graph._
import axle.visualize._
import axle.ml._
import axle.stats._
import java.awt.Font
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.swing.JPanel
import javax.swing.CellRendererPane

package object visualize {

  def newFrame() = new AxleFrame(width = 1100, height = 800, bgColor = Color.white, title = "αχλε")

  def show(component: Component) = {
    val frame = newFrame()
    frame.initialize()
    val rc = frame.add(component)
    rc.setVisible(true)
    frame.setVisible(true)
  }

  implicit def enComponentPlot[X, Y](plot: Plot[X, Y]): Component = new PlotComponent(plot)

  implicit def enComponentBarChart[X, S, Y](barChart: BarChart[X, S, Y]): Component = new BarChartComponent(barChart)
  
  implicit def enComponentJungUndirectedGraph[VP, EP](jug: JungUndirectedGraph[VP, EP]): Component =
    new JungUndirectedGraphVisualization().component(jug)

  implicit def enComponentNativeUndirectedGraph[VP, EP](nug: NativeUndirectedGraph[VP, EP]): Component =
    JungUndirectedGraph[VP, EP](nug.vps, nug.ef)

  implicit def enComponentJungDirectedGraph[VP, EP](jdg: JungDirectedGraph[VP, EP]): Component =
    new JungDirectedGraphVisualization().component(jdg)

  implicit def enComponentNativeDirectedGraph[VP, EP](ndg: NativeDirectedGraph[VP, EP]): Component =
    new JungDirectedGraph[VP, EP](ndg.vps, ndg.ef)

  implicit def enComponentBayesianNetwork(bn: BayesianNetwork): Component = bn.graph match {
    case jdg: JungDirectedGraph[_, _] => jdg
    case ndg: NativeDirectedGraph[_, _] => ndg
    case _ => null
  }

  implicit def enComponentKMeansClassifier[T](
    classifier: KMeans.KMeansClassifier[T]): Component =
    new KMeansVisualization[T](classifier)

  /**
   * component2file
   *
   * encoding: PNG, JPEG, gif, BMP
   *
   * http://stackoverflow.com/questions/4028898/create-an-image-from-a-non-visible-awt-component
   */

  def component2file(component: Component, filename: String, encoding: String): Unit = {

    val frame = newFrame()
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

  def png(component: Component, filename: String) = component2file(component, filename, "PNG")

  def jpeg(component: Component, filename: String) = component2file(component, filename, "JPEG")

  def gif(component: Component, filename: String) = component2file(component, filename, "gif")

  def bmp(component: Component, filename: String) = component2file(component, filename, "BMP")

}