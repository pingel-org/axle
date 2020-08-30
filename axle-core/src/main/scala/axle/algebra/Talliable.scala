package axle.algebra

import scala.annotation.implicitNotFound
import spire.algebra.Ring
import spire.algebra.CRing


@implicitNotFound("Witness not found for Talliable[${F}]")
trait Talliable[F[_]] {

  def tally[A, N](ts: F[A])(implicit ring: Ring[N]): Map[A, N]
}

object Talliable {

  implicit val tallySeq =
    new Talliable[Seq] {

      def tally[A, N](xs: Seq[A])(implicit ring: CRing[N]): Map[A, N] = {
        val mapCR = new spire.std.MapCRng[A, N]()
        xs.aggregate(Map.empty[A, N].withDefaultValue(ring.zero))(
          (m, x) => m + (x -> ring.plus(m(x), ring.one)),
          mapCR.plus)
      }
    }

  implicit val tallyList =
    new Talliable[List] {

      def tally[A, N](xs: List[A])(implicit ring: CRing[N]): Map[A, N] = {
        val mapCR = new spire.std.MapCRng[A, N]()
        xs.aggregate(Map.empty[A, N].withDefaultValue(ring.zero))(
          (m, x) => m + (x -> ring.plus(m(x), ring.one)),
          mapCR.plus)
      }
    }

}
