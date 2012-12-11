
package org.pingel.bayes

import scala.collection._
import axle.matrix.JblasMatrixFactory._

class PartiallyDirectedGraph(variables: List[RandomVariable]) {

  var variable2index = Map[RandomVariable, Int]()

  val connected = falses(variables.size, variables.size)
  val marked = falses(variables.size, variables.size)
  val arrowed = falses(variables.size, variables.size)

  for (i <- 0 until variables.size) {
    variable2index += variables(i) -> i
  }

  def indexOf(v: RandomVariable): Int = variable2index(v).intValue

  def connect(v1: RandomVariable, v2: RandomVariable): Unit = {
    val i1 = indexOf(v1)
    val i2 = indexOf(v2)
    connected.setValueAt(i1, i2, true)
    connected.setValueAt(i2, i1, true)
  }

  def areAdjacent(v1: RandomVariable, v2: RandomVariable): Boolean =
    connected.valueAt(indexOf(v1), indexOf(v2))

  def undirectedAdjacent(v1: RandomVariable, v2: RandomVariable): Boolean = {
    val i1 = indexOf(v1)
    val i2 = indexOf(v2)
    connected.valueAt(i1, i2) && (!arrowed.valueAt(i1, i2)) && (!arrowed.valueAt(i2, i1))
  }

  def mark(v1: RandomVariable, v2: RandomVariable): Unit = {
    val i1 = indexOf(v1)
    val i2 = indexOf(v2)
    marked.setValueAt(i1, i2, true)
    marked.setValueAt(i2, i1, true)
  }

  def orient(v1: RandomVariable, v2: RandomVariable): Unit = {
    // Note: we assume they are already adjacent without checking
    val i1 = indexOf(v1)
    val i2 = indexOf(v2)
    arrowed.setValueAt(i1, i2, true)
  }

  def size: Int = variables.size

  // TODO: scala version should probably use Option[Boolean] instead of allowing null
  def links(v: RandomVariable, arrowInOpt: Option[Boolean], markedOpt: Option[Boolean], arrowOutOpt: Option[Boolean]): List[RandomVariable] = {

    var result = mutable.ListBuffer[RandomVariable]()

    val i = indexOf(v)

    for (j <- 0 until size) {

      val u = variables(j)
      var pass = connected.valueAt(i, j)

      if (pass && (arrowInOpt != None)) {
        pass = (arrowInOpt.get == arrowed.valueAt(j, i))
      }

      if (pass && (markedOpt != None)) {
        pass = (markedOpt.get == marked.valueAt(i, j))
      }

      if (pass && (arrowOutOpt != None)) {
        pass = (arrowOutOpt.get == arrowed.valueAt(i, j))
      }

      if (pass) {
        result += u
      }
    }

    result.toList
  }

  def markedPathExists(from: RandomVariable, target: RandomVariable): Boolean = {
    // this will not terminate if there are cycles

    var frontier = mutable.ListBuffer[RandomVariable]()
    frontier += from

    while (frontier.size > 0) {
      val head = frontier.remove(0)
      //      val head = frontier(0)
      //      frontier.removeElementAt(0)
      if (head.equals(target)) {
        return true
      }
      var follow = links(head, None, Some(true), Some(true))
      frontier.appendAll(follow)
    }
    false
  }

  override def toString(): String = {

    var result = ""

    for (rv <- variables) {
      result += "var " + rv + " has index " + variable2index(rv)
      result += "\n"
    }

    result += "connect\n\n"
    for (i <- 0 until variables.size) {
      for (j <- 0 until variables.size) {
        if (connected.valueAt(i, j)) {
          result += "x"
        } else {
          result += " "
        }
      }
      result += "\n"
    }
    result += "\n\n"

    result += "mark\n\n"
    for (i <- 0 until variables.size) {
      for (j <- 0 until variables.size) {
        if (marked.valueAt(i, j)) {
          result += "x"
        } else {
          result += " "
        }
      }
      result += "\n"
    }
    result += "\n\n"

    result += "arrow\n\n"
    for (i <- 0 until variables.size) {
      for (j <- 0 until variables.size) {
        if (arrowed.valueAt(i, j)) {
          result += "x"
        } else {
          result += " "
        }
      }
      result += "\n"
    }
    result += "\n\n"

    result
  }
}
