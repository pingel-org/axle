
package org.pingel.bayes.examples


import scala.collection._
import org.pingel.bayes.BayesianNetwork
import org.pingel.bayes.Case
import org.pingel.bayes.EliminationTree
import org.pingel.bayes.Factor
import org.pingel.bayes.InteractionGraph
import org.pingel.bayes.JoinTree
import org.pingel.bayes.RandomVariable
import org.pingel.gestalt.core.Domain
import org.pingel.gestalt.core.Value

object ScalaFigures {

  val booleanDomain = Some(new PBooleans())

  val A = new RandomVariable("A", booleanDomain)
  val B = new RandomVariable("B", booleanDomain)
  val C = new RandomVariable("C", booleanDomain)
  val D = new RandomVariable("D", booleanDomain)
  val E = new RandomVariable("E", booleanDomain)

  lazy val figure6_1 = {

    val result = new BayesianNetwork()
    val av = result.g += A
    val bv = result.g += B
    val cv = result.g += C
    val dv = result.g += D
    val ev = result.g += E

    result.g.edge(av, bv, "")
    result.g.edge(av, cv, "")
    result.g.edge(bv, dv, "")
    result.g.edge(cv, dv, "")
    result.g.edge(cv, ev, "")

    val cptA = result.getCPT(A) // A
    cptA.write(cptA.caseOf(0), 0.6)
    cptA.write(cptA.caseOf(1), 0.4)

    val cptB = result.getCPT(B) // B | A
    cptB.write(cptB.caseOf(0), 0.2)
    cptB.write(cptB.caseOf(1), 0.8)
    cptB.write(cptB.caseOf(2), 0.75)
    cptB.write(cptB.caseOf(3), 0.25)

    val cptC = result.getCPT(C) // C | A
    cptC.write(cptC.caseOf(0), 0.8)
    cptC.write(cptC.caseOf(1), 0.2)
    cptC.write(cptC.caseOf(2), 0.1)
    cptC.write(cptC.caseOf(3), 0.9)

    val cptD = result.getCPT(D) // D | BC
    cptD.write(cptD.caseOf(0), 0.95)
    cptD.write(cptD.caseOf(1), 0.05)
    cptD.write(cptD.caseOf(2), 0.9)
    cptD.write(cptD.caseOf(3), 0.1)
    cptD.write(cptD.caseOf(4), 0.8)
    cptD.write(cptD.caseOf(5), 0.2)
    cptD.write(cptD.caseOf(6), 0)
    cptD.write(cptD.caseOf(7), 1)

    val cptE = result.getCPT(E) // E | C
    cptE.write(cptE.caseOf(0), 0.7)
    cptE.write(cptE.caseOf(1), 0.3)
    cptE.write(cptE.caseOf(2), 0)
    cptE.write(cptE.caseOf(3), 1)

    result
  }

  lazy val figure6_2 = {
    val result = figure6_1.getJointProbabilityTable()
    result.print
    result
  }

  lazy val figure6_3 = {

    val result1 = new Factor(B :: C :: D :: Nil)
    result1.write(result1.caseOf(0), 0.95)
    result1.write(result1.caseOf(1), 0.05)
    result1.write(result1.caseOf(2), 0.9)
    result1.write(result1.caseOf(3), 0.1)
    result1.write(result1.caseOf(4), 0.8)
    result1.write(result1.caseOf(5), 0.2)
    result1.write(result1.caseOf(6), 0)
    result1.write(result1.caseOf(7), 1)

    println("figure3sub1")
    result1.print

    val g = result1.sumOut(D)
    println("g")
    g.print

    val h = g.sumOut(C)
    println("h")
    h.print

    val result2 = new Factor(D :: E :: Nil)
    result2.write(result2.caseOf(0), 0.448)
    result2.write(result2.caseOf(1), 0.192)
    result2.write(result2.caseOf(2), 0.112)
    result2.write(result2.caseOf(3), 0.248)

    println("figure3sub2")
    result2.print

    val m = result1.multiply(result2)
    println("f1 * f2")
    m.print
    
    (result1, result2)
  }

  lazy val figure6_4 = {

    val result = new BayesianNetwork()
    val av = result.g += A
    val bv = result.g += B
    val cv = result.g += C

    result.g.edge(av, bv, "")
    result.g.edge(bv, cv, "")

    val cptA = result.getCPT(A) // A
    cptA.write(cptA.caseOf(0), 0.6)
    cptA.write(cptA.caseOf(1), 0.4)

    val cptB = result.getCPT(B) // B | A
    cptB.write(cptB.caseOf(0), 0.9)
    cptB.write(cptB.caseOf(1), 0.1)
    cptB.write(cptB.caseOf(2), 0.2)
    cptB.write(cptB.caseOf(3), 0.8)

    val cptC = result.getCPT(C) // C | B
    cptC.write(cptC.caseOf(0), 0.3)
    cptC.write(cptC.caseOf(1), 0.7)
    cptC.write(cptC.caseOf(2), 0.5)
    cptC.write(cptC.caseOf(3), 0.5)

    // result.g.draw

    val ab = cptA.multiply(cptB)
    println("cptA * cptB:")
    ab.print()

    val blah = ab.sumOut(A)
    println("sumout(A, cptA * cptB)")
    blah.print

    val foo = blah.multiply(cptC);
    println("cptC * sumout(A, cptA * cptB)")
    foo.print

    val bar = foo.sumOut(C)
    println("sumout(C, cptC * sumout(A, cptA * cptB))")
    bar.print
    
    result
  }

