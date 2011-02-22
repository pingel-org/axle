package org.pingel.bayes.examples;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.pingel.bayes.BayesianNetwork;
import org.pingel.bayes.Case;
import org.pingel.bayes.Domain;
import org.pingel.bayes.EliminationTree;
import org.pingel.bayes.EliminationTreeNode;
import org.pingel.bayes.Factor;
import org.pingel.bayes.InteractionGraph;
import org.pingel.bayes.JoinTree;
import org.pingel.bayes.JoinTreeNode;
import org.pingel.bayes.ModelVisualizer;
import org.pingel.bayes.RandomVariable;
import org.pingel.bayes.Value;

public class Figures {

	Value trueValue = new Value("true");
	Value falseValue = new Value("false");
	Domain booleanDomain = new Domain(trueValue, falseValue);

	BayesianNetwork figure6_1 = null;

	RandomVariable A = new RandomVariable("A", booleanDomain);
	RandomVariable B = new RandomVariable("B", booleanDomain);
	RandomVariable C = new RandomVariable("C", booleanDomain);
	RandomVariable D = new RandomVariable("D", booleanDomain);
	RandomVariable E = new RandomVariable("E", booleanDomain);

	public void setFigure6_1()
	{
		if( figure6_1 != null ) {
			return;
		}
		
		figure6_1 = new BayesianNetwork();
		figure6_1.addVariable(A);
		figure6_1.addVariable(B);
		figure6_1.addVariable(C);
		figure6_1.addVariable(D);
		figure6_1.addVariable(E);

		figure6_1.connect(A,B);
		figure6_1.connect(A,C);
		figure6_1.connect(B,D);
		figure6_1.connect(C,D);
		figure6_1.connect(C,E);

		Factor cptA = figure6_1.getCPT(A); // A
		cptA.write(cptA.caseOf(0), 0.6);
		cptA.write(cptA.caseOf(1), 0.4);
		
		Factor cptB = figure6_1.getCPT(B); // B | A
		cptB.write(cptB.caseOf(0), 0.2);
		cptB.write(cptB.caseOf(1), 0.8);
		cptB.write(cptB.caseOf(2), 0.75);
		cptB.write(cptB.caseOf(3), 0.25);

		Factor cptC = figure6_1.getCPT(C); // C | A
		cptC.write(cptC.caseOf(0), 0.8);
		cptC.write(cptC.caseOf(1), 0.2);
		cptC.write(cptC.caseOf(2), 0.1);
		cptC.write(cptC.caseOf(3), 0.9);

		Factor cptD = figure6_1.getCPT(D); // D | BC
		cptD.write(cptD.caseOf(0), 0.95);
		cptD.write(cptD.caseOf(1), 0.05);
		cptD.write(cptD.caseOf(2), 0.9);
		cptD.write(cptD.caseOf(3), 0.1);
		cptD.write(cptD.caseOf(4), 0.8);
		cptD.write(cptD.caseOf(5), 0.2);
		cptD.write(cptD.caseOf(6), 0);
		cptD.write(cptD.caseOf(7), 1);

		Factor cptE = figure6_1.getCPT(E); // E | C
		cptE.write(cptE.caseOf(0), 0.7);
		cptE.write(cptE.caseOf(1), 0.3);
		cptE.write(cptE.caseOf(2), 0);
		cptE.write(cptE.caseOf(3), 1);

		ModelVisualizer.draw(figure6_1);

	}

	Factor figure6_2 = null;
	
	public void setFigure6_2()
	{
		setFigure6_1();

		figure6_2 = figure6_1.getJointProbabilityTable();
		
		figure6_2.print();
	}

	Factor figure6_3_1 = null;
	Factor figure6_3_2 = null;
	
	public void setFigure6_3() {

		figure6_3_1 = new Factor(B, C, D);
		figure6_3_1.write(figure6_3_1.caseOf(0), 0.95);
		figure6_3_1.write(figure6_3_1.caseOf(1), 0.05);
		figure6_3_1.write(figure6_3_1.caseOf(2), 0.9);
		figure6_3_1.write(figure6_3_1.caseOf(3), 0.1);
		figure6_3_1.write(figure6_3_1.caseOf(4), 0.8);
		figure6_3_1.write(figure6_3_1.caseOf(5), 0.2);
		figure6_3_1.write(figure6_3_1.caseOf(6), 0);
		figure6_3_1.write(figure6_3_1.caseOf(7), 1);

		System.out.println("figure3sub1");
		figure6_3_1.print();

		Factor g = figure6_3_1.sumOut(D);
		System.out.println("g");
		g.print();
		
		Factor h = g.sumOut(C);
		System.out.println("h");
		h.print();

		figure6_3_2 = new Factor(D, E);
		figure6_3_2.write(figure6_3_2.caseOf(0), 0.448);
		figure6_3_2.write(figure6_3_2.caseOf(1), 0.192);
		figure6_3_2.write(figure6_3_2.caseOf(2), 0.112);
		figure6_3_2.write(figure6_3_2.caseOf(3), 0.248);

		System.out.println("figure3sub2");
		figure6_3_2.print();

		Factor m = figure6_3_1.multiply(figure6_3_2);
		System.out.println("f1 * f2");
		m.print();
	}

