package org.pingel.bayes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

public class BayesianNetwork extends Model
{
	private Map<RandomVariable, Factor> var2cpt = new HashMap<RandomVariable, Factor>();
	
	public Factor getJointProbabilityTable()
	{
		Factor jpt = new Factor(getRandomVariables());

		for(int j=0; j < jpt.numCases(); j++) {
			Case c = jpt.caseOf(j);
			double p = probabilityOf(c);
			jpt.write(c, p);
		}

		return jpt;
	}
	

	public void setCPT(RandomVariable rv, Factor factor)
	{
		var2cpt.put(rv, factor);
	}

	private Factor makeFactorFor(RandomVariable variable)
	{
		Set<RandomVariable> vars = getGraph().getPredecessors(variable);
		List<RandomVariable> cptVarList = new ArrayList<RandomVariable>();
		for(RandomVariable rv : getRandomVariables() ) {
			if( vars.contains(rv) ) {
				cptVarList.add(rv);
			}
		}
		cptVarList.add(variable);
		Factor cpt = new Factor(cptVarList);
		cpt.setName("cpt for " + variable.name);

		return cpt;
	}
	
	public Factor getCPT(RandomVariable variable)
	{
		
		Factor cpt = var2cpt.get(variable);
		
		if( cpt == null ) {
			cpt = makeFactorFor(variable);
			var2cpt.put(variable, cpt);
		}
		return cpt;
	}

	public List<Factor> getAllCPTs()
	{
		List<Factor> result = new ArrayList<Factor>();
		
		for(RandomVariable rv : getRandomVariables() ) {
			result.add(getCPT(rv));
		}
		
		return result;
	}
	
	public double probabilityOf(Case c)
	{
		double answer = 1.0;
		
		Iterator<RandomVariable> it = c.getVariables().iterator();
		while( it.hasNext() ) {
		    RandomVariable var = it.next();
			answer *= getCPT(var).read(c);
		}
		
		return answer;
	}

	public void copyTo(BayesianNetwork other)
	{
		super.copyTo(other);
		for(RandomVariable v : var2cpt.keySet()) {
			other.var2cpt.put(v, var2cpt.get(v));
		}
		
	}
	
	public Independence getMarkovAssumptionsFor(RandomVariable var)
	{
		Set<RandomVariable> X = new HashSet<RandomVariable>();
		X.add(var);
		
		Set<RandomVariable> Z = getGraph().getPredecessors(var);

		Set<RandomVariable> D = new HashSet<RandomVariable>();
		getGraph().collectDescendants(var, D);
		D.add(var); // probably already includes this
		D.addAll(getGraph().getPredecessors(var));

		Set<RandomVariable> Y = new HashSet<RandomVariable>();
		Iterator<RandomVariable> it = getRandomVariables().iterator();
		while( it.hasNext() ) {
		    RandomVariable u = it.next();
			if( ! D.contains(u) ) {
				Y.add(u);
			}
		}
		
		return new Independence(X, Z, Y);
	}

	public void printAllMarkovAssumptions()
	{
		Iterator<RandomVariable> it = getRandomVariables().iterator();
		while( it.hasNext() ) {
			RandomVariable var = it.next();
			Independence I = getMarkovAssumptionsFor(var);
			System.out.println(I.toString());
		}
	}

	public double computeFullCase(Case c)
	{
		if( numVariables() != c.size() ) {
			// not an airtight check
			System.err.println("malformed case to computeFullCase");
			System.exit(1);
		}
		
		// order variables such that all nodes appear before their ancestors
		// rewrite using chain rule
		// drop on conditionals in expressions using Markov independence assumptions
		// now each term should simply be a lookup in the curresponding CPT
		// multiply results
		
		return -1.0; // TODO
	}

