package org.pingel.bayes

class Case(rv: RandomVariable, value: Value) extends Comparable[Case]
{
  var assignments = new TreeMap[RandomVariable, Value]()

  assign(rv, value)

  def getVariables(): Set[RandomVariable] = assignments.keySet
	
  def size(): Int = assignments.keySet.size

  def valueOf(variable: RandomVariable): Value = {
    assignments(variable)
  }

  def valuesOf(vars: List[RandomVariable]): List[Value] =  {

    // Note: this may contain null entries if assignments.keySet()
    // is a strict subset of vars
    
    var result = List[Value]()
    for( i <- 0 to (vars.length-1) ) {
      result.add(assignments(vars(i)))
    }
    result
  }
  
  def assign(rv: RandomVariable, value: Value): Unit = {
    assignments.put(rv, value);
  }

  def assign(vars: List[RandomVariable], vals: List[Value]): Unit = {
    for(i <- 0 to (vars.size-1) ) {
      val variable = vars(i)
      val value = vals(i)
      assignments += variable -> value
    }
  }

  def isSupersetOf(other: Case): Boolean = {
    val it = other.assignments.keySet.iterator
    while( it.hasNext() ) {
      val variable = it.next
      val otherVal = other.valueOf(variable)
      val thisVal = valueOf(variable)
      if ( otherVal != null && thisVal != null && ! thisVal.equals(otherVal) ) {
	return false
      }
    }
    true
  }
	
  def copy(): Case = {
    var result = new Case()
    result.assignments = new TreeMap[RandomVariable, Value]();
    result.assignments.putAll(assignments);
    result
  }

  def projectToVars(pVars: List[RandomVariable]): Case = {
    var result = new Case()
    for( variable <- pVars ) {
      result.assign(variable, valueOf(variable))
    }
    result
  }

  override def equals(o: Object): Boolean = {
    if( o instanceof Case ) {
      return compareTo((Case)o) == 0;
    }
    else {
      return false
    }
  }
	
  def compareTo(other: Case): Integer = {
    if( assignments.size < other.assignments.size ) {
      return -1
    }
    if( assignments.size > other.assignments.size ) {
      return 1
    }
    for( variable <- assignments.keySet ) {
      val myValue = assignments(variable)
      val otherValue = other.assignments(variable)
      if( ! myValue.equals(otherValue) ) {
	return myValue.compareTo(otherValue)
      }
    }
    0
  }
  
  override def toString(): String = {
    var result = ""
    for( rv <- assignments.keySet ) {
      // System.out.println("rv = " + rv.name);
      result += rv.name + " = ";
      if(  assignments.contains(rv) ) {
        result += assignments(rv).toString()
      }
      else {
        result += "null"
      }
      result += ", "
    }
    result
  }
    
  def toOrderedString(vs: List[RandomVariable]): String = 
    (for( variable <- vs ) yield variable.getName + " = " + assignments(variable)).mkString(", ")
  
}
