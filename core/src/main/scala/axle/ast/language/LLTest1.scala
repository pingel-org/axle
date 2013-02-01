
package axle.ast.language

import collection._
import axle.Loggable
import axle.ast._

object LLTest1 extends Loggable {

  import LLLanguage._

  val ll1 = new LLLanguage("LLTest1", List(
    ("S", List("F")),
    ("S", List("(", "S", "+", "F", ")")),
    ("F", List("a"))
  ))

  // val derivation = ll1.parse("S", "(a+a)")
  // for( action <- derivation ) {
  //   replay(action)
  // }

}