	public Factor variableEliminationPriorMarginalI(Set Q, List<RandomVariable> pi)
	{
		// Algorithm 1 from Chapter 6 (page  9)
		
		// Q is a set of variables
		// pi is an ordered list of the variables not in Q
		// returns the prior marginal pr(Q)

		Set<Factor> S = new HashSet<Factor>();
		for(RandomVariable var : getRandomVariables() ) {
			S.add(getCPT(var));
		}
		
		for( RandomVariable var : pi ) {
			Set<Factor> allMentions = new HashSet<Factor>();
			Set<Factor> newS = new HashSet<Factor>();

			for( Factor pt : S ) {
				if( pt.mentions(var) ) {
					allMentions.add(pt);
				} else {
					newS.add(pt);
				}
			}

			Factor T = Factor.multiply(allMentions);
			Factor Ti = T.sumOut(var);

			newS.add(Ti);
			S = newS;
		}
		
		Factor result = Factor.multiply(S);
		return result;

		// the cost is the cost of the Tk multiplication
		// this is highly dependent on pi
	}

	public Factor variableEliminationPriorMarginalII(Set Q, List<RandomVariable> pi, Case e) {
		
		// Chapter 6 Algorithm 5 (page 17)

		// assert: Q subset of variables
		// assert: pi ordering of variables in S but not in Q
		// assert: e assigns values to variables in this network
		
		Set<Factor> S = new HashSet<Factor>();
		for( RandomVariable var : getRandomVariables() ) {
			S.add(getCPT(var).projectRowsConsistentWith(e));
		}
		
		for( RandomVariable var : pi ) {
            
			Set<Factor> allMentions = new HashSet<Factor>();
			Set<Factor> newS = new HashSet<Factor>();
            
			for( Factor pt : S ) {
				if( pt.mentions(var) ) {
					allMentions.add(pt);
				} else {
					newS.add(pt);
				}
			}

			Factor T = Factor.multiply(allMentions);
			Factor Ti = T.sumOut(var);

			newS.add(Ti);
			S = newS;
		}

		Factor result = Factor.multiply(S);
		return result;
	}

	public boolean interactsWith(RandomVariable v1, RandomVariable v2)
	{
		for( Factor f : getAllCPTs() ) {
			if( f.mentions(v1) && f.mentions(v2) ) {
				return true;
			}
		}
		return false;
	}
	
	public InteractionGraph interactionGraph()
	{
		// Also called the "moral graph"

		InteractionGraph result = new InteractionGraph();

		List<RandomVariable> rvs = getRandomVariables();
		
		for( RandomVariable rv : rvs ) {
			result.addVertex(rv);
		}
		
		for( int i=0; i < rvs.size() - 1; i++ ) {
			RandomVariable vi = rvs.get(i);
			for( int j=i+1; j < rvs.size(); j++ ) {
				RandomVariable vj = rvs.get(j);
				
				if( interactsWith(vi, vj) ) {
					result.addEdge(new VariableLink(vi, vj));
				}
			}
		}
		
		return result;
	}
	
	public int orderWidth(List order)
	{
		// Chapter 6 Algorithm 2 (page 13)
		
		InteractionGraph G = interactionGraph();
		int w = 0;
        
		for( RandomVariable var : getRandomVariables() ) {
			int d = G.getNeighbors(var).size();
			w = Math.max(w, d);
			G.eliminate(var);
		}
		return w;
	}
	
	public void pruneEdges(Case e)
	{
		// 6.8.2
		
		if( e == null ) {
			return;
		}
		
		for(RandomVariable U : e.getVariables() ) {
			for(ModelEdge edge : getGraph().outputEdgesOf(U) ) {
				
				RandomVariable X = edge.getDest();
				
				Factor oldF = getCPT(X);

				getGraph().deleteEdge(edge);
				Factor smallerF = makeFactorFor(X);

				for(int i=0; i < smallerF.numCases(); i++) {
					Case c = smallerF.caseOf(i);
					// set its value to what e sets it to
					c.assign(U, e.valueOf(U));
					double oldValue = oldF.read(c);
					smallerF.write(smallerF.caseOf(i), oldValue);
				}
				
				setCPT(edge.getDest(), smallerF);
				
			}
		}
		
	}

