package axle.stats

import collection._
import axle._
import axle.graph._

object Direction {

  val UNKNOWN = 0
  val OUTWARD = -1
  val INWARD = 1

}

class Model[MVP](graph: DirectedGraph[MVP, String]) {

  import graph._

  def name(): String = "model name"

  def vertexPayloadToRandomVariable(mvp: MVP): RandomVariable[_] = ???

  def randomVariables(): List[RandomVariable[_]] = graph.vertices().map(v => vertexPayloadToRandomVariable(v.payload)).toList

  def variable(name: String): RandomVariable[_] = ??? // TODO name2variable(name)

  def numVariables(): Int = graph.size()

  def blocks(
    from: Set[RandomVariable[_]],
    to: Set[RandomVariable[_]],
    given: Set[RandomVariable[_]]): Boolean =
    _findOpenPath(
      Map[RandomVariable[_], Set[RandomVariable[_]]](),
      Direction.UNKNOWN,
      None,
      from,
      to,
      given).isEmpty

  //  val rvNameGetter = new Lister[RandomVariable, String]() {
  //    def function(rv: RandomVariable): String = rv.getName
  //  }

  def _findOpenPath(
    visited: Map[RandomVariable[_], Set[RandomVariable[_]]],
    priorDirection: Int,
    priorOpt: Option[RandomVariable[_]],
    current: Set[RandomVariable[_]], // Note: this used to be mutabl.  I may have introduced bugs.
    to: Set[RandomVariable[_]],
    given: Set[RandomVariable[_]]): Option[List[RandomVariable[_]]] = {

    println("_fOP: " + priorDirection +
      ", prior = " + priorOpt.map(_.name).getOrElse("<none>") +
      ", current = " + current.map(_.name).mkString(", ") +
      ", to = " + to.map(_.name).mkString(", ") +
      ", evidence = " + given.map(_.name).mkString(", "))

    val priorVertexOpt = priorOpt.map(prior => graph.findVertex((v: Vertex[MVP]) => vertexPayloadToRandomVariable(v.payload) == prior).get)
    val givenVertices = given.map(v1 => graph.findVertex((v2: Vertex[MVP]) => vertexPayloadToRandomVariable(v2.payload) == v1).get)

    (current -- priorOpt.map(visited(_)).getOrElse(Set())).toList.flatMap(variable => {

      val variableVertex = graph.findVertex((v: Vertex[MVP]) => vertexPayloadToRandomVariable(v.payload) == variable).get

      val (directionPriorToVar, openToVar) = priorVertexOpt.map(priorVertex => {
        val d = if (graph.precedes(variableVertex, priorVertex)) {
          Direction.INWARD
        } else {
          Direction.OUTWARD
        }
        (d, if (priorDirection != Direction.UNKNOWN) {
          val priorGiven = given.contains(priorOpt.get)
          (priorDirection == Direction.INWARD && !priorGiven && d == Direction.OUTWARD) ||
            (priorDirection == Direction.OUTWARD && !priorGiven && d == Direction.OUTWARD) ||
            (priorDirection == Direction.INWARD && graph.descendantsIntersectsSet(variableVertex, givenVertices) && d == Direction.INWARD)
        } else {
          true
        })
      }).getOrElse((Direction.UNKNOWN, true))

      if (openToVar) {
        if (to.contains(variable)) {
          Some(List(variable))
        } else {
          _findOpenPath(
            priorOpt.map(prior => {
              visited + (prior -> (visited.get(prior).getOrElse(Set[RandomVariable[_]]()) ++ Set(variable)))
            }).getOrElse(visited),
            -1 * directionPriorToVar,
            Some(variable),
            (graph.neighbors(variableVertex) - priorVertexOpt.get).map(_.payload).map(vertexPayloadToRandomVariable(_)),
            to,
            given)
            .map(_ ++ List(variable)
            )
        }
      } else {
        None
      }
    }).headOption // TODO: short-circuit

  }

}

object Model {

  var newVarIndex = 0

  def apply[A](
    vps: Seq[A],
    ef: Seq[Vertex[A]] => Seq[(Vertex[A], Vertex[A], String)]): Model[A] =
    new Model(JungDirectedGraph(vps, ef))

}
