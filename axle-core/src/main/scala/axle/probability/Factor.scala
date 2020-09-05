package axle.probability

import cats.Show
//import cats.implicits.catsKernelStdOrderForString
import cats.implicits.catsSyntaxEq
//import cats.implicits._
import cats.kernel.Eq
import cats.kernel.Order
import cats.Order.catsKernelOrderingForOrder

import spire.algebra.Field
import spire.algebra.MultiplicativeMonoid
//import spire.implicits.convertableOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.math.ConvertableFrom

import axle.IndexedCrossProduct
import axle.algebra.LinearAlgebra
import axle.algebra.RegionEq

object Factor {

  implicit def showFactor[T: Show, N: Show]: Show[Factor[T, N]] =
    factor => {
      import cats.implicits._
      import factor._
      variables.map(d => d.name.padTo(d.charWidth, " ").mkString("")).mkString(" ") + "\n" +
        factor.cases.map { regions =>
          variables.zip(regions).map { case (variable, region) =>
            region.x.show.padTo(variable.charWidth, " ").mkString("")
          }.mkString(" ") +
          " " + factor(regions).show
        }.mkString("\n") // Note: was "%f".format() prior to spire.math
    }

  implicit def factorEq[T: Eq, N] = Eq.fromUniversalEquals[Factor[T, N]] // TODO go deeper

  implicit def factorMultMonoid[T: Eq, N: Field: ConvertableFrom: Order]: MultiplicativeMonoid[Factor[T, N]] =
    new MultiplicativeMonoid[Factor[T, N]] {

      val field = Field[N]

      def times(x: Factor[T, N], y: Factor[T, N]): Factor[T, N] = {

        val newVars: Vector[Variable[T]] = (x.variables.toSet union y.variables.toSet).toVector

        val newVariablesWithValues: Vector[(Variable[T], Vector[T])] =
          newVars.map(variable =>
            (variable, x.valuesOfVariable.get(variable).getOrElse(y.valuesOfVariable(variable)))
          )

        val kases: Iterable[Vector[RegionEq[T]]] =
          Factor.cases(newVariablesWithValues)

        val newProbabilities: Map[Vector[RegionEq[T]], N] = kases.map(kase => {
          val xFilteredKase = kase.zip(newVars).filter({ case (_, v) => x.variables.contains(v)}).map(_._1)
          val yFilteredKase = kase.zip(newVars).filter({ case (_, v) => y.variables.contains(v)}).map(_._1)
          kase -> (x(xFilteredKase) * y(yFilteredKase))
        }).toMap

        Factor(newVariablesWithValues, newProbabilities)
      }
    
      def one: Factor[T, N] = Factor(Vector.empty, Map.empty.withDefaultValue(field.one))
    }

  def cases[T: Eq](varSeq: Vector[(Variable[T], IndexedSeq[T])]): Iterable[Vector[RegionEq[T]]] =
    IndexedCrossProduct(varSeq.map(_._2)) map { kase =>
      varSeq.map(_._1).zip(kase) map {
        case (variable, value) => RegionEq(value)
      } toVector
    }

}

case class Factor[T: Eq, N: Field: Order: ConvertableFrom](
  variablesWithValues: Vector[(Variable[T], Vector[T])],
  probabilities:       Map[Vector[RegionEq[T]], N]) {

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

  def evaluate(prior: Seq[(Variable[T], RegionEq[T])], condition: Seq[(Variable[T], RegionEq[T])]): N = {
    import spire.implicits.RingProduct2
    val pw = spire.optional.unicode.Σ(cases.map( c => {
      val vc = variables.zip(c)
      if (isSupersetOf(vc, prior)) {
        if (isSupersetOf(vc, condition)) {
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

  def indexOf(regions: Seq[RegionEq[T]]): Int =
    crossProduct.indexOf(regions.map(_.x))

  private[this] def caseOf(i: Int): Vector[RegionEq[T]] =
    variables.zip(crossProduct(i)) map { case (variable, value) => RegionEq(value) }

  def cases: Iterable[Seq[RegionEq[T]]] = (0 until elements.length) map { caseOf }

  def apply(c: Seq[RegionEq[T]]): N = elements(indexOf(c))

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
      Factor.cases[T](remainingVars.map({ variable => (variable, valuesOfVariable(variable)) })).toVector
        .map(kase => (projectToVars(kase, remainingVars.toSet), this(kase)))
        .groupBy(_._1)
        .map({ case (k, vs) => (k.toVector.map(_._2), spire.optional.unicode.Σ(vs.map(_._2))) })
        .toMap)

  def tally[M](
    a: Variable[T],
    b: Variable[T])(
    implicit
    la: LinearAlgebra[M, Int, Int, Double],
    cf: ConvertableFrom[N]): M = {
    val foo = la.matrix(
      valuesOfVariable(a).size,
      valuesOfVariable(b).size,
      {(r: Int, c: Int) => {
        val sumN = spire.optional.unicode.Σ({
          cases.filter(regions => {
            isSupersetOf(
              variables.zip(regions),
              Vector(
                (a, RegionEq(valuesOfVariable(a).apply(r))),
                (b, RegionEq(valuesOfVariable(b).apply(c))))
              )
            }).map(this(_)).toIterable
          })
        cf.toDouble(sumN)
      }}
    )
    foo
  }

  def Σ(varToSumOut: Variable[T]): Factor[T, N] = this.sumOut(varToSumOut)

  // depending on assumptions, this may not be the best way to remove the vars
  def sumOut(gone: Variable[T]): Factor[T, N] = {
    val position = variables.indexOf(gone)
    val newVars = variables.filter(v => !(v === gone))
    val newVariablesWithValues = variablesWithValues.filter({ case (variable, _) => newVars.contains(variable) })
    val newKases = Factor.cases(newVariablesWithValues).map(kase => {
      val reals = valuesOfVariable(gone).map(gv => {
        val ciGone = List(RegionEq(gv))
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
  def projectRowsConsistentWith(eOpt: Option[List[(Variable[T], RegionEq[T])]]): Factor[T, N] = {
    val e = eOpt.get // TODO either don't take Option or return an Option
    val newVars = e.map(_._1)
    val newVariablesWithValues = variablesWithValues.filter({ case (variable, _) => newVars.contains(variable) })
    Factor(
      newVariablesWithValues,
      Factor.cases(
        e.map(_._1).toVector.map({ variable => (variable, valuesOfVariable(variable)) })
      )
      .map { regions =>
        (regions, if (isSupersetOf(e.map(_._1).zip(regions), e)) this(regions) else field.zero)
      }.toMap)
  }

  def isSupersetOf(
    left: Seq[(Variable[T], RegionEq[T])],
    right: Seq[(Variable[T], RegionEq[T])]): Boolean = {
    val ll: Seq[(Variable[T], T)] = left.map(ci => (ci._1, ci._2.x))
    val lm = ll.toMap
    right.forall((rightCaseIs: (Variable[T], RegionEq[T])) =>
      lm.contains(rightCaseIs._1) && (rightCaseIs._2.x === lm(rightCaseIs._1))
    )
  }

  def projectToVars(cs: Seq[RegionEq[T]], pVars: Set[Variable[T]]): Seq[(Variable[T], RegionEq[T])] =
    variables.zip(cs).filter { case (variable, region) =>
      pVars.contains(variable)
    }

}
