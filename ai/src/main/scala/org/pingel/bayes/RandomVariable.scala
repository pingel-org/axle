package org.pingel.bayes

import axle.graph.JungDirectedGraphFactory
import axle.graph.JungUndirectedGraphFactory
import org.pingel.gestalt.core.PType
import org.pingel.gestalt.core.Value
import org.pingel.gestalt.core.Domain

class Variable extends PType {

}

case class RandomVariable(name: String, domain: Option[Domain] = None, observable: Boolean = true)
  extends Variable
  with Comparable[RandomVariable] {

  val lcName = name.toLowerCase()

  def getName() = name

  def getDomain() = domain

  def compareTo(other: RandomVariable): Int = name.compareTo(other.getName)

  override def toString() = name

  def getLabel() = name

  def nextVariable(namer: VariableNamer): RandomVariable // TODO
}
