package axle.algebra

import spire.algebra._

trait Identity[A] {

  val value: A

  def |+|(a2: A)(implicit s: Semigroup[A]) = s.op(value, a2)

  def ===(other: A): Boolean = (value equals other)

}
