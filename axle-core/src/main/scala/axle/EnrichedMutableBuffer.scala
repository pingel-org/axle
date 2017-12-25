package axle

import scala.collection.mutable.Buffer
import scala.collection.mutable.ListBuffer

import spire.implicits.IntAlgebra
import spire.implicits.eqOps

case class EnrichedMutableBuffer[T](buffer: Buffer[T]) {

  def apply(range: Range): Buffer[T] = {
    assert(range.step === 1)
    if (range.isEmpty) {
      new ListBuffer[T]()
    } else {
      buffer.slice(range.start, range.last + 1)
    }
  }

  def update(r: Range, newvals: Buffer[T]): Unit = {
    assert(r.step === 1)
    assert(r.length === newvals.length)
    r.zip(newvals) foreach {
      case (i, v) =>
        buffer(i) = v
    }
  }

}
