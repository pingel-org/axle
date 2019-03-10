package axle

import org.scalatest._

import spire.algebra._
import cats.implicits._

class BasePackageSpec extends FunSuite with Matchers {

  implicit val boolBoolean: Bool[Boolean] = spire.implicits.BooleanStructure

  test("quantifiers there exists (∃) and for all (∀)") {
    ∃(List(1, 2, 3)) { i: Int => i % 2 == 0 } should be(true)
    ∀(List(1, 2, 3)) { i: Int => i % 2 == 0 } should be(false)
  }

}
