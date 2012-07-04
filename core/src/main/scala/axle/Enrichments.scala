
package axle

import iterator.ListCrossProduct

import collection._

object Enrichments {

  case class EnrichedGenTraversable[+T](gt: GenTraversable[T]) {

    def Σ(f: T => Double) = gt.aggregate(0.0)(_ + f(_), _ + _)

    def Sigma(f: T => Double) = Σ(f)

    def Π(f: T => Double): Double = gt.aggregate(1.0)(_ * f(_), _ * _)

    def Pi(f: T => Double) = Π(f)

    def ∀(p: T => Boolean) = gt.forall(p)

    def ∃(p: T => Boolean) = gt.exists(p)

    def doubles(): GenTraversable[(T, T)] = for (x <- gt; y <- gt) yield (x, y)

    def triples(): GenTraversable[(T, T, T)] = for (x <- gt; y <- gt; z <- gt) yield (x, y, z)

    def ⨯[S](right: GenTraversable[S]) = for (x <- gt; y <- right) yield (x, y)

  }

  implicit def enrichGenTraversable[T](gt: GenTraversable[T]) = EnrichedGenTraversable(gt)

  case class EnrichedByteArray(barr: Array[Byte]) {
    def ⊕(other: Array[Byte]): Array[Byte] = barr.zip(other).map(lr => (lr._1 ^ lr._2).toByte).toArray
  }

  implicit def enrichByteArray(barr: Array[Byte]) = EnrichedByteArray(barr)

  case class EnrichedArray[T](arr: Array[T]) {

    def apply(range: Range) = {
      assert(range.step == 1)
      arr.slice(range.start, range.end)
    }
  }

  implicit def enrichArray[T](arr: Array[T]) = EnrichedArray(arr)

  case class EnrichedBoolean(b: Boolean) {

    def ¬:() = !b

    def ∧(other: Boolean) = b && other
    def and(other: Boolean) = b && other

    def ∨(other: Boolean) = b || other
    def or(other: Boolean) = b || other

    def ⊃(other: Boolean) = (!b) || other
    def implies(other: Boolean) = (!b) || other

    def ⊕(other: Boolean) = ((!b) && (!other)) || (b && other)
    def ⊻(other: Boolean) = ((!b) && (!other)) || (b && other)
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
