package axle.algebra

import spire.algebra.Field
import spire.algebra.Module
import spire.math.abs
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

class ScaleExp[N](base: Int, exp: Int)(
  implicit
  fieldN:         Field[N],
  convertableToN: ConvertableTo[N]) extends Bijection[N, N] {

  val growFactor = fieldN.multiplicative.combineN(convertableToN.fromInt(base), abs(exp))
  val shrinkFactor = fieldN.multiplicative.combineN(fieldN.one / convertableToN.fromInt(base), abs(exp))

  val applyFactor = if (exp < 0) { shrinkFactor } else { growFactor }
  val unapplyFactor = if (exp < 0) { growFactor } else { shrinkFactor }

  def apply(n: N): N = n * applyFactor

  def unapply(n: N): N = n * unapplyFactor
}

case class Scale10s[N](exp: Int)(
  implicit
  fieldN:         Field[N],
  convertableToN: ConvertableTo[N]) extends ScaleExp(10, exp)

case class Scale2s[N](exp: Int)(
  implicit
  fieldN:         Field[N],
  convertableToN: ConvertableTo[N]) extends ScaleExp(2, exp)
