package axle.ml.distance

import scala.math.abs

import axle.algebra.LinearAlgebra
import axle.syntax.endofunctor.endofunctorOps
import axle.syntax.linearalgebra.matrixOps
import spire.algebra.AdditiveMonoid
import spire.algebra.MetricSpace
import spire.implicits.additiveGroupOps

case class Manhattan[M, R, C, D](implicit la: LinearAlgebra[M, R, C, D], subSpace: MetricSpace[D, D], add: AdditiveMonoid[D])
  extends MetricSpace[M, D] {

  def distance(r1: M, r2: M): D = {
    val subDistances = r1.zipWith(subSpace.distance)(r2)
    subDistances.reduceToScalar(add.additive.op _)
  }

}
