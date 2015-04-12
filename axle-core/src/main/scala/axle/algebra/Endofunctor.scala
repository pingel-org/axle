package axle.algebra

import scala.annotation.implicitNotFound

@implicitNotFound("No member of typeclass Endofunctor found for types ${E}, ${T}")
trait Endofunctor[E, T] {

  def map(e: E)(f: T => T): E
}
