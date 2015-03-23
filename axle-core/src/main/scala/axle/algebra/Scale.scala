package axle.algebra

import spire.algebra.Field
import spire.algebra.Group
import spire.implicits.literalDoubleOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.additiveGroupOps
import spire.implicits.additiveMonoidOps

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
