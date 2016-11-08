package axle.algebra

import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for FoldLeft[${F}]")
trait FoldLeft[F[_]] {

  def foldLeft[A, B](xs: F[A], zero: B, op: (B, A) => B): B

}

object FoldLeft {

  final def apply[F[_]: FoldLeft]: FoldLeft[F] = implicitly[FoldLeft[F]]

  implicit val foldLeftList = new FoldLeft[List] {
    def foldLeft[A, B](xs: List[A], zero: B, op: (B, A) => B): B = xs.foldLeft(zero)(op)
  }

}
