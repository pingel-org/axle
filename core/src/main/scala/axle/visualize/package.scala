package axle

import java.awt.Color
import java.awt.Component

import axle.visualize.JungDirectedGraphVisualization
import axle.visualize.JungUndirectedGraphVisualization

package object visualize {

  def newFrame() = new axle.visualize.AxleFrame(
    width = 1100,
    height = 800,
    bgColor = Color.white,
    title = "αχλε")

  def show(component: Component) = newFrame().add(component)

  implicit def enComponentPlot[X, DX, Y, DY](plot: Plot[X, DX, Y, DY]): Component = new PlotComponent(plot)

  implicit def enComponentJungUndirectedGraph[VP, EP](g: axle.graph.JungUndirectedGraphFactory.JungUndirectedGraph[VP, EP]): Component =
    new JungUndirectedGraphVisualization().component(g)

  implicit def enComponentNativeUndirectedGraph[VP, EP](g: axle.graph.NativeUndirectedGraphFactory.NativeUndirectedGraph[VP, EP]): Component = {
    // TODO: remove this cast
    val asUG = this.asInstanceOf[axle.graph.JungUndirectedGraphFactory.UndirectedGraph[VP, EP]]
    val jug = axle.graph.JungUndirectedGraphFactory.graphFrom[VP, EP, VP, EP](asUG)(vp => vp, ep => ep)
    jug
  }

  implicit def enComponentJungDirectedGraph[VP, EP](g: axle.graph.JungDirectedGraphFactory.JungDirectedGraph[VP, EP]): Component =
    new JungDirectedGraphVisualization().component(g)

  implicit def enComponentNativeDirectedGraph[VP, EP](g: axle.graph.NativeDirectedGraphFactory.NativeDirectedGraph[VP, EP]): Component = {
    // TODO: remove this cast
    val asDG = g.asInstanceOf[axle.graph.JungDirectedGraphFactory.DirectedGraph[VP, EP]]
    val jdg = axle.graph.JungDirectedGraphFactory.graphFrom[VP, EP, VP, EP](asDG)(vp => vp, ep => ep)
    jdg
  }

  implicit def enComponentKMeansClassifier[T](
    classifier: axle.ml.KMeans.KMeansClassifier[T]): Component =
    new axle.visualize.KMeansVisualization[T](classifier)

}