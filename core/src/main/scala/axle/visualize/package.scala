package axle

import java.awt.Color
import java.awt.Component
import axle.graph._
import axle.visualize._
import axle.ml._

package object visualize {

  def newFrame() = new AxleFrame(width = 1100, height = 800, bgColor = Color.white, title = "αχλε")

  def show(component: Component) = newFrame().add(component)

  implicit def enComponentPlot[X, DX, Y, DY](plot: Plot[X, DX, Y, DY]): Component = new PlotComponent(plot)

  implicit def enComponentJungUndirectedGraph[VP, EP](jug: JungUndirectedGraph[VP, EP]): Component =
    new JungUndirectedGraphVisualization().component(jug)

  implicit def enComponentNativeUndirectedGraph[VP, EP](nug: NativeUndirectedGraph[VP, EP]): Component =
    JungUndirectedGraph(nug.vps, nug.ef)

  implicit def enComponentJungDirectedGraph[VP, EP](jdg: JungDirectedGraph[VP, EP]): Component =
    new JungDirectedGraphVisualization().component(jdg)

  implicit def enComponentNativeDirectedGraph[VP, EP](ndg: NativeDirectedGraph[VP, EP]): Component =
    JungDirectedGraph(ndg.vps, ndg.ef)

  implicit def enComponentKMeansClassifier[T](
    classifier: KMeans.KMeansClassifier[T]): Component =
    new KMeansVisualization[T](classifier)

}