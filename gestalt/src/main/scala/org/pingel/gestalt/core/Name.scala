package org.pingel.gestalt.core

object Name {
  var count = 0
  def nextName() = {
    val result = "s" + count
    count += 1
    result
  }
}

case class Name(var base: String = null) extends Comparable[Name] {
  if (base == null) {
    base = Name.nextName()
  }

  override def toString() = base

  def compareTo(other: Name) = base.compareTo(other.base)

  def equals(other: Name) = base.equals(other.base)

}
