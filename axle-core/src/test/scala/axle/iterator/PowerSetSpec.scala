
package axle.iterator

import axle._
import org.scalatest._

class PowerSetSpec extends FunSuite with Matchers {

  test("Indexed Power Set Enumerate subsets of {a, b}") {
    val elems = Vector("a", "b")
    val psAB = ℘(elems).toList
    psAB should have size (4)
    psAB.contains(Set()) should be(true)
    psAB.contains(Set("a")) should be(true)
    psAB.contains(Set("b")) should be(true)
    psAB.contains(Set("a", "b")) should be(true)
  }

}
