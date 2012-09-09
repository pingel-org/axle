package axle.stats

import org.specs2.mutable._
import collection._
import axle.graph.JungDirectedGraphFactory._
import axle.graph.JungUndirectedGraphFactory.JungUndirectedGraph

class ConditionalProbabilityTableSpecification extends Specification {

  val bools = Some(Vector(true, false))

  val A = new RandomVariable0("A", bools, None)
  val B = new RandomVariable0("B", bools, None)
  val C = new RandomVariable0("C", bools, None)
  val D = new RandomVariable0("D", bools, None)
  val E = new RandomVariable0("E", bools, None)

  val bn = BayesianNetwork("6.1")

  // A
  val av = bn += BayesianNetworkNode(A,
    Factor(Vector(A), Some(Map(
      List(A eq true) -> 0.6,
      List(A eq false) -> 0.4
    ))))

  // B | A
  val bv = bn += BayesianNetworkNode(B,
    Factor(Vector(B), Some(Map(
      List(B eq true, A eq true) -> 0.2,
      List(B eq true, A eq false) -> 0.8,
      List(B eq false, A eq true) -> 0.75,
      List(B eq false, A eq false) -> 0.25
    ))))

  // C | A
  val cv = bn += BayesianNetworkNode(C,
    Factor(Vector(C), Some(Map(
      List(C eq true, A eq true) -> 0.8,
      List(C eq true, A eq false) -> 0.2,
      List(C eq false, A eq true) -> 0.1,
      List(C eq false, A eq false) -> 0.9
    ))))

  // D | BC
  val dv = bn += BayesianNetworkNode(D,
    Factor(Vector(D), Some(Map(
      List(D eq true, B eq true, C eq true) -> 0.95,
      List(D eq true, B eq true, C eq false) -> 0.05,
      List(D eq true, B eq false, C eq true) -> 0.9,
      List(D eq true, B eq false, C eq false) -> 0.1,
      List(D eq false, B eq true, C eq true) -> 0.8,
      List(D eq false, B eq true, C eq false) -> 0.2,
      List(D eq false, B eq false, C eq true) -> 0.0,
      List(D eq false, B eq false, C eq false) -> 1.0
    ))))

  // E | C
  val ev = bn += BayesianNetworkNode(E,
    Factor(Vector(E), Some(Map(
      List(E eq true, C eq true) -> 0.7,
      List(E eq true, C eq false) -> 0.3,
      List(E eq false, C eq true) -> 0.0,
      List(E eq false, C eq false) -> 1.0
    ))))

  bn += (av -> bv, "")
  bn += (av -> cv, "")
  bn += (bv -> dv, "")
  bn += (cv -> dv, "")
  bn += (cv -> ev, "")

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