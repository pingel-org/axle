package axle.stats.examples

import collection._
import axle.stats._
import axle.visualize._
import axle.graph.JungDirectedGraphFactory._

object AlarmBurglaryEarthquake {

  def main(args: Array[String]) {

    val bools = Some(List(true, false))

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

    val bCase = new CaseX()
    bCase.assign(burglary, true)
    bn.getCPT(burglary)(bCase) = 0.001
    bCase.assign(burglary, false)
    bn.getCPT(burglary)(bCase) = 0.999

    val eCase = new CaseX()
    eCase.assign(earthquake, true)
    bn.getCPT(earthquake)(eCase) = 0.002
    eCase.assign(earthquake, false)
    bn.getCPT(earthquake)(eCase) = 0.998

    val beaCase = new CaseX()
    beaCase.assign(burglary, false)
    beaCase.assign(earthquake, false)
    beaCase.assign(alarm, true)
    bn.getCPT(alarm)(beaCase) = 0.001

    beaCase.assign(alarm, false)
    bn.getCPT(alarm)(beaCase) = 0.999

    beaCase.assign(burglary, true)
    beaCase.assign(earthquake, false)
    beaCase.assign(alarm, true)
    bn.getCPT(alarm)(beaCase) = 0.94
    beaCase.assign(alarm, false)
    bn.getCPT(alarm)(beaCase) = 0.06

    beaCase.assign(burglary, false)
    beaCase.assign(earthquake, true)
    beaCase.assign(alarm, true)
    bn.getCPT(alarm)(beaCase) = 0.29
    beaCase.assign(alarm, false)
    bn.getCPT(alarm)(beaCase) = 0.71

    beaCase.assign(burglary, true)
    beaCase.assign(earthquake, true)
    beaCase.assign(alarm, true)
    bn.getCPT(alarm)(beaCase) = 0.95
    beaCase.assign(alarm, false)
    bn.getCPT(alarm)(beaCase) = 0.05

    val ajCase = new CaseX()

    ajCase.assign(alarm, true)
    ajCase.assign(johnCalls, true)
    bn.getCPT(johnCalls)(ajCase) = 0.9
    ajCase.assign(johnCalls, false)
    bn.getCPT(johnCalls)(ajCase) = 0.1

    ajCase.assign(alarm, false)
    ajCase.assign(johnCalls, true)
    bn.getCPT(johnCalls)(ajCase) = 0.05
    ajCase.assign(johnCalls, false)
    bn.getCPT(johnCalls)(ajCase) = 0.95

    val amCase = new CaseX()

    amCase.assign(alarm, true)
    amCase.assign(maryCalls, true)
    bn.getCPT(maryCalls)(amCase) = 0.7
    amCase.assign(maryCalls, false)
    bn.getCPT(maryCalls)(amCase) = 0.3

    amCase.assign(alarm, false)
    amCase.assign(maryCalls, true)
    bn.getCPT(maryCalls)(amCase) = 0.01
    amCase.assign(maryCalls, false)
    bn.getCPT(maryCalls)(amCase) = 0.99

    new AxleFrame().add(new JungDirectedGraphVisualization(500, 500, 10).component(g))

    bn.printAllMarkovAssumptions()

    println("creating joint probability table")
    val jpt = bn.getJointProbabilityTable()
    jpt.print()

    println("summing out maryCalls")
    val sansMaryCalls = jpt.sumOut(maryCalls)
    sansMaryCalls.print()

    println("summing out johnCalls")
    val sansJohnCalls = sansMaryCalls.sumOut(johnCalls)
    sansJohnCalls.print()

    println("summing out alarm");
    val sansAlarm = sansJohnCalls.sumOut(alarm)
    sansAlarm.print()

    println("summing out burglary")
    val sansBurglary = sansAlarm.sumOut(burglary)
    sansBurglary.print()

    println("summing out earthquake")
    val sansAll = sansBurglary.sumOut(earthquake)
    sansAll.print()

    /*
		double ans1 = burglary.lookup(BooleanVariable.true, new Case())
		println("P(B) = " + ans1) // 0.001
		
		Case burglaryTrue_earthquakeFalse2 = new Case()
		burglaryTrue_earthquakeFalse2.assign(burglary, true)
		burglaryTrue_earthquakeFalse2.assign(earthquake, false)
		double ans2 = alarm.lookup(BooleanVariable.true, burglaryTrue_earthquakeFalse2)
		println("P(A| B, -E) = " + ans2) // 0.94
    */

    println("alarm")
    bn.getCPT(alarm).print()

    println("burglary")
    bn.getCPT(burglary).print()

    val ab = bn.getCPT(alarm).multiply(bn.getCPT(burglary))
    println("ab")
    ab.print()

    val abe = ab.multiply(bn.getCPT(earthquake))
    println("abe")
    abe.print()

    val Q: immutable.Set[RandomVariable[_]] = immutable.Set(earthquake, burglary, alarm)
    val order = List(johnCalls, maryCalls)

    // val afterVE = bn.variableEliminationPriorMarginalI(Q, order)

    val vepr2case = new CaseX()
    vepr2case.assign(earthquake, true)
    val afterVE = bn.variableEliminationPriorMarginalII(Q, order, vepr2case)

    println("eliminating variables other than alarm, burglary, and earthquake; and then finding those consistent with earthquake = true")
    afterVE.print()

  }

}
