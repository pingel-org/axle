package axle

import java.awt.Color
import java.awt.Component
import java.io.File
import axle.graph._
import axle.visualize._
import axle.ml._
import axle.stats._
import java.awt.Font
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
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

  implicit def enComponentPlot[X, DX, Y, DY](plot: Plot[X, DX, Y, DY]): Component = new PlotComponent(plot)

  implicit def enComponentJungUndirectedGraph[VP, EP](jug: JungUndirectedGraph[VP, EP]): Component =
    new JungUndirectedGraphVisualization().component(jug)

  // TODO: Avoid very wastefully converting to Native vertices and back
  implicit def enComponentNativeUndirectedGraph[VP, EP](nug: NativeUndirectedGraph[VP, EP]): Component =
    JungUndirectedGraph[VP, EP](nug.vps,
      (vs: Seq[Vertex[VP]]) =>
        nug.ef(vs.map(v => new NativeUndirectedGraphVertex(v.payload)))
          .map({ case (nv1, nv2, ep) => (Vertex(nv1.payload), Vertex(nv2.payload), ep) })
    )

  implicit def enComponentJungDirectedGraph[VP, EP](jdg: JungDirectedGraph[VP, EP]): Component =
    new JungDirectedGraphVisualization().component(jdg)

  // TODO: Avoid very wastefully converting to Native vertices and back
  implicit def enComponentNativeDirectedGraph[VP, EP](ndg: NativeDirectedGraph[VP, EP]): Component =
    new JungDirectedGraph[VP, EP](ndg.vps,
      (vs: Seq[Vertex[VP]]) => {
        val nativeVertices = vs.map(v => Vertex(v.payload))
        val native2jungVertex = nativeVertices.zip(vs).toMap
        ndg.ef(nativeVertices)
          .map({ case (nv1, nv2, ep) => (native2jungVertex(nv1), native2jungVertex(nv2), ep) })
      }
    )

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