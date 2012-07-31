package axle.stats

import axle.graph.JungDirectedGraphFactory._
import collection._

class InteractionGraph {

  val g = graph[RandomVariable[_], String]()

  def eliminationSequence(π: List[RandomVariable[_]]): List[InteractionGraph] = {

    var G: InteractionGraph = this
    val result = mutable.ListBuffer(G)

    π.map(rv => {
      val newG = new InteractionGraph()
      G.copyTo(newG)
      newG.eliminate(rv)
      result += newG
      G = newG
    })

    result.toList
  }

}
