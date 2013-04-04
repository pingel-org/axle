package axle.algebra

// not necessarily a bijection, but could be

trait FunctionPair[A, B] {
  val forward: A => B
  val backward: B => A
}
