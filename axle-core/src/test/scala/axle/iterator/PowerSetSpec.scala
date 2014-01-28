
package axle.iterator

import axle._
import org.specs2.mutable._

class PowerSetSpec extends Specification {

  "Indexed Power Set" should {
    "Enumerate subsets of {a, b}" in {
      val elems = Vector("a", "b")
      val psAB = ℘(elems).toList
      psAB must have size (4)
      psAB.contains(Set()) must be equalTo true
      psAB.contains(Set("a")) must be equalTo true
      psAB.contains(Set("b")) must be equalTo true
      psAB.contains(Set("a", "b")) must be equalTo true
    }
  }

}
