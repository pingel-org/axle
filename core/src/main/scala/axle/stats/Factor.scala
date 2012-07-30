package org.pingel.bayes

import axle.iterator.ListCrossProduct
import axle.matrix.JblasMatrixFactory._
import org.pingel.gestalt.core.Value
import scala.collection._

object Factor {

  def multiply(tables: Collection[Factor]): Factor = {

    if (tables.size == 0) {
      return null
    }

    // TODO this can be made more efficient by constructing a single
    // result table ahead of time.

    tables.reduceLeft((current, table) => current.multiply(table))
  }

}

/* Technically a "Distribution" is probably a table that sums to 1, which is not
 * always true in a Factor.  They should be siblings rather than parent/child.
 */

class Factor(varList: List[RandomVariable]) extends Distribution(varList) {

  var elements: Array[Double]

  var cp: ListCrossProduct[Value] = null

  makeCrossProduct()

  var name = "unnamed"

  def setName(name: String): Unit = { this.name = name; }

  def getName(): String = name

  def getLabel(): String = name

  def makeCrossProduct(): Unit = {
    val valLists: List[List[Value]] = varList.map(_.getDomain.map(_.getValues).getOrElse(Nil))
    cp = new ListCrossProduct[Value](valLists)
    elements = new Array[Double](cp.size)
  }

  def evaluate(prior: Case, condition: Case): Double = {
    // assume prior and condition are disjoint, and that they are
    // each compatible with this table

    var w = 0.0
    var p = 0.0
    for (i <- 0 until numCases) {
      val c = caseOf(i)
      if (c.isSupersetOf(prior)) {
        w += read(c)
        if (c.isSupersetOf(condition)) {
          p += read(c)
        }
      }
    }

    p / w
  }

  def indexOf(c: Case): Int = {
    val objects = c.valuesOf(varList)
    cp.indexOf(objects)
  }

  def caseOf(i: Int): Case = {
    val result = new Case()
    val values = cp(i)
    result.assign(varList, values)
    result
  }

  def numCases() = elements.length

  def write(c: Case, d: Double): Unit = {
    // println("write: case = " + c.toOrderedString(variables) + ", d = " + d)
    // println("variables.length = " + variables.length)
    elements(indexOf(c)) = d
  }

  def writes(values: List[Double]): Unit = {
    assert(values.length == elements.length)
    values.zipWithIndex.map({ case (v, i) => elements(i) = v})
  }
  
  def read(c: Case): Double = elements(indexOf(c))

  def print(): Unit = {
    for (i <- 0 until elements.length) {
      val c = caseOf(i)
      println(c.toOrderedString(varList) + " " + read(c))
    }
  }

  def maxOut(variable: RandomVariable): Factor = {
    // Chapter 6 definition 6

    val vars = getVariables.filter(v => !variable.equals(v))

    val newFactor = new Factor(vars)
    for (i <- 0 until newFactor.numCases()) {
      def ci = newFactor.caseOf(i)
      var bestValue: Value = null
      var maxSoFar = Double.MinValue
      for (value <- variable.getDomain.map(_.getValues).getOrElse(Nil)) {
        var cj = newFactor.caseOf(i)
        cj.assign(variable, value)
        val s = this.read(cj)
        if (bestValue == null) {
          maxSoFar = s
          bestValue = value
        } else {
          if (s > maxSoFar) {
            maxSoFar = s
            bestValue = value
          }
        }
      }
      newFactor.write(ci, maxSoFar)
    }
    newFactor
  }

  def projectToOnly(remainingVars: List[RandomVariable]): Factor = {
    val result = new Factor(remainingVars)
    for (j <- 0 until numCases) {
      val fromCase = this.caseOf(j)
      val toCase = fromCase.projectToVars(remainingVars)
      val additional = this.read(fromCase)
      val previous = result.read(toCase)
      result.write(toCase, previous + additional)
    }
    result
  }

  def tally(a: RandomVariable, b: RandomVariable): Matrix[Double] = {
    val aValues = a.getDomain.map(_.getValues).getOrElse(Nil)
    val bValues = b.getDomain.map(_.getValues).getOrElse(Nil)

    val tally = zeros[Double](aValues.size, bValues.size)
    val w = new Case()
    var r = 0
    for (aVal <- aValues) {
      w.assign(a, aVal)
      var c = 0
      for (bVal <- bValues) {
        w.assign(b, bVal)
        for (j <- 0 until numCases) {
          val m = this.caseOf(j)
          if (m.isSupersetOf(w)) {
            tally.setValueAt(r, c, tally.valueAt(r, c) + this.read(m))
          }
        }
        c += 1
      }
      r += 1
    }
    tally
  }

  def sumOut(varToSumOut: RandomVariable): Factor = {
    // depending on assumptions, this may not be the best way to remove the vars

    val result = new Factor(getVariables().filter(x => x.compareTo(varToSumOut) != 0).toList)
    for (j <- 0 until result.numCases()) {
      val c = result.caseOf(j)
      var p = 0.0
      for (value <- varToSumOut.getDomain.map(_.getValues).getOrElse(Nil)) {
        val f = c.copy()
        f.assign(varToSumOut, value)
        p += read(f)
      }
      result.write(c, p)
    }

    result
  }

  def sumOut(varsToSumOut: Set[RandomVariable]): Factor =
    varsToSumOut.foldLeft(this)((result, v) => result.sumOut(v))

  def projectRowsConsistentWith(e: Case): Factor = {
    // as defined on chapter 6 page 15
    val result = new Factor(getVariables())
    for (j <- 0 until result.numCases) {
      result.elements(j) = c.isSupersetOf(e) match {
        case true => elements(j)
        case false => 0.0
      }
    }
    result
  }

  def multiply(other: Factor): Factor = {

    val newVars = getVariables().union(other.getVariables())
    val result = new Factor(newVars.toList)
    for (j <- 0 until result.numCases()) {
      val c = result.caseOf(j)
      val myContribution = this.read(c)
      val otherContribution = other.read(c)
      result.write(c, myContribution * otherContribution)
    }
    result
  }

  def mentions(variable: RandomVariable) = getVariables.exists(v => variable.getName.equals(v.getName))

}
