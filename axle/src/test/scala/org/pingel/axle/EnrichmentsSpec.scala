package org.pingel.axle

import org.specs2.mutable._

class EnrichmentsSpec extends Specification {

  import org.pingel.axle.Enrichments._

  "List Enrichment" should {
    "work" in {
      
      val cp = List(1, 2, 3) ⨯ List(4, 5, 6)
      val cpl = cp.toList
      
      cpl.length must be equalTo (9)
      cpl(0) must be equalTo ( List(1, 4) )
      cpl(8) must be equalTo ( List(3, 6) )
    }
  }

}
