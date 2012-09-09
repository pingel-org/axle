package axle.stats

import axle.graph.JungUndirectedGraphFactory._
import collection._

class InteractionGraph()
  extends JungUndirectedGraph[RandomVariable[_], String] {

  def eliminate(rv: RandomVariable[_]): InteractionGraph = null.asInstanceOf[InteractionGraph] // TODO

  def eliminationSequence(π: List[RandomVariable[_]]): List[InteractionGraph] =
    π.scanLeft(this)((G, rv) => G.eliminate(rv))

}
