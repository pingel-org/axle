
package org.pingel.cattheory.awodney

object Enrichments {

  case class EnrichedSet[T](s: Set[T]) {
    def ∀(p: T => Boolean) = s.forall(p)
    def ∃(p: T => Boolean) = s.exists(p)
    def doubles: Set[(T, T)] = for (x<-s; y<-s) yield (x,y)
    def triples: Set[(T, T, T)] = for (x<-s; y<-s; z<-s) yield (x,y,z)
  }

  implicit def enrichSet[T](s: Set[T]) = EnrichedSet(s)

  case class EnrichedBoolean(b: Boolean) {

    def ∧(other: Boolean) = b && other
    def and(other: Boolean) = b && other

    def ∨(other: Boolean) = b || other // TODO vee = "V"
    def or(other: Boolean) = b || other

    def implies(other: Boolean) = (! b) || other
  }

  implicit def enrichBoolean(b: Boolean) = EnrichedBoolean(b)

}
