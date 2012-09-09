package axle.stats.examples

import collection._
import axle.stats._
import axle.visualize._
import axle.graph.JungDirectedGraphFactory._
import org.specs2.mutable._

class AlarmBurglaryEarthquake extends Specification {

  val bools = Some(Vector(true, false))

  val g = graph[RandomVariable[_], String]()

  val burglary = new RandomVariable0("burglary", bools, None)
  val burglaryVertex = g += burglary

  val earthquake = new RandomVariable0("earthquake", bools, None)
  val earthquakeVertex = g += earthquake

  val alarm = new RandomVariable0("alarm", bools, None)
  val alarmVertex = g += alarm

  val johnCalls = new RandomVariable0("johnCalls", bools, None)
  val johnCallsVertex = g += johnCalls

  val maryCalls = new RandomVariable0("maryCalls", bools, None)
  val maryCallsVertex = g += maryCalls

  g += (burglaryVertex -> alarmVertex, "")
  g += (earthquakeVertex -> alarmVertex, "")
  g += (alarmVertex -> johnCallsVertex, "")
  g += (alarmVertex -> maryCallsVertex, "")

  val bn = new BayesianNetwork("abe", g)

  bn.getCPT(burglary)(List(burglary eq true)) = 0.001
  bn.getCPT(burglary)(List(burglary eq false)) = 0.999

  bn.getCPT(earthquake)(List(earthquake eq true)) = 0.002
  bn.getCPT(earthquake)(List(earthquake eq false)) = 0.998

  bn.getCPT(alarm)(List(burglary eq false, earthquake eq false, alarm eq true)) = 0.001
  bn.getCPT(alarm)(List(burglary eq false, earthquake eq false, alarm eq false)) = 0.999
  bn.getCPT(alarm)(List(burglary eq true, earthquake eq false, alarm eq true)) = 0.94
  bn.getCPT(alarm)(List(burglary eq true, earthquake eq false, alarm eq false)) = 0.06
  bn.getCPT(alarm)(List(burglary eq false, earthquake eq true, alarm eq true)) = 0.29
  bn.getCPT(alarm)(List(burglary eq false, earthquake eq true, alarm eq false)) = 0.71
  bn.getCPT(alarm)(List(burglary eq true, earthquake eq true, alarm eq true)) = 0.95
  bn.getCPT(alarm)(List(burglary eq true, earthquake eq true, alarm eq false)) = 0.05

  bn.getCPT(johnCalls)(List(alarm eq true, johnCalls eq true)) = 0.9
  bn.getCPT(johnCalls)(List(alarm eq true, johnCalls eq false)) = 0.1
  bn.getCPT(johnCalls)(List(alarm eq false, johnCalls eq true)) = 0.05
  bn.getCPT(johnCalls)(List(alarm eq false, johnCalls eq false)) = 0.95

  bn.getCPT(maryCalls)(List(alarm eq true, maryCalls eq true)) = 0.7
  bn.getCPT(maryCalls)(List(alarm eq true, maryCalls eq false)) = 0.3
  bn.getCPT(maryCalls)(List(alarm eq false, maryCalls eq true)) = 0.01
  bn.getCPT(maryCalls)(List(alarm eq false, maryCalls eq false)) = 0.99

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

      //      println("P(B) = " + ans1) // 0.001
      //      println("P(A| B, -E) = " + ans2) // 0.94

      println("eliminating variables other than alarm, burglary, and earthquake; and then finding those consistent with earthquake = true")
      println(afterVE)

      1 must be equalTo 1
    }
  }

}
