
package axle.category.awodey

// unicode
// http://formex.publications.europa.eu/formex-4/physspec/formex-4-character-encoding-c06.htm
// http://tlt.its.psu.edu/suggestions/international/bylanguage/mathchart.html

object CategoryTheory {

  import axle.Enrichments._

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
	"→.≡ " +
	"this(" + domain + ", " + codomain + "), " +
	"other(" + other.domain + ", " + other.codomain+")"
      )
      (domain ≡ other.domain) ∧ (codomain ≡ other.codomain) // TODO
    }

  }

  def dom(f: →) = f.domain

  def cod(f: →) = f.codomain

  case class Category(objects: Set[⋅], arrows: Set[→]) {

    def containsUnits = objects.∀( I(_) ∈ arrows )

    def isAssociative = arrows.triples
      .filter({ case (f, g, h) ⇒ ((cod(f) ≡ dom(g)) ∧ (cod(g) ≡ dom(h))) }) // "can compose"
      .∀({ case (f, g, h) ⇒ (h ∘ (g ∘ f)) ≡ ((h ∘ g) ∘ f) })

    def unitProperty = arrows.∀( f ⇒ ((f ≡ (f ∘ I(dom(f)))) ∧ (f ≡ (I(cod(f)) ∘ f))))

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
