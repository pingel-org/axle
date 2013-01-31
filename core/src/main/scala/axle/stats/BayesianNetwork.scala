package axle.stats

/**
 * Notes on unimplemented aspects of Bayesian networks:
 *
 * Bayes Rule
 *
 * P(A=a|B=b) = P(B=b|A=a)P(A=a)/P(B=b)
 *
 * Case analysis
 *
 * P(A=a) = P(A=a, B=b1) + ... + P(A=a, B=bN)
 *
 * Chain Rule
 *
 * P(X1=x1, ..., XN = xN) = P(X1=x1 | X2=x2, ..., XN=xN) * ... * P(XN=xN)
 *
 * Jeffrey's Rule
 *
 * ???
 *
 * markov independence: a variable is independent of its non-descendents given its parents
 *
 * d-separation cases:
 *
 * x: given variable (it appears in Z)
 * o: not given
 *
 * Sequence:
 *
 * -> o ->   open
 * -> x ->   closed
 *
 * Divergent:
 *
 * <- o ->   open
 * <- x ->   closed
 *
 * Convergent:
 *
 * -> o <-   closed (and none of the descendants of the node are in Z)
 * -> x <-   open (or if any of the descendants of the node are in Z)
 *
 *
 * A path is blocked (independent) if any valve in the path is blocked.
 *
 * Two variables are separated if all paths are blocked.
 *
 * Independence
 *
 * I(X, Z, Y) is read "X is independent of Y given Z"
 *
 * Symmetry (ch 3 stuff)
 *
 * I(X, Z, Y) <=> I(Y, Z, X)
 *
 * Decomposition
 *
 * I(X, Z, Y or W) <=> I(X, Z, Y) and I(X, Z, W)
 *
 * Weak Union
 *
 * I(X, Z, Y or W) => I(X, Z or Y, W)
 *
 * Contraction
 *
 * I(X, Z, Y) and I(X, Z or Y, W) => I(X, Z, Y or W)
 *
 * Intersection
 *
 * I(X, Z or W, Y) and I(X, Z or Y, W) => I(X, Z, Y or W)
 *
 * independence closure
 *
 * ???  used for what ???
 *
 * MPE: most probable explanation
 *
 * find {x1, ..., xN} such that P(X1=x1, ..., XN=xN | e) is maximized
 *
 * MPE is much easier to compute algorithmically
 *
 * MAP: maximum a posteriori hypothesis
 *
 * Let M be a subset of network variables X
 *
 * Let e be some evidence
 *
 * Find an instantiation m over variables M such that pr(M=m|e) is maximized
 *
 * Tree networks (ch5 p20)
 *
 * are the ones where each node has at most one parent
 * treewidth <= 1
 *
 * A polytree is:
 *
 * there is at most one (undirected) path between any two nodes
 * treewidth = maximum number of parents
 *
 */

import collection._
import axle._
import axle.XmlAble
import axle.graph._
import math.max
import Stream.{ cons, empty }

