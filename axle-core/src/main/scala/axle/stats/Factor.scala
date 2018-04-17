package axle.stats

import cats.Show
import cats.implicits.catsKernelStdOrderForString
import cats.implicits.catsSyntaxEq
import cats.implicits._
import cats.kernel.Eq
import cats.kernel.Order
import cats.Order.catsKernelOrderingForOrder

import spire.algebra.Field
import spire.algebra.MultiplicativeMonoid
import spire.implicits.RingProduct2
import spire.implicits.convertableOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.math.ConvertableFrom

import axle.IndexedCrossProduct
import axle.algebra.LinearAlgebra

/* Technically a "Distribution" is probably a table that sums to 1, which is not
 * always true in a Factor.  They should be siblings rather than parent/child.
 */

object Factor {

  implicit def showFactor[T: Show, N: Show]: Show[Factor[T, N]] =
    factor => {
      import factor._
      variables.map(d => d.name.padTo(d.charWidth, " ").mkString("")).mkString(" ") + "\n" +
        factor.cases.map(kase =>
          kase.map(ci => ci.value.show.padTo(ci.variable.charWidth, " ").mkString("")).mkString(" ") +
            " " + factor(kase).show).mkString("\n") // Note: was "%f".format() prior to spire.math
    }

  implicit def factorEq[T: Eq, N]: Eq[Factor[T, N]] =
    (x, y) => x equals y // TODO

  implicit def factorMultMonoid[T: Eq, N: Field: ConvertableFrom: Order]: MultiplicativeMonoid[Factor[T, N]] =
    new MultiplicativeMonoid[Factor[T, N]] {

      val field = Field[N]

      def times(x: Factor[T, N], y: Factor[T, N]): Factor[T, N] = {
        val newVars = (x.variables.toSet union y.variables.toSet).toVector
        val newVariablesWithValues = newVars.map(variable => (variable, x.valuesOfVariable(variable)))
        Factor(
          newVariablesWithValues,
          Factor.cases(
            newVars.map({ variable =>
              (variable, x.valuesOfVariable(variable)) // TODO assert the x is same as y in this regard
            })).map(kase => (kase, x(kase) * y(kase))).toMap)
      }
      def one: Factor[T, N] = Factor(Vector.empty, Map.empty.withDefaultValue(field.one))
    }

  def cases[T: Eq, N: Field](varSeq: Vector[(Variable[T], IndexedSeq[T])]): Iterable[Vector[CaseIs[T]]] =
    IndexedCrossProduct(varSeq.map(_._2)) map { kase =>
      varSeq.map(_._1).zip(kase) map {
        case (variable, value) => CaseIs(value, variable)
      } toVector
    }

}

