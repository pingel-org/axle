package axle.stats

import collection._

case class CaseX() extends Comparable[CaseX] {

  val assignments = mutable.Map[RandomVariable[_], Any]() // NOTE: was TreeMap

  def getVariables(): Set[RandomVariable[_]] = assignments.keySet

  def size() = assignments.keySet.size

  def valueOf(variable: RandomVariable[_]): Any = assignments(variable)

  // Note: this may contain null entries if assignments.keySet() is a strict subset of vars
  def valuesOf(vars: List[RandomVariable[_]]): List[Any] = vars.map(assignments(_))

  //  def assign(rv: RandomVariable[_], value: Any): Unit = assignments += rv -> value
  //
  //  def assign(vars: List[RandomVariable[_]], vals: List[Any]): Unit = {
  //    for (i <- 0 until vars.size) {
  //      assignments += vars(i) -> vals(i)
  //    }
  //  }

//  def copy(): CaseX = {
//    val result = new CaseX()
//    for ((rv, value) <- assignments) { result.assignments += rv -> value }
//    result
//  }


  // TODO !!!
  //  override def equals(o: Object) = o match {
  //    case c: CaseX => compareTo(c) == 0
  //    case _ => false
  //  }

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
        return 0 // TODO !!! myValue.compareTo(otherValue)
      }
    }
    0
  }

  override def toString(): String = assignments.keySet.map(rv => {
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
