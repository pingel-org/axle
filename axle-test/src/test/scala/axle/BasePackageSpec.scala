package axle

import org.specs2.mutable.Specification

import spire.implicits._

class BasePackageSpec extends Specification {

  "quantifiers there exists (∃) and for all (∀)" should {
    "work" in {
      ∃(List(1, 2, 3)) { i: Int => i % 2 == 0 } must beTrue
      ∀(List(1, 2, 3)) { i: Int => i % 2 == 0 } must beFalse
    }
  }

}