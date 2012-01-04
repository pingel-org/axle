package org.pingel.bayes;

/**
Notes on unimplemented aspects of Bayesian networks:

Bayes Rule

  pr(A=a|B=b) = pr(B=b|A=a)pr(A=a)/pr(B=b)
  
Case analysis

  pr(A=a) = pr(A=a, B=b1) + ... + pr(A=a, B=bN)

Chain Rule

  pr(X1=x1, ..., XN = xN) = pr(X1=x1 | X2=x2, ..., XN=xN) * ... * pr(XN=xN)

Jeffrey's Rule

  ???

markov independence: a variable is independent of its non-descendents given its parents

d-separation cases:

  x: given variable (it appears in Z)
  o: not given

  Sequence:
  
  -> o ->   open
  -> x ->   closed

  Divergent:

  <- o ->   open
  <- x ->   closed
  
  Convergent:  

  -> o <-   closed (and none of the descendants of the node are in Z)
  -> x <-   open (or if any of the descendants of the node are in Z)


  A path is blocked (independent) if any valve in the path is blocked.
  
  Two variables are separated if all paths are blocked.

Independence

  I(X, Z, Y) is read "X is independent of Y given Z"

Symmetry (ch 3 stuff)

  I(X, Z, Y) <=> I(Y, Z, X)

Decomposition

  I(X, Z, Y or W) <=> I(X, Z, Y) and I(X, Z, W)

Weak Union

  I(X, Z, Y or W) => I(X, Z or Y, W)

Contraction

  I(X, Z, Y) and I(X, Z or Y, W) => I(X, Z, Y or W)

Intersection

  I(X, Z or W, Y) and I(X, Z or Y, W) => I(X, Z, Y or W)

independence closure

    ???  used for what ??? 

MPE: most probable explanation

  find {x1, ..., xN} such that pr(X1=x1, ..., XN=xN | e) is maximized

  MPE is much easier to compute algorithmically

MAP: maximum a posteriori hypothesis

  Let M be a subset of network variables X
  
  Let e be some evidence
  
  Find an instantiation m over variables M such that pr(M=m|e) is maximized

Tree networks (ch5 p20)

  are the ones where each node has at most one parent
  treewidth <= 1

A polytree is:

  there is at most one (undirected) path between any two nodes
  treewidth = maximum number of parents  

 */

import scala.collection._

class BayesianNetwork(name: String="bn") extends Model(name)
{
  var var2cpt = Map[RandomVariable, Factor]()
	
  def getJointProbabilityTable(): Factor = {
    var jpt = new Factor(getRandomVariables())
    for(j <- 0 until jpt.numCases ) {
      val c = jpt.caseOf(j)
      jpt.write(c, probabilityOf(c))
    }
    jpt
  }

  def setCPT(rv: RandomVariable, factor: Factor): Unit = var2cpt += rv -> factor

  def makeFactorFor(variable: RandomVariable): Factor = {
    var vars = getGraph().getPredecessors(variable) // Set<RandomVariable>
    val cptVarList = getRandomVariables.filter( rv => vars.contains(rv) )
    var cpt = new Factor(cptVarList ++ List(variable))
    cpt.setName("cpt for " + variable.getName)
    cpt
  }
	
  def getCPT(variable: RandomVariable): Factor = {
    var cpt = var2cpt(variable)
    if( cpt == null ) {
      cpt = makeFactorFor(variable)
      var2cpt += variable -> cpt
    }
    cpt
  }

  def getAllCPTs(): List[Factor] = getRandomVariables.map( getCPT(_) )

  def probabilityOf(c: Case) = c.getVariables.map( getCPT(_).read(c) ).foldLeft(1.0)(_*_)

  def copyTo(other: BayesianNetwork): Unit = {
    super.copyTo(other)
    var2cpt.keys.map( v => { other.var2cpt += v -> var2cpt(v) } )
  }
  
  def getMarkovAssumptionsFor(rv: RandomVariable): Independence = {

    var X = immutable.Set(rv)

    var Z = getGraph().getPredecessors(rv)

    var D = mutable.Set[RandomVariable]()
    getGraph().collectDescendants(rv, D)
    D += rv // probably already includes this
    D ++= getGraph().getPredecessors(rv)

    val Y = getRandomVariables.filter( ! D.contains(_) ).toSet
    
    new Independence(X, Z, Y)
  }

