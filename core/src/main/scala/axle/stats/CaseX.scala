package axle.stats

// OLD VERSION HAD THESE DATA MEMBERS IN CONSTRUCTOR:
// rv: RandomVariable, value: Value

case class CaseX() extends Comparable[CaseX] {
  var assignments = Map[RandomVariable[_], Any]() // NOTE: was TreeMap

  // OLD VERSION DID THIS: assign(rv, value)

  def getVariables(): Set[RandomVariable[_]] = assignments.keySet

  def size() = assignments.keySet.size

  def valueOf(variable: RandomVariable[_]): Any = assignments(variable)

  def valuesOf(vars: List[RandomVariable[_]]): List[Any] = {
    // Note: this may contain null entries if assignments.keySet()
    // is a strict subset of vars
    vars map { assignments(_) }
  }

  def assign(rv: RandomVariable[_], value: Any): Unit = {
    assignments += rv -> value
  }

  def assign(vars: List[RandomVariable[_]], vals: List[Any]): Unit = {
    for (i <- 0 until vars.size) {
      assignments += vars(i) -> vals(i)
    }
  }

  def isSupersetOf(other: CaseX): Boolean = {
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

  def copy(): CaseX = {
    var result = new CaseX()
    for ((rv, value) <- assignments) {
      result.assignments += rv -> value
    }
    result
  }

  def projectToVars(pVars: List[RandomVariable[_]]): CaseX = {
    var result = new CaseX()
    for (variable <- pVars) {
      result.assign(variable, valueOf(variable))
    }
    result
  }

  override def equals(o: Object) = o match {
    case c: CaseX => compareTo(c) == 0
    case _ => false
  }

  def compareTo(other: CaseX): Int = {
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

  override def toString(): String =
    assignments.keySet.map(rv => {
      rv.getName + " = " +
        (if (assignments.contains(rv)) {
          assignments(rv).toString()
        } else {
          "null"
        })
    }).mkString(", ")

  def toOrderedString(vs: List[RandomVariable[_]]): String =
    (for (variable <- vs) yield variable.getName + " = " + assignments(variable)).mkString(", ")

}
