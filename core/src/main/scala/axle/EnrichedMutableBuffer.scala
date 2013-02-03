package axle

import collection._

case class EnrichedMutableBuffer[T](buffer: mutable.Buffer[T]) {

  def apply(range: Range): mutable.Buffer[T] = {
    assert(range.step == 1)
    if (range.isEmpty) {
      new mutable.ListBuffer[T]()
    } else {
      buffer.slice(range.start, range.last + 1)
    }
  }

  def update(r: Range, newvals: mutable.Buffer[T]): Unit = {
    assert(r.step == 1)
    assert(r.length == newvals.length)
    for ((i, v) <- r.zip(newvals)) {
      buffer(i) = v
    }
  }

}
