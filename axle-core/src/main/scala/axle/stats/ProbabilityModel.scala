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

import axle.algebra.Region

@implicitNotFound("Witness not found for ProbabilityModel[${M}]")
trait ProbabilityModel[M[_, _]] {

  def adjoin[A, V1, V2](model: M[A, V1])(other: M[A, V2])(implicit eqA: cats.kernel.Eq[A], fieldV1: Field[V1], fieldV2: Field[V2], eqV1: cats.kernel.Eq[V1], eqV2: cats.kernel.Eq[V2]): M[A, (V1, V2)]

  def chain[A, B, V](model: M[A, V])
    (other: M[B, V])
    (implicit fieldV: Field[V], eqA: cats.kernel.Eq[A], eqB: cats.kernel.Eq[B]): M[(A, B), V]

  def map[A, B, V](model: M[A, V])(f: A => B)(implicit eqB: cats.kernel.Eq[B]): M[B, V]

  def mapValues[A, V, V2](model: M[A, V])(f: V => V2)(implicit fieldV: Field[V], ringV2: Ring[V2]): M[A, V2]

  /**
   * 
   * redistribute
   * 
   * val d1 = redistribute(d0)(from, to, weight)
   * 
   * d1 has to still satisfy 3 axioms, plus:
   * 
   * d0.P(to) + mass = d1.P(to)
   * d0.P(from) - mass = d1.P(from)
   * 
   */

  def redistribute[A: cats.kernel.Eq, V: Ring](model: M[A, V])(from: A, to: A, mass: V): M[A, V]

  def flatMap[A, B, V](model: M[A, V])(f: A => M[B, V])(implicit eqB: cats.kernel.Eq[B]): M[B, V]

  def unit[A, V](a: A, variable: Variable[A])(implicit eqA: cats.kernel.Eq[A], ringV: Ring[V]): M[A, V]

  def probabilityOf[A, V](model: M[A, V])(predicate: Region[A])(implicit fieldV: Field[V]): V

  def filter[A, V](model: M[A, V])(predicate: Region[A])(implicit fieldV: Field[V]): M[A, V]

  def observe[A, V: Dist: Ring: Order](model: M[A, V])(gen: Generator): A

}

object ProbabilityModel {

  def apply[M[_, _]](implicit aggFA: ProbabilityModel[M]): ProbabilityModel[M] =
    implicitly[ProbabilityModel[M]]

}