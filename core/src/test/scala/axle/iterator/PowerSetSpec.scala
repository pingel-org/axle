
package axle.iterator

import axle._
import org.specs2.mutable._

class PowerSetSpec extends Specification {

  "Indexed Power Set" should {
    "Enumerate subsets of {a, b}" in {
      val elems = Vector("a", "b")
      val psAB = ℘(elems)
      psAB must have size (4)
      psAB must contain(Set())
      psAB must contain(Set("a"))
      psAB must contain(Set("b"))
      psAB must contain(Set("a", "b"))
    }
  }

}
