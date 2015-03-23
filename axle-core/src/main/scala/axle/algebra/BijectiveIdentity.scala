package axle.algebra

case class BijectiveIdentity[N]() extends Bijection[N, N] {

  def apply(n: N): N = n

  def unapply(n: N): N = n
}
