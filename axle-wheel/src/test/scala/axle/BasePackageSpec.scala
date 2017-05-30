package axle

import org.scalatest._

import spire.implicits._

class BasePackageSpec extends FunSuite with Matchers {

  test("quantifiers there exists (∃) and for all (∀)") {
    ∃(List(1, 2, 3)) { i: Int => i % 2 == 0 } should be(true)
    ∀(List(1, 2, 3)) { i: Int => i % 2 == 0 } should be(false)
  }

}
