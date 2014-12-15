package axle.stats

import scala.reflect.ClassTag
import scala.xml.NodeSeq.seqToNodeSeq

import axle.IndexedCrossProduct
import axle.algebra.LinearAlgebra
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
import spire.optional.unicode.Σ
import spire.compat.ordering

import axle.Show
import axle.string

/* Technically a "Distribution" is probably a table that sums to 1, which is not
   * always true in a Factor.  They should be siblings rather than parent/child.
   */

object Factor {

  implicit def showFactor[T: Show, N: Show]: Show[Factor[T, N]] = new Show[Factor[T, N]] {

    def text(factor: Factor[T, N]): String = {
      import factor._
      varList.map(d => d.name.padTo(d.charWidth, " ").mkString("")).mkString(" ") + "\n" +
        factor.cases.map(kase =>
          kase.map(ci => string(ci.v).padTo(ci.distribution.charWidth, " ").mkString("")).mkString(" ") +
            " " + string(factor(kase))).mkString("\n") // Note: was "%f".format() prior to spire.math
    }

  }

  implicit def factorEq[T: Eq, N: Field]: Eq[Factor[T, N]] = new Eq[Factor[T, N]] {
    def eqv(x: Factor[T, N], y: Factor[T, N]): Boolean = x equals y // TODO
  }

  implicit def factorMultMonoid[T: Eq: Show, N: Field: ConvertableFrom: Order: ClassTag]: MultiplicativeMonoid[Factor[T, N]] =
    new MultiplicativeMonoid[Factor[T, N]] {

      lazy val field = implicitly[Field[N]]

      def times(x: Factor[T, N], y: Factor[T, N]): Factor[T, N] = {
        val newVars = (x.variables.toSet union y.variables.toSet).toVector
        new Factor(newVars, Factor.cases(newVars).map(kase => (kase, x(kase) * y(kase))).toMap)
      }
      def one: Factor[T, N] = new Factor(Vector.empty, Map.empty.withDefaultValue(field.one))
    }

  def apply[T: Eq: Show, N: Field: ConvertableFrom: Order: ClassTag](varList: Vector[Distribution[T, N]], values: Map[Vector[CaseIs[T, N]], N]): Factor[T, N] =
    new Factor(varList, values)

  def cases[T: Eq, N: Field](varSeq: Vector[Distribution[T, N]]): Iterable[Vector[CaseIs[T, N]]] =
    IndexedCrossProduct(varSeq.map(_.values)) map { kase =>
      varSeq.zip(kase) map {
        case (rv, v) =>
          CaseIs(rv, v)
      } toVector
    }

}

class Factor[T: Eq: Show, N: Field: Order: ClassTag: ConvertableFrom](val varList: Vector[Distribution[T, N]], val values: Map[Vector[CaseIs[T, N]], N]) {

  val field = implicitly[Field[N]]

  lazy val crossProduct = new IndexedCrossProduct(varList.map(_.values))

  lazy val elements: Array[N] =
    (0 until crossProduct.size) map { i =>
      values.get(caseOf(i)).getOrElse(field.zero)
    } toArray

  def variables: Vector[Distribution[T, N]] = varList

  // assume prior and condition are disjoint, and that they are
  // each compatible with this table

  def evaluate(prior: Seq[CaseIs[T, N]], condition: Seq[CaseIs[T, N]]): N = {
    val pw = spire.optional.unicode.Σ(cases.map(c => {
      if (isSupersetOf(c, prior)) {
        if (isSupersetOf(c, condition)) {
          (this(c), this(c))
        } else {
          (this(c), field.zero)
        }
      } else {
        (field.zero, field.zero)
      }
    }).toVector)

    pw._1 / pw._2
  }

  def indexOf(cs: Seq[CaseIs[T, N]]): Int = {
    val rvvs: Seq[(Distribution[T, N], T)] = cs.map(ci => (ci.distribution, ci.v))
    val rvvm = rvvs.toMap
    crossProduct.indexOf(varList.map(rvvm))
  }

  private[this] def caseOf(i: Int): Vector[CaseIs[T, N]] =
    varList.zip(crossProduct(i)) map { case (variable, value) => CaseIs(variable, value) }

