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

//import axle.algebra.Region

@implicitNotFound("Witness not found for ProbabilityModel[${M}]")
trait ProbabilityModel[M[_, _]] {

  // def construct[A, V](variable: Variable[A], universe: Region[A], f: Region[A] => V)(implicit ring: Ring[V], eqA: cats.kernel.Eq[A]): M[A, V]

  // def regions[A](model: M[A, _]): Iterable[A => Boolean]

  def adjoin[A, V1, V2](model: M[A, V1])(other: M[A, V2])(implicit eqA: cats.kernel.Eq[A], fieldV1: Field[V1], fieldV2: Field[V2], eqV1: cats.kernel.Eq[V1], eqV2: cats.kernel.Eq[V2]): M[A, (V1, V2)]

  def chain[A, B, V](model: M[A, V])
    (other: M[B, V])
    (implicit fieldV: Field[V], eqA: cats.kernel.Eq[A], eqB: cats.kernel.Eq[B]): M[(A, B), V]

  def mapValues[A, V, V2](model: M[A, V])(f: V => V2)(implicit fieldV: Field[V], ringV2: Ring[V2]): M[A, V2]

  def empty[A, V](variable: Variable[A])(implicit eqA: cats.kernel.Eq[A], ringV: Ring[V]): M[A, V]

//  def probabilityOf[A, V](model: M[A, V])(a: A)(implicit fieldV: Field[V]): V

  def probabilityOfExpression[A, V](model: M[A, V])(predicate: A => Boolean)(implicit fieldV: Field[V]): V

  def filter[A, V](model: M[A, V])(predicate: A => Boolean)(implicit fieldV: Field[V]): M[A, V]

//  def observe[A, V](model: M[A, V])(gen: Generator)(implicit spireDist: Dist[V], ringV: Ring[V], orderV: Order[V]): A

  def observe[A, V: Dist: Ring: Order](model: M[A, V])(gen: Generator): A

}

object ProbabilityModel {

  def apply[M[_, _]](implicit aggFA: ProbabilityModel[M]): ProbabilityModel[M] =
    implicitly[ProbabilityModel[M]]

}