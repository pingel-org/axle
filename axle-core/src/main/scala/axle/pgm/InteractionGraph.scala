package axle.pgm

import axle._
import axle.stats._
import axle.graph._
import spire.implicits._
import spire.algebra._

case class InteractionGraph[T: Manifest: Eq, N: Field: Manifest](
  vps: Seq[RandomVariable[T, N]],
  ef: Seq[Vertex[RandomVariable[T, N]]] => Seq[(Vertex[RandomVariable[T, N]], Vertex[RandomVariable[T, N]], String)]) {

  lazy val graph = JungUndirectedGraph(vps, ef)

  def eliminate(rv: RandomVariable[T, N]): InteractionGraph[T, N] = ???

  def eliminationSequence(π: List[RandomVariable[T, N]]): List[InteractionGraph[T, N]] =
    π.scanLeft(this)(_ eliminate _)

}
