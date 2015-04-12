package axle.algebra

import scala.annotation.implicitNotFound

@implicitNotFound("No member of typeclass FoldLeft found for type ${F}")
trait FoldLeft[F[_]] {

  def foldLeft[A, B](xs: F[A], zero: B, op: (B, A) => B): B

}

object FoldLeft {

  implicit val foldLeftList = new FoldLeft[List] {
    def foldLeft[A, B](xs: List[A], zero: B, op: (B, A) => B): B = xs.foldLeft(zero)(op)
  }

}
