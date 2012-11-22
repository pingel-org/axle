package axle

import java.awt.Color
import java.awt.Component
//import axle.graph._
import axle.graph.JungUndirectedGraph._
import axle.graph.JungDirectedGraph._
import axle.graph.NativeUndirectedGraph._
import axle.graph.NativeDirectedGraph._
import axle.visualize._
import axle.ml._

package object visualize {

  def newFrame() = new AxleFrame(width = 1100, height = 800, bgColor = Color.white, title = "αχλε")

  def show(component: Component) = newFrame().add(component)

  implicit def enComponentPlot[X, DX, Y, DY](plot: Plot[X, DX, Y, DY]): Component = new PlotComponent(plot)

  implicit def enComponentJungUndirectedGraph[VP, EP](jug: JungUndirectedGraph[VP, EP]): Component =
    new JungUndirectedGraphVisualization().component(jug)

  implicit def enComponentNativeUndirectedGraph[VP, EP](nug: NativeUndirectedGraph[VP, EP]): Component = {

    // TODO: Avoid very wastefully converting to Native vertices and back
    val wrappedEf = (vs: Seq[JungUndirectedGraphVertex[VP]]) =>
      nug.ef(vs.map(v => new NativeUndirectedGraphVertex(v.payload)))
        .map({ case (nv1, nv2, ep) => (new JungUndirectedGraphVertex(nv1.payload), new JungUndirectedGraphVertex(nv2.payload), ep) })

    JungUndirectedGraph[VP, EP](nug.vps, wrappedEf)
  }

  implicit def enComponentJungDirectedGraph[VP, EP](jdg: JungDirectedGraph[VP, EP]): Component =
    new JungDirectedGraphVisualization().component(jdg)

  implicit def enComponentNativeDirectedGraph[VP, EP](ndg: NativeDirectedGraph[VP, EP]): Component = {

    // TODO: Avoid very wastefully converting to Native vertices and back
    val wrappedEf = (vs: Seq[JungDirectedGraphVertex[VP]]) =>
      ndg.ef(vs.map(v => new NativeDirectedGraphVertex(v.payload)))
        .map({ case (nv1, nv2, ep) => (new JungDirectedGraphVertex(nv1.payload), new JungDirectedGraphVertex(nv2.payload), ep) })

    new JungDirectedGraph[VP, EP](ndg.vps, wrappedEf)
  }

  implicit def enComponentKMeansClassifier[T](
    classifier: KMeans.KMeansClassifier[T]): Component =
    new KMeansVisualization[T](classifier)

}