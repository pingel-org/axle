package axle.algebra

import spire.algebra.Field
import spire.algebra.Module
import spire.implicits.literalDoubleOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps

// TODO this needs less than a Field
case class Scale[N, M: Field](factor: M)(implicit module: Module[N, M]) extends Bijection[N, N] {

  val inverseFactor: M = Field[M].multiplicative.inverse(factor)

  def apply(n: N): N = module.timesr(n, factor)

  def unapply(n: N): N = module.timesr(n, inverseFactor)
}

case class Scale10s[N: Field](exp: Int) extends Bijection[N, N] {

  require(exp > 0)

  def apply(n: N): N = n * (10d ** exp.toDouble)

  def unapply(n: N): N = n * (10d ** -exp.toDouble)
}

case class Scale2s[N: Field](exp: Int) extends Bijection[N, N] {

  require(exp > 0)

  def apply(n: N): N = n * (1 << exp)

  def unapply(n: N): N = n / (1 << exp)
}
