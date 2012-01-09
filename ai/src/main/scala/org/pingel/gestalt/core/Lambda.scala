package org.pingel.gestalt.core

import scala.collection._

object Lambda {
    val top = new Name("top")  
}

case class Lambda {

	var names = Set[Name]()
    var name2type = mutable.Map[Name, Name]()

    def getNames() = names

    def contains(n: Name) = names.contains(n)  

    def add(varName: Name): Unit = add(varName, Lambda.top)

	def add(varName: Name, typeName: Name): Unit = {
		names += varName
        name2type += varName -> typeName
	}
	
	def addAll(other: Lambda) = {
		names ++= other.getNames()
	}
}
