package axle.stats

import scala.reflect.ClassTag
import scala.xml.NodeSeq.seqToNodeSeq

import axle.IndexedCrossProduct
import axle.matrix.JblasMatrixModule.Matrix
import axle.matrix.JblasMatrixModule.convertDouble
import axle.matrix.JblasMatrixModule.matrix
import spire.algebra.Eq
import spire.algebra.Field
import spire.algebra.MultiplicativeMonoid
import spire.algebra.Order
import spire.implicits.RingProduct2
import spire.implicits.StringOrder
import spire.implicits.convertableOps
import spire.implicits.eqOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.math.ConvertableFrom

object FactorModule extends FactorModule

trait FactorModule {

  /* Technically a "Distribution" is probably a table that sums to 1, which is not
   * always true in a Factor.  They should be siblings rather than parent/child.
   */

  object Factor {

    implicit def factorEq[T: Eq, N: Field]: Eq[Factor[T, N]] = new Eq[Factor[T, N]] {
      def eqv(x: Factor[T, N], y: Factor[T, N]): Boolean = x equals y // TODO
    }

    implicit def factorMultMonoid[T: Eq, N: Field: ConvertableFrom: Order: Ordering: ClassTag]: MultiplicativeMonoid[Factor[T, N]] = new MultiplicativeMonoid[Factor[T, N]] {

      lazy val field = implicitly[Field[N]]

      def times(x: Factor[T, N], y: Factor[T, N]): Factor[T, N] = {
        val newVars = (x.variables.toSet union y.variables.toSet).toVector
        new Factor(newVars, Factor.cases(newVars).map(kase => (kase, x(kase) * y(kase))).toMap)
      }
      def one: Factor[T, N] = new Factor(Vector.empty, Map.empty.withDefaultValue(field.one))
    }

    def apply[T: Eq, N: Field: ConvertableFrom: Order: Ordering: ClassTag](varList: Vector[RandomVariable[T, N]], values: Map[Vector[CaseIs[T, N]], N]): Factor[T, N] =
      new Factor(varList, values)

    def cases[T: Eq, N: Field](varSeq: Vector[RandomVariable[T, N]]): Iterable[Vector[CaseIs[T, N]]] =
      IndexedCrossProduct(varSeq.map(_.values)) map { kase =>
        varSeq.zip(kase) map {
          case (rv, v) =>
            CaseIs(rv, v)
        } toVector
      }

  }

  class Factor[T: Eq, N: Field: Order: Ordering: ClassTag: ConvertableFrom](varList: Vector[RandomVariable[T, N]], values: Map[Vector[CaseIs[T, N]], N]) {

    val field = implicitly[Field[N]]

    lazy val crossProduct = new IndexedCrossProduct(varList.map(_.values))

    lazy val elements: Array[N] =
      (0 until crossProduct.size) map { i =>
        values.get(caseOf(i)).getOrElse(field.zero)
      } toArray

    def variables: Vector[RandomVariable[T, N]] = varList

    // assume prior and condition are disjoint, and that they are
    // each compatible with this table

    def evaluate(prior: Seq[CaseIs[T, N]], condition: Seq[CaseIs[T, N]]): N = {
      val pw = axle.Σ(cases.map(c => {
        if (isSupersetOf(c, prior)) {
          if (isSupersetOf(c, condition)) {
            (this(c), this(c))
          } else {
            (this(c), field.zero)
          }
        } else {
          (field.zero, field.zero)
        }
      }).toVector)(identity)

      pw._1 / pw._2
    }

    def indexOf(cs: Seq[CaseIs[T, N]]): Int = {
      val rvvs: Seq[(RandomVariable[T, N], T)] = cs.map(ci => (ci.rv, ci.v))
      val rvvm = rvvs.toMap
      crossProduct.indexOf(varList.map(rvvm))
    }

    private[this] def caseOf(i: Int): Vector[CaseIs[T, N]] =
      varList.zip(crossProduct(i)) map { case (variable, value) => CaseIs(variable, value) }

    def cases: Iterable[Seq[CaseIs[T, N]]] = (0 until elements.length) map { caseOf }

    def apply(c: Seq[CaseIs[T, N]]): N = elements(indexOf(c))

