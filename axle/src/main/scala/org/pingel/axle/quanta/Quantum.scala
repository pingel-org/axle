package org.pingel.axle.quanta

/**
 * Quantum
 * 
 * World English Dictionary
 * 
 * quantum
 * 
 * 4. something that can be quantified or measured
 * 
 * http://dictionary.reference.com/browse/quantum
 * 
 */

trait Quantum {

  val wikipediaUrl: String

  val unitsOfMeasurement: List[UnitOfMeasurement]

  val derivations: List[Quantum]

  // examples are often used as units in their own right
  val examples: List[Quantity]

  def *(right: Quantum) = QuantumMultiplication(this, right)

  def /(right: Quantum) = QuantumMultiplication(this, right)

  def squared() = QuantumMultiplication(this, this)
}

case class QuantumMultiplication(left: Quantum, right: Quantum) extends Quantum {
  // units = multiplications of the cross-product of left and right
}

case class QuantumDivision(left: Quantum, right: Quantum) extends Quantum {
  // units = divisions of the cross-product of left and right
}
