
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
    def and(other: Boolean) = b && other
    def vee(other: Boolean) = b || other // TODO vee = "V"
    def or(other: Boolean) = b || other
    def implies(other: Boolean) = (! b) || other
  }

  implicit def enrichBoolean(b: Boolean) = EnrichedBoolean(b)

}

object Awodney {

  import Enrichments._

  case class ⋅(v: Any) { // "object"
    def ≡(other: ⋅) = this == other // TODO
  }

  def I(o: ⋅) = →(o, o) // TODO the identity arrow

  case class →(domain: ⋅, codomain: ⋅) { // "arrow"
    def ∘(other: →) = (codomain ≡ other.domain) match {
      case true => →(domain, other.codomain)
      case false => null // TODO wrap in Option?
    }
    def ∈(as: Set[→]) = as.contains(this)
    def ≡(other: →) = {
      println(
	"→.≡ "+
	"this("+this.domain+", "+this.codomain+"), "+
	"other("+other.domain+", "+other.codomain+")"
      )
      (domain ≡ other.domain) ∧ (codomain ≡ other.codomain) // TODO
    }
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

object Foo { // TODO find a name for this 

  import Enrichments._

  case class BinaryRelation[T](domain: Set[T], relation: Set[(T, T)]) {
    
    def contains(a1: T, a2: T) = relation.contains( (a1, a2) )
    
    def isReflexive = 
      domain.∀( a ⇒ relation.contains(a, a)  )
    
    def isTransitive = 
      domain.triples.∀({
	case (a, b, c) ⇒ 
	(relation.contains(a, b) ∧ relation.contains(b, c)) implies relation.contains(a, c)
      })
    
    def isAntiSymmetric = 
      domain.doubles.∀({
	case (a, b) ⇒
	(relation.contains(a, b) ∧ relation.contains(b, a)) implies (a == b)
      })
    
    def isSymmetric = 
      domain.doubles.∀({
	case (a, b) ⇒ relation.contains(a, b) implies relation.contains(b, a)
      })
  }
  
  case class PoSet[T](A: Set[T], RA: BinaryRelation[T]) { // ≤A
    
    // TODO: check that ≤A is for A
    
    // a partially ordered set or "poset" is a set A
    // equipped with a binary relation a ≤A b such that the following
    // conditions hold for all a,b,c in A:
    
    def isValid = {
      val re = RA.isReflexive
      println("≤A is reflexive: " + re)
      
      val tr = RA.isTransitive
      println("≤A is transitive: " + tr)
	
      val as = RA.isAntiSymmetric
      println("≤A is antisymmetric: " + as)

      re ∧ tr ∧ as
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

    import Foo._

    println("TODO")
    // TODO
     
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
