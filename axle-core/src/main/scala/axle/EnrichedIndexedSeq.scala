package axle

import scala.collection.IndexedSeq

import cats.implicits._

case class EnrichedIndexedSeq[T](is: scala.collection.immutable.IndexedSeq[T]) {

  def apply(range: Range): IndexedSeq[T] = {
    assert(range.step === 1)
    if (range.isEmpty) {
      List[T]().toIndexedSeq
    } else {
      is.slice(range.start, range.last + 1)
    }
  }

  def swap(i0: Int, i1: Int): IndexedSeq[T] =
    is.zipWithIndex.map({
      case (v, i) =>
        if (i === i0) is(i1) else (if (i === i1) is(i0) else v)
    })

}
