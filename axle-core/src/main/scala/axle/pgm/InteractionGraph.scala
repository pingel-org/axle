package axle.pgm

import cats.kernel.Eq
import axle.algebra.UndirectedGraph
import axle.probability.Variable

class InteractionGraphEdge

case class InteractionGraph[T: Eq, UG](
  vps: Seq[Variable[T]],
  ef:  Seq[(Variable[T], Variable[T], InteractionGraphEdge)])(
  implicit
  ug: UndirectedGraph[UG, Variable[T], InteractionGraphEdge]) {

  lazy val graph = ug.make(vps, ef)

  def eliminate(rv: Variable[T]): InteractionGraph[T, UG] = ???

  def eliminationSequence(π: List[Variable[T]]): List[InteractionGraph[T, UG]] =
    π.scanLeft(this)(_ eliminate _)

}
