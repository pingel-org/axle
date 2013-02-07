package axle.iterator

import org.specs2.mutable._

class CombinerSpec extends Specification {

  "Combine (a, b) 2" in {
    val cAB2 = Permutations(Vector("a", "b"), 2).toList
    cAB2 must have size (1)
    cAB2 must contain(List("a", "b"))
  }

}