case class Factor[T: Eq, N: Field: Order: ConvertableFrom](
  variablesWithValues: Vector[(Variable[T], Vector[T])],
  probabilities:       Map[Vector[CaseIs[T]], N]) {

  val variables = variablesWithValues.map(_._1)

  val valuesOfVariable: Map[Variable[T], Vector[T]] = variablesWithValues.toMap

  val field = Field[N]

  lazy val crossProduct = IndexedCrossProduct(variables.map(valuesOfVariable))

  lazy val elements: IndexedSeq[N] =
    (0 until crossProduct.size) map { i =>
      probabilities.get(caseOf(i)).getOrElse(field.zero)
    } toIndexedSeq

  // assume prior and condition are disjoint, and that they are
  // each compatible with this table

  def evaluate(prior: Seq[CaseIs[T]], condition: Seq[CaseIs[T]]): N = {
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

  def indexOf(cs: Seq[CaseIs[T]]): Int = {
    val rvvs: Seq[(Variable[T], T)] = cs.map(ci => (ci.variable, ci.value))
    val rvvm = rvvs.toMap
    crossProduct.indexOf(variables.map(rvvm))
  }

  private[this] def caseOf(i: Int): Vector[CaseIs[T]] =
    variables.zip(crossProduct(i)) map { case (variable, value) => CaseIs(value, variable) }

  def cases: Iterable[Seq[CaseIs[T]]] = (0 until elements.length) map { caseOf }

  def apply(c: Seq[CaseIs[T]]): N = elements(indexOf(c))

  // Chapter 6 definition 6
  def maxOut(variable: Variable[T]): Factor[T, N] = {
    val newVars = variables.filterNot(variable === _)
    Factor(
      variablesWithValues.filter({ case (variable, _) => newVars.contains(variable) }),
      Factor.cases(newVars.map({ variable => (variable, valuesOfVariable(variable)) }))
        .map(kase => (kase, valuesOfVariable(variable).map(value => this(kase)).max))
        .toMap)
  }

  def projectToOnly(remainingVars: Vector[Variable[T]]): Factor[T, N] =
    Factor(
      remainingVars.map(variable => (variable, valuesOfVariable(variable))),
      Factor.cases[T, N](remainingVars.map({ variable => (variable, valuesOfVariable(variable)) })).toVector
        .map(kase => (projectToVars(kase, remainingVars.toSet), this(kase)))
        .groupBy(_._1)
        .map({ case (k, v) => (k.toVector, spire.optional.unicode.Σ(v.map(_._2))) })
        .toMap)

  def tally[M](
    a: Variable[T],
    b: Variable[T])(
    implicit
    la: LinearAlgebra[M, Int, Int, Double]): M =
    la.matrix(
      valuesOfVariable(a).size,
      valuesOfVariable(b).size,
      (r: Int, c: Int) => spire.optional.unicode.Σ(
        cases.filter(isSupersetOf(_, Vector(a is valuesOfVariable(a).apply(r), b is valuesOfVariable(b).apply(c)))).map(this(_)).toVector).toDouble)

  def Σ(varToSumOut: Variable[T]): Factor[T, N] = this.sumOut(varToSumOut)

  // depending on assumptions, this may not be the best way to remove the vars
  def sumOut(gone: Variable[T]): Factor[T, N] = {
    val position = variables.indexOf(gone)
    val newVars = variables.filter(v => !(v === gone))
    val newVariablesWithValues = variablesWithValues.filter({ case (variable, _) => newVars.contains(variable) })
    val newKases = Factor.cases(newVariablesWithValues).map(kase => {
      val reals = valuesOfVariable(gone).map(gv => {
        val ciGone = List(CaseIs(gv, gone))
        this(kase.slice(0, position) ++ ciGone ++ kase.slice(position, kase.length))
      })
      (kase, spire.optional.unicode.Σ(reals))
    }).toMap
    Factor(newVariablesWithValues, newKases)
  }

  def Σ(varsToSumOut: Set[Variable[T]]): Factor[T, N] = sumOut(varsToSumOut)

  def sumOut(varsToSumOut: Set[Variable[T]]): Factor[T, N] =
    varsToSumOut.foldLeft(this)((result, v) => result.sumOut(v))

  // as defined on chapter 6 page 15
  def projectRowsConsistentWith(eOpt: Option[List[CaseIs[T]]]): Factor[T, N] = {
    val e = eOpt.get // TODO either don't take Option or return an Option
    val newVars = e.map(_.variable)
    val newVariablesWithValues = variablesWithValues.filter({ case (variable, _) => newVars.contains(variable) })
    Factor(
      newVariablesWithValues,
      Factor.cases(
        e.map(_.variable).toVector
          .map({ variable => (variable, valuesOfVariable(variable)) }))
        .map(kase => (kase, if (isSupersetOf(kase, e)) this(kase) else field.zero)).toMap)
  }

  def mentions(variable: Variable[T]): Boolean =
    variables.exists(v => variable.name === v.name)

  def isSupersetOf(left: Seq[CaseIs[T]], right: Seq[CaseIs[T]]): Boolean = {
    val ll: Seq[(Variable[T], T)] = left.map(ci => (ci.variable, ci.value))
    val lm = ll.toMap
    right.forall((rightCaseIs: CaseIs[T]) => lm.contains(rightCaseIs.variable) && (rightCaseIs.value === lm(rightCaseIs.variable)))
  }

  def projectToVars(cs: Seq[CaseIs[T]], pVars: Set[Variable[T]]): Seq[CaseIs[T]] =
    cs.filter(ci => pVars.contains(ci.variable))

}