  def cases: Iterable[Seq[CaseIs[T, N]]] = (0 until elements.length) map { caseOf }

  def apply(c: Seq[CaseIs[T, N]]): N = elements(indexOf(c))

  def toHtml: xml.Node =
    <table border={ "1" }>
      <tr>{ varList.map(rv => <td>{ rv.name }</td>): xml.NodeSeq }<td>P</td></tr>
      {
        cases.map(kase =>
          <tr>
            { kase.map(ci => <td>{ string(ci.v) }</td>) }
            <td>{ this(kase) }</td>
          </tr>)
      }
    </table>

  // Chapter 6 definition 6
  def maxOut(variable: Distribution[T, N]): Factor[T, N] = {
    val newVars = variables.filter(v => !(variable === v))
    new Factor(newVars,
      Factor.cases(newVars)
        .map(kase => (kase, variable.values.map(value => this(kase)).max))
        .toMap)
  }

  def projectToOnly(remainingVars: Vector[Distribution[T, N]]): Factor[T, N] =
    new Factor(remainingVars,
      Factor.cases[T, N](remainingVars).toVector
        .map(kase => (projectToVars(kase, remainingVars.toSet), this(kase)))
        .groupBy(_._1)
        .map({ case (k, v) => (k.toVector, spire.optional.unicode.Σ(v.map(_._2))) })
        .toMap)

  def tally[M](a: Distribution[T, N], b: Distribution[T, N])(implicit la: LinearAlgebra[M, Double]): M =
    la.matrix(
      a.values.size,
      b.values.size,
      (r: Int, c: Int) => spire.optional.unicode.Σ(cases.filter(isSupersetOf(_, Vector(a is a.values(r), b is b.values(c)))).map(this(_)).toVector).toDouble)

  def Σ(varToSumOut: Distribution[T, N]): Factor[T, N] = this.sumOut(varToSumOut)

  // depending on assumptions, this may not be the best way to remove the vars
  def sumOut(gone: Distribution[T, N]): Factor[T, N] = {
    val position = varList.indexOf(gone)
    val newVars = varList.filter(v => !(v === gone))
    new Factor(
      newVars,
      Factor.cases(newVars).map(kase => {
        val reals = gone.values.map(gv => {
          val ciGone = List(CaseIs(gone.asInstanceOf[Distribution[T, N]], gv)) // TODO cast
          this(kase.slice(0, position) ++ ciGone ++ kase.slice(position, kase.length))
        })
        (kase, spire.optional.unicode.Σ(reals))
      }).toMap)
  }

  def Σ(varsToSumOut: Set[Distribution[T, N]]): Factor[T, N] = sumOut(varsToSumOut)

  def sumOut(varsToSumOut: Set[Distribution[T, N]]): Factor[T, N] =
    varsToSumOut.foldLeft(this)((result, v) => result.sumOut(v))

  // as defined on chapter 6 page 15
  def projectRowsConsistentWith(eOpt: Option[List[CaseIs[T, N]]]): Factor[T, N] = {
    val e = eOpt.get
    new Factor(variables,
      Factor.cases(e.map(_.distribution).toVector).map(kase => (kase, if (isSupersetOf(kase, e)) this(kase) else field.zero)).toMap)
  }

  def mentions(variable: Distribution[T, N]): Boolean =
    variables.exists(v => variable.name === v.name)

  def isSupersetOf(left: Seq[CaseIs[T, N]], right: Seq[CaseIs[T, N]]): Boolean = {
    val ll: Seq[(Distribution[T, N], T)] = left.map(ci => (ci.distribution, ci.v))
    val lm = ll.toMap
    right.forall((rightCaseIs: CaseIs[T, N]) => lm.contains(rightCaseIs.distribution) && (rightCaseIs.v === lm(rightCaseIs.distribution)))
  }

  def projectToVars(cs: Seq[CaseIs[T, N]], pVars: Set[Distribution[T, N]]): Seq[CaseIs[T, N]] =
    cs.filter(ci => pVars.contains(ci.distribution))

}
