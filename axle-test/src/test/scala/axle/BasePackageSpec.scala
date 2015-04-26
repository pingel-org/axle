package axle

import org.specs2.mutable.Specification

import spire.implicits._
import spire.optional.unicode.Σ

class BasePackageSpec extends Specification {

  // from spire: Σ((1 to 10) map { _ * 2 })
  
  "quantifiers there exists (∃) and for all (∀)" should {
    "work" in {
      ∃(List(1, 2, 3)) { _ % 2 == 0 } must beTrue
      ∀(List(1, 2, 3)) { _ % 2 == 0 } must beFalse
    }
  }

}