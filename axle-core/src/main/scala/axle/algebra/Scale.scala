package axle.algebra

import spire.algebra.Field
import spire.algebra.Module
import spire.math.ConvertableTo
import spire.math.Rational
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps

// TODO this needs less than a Field
case class Scale[N](factor: Rational)(implicit module: Module[N, Rational]) extends Bijection[N, N] {

  val inverseFactor: Rational = Field[Rational].multiplicative.inverse(factor)

  def apply(n: N): N = module.timesr(n, factor)

  def unapply(n: N): N = module.timesr(n, inverseFactor)
}

case class Scale10s[N](exp: Int)(
  implicit
  fieldN:         Field[N],
  convertableToN: ConvertableTo[N]) extends Bijection[N, N] {

  require(exp > 0)

  val up = fieldN.multiplicative.combineN(convertableToN.fromInt(10), exp)

  def apply(n: N): N = n * up

  val down = fieldN.multiplicative.combineN(fieldN.one / convertableToN.fromInt(10), exp)

  def unapply(n: N): N = n * down
}

case class Scale2s[N](exp: Int)(
  implicit
  fieldN:         Field[N],
  convertableToN: ConvertableTo[N]) extends Bijection[N, N] {

  require(exp > 0)

  val up = fieldN.multiplicative.combineN(convertableToN.fromInt(2), exp)

  def apply(n: N): N = n * up

  val down = fieldN.multiplicative.combineN(fieldN.one / convertableToN.fromInt(2), exp)

  def unapply(n: N): N = n * down
}
