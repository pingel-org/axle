
package axle.iterator

import axle._
import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

class CrossProductSpec extends AnyFunSuite with Matchers {

  test("Cross Product") {

    val v1 = Vector("a", "b")
    val v2 = Vector("0", "1")
    val v3 = Vector("X")

    val cp = CrossProduct(Vector(v1, v2, v3, v2))

    cp.size should be(8)
  }

  test("Indexed Cross Product") {

    val v1 = Vector("a", "b")
    val v2 = Vector("0", "1")
    val v3 = Vector("X")

    val cp = IndexedCrossProduct(Vector(v1, v2, v3, v2))

    cp(0) should be(List("a", "0", "X", "0"))

    cp.size should be(8)
  }

}
