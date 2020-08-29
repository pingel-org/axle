package axle

import scala.collection.GenTraversable
import scala.collection.Seq

case class EnrichedGenTraversable[+T](gt: GenTraversable[T]) {

  def doubles: Seq[(T, T)] = gt.toIndexedSeq.permutations(2).map(d => (d(0), d(1))).toSeq

  def triples: Seq[(T, T, T)] = gt.toIndexedSeq.permutations(3).map(t => (t(0), t(1), t(2))).toSeq

  def ⨯[S](right: GenTraversable[S]) = for (x <- gt; y <- right) yield (x, y)

}