    override def toString: String =
      varList.map(rv => rv.name.padTo(rv.charWidth, " ").mkString("")).mkString(" ") + "\n" +
        cases.map(kase =>
          kase.map(ci => ci.v.toString.padTo(ci.rv.charWidth, " ").mkString("")).mkString(" ") +
            " " + this(kase).toString).mkString("\n") // Note: was "%f".format() prior to spire.math

    def toHtml: xml.Node =
      <table border={ "1" }>
        <tr>{ varList.map(rv => <td>{ rv.name }</td>): xml.NodeSeq }<td>P</td></tr>
        {
          cases.map(kase =>
            <tr>
              { kase.map(ci => <td>{ ci.v.toString }</td>) }
              <td>{ this(kase) }</td>
            </tr>)
        }
      </table>

    // Chapter 6 definition 6
    def maxOut(variable: RandomVariable[T, N]): Factor[T, N] = {
      val newVars = variables.filter(v => !(variable === v))
      new Factor(newVars,
        Factor.cases(newVars)
          .map(kase => (kase, variable.values.map(value => this(kase)).max))
          .toMap)
    }

    def projectToOnly(remainingVars: Vector[RandomVariable[T, N]]): Factor[T, N] =
      new Factor(remainingVars,
        Factor.cases[T, N](remainingVars).toVector
          .map(kase => (projectToVars(kase, remainingVars.toSet), this(kase)))
          .groupBy(_._1)
          .map({ case (k, v) => (k.toVector, axle.Σ(v.map(_._2))(identity)) })
          .toMap)

    def tally(a: RandomVariable[T, N], b: RandomVariable[T, N]): Matrix[Double] = {
      matrix[Double](
        a.values.size,
        b.values.size,
        (r: Int, c: Int) => axle.Σ(cases.filter(isSupersetOf(_, Vector(a is a.values(r), b is b.values(c)))).map(this(_)).toVector)(identity).toDouble)
    }

    def Σ(varToSumOut: RandomVariable[T, N]): Factor[T, N] = this.sumOut(varToSumOut)

    // depending on assumptions, this may not be the best way to remove the vars
    def sumOut(gone: RandomVariable[T, N]): Factor[T, N] = {
      val position = varList.indexOf(gone)
      val newVars = varList.filter(v => !(v === gone))
      new Factor(
        newVars,
        Factor.cases(newVars).map(kase => {
          val reals = gone.values.map(gv => {
            val ciGone = List(CaseIs(gone.asInstanceOf[RandomVariable[T, N]], gv)) // TODO cast
            this(kase.slice(0, position) ++ ciGone ++ kase.slice(position, kase.length))
          })
          (kase, axle.Σ(reals)(identity))
        }).toMap)
    }

    def Σ(varsToSumOut: Set[RandomVariable[T, N]]): Factor[T, N] = sumOut(varsToSumOut)

    def sumOut(varsToSumOut: Set[RandomVariable[T, N]]): Factor[T, N] =
      varsToSumOut.foldLeft(this)((result, v) => result.sumOut(v))

    // as defined on chapter 6 page 15
    def projectRowsConsistentWith(eOpt: Option[List[CaseIs[T, N]]]): Factor[T, N] = {
      val e = eOpt.get
      new Factor(variables,
        Factor.cases(e.map(_.rv).toVector).map(kase => (kase, if (isSupersetOf(kase, e)) this(kase) else field.zero)).toMap)
    }

    def mentions(variable: RandomVariable[T, N]): Boolean =
      variables.exists(v => variable.name === v.name)

    def isSupersetOf(left: Seq[CaseIs[T, N]], right: Seq[CaseIs[T, N]]): Boolean = {
      val ll: Seq[(RandomVariable[T, N], T)] = left.map(ci => (ci.rv, ci.v))
      val lm = ll.toMap
      right.forall((rightCaseIs: CaseIs[T, N]) => lm.contains(rightCaseIs.rv) && (rightCaseIs.v === lm(rightCaseIs.rv)))
    }

    def projectToVars(cs: Seq[CaseIs[T, N]], pVars: Set[RandomVariable[T, N]]): Seq[CaseIs[T, N]] =
      cs.filter(ci => pVars.contains(ci.rv))

  }

}
