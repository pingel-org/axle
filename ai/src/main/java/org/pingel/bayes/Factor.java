package org.pingel.bayes;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.pingel.util.ListCrossProduct;

/* Technically a "Distribution" is probably a table that sums to 1, which is not
 * always true in a Factor.  They should be siblings rather than parent/child.
 */

public class Factor extends Distribution
{
	private double[] elements;
	private ListCrossProduct<Value> cp;
	List<RandomVariable> varList = new Vector<RandomVariable>();

	private String name = "unnamed";
	
	public Factor(RandomVariable... pVars)
	{
		super(pVars);
		for(RandomVariable rv : pVars) {
			varList.add(rv);
		}
		makeCrossProduct();
	}
	
	public Factor(List<RandomVariable> pVars)
	{
		super(pVars);
		varList.addAll(pVars);
		makeCrossProduct();
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
	
	public String getLabe()
	{
		return name;
	}
	
	private void makeCrossProduct()
	{
		List<List<Value>> valLists = new ArrayList<List<Value>>();
		for( RandomVariable var : varList ) {
			valLists.add(var.getDomain().getValues());
		}
		cp = new ListCrossProduct<Value>(valLists);
		elements = new double[cp.size()];
	}
	
	public Double evaluate(Case prior, Case condition)
	{
		// assume prior and condition are disjoint, and that they are
		// each compatible with this table
		
		double w = 0.0;
		double p = 0.0;
		for(int i=0; i < numCases(); i++) {
			Case c = caseOf(i);
			if( c.isSupersetOf(prior) ) {
				w += read(c);
				if( c.isSupersetOf(condition) ) {
					p += read(c);
				}
			}
		}
		
		return p / w;
	}
	
	public int indexOf(Case c)
	{
		List<Value> objects = c.valuesOf(varList);
		return cp.indexOf(objects);
	}
	
	public Case caseOf(int i) {
		Case result = new Case();
		List<Value> values = cp.get(i);
		result.assign(varList, values);
		return result;
	}
	
	public int numCases()
	{
		return elements.length;
	}
	
	public void write(Case c, double d)
	{
//		System.out.println("write: case = " + c.toOrderedString(variables) + ", d = " + d);
//		System.out.println("variables.length = " + variables.length);
		elements[indexOf(c)] = d;
	}
	
	public double read(Case c)
	{
		return elements[indexOf(c)];
	}
	
	public void print()
	{
		for(int i=0; i < elements.length; i++) {
			Case c = caseOf(i);
			System.out.println(c.toOrderedString(varList) + " " + read(c));
		}
	}
	
	public Factor maxOut(RandomVariable var)
	{
		// Chapter 6 definition 6

		List<RandomVariable> vars = new ArrayList<RandomVariable>();
		for(RandomVariable v : getVariables() ) {
			if( ! var.equals(v) ) {
				vars.add(v);
			}
		}
		
		Factor newFactor = new Factor(vars);
		for(int i=0; i < newFactor.numCases(); i++) {
			Case ci = newFactor.caseOf(i);
			Value bestValue = null;
			double maxSoFar = Double.MIN_VALUE;
			for(Value val : var.getDomain().getValues()) {
				Case cj = newFactor.caseOf(i);
				cj.assign(var, val);
				double s = this.read(cj);
				if( bestValue == null ) {
					maxSoFar = s;
					bestValue = val;
				}
				else {
					if( s > maxSoFar ) {
						maxSoFar = s;
						bestValue = val;
					}
				}
			}
			
			newFactor.write(ci, maxSoFar);
		}
		
		return newFactor;
	}
	
	public Factor projectToOnly(List<RandomVariable> remainingVars)
	{
		Factor result = new Factor(remainingVars);
		
		for( int j=0; j < this.numCases(); j++ )
		{
			Case fromCase = this.caseOf(j);
			Case toCase = fromCase.projectToVars(remainingVars);
			
			double additional = this.read(fromCase);
			double previous = result.read(toCase);
			result.write(toCase, previous + additional);
		}
		
		return result;
	}
	
	private double[][] tally(RandomVariable a, RandomVariable b)
	{
		List<Value> aValues = a.getDomain().getValues();
		List<Value> bValues = b.getDomain().getValues();
		
		double[][] tally = new double[aValues.size()][bValues.size()];
		Case w = new Case();
		int r = 0;
		for( Value aVal : aValues ) {
			w.assign(a, aVal);
			int c = 0;
			for( Value bVal : bValues ) {
				w.assign(b, bVal);
				for( int j=0; j < this.numCases(); j++ )
				{
					Case m = this.caseOf(j);
					if( m.isSupersetOf(w) ) {
						tally[r][c] += this.read(m);
					}
				}
				c++;
			}
			r++;
		}
		return tally;
	}
	
	
	public Factor sumOut(RandomVariable varToSumOut)
	{
		// depending on assumptions, this may not be the best way to remove the vars
		
		List<RandomVariable> newVars = new ArrayList<RandomVariable>();
		for(RandomVariable x : getVariables() ) {
			if( x.compareTo(varToSumOut) != 0 ) {
				newVars.add(x);
			}
		}
		
		Factor result = new Factor(newVars);
		for( int j=0; j < result.numCases(); j++ ) {
			
			Case c = result.caseOf(j);
			
			double p = 0.0;
			
			for( Value val : varToSumOut.getDomain().getValues() ) {
				Case f = c.copy();
				f.assign(varToSumOut, val);
				p += read(f);
			}
			
			result.write(c, p);
		}
		
		return result;
	}

	public Factor sumOut(Set<RandomVariable> varsToSumOut)
	{
		// TODO not the most efficient way to sum out a set of variables
		
		Factor result = this;
		for( RandomVariable v : varsToSumOut ) {
			result = result.sumOut(v);
		}
		
		return result;
	}
	
	public Factor projectRowsConsistentWith(Case e) {
		
		// as defined on chapter 6 page 15
		
		Factor result = new Factor(getVariables());
		
		for( int j=0; j < result.numCases(); j++ ) {
			Case c = result.caseOf(j);
			if( c.isSupersetOf(e) ) {
				result.elements[j] = elements[j];
			}
			else {
				result.elements[j] = 0.0;
			}
		}
		
		return result;
	}
	
	public Factor multiply(Factor other) {
		
		List<RandomVariable> newVarList = new ArrayList<RandomVariable>();
		newVarList.addAll(getVariables());

		Set<RandomVariable> myVarsAsSet = new HashSet<RandomVariable>();
		myVarsAsSet.addAll(getVariables());

		for( RandomVariable x : other.getVariables() ) {
			if( ! myVarsAsSet.contains(x) ) {
				newVarList.add(x);
			}
		}
		
		Factor result = new Factor(newVarList);
		
		for(int j=0; j < result.numCases(); j++) {
			Case c = result.caseOf(j);
			double myContribution = this.read(c);
			double otherContribution = other.read(c);
			result.write(c, myContribution * otherContribution);
		}
		
		return result;
	}
	
	public boolean mentions(RandomVariable var) {
		
		for( RandomVariable mine : getVariables() ) {
			if( var.name.equals(mine.name) ) {
				return true;
			}
		}
		return false;
	}
	
	static Factor multiply(Collection<Factor> tables) {
		
		if( tables.size() == 0 ) {
			return null;
		}
		
		// TODO this can be made more efficient by constructing a single
		// result table ahead of time.
		
		Iterator<Factor> it = tables.iterator();
		Factor current = it.next();
		while( it.hasNext() ) {
			Factor next = it.next();
			current = current.multiply(next);
		}
		
		return current;
	}
}
