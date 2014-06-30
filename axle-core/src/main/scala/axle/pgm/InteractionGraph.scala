package axle.pgm

import axle._
import axle.stats._
import axle.graph._
import spire.implicits._
import spire.algebra._

case class InteractionGraph[T: Manifest: Eq, N: Field: Manifest](
  vps: Seq[Distribution[T, N]],
  ef: Seq[Vertex[Distribution[T, N]]] => Seq[(Vertex[Distribution[T, N]], Vertex[Distribution[T, N]], String)]) {

  lazy val graph = JungUndirectedGraph(vps, ef)

  def eliminate(rv: Distribution[T, N]): InteractionGraph[T, N] = ???

  def eliminationSequence(π: List[Distribution[T, N]]): List[InteractionGraph[T, N]] =
    π.scanLeft(this)(_ eliminate _)

}
