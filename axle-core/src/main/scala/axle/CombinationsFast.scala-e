package axle

import scala.collection.mutable.ListBuffer

case class CombinationsFast[E: Manifest](pool: IndexedSeq[E], r: Int)
  extends Iterable[IndexedSeq[E]] {

  val n = pool.size

  if (r > n) {
    throw new IndexOutOfBoundsException()
  }

  lazy val syze = if (0 <= r && r <= n) { n.factorial / r.factorial / (n - r).factorial } else { 0 }

  override def size: Int = syze

  val yeeld = new ListBuffer[IndexedSeq[E]]() // TODO substitute for "yield" for now

  def iterator: Iterator[IndexedSeq[E]] = yeeld.iterator

  val indices = (0 until r).toBuffer
  yeeld += indices.map(pool).toIndexedSeq
  var done = false

  while (!done) {
    var broken = false
    var i = r - 1
    while (i >= 0 && !broken) {
      if (indices(i) != (i + n - r)) {
        broken = true
      }
      if (!broken) {
        i -= 1
      }
    }
    if (!broken) {
      done = true
    } else {
      indices(i) += 1
      (i + 1 until r) foreach { j =>
        indices(j) = indices(j - 1) + 1
      }
      yeeld += indices.map(pool).toIndexedSeq
    }
  }

}
