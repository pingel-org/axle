package org.pingel.bayes

import org.pingel.axle.graph.DirectedGraphVertex
import org.pingel.axle.graph.UndirectedGraphVertex
import org.pingel.ptype.PType
import org.pingel.forms.Variable
import org.pingel.gestalt.core.Value
import org.pingel.gestalt.core.Domain

case class RandomVariable(name: String, domain: Option[Domain]=None, observable: Boolean=true)
extends Variable
with DirectedGraphVertex[ModelEdge]
with UndirectedGraphVertex[VariableLink]
with Comparable[RandomVariable]
{

  val lcName = name.toLowerCase()

  def getName() = name
	
  def getDomain() = domain

  def compareTo(other: RandomVariable): Int = name.compareTo(other.getName)

  override def toString() = name

  def getLabel() = name

  def nextVariable(namer: VariableNamer): RandomVariable // TODO
}
