package axle.algebra

trait Monoid[A] extends Zero[A] with Semigroup[A]

object Monoid {

  implicit def monoid[M](implicit s: Semigroup[M], z: Zero[M]): Monoid[M] = new Monoid[M] {

    def mappend(a: M, b: M) = s.mappend(a, b)

    def zero = z.zero
  }

  def checkLeftZero[A: Monoid](x: A): Boolean =
    (∅[A] |+| x) === x

  def checkRightZero[A: Monoid](x: A): Boolean =
    (x |+| ∅[A]) === x

  def checkAssociativity[A: Monoid](x: A, y: A, z: A): Boolean =
    ((x |+| y) |+| z) == (x |+| (y |+| z))

  // hyper log log, etc

}
