package axle.pgm

import axle._
import axle.stats._
import axle.graph._
import spire.implicits._
import spire.algebra._

case class InteractionGraph[T: Manifest: Eq](
  vps: Seq[RandomVariable[T]],
  ef: Seq[Vertex[RandomVariable[T]]] => Seq[(Vertex[RandomVariable[T]], Vertex[RandomVariable[T]], String)]) {

  lazy val graph = JungUndirectedGraph(vps, ef)

  def eliminate(rv: RandomVariable[T]): InteractionGraph[T] = ???

  def eliminationSequence(π: List[RandomVariable[T]]): List[InteractionGraph[T]] =
    π.scanLeft(this)((G, rv) => G.eliminate(rv))

}
