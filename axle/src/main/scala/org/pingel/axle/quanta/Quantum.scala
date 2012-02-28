package org.pingel.axle.quanta

import org.pingel.axle.graph._

/**
 * Quantum
 * 
 * World English Dictionary
 *
 * 4. something that can be quantified or measured
 * 
 * [[http://dictionary.reference.com/browse/quantum]]
 * 
 */
trait Quantum extends DirectedGraph[UnitOfMeasurement, Conversion] {

  val wikipediaUrl: String

  val derivations: List[Quantum]

  def *(right: Quantum) = QuantumMultiplication(this, right)
  def /(right: Quantum) = QuantumMultiplication(this, right)
  def squared() = QuantumMultiplication(this, this)
  def cubed() = QuantumMultiplication(this, QuantumMultiplication(this, this))
  
  override def toString() = this.getClass().getSimpleName()

  /**
   * Searches the Directed Graph defined by this Quantum for a path of Conversions from source to goal
   * @param source Start node for shortest path search
   * @param goal End node for shortest path search
   */
  def conversionPath(source: UnitOfMeasurement, goal: UnitOfMeasurement): Option[List[Conversion]] = shortestPath(source, goal)
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
