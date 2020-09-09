package axle.probability

import scala.annotation.implicitNotFound

import spire.algebra.Field
import axle.algebra.Region

@implicitNotFound("Witness not found for Kolmogorov[${M}]")
trait Kolmogorov[M[_, _]] {

  def probabilityOf[A, V: Field](model: M[A, V])(predicate: Region[A]): V

}

object Kolmogorov {

  def apply[M[_, _]](implicit ev: Kolmogorov[M]): Kolmogorov[M] = ev
}