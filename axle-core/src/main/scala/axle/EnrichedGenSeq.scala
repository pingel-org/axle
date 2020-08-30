package axle

//import scala.collection.GenSeq
import scala.collection.immutable.TreeMap

import cats.kernel.Order
import cats.Order.catsKernelOrderingForOrder
import spire.algebra.Ring
import spire.algebra.CRing

case class EnrichedIterable[T](ita: Iterable[T]) {

  def tally[N: CRing]: Map[T, N] = {
    val ring = Ring[N]
    val mapCR = new spire.std.MapCRng[T, N]()
    ita.foldLeft(Map.empty[T, N].withDefaultValue(ring.zero))(
      (m, x) => mapCR.plus(m, Map(x -> ring.plus(m(x), ring.one))))
  }

  def orderedTally[N: CRing](implicit o: Order[T]): TreeMap[T, N] = {
    new TreeMap[T, N]() ++ tally[N]
  }

  def doubles: Seq[(T, T)] = ita.toIndexedSeq.permutations(2).map(d => (d(0), d(1))).toSeq

  def triples: Seq[(T, T, T)] = ita.toIndexedSeq.permutations(3).map(t => (t(0), t(1), t(2))).toSeq

  def ⨯[S](right: Iterable[S]) = for {
    x <- ita
    y <- right
  } yield (x, y)

}
