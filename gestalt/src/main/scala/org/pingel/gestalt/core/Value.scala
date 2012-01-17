package org.pingel.gestalt.core

class Value(v: String)
extends SimpleForm(new Name(v)) // may not be what was intended in original
// with Comparable[Value] 
{

  def compareTo(other: Value): Int = v.compareTo(other.getV)

  def getV(): String = v

  override def toString() = v

}

