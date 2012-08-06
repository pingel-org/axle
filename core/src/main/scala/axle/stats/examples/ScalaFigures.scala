
package org.pingel.bayes.examples

import collection._
import axle.stats._
import axle.visualize._
import axle.graph.JungDirectedGraphFactory._

object ScalaFigures {

  val bools = Some(List(true, false))

  val A = new RandomVariable0("A", bools, None)
  val B = new RandomVariable0("B", bools, None)
  val C = new RandomVariable0("C", bools, None)
  val D = new RandomVariable0("D", bools, None)
  val E = new RandomVariable0("E", bools, None)

  def figure6_1() = {

    val g = graph[RandomVariable[_], String]()

    val av = g += A
    val bv = g += B
    val cv = g += C
    val dv = g += D
    val ev = g += E

    g.edge(av, bv, "")
    g.edge(av, cv, "")
    g.edge(bv, dv, "")
    g.edge(cv, dv, "")
    g.edge(cv, ev, "")

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

    // new AxleFrame().add(new JungUndirectedGraphVisualization(500, 500, 10).component(g))

    result
  }

  def figure6_2() = {
    val result = figure6_1.getJointProbabilityTable()
    result.print
    result
  }

  def figure6_3() = {

    val result1 = new Factor(B :: C :: D :: Nil)
    result1.writes(
      0.95 :: 0.05 ::
        0.9 :: 0.1 ::
        0.8 :: 0.2 ::
        0.0 :: 1.0 ::
        Nil)

    println("figure3sub1")
    result1.print

    val g = result1.sumOut(D)
    println("g")
    g.print

    val h = g.sumOut(C)
    println("h")
    h.print

    val result2 = new Factor(D :: E :: Nil)
    result2.writes(0.448 :: 0.192 :: 0.112 :: 0.248 :: Nil)

    println("figure3sub2")
    result2.print

    val m = result1.multiply(result2)
    println("f1 * f2")
    m.print

    (result1, result2)
  }

  def figure6_4() = {

    val g = graph[RandomVariable[_], String]()

    val av = g += A
    val bv = g += B
    val cv = g += C

    g.edge(av, bv, "")
    g.edge(bv, cv, "")

    val result = new BayesianNetwork("6.4", g)

    val cptA = result.getCPT(A) // A
    cptA.writes(0.6 :: 0.4 :: Nil)

    val cptB = result.getCPT(B) // B | A
    cptB.writes(0.9 :: 0.1 :: 0.2 :: 0.8 :: Nil)

    val cptC = result.getCPT(C) // C | B
    cptC.writes(0.3 :: 0.7 :: 0.5 :: 0.5 :: Nil)

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

  def figure6_5(): List[InteractionGraph] = {

    val π = List(B, C, A, D)

    val G = figure6_1().interactionGraph()

    new AxleFrame().add(new JungDirectedGraphVisualization(500, 500, 10).component(G.getGraph))

    val result = G.eliminationSequence(π)
    for (gi <- result) {
      new AxleFrame().add(new JungDirectedGraphVisualization(500, 500, 10).component(gi.getGraph))
    }
    result
  }

  def figure6_7() = {

    val f61 = figure6_1()

    val Q1 = Set(B, E)
    val f67pBE = f61.pruneNetworkVarsAndEdges(Q1, None)
    println("Figure 6.1 pruned towards " + Q1)
    new AxleFrame().add(new JungUndirectedGraphVisualization(500, 500, 10).component(f67pBE.g))

    val Q2 = Set(B)
    val f67pB = f61.pruneNetworkVarsAndEdges(Q2, None)
    println("Figure 6.2 pruned towards " + Q2)
    new AxleFrame().add(new JungUndirectedGraphVisualization(500, 500, 10).component(f67pB.g))

    (f67pBE, f67pB)
  }

  def figure6_8() = {

    val f61 = figure6_1()

    val c = new CaseX()
    c.assign(C, false)
    val f68 = new BayesianNetwork("f68", f61.pruneEdges(Some(c), f61.getGraph))

    println("Figure 6.1 with edges pruned towards C=false")
    new AxleFrame().add(new JungUndirectedGraphVisualization(500, 500, 10).component(f68.getGraph))

    for (rv <- f68.getRandomVariables) {
      val f = f68.getCPT(rv)
      println("Factor for " + rv)
      f.print
    }
    f68
  }

  def figure6_9() = {

    val f61 = figure6_1()

    val c = new CaseX()
    c.assign(A, true)
    c.assign(C, false)

    val f69 = f61.pruneNetworkVarsAndEdges(Set(D), Some(c))

    println("Figure 6.1 pruned towards Q={D} and A=true,C=false")
    new AxleFrame().add(new JungUndirectedGraphVisualization(500, 500, 10).component(f69.getGraph))

    for (rv <- f69.getRandomVariables()) {
      val f = f69.getCPT(rv)
      println("Factor for " + rv)
      f.print
    }

    f69
  }

  def figure7_2() = {

    val result = figure6_4.duplicate()
    val f = result.factorElimination1(Set(C))
    println("Result of fe-i on a->b->c with Q={C}")
    f.print
    result
  }

  def figure7_4() = {

    val f61 = figure6_1()

    val τ = new EliminationTree()

    val τ_n1 = τ.g.vertex(f61.getCPT(A))
    val τ_n2 = τ.g.vertex(f61.getCPT(B))
    val τ_n3 = τ.g.vertex(f61.getCPT(C))
    val τ_n4 = τ.g.vertex(f61.getCPT(D))
    val τ_n5 = τ.g.vertex(f61.getCPT(E))

    τ.g.edge(τ_n1, τ_n2, "")
    τ.g.edge(τ_n1, τ_n4, "")
    τ.g.edge(τ_n4, τ_n3, "")
    τ.g.edge(τ_n3, τ_n5, "")

    val (f68, elim) = f61.factorElimination2(Set(C), τ, τ_n3)

    println("Doing factorElimination2 on figure6.1 with Q={C} and τ={...} and r=n3")
    elim.print

    (f68, τ, τ_n3)
  }

  def figure7_5() = {

    val f61 = figure6_1()

    val (bn, τ, τ_n3) = figure7_4()

    val (f75, elim) = f61.factorElimination2(Set(C), τ, τ_n3)

    println("Doing factorElimination3 on figure6.1 with Q={C} and τ={...} and r=n3")
    elim.print
    f61
  }

  def figure7_12() = {
    val result = new JoinTree()
    val jtn1 = result.g.vertex(mutable.Set(A, B, C))
    val jtn2 = result.g.vertex(mutable.Set(B, C, D))
    val jtn3 = result.g.vertex(mutable.Set(C, E))
    result.g.edge(jtn1, jtn2, "")
    result.g.edge(jtn2, jtn3, "")
    new AxleFrame().add(new JungUndirectedGraphVisualization(500, 500, 10).component(result.g))
    result
  }

  def main(args: List[String]): Unit = {

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
