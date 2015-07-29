package axle.pgm

import axle.algebra.DirectedGraph
import axle.stats.Distribution
import spire.algebra.Eq
import spire.algebra.Field
import spire.implicits.IntAlgebra
import spire.implicits.eqOps
import axle.algebra.Finite
import axle.syntax.directedgraph._
import axle.syntax.finite._

object Direction {

  val UNKNOWN = 0
  val OUTWARD = -1
  val INWARD = 1

}

case class GenModel[T: Eq, N: Field, DG[_, _]: DirectedGraph](
    graph: DG[Distribution[T, N], Edge])(
        implicit finiteDG: Finite[DG[Distribution[T, N], Edge], Int]) {

  def vertexPayloadToDistribution(mvp: T): Distribution[T, N] = ???

  def randomVariables: Vector[Distribution[T, N]] =
    graph.vertices.toVector

  def variable(name: String): Distribution[T, N] = ??? // TODO name2variable(name)

  def numVariables: Int = graph.size

  def blocks(
    from: Set[Distribution[T, N]],
    to: Set[Distribution[T, N]],
    given: Set[Distribution[T, N]]): Boolean =
    _findOpenPath(
      Map.empty[Distribution[T, N], Set[Distribution[T, N]]],
      Direction.UNKNOWN,
      None,
      from,
      to,
      given).isEmpty

  //  val rvNameGetter = new Lister[Distribution, String]() {
  //    def function(rv: Distribution): String = rv.getName
  //  }

  def _findOpenPath(
    visited: Map[Distribution[T, N], Set[Distribution[T, N]]],
    priorDirection: Int,
    priorOpt: Option[Distribution[T, N]],
    current: Set[Distribution[T, N]], // Note: this used to be mutabl.  I may have introduced bugs.
    to: Set[Distribution[T, N]],
    given: Set[Distribution[T, N]]): Option[List[Distribution[T, N]]] = {

    lazy val logMessage = "_fOP: " + priorDirection +
      ", prior = " + priorOpt.map(_.name).getOrElse("<none>") +
      ", current = " + current.map(_.name).mkString(", ") +
      ", to = " + to.map(_.name).mkString(", ") +
      ", evidence = " + given.map(_.name).mkString(", ")

    (current -- priorOpt.map(visited).getOrElse(Set.empty)).toList.flatMap(variable => {

      val (directionPriorToVar, openToVar) = priorOpt.map(prior => {
        val d = if (graph.precedes(variable, prior)) {
          Direction.INWARD
        } else {
          Direction.OUTWARD
        }
        val otv = if (priorDirection != Direction.UNKNOWN) {
          val priorGiven = given.contains(priorOpt.get)
          (priorDirection === Direction.INWARD && !priorGiven && d === Direction.OUTWARD) ||
            (priorDirection === Direction.OUTWARD && !priorGiven && d === Direction.OUTWARD) ||
            (priorDirection === Direction.INWARD && graph.descendantsIntersectsSet(variable, given) && d === Direction.INWARD)
        } else {
          true
        }
        (d, otv)
      }).getOrElse((Direction.UNKNOWN, true))

      if (openToVar) {
        if (to.contains(variable)) {
          Some(List(variable))
        } else {
          _findOpenPath(
            priorOpt.map(prior => {
              visited + (prior -> (visited.get(prior).getOrElse(Set[Distribution[T, N]]()) ++ Set(variable)))
            }).getOrElse(visited),
            -1 * directionPriorToVar,
            Some(variable),
            (graph.neighbors(variable) - priorOpt.get),
            to,
            given)
            .map(_ ++ List(variable))
        }
      } else {
        None
      }
    }).headOption // TODO: short-circuit

  }

}
