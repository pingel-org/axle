package org.pingel.bayes

import org.pingel.util.DirectedGraphVertex
import org.pingel.util.UndirectedGraphVertex

class RandomVariable(name: String, domain: Domain, lcName: String="TODO", observable: Boolean=true)
// TODO: extends DirectedGraphVertex[ModelEdge], UndirectedGraphVertex[VariableLink], Comparable[RandomVariable]
{

  def getName(): String = name
	
  def getDomain(): Domain = domain

  def compareTo(other: RandomVariable): Int = name.compareTo(other.name)

  override def toString(): String = name

  def getLabel(): String = name

}
