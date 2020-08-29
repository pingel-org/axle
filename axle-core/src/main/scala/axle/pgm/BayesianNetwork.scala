package axle.pgm

import scala.Stream.cons
import scala.Stream.empty

import cats.kernel.Eq
import cats.kernel.Order
import cats.implicits._

import spire.algebra.Field
import spire.math.ConvertableFrom

import axle.algebra.RegionEq
import axle.algebra.UndirectedGraph
import axle.algebra.DirectedGraph
import axle.stats.Variable
import axle.stats.Independence
import axle.stats.Factor
import axle.math.Π
import axle.syntax.directedgraph._
import axle.syntax.undirectedgraph._

class Edge

object Edge {

  import cats.Show

  implicit def showPgmEdge: Show[axle.pgm.Edge] = _ => ""

}

object BayesianNetwork {

  def withGraphK2[T: Eq, N: Field: ConvertableFrom: Order, DG[_, _]](
    variableFactorMap: Map[Variable[T], Factor[T, N]])(
    implicit
    dg: DirectedGraph[DG[BayesianNetworkNode[T, N], Edge], BayesianNetworkNode[T, N], Edge]) =
    BayesianNetwork[T, N, DG[BayesianNetworkNode[T, N], Edge]](variableFactorMap)

}

case class BayesianNetwork[T, V, DG](
  variableFactorMap: Map[Variable[T], Factor[T, V]])(
  implicit
  val eqT: Eq[T],
  val dg: DirectedGraph[DG, BayesianNetworkNode[T, V], Edge],
  val fieldV: Field[V],
  val convertableFromV: ConvertableFrom[V],
  val orderV: Order[V]) {

  val bnns = variableFactorMap.map({ case (d, f) => BayesianNetworkNode(d, f) }).toList

  val bnnByVariable = bnns.map(bnn => bnn.variable -> bnn).toMap

  val graph =
    dg.make(
      bnns,
      bnns.flatMap(dest =>
        dest.cpt.variables.filterNot(_ === dest.variable)
          .map(source => (bnnByVariable(source), dest, new Edge))))

  def numVariables = variableFactorMap.size

  def randomVariables: Vector[Variable[T]] =
    dg.vertices(graph).map(_.variable).toVector

  def jointProbabilityTable: Factor[T, V] = {
    val newVars = randomVariables
    val newVarsWithValues = newVars.map({ variable => (variable, variableFactorMap(variable).valuesOfVariable(variable)) })
    val kases = Factor.cases( newVarsWithValues )
    val kase2prob = kases.map({regions => (regions, probabilityOf(newVars.zip(regions)))}).toMap
    Factor(newVarsWithValues, kase2prob)
  }

  def factorFor(variable: Variable[T]): Factor[T, V] =
    graph.findVertex(_.variable === variable).map(_.cpt).get

  def probabilityOf(cs: Seq[(Variable[T], RegionEq[T])]): V =
    Π[V, Vector](cs.map({ case (variable, _) =>
      val factor = factorFor(variable)
      val row = factor.variables.map(v =>
        cs.find(_._1 === v).get // TODO this is tortured
      ).map(_._2)
      factor(row)
    }).toVector)

  def markovAssumptionsFor(rv: Variable[T]): Independence[T] = {
    val rvVertex = graph.findVertex(_.variable === rv).get
    val X: Set[Variable[T]] = Set(rv)
    val Z: Set[Variable[T]] = graph.predecessors(rvVertex).map(_.variable).toSet
    val D = graph.descendants(rvVertex) ++ graph.predecessors(rvVertex) + rvVertex
    val Dvars = D.map(_.variable)
    Independence(X, Z, randomVariables.filterNot(Dvars.contains).toSet)
  }

  def computeFullCase(c: List[RegionEq[T]]): Double = {

    // not an airtight check
    assert(numVariables === c.size)

    // order variables such that all nodes appear before their ancestors
    // rewrite using chain rule
    // drop on conditionals in expressions using Markov independence assumptions
    // now each term should simply be a lookup in the curresponding CPT
    // multiply results

    -1.0 // TODO
  }

  /**
   * Algorithm 1 from Chapter 6 (page 9)
   *
   * @param Q is a set of variables
   * @param π is an ordered list of the variables not in Q
   * @return the prior marginal pr(Q)
   *
   * The cost is the cost of the Tk multiplication. This is highly dependent on π
   */

  def variableEliminationPriorMarginalI(
    Q: Set[Variable[T]],
    π: List[Variable[T]]): Factor[T, V] =
    Π[Factor[T, V], Set](π.foldLeft(randomVariables.map(factorFor).toSet)((S, rv) => {
      val allMentions: Set[Factor[T, V]] = S.filter(_.variables.contains(rv))
      val mentionsWithout = Π[Factor[T, V], Set](allMentions).sumOut(rv)
      (S -- allMentions) + mentionsWithout
    }))

  /**
   *
   * Chapter 6 Algorithm 5 (page 17)
   *
   * assert: Q subset of variables
   * assert: π ordering of variables in S but not in Q
   * assert: e assigns values to variables in this network
   *
   */

  def variableEliminationPriorMarginalII(
    Q: Set[Variable[T]],
    π: List[Variable[T]],
    e: (Variable[T], RegionEq[T])): Factor[T, V] =
    Π[Factor[T, V], Set](
      π.foldLeft(
        randomVariables.map { rv =>
          factorFor(rv).projectRowsConsistentWith(Some(List(e)))
        }.toSet
      ) { (S, rv) => {
            val allMentions = S.filter(_.variables.contains(rv))
            (S -- allMentions) + Π[Factor[T, V], Set](allMentions).sumOut(rv)
        }
      }
    )

  def interactsWith(v1: Variable[T], v2: Variable[T]): Boolean =
    graph.vertices.map(_.cpt).exists(f => f.variables.contains(v1) && f.variables.contains(v2))

  /**
   * interactionGraph
   *
   * Also called the "moral graph"
   */

  def interactionGraph[UG](implicit ug: UndirectedGraph[UG, Variable[T], InteractionGraphEdge]): InteractionGraph[T, UG] =
    InteractionGraph(
      randomVariables,
      (for {
        vi <- randomVariables // TODO "doubles"
        vj <- randomVariables
        if interactsWith(vi, vj)
      } yield (vi, vj, new InteractionGraphEdge)))

  /**
   * orderWidth
   *
   * Chapter 6 Algorithm 2 (page 13)
   */

  def orderWidth[UG](order: List[Variable[T]])(
    implicit
    ug: UndirectedGraph[UG, Variable[T], InteractionGraphEdge]): Int =
    randomVariables.scanLeft((interactionGraph, 0))(
      (gi, rv) => {
        val ig = gi._1
        (ig.eliminate(rv), ig.graph.neighbors(ig.graph.findVertex(_ === rv).get).size)
      }).map(_._2).max

  //  def makeFactorFor(rv: Distribution[_]): Factor =
  //    Factor(randomVariables.filter(getPredecessors(findVertex(_.rv === rv).get).map(_.getPayload.rv).contains) ++ List(rv))

  /**
   * pruneEdges
   *
   * 6.8.2
   */

  def pruneEdges(resultName: String, eOpt: Option[List[RegionEq[T]]]): BayesianNetwork[T, V, DG] = {
    //    val result = BayesianNetwork[T, N, DG](resultName, ???)
    //    eOpt.map(e => {
    //      e.map(_.distribution) foreach { U =>
    //        val uVertex = result.graph.findVertex(_.rv === U).get
    //        result.graph.outputEdgesOf(uVertex) foreach { edge => // ModelEdge
    //          // TODO !!!
    //          //          val X = edge.dest.payload.rv
    //          //          val oldF = result.cpt(X)
    //          //          result.deleteEdge(edge) // TODO: not functional
    //          //          val smallerF: Factor = makeFactorFor(X)
    //          //          smallerF.cases foreach { c =>
    //          //            // set its value to what e sets it to
    //          //            // TODO c(U) = e.valueOf(U)
    //          //            // TODO smallerF(c) = oldF(c)
    //          //          }
    //          // TODO result.setCPT(edge.getDest.getPayload, smallerF) // TODO should be setting on the return value
    //        }
    //      }
    //      result
    //    }).getOrElse(result)
    ???
  }

  def pruneNodes(Q: Set[Variable[T]], eOpt: Option[List[Variable[T]]], g: BayesianNetwork[T, V, DG]): BayesianNetwork[T, V, DG] = {

    val vars = eOpt.map(Q ++ _).getOrElse(Q)

    def nodePruneStream(g: BayesianNetwork[T, V, DG]): Stream[BayesianNetwork[T, V, DG]] = {
      val xVertices = g.graph.leaves.toSet -- vars.map(rv => g.graph.findVertex(_.variable === rv).get)
      xVertices.size match {
        case 0 => empty
        case _ => {
          val result = xVertices.foldLeft(g)(
            (bn, xV) => BayesianNetwork(bn.variableFactorMap /* TODO filterVertices(v => ! v === xV) */ ))
          cons(result, nodePruneStream(result))
        }
      }
    }
    nodePruneStream(g).last
  }

  /**
   * pruneNetworkVarsAndEdges
   *
   * 6.8.3
   */

  def pruneNetworkVarsAndEdges(
    Q:    Set[Variable[T]],
    eOpt: Option[List[RegionEq[T]]]): BayesianNetwork[T, V, DG] = {
    // TODO pruneNodes(Q, eOpt, pruneEdges("pruned", eOpt).getGraph)
    // BayesianNetwork(this.name, ???)
    ???
  }
  //
  //  def variableEliminationPR(Q: Set[Distribution[_]], eOpt: Option[List[CaseIs[_]]]): (Factor, BayesianNetwork) = {
  //
  //    val pruned = pruneNetworkVarsAndEdges(Q, eOpt)
  //    val R = randomVariables.filter(!Q.contains(_)).toSet
  //    val π = pruned.minDegreeOrder(R)
  //
  //    val S = π.foldLeft(pruned.randomVariables.map(rv => pruned.cpt(rv).projectRowsConsistentWith(eOpt)).toSet)(
  //      (S, rv) => {
  //        val allMentions = S.filter(_.mentions(rv))
  //        (S -- allMentions) + allMentions.reduce(_ * _).sumOut(rv)
  //      })
  //
  //    (S.reduce(_ * _), pruned)
  //  }
  //
  //  def variableEliminationMPE(e: List[CaseIs[_]]): (Double, BayesianNetwork) = {
  //
  //    val pruned = pruneEdges("pruned", Some(e))
  //    val Q = pruned.randomVariables
  //    val π = pruned.minDegreeOrder(Q.toSet)
  //
  //    val S = π.foldLeft(Q.map(rv => pruned.cpt(rv).projectRowsConsistentWith(Some(e))).toSet)(
  //      (S, rv) => {
  //        val allMentions = S.filter(_.mentions(rv))
  //        (S -- allMentions) + allMentions.reduce(_ * _).maxOut(rv)
  //      })
  //
  //    // at this point (since we're iterating over *all* variables in Q)
  //    // S will contain exactly one trivial Factor
  //
  //    assert(S.size === 1)
  //
  //    val sl = S.toList
  //    val result = sl(0)
  //
  //    // assert(result.numCases === 1)
  //
  //    (result(List()), pruned)
  //  }

  /**
   * variableEliminationMAP
   *
   * returns an instantiation q which maximizes Pr(q,e) and that probability
   *
   * see ch 6 page 31: Algorithm 8
   */

  def variableEliminationMAP(Q: Set[Variable[T]], e: List[Variable[T]]): List[RegionEq[T]] = {
    // TODO
    Nil
  }

  //  def minDegreeOrder(pX: Set[Distribution[_]]): List[Distribution[_]] = {
  //    val X = Set[Distribution[_]]() ++ pX
  //    val ig = interactionGraph
  //    while (X.size > 0) {
  //      val xVertices = X.map(ig.findVertex(_).get)
  //      val rv = ig.vertexWithFewestNeighborsAmong(xVertices).payload
  //      result += rv
  //      ig.eliminate(rv)
  //      X -= rv
  //    }
  //  }
  //
  //  def minFillOrder(pX: Set[Distribution[_]]): List[Distribution[_]] = {
  //
  //    val X = Set[Distribution[_]]() ++ pX
  //    val ig = interactionGraph
  //
  //    while (X.size > 0) {
  //      val xVertices = X.map(ig.findVertex(_).get)
  //      val rv = ig.vertexWithFewestEdgesToEliminateAmong(xVertices, (v1, v2) => { "x" }).payload
  //      result += rv
  //      ig.eliminate(rv)
  //      X -= rv
  //    }
  //  }

  def _factorElimination1(Q: Set[Variable[T]], S: List[Factor[T, V]]): Factor[T, V] = S match {

    case Nil       => throw new Exception("S is empty")

    case fi :: Nil => fi.projectToOnly(Q.toVector)

    case fi :: fj :: rest => {
      implicit val mmFactorTN = Factor.factorMultMonoid[T, V]
      val fiSummedOut: Factor[T, V] =
        fi.sumOut(fi.variables.filter(v => !Q.contains(v) && !S.exists(_.variables.contains(v))).toSet)
      _factorElimination1(Q, rest ++ List(mmFactorTN.times(fj, fiSummedOut)))
    }

  }

  def factorElimination1(Q: Set[Variable[T]]): Factor[T, V] =
    _factorElimination1(Q, randomVariables.map(factorFor).toList)

  // TODO: Make immutable: this should not be calling delete or setPayload
  // the variables Q appear on the CPT for the product of Factors assigned to node r
  def factorElimination2[UG](
    Q: Set[Variable[T]],
    τ: EliminationTree[T, V, UG],
    f: Factor[T, V]) // (implicit ug: UndirectedGraph[UG, Factor[T, V], EliminationTreeEdge])
    : (BayesianNetwork[T, V, DG], Factor[T, V]) = {
    //    while (τ.graph.vertices.size > 1) {
    //      // remove node i (other than r) that has single neighbor j in τ
    //      val fl = τ.graph.firstLeafOtherThan(τ.graph.findVertex(_ === f).get)
    //      fl.map(i => {
    //        val j = τ.graph.neighbors(i).iterator.next()
    //        val ɸ_i = i
    //        //τ.graph.delete(i)
    //        // TODO j.setPayload(ɸ_i.sumOut(ɸ_i.getVariables.toSet -- τ.getAllVariables.toSet))
    //      })
    //    }
    //    (???, f.projectToOnly(Q.toVector))
    ???
  }

  //  def factorElimination3(Q: Set[Distribution[_]], τ: EliminationTree, f: Factor): Factor = {
  //    // Q is a subset of C_r
  //    while (τ.vertices.size > 1) {
  //      // remove node i (other than r) that has single neighbor j in tau
  //      val fl = τ.firstLeafOtherThan(τ.findVertex(f).get)
  //      fl.map(i => {
  //        val j = τ.neighbors(i).iterator.next()
  //        val ɸ_i = i.payload
  //        τ.delete(i)
  //        val Sij = τ.separate(i, j)
  //        // TODO j.setPayload(ɸ_i.projectToOnly(Sij.toList))
  //      })
  //    }
  //    f.projectToOnly(Q.toList)
  //  }

  // Note: not sure about this return type:
  def factorElimination[UG](
    τ: EliminationTree[T, V, UG],
    e: List[RegionEq[T]])(implicit ug: UndirectedGraph[UG, Factor[T, V], EliminationTreeEdge]): Map[Factor[T, V], Factor[T, V]] =
    {
      τ.graph.vertices foreach { i =>
        e foreach { ci =>
          // val lambdaE = Factor(ci.rv, Map())
          // assign lambdaE.E to e.get(E)
        }
      }
      // TODO val root = chooseRoot(τ)
      // TODO pullMessagesTowardsRoot()
      // TODO pushMessagesFromRoot()
      τ.graph.vertices foreach { v =>

      }
      ???
    }

}
