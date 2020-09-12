package axle.probability

import scala.annotation.implicitNotFound

import cats.kernel.Order

import spire.algebra.Ring
import spire.random.Dist
import spire.random.Generator

@implicitNotFound("Witness not found for Sampler[${M}]")
trait Sampler[M[_, _]] {

  def sample[A, V: Dist: Ring: Order](model: M[A, V])(gen: Generator): A

}

object Sampler {

  def apply[M[_, _]](implicit ev: Sampler[M]): Sampler[M] = ev
}