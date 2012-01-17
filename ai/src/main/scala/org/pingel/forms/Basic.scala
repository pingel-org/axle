package org.pingel.forms

import org.pingel.gestalt.core.PType
import org.pingel.gestalt.core.Value
import org.pingel.gestalt.core.Domain

import scala.collection._

object Basic {

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

  class PBooleans extends Domain(List(PBooleansValues.tVal, PBooleansValues.fVal)) {}

}
