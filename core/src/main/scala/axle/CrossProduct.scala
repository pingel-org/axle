package axle

import collection._
import Stream.{ cons, empty }

class CrossProduct[E](collections: IndexedSeq[IndexedSeq[E]]) extends Iterable[List[E]] {

  def iterator() = result.toIterator

  lazy val result: Stream[List[E]] = tail(None, 0)

  def current(indices: IndexedSeq[Int]) = collections.zip(indices).map({ case (c, i) => c(i) }).toList

  def tail(indices0opt: Option[IndexedSeq[Int]], i0: Int): Stream[List[E]] =
    indices0opt.map( indices0 =>
      if (i0 == collections.size) {
        empty
      } else if (indices0(i0) + 1 < collections(i0).size ) {
        val indices1 = indices0.updated(i0, indices0(i0) + 1)
        cons(current(indices1), tail(Some(indices1), 0))
      } else {
        tail(Some(indices0.updated(i0, 0)), i0 + 1)
      }
    ).getOrElse({
      val indices1 = collections.map(c => 0)
      cons(current(indices1), tail(Some(indices1), 0))
    })

}