  def printAllMarkovAssumptions(): Unit = 
    getRandomVariables.map( rv => println(getMarkovAssumptionsFor(rv)) )

  def computeFullCase(c: Case): Double = {
    if( numVariables() != c.size() ) {
      // not an airtight check
      System.err.println("malformed case to computeFullCase")
      System.exit(1)
    }
    
    // order variables such that all nodes appear before their ancestors
    // rewrite using chain rule
    // drop on conditionals in expressions using Markov independence assumptions
    // now each term should simply be a lookup in the curresponding CPT
    // multiply results
    
    -1.0 // TODO
  }

  def variableEliminationPriorMarginalI(Q: Set[RandomVariable], π: List[RandomVariable]): Factor = {
    // Algorithm 1 from Chapter 6 (page  9)
    
    // Q is a set of variables
    // pi is an ordered list of the variables not in Q
    // returns the prior marginal pr(Q)

    var S = mutable.Set[Factor]()
    for( rv <- getRandomVariables() ) {
      S.add(getCPT(rv))
    }
    
    for( rv <- π ) {
      var allMentions = mutable.Set[Factor]()
      var newS = mutable.Set[Factor]()
      for( pt <- S ) {
    	  if( pt.mentions(rv) ) {
    		  allMentions.add(pt)
    	  }
    	  else {
    		  newS.add(pt)
    	  }
      }
      
      val T = Factor.multiply(allMentions)
      val Ti = T.sumOut(rv)
      newS.add(Ti)
      S = newS
    }
    
    Factor.multiply(S)

    // the cost is the cost of the Tk multiplication
    // this is highly dependent on π
  }

  def variableEliminationPriorMarginalII(Q: Set[RandomVariable], π: List[RandomVariable], e: Case): Factor = {
    
    // Chapter 6 Algorithm 5 (page 17)

    // assert: Q subset of variables
    // assert: π ordering of variables in S but not in Q
    // assert: e assigns values to variables in this network
    
    var S = mutable.Set[Factor]()
    for( rv <- getRandomVariables() ) {
      S.add(getCPT(rv).projectRowsConsistentWith(e))
    }
    
    for( rv <- π ) {
      var allMentions = mutable.Set[Factor]()
      var newS = mutable.Set[Factor]()
      for( pt <- S ) {
    	  if( pt.mentions(rv) ) {
    		  allMentions.add(pt)
    	  }
    	  else {
    		  newS.add(pt)
    	  }
      }
      
      val T = Factor.multiply(allMentions)
      val Ti = T.sumOut(rv)
      
      newS.add(Ti)
      S = newS
    }
    
    Factor.multiply(S)
  }
  
  def interactsWith(v1: RandomVariable, v2: RandomVariable): Boolean = {
    for( f <- getAllCPTs() ) {
      if( f.mentions(v1) && f.mentions(v2) ) {
    	  return true
      }
    }
    false
  }
  
  def interactionGraph(): InteractionGraph = {
    // Also called the "moral graph"

    var result = new InteractionGraph()
    
    getRandomVariables.map( rv => result.addVertex(rv) )
    
    val rvs = getRandomVariables()
    for( i <- 0 to rvs.size-2 ) {
      val vi = rvs(i)
      for( j <- i+1 until rvs.size ) {
    	  val vj = rvs(j)
          if( interactsWith(vi, vj) ) {
        	  result.addEdge(new VariableLink(vi, vj))
          }
      }
    }
    
    result
  }
  
  def orderWidth(order: List[RandomVariable]): Integer = {
    // Chapter 6 Algorithm 2 (page 13)
    
    var G = interactionGraph()
    var w = 0
    
    for( rv <- getRandomVariables() ) {
      val d = G.getNeighbors(rv).size
      w = Math.max(w, d)
      G.eliminate(rv)
    }
    w
  }
  
  def pruneEdges(e: Case): Unit = {
    // 6.8.2
    
    if( e == null ) {
      return
    }
    
    for( U <- e.getVariables() ) {
      for( edge <- getGraph().outputEdgesOf(U) ) { // ModelEdge
	
    	  val X = edge.getDest()
    	  val oldF = getCPT(X)
	
    	  getGraph().deleteEdge(edge)
    	  val smallerF = makeFactorFor(X)
    	  for( i <- 0 until smallerF.numCases ) {
    		  val c = smallerF.caseOf(i)
    		  // set its value to what e sets it to
    		  c.assign(U, e.valueOf(U))
    		  val oldValue = oldF.read(c)
    		  smallerF.write(smallerF.caseOf(i), oldValue)
    	  }
    	  setCPT(edge.getDest(), smallerF)
      }
    }
    
  }
  
