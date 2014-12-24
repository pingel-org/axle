
package axle.iterator

import axle._
import org.specs2.mutable._

class CrossProductSpec extends Specification {

  "Cross Product" should {
    "work" in {

      val v1 = Vector("a", "b")
      val v2 = Vector("0", "1")
      val v3 = Vector("X")

      val cp = CrossProduct(Vector(v1, v2, v3, v2))

      cp.size must be equalTo (8)
    }
  }

  "Indexed Cross Product" should {
    "work" in {

      val v1 = Vector("a", "b")
      val v2 = Vector("0", "1")
      val v3 = Vector("X")

      val cp = IndexedCrossProduct(Vector(v1, v2, v3, v2))

      cp(0) must be equalTo (List("a", "0", "X", "0"))

      cp.size must be equalTo (8)
    }
  }

}
