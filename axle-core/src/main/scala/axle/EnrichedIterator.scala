package axle

class EnrichedIterator[T](it: Iterator[T]) {
  def lastOption: Option[T] = {
    if (it.hasNext) {
      var last = it.next
      while (it.hasNext) {
        last = it.next
      }
      Option(last)
    } else {
      None
    }
  }
}
