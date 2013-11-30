
package axle.pgm

import axle._
import axle.stats._
import axle.graph._
import axle.pgm._
import org.specs2.mutable._
import spire.implicits._
import spire.math._

class ScalaFigures extends Specification {

  import BayesianNetworkModule.{BayesianNetwork, BayesianNetworkNode}
  import EliminationTreeModule.EliminationTree
  import FactorModule.Factor

  val bools = Some(Vector(true, false))

  val A = new RandomVariable0("A", bools, None)
  val B = new RandomVariable0("B", bools, None)
  val C = new RandomVariable0("C", bools, None)
  val D = new RandomVariable0("D", bools, None)
  val E = new RandomVariable0("E", bools, None)

  def figure6_1(): BayesianNetwork[Boolean] = {

    val bn = BayesianNetwork(
      "6.1",
      List(
        BayesianNetworkNode(A,
          Factor(Vector(A), Map(
            Vector(A is true) -> 0.6,
            Vector(A is false) -> 0.4
          ))),
        BayesianNetworkNode(B, // B | A
          Factor(Vector(B), Map(
            Vector(B is true, A is true) -> 0.2,
            Vector(B is true, A is false) -> 0.8,
            Vector(B is false, A is true) -> 0.75,
            Vector(B is false, A is false) -> 0.25
          ))),
        BayesianNetworkNode(C, // C | A
          Factor(Vector(C), Map(
            Vector(C is true, A is true) -> 0.8,
            Vector(C is true, A is false) -> 0.2,
            Vector(C is false, A is true) -> 0.1,
            Vector(C is false, A is false) -> 0.9
          ))),
        BayesianNetworkNode(D, // D | BC
          Factor(Vector(D), Map(
            Vector(D is true, B is true, C is true) -> 0.95,
            Vector(D is true, B is true, C is false) -> 0.05,
            Vector(D is true, B is false, C is true) -> 0.9,
            Vector(D is true, B is false, C is false) -> 0.1,
            Vector(D is false, B is true, C is true) -> 0.8,
            Vector(D is false, B is true, C is false) -> 0.2,
            Vector(D is false, B is false, C is true) -> 0.0,
            Vector(D is false, B is false, C is false) -> 1.0
          ))),
        BayesianNetworkNode(E, // E | C
          Factor(Vector(E), Map(
            Vector(E is true, C is true) -> 0.7,
            Vector(E is true, C is false) -> 0.3,
            Vector(E is false, C is true) -> 0.0,
            Vector(E is false, C is false) -> 1.0
          )))),
      (vs: Seq[Vertex[BayesianNetworkNode[Boolean]]]) => vs match {
        case a :: b :: c :: d :: e :: Nil => List((a, b, ""), (a, c, ""), (b, d, ""), (c, d, ""), (c, e, ""))
        case _ => Nil
      })

    bn
  }

  def figure6_2(): Factor[Boolean] = figure6_1.jointProbabilityTable

  def figure6_3(): (Factor[Boolean], Factor[Boolean]) = {

    //Figure 3.1
    val cptB = Factor(Vector(B, C, D), Map(
      Vector(B is true, C is true, D is true) -> 0.95,
      Vector(B is true, C is true, D is false) -> 0.05,
      Vector(B is true, C is false, D is true) -> 0.9,
      Vector(B is true, C is false, D is false) -> 0.1,
      Vector(B is false, C is true, D is true) -> 0.8,
      Vector(B is false, C is true, D is false) -> 0.2,
      Vector(B is false, C is false, D is true) -> 0.0,
      Vector(B is false, C is false, D is false) -> 1.0
    ))

    // Figure 3.2
    val cptD = Factor(Vector(D, E), Map(
      Vector(D is true, E is true) -> 0.448,
      Vector(D is true, E is false) -> 0.192,
      Vector(D is false, E is true) -> 0.112,
      Vector(D is false, E is false) -> 0.248
    ))

    val h = (cptB.sumOut(D)).sumOut(C)
    val m = cptB * cptD

    (cptB, cptD)
  }

