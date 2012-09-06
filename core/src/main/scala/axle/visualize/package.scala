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

  implicit def enComponentKMeansClassifier[T](classifier: axle.ml.KMeans.KMeansClassifier[T]): Component =
    new axle.visualize.KMeansVisualization[T](classifier)

  implicit def enComponentJungUndirectedGraph[VP, EP](g: axle.graph.JungUndirectedGraphFactory.JungUndirectedGraph[VP, EP]): Component = 
    new JungUndirectedGraphVisualization().component(g)

  implicit def enComponentJungDirectedGraph[VP, EP](g: axle.graph.JungDirectedGraphFactory.JungDirectedGraph[VP, EP]): Component = 
    new JungDirectedGraphVisualization().component(g)

  implicit def enComponentPlot[X, DX, Y, DY](plot: Plot[X, DX, Y, DY]): Component = new PlotComponent(plot)
  
}