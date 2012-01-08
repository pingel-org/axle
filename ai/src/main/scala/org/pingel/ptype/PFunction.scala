package org.pingel.ptype

class PFunction(from: PType, to: List[PType]) extends PType {

  // TODO: are "from" and "to" reversed ??
  
	def toString() = from.toString + " => " + to.toString

}
