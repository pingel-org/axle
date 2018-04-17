package axle.algebra.distance

import spire.algebra.AdditiveMonoid
import spire.algebra.MetricSpace
import axle.algebra.LinearAlgebra
import axle.syntax.linearalgebra.matrixOps

class Manhattan[M, R, C, D](
  implicit
  la:       LinearAlgebra[M, R, C, D],
  subSpace: MetricSpace[D, D],
  add:      AdditiveMonoid[D])
  extends MetricSpace[M, D] {

  def distance(r1: M, r2: M): D = {

    val subDistances = r1.zipWith(subSpace.distance)(r2)

    subDistances.reduceToScalar(add.additive.combine _)
  }

}