	BayesianNetwork figure6_4 = null;

	public void setFigure6_4()
	{
		if( figure6_4 != null ) {
			return;
		}

		figure6_4 = new BayesianNetwork();
		figure6_4.addVariable(A);
		figure6_4.addVariable(B);
		figure6_4.addVariable(C);

		figure6_4.connect(A,B);
		figure6_4.connect(B,C);

		Factor cptA = figure6_4.getCPT(A); // A
		cptA.write(cptA.caseOf(0), 0.6);
		cptA.write(cptA.caseOf(1), 0.4);
		
		Factor cptB = figure6_4.getCPT(B); // B | A
		cptB.write(cptB.caseOf(0), 0.9);
		cptB.write(cptB.caseOf(1), 0.1);
		cptB.write(cptB.caseOf(2), 0.2);
		cptB.write(cptB.caseOf(3), 0.8);

		Factor cptC = figure6_4.getCPT(C); // C | B
		cptC.write(cptC.caseOf(0), 0.3);
		cptC.write(cptC.caseOf(1), 0.7);
		cptC.write(cptC.caseOf(2), 0.5);
		cptC.write(cptC.caseOf(3), 0.5);

		ModelVisualizer.draw(figure6_4);

		Factor ab = cptA.multiply(cptB);
		System.out.println("cptA * cptB:");
		ab.print();

		Factor blah = ab.sumOut(A);
		System.out.println("sumout(A, cptA * cptB)");
		blah.print();

		Factor foo = blah.multiply(cptC);
		System.out.println("cptC * sumout(A, cptA * cptB)");
		foo.print();
		
		Factor bar = foo.sumOut(C);
		System.out.println("sumout(C, cptC * sumout(A, cptA * cptB))");
		bar.print();
	}
	
	List<InteractionGraph> figure6_5 = null;
	
	public void setFigure6_5()
	{
		setFigure6_1();

		List<RandomVariable> pi = new ArrayList<RandomVariable>();
		
		pi.add(B);
		pi.add(C);
		pi.add(A);
		pi.add(D);

		InteractionGraph G = figure6_1.interactionGraph();
		G.draw();

		figure6_5 = G.eliminationSequence(pi);
		for(InteractionGraph gi : figure6_5) {
			gi.draw();
		}
		
	}

	BayesianNetwork figure6_7_1 = null;
	BayesianNetwork figure6_7_2 = null;
	
	public void setFigure6_7()
	{
		setFigure6_1();

		figure6_7_1 = new BayesianNetwork();
		figure6_1.copyTo(figure6_7_1);
		Set<RandomVariable> Q1 = new HashSet<RandomVariable>();
		Q1.add(B);
		Q1.add(E);
		figure6_7_1.pruneNetwork(Q1, null);
		System.out.println("Figure 6.1 pruned towards " + Q1);
		ModelVisualizer.draw(figure6_7_1);

		figure6_7_2 = new BayesianNetwork();
		figure6_1.copyTo(figure6_7_2);
		Set<RandomVariable> Q2 = new HashSet<RandomVariable>();
		Q2.add(B);
		figure6_7_2.pruneNetwork(Q2, null);
		System.out.println("Figure 6.2 pruned towards " + Q2);
		ModelVisualizer.draw(figure6_7_2);
	}

	BayesianNetwork figure6_8 = null;
	
	public void setFigure6_8()
	{
		setFigure6_1();

		figure6_8 = new BayesianNetwork();
		figure6_1.copyTo(figure6_8);
		
		Case c = new Case();
		c.assign(C, falseValue);
		figure6_8.pruneEdges(c);
		
		System.out.println("Figure 6.1 with edges pruned towards C=false");
		ModelVisualizer.draw(figure6_8);

		for(RandomVariable rv : figure6_8.getRandomVariables()) {
			Factor f = figure6_8.getCPT(rv);
			System.out.println("Factor for " + rv);
			f.print();
		}
	}

	BayesianNetwork figure6_9 = null;
	
	public void setFigure6_9()
	{
		setFigure6_1();

		figure6_9 = new BayesianNetwork();
		figure6_1.copyTo(figure6_9);
		
		Case c = new Case();
		c.assign(A, trueValue);
		c.assign(C, falseValue);
		
		Set<RandomVariable> Q = new HashSet<RandomVariable>();
		Q.add(D);
		
		figure6_9.pruneNetwork(Q, c);
		
		System.out.println("Figure 6.1 pruned towards Q={D} and A=true,C=false");
		ModelVisualizer.draw(figure6_9);
		
		for(RandomVariable rv : figure6_9.getRandomVariables()) {
			Factor f = figure6_9.getCPT(rv);
			System.out.println("Factor for " + rv);
			f.print();
		}
		
	}

