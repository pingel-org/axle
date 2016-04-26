package axle

class EnrichedIterator[T](it: Iterator[T]) {

  def terminatesWithin(k: Int): Boolean = {
    it.zipWithIndex.take(k).lastOption.map({ case (_, i) => i + 1 < k }).getOrElse(false)
  }

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
