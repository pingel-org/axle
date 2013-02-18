package axle.algebra

object Monoid {

  import spire.algebra.Monoid

  def checkLeftZero[A: Monoid](x: A): Boolean = {
    val m = implicitly[Monoid[A]]
    m.op(∅[A], x) === x
  }

  def checkRightZero[A: Monoid](x: A): Boolean = {
    val m = implicitly[Monoid[A]]
    m.op(x, ∅[A]) === x
  }

  def checkAssociativity[A: Monoid](x: A, y: A, z: A): Boolean = {
    val m = implicitly[Monoid[A]]
    m.op(m.op(x, y), z) == m.op(x, m.op(y, z))
  }

  // hyper log log, etc

}
