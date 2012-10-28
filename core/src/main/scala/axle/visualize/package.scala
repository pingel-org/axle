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

  implicit def enComponentJungUndirectedGraph[VP, EP](g: axle.graph.JungUndirectedGraph[VP, EP]): Component =
    new JungUndirectedGraphVisualization().component(g)

  implicit def enComponentNativeUndirectedGraph[VP, EP](g: axle.graph.NativeUndirectedGraph[VP, EP]): Component = {
    // TODO: remove this cast
    val asUG = this.asInstanceOf[axle.graph.JungUndirectedGraph[VP, EP]]
    val jug = axle.graph.JungUndirectedGraph[VP, EP, VP, EP](asUG)(vp => vp, ep => ep)
    jug
  }

  implicit def enComponentJungDirectedGraph[VP, EP](g: axle.graph.JungDirectedGraph[VP, EP]): Component =
    new JungDirectedGraphVisualization().component(g)

  implicit def enComponentNativeDirectedGraph[VP, EP](g: axle.graph.NativeDirectedGraph[VP, EP]): Component = {
    // TODO: remove this cast
    val asDG = g.asInstanceOf[axle.graph.JungDirectedGraph[VP, EP]]
    val jdg = axle.graph.JungDirectedGraph[VP, EP, VP, EP](asDG)(vp => vp, ep => ep)
    jdg
  }

  implicit def enComponentKMeansClassifier[T](
    classifier: axle.ml.KMeans.KMeansClassifier[T]): Component =
    new axle.visualize.KMeansVisualization[T](classifier)

}