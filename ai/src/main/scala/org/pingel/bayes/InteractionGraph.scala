package org.pingel.bayes

import org.pingel.axle.graph.JungDirectedGraphFactory._
import scala.collection._

class InteractionGraph {

  val g = graph[RandomVariable, String]()
  
  def eliminationSequence(π: List[RandomVariable]): List[InteractionGraph] = {

    var G: InteractionGraph = this
    val result = List(G)

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
