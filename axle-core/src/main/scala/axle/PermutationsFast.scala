package axle

import scala.collection.mutable.ListBuffer

import spire.implicits.IntAlgebra
import spire.implicits.eqOps

case class PermutationsFast[E: Manifest](pool: IndexedSeq[E], r: Int)
  extends Iterable[IndexedSeq[E]] {

  val n = pool.length

  override def size: Int = if (r >= 0 && r <= n) { n.factorial / (n - r).factorial } else { 0 }

  val yeeld = new ListBuffer[IndexedSeq[E]]() // TODO substitute for "yield" for now

  if (r <= n) {
    val indices = (0 until n).toBuffer
    val cycles = n.until(n - r, -1).toArray
    yeeld += indices(0 until r).map(pool).toIndexedSeq
    var done = false
    while (n > 0 && !done) {
      var i = r - 1
      var broken = false
      while (i >= 0 && !broken) {
        cycles(i) -= 1
        if (cycles(i) === 0) {
          indices(i until n) = indices(i + 1 until n) ++ indices(i until i + 1)
          cycles(i) = n - i
        } else {
          val j = cycles(i)
          val (v1, v2) = (indices((n - j) % n), indices(i))
          indices(i) = v1
          indices((n - j) % n) = v2
          yeeld += indices(0 until r).map(pool).toIndexedSeq
          broken = true
        }
        if (!broken) {
          i -= 1
        }
      }
      if (!broken) {
        done = true
      }
    }
  }

  def iterator: Iterator[IndexedSeq[E]] = yeeld.iterator

}
