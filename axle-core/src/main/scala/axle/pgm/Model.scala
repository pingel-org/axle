package axle.pgm

import axle._
import axle.graph._
import axle.stats._
import spire.algebra._
import spire.implicits._

object Direction {

  val UNKNOWN = 0
  val OUTWARD = -1
  val INWARD = 1

}

case class GenModel[T: Eq, N: Field](graph: DirectedGraph[Distribution[T, N], String]) {

  def vertexPayloadToDistribution(mvp: T): Distribution[T, N] = ???

  def randomVariables: Vector[Distribution[T, N]] =
    graph.vertices.map(_.payload).toVector

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

    val priorVertexOpt = priorOpt.map(prior => graph.findVertex(_.payload === prior).get)
    val givenVertices = given.map(v1 => graph.findVertex(_.payload === v1).get)

    (current -- priorOpt.map(visited).getOrElse(Set())).toList.flatMap(variable => {

      val variableVertex = graph.findVertex(_.payload === variable).get

      val (directionPriorToVar, openToVar) = priorVertexOpt.map(priorVertex => {
        val d = if (graph.precedes(variableVertex, priorVertex)) {
          Direction.INWARD
        } else {
          Direction.OUTWARD
        }
        val otv = if (priorDirection != Direction.UNKNOWN) {
          val priorGiven = given.contains(priorOpt.get)
          (priorDirection === Direction.INWARD && !priorGiven && d === Direction.OUTWARD) ||
            (priorDirection === Direction.OUTWARD && !priorGiven && d === Direction.OUTWARD) ||
            (priorDirection === Direction.INWARD && graph.descendantsIntersectsSet(variableVertex, givenVertices) && d === Direction.INWARD)
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
            (graph.neighbors(variableVertex) - priorVertexOpt.get).map(_.payload),
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

//object Model {
//
//  val newVarIndex = 0
//
//  def apply[A: Eq](
//    vps: Vector[A],
//    ef: Seq[Vertex[A]] => Seq[(Vertex[A], Vertex[A], String)]): Model[A] =
//    new Model(JungDirectedGraph(vps, ef))
//
//}