  def figure6_4(): BayesianNetwork[Boolean] = {

    val bn = BayesianNetwork("6.4",
      Vector(
        BayesianNetworkNode(A, Factor(Vector(A), Map(
          Vector(A is true) -> 0.6,
          Vector(A is false) -> 0.4
        ))),
        BayesianNetworkNode(B, Factor(Vector(B), Map( // B | A
          Vector(B is true, A is true) -> 0.9,
          Vector(B is true, A is false) -> 0.1,
          Vector(B is false, A is true) -> 0.2,
          Vector(B is false, A is false) -> 0.8
        ))),
        BayesianNetworkNode(C, Factor(Vector(C), Map( // C | B
          Vector(C is true, B is true) -> 0.3,
          Vector(C is true, B is false) -> 0.7,
          Vector(C is false, B is true) -> 0.5,
          Vector(C is false, B is false) -> 0.5
        )))),
      (vs: Seq[Vertex[BayesianNetworkNode[Boolean]]]) => vs match {
        case a :: b :: c :: Nil => List((a, b, ""), (b, c, ""))
        case _ => Nil
      })

    val pB = (((bn.cpt(B) * bn.cpt(A)).sumOut(A)) * bn.cpt(C)).sumOut(C)

    bn
  }

  def figure6_5(): List[InteractionGraph[Boolean]] =
    figure6_1().interactionGraph().eliminationSequence(List(B, C, A, D))

  def figure6_7() = {

    val f61 = figure6_1()

    // Figure 6.1 pruned towards B & E
    val Q1: Set[RandomVariable[Boolean]] = Set(B, E)
    val f67pBE = f61.pruneNetworkVarsAndEdges(Q1, None)

    // Figure 6.2 pruned towards B
    val Q2: Set[RandomVariable[Boolean]] = Set(B)
    val f67pB = f61.pruneNetworkVarsAndEdges(Q2, None)

    (f67pBE, f67pB)
  }

  // Figure 6.1 with edges pruned towards C=false
  def figure6_8() = figure6_1().pruneEdges("Figure 6.8", Some(List(C is false)))

  // Figure 6.1 pruned towards Q={D} and A=true,C=false
  def figure6_9() =
    figure6_1().pruneNetworkVarsAndEdges(Set(D), Some(List(A is true, C is false)))

  // Result of fe-i on a->b->c with Q={C}
  def figure7_2() = figure6_4.factorElimination1(Set(C))

  def figure7_4() = {

    val f61 = figure6_1()

    val τ = EliminationTree[Boolean](
      Vector(A, B, C, D, E).map(f61.cpt(_)),
      (vs: Seq[Vertex[Factor[Boolean]]]) => vs match {
        case a :: b :: c :: d :: e :: Nil => List(
          (a, b, ""), (a, d, ""), (d, c, ""), (c, e, ""))
        case _ => Nil
      })

    // factorElimination2 on figure6.1 with Q={C} and τ={...} and r=n3
    val (f68, elim) = f61.factorElimination2(Set(C), τ, f61.cpt(C))
    (f68, τ, f61.cpt(C))
  }

  // factorElimination3 on figure6.1 with Q={C} and τ={...} and r=n3
  def figure7_5() = {
    // TODO: needs to be immutable
    val f61 = figure6_1()
    val (bn, τ, cptC) = figure7_4()
    val (f75, elim) = f61.factorElimination2(Set(C), τ, cptC)
    f75
  }

  def figure7_12() = JoinTree(
    Vector[Set[RandomVariable[Boolean]]](Set(A, B, C), Set(B, C, D), Set(C, E)),
    (vs: Seq[Vertex[Set[RandomVariable[Boolean]]]]) => vs match {
      case abc :: bcd :: ce :: Nil => List((abc, bcd, ""), (bcd, ce, ""))
      case _ => Nil
    })

  "bayesian networks" should {
    "work" in {

      1 must be equalTo 1
    }
  }

}
