package axle

import axle.algebra.Zipper
import axle.algebra.Cephalate
import axle.syntax.zipper.zipperOps
import axle.syntax.cephalate.cephalateOps

package object nlp {

  def bigrams[T, F[_]: Zipper: Cephalate](xs: F[T]): F[(T, T)] =
    xs.zip(xs.tail)

}
