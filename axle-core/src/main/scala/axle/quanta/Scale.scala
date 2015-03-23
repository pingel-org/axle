package axle.quanta

import axle.algebra.Bijection
import spire.algebra.Field
import spire.algebra.Group
import spire.implicits.literalDoubleOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.additiveGroupOps
import spire.implicits.additiveMonoidOps

// TODO move this to axle.algebra

case class BijectiveIdentity[N]() extends Bijection[N, N] {
  def apply(n: N): N = n
  def unapply(n: N): N = n
}

case class Transform[N](t: N)(implicit group: Group[N]) extends Bijection[N, N] {
  def apply(n: N): N = group.op(t, n)
  def unapply(n: N): N = group.opInverse(t, n)
}

case class Scale10s[N: Field](exp: Int) extends Bijection[N, N] {

  require(exp > 0)

  def apply(n: N): N = n * (10d ** exp)
  def unapply(n: N): N = n * (10d ** -exp)
}

case class Scale2s[N: Field](exp: Int) extends Bijection[N, N] {

  require(exp > 0)

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
