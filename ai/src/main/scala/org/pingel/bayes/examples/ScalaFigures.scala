
package org.pingel.bayes.examples

import org.pingel.bayes.BayesianNetwork
import org.pingel.bayes.Case
import org.pingel.bayes.Domain
import org.pingel.bayes.EliminationTree
import org.pingel.bayes.EliminationTreeNode
import org.pingel.bayes.Factor
import org.pingel.bayes.InteractionGraph
import org.pingel.bayes.JoinTree
import org.pingel.bayes.JoinTreeNode
import org.pingel.bayes.ModelVisualizer
import org.pingel.bayes.RandomVariable
import org.pingel.bayes.Value

class ScalaFigures {

  val trueValue = new Value("true")
  val falseValue = new Value("false")
  val booleanDomain = new Domain(trueValue, falseValue)

  var figure6_1: BayesianNetwork = null

  val A = new RandomVariable("A", booleanDomain)
  val B = new RandomVariable("B", booleanDomain)
  val C = new RandomVariable("C", booleanDomain)
  val D = new RandomVariable("D", booleanDomain)
  val E = new RandomVariable("E", booleanDomain)
  
  def setFigure6_1(): Unit = 
  {
    if( figure6_1 != null ) {
      return
    }
    figure6_1 = new BayesianNetwork()
    figure6_1.addVariable(A)
    figure6_1.addVariable(B)
    figure6_1.addVariable(C)
    figure6_1.addVariable(D)
    figure6_1.addVariable(E)

    figure6_1.connect(A,B)
    figure6_1.connect(A,C)
    figure6_1.connect(B,D)
    figure6_1.connect(C,D)
    figure6_1.connect(C,E)

    var cptA = figure6_1.getCPT(A) // A
    cptA.write(cptA.caseOf(0), 0.6)
    cptA.write(cptA.caseOf(1), 0.4)
		
    var cptB = figure6_1.getCPT(B) // B | A
    cptB.write(cptB.caseOf(0), 0.2)
    cptB.write(cptB.caseOf(1), 0.8)
    cptB.write(cptB.caseOf(2), 0.75)
    cptB.write(cptB.caseOf(3), 0.25)

    var cptC = figure6_1.getCPT(C) // C | A
    cptC.write(cptC.caseOf(0), 0.8)
    cptC.write(cptC.caseOf(1), 0.2)
    cptC.write(cptC.caseOf(2), 0.1)
    cptC.write(cptC.caseOf(3), 0.9)

    var cptD = figure6_1.getCPT(D) // D | BC
    cptD.write(cptD.caseOf(0), 0.95)
    cptD.write(cptD.caseOf(1), 0.05)
    cptD.write(cptD.caseOf(2), 0.9)
    cptD.write(cptD.caseOf(3), 0.1)
    cptD.write(cptD.caseOf(4), 0.8)
    cptD.write(cptD.caseOf(5), 0.2)
    cptD.write(cptD.caseOf(6), 0)
    cptD.write(cptD.caseOf(7), 1)

    var cptE = figure6_1.getCPT(E) // E | C
    cptE.write(cptE.caseOf(0), 0.7)
    cptE.write(cptE.caseOf(1), 0.3)
    cptE.write(cptE.caseOf(2), 0)
    cptE.write(cptE.caseOf(3), 1)

    ModelVisualizer.draw(figure6_1)
  }

  var figure6_2: Factor = null
	
  def setFigure6_2(): Unit = {
    setFigure6_1
    figure6_2 = figure6_1.getJointProbabilityTable()
    figure6_2.print
  }

  var figure6_3_1: Factor = null
  var figure6_3_2: Factor = null
	
  def setFigure6_3(): Unit = {

    figure6_3_1 = new Factor(B, C, D)
    figure6_3_1.write(figure6_3_1.caseOf(0), 0.95)
    figure6_3_1.write(figure6_3_1.caseOf(1), 0.05)
    figure6_3_1.write(figure6_3_1.caseOf(2), 0.9)
    figure6_3_1.write(figure6_3_1.caseOf(3), 0.1)
    figure6_3_1.write(figure6_3_1.caseOf(4), 0.8)
    figure6_3_1.write(figure6_3_1.caseOf(5), 0.2)
    figure6_3_1.write(figure6_3_1.caseOf(6), 0)
    figure6_3_1.write(figure6_3_1.caseOf(7), 1)

    Console.println("figure3sub1")
    figure6_3_1.print
    
    val g = figure6_3_1.sumOut(D)
    Console.println("g")
    g.print
		
    val h = g.sumOut(C)
    Console.println("h")
    h.print

    figure6_3_2 = new Factor(D, E)
    figure6_3_2.write(figure6_3_2.caseOf(0), 0.448)
    figure6_3_2.write(figure6_3_2.caseOf(1), 0.192)
    figure6_3_2.write(figure6_3_2.caseOf(2), 0.112)
    figure6_3_2.write(figure6_3_2.caseOf(3), 0.248)

    Console.println("figure3sub2")
    figure6_3_2.print

    val m = figure6_3_1.multiply(figure6_3_2)
    Console.println("f1 * f2")
    m.print
  }

