package axle.stats

import cats.Show
//import cats.implicits.catsSyntaxEq
import cats.kernel.Eq
import cats.kernel.Order
import cats.Order.catsKernelOrderingForOrder

import spire.algebra.AdditiveMonoid
import spire.algebra.Field
import spire.algebra.Ring
//import spire.implicits.literalIntAdditiveGroupOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.random.Dist
import spire.random.Generator

import axle.math.Σ
import axle.string
import axle.dummy

object TallyDistribution0 {

  implicit def show[A: Order: Show, N: Show](
    implicit
    prob: ProbabilityModel[({ type λ[T] = TallyDistribution0[T, N] })#λ, N]): Show[TallyDistribution0[A, N]] =
    new Show[TallyDistribution0[A, N]] {

      def show(td: TallyDistribution0[A, N]): String =
        td.values.sorted.map(a => {
          val aString = string(a)
          // (aString + (1 to (td.charWidth - aString.length)).map(i => " ").mkString("") + " " + string(td.probabilityOf(a)))
          (aString + " " + string(prob.probabilityOf(td, a)))
        }).mkString("\n")
    }

  implicit def probability[N](
    implicit
    fieldN: Field[N],
    orderN: Order[N]): ProbabilityModel[({ type λ[T] = TallyDistribution0[T, N] })#λ, N] =
    new ProbabilityModel[({ type λ[T] = TallyDistribution0[T, N] })#λ, N] {

      def construct[A](variable: Variable[A], as: Iterable[A], f: A => N): TallyDistribution0[A, N] =
        TallyDistribution0(as.map(a => a -> f(a)).toMap, variable)

      def values[A](model: TallyDistribution0[A, N]): IndexedSeq[A] =
        model.values

      def combine[A](modelsToProbabilities: Map[TallyDistribution0[A, N], N]): TallyDistribution0[A, N] = {

        // TODO assert that all models are oriented for same Variable[A]

        val parts: IndexedSeq[(A, N)] =
          modelsToProbabilities.toVector flatMap {
            case (model, weight) =>
              values(model).map(v => (v, model.tally.get(v).getOrElse(model.ring.zero) * weight))
          }

        val newDist: Map[A, N] =
          parts.groupBy(_._1).mapValues(xs => xs.map(_._2).reduce(fieldN.plus)).toMap

        val v = modelsToProbabilities.headOption.map({ case (m, _) => orientation(m) }).getOrElse(Variable("?"))

        TallyDistribution0[A, N](newDist, v)
      }

      def condition[A, G](model: TallyDistribution0[A, N], given: CaseIs[G]): TallyDistribution0[A, N] =
        model // TODO true unless G =:= A and model.variable === variable

      def empty[A](variable: Variable[A]): TallyDistribution0[A, N] =
        TallyDistribution0(Map.empty, variable)

      def orientation[A](model: TallyDistribution0[A, N]): Variable[A] =
        model.variable

      def orient[A, B](model: TallyDistribution0[A, N], newVariable: Variable[B]): TallyDistribution0[B, N] =
        empty[B](newVariable) // TODO could check if variable == newVariable

      def observe[A](model: TallyDistribution0[A, N], gen: Generator)(implicit spireDist: Dist[N]): A = {
        val r: N = model.totalCount * gen.next[N]
        model.bars.find({ case (_, v) => model.order.gteqv(v, r) }).get._1 // or distribution is malformed
      }

      def probabilityOf[A](model: TallyDistribution0[A, N], a: A): N =
        model.tally.get(a).getOrElse(model.ring.zero) / model.totalCount

    }

}

case class TallyDistribution0[A, N: Field: Order](
  tally:    Map[A, N],
  variable: Variable[A]) {

  val ring = Ring[N]
  val addition = implicitly[AdditiveMonoid[N]]
  val order = Order[N]

  val values: IndexedSeq[A] = tally.keys.toVector

  val totalCount: N = Σ[N, Iterable[N]](tally.values)

  val bars: Map[A, N] =
    tally.scanLeft((dummy[A], ring.zero))((x, y) => (y._1, addition.plus(x._2, y._2))).drop(1)

}

//object TallyDistribution1 {
//
//  def probabilityOf(a: A): N =
//    Σ[N, Iterable[N]](gvs.map(gv => tally((a, gv)))) / totalCount
//
//  def probabilityOf(a: A, given: Case[G]): N = given match {
//    case CaseIs(argGrv, gv)   => tally.get((a, gv)).getOrElse(Field[N].zero) / Σ[N, Iterable[N]](tally.filter(_._1._2 === gv).map(_._2))
//    case CaseIsnt(argGrv, gv) => 1 - (tally.get((a, gv)).getOrElse(Field[N].zero) / Σ[N, Iterable[N]](tally.filter(_._1._2 === gv).map(_._2)))
//    case _                    => throw new Exception("unhandled case in TallyDistributionWithInput.probabilityOf")
//  }
//}

case class TallyDistribution1[A, G: Eq, N: Field: Order](
  tally:    Map[(A, G), N],
  variable: Variable[A]) {

  lazy val _values: IndexedSeq[A] =
    tally.keys.map(_._1).toSet.toVector

  def values: IndexedSeq[A] = _values

  val gvs = tally.keys.map(_._2).toSet

  val totalCount: N = Σ[N, Iterable[N]](tally.values)
}
