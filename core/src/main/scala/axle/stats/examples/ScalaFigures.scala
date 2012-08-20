
package org.pingel.bayes.examples

import collection._
import axle.stats._
import axle.visualize._
import axle.graph.JungDirectedGraphFactory._
import axle.graph.JungUndirectedGraphFactory.JungUndirectedGraph

object ScalaFigures {

  def draw(title: String, dg: JungDirectedGraph[RandomVariable[_], String]): Unit = {
    new AxleFrame().add(new JungDirectedGraphVisualization(500, 500, 10).component(dg))
  }

  def draw(title: String, ug: JungUndirectedGraph[RandomVariable[_], String]): Unit = {
    new AxleFrame().add(new JungUndirectedGraphVisualization(500, 500, 10).component(ug))
  }

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

    g += (av -> bv, "")
    g += (av -> cv, "")
    g += (bv -> dv, "")
    g += (cv -> dv, "")
    g += (cv -> ev, "")

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
    println(result)
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
    println(result1)

    val g = result1.sumOut(D)
    println("g")
    println(g)

    val h = g.sumOut(C)
    println("h")
    println(h)

    val result2 = new Factor(D :: E :: Nil)
    result2.writes(0.448 :: 0.192 :: 0.112 :: 0.248 :: Nil)

    println("figure3sub2")
    println(result2)

    val m = result1.multiply(result2)
    println("f1 * f2")
    println(m)

    (result1, result2)
  }

  def figure6_4() = {

    val g = graph[RandomVariable[_], String]()

    val av = g += A
    val bv = g += B
    val cv = g += C

    g += (av -> bv, "")
    g += (bv -> cv, "")

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
    println(ab)

    val blah = ab.sumOut(A)
    println("sumout(A, cptA * cptB)")
    println(blah)

    val foo = blah.multiply(cptC)
    println("cptC * sumout(A, cptA * cptB)")
    println(foo)

    val bar = foo.sumOut(C)
    println("sumout(C, cptC * sumout(A, cptA * cptB))")
    println(bar)

    result
  }

  def figure6_5(): List[InteractionGraph] = {

    val IG = figure6_1().interactionGraph()
    val result = IG.eliminationSequence(List(B, C, A, D))

    draw("figure 6.1 interaction graph", IG.getGraph)
    for (gi <- result) {
      draw("6.1 interaction graph pruned", gi.getGraph)
    }

    result
  }

  def figure6_7() = {

    val f61 = figure6_1()

    val Q1: immutable.Set[RandomVariable[_]] = immutable.Set(B, E)
    val f67pBE = f61.pruneNetworkVarsAndEdges(Q1, None)

    val Q2: immutable.Set[RandomVariable[_]] = immutable.Set(B)
    val f67pB = f61.pruneNetworkVarsAndEdges(Q2, None)

    draw("Figure 6.1 pruned towards " + Q1, f67pBE.getGraph)
    draw("Figure 6.2 pruned towards " + Q2, f67pB.getGraph)

    (f67pBE, f67pB)
  }

  def figure6_8() = {

    val f61 = figure6_1()

    val f68 = f61.pruneEdges("Figure 6.8", Some(List(C eq false)))

    draw("Figure 6.1 with edges pruned towards C=false", f68.getGraph)

    for (rv <- f68.getRandomVariables) {
      val f = f68.getCPT(rv)
      println("Factor for " + rv)
      println(f)
    }
    f68
  }

  def figure6_9() = {

    val f61 = figure6_1()

    val c = List(A eq true, C eq false)

    val f69 = f61.pruneNetworkVarsAndEdges(Set(D), Some(c))
    draw("Figure 6.1 pruned towards Q={D} and A=true,C=false", f69.getGraph)

    for (rv <- f69.getRandomVariables()) {
      val f = f69.getCPT(rv)
      println("Factor for " + rv)
      println(f)
    }

    f69
  }

  def figure7_2() = {

    val result = figure6_4.duplicate()
    val f = result.factorElimination1(Set(C))
    println("Result of fe-i on a->b->c with Q={C}")
    println(f)
    result
  }

  def figure7_4() = {

    val f61 = figure6_1()

    val τ = new EliminationTree()

    val τ_vA = τ.g += f61.getCPT(A)
    val τ_vB = τ.g += f61.getCPT(B)
    val τ_vC = τ.g += f61.getCPT(C)
    val τ_vD = τ.g += f61.getCPT(D)
    val τ_vE = τ.g += f61.getCPT(E)

    τ.g += (τ_vA -> τ_vB, "")
    τ.g += (τ_vA -> τ_vD, "")
    τ.g += (τ_vD -> τ_vC, "")
    τ.g += (τ_vC -> τ_vE, "")

    val (f68, elim) = f61.factorElimination2(Set(C), τ, f61.getCPT(C))

    println("Doing factorElimination2 on figure6.1 with Q={C} and τ={...} and r=n3")
    println(elim)

    (f68, τ, f61.getCPT(C))
  }

  def figure7_5() = {

    val f61 = figure6_1()

    val (bn, τ, cptC) = figure7_4()

    val (f75, elim) = f61.factorElimination2(Set(C), τ, cptC)

    println("Doing factorElimination3 on figure6.1 with Q={C} and τ={...} and r=n3")
    println(elim)
    f61
  }

  def figure7_12() = {
    import axle.graph.JungUndirectedGraphFactory._
    val g = graph[mutable.Set[RandomVariable[_]], String]()
    val vABC = g += mutable.Set(A, B, C)
    val vBCD = g += mutable.Set(B, C, D)
    val vCE = g += mutable.Set(C, E)
    g += (vABC -> vBCD, "")
    g += (vBCD -> vCE, "")
    // draw("figure 7.12", result.g)
    new AxleFrame().add(new JungUndirectedGraphVisualization(500, 500, 10).component(g))
    new JoinTree(g)
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
