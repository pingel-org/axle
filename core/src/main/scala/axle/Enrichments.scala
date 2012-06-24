
package axle

import iterator.ListCrossProduct

import collection._

object Enrichments {

  case class EnrichedGenTraversable[+T](gt: GenTraversable[T]) {

    // Note: When gt is a Set, Sigma and Pi
    // would harmfully unique results of f(_)
    // if not for the toSeq call.
    // We may want GenTraversable.aggregate here
    
    def Σ(f: T => Double): Double = gt.toSeq.map(f(_)).sum

    def Sigma(f: T => Double) = Σ(f)

    def Π(f: T => Double): Double = gt.toSeq.map(f(_)).product
    
    def Pi(f: T => Double) = Π(f)

    def ∀(p: T => Boolean) = gt.forall(p)

    def ∃(p: T => Boolean) = gt.exists(p)

    def doubles(): GenTraversable[(T, T)] = for (x <- gt; y <- gt) yield (x, y)

    def triples(): GenTraversable[(T, T, T)] = for (x <- gt; y <- gt; z <- gt) yield (x, y, z)

    def ⨯[S](right: GenTraversable[S]) = for(x <- gt; y <- right) yield (x, y)
    
  }

  implicit def enrichGenTraversable[T](gt: GenTraversable[T]) = EnrichedGenTraversable(gt)

  case class EnrichedBoolean(b: Boolean) {

    def ∧(other: Boolean) = b && other
    def and(other: Boolean) = b && other

    def ∨(other: Boolean) = b || other
    def or(other: Boolean) = b || other

    def implies(other: Boolean) = (!b) || other
  }

  implicit def enrichBoolean(b: Boolean) = EnrichedBoolean(b)

  case class EnrichedList[T](list: List[T]) {

    // def ⨯[S](right: GenTraversable[S]) = new ListCrossProduct[T](Seq(list, right)).iterator
    
  }

  implicit def enrichList[T](list: List[T]) = EnrichedList(list)


//  case class EnrichedSet[T](s: Set[T]) {}
//
//  implicit def enrichSet[T](s: Set[T]) = EnrichedSet(s)
  
  
}
