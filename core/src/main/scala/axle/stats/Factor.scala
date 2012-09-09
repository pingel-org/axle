package axle.stats

import axle._
import axle.IndexedCrossProduct
import axle.matrix.JblasMatrixFactory._
import collection._
import scalaz._
import Scalaz._

/* Technically a "Distribution" is probably a table that sums to 1, which is not
 * always true in a Factor.  They should be siblings rather than parent/child.
 */

object Factor {

  def apply(varList: Seq[RandomVariable[_]], values: Map[Seq[CaseIs[_]], Double]): Factor =
    new Factor(varList, values)

  def spaceFor(varSeq: Seq[RandomVariable[_]]): Iterator[Seq[CaseIs[_]]] = {
    val x = varSeq.map(_.getValues.getOrElse(Nil).toIndexedSeq)
    val kaseIt = IndexedCrossProduct(x).iterator
    kaseIt.map(kase => kase.zipWithIndex.map({
      case (v, i: Int) => {
        val rv = varSeq(i).asInstanceOf[RandomVariable[Any]] // TODO: remove cast
        CaseIs(rv, v)
      }
    }))
  }

}

class Factor(varList: Seq[RandomVariable[_]], values: Map[Seq[CaseIs[_]], Double]) {

  lazy val cp = new IndexedCrossProduct(varList.map(
    _.getValues.getOrElse(Nil.toIndexedSeq)
  ))

  lazy val elements = (0 until cp.size).map(i => values.get(caseOf(i)).getOrElse(0.0)).toArray

  def getVariables() = varList

  // assume prior and condition are disjoint, and that they are
  // each compatible with this table
  def evaluate(prior: Seq[CaseIs[_]], condition: Seq[CaseIs[_]]): Double = {
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

  def indexOf(cs: Seq[CaseIs[_]]): Int = {
    val rvvs: Seq[(RandomVariable[_], Any)] = cs.map(ci => (ci.rv, ci.v))
    val rvvm = rvvs.toMap
    cp.indexOf(varList.map(rvvm(_)))
  }

  private def caseOf(i: Int): Seq[CaseIs[_]] =
    varList.zip(cp(i)).map({ case (variable: RandomVariable[_], value) => CaseIs(variable, value) })

  def cases(): Iterator[Seq[CaseIs[_]]] = (0 until elements.length).iterator.map(caseOf(_))

  // def update(c: Seq[CaseIs[_]], d: Double): Unit = elements(indexOf(c)) = d

  def apply(c: Seq[CaseIs[_]]): Double = elements(indexOf(c))

  override def toString(): String =
    varList.map(rv => rv.getName.padTo(rv.charWidth, " ").mkString("")).mkString(" ") + "\n" +
      cases.map(kase =>
        kase.map(ci => ci.v.toString.padTo(ci.rv.charWidth, " ").mkString("")).mkString(" ") +
          " " + "%f".format(this(kase))
      ).mkString("\n")

  def toHtml(): xml.Node =
    <table border={ "1" }>
      <tr>{ varList.map(rv => <td>{ rv.getName }</td>): xml.NodeSeq }<td>P</td></tr>
      {
        cases.map(kase => <tr>
                            { kase.map(ci => <td>{ ci.v.toString }</td>) }
                            <td>{ "%f".format(this(kase)) }</td>
                          </tr>)
      }
    </table>

  // Chapter 6 definition 6
  def maxOut[T](variable: RandomVariable[T]): Factor = {
    val newVars = getVariables.filter(!variable.equals(_))
    new Factor(newVars,
      Factor.spaceFor(newVars)
        .map(kase => (kase, variable.getValues.getOrElse(Nil).map(value => this(kase)).max))
        .toMap
    )
  }

  def projectToOnly(remainingVars: List[RandomVariable[_]]): Factor =
    new Factor(remainingVars,
      Factor.spaceFor(remainingVars).toList
        .map(kase => (projectToVars(kase, remainingVars.toSet), this(kase)))
        .groupBy(_._1)
        .map({ case (k, v) => (k, v.map(_._2).sum) })
        .toMap
    )

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
    val newVars = varList.filter(!_.equals(varToSumOut)).toList
    new Factor(newVars,
      Factor.spaceFor(newVars).toSeq
        .map(kase => (kase.filter(_.rv != varToSumOut), this(kase)))
        .groupBy(kv => kv._1)
        .map({ case (k, v) => (k, v.map(_._2).sum) })
        .toMap
    )
  }

  def Σ(varsToSumOut: Set[RandomVariable[_]]): Factor = sumOut(varsToSumOut)

  def sumOut(varsToSumOut: Set[RandomVariable[_]]): Factor =
    varsToSumOut.foldLeft(this)((result, v) => result.sumOut(v))

  // as defined on chapter 6 page 15
  def projectRowsConsistentWith(eOpt: Option[List[CaseIs[_]]]): Factor = {
    val e = eOpt.get
    new Factor(getVariables(),
      Factor.spaceFor(e.map(_.rv)).map(kase => (kase, isSupersetOf(kase, e) ? this(kase) | 0.0)).toMap
    )
  }

  def *(other: Factor): Factor = {
    val newVars = (getVariables().toSet union other.getVariables().toSet).toList
    new Factor(newVars.toList, Factor.spaceFor(newVars).map(kase => (kase, this(kase) * other(kase))).toMap)
  }

  def mentions(variable: RandomVariable[_]) = getVariables.exists(v => variable.getName.equals(v.getName))

  def isSupersetOf(left: Seq[CaseIs[_]], right: Seq[CaseIs[_]]): Boolean = {
    val ll: Seq[(RandomVariable[_], Any)] = left.map(ci => (ci.rv, ci.v))
    val lm = ll.toMap
    right.forall((rightCaseIs: CaseIs[_]) => lm.contains(rightCaseIs.rv) && (rightCaseIs.v == lm(rightCaseIs.rv)))
  }

  def projectToVars(cs: Seq[CaseIs[_]], pVars: Set[RandomVariable[_]]): Seq[CaseIs[_]] =
    cs.filter(ci => pVars.contains(ci.rv))

}