	public void pruneNodes(Set<RandomVariable> Q, Case e)
	{
		Set<RandomVariable> vars = new HashSet<RandomVariable>();
		if( Q != null ) {
			vars.addAll(Q);
		}
		if( e != null ) {
			vars.addAll(e.getVariables());
		}

//		System.out.println("BN.pruneNodes vars = " + vars);
		
		// not optimally inefficient

		boolean keepGoing = true;
		while( keepGoing ) {
			keepGoing = false;
			for(RandomVariable leaf : getGraph().getLeaves()) {
				if( ! vars.contains(leaf) ) {

					this.deleteVariable(leaf);
					keepGoing = true;
				}
				
			}
		}
		
	}
	
	public void pruneNetwork(Set<RandomVariable> Q, Case e)
	{
		// 6.8.3
		pruneEdges(e);
		pruneNodes(Q, e);

	}

	public Factor variableEliminationPR(Set<RandomVariable> Q, Case e)
	{
		BayesianNetwork pruned = new BayesianNetwork();
		copyTo(pruned);
		pruned.pruneNetwork(Q, e);
		
		Set<RandomVariable> R = new HashSet<RandomVariable>();
		for(RandomVariable v : getRandomVariables() ) {
			if( ! Q.contains(v) ) {
				R.add(v);
			}
		}
		List<RandomVariable> pi = pruned.minDegreeOrder(R);

		Set<Factor> S = new HashSet<Factor>();
		for( RandomVariable var : pruned.getRandomVariables() ) {
			S.add(pruned.getCPT(var).projectRowsConsistentWith(e));
		}
		
		for( RandomVariable var : pi ) {
            
			Set<Factor> allMentions = new HashSet<Factor>();
			Set<Factor> newS = new HashSet<Factor>();
            
			for( Factor pt : S ) {
				if( pt.mentions(var) ) {
					allMentions.add(pt);
				} else {
					newS.add(pt);
				}
			}

			Factor T = Factor.multiply(allMentions);
			Factor Ti = T.sumOut(var);

			newS.add(Ti);
			S = newS;
		}

		Factor result = Factor.multiply(S);
		return result;
	}


	public double variableEliminationMPE(Case e)
	{
		BayesianNetwork pruned = new BayesianNetwork();
		copyTo(pruned);
		pruned.pruneEdges(e);

		List<RandomVariable> Q = pruned.getRandomVariables();

		List<RandomVariable> pi = pruned.minDegreeOrder(Q);

		Set<Factor> S = new HashSet<Factor>();
		for( RandomVariable var : Q ) {
			S.add(pruned.getCPT(var).projectRowsConsistentWith(e));
		}

		for( RandomVariable var : pi ) {
            
			Set<Factor> allMentions = new HashSet<Factor>();
			Set<Factor> newS = new HashSet<Factor>();
            
			for( Factor pt : S ) {
				if( pt.mentions(var) ) {
					allMentions.add(pt);
				}
				else {
					newS.add(pt);
				}
			}

			Factor T = Factor.multiply(allMentions);
			Factor Ti = T.maxOut(var);

			newS.add(Ti);
			S = newS;
		}

		// at this point (since we're iterating over *all* variables in Q)
		// S will contain exactly one trivial Factor
		
		if( S.size() != 1 ) {
			System.err.println("Assertion failed S.size() != 1");
			System.exit(1);
		}
		
		Iterator<Factor> fit = S.iterator();
		Factor result = fit.next();

		if( result.numCases() != 1 ) {
			System.err.println("Assertion failed result.numCases() != 1");
			System.exit(1);
		}
		
		return result.read(result.caseOf(0));
		
	}

	
//	public Case variableEliminationMAP(Set Q, Case e)
//	{
//		// see ch 6 page 31: Algorithm 8
//		// TODO
//      // returns an instantiation q which maximizes Pr(q,e) and that probability	
//
//	}
	
	public List<RandomVariable> minDegreeOrder(Collection<RandomVariable> pX)
	{
		Set<RandomVariable> X = new HashSet<RandomVariable>();
		X.addAll(pX);
		
		InteractionGraph G = interactionGraph();
		List<RandomVariable> result = new ArrayList<RandomVariable>();
		
		while( X.size() > 0 ) {
		    RandomVariable var = G.vertexWithFewestNeighborsAmong(X);
			result.add(var);
			G.eliminate(var);
			X.remove(var);
		}
		return result;
	}

