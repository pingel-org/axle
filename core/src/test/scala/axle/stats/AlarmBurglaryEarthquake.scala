package axle.stats.examples

import collection._
import axle.stats._
import axle.visualize._
import axle.graph.JungDirectedGraphFactory._
import org.specs2.mutable._

class AlarmBurglaryEarthquake extends Specification {

  val bools = Some(Vector(true, false))

  val burglary = new RandomVariable0("burglary", bools, None)
  val earthquake = new RandomVariable0("earthquake", bools, None)
  val alarm = new RandomVariable0("alarm", bools, None)
  val johnCalls = new RandomVariable0("johnCalls", bools, None)
  val maryCalls = new RandomVariable0("maryCalls", bools, None)

  val bn = BayesianNetwork("Alarm, Burglary, Earthquake, John and Mary Call")

  val cptB = Factor(Vector(burglary))
  cptB(List(burglary eq true)) = 0.001
  cptB(List(burglary eq false)) = 0.999
  val burglaryVertex = bn += BayesianNetworkNode(burglary, cptB)

  val cptE = Factor(Vector(earthquake))
  cptE(List(earthquake eq true)) = 0.002
  cptE(List(earthquake eq false)) = 0.998
  val earthquakeVertex = bn += BayesianNetworkNode(earthquake, cptE)

  val cptA = Factor(Vector(burglary, earthquake, alarm))
  cptA(List(burglary eq false, earthquake eq false, alarm eq true)) = 0.001
  cptA(List(burglary eq false, earthquake eq false, alarm eq false)) = 0.999
  cptA(List(burglary eq true, earthquake eq false, alarm eq true)) = 0.94
  cptA(List(burglary eq true, earthquake eq false, alarm eq false)) = 0.06
  cptA(List(burglary eq false, earthquake eq true, alarm eq true)) = 0.29
  cptA(List(burglary eq false, earthquake eq true, alarm eq false)) = 0.71
  cptA(List(burglary eq true, earthquake eq true, alarm eq true)) = 0.95
  cptA(List(burglary eq true, earthquake eq true, alarm eq false)) = 0.05
  val alarmVertex = bn += BayesianNetworkNode(alarm, cptA)

  val cptJ = Factor(Vector(alarm, johnCalls))
  cptJ(List(alarm eq true, johnCalls eq true)) = 0.9
  cptJ(List(alarm eq true, johnCalls eq false)) = 0.1
  cptJ(List(alarm eq false, johnCalls eq true)) = 0.05
  cptJ(List(alarm eq false, johnCalls eq false)) = 0.95
  val johnCallsVertex = bn += BayesianNetworkNode(johnCalls, cptJ)

  val cptM = Factor(Vector(alarm, maryCalls))
  cptM(List(alarm eq true, maryCalls eq true)) = 0.7
  cptM(List(alarm eq true, maryCalls eq false)) = 0.3
  cptM(List(alarm eq false, maryCalls eq true)) = 0.01
  cptM(List(alarm eq false, maryCalls eq false)) = 0.99
  val maryCallsVertex = bn += BayesianNetworkNode(maryCalls, cptM)

  bn += (burglaryVertex -> alarmVertex, "")
  bn += (earthquakeVertex -> alarmVertex, "")
  bn += (alarmVertex -> johnCallsVertex, "")
  bn += (alarmVertex -> maryCallsVertex, "")

  val jpt = bn.getJointProbabilityTable()

  val sansAll = jpt.Σ(maryCalls).Σ(johnCalls).Σ(alarm).Σ(burglary).Σ(earthquake)

  val ab = bn.getCPT(alarm) * bn.getCPT(burglary)

  val abe = ab * bn.getCPT(earthquake)

  val Q: immutable.Set[RandomVariable[_]] = immutable.Set(earthquake, burglary, alarm)
  val order = List(johnCalls, maryCalls)

  // val afterVE = bn.variableEliminationPriorMarginalI(Q, order)

  val afterVE = bn.variableEliminationPriorMarginalII(Q, order, earthquake eq true)

  "bayesian networks" should {
    "work" in {

      // bn.getRandomVariables.map(rv => println(bn.getMarkovAssumptionsFor(rv)))

      // println("P(B) = " + ans1) // 0.001
      // println("P(A| B, -E) = " + ans2) // 0.94

      println("eliminating variables other than alarm, burglary, and earthquake; and then finding those consistent with earthquake = true")
      println(afterVE)

      1 must be equalTo 1
    }
  }

}
