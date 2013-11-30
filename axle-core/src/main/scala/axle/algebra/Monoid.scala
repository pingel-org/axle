package axle.algebra

import spire.algebra._
import spire.implicits._

object MonoidLaws {

  def checkLeftZero[A: Eq: Monoid](x: A): Boolean = {
    val m = implicitly[Monoid[A]]
    m.op(m.id, x) === x
  }

  def checkRightZero[A: Eq: Monoid](x: A): Boolean = {
    val m = implicitly[Monoid[A]]
    m.op(x, m.id) === x
  }

  def checkAssociativity[A: Monoid](x: A, y: A, z: A): Boolean = {
    val m = implicitly[Monoid[A]]
    m.op(m.op(x, y), z) == m.op(x, m.op(y, z))
  }

  // hyper log log, etc

}
