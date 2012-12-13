package axle.stats

import axle.graph._
import collection._

object InteractionGraph {

  def apply(
    vps: Seq[RandomVariable[_]],
    ef: Seq[JungUndirectedGraphVertex[RandomVariable[_]]] => Seq[(JungUndirectedGraphVertex[RandomVariable[_]], JungUndirectedGraphVertex[RandomVariable[_]], String)]): InteractionGraph =
    InteractionGraph(vps, ef)

}

class InteractionGraph(
  vps: Seq[RandomVariable[_]],
  ef: Seq[JungUndirectedGraphVertex[RandomVariable[_]]] => Seq[(JungUndirectedGraphVertex[RandomVariable[_]], JungUndirectedGraphVertex[RandomVariable[_]], String)]) {

  val graph = JungUndirectedGraph(vps, ef)
  
  def eliminate(rv: RandomVariable[_]): InteractionGraph = null.asInstanceOf[InteractionGraph] // TODO

  def eliminationSequence(π: List[RandomVariable[_]]): List[InteractionGraph] =
    π.scanLeft(this)((G, rv) => G.eliminate(rv))

}
