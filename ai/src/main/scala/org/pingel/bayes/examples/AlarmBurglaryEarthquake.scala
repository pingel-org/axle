package org.pingel.bayes.examples

import scala.collection._
import org.pingel.bayes.BayesianNetwork
import org.pingel.bayes.Case
import org.pingel.gestalt.core.Domain
import org.pingel.bayes.Factor
import org.pingel.bayes.RandomVariable
import org.pingel.forms.Basic.PBooleans
import org.pingel.forms.Basic.PBooleansValues._

object AlarmBurglaryEarthquake {

  def main(args: Array[String]) {

    val bn = new BayesianNetwork()

    val bools = Some(new PBooleans())

    val burglary = new RandomVariable("burglary", bools)
    bn.addVariable(burglary)
    val earthquake = new RandomVariable("earthquake", bools)
    bn.addVariable(earthquake)
    val alarm = new RandomVariable("alarm", bools)
    bn.addVariable(alarm)
    val johnCalls = new RandomVariable("johnCalls", bools)
    bn.addVariable(johnCalls)
    val maryCalls = new RandomVariable("maryCalls", bools)
    bn.addVariable(maryCalls)

    bn.connect(burglary, alarm)
    bn.connect(earthquake, alarm)
    bn.connect(alarm, johnCalls)
    bn.connect(alarm, maryCalls)

    bn.getCPT(burglary).write(new Case(burglary, tVal), 0.001)
    bn.getCPT(burglary).write(new Case(burglary, fVal), 0.999)

    bn.getCPT(earthquake).write(new Case(earthquake, tVal), 0.002)
    bn.getCPT(earthquake).write(new Case(earthquake, fVal), 0.998)

    val beaCase = new Case()
    beaCase.assign(burglary, fVal)
    beaCase.assign(earthquake, fVal)
    beaCase.assign(alarm, tVal)
    bn.getCPT(alarm).write(beaCase, 0.001)

    beaCase.assign(alarm, fVal)
    bn.getCPT(alarm).write(beaCase, 0.999)

    beaCase.assign(burglary, tVal)
    beaCase.assign(earthquake, fVal)
    beaCase.assign(alarm, tVal)
    bn.getCPT(alarm).write(beaCase, 0.94)
    beaCase.assign(alarm, fVal)
    bn.getCPT(alarm).write(beaCase, 0.06)

    beaCase.assign(burglary, fVal)
    beaCase.assign(earthquake, tVal)
    beaCase.assign(alarm, tVal)
    bn.getCPT(alarm).write(beaCase, 0.29)
    beaCase.assign(alarm, fVal)
    bn.getCPT(alarm).write(beaCase, 0.71)

    beaCase.assign(burglary, tVal)
    beaCase.assign(earthquake, tVal)
    beaCase.assign(alarm, tVal)
    bn.getCPT(alarm).write(beaCase, 0.95)
    beaCase.assign(alarm, fVal)
    bn.getCPT(alarm).write(beaCase, 0.05)

    val ajCase = new Case()

    ajCase.assign(alarm, tVal)
    ajCase.assign(johnCalls, tVal)
    bn.getCPT(johnCalls).write(ajCase, 0.9)
    ajCase.assign(johnCalls, fVal)
    bn.getCPT(johnCalls).write(ajCase, 0.1)

    ajCase.assign(alarm, fVal)
    ajCase.assign(johnCalls, tVal)
    bn.getCPT(johnCalls).write(ajCase, 0.05)
    ajCase.assign(johnCalls, fVal)
    bn.getCPT(johnCalls).write(ajCase, 0.95)

    val amCase = new Case()

    amCase.assign(alarm, tVal)
    amCase.assign(maryCalls, tVal)
    bn.getCPT(maryCalls).write(amCase, 0.7)
    amCase.assign(maryCalls, fVal)
    bn.getCPT(maryCalls).write(amCase, 0.3)

    amCase.assign(alarm, fVal)
    amCase.assign(maryCalls, tVal)
    bn.getCPT(maryCalls).write(amCase, 0.01)
    amCase.assign(maryCalls, fVal)
    bn.getCPT(maryCalls).write(amCase, 0.99)

    bn.g.draw

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
		double ans1 = burglary.lookup(BooleanVariable.tVal, new Case());
		System.out.println("pr(B) = " + ans1); // 0.001
		
		Case burglaryTrue_earthquakeFalse2 = new Case();
		burglaryTrue_earthquakeFalse2.assign(burglary, BooleanVariable.tVal);
		burglaryTrue_earthquakeFalse2.assign(earthquake, BooleanVariable.fVal);
		double ans2 = alarm.lookup(BooleanVariable.tVal, burglaryTrue_earthquakeFalse2);
		System.out.println("pr(A| B, -E) = " + ans2); // 0.94
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

    val Q = Set(earthquake, burglary, alarm)
    val order = List(johnCalls, maryCalls)

    //		ProbabilityTable afterVE = bn.variableEliminationPriorMarginalI(Q, order);

    val vepr2case = new Case()
    vepr2case.assign(earthquake, tVal)
    val afterVE = bn.variableEliminationPriorMarginalII(Q, order, vepr2case)

    println("eliminating variables other than alarm, burglary, and earthquake; and then finding those consistent with earthquake = true")
    afterVE.print()

  }

}
