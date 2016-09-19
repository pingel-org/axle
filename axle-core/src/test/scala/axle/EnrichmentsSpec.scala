package axle

import org.specs2.mutable._
import spire.implicits._

class EnrichmentsSpec extends Specification {

  "x in EnrichedGenTraversable" should {
    "work" in {
      val cp = List(1, 2, 3) ⨯ List(4, 5, 6)
      val cpl = cp.toList
      cpl.length must be equalTo (9)
      cpl(0) must be equalTo ((1, 4))
      cpl(8) must be equalTo ((3, 6))
    }
  }

  "EnrichedIndexdSeq" should {
    "apply(Range) returns sub-sequence" in {
      val xs = (1 to 10).toVector
      xs(3 to 7).size must be equalTo 5
    }
    "swap(i, j) swaps values" in {
      val xs = (1 to 3).toVector
      xs.swap(0, 1) must be equalTo Vector(2, 1, 3)
    }
    "random selects random element" in {
      val xs = (1 to 10).toVector
      xs.random must be greaterThan 0
    }
    "powerset creates powerset" in {
      val xs = (1 to 3).toVector
      xs.powerset.size must be equalTo 8
    }
    "℘ also creates powerset" in {
      val xs = (1 to 3).toVector
      xs.℘ must be equalTo xs.powerset
    }
    "permutations(n) creates permutations" in {
      val xs = (1 to 5).toVector
      xs.permutations(2).size must be equalTo 20
    }
    "combinations(n) creates combinations" in {
      val xs = (1 to 5).toVector
      xs.combinations(2).size must be equalTo 10
    }
  }

  "forall in axle._" should {
    "work" in {
      forall(List(2, 4, 6))(_ % 2 == 0) must be
    }
  }

}
