package axle.category

import org.specs2.mutable._

class AwodeySpecification extends Specification {

  // TODO: produce latex images of these categories, arrows, objects, etc
  //   use one of the latex webservices

  "Category" should {
    "work" in {

      import awodey.CategoryTheory._

      val ints = ⋅(Set(1, 2))
      val strings = ⋅(Set("A", "B"))

      println("ints ≡ strings: " + (ints ≡ strings))
      println("ints ≡ ints   : " + (ints ≡ ints))

      val fii = →(ints, ints) /* TODO */
      val fis = →(ints, strings) /* TODO */
      val fsi = →(strings, ints) /* TODO */
      val fss = →(strings, strings) /* TODO */

      println("fii ≡ fii: " + (fii ≡ fii))
      println("fii ≡ fis: " + (fii ≡ fis))

      // val setsFin1 = Category(Set(ints, strings), Set(fii, fis, fsi, fss))
      val setsFin1 = Category(Set(ints), Set(fii))

      1 must be equalTo(1)
    }
  }

}