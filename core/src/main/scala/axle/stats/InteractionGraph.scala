package axle.stats

import axle.graph._
import collection._

//object InteractionGraph {
//  def apply(
//    vps: Seq[RandomVariable[_]],
//    ef: Seq[UndirectedGraphVertex[RandomVariable[_]]] => Seq[(UndirectedGraphVertex[RandomVariable[_]], UndirectedGraphVertex[RandomVariable[_]], String)]): InteractionGraph =
//    new InteractionGraph(JungUndirectedGraph(vps, ef))
//}

case class InteractionGraph(
  vps: Seq[RandomVariable[_]],
  ef: Seq[UndirectedGraphVertex[RandomVariable[_]]] => Seq[(UndirectedGraphVertex[RandomVariable[_]], UndirectedGraphVertex[RandomVariable[_]], String)]) {

  lazy val graph = JungUndirectedGraph(vps, ef)

  def eliminate(rv: RandomVariable[_]): InteractionGraph = null.asInstanceOf[InteractionGraph] // TODO

  def eliminationSequence(π: List[RandomVariable[_]]): List[InteractionGraph] =
    π.scanLeft(this)((G, rv) => G.eliminate(rv))

}
