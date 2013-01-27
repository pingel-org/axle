package axle.algebra

trait Semigroup[A] {
  def mappend(a: A, b: A): A
}
