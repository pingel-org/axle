package axle.stats

import collection._
import axle.graph._
import axle.stats._

object Direction {

  val UNKNOWN = 0
  val OUTWARD = -1
  val INWARD = 1

}

class Model[MVP](graph: DirectedGraph[MVP, String]) {

  import graph._
  
  def name(): String = "model name"

  def vertexPayloadToRandomVariable(mvp: MVP): RandomVariable[_] = null // TODO

  def randomVariables(): List[RandomVariable[_]] = graph.vertices().map(v => vertexPayloadToRandomVariable(v.payload)).toList

  def variable(name: String): RandomVariable[_] = null // TODO name2variable(name)

  def numVariables(): Int = graph.size()

  def blocks(
    from: immutable.Set[RandomVariable[_]],
    to: immutable.Set[RandomVariable[_]],
    given: immutable.Set[RandomVariable[_]]): Boolean = {

    val x = Map[RandomVariable[_], mutable.Set[RandomVariable[_]]]()
    val mutableFromCopy = mutable.Set() ++ from
    _findOpenPath(x, Direction.UNKNOWN, null, mutableFromCopy, to, given).isEmpty
  }

  //  val rvNameGetter = new Lister[RandomVariable, String]() {
  //    def function(rv: RandomVariable): String = rv.getName
  //  }

  def _findOpenPath(
    visited: Map[RandomVariable[_], mutable.Set[RandomVariable[_]]],
    priorDirection: Int,
    prior: RandomVariable[_],
    current: mutable.Set[RandomVariable[_]],
    to: immutable.Set[RandomVariable[_]],
    given: immutable.Set[RandomVariable[_]]): Option[List[RandomVariable[_]]] = {

    println("_fOP: " + priorDirection +
      ", prior = " + "TODO" + // ((prior == null ) ? "null" : prior.name) +
      ", current = " + current.map(_.name).mkString(", ") +
      ", to = " + to.map(_.name).mkString(", ") +
      ", evidence = " + given.map(_.name).mkString(", "))

    val cachedOuts = visited(prior)
    if (cachedOuts != null) {
      current --= cachedOuts
    }

    val priorVertex = graph.findVertex((v: DirectedGraphVertex[MVP]) => vertexPayloadToRandomVariable(v.payload) == prior).get
    val givenVertices = given.map(v1 => graph.findVertex((v2: DirectedGraphVertex[MVP]) => vertexPayloadToRandomVariable(v2.payload) == v1).get)

    for (variable <- current) {

      val variableVertex = graph.findVertex((v: DirectedGraphVertex[MVP]) => vertexPayloadToRandomVariable(v.payload) == variable).get

      var openToVar = false
      var directionPriorToVar = Direction.UNKNOWN
      if (prior == null) {
        openToVar = true
      } else {
        directionPriorToVar = Direction.OUTWARD
        if (graph.precedes(variableVertex, priorVertex)) {
          directionPriorToVar = Direction.INWARD
        }

        if (priorDirection != Direction.UNKNOWN) {
          val priorGiven = given.contains(prior)
          openToVar = (priorDirection == Direction.INWARD && !priorGiven && directionPriorToVar == Direction.OUTWARD) ||
            (priorDirection == Direction.OUTWARD && !priorGiven && directionPriorToVar == Direction.OUTWARD) ||
            (priorDirection == Direction.INWARD && graph.descendantsIntersectsSet(variableVertex, givenVertices) && directionPriorToVar == Direction.INWARD)
        } else {
          openToVar = true
        }
      }

      if (openToVar) {
        if (to.contains(variable)) {
          return Some(List(variable))
        }
        val neighs = mutable.Set() ++ (graph.neighbors(variableVertex) - priorVertex).map(_.payload)

        val visitedCopy = mutable.Map[RandomVariable[_], mutable.Set[RandomVariable[_]]]() ++ visited
        if (!visited.contains(prior)) {
          visitedCopy += prior -> mutable.Set[RandomVariable[_]]()
        }
        visited(prior) += variable

        val path = _findOpenPath(visitedCopy, -1 * directionPriorToVar, variable, neighs.map(vertexPayloadToRandomVariable(_)), to, given)
        if (path.isDefined) {
          return Some(path.get ++ List(variable))
        }
      }
    }
    return None
  }

}

object Model {

  var newVarIndex = 0

  def apply[A](
    vps: Seq[A],
    ef: Seq[JungDirectedGraphVertex[A]] => Seq[(JungDirectedGraphVertex[A], JungDirectedGraphVertex[A], String)]): Model[A] =
    new Model(JungDirectedGraph(vps, ef))

}
