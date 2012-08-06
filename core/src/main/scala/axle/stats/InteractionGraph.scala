package axle.stats

import axle.graph.JungDirectedGraphFactory._
import collection._

class InteractionGraph(g: JungDirectedGraph[RandomVariable[_], String] = graph[RandomVariable[_], String]()) {

  def getGraph() = g

  // val g = graph[RandomVariable[_], String]()

  def eliminate(rv: RandomVariable[_]): InteractionGraph = {
    "TODO"
  }

  def eliminationSequence(π: List[RandomVariable[_]]): List[InteractionGraph] =
    π.scanLeft(this)((G, rv) => G.eliminate(rv))

}
