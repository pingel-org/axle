
package org.pingel.cattheory

// unicode
// http://formex.publications.europa.eu/formex-4/physspec/formex-4-character-encoding-c06.htm
// http://tlt.its.psu.edu/suggestions/international/bylanguage/mathchart.html

object Enrichments {

  case class EnrichedSet[T](s: Set[T]) {
    def ∀(p: T => Boolean) = s.forall(p)
    def doubles: Set[(T, T)] = for (x<-s; y<-s) yield (x,y)
    def triples: Set[(T, T, T)] = for (x<-s; y<-s; z<-s) yield (x,y,z)
    def quadruples: Set[(T, T, T, T)] = for (w<-s; x<-s; y<-s; z<-s) yield (w,x,y,z)
  }

  implicit def enrichSet[T](s: Set[T]) = EnrichedSet(s)

  case class EnrichedBoolean(b: Boolean) {
    def ∧(other: Boolean) = b && other
  }

  implicit def enrichBoolean(b: Boolean) = EnrichedBoolean(b)

}

object Awodney {

  import Enrichments._

  case class ⋅(v: Any) { // "object"
    def ≡(other: ⋅): Boolean = true // TODO
  }

  def I(o: ⋅) = →(o, o) // TODO the identity arrow

  case class →(domain: ⋅, codomain: ⋅) { // "arrow"
    def ∘(other: →): →  = null // TODO
    def ∈(as: Set[→]) = as.contains(this)
    def ≡(other: →): Boolean = true // TODO
  }

  def dom(f: →) = f.domain

  def cod(f: →) = f.codomain

  case class Category(objects: Set[⋅], arrows: Set[→]) {

    // objects: A,B,C
    // arrows : f,g,h

    def containsUnits = objects.∀( I(_) ∈ arrows )

    def isAssociative = arrows.triples
      .filter({ case (f, g, h) ⇒ ((cod(f) ≡ dom(g)) ∧ (cod(g) ≡ dom(h))) }) // f g h can compose
      .∀({ case (f, g, h) ⇒ (h ∘ (g ∘ f)) ≡ ((h ∘ g) ∘ f) })

    def unitProperty = arrows.∀( f ⇒ ((f ≡ (f ∘ I(dom(f)) ) ) ∧ (f ≡ ( I(cod(f)) ∘ f))))

    def isValid = {
      val cu = containsUnits
      println("contains units: " + cu)

      val ia = isAssociative
      println("is associative: " + ia)

      val up = unitProperty
      println("unit property : " + up)

      cu ∧ ia ∧ up
    }

    require(isValid)

  }

}

object Examples {

  import Awodney._

  object ℝ { }
  object ℕ { }

  {
    // finite sets
    val ints = ⋅(Set(1, 2))
    val strings = ⋅(Set("A", "B"))
    val fii = →(ints, ints) /* TODO */
    val fis = →(ints, strings) /* TODO */
    val fsi = →(strings, ints) /* TODO */
    val fss = →(strings, strings) /* TODO */
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
    
    // a partially ordered set or "poset" is a set A
    // equipped with a binary relation a ≤A b such that the following
    // conditions hold for all a,b,c in A:
      
    // reflexivity: a ≤A a
    // transitivity: (a ≤A b ∧ b ≤A c) then a ≤A c
    // antisymmetry: (a ≤A b ∧ b ≤A a) then a ≡ b
      
    // an arrow, m: A → B, is "monotone" if 
    // for all a and a' in A
    // TODO
  }
}

object Main {

  def main(args: Array[String]) {

    println("Hello, world")

    import Awodney._

    val ints = ⋅(Set(1, 2))
    val strings = ⋅(Set("A", "B"))
    val fii = →(ints, ints) /* TODO */
    val fis = →(ints, strings) /* TODO */
    val fsi = →(strings, ints) /* TODO */
    val fss = →(strings, strings) /* TODO */
    val setsFin1 = Category(Set(ints, strings), Set(fii, fis, fsi, fss))

  }

}
