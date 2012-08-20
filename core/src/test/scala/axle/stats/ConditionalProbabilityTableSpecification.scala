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
      cptA.writes(0.6 :: 0.4 :: Nil)

      val cptB = result.getCPT(B) // B | A
      cptB.writes(
        0.2 :: 0.8 ::
          0.75 :: 0.25 ::
          Nil)

      val cptC = result.getCPT(C) // C | A
      cptC.writes(
        0.8 :: 0.2 ::
          0.1 :: 0.9 ::
          Nil)

      val cptD = result.getCPT(D) // D | BC
      cptD.writes(
        0.95 :: 0.5 ::
          0.9 :: 0.1 ::
          0.8 :: 0.2 ::
          0.0 :: 1.0 ::
          Nil)

      val cptE = result.getCPT(E) // E | C
      cptE.writes(
        0.7 :: 0.3 ::
          0.0 :: 1.0 ::
          Nil)

      val cB0 = cptB.caseOf(0)
      for (caseIs <- cB0) {
        println(caseIs.rv + " " + caseIs.v)
      }

      1 must be equalTo (1)

    }
  }

}