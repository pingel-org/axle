package axle

case class EnrichedArray[T](arr: Array[T]) {

  def apply(range: Range) = {
    assert(range.step == 1)
    arr.slice(range.start, range.last)
  }
}
