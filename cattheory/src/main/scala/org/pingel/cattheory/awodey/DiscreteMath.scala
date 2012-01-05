
package org.pingel.cattheory.awodey

object DiscreteMath {

  import Enrichments._

  object ℝ { }

  object ℕ { }

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
