package axle.pgm

import axle.algebra.UndirectedGraph
import axle.stats.Distribution
import spire.algebra.Eq
import spire.algebra.Field
import spire.implicits.StringOrder

class InteractionGraphEdge

case class InteractionGraph[T: Manifest: Eq, N: Field: Manifest, UG](
    vps: Seq[Distribution[T, N]],
    ef: Seq[(Distribution[T, N], Distribution[T, N], InteractionGraphEdge)])(
        implicit ug: UndirectedGraph[UG, Distribution[T, N], InteractionGraphEdge]) {

  lazy val graph = ug.make(vps, ef)

  def eliminate(rv: Distribution[T, N]): InteractionGraph[T, N, UG] = ???

  def eliminationSequence(π: List[Distribution[T, N]]): List[InteractionGraph[T, N, UG]] =
    π.scanLeft(this)(_ eliminate _)

}
