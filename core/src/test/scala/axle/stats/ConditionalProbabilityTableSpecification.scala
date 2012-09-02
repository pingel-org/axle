package axle.stats

import org.specs2.mutable._
import collection._
import axle.graph.JungDirectedGraphFactory._
import axle.graph.JungUndirectedGraphFactory.JungUndirectedGraph

class ConditionalProbabilityTableSpecification extends Specification {

  "CPT" should {
    "work" in {

      val bools = Some(List(true, false))

      val A = new RandomVariable0("A", bools, None)
      val B = new RandomVariable0("B", bools, None)
      val C = new RandomVariable0("C", bools, None)
      val D = new RandomVariable0("D", bools, None)
      val E = new RandomVariable0("E", bools, None)

      val g = graph[RandomVariable[_], String]()

      val av = g += A
      val bv = g += B
      val cv = g += C
      val dv = g += D
      val ev = g += E

      g += (av -> bv, "")
      g += (av -> cv, "")
      g += (bv -> dv, "")
      g += (cv -> dv, "")
      g += (cv -> ev, "")

      val result = new BayesianNetwork("6.1", g)

      val cptA = result.getCPT(A) // A
      cptA(List(A eq true)) = 0.6
      cptA(List(A eq false)) = 0.4

      val cptB = result.getCPT(B) // B | A
      cptB(List(B eq true, A eq true)) = 0.2
      cptB(List(B eq true, A eq false)) = 0.8
      cptB(List(B eq false, A eq true)) = 0.75
      cptB(List(B eq false, A eq false)) = 0.25

      val cptC = result.getCPT(C) // C | A
      cptC(List(C eq true, A eq true)) = 0.8
      cptC(List(C eq true, A eq false)) = 0.2
      cptC(List(C eq false, A eq true)) = 0.1
      cptC(List(C eq false, A eq false)) = 0.9

      val cptD = result.getCPT(D) // D | BC
      cptD(List(D eq true, B eq true, C eq true)) = 0.95
      cptD(List(D eq true, B eq true, C eq false)) = 0.05
      cptD(List(D eq true, B eq false, C eq true)) = 0.9
      cptD(List(D eq true, B eq false, C eq false)) = 0.1
      cptD(List(D eq false, B eq true, C eq true)) = 0.8
      cptD(List(D eq false, B eq true, C eq false)) = 0.2
      cptD(List(D eq false, B eq false, C eq true)) = 0.0
      cptD(List(D eq false, B eq false, C eq false)) = 1.0

      val cptE = result.getCPT(E) // E | C
      cptE(List(E eq true, C eq true)) = 0.7
      cptE(List(E eq true, C eq false)) = 0.3
      cptE(List(E eq false, C eq true)) = 0.0
      cptE(List(E eq false, C eq false)) = 1.0

      for (kase <- cptB.cases) {
        for (caseIs <- kase) {
          println(caseIs.rv + " " + caseIs.v)
        }
      }

      1 must be equalTo (1)

    }
  }

}