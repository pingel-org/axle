package org.pingel.gestalt.core

import java.util.TreeMap
import scala.collection._

class PType {

  var values = mutable.ListBuffer[Form]()

  var values2index = new TreeMap[Form, Integer]()

  def getValues() = values

  def addValue(value: Form) = {
    val index = values.size
    values += value
    values2index.put(value, new Integer(index))
  }

  def indexOf(value: Form): Int = values2index.get(value).intValue

}

class PUnknownType extends PType

class PTupleType(types: List[PType]) extends PType

class PSet(memberType: PType) extends PType

class PReals extends PType {}

class PModel extends PType {}

class PIntegers extends PType {}

class PFunction(from: PType, to: List[PType]) extends PType {

  // TODO: are "from" and "to" reversed ??

  override def toString() = from.toString + " => " + to.toString

}

object PBooleansValues {
  val tVal = new Value("true")
  val fVal = new Value("false")
}

class PBooleans
  extends Domain(List(PBooleansValues.tVal, PBooleansValues.fVal)) {}
