package axle.stats

import collection._
import axle.graph.JungDirectedGraphFactory._
import axle.iterator.Lister
import axle.stats._

object Direction {

  val UNKNOWN = 0
  val OUTWARD = -1
  val INWARD = 1

}

case class Model(name: String = "no name") {

  def duplicate(): Model = {
    "TODO"
  }
  
  def copyTo(other: Model): Unit = {
    todo()
  }
  
  val g = graph[RandomVariable[_], String]()
  var newVarIndex = 0
  val name2variable = mutable.Map[String, RandomVariable[_]]()

  def getName(): String = name

  def getGraph() = g

  def getRandomVariables(): List[RandomVariable[_]] = g.getVertices().map(_.getPayload).toList

  def getVariable(name: String): RandomVariable[_] = name2variable(name)

  def numVariables(): Int = g.size

  def blocks(from: immutable.Set[RandomVariable[_]],
    to: immutable.Set[RandomVariable[_]],
    given: immutable.Set[RandomVariable[_]]): Boolean =
    _findOpenPath(Map[RandomVariable[_], Set[RandomVariable[_]]](), Direction.UNKNOWN,
      null, from, to, given).isEmpty

  //  var rvNameGetter = new Lister[RandomVariable, String]() {
  //    def function(rv: RandomVariable): String = rv.getName
  //  }

  def _findOpenPath(
    visited: Map[RandomVariable[_], Set[RandomVariable[_]]],
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

    for (variable <- current) {

      var openToVar = false
      var directionPriorToVar = Direction.UNKNOWN
      if (prior == null) {
        openToVar = true
      } else {
        directionPriorToVar = Direction.OUTWARD
        if (getGraph().precedes(variable, prior)) {
          directionPriorToVar = Direction.INWARD
        }

        if (priorDirection != Direction.UNKNOWN) {
          val priorGiven = given.contains(prior)
          openToVar = (
            priorDirection == Direction.INWARD &&
            !priorGiven &&
            directionPriorToVar == Direction.OUTWARD) ||
            (priorDirection == Direction.OUTWARD &&
              !priorGiven &&
              directionPriorToVar == Direction.OUTWARD) ||
              (priorDirection == Direction.INWARD &&
                graph.descendantsIntersectsSet(variable, given) &&
                directionPriorToVar == Direction.INWARD)
        } else {
          openToVar = true
        }
      }

      if (openToVar) {
        if (to.contains(variable)) {
          return Some(List(variable))
        }
        val neighbors = graph.getNeighbors(variable) - prior

        val visitedCopy = mutable.Map[RandomVariable[_], mutable.Set[RandomVariable[_]]]()
        visitedCopy ++= visited
        var outs = visited.get(prior)
        if (outs == null) {
          outs = mutable.Set[RandomVariable[_]]()
          visitedCopy += prior -> outs
        }
        outs += variable

        var path = _findOpenPath(visitedCopy, -1 * directionPriorToVar, variable, neighbors, to, given)
        if (path.isDefined) {
          return path.get + variable
        }
      }
    }
    return null
  }

}
