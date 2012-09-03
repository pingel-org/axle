package axle.stats

import axle._
import axle.ListCrossProduct
import axle.matrix.JblasMatrixFactory._
import collection._

//object Factor {
//
//  // TODO this can be made more efficient by constructing a single result table ahead of time.
//  def Π(tables: Seq[Factor]): Factor = tables.reduce(_*_)
//
//}

/* Technically a "Distribution" is probably a table that sums to 1, which is not
 * always true in a Factor.  They should be siblings rather than parent/child.
 */

class Factor(varList: List[RandomVariable[_]], name: String = "unnamed") {

  import scalaz._
  import Scalaz._

  val valLists = varList.map(rv => rv.getValues.getOrElse(Nil).toList)
  val cp = new ListCrossProduct(valLists)
  val elements = new Array[Double](cp.size)

  // var name = "unnamed"
  // def setName(name: String): Unit = { this.name = name }

  def getName(): String = name

  def getLabel(): String = name

  def getVariables() = varList

  // assume prior and condition are disjoint, and that they are
  // each compatible with this table
  def evaluate(prior: List[CaseIs[_]], condition: List[CaseIs[_]]): Double = {
    val pw = cases().map(c => {
      if (isSupersetOf(c, prior)) {
        if (isSupersetOf(c, condition)) {
          (this(c), this(c))
        } else {
          (this(c), 0.0)
        }
      } else {
        (0.0, 0.0)
      }
    }).reduce(_ |+| _)

    pw._1 / pw._2
  }

  def indexOf(cs: List[CaseIs[_]]): Int = {
    val rvvs: List[(RandomVariable[_], Any)] = cs.map(ci => (ci.rv, ci.v))
    val rvvm = rvvs.toMap
    cp.indexOf(varList.map(rvvm(_)))
  }

  private def caseOf(i: Int): List[CaseIs[_]] =
    varList.zip(cp(i)).map({ case (variable: RandomVariable[_], value) => CaseIs(variable, value) })

  def cases(): Iterator[List[CaseIs[_]]] = (0 until elements.length).iterator.map(caseOf(_))

  // println("write: case = " + c.toOrderedString(variables) + ", d = " + d)
  // println("variables.length = " + variables.length)
  // def update[A](c: CaseIs[A], d: Double): Unit = elements(indexOf(c)) = d

  def update(c: List[CaseIs[_]], d: Double): Unit = elements(indexOf(c)) = d

  //  def writes(values: List[Double]): Unit = {
  //    assert(values.length == elements.length)
  //    values.zipWithIndex.map({ case (v, i) => elements(i) = v })
  //  }

  def apply(c: List[CaseIs[_]]): Double = elements(indexOf(c))

  override def toString(): String =
    varList.map(rv => rv.getName.padTo(rv.charWidth, " ").mkString("")).mkString(" ") + "\n" +
      cases.map(kase =>
        kase.map(ci => ci.v.toString.padTo(ci.rv.charWidth, " ").mkString("")).mkString(" ") +
          " " + "%f".format(this(kase))
      ).mkString("\n")

  // Chapter 6 definition 6
  def maxOut[T](variable: RandomVariable[T]): Factor = {
    val newFactor = new Factor(getVariables.filter(!variable.equals(_)))
    for (c <- newFactor.cases()) {
      newFactor(c) = variable.getValues.getOrElse(Nil).map(value => this(c)).max
    }
    newFactor
  }

  def projectToOnly(remainingVars: List[RandomVariable[_]]): Factor = {
    val result = new Factor(remainingVars)
    for (fromCase <- cases()) {
      val toCase = projectToVars(fromCase, remainingVars.toSet)
      val additional = this(fromCase)
      val previous = result(toCase)
      result(toCase) = previous + additional
    }
    result
  }

  def tally[A, B](a: RandomVariable[A], b: RandomVariable[B]): Matrix[Double] = {
    val aValues = a.getValues.getOrElse(Nil).toList
    val bValues = b.getValues.getOrElse(Nil).toList
    val tally = zeros[Double](aValues.size, bValues.size)
    aValues.zipWithIndex.map({
      case (aVal, r) => {
        bValues.zipWithIndex.map({
          case (bVal, c) => {
            for (m <- cases()) {
              if (isSupersetOf(m, List(a eq aVal, b eq bVal))) {
                tally(r, c) += this(m)
              }
            }
          }
        })
      }
    })
    tally
  }

  def Σ[T](varToSumOut: RandomVariable[T]): Factor = sumOut(varToSumOut)

  // depending on assumptions, this may not be the best way to remove the vars
  def sumOut[T](varToSumOut: RandomVariable[T]): Factor = {
    val result = new Factor(getVariables().filter(!_.equals(varToSumOut)).toList)
    for (c <- cases()) {
      result(c.filter(_.rv != varToSumOut)) += this(c)
    }
    result
  }

  def Σ(varsToSumOut: Set[RandomVariable[_]]): Factor = sumOut(varsToSumOut)

  def sumOut(varsToSumOut: Set[RandomVariable[_]]): Factor =
    varsToSumOut.foldLeft(this)((result, v) => result.sumOut(v))

  // as defined on chapter 6 page 15
  def projectRowsConsistentWith(eOpt: Option[List[CaseIs[_]]]): Factor = {
    val e = eOpt.get
    val result = new Factor(getVariables())
    for (c <- result.cases()) {
      result(c) = (isSupersetOf(c, e) match {
        case true => this(c)
        case false => 0.0
      })
    }
    result
  }

  def *(other: Factor): Factor = {
    val newVars = getVariables().toSet union other.getVariables().toSet
    val result = new Factor(newVars.toList)
    for (c <- result.cases()) {
      result(c) = this(c) * other(c)
    }
    result
  }

  def mentions(variable: RandomVariable[_]) = getVariables.exists(v => variable.getName.equals(v.getName))

  def isSupersetOf(left: List[CaseIs[_]], right: List[CaseIs[_]]): Boolean = {
    val ll: List[(RandomVariable[_], Any)] = left.map(ci => (ci.rv, ci.v))
    val lm = ll.toMap
    right.forall((rightCaseIs: CaseIs[_]) => lm.contains(rightCaseIs.rv) && (rightCaseIs.v == lm(rightCaseIs.rv)))
  }

  def projectToVars(cs: List[CaseIs[_]], pVars: Set[RandomVariable[_]]): List[CaseIs[_]] =
    cs.filter(ci => pVars.contains(ci.rv))

}
