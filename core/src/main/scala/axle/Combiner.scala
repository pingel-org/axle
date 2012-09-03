package axle

case class Combiner[E](objects: Seq[E], n: Int) extends Iterable[Set[E]] {

  if (n > objects.size) {
    throw new IndexOutOfBoundsException()
  }

  def iterator() = new CombinationIterator[E](this)

  class CombinationIterator[InE](combiner: Combiner[InE]) extends Iterator[Set[InE]] {

    def remove() = throw new UnsupportedOperationException()

    def hasNext() = false

    def next(): Set[InE] = null

  }

}