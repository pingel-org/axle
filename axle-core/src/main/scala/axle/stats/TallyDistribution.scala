package axle.stats

import cats.Show
import cats.kernel.Eq
import cats.kernel.Order
import cats.implicits._

import spire.algebra.Field
import spire.algebra.Ring

import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.random.Dist
import spire.random.Generator

import axle.math.Σ
import axle.dummy

object TallyDistribution0 {

  implicit def show[A: Order: Show, V: Show: Field]: Show[TallyDistribution0[A, V]] = td =>
    td.values.sorted.map(a => {
      val aString = Show[A].show(a)
      // (aString + (1 to (td.charWidth - aString.length)).map(i => " ").mkString("") + " " + string(td.probabilityOf(a)))
      (aString + " " + Show[V].show(probabilityWitness.probabilityOf(td, a)))
    }).mkString("\n")


    implicit val probabilityWitness: ProbabilityModel[TallyDistribution0] =
    new ProbabilityModel[TallyDistribution0] {

      def construct[A, V](variable: Variable[A], as: Iterable[A], f: A => V)(implicit ring: Ring[V]): TallyDistribution0[A, V] =
        TallyDistribution0[A, V](as.map(a => a -> f(a)).toMap, variable)

      def values[A](model: TallyDistribution0[A, _]): IndexedSeq[A] =
        model.values

      def combine[A, V](modelsToProbabilities: Map[TallyDistribution0[A, V], V])(implicit fieldV: Field[V]): TallyDistribution0[A, V] = {

        // TODO assert that all models are oriented for same Variable[A]

        val parts: IndexedSeq[(A, V)] =
          modelsToProbabilities.toVector flatMap {
            case (model, weight) =>
              values(model).map(v => (v, model.tally.get(v).getOrElse(fieldV.zero) * weight))
          }

        val newDist: Map[A, V] =
          parts.groupBy(_._1).mapValues(xs => xs.map(_._2).reduce(fieldV.plus)).toMap

        val v = modelsToProbabilities.headOption.map({ case (m, _) => orientation(m) }).getOrElse(Variable("?"))

        TallyDistribution0[A, V](newDist, v)
      }

      def condition[A, V, G](model: TallyDistribution0[A, V], given: CaseIs[G]): TallyDistribution0[A, V] =
        model // TODO true unless G =:= A and model.variable === variable

      def empty[A, V](variable: Variable[A])(implicit ringV: Ring[V]): TallyDistribution0[A, V] =
        TallyDistribution0(Map.empty, variable)

      def orientation[A, V](model: TallyDistribution0[A, V]): Variable[A] =
        model.variable

      def orient[A, B, V](model: TallyDistribution0[A, V], newVariable: Variable[B])(implicit ringV: Ring[V]): TallyDistribution0[B, V] =
        empty(newVariable) // TODO could check if variable == newVariable

      def observe[A, V](model: TallyDistribution0[A, V], gen: Generator)(implicit spireDist: Dist[V], ringV: Ring[V], orderV: Order[V]): A = {
        val r: V = model.totalCount * gen.next[V]
        model.bars.find({ case (_, v) => orderV.gteqv(v, r) }).get._1 // or distribution is malformed
      }

      def probabilityOf[A, V](model: TallyDistribution0[A, V], a: A)(implicit fieldV: Field[V]): V =
        model.tally.get(a).getOrElse(fieldV.zero) / model.totalCount

    }

}

case class TallyDistribution0[A, V](
  tally:    Map[A, V],
  variable: Variable[A])(implicit ring: Ring[V]) {

  val values: IndexedSeq[A] = tally.keys.toVector

  val totalCount: V = Σ[V, Iterable](tally.values)

  val bars: Map[A, V] =
    tally.scanLeft((dummy[A], ring.zero))((x, y) => (y._1, ring.plus(x._2, y._2))).drop(1)

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

  val totalCount: N = Σ[N, Iterable](tally.values)
}
