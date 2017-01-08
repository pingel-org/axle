package axle.pgm

import edu.uci.ics.jung.graph.DirectedSparseGraph
import org.scalatest._
import cats.implicits._
import spire.math._
import axle.stats._
import axle.jung.directedGraphJung

class ConditionalProbabilityTableSpecification
    extends FunSuite with Matchers {

  val bools = Vector(true, false)

  def ubd(name: String) = UnknownDistribution0[Boolean, Rational](bools, name)

  val A = ubd("A")
  val B = ubd("B")
  val C = ubd("C")
  val D = ubd("D")
  val E = ubd("E")

  val aFactor = Factor(Vector(A), Map(
    Vector(A is true) -> Rational(6, 10),
    Vector(A is false) -> Rational(4, 10)))

  val bFactor = Factor(Vector(B), Map(
    Vector(B is true, A is true) -> Rational(2, 10),
    Vector(B is true, A is false) -> Rational(8, 10),
    Vector(B is false, A is true) -> Rational(3, 4),
    Vector(B is false, A is false) -> Rational(1, 4)))

  val cFactor = Factor(Vector(C), Map(
    Vector(C is true, A is true) -> Rational(8, 10),
    Vector(C is true, A is false) -> Rational(2, 10),
    Vector(C is false, A is true) -> Rational(1, 10),
    Vector(C is false, A is false) -> Rational(9, 10)))

  val dFactor = Factor(Vector(D), Map(
    Vector(D is true, B is true, C is true) -> Rational(95, 100),
    Vector(D is true, B is true, C is false) -> Rational(5, 100),
    Vector(D is true, B is false, C is true) -> Rational(9, 10),
    Vector(D is true, B is false, C is false) -> Rational(1, 10),
    Vector(D is false, B is true, C is true) -> Rational(8, 10),
    Vector(D is false, B is true, C is false) -> Rational(2, 10),
    Vector(D is false, B is false, C is true) -> Rational(0),
    Vector(D is false, B is false, C is false) -> Rational(1)))

  val eFactor = Factor(Vector(E), Map(
    Vector(E is true, C is true) -> Rational(7, 10),
    Vector(E is true, C is false) -> Rational(3, 10),
    Vector(E is false, C is true) -> Rational(0),
    Vector(E is false, C is false) -> Rational(1)))

  // edges: ab, ac, bd, cd, ce
  val bn = BayesianNetwork[Boolean, Rational, DirectedSparseGraph[BayesianNetworkNode[Boolean, Rational], Edge]](
    "6.1",
    Map(A -> aFactor,
      B -> bFactor,
      C -> cFactor,
      D -> dFactor,
      E -> eFactor))

  test("CPT") {

    // TODO more CPT-specific tests

    val factor = bn.jointProbabilityTable.sumOut(A).sumOut(B).sumOut(C).sumOut(D).sumOut(E)

    factor.values should have size 1
  }

}
