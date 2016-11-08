
package axle.ast.language

import axle.ast._
import org.specs2.mutable._

class LLTest1 extends Specification {

  "LL Grammar #1" should {
    "work" in {

      val g = LLLanguage("(...+a)", List(
        ("S", List("F")),
        ("S", List("(", "S", "+", "F", ")")),
        ("F", List("a"))))

      g.parse("(a+a)").map(_.map(_.id)) must be equalTo Some(List(2, 1, 3, 3))
    }
  }

  "LL Grammar #2" should {
    "work" in {

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
      g.parse("(a+a)").map(_.map(_.id)) must be equalTo None
    }
  }

  "LL Grammar #3" should {
    "work" in {

      // http://www.cs.ucr.edu/~gupta/teaching/453-03/Recitation/2-parse.pdf

      val g = LLLanguage("LL Test 3", List(
        ("S", List("A", "B", "e")),
        ("A", List("d", "B")),
        ("A", List("a", "S")),
        ("A", List("c")),
        ("B", List("A", "S")),
        ("B", List("b"))))

      g.parse("adbbeccbee").map(_.map(_.id)) must be equalTo Some(List(1, 3, 1, 2, 6, 6, 5, 4, 1, 4, 6))
    }
  }

  "LL #4" should {
    "work" in {

      val g = LLLanguage("{a^n c b^n | n > 0}", List(
        ("S", List("a", "A", "b")),
        ("A", List("a", "A", "b")),
        ("A", List("c"))))

      g.parse("aaacbbb").map(_.map(_.id)) must be equalTo Some(List(1, 2, 2, 3))
    }
  }

  /*

  // TODO remove left recursion
  "LL #5" should {
    "work" in {

      val g = LLLanguage("{a^n b^n c^m} | n >= 0, m > 0", List(
        ("S", List("S", "c")),
        ("S", List("A")),
        ("A", List("a", "A", "b")),
        ("A", List("a", "b"))
      ))

      g.parse("aaabbbcc").map(_.map(_.id)) must be equalTo Some(List(1, 3, 1, 2, 6, 6, 5, 4, 1, 4, 6))
    }
  }
  */

}