	BayesianNetwork figure7_2 = null;
	
	public void setFigure7_2()
	{
		setFigure6_4();

		figure7_2 = new BayesianNetwork();
		figure6_4.copyTo(figure7_2);
		
		Set<RandomVariable> Q = new HashSet<RandomVariable>();
		Q.add(C);
		Factor f = figure7_2.factorElimination1(Q);

		System.out.println("Result of fe-i on a->b->c with Q={C}");
		f.print();
	}

	BayesianNetwork figure7_4 = null;
	EliminationTree τ = new EliminationTree();
	EliminationTreeNode τ_n1 = null;
	EliminationTreeNode τ_n2 = null;
	EliminationTreeNode τ_n3 = null;
	EliminationTreeNode τ_n4 = null;
	EliminationTreeNode τ_n5 = null;
	
	public void setFigure7_4()
	{
		setFigure6_1();
		
		figure7_4 = new BayesianNetwork();
		figure6_1.copyTo(figure7_4);
		
		τ = new EliminationTree();
		
		τ_n1 = new EliminationTreeNode("n1");
		τ.addVertex(τ_n1);
		τ.addFactor(τ_n1, figure7_4.getCPT(A));

		τ_n2 = new EliminationTreeNode("n2");
		τ.addVertex(τ_n2);
		τ.addFactor(τ_n2, figure7_4.getCPT(B));

		τ_n3 = new EliminationTreeNode("n3");
		τ.addVertex(τ_n3);
		τ.addFactor(τ_n3, figure7_4.getCPT(C));

		τ_n4 = new EliminationTreeNode("n4");
		τ.addVertex(τ_n4);
		τ.addFactor(τ_n4, figure7_4.getCPT(D));

		τ_n5 = new EliminationTreeNode("n5");
		τ.addVertex(τ_n5);
		τ.addFactor(τ_n5, figure7_4.getCPT(E));

		τ.addEdge(τ.constructEdge(τ_n1, τ_n2));
		τ.addEdge(τ.constructEdge(τ_n1, τ_n4));
		τ.addEdge(τ.constructEdge(τ_n4, τ_n3));
		τ.addEdge(τ.constructEdge(τ_n3, τ_n5));

		Set<RandomVariable> Q = new HashSet<RandomVariable>();
		Q.add(C);
		Factor result = figure7_4.factorElimination2(Q, τ, τ_n3);

		System.out.println("Doing factorElimination2 on figure6.1 with Q={C} and τ={...} and r=n3");
		result.print();

	}

	BayesianNetwork figure7_5 = null;
	
	public void setFigure7_5()
	{
		setFigure6_1();
		setFigure7_4();
		
		figure7_5 = new BayesianNetwork();
		figure6_1.copyTo(figure7_5);

		Set<RandomVariable> Q = new HashSet<RandomVariable>();
		Q.add(C);
		Factor result = figure7_5.factorElimination2(Q, τ, τ_n3);

		System.out.println("Doing factorElimination3 on figure6.1 with Q={C} and τ={...} and r=n3");
		result.print();

	}

	JoinTree figure7_12 = null;
	
	public void setFigure7_12()
	{
		setFigure6_1();
		
		figure7_12 = new JoinTree();

		JoinTreeNode jtn1 = new JoinTreeNode("jtn1");
		figure7_12.addVertex(jtn1);
		
		JoinTreeNode jtn2 = new JoinTreeNode("jtn2");
		figure7_12.addVertex(jtn2);
		
		JoinTreeNode jtn3 = new JoinTreeNode("jtn3");
		figure7_12.addVertex(jtn3);

		figure7_12.addEdge(figure7_12.constructEdge(jtn1, jtn2));
		figure7_12.addEdge(figure7_12.constructEdge(jtn2, jtn3));

		figure7_12.addToCluster(jtn1, A);
		figure7_12.addToCluster(jtn1, B);
		figure7_12.addToCluster(jtn1, C);

		figure7_12.addToCluster(jtn2, B);
		figure7_12.addToCluster(jtn2, C);
		figure7_12.addToCluster(jtn2, D);

		figure7_12.addToCluster(jtn3, C);
		figure7_12.addToCluster(jtn3, E);

		figure7_12.draw();
		
	}
	
	public static void main(String[] args)
	{
		Figures figures = new Figures();

		figures.setFigure6_1();
		
//		figures.setFigure6_2();

//		figures.setFigure6_3();
	
//		figures.setFigure6_4();

//		figures.setFigure6_5();
	
//		figures.setFigure6_7();

//		figures.setFigure6_8();
	
//		figures.setFigure6_9();

//		figures.setFigure7_2();

		figures.setFigure7_4();

//		figures.setFigure7_5();

//		figures.setFigure7_12();
		
	}
	
}
