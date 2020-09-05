package axle.probability

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

  def probabilityOf[A, V](model: M[A, V])(predicate: Region[A])(implicit fieldV: Field[V]): V

  def filter[A, V](model: M[A, V])(predicate: Region[A])(implicit fieldV: Field[V]): M[A, V]

  def observe[A, V: Dist: Ring: Order](model: M[A, V])(gen: Generator): A

  def unit[A, V](a: A)(implicit eqA: cats.kernel.Eq[A], ringV: Ring[V]): M[A, V]

  def map[A, B, V](model: M[A, V])(f: A => B)(implicit eqB: cats.kernel.Eq[B]): M[B, V]

  def flatMap[A, B, V](model: M[A, V])(f: A => M[B, V])(implicit eqB: cats.kernel.Eq[B]): M[B, V]

}

object ProbabilityModel {

  def apply[M[_, _]](implicit aggFA: ProbabilityModel[M]): ProbabilityModel[M] =
    implicitly[ProbabilityModel[M]]

}