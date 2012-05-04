
package axle.iterator

import org.specs2.mutable._
	  
class CrossProductSpec extends Specification {

  "Cross Product" should {
    "work" in {
      val v1 = List("a", "b")
      val v2 = List("0", "1")
      val v3 = List("X")
      val cp = new CrossProduct[String](List(v1, v2, v3, v2))
      val cl = cp.toList
      cl.size must be equalTo(3)
    }
  }

}
