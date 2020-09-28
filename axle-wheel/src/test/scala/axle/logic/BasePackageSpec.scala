package axle.logic

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

import spire.algebra._
import cats.implicits._

class BasePackageSpec extends AnyFunSuite with Matchers {

  implicit val boolBoolean: Bool[Boolean] = spire.implicits.BooleanStructure

  test("quantifiers there exists (∃) and for all (∀)") {
    ∃(List(1, 2, 3)) { i: Int => i % 2 == 0 } should be(true)
    ∀(List(1, 2, 3)) { i: Int => i % 2 == 0 } should be(false)
  }

}
