package axle.stats

import cats.kernel.Eq
import axle.algebra.RegionEq

case class Variable[T](name: String) {

  def is(t: T)(implicit eqT: Eq[T]): (Variable[T], RegionEq[T]) = (this, RegionEq(t))

  def charWidth: Int = name.length
}

object Variable {

  import cats.kernel.Eq
  import cats.implicits._

  implicit def eqVariable[T]: Eq[Variable[T]] =
    (x, y) => x.name === y.name
}