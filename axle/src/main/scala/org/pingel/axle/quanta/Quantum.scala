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

  val examples: List[Quantity]

  def *(right: Quantum) = QuantumMultiplication(this, right)

  def /(right: Quantum) = QuantumMultiplication(this, right)

  def squared() = QuantumMultiplication(this, this)
  
  override def toString() = this.getClass().getSimpleName()

  def path(source: UnitOfMeasurement, goal: UnitOfMeasurement): Option[List[Quantity]] = {
	  Some(List(Distance.kilometer)) // TODO search the graph
  }

}

case class QuantumMultiplication(left: Quantum, right: Quantum) extends Quantum {

  val wikipediaUrl = ""
  
  val unitsOfMeasurement = Nil // TODO multiplications of the cross-product of left and right
  
  val derivations = Nil
  
  val examples = Nil
}

case class QuantumDivision(left: Quantum, right: Quantum) extends Quantum {

  val wikipediaUrl = ""
  
  val unitsOfMeasurement = Nil // TODO divisions of the cross-product of left and right

  val derivations = Nil
  
  val examples = Nil
 
}
