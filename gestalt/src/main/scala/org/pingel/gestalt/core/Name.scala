package org.pingel.gestalt.core

object Name {
  var count = 0
  def nextName() = {
    val result = "s" + count
    count += 1
    result
  }
}

case class Name(var base: String=null) extends Comparable[Name]
{
	if( base == null ) {
	  base = Name.nextName()
	}

    def toString() = base

    def compareTo(obj: Object) = {
		val other = obj.asInstanceOf[Name]
		base.compareTo(other.base)
    }

    // runtime error in asInstanceOf:
    def equals(obj: Object) = base.equals(obj.asInstanceOf[Name].base)

}
