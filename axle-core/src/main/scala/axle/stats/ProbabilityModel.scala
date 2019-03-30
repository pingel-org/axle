package axle.stats

/**
 * ProbabilityModel
 *
 * See http://www.stat.yale.edu/Courses/1997-98/101/probint.htm
 */

import scala.annotation.implicitNotFound

import cats.kernel.Order

import spire.random.Generator
import spire.random.Dist
import spire.algebra.Ring
import spire.algebra.Field

@implicitNotFound("Witness not found for ProbabilityModel[${M}]")
trait ProbabilityModel[M[_, _]] {

  def construct[A, V](variable: Variable[A], as: Iterable[A], f: A => V)(implicit ring: Ring[V]): M[A, V]

  def values[A](model: M[A, _]): IndexedSeq[A]

  def combine[A, V](modelsToProbabilities: Map[M[A, V], V])(implicit fieldV: Field[V]): M[A, V]

  def empty[A, V](variable: Variable[A])(implicit ringV: Ring[V]): M[A, V]

  def probabilityOf[A, V](model: M[A, V], a: A)(implicit fieldV: Field[V]): V

  def probabilityOfExpression[A, V](model: M[A, V], predicate: A => Boolean)(implicit fieldV: Field[V]): V

  def conditionExpression[A, B, V](model: M[A, V], predicate: A => Boolean, screen: A => B)(implicit fieldV: Field[V]): M[B, V]

  def observe[A, V](model: M[A, V], gen: Generator)(implicit spireDist: Dist[V], ringV: Ring[V], orderV: Order[V]): A

}

object ProbabilityModel {

  def apply[M[_, _]](implicit aggFA: ProbabilityModel[M]): ProbabilityModel[M] =
    implicitly[ProbabilityModel[M]]

}