package axle.stats

import org.specs2.mutable._
import collection._
import axle.graph._

class ConditionalProbabilityTableSpecification extends Specification {

  import BayesianNetworkModule._
  
  val bools = Some(Vector(true, false))

  val A = new RandomVariable0("A", bools, None)
  val B = new RandomVariable0("B", bools, None)
  val C = new RandomVariable0("C", bools, None)
  val D = new RandomVariable0("D", bools, None)
  val E = new RandomVariable0("E", bools, None)

  val bn = BayesianNetwork("6.1", List(
    BayesianNetworkNode(A,
      Factor(Vector(A), Map(
        List(A is true) -> 0.6,
        List(A is false) -> 0.4
      ))),
    BayesianNetworkNode(B, // B | A
      Factor(Vector(B), Map(
        List(B is true, A is true) -> 0.2,
        List(B is true, A is false) -> 0.8,
        List(B is false, A is true) -> 0.75,
        List(B is false, A is false) -> 0.25
      ))),
    BayesianNetworkNode(C, // C | A
      Factor(Vector(C), Map(
        List(C is true, A is true) -> 0.8,
        List(C is true, A is false) -> 0.2,
        List(C is false, A is true) -> 0.1,
        List(C is false, A is false) -> 0.9
      ))),
    BayesianNetworkNode(D, // D | BC
      Factor(Vector(D), Map(
        List(D is true, B is true, C is true) -> 0.95,
        List(D is true, B is true, C is false) -> 0.05,
        List(D is true, B is false, C is true) -> 0.9,
        List(D is true, B is false, C is false) -> 0.1,
        List(D is false, B is true, C is true) -> 0.8,
        List(D is false, B is true, C is false) -> 0.2,
        List(D is false, B is false, C is true) -> 0.0,
        List(D is false, B is false, C is false) -> 1.0
      ))),
    BayesianNetworkNode(E, // E | C
      Factor(Vector(E), Map(
        List(E is true, C is true) -> 0.7,
        List(E is true, C is false) -> 0.3,
        List(E is false, C is true) -> 0.0,
        List(E is false, C is false) -> 1.0
      )))),
    (vs: Seq[Vertex[BayesianNetworkNode]]) => vs match {
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
