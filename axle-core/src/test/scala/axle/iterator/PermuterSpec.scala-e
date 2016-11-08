
package axle.iterator

import axle._
import org.specs2.mutable._

class PermuterSpec extends Specification {

  "Permuter" should {

    "Permute () 0" in {
      val p0 = Permutations(Vector(), 0)
      p0 must have size (1) // TODO: should this be 0 or 1 ?
    }

    "Permute (a) 1" in {
      val pA1 = Permutations(Vector("a"), 1).toList
      pA1 must have size (1)
      pA1.contains(List("a")) must be equalTo true
    }

    "Permute (a, b) 1" in {
      val pAB2 = Permutations(Vector("a", "b"), 1).toList
      pAB2 must have size (2)
      pAB2.contains(List("a")) must be equalTo true
      pAB2.contains(List("b")) must be equalTo true
    }

    "Permute (a, b) 2" in {
      val pAB2 = Permutations(Vector("a", "b"), 2).toList
      pAB2 must have size (2)
      pAB2.contains(List("a", "b")) must be equalTo true
      pAB2.contains(List("b", "a")) must be equalTo true
    }

    "Permute (a, b, c) 1" in {
      val pABC1 = Permutations(Vector("a", "b", "c"), 1).toList
      pABC1 must have size (3)
    }

    "Permute (a, b, c) 2" in {
      val pABC2 = Permutations(Vector("a", "b", "c"), 2).toList
      pABC2 must have size (6)
    }

    "Permute (a, b, c) 3" in {
      val pABC3 = Permutations(Vector("a", "b", "c"), 3).toList
      pABC3 must have size (6)
    }

  }

  "Fast Permuter" should {

    "Permute () 0" in {
      val p0 = PermutationsFast(Vector(), 0)
      p0 must have size (1) // TODO: should this be 0 or 1 ?
    }

    "Permute (a) 1" in {
      val pA1 = PermutationsFast(Vector("a"), 1).toList
      pA1 must have size (1)
      pA1.contains(List("a")) must be equalTo true
    }

    "Permute (a, b) 1" in {
      val pAB2 = PermutationsFast(Vector("a", "b"), 1).toList
      pAB2 must have size (2)
      pAB2.contains(List("a")) must be equalTo true
      pAB2.contains(List("b")) must be equalTo true
    }

    "Permute (a, b) 2" in {
      val pAB2 = PermutationsFast(Vector("a", "b"), 2).toList
      pAB2 must have size (2)
      pAB2.contains(List("a", "b")) must be equalTo true
      pAB2.contains(List("b", "a")) must be equalTo true
    }

    "Permute (a, b, c) 1" in {
      val pABC1 = PermutationsFast(Vector("a", "b", "c"), 1).toList
      pABC1 must have size (3)
    }

    "Permute (a, b, c) 2" in {
      val pABC2 = PermutationsFast(Vector("a", "b", "c"), 2).toList
      pABC2 must have size (6)
    }

    "Permute (a, b, c) 3" in {
      val pABC3 = PermutationsFast(Vector("a", "b", "c"), 3).toList
      pABC3 must have size (6)
    }
  }

}
