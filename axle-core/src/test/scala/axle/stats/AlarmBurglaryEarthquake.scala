package axle.stats

import collection._
import axle.stats._
import axle.graph._
import axle.pgm._
import org.specs2.mutable._

class ABE extends Specification {

  import BayesianNetworkModule._
  import FactorModule._

  val bools = Some(Vector(true, false))

  val B = new RandomVariable0("Burglary", bools, None)
  val E = new RandomVariable0("Earthquake", bools, None)
  val A = new RandomVariable0("Alarm", bools, None)
  val J = new RandomVariable0("John Calls", bools, None)
  val M = new RandomVariable0("Mary Calls", bools, None)

  val bn = BayesianNetwork(
    "A sounds (due to Burglary or Earthquake) and John or Mary Call",
    List(BayesianNetworkNode(B,
      Factor(Vector(B), Map(
        List(B is true) -> 0.001,
        List(B is false) -> 0.999
      ))),
      BayesianNetworkNode(E,
        Factor(Vector(E), Map(
          List(E is true) -> 0.002,
          List(E is false) -> 0.998
        ))),
      BayesianNetworkNode(A,
        Factor(Vector(B, E, A), Map(
          List(B is false, E is false, A is true) -> 0.001,
          List(B is false, E is false, A is false) -> 0.999,
          List(B is true, E is false, A is true) -> 0.94,
          List(B is true, E is false, A is false) -> 0.06,
          List(B is false, E is true, A is true) -> 0.29,
          List(B is false, E is true, A is false) -> 0.71,
          List(B is true, E is true, A is true) -> 0.95,
          List(B is true, E is true, A is false) -> 0.05))),
      BayesianNetworkNode(J,
        Factor(Vector(A, J), Map(
          List(A is true, J is true) -> 0.9,
          List(A is true, J is false) -> 0.1,
          List(A is false, J is true) -> 0.05,
          List(A is false, J is false) -> 0.95
        ))),
      BayesianNetworkNode(M,
        Factor(Vector(A, M), Map(
          List(A is true, M is true) -> 0.7,
          List(A is true, M is false) -> 0.3,
          List(A is false, M is true) -> 0.01,
          List(A is false, M is false) -> 0.99
        )))),
    (vs: Seq[Vertex[BayesianNetworkNode]]) => vs match {
      case b :: e :: a :: j :: m :: Nil => List((b, a, ""), (e, a, ""), (a, j, ""), (a, m, ""))
      case _ => Nil
    })

  // val (bn, es): (BayesianNetwork, Seq[BayesianNetwork#E]) = 

  "bayesian networks" should {
    "work" in {

      val jpt = bn.jointProbabilityTable()

      val sansAll = jpt.Σ(M).Σ(J).Σ(A).Σ(B).Σ(E)

      val abe = (bn.cpt(A) * bn.cpt(B)) * bn.cpt(E)

      val Q: immutable.Set[RandomVariable[_]] = immutable.Set(E, B, A)
      val order = List(J, M)

      // val afterVE = bn.variableEliminationPriorMarginalI(Q, order)
      // val afterVE = bn.variableEliminationPriorMarginalII(Q, order, E is true)

      // bn.getRandomVariables.map(rv => println(bn.getMarkovAssumptionsFor(rv)))

      // println("P(B) = " + ans1) // 0.001
      // println("P(A| B, -E) = " + ans2) // 0.94

      // println("eliminating variables other than A, B, and E; and then finding those consistent with E = true")
      // println(afterVE)

      1 must be equalTo 1
    }
  }

}
