package axle.stats

import axle.graph._
import collection._

object InteractionGraph {

  def apply(
    vps: Seq[RandomVariable[_]],
    ef: Seq[JungUndirectedGraphVertex[RandomVariable[_]]] => Seq[(JungUndirectedGraphVertex[RandomVariable[_]], JungUndirectedGraphVertex[RandomVariable[_]], String)]): InteractionGraph =
    new InteractionGraph(JungUndirectedGraph(vps, ef))

}

case class InteractionGraph(graph: UndirectedGraph[RandomVariable[_], String]) {

  def eliminate(rv: RandomVariable[_]): InteractionGraph = null.asInstanceOf[InteractionGraph] // TODO

  def eliminationSequence(π: List[RandomVariable[_]]): List[InteractionGraph] =
    π.scanLeft(this)((G, rv) => G.eliminate(rv))

}
