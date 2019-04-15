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

  def sum[A, V1, V2](model: M[A, V1])(other: M[A, V2])(implicit fieldV1: Field[V1], fieldV2: Field[V2], eqV1: cats.kernel.Eq[V1], eqV2: cats.kernel.Eq[V2]): M[A, (V1, V2)]

  def product[A, B, V](model: M[A, V])(other: M[B, V])(implicit fieldV: Field[V]): M[(A, B), V]

  def mapValues[A, V, V2](model: M[A, V])(f: V => V2)(implicit fieldV: Field[V], ringV2: Ring[V2]): M[A, V2]

  def empty[A, V](variable: Variable[A])(implicit ringV: Ring[V]): M[A, V]

  def probabilityOf[A, V](model: M[A, V])(a: A)(implicit fieldV: Field[V]): V

  def probabilityOfExpression[A, V](model: M[A, V])(predicate: A => Boolean)(implicit fieldV: Field[V]): V

  def filter[A, V](model: M[A, V])(predicate: A => Boolean)(implicit fieldV: Field[V]): M[A, V]

  def observe[A, V](model: M[A, V])(gen: Generator)(implicit spireDist: Dist[V], ringV: Ring[V], orderV: Order[V]): A

}

object ProbabilityModel {

  def apply[M[_, _]](implicit aggFA: ProbabilityModel[M]): ProbabilityModel[M] =
    implicitly[ProbabilityModel[M]]

}