package axle.pgm

import axle._
import axle.graph._
import axle.stats._
import spire.implicits._
import org.specs2.mutable._

class ConditionalProbabilityTableSpecification extends Specification {

  import BayesianNetworkModule._
  import FactorModule._

  val bools = Some(Vector(true, false))

  val A = new RandomVariable0("A", bools, None)
  val B = new RandomVariable0("B", bools, None)
  val C = new RandomVariable0("C", bools, None)
  val D = new RandomVariable0("D", bools, None)
  val E = new RandomVariable0("E", bools, None)

  val bn = BayesianNetwork("6.1", Vector(
    BayesianNetworkNode(A,
      Factor(Vector(A), Map(
        Vector(A is true) -> 0.6,
        Vector(A is false) -> 0.4
      ))),
    BayesianNetworkNode(B, // B | A
      Factor(Vector(B), Map(
        Vector(B is true, A is true) -> 0.2,
        Vector(B is true, A is false) -> 0.8,
        Vector(B is false, A is true) -> 0.75,
        Vector(B is false, A is false) -> 0.25
      ))),
    BayesianNetworkNode(C, // C | A
      Factor(Vector(C), Map(
        Vector(C is true, A is true) -> 0.8,
        Vector(C is true, A is false) -> 0.2,
        Vector(C is false, A is true) -> 0.1,
        Vector(C is false, A is false) -> 0.9
      ))),
    BayesianNetworkNode(D, // D | BC
      Factor(Vector(D), Map(
        Vector(D is true, B is true, C is true) -> 0.95,
        Vector(D is true, B is true, C is false) -> 0.05,
        Vector(D is true, B is false, C is true) -> 0.9,
        Vector(D is true, B is false, C is false) -> 0.1,
        Vector(D is false, B is true, C is true) -> 0.8,
        Vector(D is false, B is true, C is false) -> 0.2,
        Vector(D is false, B is false, C is true) -> 0.0,
        Vector(D is false, B is false, C is false) -> 1.0
      ))),
    BayesianNetworkNode(E, // E | C
      Factor(Vector(E), Map(
        Vector(E is true, C is true) -> 0.7,
        Vector(E is true, C is false) -> 0.3,
        Vector(E is false, C is true) -> 0.0,
        Vector(E is false, C is false) -> 1.0
      )))),
    (vs: Seq[Vertex[BayesianNetworkNode[Boolean]]]) => vs match {
      case a :: b :: c :: d :: e :: Nil => List((a, b, ""), (a, c, ""), (b, d, ""), (c, d, ""), (c, e, ""))
      case _ => Nil
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