  lazy val figure6_5: List[InteractionGraph] = {

    val pi = List(B, C, A, D)

    val G = figure6_1.interactionGraph()
    G.g.draw

    val result = G.eliminationSequence(pi)
    for (gi <- result) {
      gi.g.draw
    }
    result
  }

  lazy val figure6_7 = {

    val result1 = new BayesianNetwork()
    figure6_1.copyTo(result1)
    val Q1 = Set(B, E)
    result1.pruneNetwork(Q1, null)
    println("Figure 6.1 pruned towards " + Q1)
    result2.g.draw

    val result2 = new BayesianNetwork()
    figure6_1.copyTo(result2)
    val Q2 = Set(B)
    result2.pruneNetwork(Q2, null)
    println("Figure 6.2 pruned towards " + Q2)
    result2.g.draw
    
    (result1, result2)
  }

  lazy val figure6_8 = {

    val result = new BayesianNetwork()
    figure6_1.copyTo(result)

    var c = new Case()
    c.assign(C, falseValue)
    result.pruneEdges(c)

    println("Figure 6.1 with edges pruned towards C=false")
    result.g.draw

    for (rv <- result.getRandomVariables) {
      val f = result.getCPT(rv)
      println("Factor for " + rv)
      f.print
    }
    result
  }

  lazy val figure6_9 = {

    val result = new BayesianNetwork()
    figure6_1.copyTo(result)

    val c = new Case()
    c.assign(A, trueValue)
    c.assign(C, falseValue)

    result.pruneNetwork(Set(D), c)

    println("Figure 6.1 pruned towards Q={D} and A=true,C=false")
    result.g.draw

    for (rv <- result.getRandomVariables()) {
      val f = result.getCPT(rv)
      println("Factor for " + rv)
      f.print
    }

    result
  }

  lazy val figure7_2 = {

    val result = new BayesianNetwork()
    figure6_4.copyTo(result)
    val f = result.factorElimination1(Set(C))
    println("Result of fe-i on a->b->c with Q={C}")
    f.print
    result
  }

  lazy val figure7_4 = {

    val result = new BayesianNetwork()
    figure6_1.copyTo(result)

    val τ = new EliminationTree()

    val τ_n1 = τ.g.vertex(result.getCPT(A))
    val τ_n2 = τ.g.vertex(result.getCPT(B))
    val τ_n3 = τ.g.vertex(result.getCPT(C))
    val τ_n4 = τ.g.vertex(result.getCPT(D))
    val τ_n5 = τ.g.vertex(result.getCPT(E))

    τ.g.edge(τ_n1, τ_n2, "")
    τ.g.edge(τ_n1, τ_n4, "")
    τ.g.edge(τ_n4, τ_n3, "")
    τ.g.edge(τ_n3, τ_n5, "")

    val elim = result.factorElimination2(Set(C), τ, τ_n3)

    println("Doing factorElimination2 on figure6.1 with Q={C} and τ={...} and r=n3")
    elim.print
    
    (result, τ, τ_n3)
  }

  lazy val figure7_5 = {

    val result = new BayesianNetwork()
    figure6_1.copyTo(result)

    val (bn, τ, τ_n3) = figure7_4
    
    val elim = result.factorElimination2(Set(C), τ, τ_n3)

    println("Doing factorElimination3 on figure6.1 with Q={C} and τ={...} and r=n3")
    elim.print
    result
  }

  lazy val figure7_12 = {
    val result = new JoinTree()
    val jtn1 = result.g.vertex(mutable.Set(A, B, C))
    val jtn2 = result.g.vertex(mutable.Set(B, C, D))
    val jtn3 = result.g.vertex(mutable.Set(C, E))
    result.g.edge(jtn1, jtn2, "")
    result.g.edge(jtn2, jtn3, "")
    result.g.draw
    result
  }

  def main(args: List[String]): Unit = {

    figure6_1.g.draw
    //figure6_2
    //figure6_3
    //figure6_4
    //figure6_5
    //figure6_7
    //figure6_8
    //figure6_9
    //figure7_2
    //figure7_4.g.draw
    //figure7_5
    //figure7_12
  }

}
