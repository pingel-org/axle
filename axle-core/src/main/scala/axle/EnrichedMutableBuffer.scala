package axle

import collection.mutable.ListBuffer
import collection.mutable.Buffer

case class EnrichedMutableBuffer[T](buffer: Buffer[T]) {

  def apply(range: Range): Buffer[T] = {
    assert(range.step == 1)
    if (range.isEmpty) {
      new ListBuffer[T]()
    } else {
      buffer.slice(range.start, range.last + 1)
    }
  }

  def update(r: Range, newvals: Buffer[T]): Unit = {
    assert(r.step == 1)
    assert(r.length == newvals.length)
    for ((i, v) <- r.zip(newvals)) {
      buffer(i) = v
    }
  }

}
