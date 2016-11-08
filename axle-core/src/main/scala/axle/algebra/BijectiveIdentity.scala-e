package axle.algebra

case class BijectiveIdentity[N]() extends Bijection[N, N] {

  def apply(n: N): N = n

  def unapply(n: N): N = n

  override def bidirectionallyAndThen[C](other: Bijection[N, C]): Bijection[N, C] = other
}
