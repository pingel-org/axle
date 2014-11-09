package axle.pgm

import axle.graph.JungUndirectedGraph
import axle.graph.Vertex
import axle.stats.Distribution
import spire.algebra.Eq
import spire.algebra.Field
import spire.implicits.StringOrder

case class InteractionGraph[T: Manifest: Eq, N: Field: Manifest](
  vps: Seq[Distribution[T, N]],
  ef: Seq[Vertex[Distribution[T, N]]] => Seq[(Vertex[Distribution[T, N]], Vertex[Distribution[T, N]], String)]) {

  lazy val graph = JungUndirectedGraph(vps, ef)

  def eliminate(rv: Distribution[T, N]): InteractionGraph[T, N] = ???

  def eliminationSequence(π: List[Distribution[T, N]]): List[InteractionGraph[T, N]] =
    π.scanLeft(this)(_ eliminate _)

}
