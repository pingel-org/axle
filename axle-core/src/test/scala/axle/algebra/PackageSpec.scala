package axle.algebra

import spire.algebra._
import org.scalatest._

class PackageSpec extends FunSuite with Matchers {

  test("etc") {
    implicit val intRing: Ring[Int] = spire.implicits.IntAlgebra
    1.etc.take(5).toList should be(List(1, 2, 3, 4, 5))
  }

}