package axle.quantumcircuit

import spire.math.Complex
import spire.algebra.Field
// import spire.implicits.multiplicativeSemigroupOps

import axle.algebra.Binary
import axle.algebra.{B0, B1}
import axle.stats.Variable
import axle.stats.ConditionalProbabilityTable

case class QBit[T: Field](a: Complex[T], b: Complex[T]) {

  require((a * a) + (b * b) === Complex(Field[T].one, Field[T].zero))

  def unindex: Vector[Complex[T]] = Vector(a, b)

  // A QBit (a b) collapses to an actual value of 0 or 1
  //  0 with probability a^2
  //  1 with probability b^2

  def probabilityModel: ConditionalProbabilityTable[Binary, T] = {
    val m = Map[Binary, T](
      B0 -> (a * a).real,
      B1 -> (b * b).real
    )
    ConditionalProbabilityTable.apply(m, Variable("Q"))
  }
    
}
