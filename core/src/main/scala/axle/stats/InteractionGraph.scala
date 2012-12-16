package axle.stats

import axle.graph._
import collection._

case class InteractionGraph(
  vps: Seq[RandomVariable[_]],
  ef: Seq[Vertex[RandomVariable[_]]] => Seq[(Vertex[RandomVariable[_]], Vertex[RandomVariable[_]], String)]) {

  lazy val graph = JungUndirectedGraph(vps, ef)

  def eliminate(rv: RandomVariable[_]): InteractionGraph = null.asInstanceOf[InteractionGraph] // TODO

  def eliminationSequence(π: List[RandomVariable[_]]): List[InteractionGraph] =
    π.scanLeft(this)((G, rv) => G.eliminate(rv))

}
