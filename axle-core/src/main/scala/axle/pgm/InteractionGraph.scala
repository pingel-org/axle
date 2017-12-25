package axle.pgm

import axle.algebra.UndirectedGraph
import axle.stats.Variable
import cats.kernel.Eq

class InteractionGraphEdge

case class InteractionGraph[T: Manifest: Eq, UG](
  vps: Seq[Variable[T]],
  ef:  Seq[(Variable[T], Variable[T], InteractionGraphEdge)])(
  implicit
  ug: UndirectedGraph[UG, Variable[T], InteractionGraphEdge]) {

  lazy val graph = ug.make(vps, ef)

  def eliminate(rv: Variable[T]): InteractionGraph[T, UG] = ???

  def eliminationSequence(π: List[Variable[T]]): List[InteractionGraph[T, UG]] =
    π.scanLeft(this)(_ eliminate _)

}
