package axle.algebra

// not necessarily a bijection, but could be

trait FunctionPair[A, B] {
  def apply(a: A): B
  def unapply(b: B): A
}