  def pruneNodes(Q: Set[RandomVariable], e: Case): Unit = {
    var vars = mutable.Set[RandomVariable]()
    if( Q != null ) {
      vars ++= Q
    }
    if( e != null ) {
      vars ++= e.getVariables()
    }
    
    // System.out.println("BN.pruneNodes vars = " + vars);
    
    // not optimally efficient
    
    var keepGoing = true
    while( keepGoing ) {
      keepGoing = false
      for( leaf <- getGraph().getLeaves()) { // RandomVariable
    	  if( ! vars.contains(leaf) ) {
    		  deleteVariable(leaf)
    		  keepGoing = true
    	  }
      }
    }
    
  }
  
  def pruneNetwork(Q: Set[RandomVariable], e: Case): Unit = {
    // 6.8.3
    pruneEdges(e)
    pruneNodes(Q, e)
  }
  
  def variableEliminationPR(Q: Set[RandomVariable], e: Case): Factor = {
    var pruned = new BayesianNetwork()
    copyTo(pruned)
    pruned.pruneNetwork(Q, e)
    
    var R = mutable.Set[RandomVariable]()
    for( v <- getRandomVariables() ) {
      if( ! Q.contains(v) ) {
    	  R.add(v)
      }
    }
    val π = pruned.minDegreeOrder(R)
    
    var S = mutable.Set[Factor]()
    for( rv <- pruned.getRandomVariables() ) {
      S.add(pruned.getCPT(rv).projectRowsConsistentWith(e))
    }
    
    for( rv <- π ) {
      
      var allMentions = mutable.Set[Factor]()
      var newS = mutable.Set[Factor]()
      
      for( pt <- S ) {
    	  if( pt.mentions(rv) ) {
    		  allMentions.add(pt)
    	  }
    	  else {
    		  newS.add(pt)
    	  }
      }
      
      val T = Factor.multiply(allMentions)
      val Ti = T.sumOut(rv)
      
      newS.add(Ti)
      S = newS
    }
    
    Factor.multiply(S)
  }
  
  def variableEliminationMPE(e: Case): Double = {
    var pruned = new BayesianNetwork()
    copyTo(pruned)
    pruned.pruneEdges(e)
    
    val Q = pruned.getRandomVariables()
    val π = pruned.minDegreeOrder(Q)
    
    var S = mutable.Set[Factor]()
    for( rv <- Q ) {
      S.add(pruned.getCPT(rv).projectRowsConsistentWith(e))
    }
    
    for( rv <- π ) {
      
      var allMentions = mutable.Set[Factor]()
      var newS = mutable.Set[Factor]()
      
      for( pt <- S ) {
    	  if( pt.mentions(rv) ) {
    		  allMentions.add(pt)
    	  }
    	  else {
    		  newS.add(pt)
    	  }
      }
      
      val T = Factor.multiply(allMentions)
      val Ti = T.maxOut(rv)
      
      newS.add(Ti)
      S = newS
    }
    
    // at this point (since we're iterating over *all* variables in Q)
    // S will contain exactly one trivial Factor
    
    if( S.size != 1 ) {
      System.err.println("Assertion failed S.size() != 1")
      System.exit(1)
    }
    
    val fit = S.iterator
    val result = fit.next()
    
    if( result.numCases() != 1 ) {
      System.err.println("Assertion failed result.numCases() != 1")
      System.exit(1)
    }
    
    result.read(result.caseOf(0))
  }
  
  //	public Case variableEliminationMAP(Set Q, Case e)
  //	{
  //		 see ch 6 page 31: Algorithm 8
  //		 TODO
  //      returns an instantiation q which maximizes Pr(q,e) and that probability	
  //
  //	}
  
