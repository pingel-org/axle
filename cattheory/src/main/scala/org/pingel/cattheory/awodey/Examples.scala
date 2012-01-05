
package org.pingel.cattheory.awoney

object Examples {

  import CategoryTheory._

  {
    // finite sets
    val ints = ⋅(Set(1, 2))
    val strings = ⋅(Set("A", "B"))
    val fii = →(ints, ints)       // TODO
    val fis = →(ints, strings)    // TODO
    val fsi = →(strings, ints)    // TODO
    val fss = →(strings, strings) // TODO
    val setsFin1 = Category(Set(ints, strings), Set(fii, fis, fsi, fss))
  }

  {
    // structured sets

    //Category(groups, groupHomomorphisms)
    //Category(vectorSpaces, linearMappings)
    //Category(graphs, graphHomomorphisms)
    //Category(ℝ, f: ℝ → ℝ)
    //Category(U, f: U → V) /* U,V ⊆ ℝ */
    //Category(topologicalSpaces, continuousMappings)
    //Category(differentiableManifolds, smoothMappings)
    //Category(ℕ, f: ℕ → ℕ) /* f is recursive */
    //Category(posets, monotoneFunctions)
  }

  {
    // poset + monotone functions

    import DiscreteMath._

    println("TODO")
    // TODO
     
    // an arrow, m: A → B, is "monotone" if 
    // for all a and a' in A
    // TODO
  }

  {
    // finite categories

    object * extends ⋅ { }
    object ✭ extends ⋅ { }
    object ● extends ⋅ { }
    
    val c0 = Category(Set(), Set())

    val c1 = Category(Set(*), Set())

    val c2 = Category(Set(*, ✭), Set())

    val c3 = Category(Set(*, ✭, ●), Set())

    // TODO: add required arrows to these categories
  }
}
