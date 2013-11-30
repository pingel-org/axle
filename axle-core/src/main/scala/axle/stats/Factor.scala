package axle.stats

import axle._
import axle.matrix._
import axle.IndexedCrossProduct
import spire.math._
import spire.implicits._
import spire.algebra._

object FactorModule extends FactorModule

trait FactorModule {

  /* Technically a "Distribution" is probably a table that sums to 1, which is not
   * always true in a Factor.  They should be siblings rather than parent/child.
   */

  import JblasMatrixModule._

  object Factor {

    implicit def factorEq[T: Eq] = new Eq[Factor[T]] {
      def eqv(x: Factor[T], y: Factor[T]): Boolean = x equals y // TODO
    }
    
    def apply[T: Eq](varList: Vector[RandomVariable[T]], values: Map[Vector[CaseIs[T]], Real]): Factor[T] =
      new Factor(varList, values)

    def spaceFor[T: Eq](varSeq: Vector[RandomVariable[T]]): Iterator[Vector[CaseIs[T]]] = {
      val x = varSeq.map(_.values.getOrElse(Nil).toIndexedSeq)
      val kaseIt = IndexedCrossProduct(x).iterator
      kaseIt.map(kase => kase.zipWithIndex.map({
        case (v, i: Int) => {
          val rv = varSeq(i) // .asInstanceOf[RandomVariable[Any]] // TODO: remove cast
          CaseIs(rv, v)
        }
      }).toVector)
    }

  }

  class Factor[T: Eq](varList: Vector[RandomVariable[T]], values: Map[Vector[CaseIs[T]], Real]) {

    lazy val cp = new IndexedCrossProduct(varList.map(
      _.values.getOrElse(Nil.toIndexedSeq)))

    lazy val elements = (0 until cp.size).map(i => values.get(caseOf(i)).getOrElse(Real(0))).toArray

    def variables = varList

    // assume prior and condition are disjoint, and that they are
    // each compatible with this table

    def evaluate(prior: Seq[CaseIs[T]], condition: Seq[CaseIs[T]]): Real = {
      val pw = cases.map(c => {
        if (isSupersetOf(c, prior)) {
          if (isSupersetOf(c, condition)) {
            (this(c), this(c))
          } else {
            (this(c), Real(0))
          }
        } else {
          (Real(0), Real(0))
        }
      }).reduce(_ + _)

      pw._1 / pw._2
    }

    def indexOf(cs: Seq[CaseIs[T]]): Int = {
      val rvvs: Seq[(RandomVariable[T], T)] = cs.map(ci => (ci.rv, ci.v))
      val rvvm = rvvs.toMap
      cp.indexOf(varList.map(rvvm(_)))
    }

    private def caseOf(i: Int): Vector[CaseIs[T]] =
      varList.zip(cp(i)).map({ case (variable: RandomVariable[T], value) => CaseIs(variable, value) })

    def cases: Iterator[Seq[CaseIs[T]]] = (0 until elements.length).iterator.map(caseOf(_))

    // def update(c: Seq[CaseIs[T]], d: Double): Unit = elements(indexOf(c)) = d

    def apply(c: Seq[CaseIs[T]]): Real = elements(indexOf(c))

    override def toString(): String =
      varList.map(rv => rv.name.padTo(rv.charWidth, " ").mkString("")).mkString(" ") + "\n" +
        cases.map(kase =>
          kase.map(ci => ci.v.toString.padTo(ci.rv.charWidth, " ").mkString("")).mkString(" ") +
            " " + "%f".format(this(kase))).mkString("\n")

    def toHtml(): xml.Node =
      <table border={ "1" }>
        <tr>{ varList.map(rv => <td>{ rv.name }</td>): xml.NodeSeq }<td>P</td></tr>
        {
          cases.map(kase => <tr>
                              { kase.map(ci => <td>{ ci.v.toString }</td>) }
                              <td>{ "%f".format(this(kase)) }</td>
                            </tr>)
        }
      </table>

    // Chapter 6 definition 6
    def maxOut(variable: RandomVariable[T]): Factor[T] = {
      val newVars = variables.filter(v => !(variable === v))
      new Factor(newVars,
        Factor.spaceFor(newVars)
          .map(kase => (kase, variable.values.getOrElse(Nil).map(value => this(kase)).max))
          .toMap)
    }

    def projectToOnly(remainingVars: Vector[RandomVariable[T]]): Factor[T] =
      new Factor(remainingVars,
        Factor.spaceFor[T](remainingVars).toVector
          .map(kase => (projectToVars(kase, remainingVars.toSet), this(kase)))
          .groupBy(_._1)
          .map({ case (k, v) => (k.toVector, v.map(_._2).Σ(identity)) })
          .toMap)

    def tally(a: RandomVariable[T], b: RandomVariable[T]): Matrix[Double] = {
      val aValues = a.values.getOrElse(Nil).toIndexedSeq
      val bValues = b.values.getOrElse(Nil).toIndexedSeq
      matrix[Double](
        aValues.size,
        bValues.size,
        (r: Int, c: Int) => cases.filter(isSupersetOf(_, Vector(a is aValues(r), b is bValues(c)))).map(this(_)).toVector.Σ(identity).toDouble)
    }

    def Σ(varToSumOut: RandomVariable[T]): Factor[T] = this.sumOut(varToSumOut)

    // depending on assumptions, this may not be the best way to remove the vars
    def sumOut(gone: RandomVariable[T]): Factor[T] = {
      val position = varList.indexOf(gone)
      val newVars = varList.filter(!_.equals(gone))
      new Factor(newVars,
        Factor.spaceFor(newVars)
          .map(kase => (kase,
            gone.values.getOrElse(Nil).map(gv => {
              val ciGone = List(CaseIs(gone.asInstanceOf[RandomVariable[T]], gv)) // TODO cast
              this(kase.slice(0, position) ++ ciGone ++ kase.slice(position, kase.length))
            }).reduce(_ + _))).toMap)
    }

    def Σ(varsToSumOut: Set[RandomVariable[T]]): Factor[T] = sumOut(varsToSumOut)

    def sumOut(varsToSumOut: Set[RandomVariable[T]]): Factor[T] =
      varsToSumOut.foldLeft(this)((result, v) => result.sumOut(v))

    // as defined on chapter 6 page 15
    def projectRowsConsistentWith(eOpt: Option[List[CaseIs[T]]]): Factor[T] = {
      val e = eOpt.get
      new Factor(variables,
        Factor.spaceFor(e.map(_.rv).toVector).map(kase => (kase, if (isSupersetOf(kase, e)) this(kase) else Real(0))).toMap)
    }

    def *(other: Factor[T]): Factor[T] = {
      val newVars = (variables.toSet union other.variables.toSet).toVector
      new Factor(newVars, Factor.spaceFor(newVars).map(kase => (kase, this(kase) * other(kase))).toMap)
    }

    def mentions(variable: RandomVariable[T]) = variables.exists(v => variable.name.equals(v.name))

    def isSupersetOf(left: Seq[CaseIs[T]], right: Seq[CaseIs[T]]): Boolean = {
      val ll: Seq[(RandomVariable[T], T)] = left.map(ci => (ci.rv, ci.v))
      val lm = ll.toMap
      right.forall((rightCaseIs: CaseIs[T]) => lm.contains(rightCaseIs.rv) && (rightCaseIs.v == lm(rightCaseIs.rv)))
    }

    def projectToVars(cs: Seq[CaseIs[T]], pVars: Set[RandomVariable[T]]): Seq[CaseIs[T]] =
      cs.filter(ci => pVars.contains(ci.rv))

  }

}
