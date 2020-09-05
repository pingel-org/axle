
package axle.iterator

import axle._
import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers


class PermuterSpec extends AnyFunSuite with Matchers {

  test("Permute () 0") {
    val p0 = Permutations(Vector(), 0)
    p0 should have size (1) // TODO: should this be 0 or 1 ?
  }

  test("Permute (a) 1") {
    val pA1 = Permutations(Vector("a"), 1).toList
    pA1 should have size (1)
    pA1.contains(List("a")) should be(true)
  }

  test("Permute (a, b) 1") {
    val pAB2 = Permutations(Vector("a", "b"), 1).toList
    pAB2 should have size (2)
    pAB2.contains(List("a")) should be(true)
    pAB2.contains(List("b")) should be(true)
  }

  test("Permute (a, b) 2") {
    val pAB2 = Permutations(Vector("a", "b"), 2).toList
    pAB2 should have size (2)
    pAB2.contains(List("a", "b")) should be(true)
    pAB2.contains(List("b", "a")) should be(true)
  }

  test("Permute (a, b, c) 1") {
    val pABC1 = Permutations(Vector("a", "b", "c"), 1).toList
    pABC1 should have size (3)
  }

  test("Permute (a, b, c) 2") {
    val pABC2 = Permutations(Vector("a", "b", "c"), 2).toList
    pABC2 should have size (6)
  }

  test("Permute (a, b, c) 3") {
    val pABC3 = Permutations(Vector("a", "b", "c"), 3).toList
    pABC3 should have size (6)
  }

  test("PermutationsFast () 0") {
    val p0 = PermutationsFast(Vector(), 0)
    p0 should have size (1) // TODO: should this be 0 or 1 ?
  }

  test("PermutationsFast (a) 1") {
    val pA1 = PermutationsFast(Vector("a"), 1).toList
    pA1 should have size (1)
    pA1.contains(List("a")) should be(true)
  }

  test("PermutationsFast (a, b) 1") {
    val pAB2 = PermutationsFast(Vector("a", "b"), 1).toList
    pAB2 should have size (2)
    pAB2.contains(List("a")) should be(true)
    pAB2.contains(List("b")) should be(true)
  }

  test("PermutationsFast (a, b) 2") {
    val pAB2 = PermutationsFast(Vector("a", "b"), 2).toList
    pAB2 should have size (2)
    pAB2.contains(List("a", "b")) should be(true)
    pAB2.contains(List("b", "a")) should be(true)
  }

  test("PermutationsFast (a, b, c) 1") {
    val pABC1 = PermutationsFast(Vector("a", "b", "c"), 1).toList
    pABC1 should have size (3)
  }

  test("PermutationsFast (a, b, c) 2") {
    val pABC2 = PermutationsFast(Vector("a", "b", "c"), 2).toList
    pABC2 should have size (6)
  }

  test("PermutationsFast (a, b, c) 3") {
    val pABC3 = PermutationsFast(Vector("a", "b", "c"), 3).toList
    pABC3 should have size (6)
  }

}
