package org.pingel.bayes

import org.pingel.gestalt.core.Value

// OLD VERSION HAD THESE DATA MEMBERS IN CONSTRUCTOR:
// rv: RandomVariable, value: Value

case class Case() extends Comparable[Case] {
  var assignments = Map[RandomVariable, Value]() // NOTE: was TreeMap

  // OLD VERSION DID THIS: assign(rv, value)

  def getVariables(): Set[RandomVariable] = assignments.keySet

  def size() = assignments.keySet.size

  def valueOf(variable: RandomVariable): Value = {
    assignments(variable)
  }

  def valuesOf(vars: List[RandomVariable]): List[Value] = {
    // Note: this may contain null entries if assignments.keySet()
    // is a strict subset of vars
    vars map { assignments(_) }
  }

  def assign(rv: RandomVariable, value: Value): Unit = {
    assignments += rv -> value
  }

  def assign(vars: List[RandomVariable], vals: List[Value]): Unit = {
    for (i <- 0 until vars.size) {
      assignments += vars(i) -> vals(i)
    }
  }

  def isSupersetOf(other: Case): Boolean = {
    val it = other.assignments.keySet.iterator
    while (it.hasNext) {
      val variable = it.next
      val otherVal = other.valueOf(variable)
      val thisVal = valueOf(variable)
      if (otherVal != null && thisVal != null && !thisVal.equals(otherVal)) {
        return false
      }
    }
    true
  }

  def copy(): Case = {
    var result = new Case()
    for ((rv, value) <- assignments) {
      result.assignments += rv -> value
    }
    result
  }

  def projectToVars(pVars: List[RandomVariable]): Case = {
    var result = new Case()
    for (variable <- pVars) {
      result.assign(variable, valueOf(variable))
    }
    result
  }

  override def equals(o: Object) = o match {
    case c: Case => compareTo(c) == 0
    case _ => false
  }

  def compareTo(other: Case): Integer = {
    if (assignments.size < other.assignments.size) {
      return -1
    }
    if (assignments.size > other.assignments.size) {
      return 1
    }
    for (variable <- assignments.keySet) {
      val myValue = assignments(variable)
      val otherValue = other.assignments(variable)
      if (!myValue.equals(otherValue)) {
        return myValue.compareTo(otherValue)
      }
    }
    0
  }

  override def toString(): String = {
    var result = ""
    for (rv <- assignments.keySet) {
      // System.out.println("rv = " + rv.name);
      result += rv.name + " = ";
      if (assignments.contains(rv)) {
        result += assignments(rv).toString()
      } else {
        result += "null"
      }
      result += ", "
    }
    result
  }

  def toOrderedString(vs: List[RandomVariable]): String =
    (for (variable <- vs) yield variable.getName + " = " + assignments(variable)).mkString(", ")

}
