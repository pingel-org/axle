package axle.algebra

import scala.collection.mutable.Buffer

import cats.implicits._

case class EnrichedMutableBuffer[T](original: Buffer[T]) {

  def update(r: Range, newvals: Buffer[T]): Unit = {
    assert(r.step === 1)
    assert(r.length === newvals.length)
    r.zip(newvals) foreach {
      case (i, v) =>
        original(i) = v
    }
  }

}
