package axle.stats

/**
 * Notes on unimplemented aspects of Bayesian networks:
 *
 * Bayes Rule
 *
 * pr(A=a|B=b) = pr(B=b|A=a)pr(A=a)/pr(B=b)
 *
 * Case analysis
 *
 * pr(A=a) = pr(A=a, B=b1) + ... + pr(A=a, B=bN)
 *
 * Chain Rule
 *
 * pr(X1=x1, ..., XN = xN) = pr(X1=x1 | X2=x2, ..., XN=xN) * ... * pr(XN=xN)
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
 * find {x1, ..., xN} such that pr(X1=x1, ..., XN=xN | e) is maximized
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
import math.max
import axle.graph.JungDirectedGraphFactory._
import scalaz._
import Scalaz._

case class BayesianNetworkNode(rv: RandomVariable[_], cpt: Factor) {

  override def toString(): String = rv.getName + "\n\n" + cpt

}

object BayesianNetwork {

  def apply(name: String): BayesianNetwork = new BayesianNetwork(name)
}

class BayesianNetwork(name: String)
  extends Model[BayesianNetworkNode] {

  def getName(): String = name

  def getGraph(): JungDirectedGraph[BayesianNetworkNode, String] = this

  def vertexPayloadToRandomVariable(mvp: BayesianNetworkNode): RandomVariable[_] = mvp.rv

  override def vertexToVisualizationHtml(vp: BayesianNetworkNode): xml.Node =
    <html>
      <div>
        <center><h2>{ vp.rv.getName }</h2></center>
        { vp.cpt.toHtml() }
      </div>
    </html>

  def duplicate(): BayesianNetwork = new BayesianNetwork(name) // TODO graphFrom(g)(v => v, e => e)

  def getJointProbabilityTable(): Factor = {
    val newVars = getRandomVariables()
    new Factor(newVars,
      Factor.spaceFor(newVars)
        .map(kase => (kase, probabilityOf(kase)))
        .toMap
    )
  }

  def getCPT(variable: RandomVariable[_]): Factor = findVertex(_.rv == variable).map(_.getPayload.cpt).get

  def getAllCPTs(): Seq[Factor] = getVertices().map(_.getPayload.cpt).toSeq

  def probabilityOf(cs: Seq[CaseIs[_]]) = cs.map(c => getCPT(c.rv)(cs)).reduce(_ * _)

  def getMarkovAssumptionsFor(rv: RandomVariable[_]): Independence = {

    val rvVertex = findVertex(_.rv == rv).get

    val X: immutable.Set[RandomVariable[_]] = immutable.Set(rv)

    val Z: immutable.Set[RandomVariable[_]] = getPredecessors(rvVertex).map(_.getPayload.rv).toSet

    val D = mutable.Set[this.V]()
    collectDescendants(rvVertex, D)
    D += rvVertex // probably already includes this
    D ++= getPredecessors(rvVertex)
    val Dvars = D.map(_.getPayload.rv)
    val Y = getRandomVariables.filter(!Dvars.contains(_)).toSet

    new Independence(X, Z, Y)
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
    π.foldLeft(getRandomVariables().map(getCPT(_)).toSet)((S, rv) => {
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
    π.foldLeft(getRandomVariables().map(getCPT(_).projectRowsConsistentWith(Some(List(e)))).toSet)(
      (S, rv) => {
        val allMentions = S.filter(_.mentions(rv))
        (S -- allMentions) + allMentions.reduce(_ * _).sumOut(rv)
      }).reduce(_ * _)

  def interactsWith(v1: RandomVariable[_], v2: RandomVariable[_]): Boolean =
    getAllCPTs().exists(f => f.mentions(v1) && f.mentions(v2))

  // Also called the "moral graph"
  def interactionGraph(): InteractionGraph = {

    import axle.graph.JungUndirectedGraphFactory._

    val ig = new InteractionGraph()

    getRandomVariables.map(ig += _)

    getRandomVariables().doubles()
      .filter({ case (vi, vj) => interactsWith(vi, vj) })
      .map({ case (vi, vj) => ig.edge(ig.findVertex(vi).get, ig.findVertex(vj).get, "") })

    ig
  }

  // Chapter 6 Algorithm 2 (page 13)
  def orderWidth(order: List[RandomVariable[_]]): Int =
    getRandomVariables().scanLeft((interactionGraph(), 0))(
      (gi, rv) => {
        val ig = gi._1
        (ig.eliminate(rv), ig.getNeighbors(ig.findVertex(rv).get).size)
      }
    ).map(_._2).max

  //  def makeFactorFor(rv: RandomVariable[_]): Factor =
  //    Factor(getRandomVariables.filter(getPredecessors(findVertex(_.rv == rv).get).map(_.getPayload.rv).contains(_)) ++ List(rv))

  // 6.8.2
  def pruneEdges(resultName: String, eOpt: Option[List[CaseIs[_]]]): BayesianNetwork = {
    val result = new BayesianNetwork(resultName) // g = outG
    eOpt.map(e => {
      for (U <- e.map(_.rv)) {
        val uVertex = result.findVertex(_.rv == U).get
        for (edge <- result.outputEdgesOf(uVertex)) { // ModelEdge
          val X = edge.getDest().getPayload.rv
          val oldF = result.getCPT(X)
          result.deleteEdge(edge) // TODO: not functional
          val smallerF: Factor = null // TODO makeFactorFor(X)
          for (c <- smallerF.cases) {
            // set its value to what e sets it to
            assert(false)
            // c(U) = e.valueOf(U)
            // smallerF(c) = oldF(c)
          }
          // TODO result.setCPT(edge.getDest().getPayload, smallerF) // TODO should be setting on the return value
        }
      }
      result
    }).getOrElse(result)
  }

  def pruneNodes(Q: Set[RandomVariable[_]], eOpt: Option[List[CaseIs[_]]], g: JungDirectedGraph[BayesianNetworkNode, String]): JungDirectedGraph[BayesianNetworkNode, String] = {

    val vars = eOpt.map(Q ++ _.map(_.rv)).getOrElse(Q)

    def nodePruneStream(g: JungDirectedGraph[BayesianNetworkNode, String]): Stream[JungDirectedGraph[BayesianNetworkNode, String]] = {
      val xVertices = g.getLeaves().toSet -- vars.map(rv => g.findVertex(_.rv == rv).get)
      xVertices.size match {
        case 0 => Stream.empty
        case _ => {
          xVertices.map(xV => g.deleteVertex(xV))
          Stream.cons(g, nodePruneStream(g))
        }
      }
    }
    nodePruneStream(g).last
    g
  }

  // 6.8.3
  def pruneNetworkVarsAndEdges(Q: Set[RandomVariable[_]], eOpt: Option[List[CaseIs[_]]]): BayesianNetwork =
    new BayesianNetwork(this.name) // TODO pruneNodes(Q, eOpt, pruneEdges("pruned", eOpt).getGraph)

  def variableEliminationPR(Q: Set[RandomVariable[_]], eOpt: Option[List[CaseIs[_]]]): (Factor, BayesianNetwork) = {

    val pruned = pruneNetworkVarsAndEdges(Q, eOpt)
    val R = getRandomVariables.filter(!Q.contains(_)).toSet
    val π = pruned.minDegreeOrder(R)

    val S = π.foldLeft(pruned.getRandomVariables().map(rv => pruned.getCPT(rv).projectRowsConsistentWith(eOpt)).toSet)(
      (S, rv) => {
        val allMentions = S.filter(_.mentions(rv))
        (S -- allMentions) + allMentions.reduce(_ * _).sumOut(rv)
      })

    (S.reduce(_ * _), pruned)
  }

  def variableEliminationMPE(e: List[CaseIs[_]]): (Double, BayesianNetwork) = {

    val pruned = pruneEdges("pruned", Some(e))
    val Q = pruned.getRandomVariables()
    val π = pruned.minDegreeOrder(Q.toSet)

    val S = π.foldLeft(Q.map(rv => pruned.getCPT(rv).projectRowsConsistentWith(Some(e))).toSet)(
      (S, rv) => {
        val allMentions = S.filter(_.mentions(rv))
        (S -- allMentions) + allMentions.reduce(_ * _).maxOut(rv)
      })

    // at this point (since we're iterating over *all* variables in Q)
    // S will contain exactly one trivial Factor

    assert(S.size == 1)

    val sl = S.toList
    val result = sl(0)

    // assert(result.numCases() == 1)

    (result(List()), pruned)
  }

  //	public Case variableEliminationMAP(Set Q, Case e)
  //	{
  //		 see ch 6 page 31: Algorithm 8
  //		 TODO
  //      returns an instantiation q which maximizes Pr(q,e) and that probability	
  //
  //	}

  def minDegreeOrder(pX: Set[RandomVariable[_]]): List[RandomVariable[_]] = {
    val X = mutable.Set[RandomVariable[_]]() ++ pX
    val ig = interactionGraph()
    val result = mutable.ListBuffer[RandomVariable[_]]()
    while (X.size > 0) {
      val xVertices = X.map(ig.findVertex(_).get)
      val rv = ig.vertexWithFewestNeighborsAmong(xVertices).getPayload
      result += rv
      ig.eliminate(rv)
      X -= rv
    }
    result.toList
  }

  def minFillOrder(pX: Set[RandomVariable[_]]): List[RandomVariable[_]] = {

    val X = mutable.Set[RandomVariable[_]]() ++ pX
    val ig = interactionGraph()
    val result = mutable.ListBuffer[RandomVariable[_]]()

    while (X.size > 0) {
      val xVertices = X.map(ig.findVertex(_).get)
      val rv = ig.vertexWithFewestEdgesToEliminateAmong(xVertices, (v1, v2) => { "x" }).getPayload
      result += rv
      ig.eliminate(rv)
      X -= rv
    }
    result.toList
  }

  def factorElimination1(Q: Set[RandomVariable[_]]): Factor = {

    val S = mutable.ListBuffer[Factor]() ++ getRandomVariables().map(getCPT(_)).toList

    while (S.size > 1) {

      val fi = S.remove(0)

      val V = fi.getVariables
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
    while (τ.getVertices().size > 1) {
      // remove node i (other than r) that has single neighbor j in τ
      val fl = τ.firstLeafOtherThan(τ.findVertex(f).get)
      fl.map(i => {
        val j = τ.getNeighbors(i).iterator.next()
        val ɸ_i = i.getPayload
        τ.delete(i)
        j.setPayload(ɸ_i.sumOut(ɸ_i.getVariables().toSet -- τ.getAllVariables().toSet))
      })
    }
    val result = null.asInstanceOf[BayesianNetwork] // TODO !!!
    (result, f.projectToOnly(Q.toList))
  }

  // TODO: Make immutable: this should not be calling delete or setPayload
  def factorElimination3(Q: Set[RandomVariable[_]], τ: EliminationTree, f: Factor): Factor = {
    // Q is a subset of C_r
    while (τ.getVertices().size > 1) {
      // remove node i (other than r) that has single neighbor j in tau
      val fl = τ.firstLeafOtherThan(τ.findVertex(f).get)
      fl.map(i => {
        val j = τ.getNeighbors(i).iterator.next()
        val ɸ_i = i.getPayload
        τ.delete(i)
        val Sij = τ.separate(i, j)
        j.setPayload(ɸ_i.projectToOnly(Sij.toList))
      })
    }
    f.projectToOnly(Q.toList)
  }

  // Note: not sure about this return type:
  def factorElimination(τ: EliminationTree, e: List[CaseIs[_]]): Map[Factor, Factor] =
    {
      for (i <- τ.getVertices()) {
        for (ci <- e) {
          // val lambdaE = new Factor(ci.rv, Map())
          // assign lambdaE.E to e.get(E)
        }
      }
      // TODO val root = chooseRoot(τ)
      // TODO pullMessagesTowardsRoot()
      // TODO pushMessagesFromRoot()
      for (i <- τ.getVertices()) {

      }
      null // TODO
    }

}

