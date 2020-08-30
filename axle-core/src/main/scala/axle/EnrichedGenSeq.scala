package axle

import scala.collection.GenSeq
import scala.collection.immutable.TreeMap

import cats.kernel.Order
import cats.Order.catsKernelOrderingForOrder
import spire.algebra.Ring
import spire.algebra.CRing
//import spire.implicits.MapCSemiring
//import spire.implicits.additiveSemigroupOps

case class EnrichedGenSeq[T](genSeq: GenSeq[T]) {

  def tally[N: CRing]: Map[T, N] = {
    val ring = Ring[N]
    val mapCR = new spire.std.MapCRng[T, N]()
    genSeq.aggregate(Map.empty[T, N].withDefaultValue(ring.zero))(
      (m, x) => m + (x -> ring.plus(m(x), ring.one)),
      mapCR.plus)
  }

  def orderedTally[N: CRing](implicit o: Order[T]): TreeMap[T, N] = {
    new TreeMap[T, N]() ++ tally[N]
  }

}
