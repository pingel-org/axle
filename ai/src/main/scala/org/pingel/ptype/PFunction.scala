package org.pingel.ptype

class Function(from: PType, to: PType) extends PType {

	def toString() = from.toString + " => " + to.toString

}
