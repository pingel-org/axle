package org.pingel.bayes.examples;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.pingel.bayes.BayesianNetwork;
import org.pingel.bayes.Case;
import org.pingel.bayes.Domain;
import org.pingel.bayes.Factor;
import org.pingel.bayes.ModelVisualizer;
import org.pingel.bayes.RandomVariable;
import org.pingel.type.Booleans;

public class doit {

	public static void main(String[] args) {
		
		BayesianNetwork bn = new BayesianNetwork();
		
		Domain bools = new Booleans();
		
		RandomVariable burglary = new RandomVariable("burglary", bools, "b");
        bn.addVariable(burglary);
		RandomVariable earthquake = new RandomVariable("earthquake", bools, "e");
        bn.addVariable(earthquake);
		RandomVariable alarm = new RandomVariable("alarm", bools, "a");
        bn.addVariable(alarm);
		RandomVariable johnCalls = new RandomVariable("johnCalls", bools, "j");
        bn.addVariable(johnCalls);
		RandomVariable maryCalls = new RandomVariable("maryCalls", bools, "m");
        bn.addVariable(maryCalls);

		bn.connect(burglary, alarm);
		bn.connect(earthquake, alarm);
		bn.connect(alarm, johnCalls);
		bn.connect(alarm, maryCalls);
		
		bn.getCPT(burglary).write(new Case(burglary, Booleans.tVal), 0.001);
		bn.getCPT(burglary).write(new Case(burglary, Booleans.fVal), 0.999);

		bn.getCPT(earthquake).write(new Case(earthquake, Booleans.tVal), 0.002);
		bn.getCPT(earthquake).write(new Case(earthquake, Booleans.fVal), 0.998);

		Case beaCase = new Case();
		beaCase.assign(burglary, Booleans.fVal);
		beaCase.assign(earthquake, Booleans.fVal);
		beaCase.assign(alarm, Booleans.tVal);
		bn.getCPT(alarm).write(beaCase, 0.001);

		beaCase.assign(alarm, Booleans.fVal);
		bn.getCPT(alarm).write(beaCase, 0.999);

		beaCase.assign(burglary, Booleans.tVal);
		beaCase.assign(earthquake, Booleans.fVal);
		beaCase.assign(alarm, Booleans.tVal);
		bn.getCPT(alarm).write(beaCase, 0.94);
		beaCase.assign(alarm, Booleans.fVal);
		bn.getCPT(alarm).write(beaCase, 0.06);

		beaCase.assign(burglary, Booleans.fVal);
		beaCase.assign(earthquake, Booleans.tVal);
		beaCase.assign(alarm, Booleans.tVal);
		bn.getCPT(alarm).write(beaCase, 0.29);
		beaCase.assign(alarm, Booleans.fVal);
		bn.getCPT(alarm).write(beaCase, 0.71);

		beaCase.assign(burglary, Booleans.tVal);
		beaCase.assign(earthquake, Booleans.tVal);
		beaCase.assign(alarm, Booleans.tVal);
		bn.getCPT(alarm).write(beaCase, 0.95);
		beaCase.assign(alarm, Booleans.fVal);
		bn.getCPT(alarm).write(beaCase, 0.05);

		
		Case ajCase = new Case();

		ajCase.assign(alarm, Booleans.tVal);
		ajCase.assign(johnCalls, Booleans.tVal);
		bn.getCPT(johnCalls).write(ajCase, 0.9);
		ajCase.assign(johnCalls, Booleans.fVal);
		bn.getCPT(johnCalls).write(ajCase, 0.1);

		ajCase.assign(alarm, Booleans.fVal);
		ajCase.assign(johnCalls, Booleans.tVal);
		bn.getCPT(johnCalls).write(ajCase, 0.05);
		ajCase.assign(johnCalls, Booleans.fVal);
		bn.getCPT(johnCalls).write(ajCase, 0.95);

		Case amCase = new Case();

		amCase.assign(alarm, Booleans.tVal);
		amCase.assign(maryCalls, Booleans.tVal);
		bn.getCPT(maryCalls).write(amCase, 0.7);
		amCase.assign(maryCalls, Booleans.fVal);
		bn.getCPT(maryCalls).write(amCase, 0.3);

		amCase.assign(alarm, Booleans.fVal);
		amCase.assign(maryCalls, Booleans.tVal);
		bn.getCPT(maryCalls).write(amCase, 0.01);
		amCase.assign(maryCalls, Booleans.fVal);
		bn.getCPT(maryCalls).write(amCase, 0.99);

		ModelVisualizer.draw(bn);
		
		bn.printAllMarkovAssumptions();

		System.out.println("creating joint probability table");
		Factor jpt = bn.getJointProbabilityTable();
		jpt.print();

		System.out.println("summing out maryCalls");
		Factor sansMaryCalls = jpt.sumOut(maryCalls);
		sansMaryCalls.print();
		
		System.out.println("summing out johnCalls");
		Factor sansJohnCalls = sansMaryCalls.sumOut(johnCalls);
		sansJohnCalls.print();
		
		System.out.println("summing out alarm");
		Factor sansAlarm = sansJohnCalls.sumOut(alarm);
		sansAlarm.print();
		
		System.out.println("summing out burglary");
		Factor sansBurglary = sansAlarm.sumOut(burglary);
		sansBurglary.print();
		
		System.out.println("summing out earthquake");
		Factor sansAll = sansBurglary.sumOut(earthquake);
		sansAll.print();
		
		/*
		double ans1 = burglary.lookup(BooleanVariable.tVal, new Case());
		System.out.println("pr(B) = " + ans1); // 0.001
		
		Case burglaryTrue_earthquakeFalse2 = new Case();
		burglaryTrue_earthquakeFalse2.assign(burglary, BooleanVariable.tVal);
		burglaryTrue_earthquakeFalse2.assign(earthquake, BooleanVariable.fVal);
		double ans2 = alarm.lookup(BooleanVariable.tVal, burglaryTrue_earthquakeFalse2);
		System.out.println("pr(A| B, -E) = " + ans2); // 0.94
		*/
		
		System.out.println("alarm");
		bn.getCPT(alarm).print();
		
		System.out.println("burglary");
		bn.getCPT(burglary).print();
		
		Factor ab = bn.getCPT(alarm).multiply(bn.getCPT(burglary));
		System.out.println("ab");
		ab.print();
		
		Factor abe = ab.multiply(bn.getCPT(earthquake));
		System.out.println("abe");
		abe.print();
	
		Set<RandomVariable> Q = new HashSet<RandomVariable>();
		Q.add(earthquake);
		Q.add(burglary);
		Q.add(alarm);
		List<RandomVariable> order = new Vector<RandomVariable>();
		order.add(johnCalls);
		order.add(maryCalls);

//		ProbabilityTable afterVE = bn.variableEliminationPriorMarginalI(Q, order);
		
		Case vepr2case = new Case();
		vepr2case.assign(earthquake, Booleans.tVal);
		Factor afterVE = bn.variableEliminationPriorMarginalII(Q, order, vepr2case);

		System.out.println("eliminating variables other than alarm, burglary, and earthquake; and then finding those consistent with earthquake = true");
		afterVE.print();

	}

}
