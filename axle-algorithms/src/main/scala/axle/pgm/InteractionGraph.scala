package axle.pgm

import axle.algebra.Vertex
import axle.algebra.UndirectedGraph
import axle.stats.Distribution
import spire.algebra.Eq
import spire.algebra.Field
import spire.implicits.StringOrder

case class InteractionGraph[T: Manifest: Eq, N: Field: Manifest, UG[_, _]: UndirectedGraph](
  vps: Seq[Distribution[T, N]],
  ef: Seq[Vertex[Distribution[T, N]]] => Seq[(Vertex[Distribution[T, N]], Vertex[Distribution[T, N]], String)]) {

  lazy val graph = implicitly[UndirectedGraph[UG]].make(vps, ef)

  def eliminate(rv: Distribution[T, N]): InteractionGraph[T, N, UG] = ???

  def eliminationSequence(π: List[Distribution[T, N]]): List[InteractionGraph[T, N, UG]] =
    π.scanLeft(this)(_ eliminate _)

}
