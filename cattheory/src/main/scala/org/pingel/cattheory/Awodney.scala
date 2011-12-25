
package org.pingel.cattheory

// TODO: produce latex images of these categories, arrows, objects, etc
//   use one of the latex webservices

object Main {

  def main(args: Array[String]) {

    println("Hello, world")

    import awodney.CategoryTheory._

    val ints = ⋅(Set(1, 2))
    val strings = ⋅(Set("A", "B"))

    println("ints ≡ strings: " + (ints ≡ strings))
    println("ints ≡ ints   : " + (ints ≡ ints))

    val fii = →(ints, ints)       /* TODO */
    val fis = →(ints, strings)    /* TODO */
    val fsi = →(strings, ints)    /* TODO */
    val fss = →(strings, strings) /* TODO */

    println("fii ≡ fii: " + (fii ≡ fii) )
    println("fii ≡ fis: " + (fii ≡ fis) )

    // val setsFin1 = Category(Set(ints, strings), Set(fii, fis, fsi, fss))
    val setsFin1 = Category(Set(ints), Set(fii))

  }

}
