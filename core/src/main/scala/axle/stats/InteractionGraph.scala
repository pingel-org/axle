package axle.stats

import axle.graph._
import collection._

trait InteractionGraphFactory extends JungUndirectedGraphFactory {

  def apply(): InteractionGraph = new InteractionGraph() {}

  def apply(
    vps: Seq[RandomVariable[_]],
    ef: Seq[JungDirectedGraphVertex[RandomVariable[_]]] => Seq[(JungDirectedGraphVertex[RandomVariable[_]], JungDirectedGraphVertex[RandomVariable[_]], String)]): InteractionGraph = {
    4
  }

}

object InteractionGraph extends InteractionGraphFactory

class InteractionGraph() extends JungUndirectedGraph[RandomVariable[_], String] {

  def eliminate(rv: RandomVariable[_]): InteractionGraph = null.asInstanceOf[InteractionGraph] // TODO

  def eliminationSequence(π: List[RandomVariable[_]]): List[InteractionGraph] =
    π.scanLeft(this)((G, rv) => G.eliminate(rv))

}