  def minDegreeOrder(pX: Collection[RandomVariable]): List[RandomVariable] = {
    
    var X = Set[RandomVariable]()
    X ++= pX
    
    var G = interactionGraph()
    var result = mutable.ListBuffer[RandomVariable]()
    
    while( X.size > 0 ) {
      val rv = G.vertexWithFewestNeighborsAmong(X)
      result += rv
      G.eliminate(rv)
      X -= rv
    }
    result.toList
  }
  
  def minFillOrder(pX: Set[RandomVariable]): List[RandomVariable] = {
    var X = Set[RandomVariable]()
    X ++= pX
    
    var G = interactionGraph()
    var result = mutable.ListBuffer[RandomVariable]()
    
    while( X.size > 0 ) {
      val rv = G.vertexWithFewestEdgesToEliminateAmong(X)
      result += rv
      G.eliminate(rv)
      X -= rv
    }
    result.toList
  }
  
  
  def factorElimination1(Q: Set[RandomVariable]): Factor = {
    
    var S = mutable.ListBuffer[Factor]()
    getRandomVariables.map( rv => S += getCPT(rv) )
    
    while( S.size > 1 ) {
      
      val fi = S(0)
      S -= fi
      
      var V = mutable.Set[RandomVariable]()
      for( v <- fi.getVariables()) {
    	  if( ! Q.contains(v) ) {
    		  var vNotInS = true
    		  var j = 0
    		  while( vNotInS && j < S.size ) {
                 vNotInS = ! S(j).mentions(v)
                 j += 1
    		  }
    		  if( vNotInS ) {
    			  V.add(v)
    		  }
    	  }
      }
      
      // At this point, V is the set of vars that are unique to this particular
      // factor, fj, and do not appear in Q
      
      val fjMinusV = fi.sumOut(V)
      
      val fj = S(0)
      S -= fj
      S += fj.multiply(fjMinusV)
    }
    
    // there should be one element left in S
    
    val f = S(0)

    f.projectToOnly(Q.toList)
  }
  
  
  def factorElimination2(Q: Set[RandomVariable], τ: EliminationTree, r: EliminationTreeNode): Factor = {
    // the variables Q appear on the CPT for the product of Factors assigned to node r
    
    while( τ.getVertices().size > 1 ) {
      
      // remove node i (other than r) that has single neighbor j in tau
      
      val i = τ.firstLeafOtherThan(r)
      val j = τ.getNeighbors(i).iterator.next()
      val ɸ_i = τ.getFactor(i)
      τ.delete(i)
      
      val allVarsInTau = τ.getAllVariables()
      var V = mutable.Set[RandomVariable]()
      for( v <- ɸ_i.getVariables()) {
    	  if( ! allVarsInTau.contains(v) ) {
    		  V.add(v)
    	  }
      }
      τ.addFactor(j, ɸ_i.sumOut(V))
      τ.draw()
    }
    
    τ.getFactor(r).projectToOnly(Q.toList)
  }
  
  
  def factorElimination3(Q: Set[RandomVariable], τ: EliminationTree, r: EliminationTreeNode): Factor = {
    // Q is a subset of C_r
    
    while( τ.getVertices().size > 1 ) {
      // remove node i (other than r) that has single neighbor j in tau
      val i = τ.firstLeafOtherThan(r)
      val j = τ.getNeighbors(i).iterator.next()
      val ɸ_i = τ.getFactor(i)
      τ.delete(i)
      val Sij = τ.separate(i, j)
      var sijList = mutable.ListBuffer[RandomVariable]()
      sijList ++= Sij
      τ.addFactor(j, ɸ_i.projectToOnly(sijList))
      τ.draw()
    }
    
    τ.getFactor(r).projectToOnly(Q.toList)
  }
  
  //	public Map<EliminationTreeNode, Factor> factorElimination(EliminationTree tau, Case e)
  //	{
  //		
  //		for(EliminationTreeNode i : tau.getVertices() ) {
  //
  //			for(RandomVariable E : e.getVariables() ) {
  //
  //				Factor lambdaE = new Factor(E);
  //				// assign lambdaE.E to e.get(E)
  //			}
  //			
  //		}
  //		
  //    TODO EliminationTreeNode r = chooseRoot(tau);
  //		
  //    TODO pullMessagesTowardsRoot();
  //	TODO pushMessagesFromRoot();
  //		
  //		for(EliminationTreeNode i : tau.getVertices()) {
  //			
  //		}
  //		
  //	}
  
	
}
