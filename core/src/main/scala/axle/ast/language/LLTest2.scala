package axle.ast.language

import collection._
import axle.ast._

object LLTest2 {

  import LLLanguage._

  new LLLanguage("LLTest2", List(
    ("S", List("F")),
    ("S", List("(", "S", "+", "F", ")")),
    ("S", List("(", "S", "-", "F", ")")),
    ("S", List("(", "S", "*", "F", ")")),
    ("S", List("(", "S", "/", "F", ")")),
    ("F", List("a")),
    ("F", List("b")),
    ("F", List("c")),
    ("F", List("d"))
  ))

}
