package axle

import java.awt.Color
import java.awt.Component

import axle.visualize.JungDirectedGraphVisualization
import axle.visualize.JungUndirectedGraphVisualization
import javax.swing.JPanel

package object visualize {

  def newFrame() = new axle.visualize.AxleFrame(
    width = 1100,
    height = 800,
    bgColor = Color.white,
    title = "αχλε")

  def show(component: Component) = newFrame().add(component)

  def show(panel: JPanel) = newFrame().add(panel)

  implicit def enComponentKMeansClassifier[T](classifier: axle.ml.KMeans.KMeansClassifier[T]): Component =
    new axle.visualize.KMeansVisualization[T](classifier)

  implicit def enComponentJungUndirectedGraph[VP, EP](g: axle.graph.JungUndirectedGraphFactory.JungUndirectedGraph[VP, EP]): Component = 
    new JungUndirectedGraphVisualization().component(g)

  implicit def enComponentJungDirectedGraph[VP, EP](g: axle.graph.JungDirectedGraphFactory.JungDirectedGraph[VP, EP]): Component = 
    new JungDirectedGraphVisualization().component(g)

}