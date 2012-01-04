package org.pingel.bayes

import org.pingel.util.DirectedGraphVertex
import org.pingel.util.UndirectedGraphVertex

case class RandomVariable(name: String, domain: Option[Domain]=None, observable: Boolean=true)
extends DirectedGraphVertex[ModelEdge]
// , UndirectedGraphVertex[VariableLink]
with Comparable[RandomVariable]
{

  val lcName = name.toLowerCase()

  def getName() = name
	
  def getDomain() = domain

  def compareTo(other: RandomVariable): Int = name.compareTo(other.getName)

  override def toString() = name

  def getLabel() = name

}
