package axle.algebra

import scala.annotation.implicitNotFound
import spire.algebra.Eq
import spire.algebra.Ring
import spire.implicits.MapRng
import spire.implicits.additiveSemigroupOps
import axle.syntax.aggregatable._

@implicitNotFound("Witness not found for Talliable[${F}, ${A}, ${N}]")
trait Talliable[F, A, N] {

  def tally(ts: F): Map[A, N]
}

object Talliable {

  implicit def tallySeq[A, N](implicit ring: Ring[N]) =
    new Talliable[Seq[A], A, N] {

      def tally(xs: Seq[A]): Map[A, N] = {
        xs.aggregate(Map.empty[A, N].withDefaultValue(ring.zero))(
          (m, x) => m + (x -> ring.plus(m(x), ring.one)),
          _ + _)
      }
    }

  implicit def tallyList[A, N](implicit ring: Ring[N]) =
    new Talliable[List[A], A, N] {

      def tally(xs: List[A]): Map[A, N] = {
        xs.aggregate(Map.empty[A, N].withDefaultValue(ring.zero))(
          (m, x) => m + (x -> ring.plus(m(x), ring.one)),
          _ + _)
      }
    }

}