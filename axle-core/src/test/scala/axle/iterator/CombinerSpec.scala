package axle.iterator

import axle._

import org.specs2.mutable._

class CombinerSpec extends Specification {

  "Combine (a, b) 2" in {
    val cAB2 = Combinations(Vector("a", "b"), 2).toList
    cAB2 must have size (1)
    cAB2.contains(List("a", "b")) must be equalTo true
  }

}

