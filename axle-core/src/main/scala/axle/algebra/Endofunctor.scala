package axle.algebra

import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for Endofunctor[${E}, ${T}]")
trait Endofunctor[E, T] {

  def map(e: E)(f: T => T): E
}

object Endofunctor {

  final def apply[E, T](implicit ev: Endofunctor[E, T]): Endofunctor[E, T] = ev

}
