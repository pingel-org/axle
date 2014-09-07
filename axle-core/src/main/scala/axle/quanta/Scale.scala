package axle.quanta

import axle.algebra.Bijection
import spire.algebra.Field
import spire.implicits._

case class BijectiveIdentity[N]() extends Bijection[N, N] {
  def apply(n: N): N = n
  def unapply(n: N): N = n
}

case class Scale10s[N: Field](exp: Int) extends Bijection[N, N] {
  def apply(n: N): N = n * (10 ** exp)
  def unapply(n: N): N = n * (10 ** -exp)
}

case class Scale2s[N: Field](exp: Int) extends Bijection[N, N] {
  def apply(n: N): N = n * (1 << exp)
  def unapply(n: N): N = n / (1 << exp)
}

case class ScaleInt[N: Field](factor: Int) extends Bijection[N, N] {
  def apply(n: N): N = n * factor
  def unapply(n: N): N = n / factor
}

case class ScaleLong[N: Field](factor: Long) extends Bijection[N, N] {
  def apply(n: N): N = n * factor
  def unapply(n: N): N = n / factor
}

case class ScaleDouble[N: Field](factor: Double) extends Bijection[N, N] {
  def apply(n: N): N = n * factor
  def unapply(n: N): N = n / factor
}
