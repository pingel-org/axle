
package axle.ast.language

import axle.ast._
import org.scalatest._

class LLTest1 extends FunSuite with Matchers {

  test("LL Grammar #1") {

    val g = LLLanguage("(...+a)", List(
      ("S", List("F")),
      ("S", List("(", "S", "+", "F", ")")),
      ("F", List("a"))))

    g.parse("(a+a)").map(_.map(_.id)) should be(Some(List(2, 1, 3, 3)))
  }

  test("LL Grammar #2") {

    val g = LLLanguage("LL Test 2", List(
      ("S", List("F")),
      ("S", List("(", "S", "+", "F", ")")),
      ("S", List("(", "S", "-", "F", ")")),
      ("S", List("(", "S", "*", "F", ")")),
      ("S", List("(", "S", "/", "F", ")")),
      ("F", List("a")),
      ("F", List("b")),
      ("F", List("c")),
      ("F", List("d"))))

    // TODO
    g.parse("(a+a)").map(_.map(_.id)) should be(None)
  }

  test("LL Grammar #3") {

    // http://www.cs.ucr.edu/~gupta/teaching/453-03/Recitation/2-parse.pdf

    val g = LLLanguage("LL Test 3", List(
      ("S", List("A", "B", "e")),
      ("A", List("d", "B")),
      ("A", List("a", "S")),
      ("A", List("c")),
      ("B", List("A", "S")),
      ("B", List("b"))))

    g.parse("adbbeccbee").map(_.map(_.id)) should be(Some(List(1, 3, 1, 2, 6, 6, 5, 4, 1, 4, 6)))
  }

  test("LL #4") {

    val g = LLLanguage("{a^n c b^n | n > 0}", List(
      ("S", List("a", "A", "b")),
      ("A", List("a", "A", "b")),
      ("A", List("c"))))

    g.parse("aaacbbb").map(_.map(_.id)) should be(Some(List(1, 2, 2, 3)))
  }

  /*

  // TODO remove left recursion
  test("LL #5") {

      val g = LLLanguage("{a^n b^n c^m} | n >= 0, m > 0", List(
        ("S", List("S", "c")),
        ("S", List("A")),
        ("A", List("a", "A", "b")),
        ("A", List("a", "b"))
      ))

      g.parse("aaabbbcc").map(_.map(_.id)) should be(Some(List(1, 3, 1, 2, 6, 6, 5, 4, 1, 4, 6)))
  }
  */

}