  var figure6_4: BayesianNetwork = null

  def setFigure6_4(): Unit =
  {
    if( figure6_4 != null ) {
      return;
    }

    figure6_4 = new BayesianNetwork()
    figure6_4.addVariable(A)
    figure6_4.addVariable(B)
    figure6_4.addVariable(C)

    figure6_4.connect(A,B)
    figure6_4.connect(B,C)

    var cptA = figure6_4.getCPT(A) // A
    cptA.write(cptA.caseOf(0), 0.6)
    cptA.write(cptA.caseOf(1), 0.4)
		
    var cptB = figure6_4.getCPT(B) // B | A
    cptB.write(cptB.caseOf(0), 0.9)
    cptB.write(cptB.caseOf(1), 0.1)
    cptB.write(cptB.caseOf(2), 0.2)
    cptB.write(cptB.caseOf(3), 0.8)

    var cptC = figure6_4.getCPT(C) // C | B
    cptC.write(cptC.caseOf(0), 0.3)
    cptC.write(cptC.caseOf(1), 0.7)
    cptC.write(cptC.caseOf(2), 0.5)
    cptC.write(cptC.caseOf(3), 0.5)

    ModelVisualizer.draw(figure6_4)

    var ab = cptA.multiply(cptB)
    Console.println("cptA * cptB:")
    ab.print()

    var blah = ab.sumOut(A)
    Console.println("sumout(A, cptA * cptB)")
    blah.print

    var foo = blah.multiply(cptC);
    Console.println("cptC * sumout(A, cptA * cptB)")
    foo.print
    
    var bar = foo.sumOut(C)
    Console.println("sumout(C, cptC * sumout(A, cptA * cptB))")
    bar.print
  }
	
  var figure6_5: List[InteractionGraph] = null
	
  def setFigure6_5(): Unit =
  {
    setFigure6_1

    val pi = List(B, C, A, D)

    val G = figure6_1.interactionGraph()
    G.draw

    figure6_5 = G.eliminationSequence(pi)
    for(gi <- figure6_5) {
      gi.draw();
    }
		
  }

  var figure6_7_1: BayesianNetwork = null
  var figure6_7_2: BayesianNetwork = null
	
  def setFigure6_7(): Unit = {
    setFigure6_1

    figure6_7_1 = new BayesianNetwork()
    figure6_1.copyTo(figure6_7_1)
    val Q1 = Set(B, E)
    figure6_7_1.pruneNetwork(Q1, null)
    Console.println("Figure 6.1 pruned towards " + Q1)
    ModelVisualizer.draw(figure6_7_1)

    figure6_7_2 = new BayesianNetwork()
    figure6_1.copyTo(figure6_7_2)
    val Q2 = Set(B)
    figure6_7_2.pruneNetwork(Q2, null)
    Console.println("Figure 6.2 pruned towards " + Q2)
    ModelVisualizer.draw(figure6_7_2)
  }

  var figure6_8: BayesianNetwork = null
	
  def setFigure6_8(): Unit = {
    setFigure6_1

    figure6_8 = new BayesianNetwork
    figure6_1.copyTo(figure6_8)
		
    var c = new Case()
    c.assign(C, falseValue)
    figure6_8.pruneEdges(c)
		
    Console.println("Figure 6.1 with edges pruned towards C=false")
    ModelVisualizer.draw(figure6_8)

    for( rv <- figure6_8.getRandomVariables ) {
      val f = figure6_8.getCPT(rv)
      Console.println("Factor for " + rv)
      f.print
    }
  }

  var figure6_9: BayesianNetwork = null
	
  def setFigure6_9(): Unit = {
    setFigure6_1

    figure6_9 = new BayesianNetwork()
    figure6_1.copyTo(figure6_9)
		
    var c = new Case()
    c.assign(A, trueValue)
    c.assign(C, falseValue)
		
    figure6_9.pruneNetwork(Set(D), c)
		
    Console.println("Figure 6.1 pruned towards Q={D} and A=true,C=false")
    ModelVisualizer.draw(figure6_9)
		
    for( rv <- figure6_9.getRandomVariables()) {
      val f = figure6_9.getCPT(rv)
      Console.println("Factor for " + rv)
      f.print
    }
		
  }

