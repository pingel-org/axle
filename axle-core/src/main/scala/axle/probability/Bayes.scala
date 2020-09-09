package axle.probability

import scala.annotation.implicitNotFound

import spire.algebra.Field
import axle.algebra.Region

@implicitNotFound("Witness not found for Bayes[${M}]")
trait Bayes[M[_, _]] {

  def filter[A, V: Field](model: M[A, V])(predicate: Region[A]): M[A, V]

}

object Bayes {

  def apply[M[_, _]](implicit ev: Bayes[M]): Bayes[M] = ev
}