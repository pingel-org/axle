package axle.stats

import collection._
import axle.graph._
import axle.graph.JungDirectedGraph._
import axle.stats._

object Direction {

  val UNKNOWN = 0
  val OUTWARD = -1
  val INWARD = 1

}

trait ModelFactory extends JungDirectedGraphFactory {

  def apply[A](
    vps: Seq[A],
    ef: Seq[JungDirectedGraphVertex[A]] => Seq[(JungDirectedGraphVertex[A], JungDirectedGraphVertex[A], String)]): Model[A] =
    new Model(vps, ef)

  class Model[MVP](
    vps: Seq[MVP],
    ef: Seq[JungDirectedGraphVertex[MVP]] => Seq[(JungDirectedGraphVertex[MVP], JungDirectedGraphVertex[MVP], String)])
    extends JungDirectedGraph[MVP, String](vps, ef) {

    def name(): String

    def vertexPayloadToRandomVariable(mvp: MVP): RandomVariable[_]

    def randomVariables(): List[RandomVariable[_]] = vertices().map(v => vertexPayloadToRandomVariable(v.payload)).toList

    def variable(name: String): RandomVariable[_] = null // TODO name2variable(name)

    def numVariables(): Int = size()

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

      val priorVertex = findVertex((v: JungDirectedGraphVertex[MVP]) => vertexPayloadToRandomVariable(v.payload) == prior).get
      val givenVertices = given.map(v1 => findVertex((v2: JungDirectedGraphVertex[MVP]) => vertexPayloadToRandomVariable(v2.payload) == v1).get)

      for (variable <- current) {

        val variableVertex = findVertex((v: JungDirectedGraphVertex[MVP]) => vertexPayloadToRandomVariable(v.payload) == variable).get

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

}

object Model extends ModelFactory {

  var newVarIndex = 0

}