	public List<RandomVariable> minFillOrder(Set<RandomVariable> pX)
	{
		Set<RandomVariable> X = new HashSet<RandomVariable>();
		X.addAll(pX);
		
		InteractionGraph G = interactionGraph();
		List<RandomVariable> result = new ArrayList<RandomVariable>();
		
		while( X.size() > 0 ) {
			RandomVariable var = G.vertexWithFewestEdgesToEliminateAmong(X);
			result.add(var);
			G.eliminate(var);
			X.remove(var);
		}
		return result;
	}

	
	public Factor factorElimination1(Set<RandomVariable> Q)
	{
		List<Factor> S = new ArrayList<Factor>();
		for(RandomVariable var : getRandomVariables() ) {
			S.add(getCPT(var));
		}

		while( S.size() > 1 ) {
			
			Factor fi = S.get(0);
			S.remove(fi);
			
			Set<RandomVariable> V = new HashSet<RandomVariable>();
			for(RandomVariable v : fi.getVariables()) {
				if( ! Q.contains(v) ) {
					boolean vNotInS = true;
					int j = 0;
					while( vNotInS && j < S.size() ) {
						vNotInS = ! S.get(j).mentions(v);
						j++;
					}
					if( vNotInS ) {
						V.add(v);
					}
				}
			}

			// At this point, V is the set of vars that are unique to this particular
			// factor, fj, and do not appear in Q

			Factor fjMinusV = fi.sumOut(V);

			Factor fj = S.get(0);
			S.remove(fj);
			Factor replacement = fj.multiply(fjMinusV);
			S.add(replacement);
		}

		// there should be one element left in S

		Factor f = S.get(0);

		List<RandomVariable> qList = new ArrayList<RandomVariable>();
		qList.addAll(Q);
		
		return f.projectToOnly(qList);
	}
	
	
	public Factor factorElimination2(Set<RandomVariable> Q, EliminationTree tau, EliminationTreeNode r)
	{
		// the variables Q appear on the CPT for the product of Factors assigned to node r

		while( tau.getVertices().size() > 1 ) {

			// remove node i (other than r) that has single neighbor j in tau

			EliminationTreeNode i = tau.firstLeafOtherThan(r);
			EliminationTreeNode j = tau.getNeighbors(i).iterator().next();
			Factor phi_i = tau.getFactor(i);
			tau.delete(i);
			
			Set<RandomVariable> allVarsInTau = tau.getAllVariables();
			Set<RandomVariable> V = new HashSet<RandomVariable>();
			for(RandomVariable v : phi_i.getVariables()) {
				if( ! allVarsInTau.contains(v) ) {
					V.add(v);
				}
			}

			Factor lean = phi_i.sumOut(V);
			tau.addFactor(j, lean);

			tau.draw();
		}

		List<RandomVariable> qList = new ArrayList<RandomVariable>();
		qList.addAll(Q);
		
		return tau.getFactor(r).projectToOnly(qList);
	}

	
	public Factor factorElimination3(Set<RandomVariable> Q, EliminationTree tau, EliminationTreeNode r)
	{
		// Q is a subset of C_r

		while( tau.getVertices().size() > 1 ) {

			// remove node i (other than r) that has single neighbor j in tau

			EliminationTreeNode i = tau.firstLeafOtherThan(r);
			EliminationTreeNode j = tau.getNeighbors(i).iterator().next();
			Factor phi_i = tau.getFactor(i);
			tau.delete(i);
			
			Set<RandomVariable> Sij = tau.separate(i, j);
			List<RandomVariable> sijList = new ArrayList<RandomVariable>();
			sijList.addAll(Sij);

			tau.addFactor(j, phi_i.projectToOnly(sijList));

			tau.draw();
		}

		List<RandomVariable> qList = new ArrayList<RandomVariable>();
		qList.addAll(Q);
		
		return tau.getFactor(r).projectToOnly(qList);
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
//		// TODO EliminationTreeNode r = chooseRoot(tau);
//		
//		// TODO pullMessagesTowardsRoot();
//		// TODO pushMessagesFromRoot();
//		
//		for(EliminationTreeNode i : tau.getVertices()) {
//			
//		}
//		
//	}
	
	
}
