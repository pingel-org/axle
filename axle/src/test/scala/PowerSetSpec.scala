
package org.pingel.axle.iterator

import org.specs2.mutable._
	  
class PowerSetSpec extends Specification {

  import org.pingel.axle.iterator.℘

  "PowerSet" should {
    "Enumerate subsets of {a, b}" in {
      val elems = List("a", "b")
      val psAB = ℘(elems)
      psAB must have size(4)
      psAB must contain(Set())
      psAB must contain(Set("a"))
      psAB must contain(Set("b"))
      psAB must contain(Set("a", "b"))
    }
  }

}
