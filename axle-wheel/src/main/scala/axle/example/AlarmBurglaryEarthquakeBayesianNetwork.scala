package axle.example

import edu.uci.ics.jung.graph.DirectedSparseGraph
import cats.implicits._
import spire.math._
import spire.implicits.additiveGroupOps

import axle.stats._
import axle.pgm.BayesianNetwork
import axle.pgm.BayesianNetworkNode
import axle.pgm.Edge
import axle.jung.directedGraphJung

/**
 * The "Alarm Burglary Earthquake" model is a classic 5-variable (all Boolean)
 * model from Bayesian Network literature.
 *
 * In English, the model represents
 * 
 *   "Alarm sounds (due to Burglary or Earthquake) and John or Mary Call."
 * 
 * There are two inputs
 * 
 *   B -- Is there a burglary?
 *   E -- Is there an earthquake?
 * 
 * Those feed to an internal variable of the graph:
 * 
 *   A -- Does the alarm sound?
 * 
 * Which in turn feeds two variables
 * 
 *   M -- Does Mary call 911?
 *   J -- Does John call 911?
 * 
 * The edges of the graph formed by these variables are:
 * 
 *   B -> A
 *   E -> A
 *   A -> M
 *   A -> J
 * 
 * There are 10 Rationals in the constructor for AlarmBurglaryEarthquakeBayesianNetwork,
 * that describe the degrees of freedom in the factors corresponding to each of the
 * nodes of the Bayesian Network:
 * 
 *   B and E require 1 each
 *   A requires 4
 *   M and J require 2 each
 * 
 */

class AlarmBurglaryEarthquakeBayesianNetwork(
  pBurgle: Rational = Rational(1, 1000),
  pEarthquake: Rational = Rational(1, 500),
  pAlarm: Rational = Rational(1, 1000),
  pAlarmBurglary: Rational = Rational(940, 1000),
  pAlarmEarthquake: Rational = Rational(290, 1000),
  pAlarmBurglaryEarthquake: Rational = Rational(950, 1000),
  pJohn: Rational = Rational(5, 100),
  pJohnAlarm: Rational = Rational(9, 10),
  pMary: Rational = Rational(1, 100),
  pMaryAlarm: Rational = Rational(7, 10)
) {

  import AlarmBurglaryEarthquakeBayesianNetwork._

  val bFactor =
    Factor(Vector(B -> booleans), Map(
      Vector(B is true) -> pBurgle,
      Vector(B is false) -> (1 - pBurgle)))

  val eFactor =
    Factor(Vector(E -> booleans), Map(
      Vector(E is true) -> pEarthquake,
      Vector(E is false) -> (1 - pEarthquake)))

  val aFactor =
    Factor(Vector(B -> booleans, E -> booleans, A -> booleans), Map(
      Vector(B is false, E is false, A is true) -> pAlarm,
      Vector(B is false, E is false, A is false) -> (1 - pAlarm),
      Vector(B is true, E is false, A is true) -> pAlarmBurglary,
      Vector(B is true, E is false, A is false) -> (1 - pAlarmBurglary),
      Vector(B is false, E is true, A is true) -> pAlarmEarthquake,
      Vector(B is false, E is true, A is false) -> (1 - pAlarmEarthquake),
      Vector(B is true, E is true, A is true) -> pAlarmBurglaryEarthquake,
      Vector(B is true, E is true, A is false) -> (1 - pAlarmBurglaryEarthquake)))

  val jFactor =
    Factor(Vector(A -> booleans, J -> booleans), Map(
      Vector(A is true, J is true) -> pJohnAlarm,
      Vector(A is true, J is false) -> (1 - pJohnAlarm),
      Vector(A is false, J is true) -> pJohn,
      Vector(A is false, J is false) -> (1 - pJohn)))

  val mFactor =
    Factor(Vector(A -> booleans, M -> booleans), Map(
      Vector(A is true, M is true) -> pMaryAlarm,
      Vector(A is true, M is false) -> (1 - pMaryAlarm),
      Vector(A is false, M is true) -> pMary,
      Vector(A is false, M is false) -> (1 - pMary)))

  val bn: BayesianNetwork[Boolean, Rational, DirectedSparseGraph[BayesianNetworkNode[Boolean, Rational], Edge]] =
    BayesianNetwork.withGraphK2[Boolean, Rational, DirectedSparseGraph](
      Map(
        B -> bFactor,
        E -> eFactor,
        A -> aFactor,
        J -> jFactor,
        M -> mFactor))

  import axle.pgm.MonotypeBayesanNetwork

  lazy val monotype = MonotypeBayesanNetwork(bn,
    AlarmBurglaryEarthquakeBayesianNetwork.select,
    AlarmBurglaryEarthquakeBayesianNetwork.combine1,
    AlarmBurglaryEarthquakeBayesianNetwork.combine2)

}

object AlarmBurglaryEarthquakeBayesianNetwork {

  val booleans = Vector(true, false)

  val domain: Vector[(Boolean, Boolean, Boolean, Boolean, Boolean)] = 
    for {
      b <- booleans
      e <- booleans
      a <- booleans
      j <- booleans
      m <- booleans
    } yield (b, e, a, j, m)

  val one = Rational(1)

  val B = Variable[Boolean]("Burglary")
  val E = Variable[Boolean]("Earthquake")
  val A = Variable[Boolean]("Alarm")
  val J = Variable[Boolean]("John Calls")
  val M = Variable[Boolean]("Mary Calls")

  def select(
    v: Variable[Boolean],
    c: (Boolean, Boolean, Boolean, Boolean, Boolean)): Boolean =
    v match {
      case B => c._1
      case E => c._2
      case A => c._3
      case J => c._4
      case M => c._5
      case _ => ??? // TODO avoid default
    }

  def combine1(vs: Vector[Boolean]):  (Boolean, Boolean, Boolean, Boolean, Boolean) =
    (vs(0), vs(1), vs(2), vs(3), vs(4))

  def combine2(m: Map[Variable[Boolean], Boolean]): (Boolean, Boolean, Boolean, Boolean, Boolean) = 
    (m(B), m(E), m(A), m(J), m(M))

}
