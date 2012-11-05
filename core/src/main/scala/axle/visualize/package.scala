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

  implicit def enComponentJungUndirectedGraph[VP, EP](g: JungUndirectedGraph[VP, EP]): Component =
    new JungUndirectedGraphVisualization().component(g)

  // TODO: remove this cast
  implicit def enComponentNativeUndirectedGraph[VP, EP](g: NativeUndirectedGraph[VP, EP]): Component =
    JungUndirectedGraph(this.asInstanceOf[JungUndirectedGraph[VP, EP]])(vp => vp, ep => ep)

  implicit def enComponentJungDirectedGraph[VP, EP](g: JungDirectedGraph[VP, EP]): Component =
    new JungDirectedGraphVisualization().component(g)

  // TODO: remove this cast
  implicit def enComponentNativeDirectedGraph[VP, EP](g: NativeDirectedGraph[VP, EP]): Component =
    JungDirectedGraph(g.asInstanceOf[JungDirectedGraph[VP, EP]])(vp => vp, ep => ep)

  implicit def enComponentKMeansClassifier[T](
    classifier: KMeans.KMeansClassifier[T]): Component =
    new KMeansVisualization[T](classifier)

}