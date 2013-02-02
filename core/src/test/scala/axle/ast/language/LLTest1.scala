
package axle.ast.language

import axle.ast._
import org.specs2.mutable._

class LLTest1 extends Specification {

  "LL Grammar #1" should {
    "work" in {

      val ll1 = new LLLanguage("LLTest1", List(
        ("S", List("F")),
        ("S", List("(", "S", "+", "F", ")")),
        ("F", List("a"))
      ))

      val derivationOpt = ll1.parse("(a+a)")

      1 must be equalTo (1)
    }
  }

  "LL Grammar #2" should {
    "work" in {

      val ll2 = new LLLanguage("LLTest2", List(
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

      val derivationOpt = ll2.parse("(a+a)")

      1 must be equalTo (1)
    }
  }

}
