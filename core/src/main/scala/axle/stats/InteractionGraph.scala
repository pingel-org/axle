package axle.stats

import axle.graph._
import collection._

trait InteractionGraphFactory extends JungUndirectedGraphFactory {

  def apply(): InteractionGraph = new InteractionGraph() {}

  def apply(
    vps: Seq[RandomVariable[_]],
    ef: Seq[JungUndirectedGraphVertex[RandomVariable[_]]] => Seq[(JungUndirectedGraphVertex[RandomVariable[_]], JungUndirectedGraphVertex[RandomVariable[_]], String)]): InteractionGraph =
    JungUndirectedGraph(vps, ef).asInstanceOf[InteractionGraph] // TODO: cast

}

object InteractionGraph extends InteractionGraphFactory

trait InteractionGraph extends JungUndirectedGraph[RandomVariable[_], String] {

  def eliminate(rv: RandomVariable[_]): InteractionGraph = null.asInstanceOf[InteractionGraph] // TODO

  def eliminationSequence(π: List[RandomVariable[_]]): List[InteractionGraph] =
    π.scanLeft(this)((G, rv) => G.eliminate(rv))

}
