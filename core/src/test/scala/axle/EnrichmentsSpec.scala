package axle

import org.specs2.mutable._

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

  "∀ in EnrichedGenTraversable" should {
    "work" in {
      Set(2, 4, 6).∀(_ % 2 == 0) must be equalTo true
    }
  }

}
