package axle

object ℘ {

  def apply[E](all: IndexedSeq[E]): IndexedPowerSet[E] = IndexedPowerSet[E](all)
}

/**
 * A ℘ (IndexedPowerSet) constructed with a collection with elements of type E can construct
 * an Iterator which enumerates all possible subsets (of type Collection<E>) of the
 * collection used to construct the PowerSet.
 *
 * @author Adam Pingel
 *
 * @param [E] The type of elements in the Collection passed to the constructor.
 */

case class IndexedPowerSet[E](all: IndexedSeq[E])
  extends Iterable[Set[E]] {

  val steps = all.length.to(0, -1).map(0x01 << _)

  // TODO: there ought to be an ultra-fast and low-level implementation of "mask"
  // which is basically just a conversion from Int => List[Boolean] (reversed)
  def mask(i: Int): List[Boolean] =
    steps.foldLeft((List[Boolean](), i))((rv, b) => {
      if (rv._2 >= b) { (true :: rv._1, rv._2 - b) } else { (false :: rv._1, rv._2) }
    })._1

  def apply(i: Int): Set[E] = {
    if (i < 0 || i >= size) { throw new java.lang.IndexOutOfBoundsException }
    mask(i).zip(all).collect({ case (b, v) if b => v }).toSet
  }

  override def size: Int = 0x01 << all.length

  def iterator: Iterator[Set[E]] = (0 until size).iterator.map(this.apply)

}
