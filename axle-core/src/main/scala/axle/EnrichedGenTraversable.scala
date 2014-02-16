package axle

import scala.collection.GenTraversable
import scala.collection.Seq

import spire.algebra.AdditiveMonoid
import spire.algebra.MultiplicativeMonoid

case class EnrichedGenTraversable[+T: Manifest](gt: GenTraversable[T]) {

  def doubles: Seq[(T, T)] = gt.toIndexedSeq.permutations(2).map(d => (d(0), d(1))).toSeq

  def triples: Seq[(T, T, T)] = gt.toIndexedSeq.permutations(3).map(t => (t(0), t(1), t(2))).toSeq

  def ⨯[S](right: GenTraversable[S]) = for (x <- gt; y <- right) yield (x, y)

}
