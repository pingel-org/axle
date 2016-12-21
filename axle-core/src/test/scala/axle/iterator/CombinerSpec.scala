package axle.iterator

import axle._

import org.scalatest._

class CombinerSpec extends FunSuite with Matchers {

  test("Combine (a, b) 2") {
    val cAB2 = Combinations(Vector("a", "b"), 2).toList
    cAB2 should have size (1)
    cAB2.contains(List("a", "b")) should be(true)
  }

  test("fast Combine (a, b) 2") {
    val cAB2 = CombinationsFast(Vector("a", "b"), 2).toList
    cAB2 should have size (1)
    cAB2.contains(List("a", "b")) should be(true)
  }

}

