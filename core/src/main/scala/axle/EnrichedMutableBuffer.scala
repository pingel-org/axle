package axle

import collection._

case class EnrichedMutableBuffer[T](buffer: mutable.Buffer[T]) {

//  def apply(i: Int): T = if (i >= 0) {
//    buffer(i)
//  } else {
//    buffer(buffer.length + i)
//  }

  def apply(range: Range): mutable.Buffer[T] = {
    assert(range.step == 1)
    buffer.slice(range.start, range.last + 1)
  }

//  def update(i: Int, v: T): Unit = if (i >= 0) {
//    buffer(i) = v
//  } else {
//    buffer(buffer.length + i) = v
//  }

  def update(r: Range, newvals: mutable.Buffer[T]): Unit = {
    assert(r.step == 1)
    assert(r.length == newvals.length)
    for ((i, v) <- r.zip(newvals)) {
      buffer(i) = v
    }
  }

}