  var figure7_2: BayesianNetwork = null
	
  def setFigure7_2(): Unit = {
    setFigure6_4
    
    figure7_2 = new BayesianNetwork()
    figure6_4.copyTo(figure7_2)
    val f = figure7_2.factorElimination1(Set(C))
    Console.println("Result of fe-i on a->b->c with Q={C}")
    f.print
  }

  var figure7_4: BayesianNetwork = null
  var τ = new EliminationTree()
  var τ_n1: EliminationTreeNode = null
  var τ_n2: EliminationTreeNode = null
  var τ_n3: EliminationTreeNode = null
  var τ_n4: EliminationTreeNode = null
  var τ_n5: EliminationTreeNode = null
	
  def setFigure7_4(): Unit = {
    setFigure6_1
		
    figure7_4 = new BayesianNetwork()
    figure6_1.copyTo(figure7_4)
		
    τ = new EliminationTree()
		
    τ_n1 = new EliminationTreeNode("n1")
    τ.addVertex(τ_n1)
    τ.addFactor(τ_n1, figure7_4.getCPT(A))

    τ_n2 = new EliminationTreeNode("n2")
    τ.addVertex(τ_n2)
    τ.addFactor(τ_n2, figure7_4.getCPT(B))

    τ_n3 = new EliminationTreeNode("n3")
    τ.addVertex(τ_n3)
    τ.addFactor(τ_n3, figure7_4.getCPT(C))

    τ_n4 = new EliminationTreeNode("n4")
    τ.addVertex(τ_n4)
    τ.addFactor(τ_n4, figure7_4.getCPT(D))

    τ_n5 = new EliminationTreeNode("n5")
    τ.addVertex(τ_n5)
    τ.addFactor(τ_n5, figure7_4.getCPT(E))

    τ.addEdge(τ.constructEdge(τ_n1, τ_n2))
    τ.addEdge(τ.constructEdge(τ_n1, τ_n4))
    τ.addEdge(τ.constructEdge(τ_n4, τ_n3))
    τ.addEdge(τ.constructEdge(τ_n3, τ_n5))

    val result = figure7_4.factorElimination2(Set(C), τ, τ_n3)

    Console.println("Doing factorElimination2 on figure6.1 with Q={C} and τ={...} and r=n3");
    result.print

  }

  var figure7_5: BayesianNetwork = null
	
  def setFigure7_5(): Unit = {
    setFigure6_1
    setFigure7_4
		
    figure7_5 = new BayesianNetwork()
    figure6_1.copyTo(figure7_5)

    var result = figure7_5.factorElimination2(Set(C), τ, τ_n3)

    Console.println("Doing factorElimination3 on figure6.1 with Q={C} and τ={...} and r=n3")
    result.print
  }

  var figure7_12: JoinTree = null
	
  def setFigure7_12(): Unit = {
    setFigure6_1
		
    figure7_12 = new JoinTree()
    
    val jtn1 = new JoinTreeNode("jtn1")
    figure7_12.addVertex(jtn1)
		
    val jtn2 = new JoinTreeNode("jtn2")
    figure7_12.addVertex(jtn2)
		
    val jtn3 = new JoinTreeNode("jtn3")
    figure7_12.addVertex(jtn3)

    figure7_12.addEdge(figure7_12.constructEdge(jtn1, jtn2))
    figure7_12.addEdge(figure7_12.constructEdge(jtn2, jtn3))

    figure7_12.addToCluster(jtn1, A)
    figure7_12.addToCluster(jtn1, B)
    figure7_12.addToCluster(jtn1, C)

    figure7_12.addToCluster(jtn2, B)
    figure7_12.addToCluster(jtn2, C)
    figure7_12.addToCluster(jtn2, D)

    figure7_12.addToCluster(jtn3, C)
    figure7_12.addToCluster(jtn3, E)

    figure7_12.draw
		
  }
	
  def main(args: List[String]): Unit = {
    var figures = new Figures()

    figures.setFigure6_1()
//		figures.setFigure6_2()
//		figures.setFigure6_3()
//		figures.setFigure6_4()
//		figures.setFigure6_5()
//		figures.setFigure6_7()
//		figures.setFigure6_8()
//		figures.setFigure6_9()
//		figures.setFigure7_2()
    figures.setFigure7_4()
//		figures.setFigure7_5()
//		figures.setFigure7_12()
  }
	
}
