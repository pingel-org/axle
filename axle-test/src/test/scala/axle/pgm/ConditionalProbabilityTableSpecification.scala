package axle.pgm

import axle._
import axle.stats._
import axle.algebra.Vertex
import axle.algebra.DirectedGraph
import axle.jblas._
import axle.jung.JungDirectedGraph.directedGraphJung
import spire.implicits._
import spire.math._
import org.specs2.mutable._

class ConditionalProbabilityTableSpecification
  extends Specification {

  val bools = Vector(true, false)

  def ubd(name: String) = UnknownDistribution0[Boolean, Rational](bools, name)

  val A = ubd("A")
  val B = ubd("B")
  val C = ubd("C")
  val D = ubd("D")
  val E = ubd("E")

  val bn = BayesianNetwork("6.1", Vector(
    BayesianNetworkNode(A,
      Factor(Vector(A), Map(
        Vector(A is true) -> Rational(6, 10),
        Vector(A is false) -> Rational(4, 10)))),
    BayesianNetworkNode(B, // B | A
      Factor(Vector(B), Map(
        Vector(B is true, A is true) -> Rational(2, 10),
        Vector(B is true, A is false) -> Rational(8, 10),
        Vector(B is false, A is true) -> Rational(3, 4),
        Vector(B is false, A is false) -> Rational(1, 4)))),
    BayesianNetworkNode(C, // C | A
      Factor(Vector(C), Map(
        Vector(C is true, A is true) -> Rational(8, 10),
        Vector(C is true, A is false) -> Rational(2, 10),
        Vector(C is false, A is true) -> Rational(1, 10),
        Vector(C is false, A is false) -> Rational(9, 10)))),
    BayesianNetworkNode(D, // D | BC
      Factor(Vector(D), Map(
        Vector(D is true, B is true, C is true) -> Rational(95, 100),
        Vector(D is true, B is true, C is false) -> Rational(5, 100),
        Vector(D is true, B is false, C is true) -> Rational(9, 10),
        Vector(D is true, B is false, C is false) -> Rational(1, 10),
        Vector(D is false, B is true, C is true) -> Rational(8, 10),
        Vector(D is false, B is true, C is false) -> Rational(2, 10),
        Vector(D is false, B is false, C is true) -> Rational(0),
        Vector(D is false, B is false, C is false) -> Rational(1)))),
    BayesianNetworkNode(E, // E | C
      Factor(Vector(E), Map(
        Vector(E is true, C is true) -> Rational(7, 10),
        Vector(E is true, C is false) -> Rational(3, 10),
        Vector(E is false, C is true) -> Rational(0),
        Vector(E is false, C is false) -> Rational(1))))),
    (vs: Seq[Vertex[BayesianNetworkNode[Boolean, Rational]]]) => vs match {
      case a :: b :: c :: d :: e :: Nil => List((a, b, ""), (a, c, ""), (b, d, ""), (c, d, ""), (c, e, ""))
      case _                            => Nil
    })

  "CPT" should {
    "work" in {

      // for (kase <- cptB.cases) {
      //   for (caseIs <- kase) {
      //     println(caseIs.rv + " " + caseIs.v)
      //   }
      // }

      1 must be equalTo (1)
    }
  }

}