class BayesianNetwork(_name: String, _graph: DirectedGraph[BayesianNetworkNode, String])
  extends Model(_graph) {

  import _graph._

  override def name() = _name  
  def graph() = _graph
  
  override def vertexPayloadToRandomVariable(mvp: BayesianNetworkNode): RandomVariable[_] = mvp.rv

  // def duplicate(): BayesianNetwork = new BayesianNetwork(name) // TODO graphFrom(g)(v => v, e => e)

  def jointProbabilityTable(): Factor = {
    val newVars = randomVariables()
    new Factor(newVars,
      Factor.spaceFor(newVars)
        .map(kase => (kase, probabilityOf(kase)))
        .toMap
    )
  }

  def cpt(variable: RandomVariable[_]): Factor = findVertex(_.payload.rv == variable).map(_.payload.cpt).get

  def probabilityOf(cs: Seq[CaseIs[_]]) = cs.map(c => cpt(c.rv)(cs)).reduce(_ * _)

  def markovAssumptionsFor(rv: RandomVariable[_]): Independence = {
    val rvVertex = findVertex(_.payload.rv == rv).get
    val X: immutable.Set[RandomVariable[_]] = immutable.Set(rv)
    val Z: immutable.Set[RandomVariable[_]] = predecessors(rvVertex).map(_.payload.rv).toSet
    val D = descendants(rvVertex) ++ predecessors(rvVertex) + rvVertex
    val Dvars = D.map(_.payload.rv)
    new Independence(X, Z, randomVariables.filter(!Dvars.contains(_)).toSet)
  }

  def computeFullCase(c: List[CaseIs[_]]): Double = {

    // not an airtight check
    assert(numVariables == c.size)

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

  def variableEliminationPriorMarginalI(Q: Set[RandomVariable[_]], π: List[RandomVariable[_]]): Factor =
    π.foldLeft(randomVariables().map(cpt(_)).toSet)((S, rv) => {
      val allMentions = S.filter(_.mentions(rv))
      (S -- allMentions) + allMentions.reduce(_ * _).sumOut(rv)
    }).reduce(_ * _)

  /**
   *
   * Chapter 6 Algorithm 5 (page 17)
   *
   * assert: Q subset of variables
   * assert: π ordering of variables in S but not in Q
   * assert: e assigns values to variables in this network
   *
   */

  def variableEliminationPriorMarginalII[A](Q: Set[RandomVariable[_]], π: List[RandomVariable[_]], e: CaseIs[A]): Factor =
    π.foldLeft(randomVariables().map(cpt(_).projectRowsConsistentWith(Some(List(e)))).toSet)(
      (S, rv) => {
        val allMentions = S.filter(_.mentions(rv))
        (S -- allMentions) + allMentions.reduce(_ * _).sumOut(rv)
      }).reduce(_ * _)

  def interactsWith(v1: RandomVariable[_], v2: RandomVariable[_]): Boolean =
    vertices().map(_.payload.cpt).exists(f => f.mentions(v1) && f.mentions(v2))

  /**
   * interactionGraph
   *
   * Also called the "moral graph"
   */

  def interactionGraph(): InteractionGraph =
    InteractionGraph(randomVariables,
      (vs: Seq[Vertex[RandomVariable[_]]]) =>
        (for {
          vi <- vs // TODO "doubles"
          vj <- vs
          if interactsWith(vi.payload, vj.payload)
        } yield {
          (vi, vj, "")
        }))

  /**
   * orderWidth
   *
   * Chapter 6 Algorithm 2 (page 13)
   */

  def orderWidth(order: List[RandomVariable[_]]): Int =
    randomVariables().scanLeft((interactionGraph(), 0))(
      (gi, rv) => {
        val ig = gi._1
        (ig.eliminate(rv), ig.graph.neighbors(ig.graph.findVertex(_.payload == rv).get).size)
      }
    ).map(_._2).max

  //  def makeFactorFor(rv: RandomVariable[_]): Factor =
  //    Factor(randomVariables.filter(getPredecessors(findVertex(_.rv == rv).get).map(_.getPayload.rv).contains(_)) ++ List(rv))

  /**
   * pruneEdges
   *
   * 6.8.2
   */

  def pruneEdges(resultName: String, eOpt: Option[List[CaseIs[_]]]): BayesianNetwork = {
    val result = BayesianNetwork(resultName, ???)
    eOpt.map(e => {
      for (U <- e.map(_.rv)) {
        val uVertex = result.graph.findVertex(_.payload.rv == U).get
        for (edge <- result.graph.outputEdgesOf(uVertex)) { // ModelEdge
          // TODO !!!
          //          val X = edge.dest().payload.rv
          //          val oldF = result.cpt(X)
          //          result.deleteEdge(edge) // TODO: not functional
          //          val smallerF: Factor = makeFactorFor(X)
          //          for (c <- smallerF.cases) {
          //            // set its value to what e sets it to
          //            // TODO c(U) = e.valueOf(U)
          //            // TODO smallerF(c) = oldF(c)
          //          }
          // TODO result.setCPT(edge.getDest().getPayload, smallerF) // TODO should be setting on the return value
        }
      }
      result
    }).getOrElse(result)
  }

  def pruneNodes(Q: Set[RandomVariable[_]], eOpt: Option[List[CaseIs[_]]], g: BayesianNetwork): BayesianNetwork = {

    val vars = eOpt.map(Q ++ _.map(_.rv)).getOrElse(Q)

    def nodePruneStream(g: BayesianNetwork): Stream[BayesianNetwork] = {
      val xVertices = g.graph.leaves().toSet -- vars.map(rv => g.graph.findVertex(_.payload.rv == rv).get)
      xVertices.size match {
        case 0 => empty
        case _ => {
          val result = xVertices.foldLeft(g)(
            (bn, xV) => new BayesianNetwork(bn.name + " - " + xV, bn.graph.deleteVertex(xV))
          )
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

  def pruneNetworkVarsAndEdges(Q: Set[RandomVariable[_]], eOpt: Option[List[CaseIs[_]]]): BayesianNetwork =
    BayesianNetwork(this.name, ???) // TODO pruneNodes(Q, eOpt, pruneEdges("pruned", eOpt).getGraph)
  //
  //  def variableEliminationPR(Q: Set[RandomVariable[_]], eOpt: Option[List[CaseIs[_]]]): (Factor, BayesianNetwork) = {
  //
  //    val pruned = pruneNetworkVarsAndEdges(Q, eOpt)
  //    val R = randomVariables.filter(!Q.contains(_)).toSet
  //    val π = pruned.minDegreeOrder(R)
  //
  //    val S = π.foldLeft(pruned.randomVariables().map(rv => pruned.cpt(rv).projectRowsConsistentWith(eOpt)).toSet)(
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
  //    val Q = pruned.randomVariables()
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
  //    assert(S.size == 1)
  //
  //    val sl = S.toList
  //    val result = sl(0)
  //
  //    // assert(result.numCases() == 1)
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

  def variableEliminationMAP(Q: Set[RandomVariable[_]], e: List[RandomVariable[_]]): List[CaseIs[_]] = {
    // TODO
    Nil
  }

  //  def minDegreeOrder(pX: Set[RandomVariable[_]]): List[RandomVariable[_]] = {
  //    val X = Set[RandomVariable[_]]() ++ pX
  //    val ig = interactionGraph()
  //    val result = mutable.ListBuffer[RandomVariable[_]]()
  //    while (X.size > 0) {
  //      val xVertices = X.map(ig.findVertex(_).get)
  //      val rv = ig.vertexWithFewestNeighborsAmong(xVertices).payload
  //      result += rv
  //      ig.eliminate(rv)
  //      X -= rv
  //    }
  //    result.toList
  //  }
  //
  //  def minFillOrder(pX: Set[RandomVariable[_]]): List[RandomVariable[_]] = {
  //
  //    val X = Set[RandomVariable[_]]() ++ pX
  //    val ig = interactionGraph()
  //    val result = mutable.ListBuffer[RandomVariable[_]]()
  //
  //    while (X.size > 0) {
  //      val xVertices = X.map(ig.findVertex(_).get)
  //      val rv = ig.vertexWithFewestEdgesToEliminateAmong(xVertices, (v1, v2) => { "x" }).payload
  //      result += rv
  //      ig.eliminate(rv)
  //      X -= rv
  //    }
  //    result.toList
  //  }

  def factorElimination1(Q: Set[RandomVariable[_]]): Factor = {

    val S = mutable.ListBuffer[Factor]() ++ randomVariables().map(cpt(_)).toList

    while (S.size > 1) {

      val fi = S.remove(0)

      val V = fi.variables
        .filter(!Q.contains(_))
        .filter(v => !S.exists(_.mentions(v)))
        .toSet

      // At this point, V is the set of vars that are unique to this particular
      // factor, fj, and do not appear in Q

      S += S.remove(0) * fi.sumOut(V)
    }

    // there should be one element left in S
    S(0).projectToOnly(Q.toList)
  }

  // TODO: Make immutable: this should not be calling delete or setPayload
  // the variables Q appear on the CPT for the product of Factors assigned to node r
  def factorElimination2(Q: Set[RandomVariable[_]], τ: EliminationTree, f: Factor): (BayesianNetwork, Factor) = {
    while (τ.graph.vertices().size > 1) {
      // remove node i (other than r) that has single neighbor j in τ
      val fl = τ.graph.firstLeafOtherThan(τ.graph.findVertex(_.payload == f).get)
      fl.map(i => {
        val j = τ.graph.neighbors(i).iterator.next()
        val ɸ_i = i.payload
        τ.graph.delete(i)
        // TODO j.setPayload(ɸ_i.sumOut(ɸ_i.getVariables().toSet -- τ.getAllVariables().toSet))
      })
    }
    (???, f.projectToOnly(Q.toList))
  }

  //  def factorElimination3(Q: Set[RandomVariable[_]], τ: EliminationTree, f: Factor): Factor = {
  //    // Q is a subset of C_r
  //    while (τ.vertices().size > 1) {
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
  def factorElimination(τ: EliminationTree, e: List[CaseIs[_]]): Map[Factor, Factor] =
    {
      for (i <- τ.graph.vertices()) {
        for (ci <- e) {
          // val lambdaE = new Factor(ci.rv, Map())
          // assign lambdaE.E to e.get(E)
        }
      }
      // TODO val root = chooseRoot(τ)
      // TODO pullMessagesTowardsRoot()
      // TODO pushMessagesFromRoot()
      for (i <- τ.graph.vertices()) {

      }
      ???
    }

}

case class BayesianNetworkNode(rv: RandomVariable[_], cpt: Factor) extends XmlAble {

  override def toString(): String = rv.name + "\n\n" + cpt

  def toXml(): xml.Node =
    <html>
      <div>
        <center><h2>{ rv.name }</h2></center>
        { cpt.toHtml() }
      </div>
    </html>
}

object BayesianNetwork {

  def apply(name: String, graph: DirectedGraph[BayesianNetworkNode, String]) =
    new BayesianNetwork(name, graph)

  def apply(
    name: String,
    vps: Seq[BayesianNetworkNode],
    ef: Seq[Vertex[BayesianNetworkNode]] => Seq[(Vertex[BayesianNetworkNode], Vertex[BayesianNetworkNode], String)]): BayesianNetwork =
    new BayesianNetwork(name, JungDirectedGraph(vps, ef))

}
