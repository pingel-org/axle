package axle.stats

import collection._
import axle.graph.JungDirectedGraphFactory._
import axle.stats._

object Direction {

  val UNKNOWN = 0
  val OUTWARD = -1
  val INWARD = 1

}

trait Model[MVP] extends JungDirectedGraph[MVP, String] {

  // def duplicate(): Model = new Model(name, graphFrom(g)(v => v, e => e))

  var newVarIndex = 0
  val name2variable = mutable.Map[String, RandomVariable[_]]()

  def getName(): String

  def vertexPayloadToRandomVariable(mvp: MVP): RandomVariable[_]

  def getRandomVariables(): List[RandomVariable[_]] = vertices().map(v => vertexPayloadToRandomVariable(v.payload)).toList

  def getVariable(name: String): RandomVariable[_] = name2variable(name)

  def numVariables(): Int = size()

  def blocks(
    from: immutable.Set[RandomVariable[_]],
    to: immutable.Set[RandomVariable[_]],
    given: immutable.Set[RandomVariable[_]]): Boolean = {

    val x = Map[RandomVariable[_], mutable.Set[RandomVariable[_]]]()
    val mutableFromCopy = mutable.Set() ++ from
    _findOpenPath(x, Direction.UNKNOWN, null, mutableFromCopy, to, given).isEmpty
  }

  //  var rvNameGetter = new Lister[RandomVariable, String]() {
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
      ", current = " + current.map(_.getName).mkString(", ") +
      ", to = " + to.map(_.getName).mkString(", ") +
      ", evidence = " + given.map(_.getName).mkString(", "))

    val cachedOuts = visited(prior)
    if (cachedOuts != null) {
      current --= cachedOuts
    }

    val priorVertex = findVertex(vertexPayloadToRandomVariable(_) == prior).get
    val givenVertices = given.map(v => findVertex(vertexPayloadToRandomVariable(_) == v).get)

    for (variable <- current) {

      val variableVertex = findVertex(vertexPayloadToRandomVariable(_) == variable).get

      var openToVar = false
      var directionPriorToVar = Direction.UNKNOWN
      if (prior == null) {
        openToVar = true
      } else {
        directionPriorToVar = Direction.OUTWARD
        if (precedes(variableVertex, priorVertex)) {
          directionPriorToVar = Direction.INWARD
        }

        if (priorDirection != Direction.UNKNOWN) {
          val priorGiven = given.contains(prior)
          openToVar = (priorDirection == Direction.INWARD && !priorGiven && directionPriorToVar == Direction.OUTWARD) ||
            (priorDirection == Direction.OUTWARD && !priorGiven && directionPriorToVar == Direction.OUTWARD) ||
            (priorDirection == Direction.INWARD && descendantsIntersectsSet(variableVertex, givenVertices) && directionPriorToVar == Direction.INWARD)
        } else {
          openToVar = true
        }
      }

      if (openToVar) {
        if (to.contains(variable)) {
          return Some(List(variable))
        }
        val neighs = mutable.Set() ++ (neighbors(variableVertex) - priorVertex).map(_.payload)

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
