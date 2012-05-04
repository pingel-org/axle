
package axle.iterator

import org.specs2.mutable._

	  
class PermuterSpec extends Specification {

  "Permuter" should {

    "Permute () 0" in {
      val p0 = Permuter[String](List(), 0).toList
      p0 must have size(1) // TODO: should this be 0 or 1 ?
    }
    
    "Permute (a) 1" in {
      val pA1 = Permuter[String](List("a"), 1).toList
      pA1 must have size(1)
      pA1 must contain(List("a"))
    }

    "Permute (a, b) 1" in {
      val pAB2 = new Permuter[String](List("a", "b"), 1).toList
      pAB2 must have size(2)
      pAB2 must contain(List("a"))
      pAB2 must contain(List("b"))
    }

    "Permute (a, b) 2" in {
      val pAB2 = new Permuter[String](List("a", "b"), 2).toList
      pAB2 must have size(2)
      pAB2 must contain(List("a", "b"))
      pAB2 must contain(List("b", "a"))
    }

    "Permute (a, b, c) 1" in {
      val pABC1 = new Permuter[String](List("a", "b", "c"), 1).toList
      pABC1 must have size(3)
    }

    "Permute (a, b, c) 2" in {
      val pABC2 = new Permuter[String](List("a", "b", "c"), 2).toList
      pABC2 must have size(6)
    }
    
    "Permute (a, b, c) 3" in {
      val pABC3 = new Permuter[String](List("a", "b", "c"), 3).toList
      pABC3 must have size(6)
    }
    
  }

}
