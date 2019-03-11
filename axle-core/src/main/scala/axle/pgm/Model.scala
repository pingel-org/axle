package axle.pgm

//import cats.implicits._
//import spire.algebra._
//import axle.algebra.Finite
//import axle.algebra.DirectedGraph
//import axle.stats.Variable
//import axle.syntax.directedgraph._
//import axle.syntax.finite._

object Direction {

  val UNKNOWN = 0
  val OUTWARD = -1
  val INWARD = 1

}
/*
case class GenModel[T: Eq, N: Field, DG](
  graph: DG)(
  implicit
  finiteDG: Finite[DG, Int],
  dg:       DirectedGraph[DG, Variable[T], Edge]) {

  def vertexPayloadToDistribution(mvp: T): Variable[T] = ???

  def randomVariables: Vector[Variable[T]] =
    graph.vertices.toVector

  def variable(name: String): Variable[T] = ??? // TODO name2variable(name)

  def numVariables: Int = graph.size

  def blocks(
    from:  Set[Variable[T]],
    to:    Set[Variable[T]],
    given: Set[Variable[T]]): Boolean =
    _findOpenPath(
      Map.empty[Variable[T], Set[Variable[T]]],
      Direction.UNKNOWN,
      None,
      from,
      to,
      given).isEmpty

  //  val rvNameGetter = new Lister[Distribution, String]() {
  //    def function(rv: Distribution): String = rv.getName
  //  }

  def _findOpenPath(
    visited:        Map[Variable[T], Set[Variable[T]]],
    priorDirection: Int,
    priorOpt:       Option[Variable[T]],
    current:        Set[Variable[T]], // Note: this used to be mutabl.  I may have introduced bugs.
    to:             Set[Variable[T]],
    given:          Set[Variable[T]]): Option[List[Variable[T]]] = {

    //    lazy val logMessage = "_fOP: " + priorDirection +
    //      ", prior = " + priorOpt.map(_.name).getOrElse("<none>") +
    //      ", current = " + current.map(_.name).mkString(", ") +
    //      ", to = " + to.map(_.name).mkString(", ") +
    //      ", evidence = " + given.map(_.name).mkString(", ")

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
              visited + (prior -> (visited.get(prior).getOrElse(Set[Variable[T]]()) ++ Set(variable)))
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
*/