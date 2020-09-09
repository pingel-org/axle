package axle.probability

import scala.annotation.implicitNotFound

import cats.kernel.Order

import spire.algebra.Ring
import spire.random.Dist
import spire.random.Generator

@implicitNotFound("Witness not found for Perceivable[${M}]")
trait Perceivable[M[_, _]] {

  def perceive[A, V: Dist: Ring: Order](model: M[A, V])(gen: Generator): A

}

object Perceivable {

  def apply[M[_, _]](implicit ev: Perceivable[M]): Perceivable[M] = ev
}