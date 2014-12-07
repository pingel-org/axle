package axle.ml.distance

import scala.math.sqrt

import axle.algebra.Matrix
import axle.syntax.matrix.matrixOps
import spire.algebra.Field
import spire.algebra.NormedVectorSpace
import spire.implicits.DoubleAlgebra

/**
 * Euclidean space
 *
 *   n = num columns in row vectors
 *
 *   distance(r1, r2) = norm(r1 - r2)
 *
 */

case class Euclidian[M[_]](n: Int)(implicit ev: Matrix[M]) extends NormedVectorSpace[M[Double], Double] {

  def negate(x: M[Double]): M[Double] = x.negate

  def zero: M[Double] = ev.zeros[Double](1, n)

  def plus(x: M[Double], y: M[Double]): M[Double] = x + y

  def timesl(r: Double, v: M[Double]): M[Double] = v * r

  def scalar: Field[Double] = DoubleAlgebra

  def norm(r: M[Double]): Double = sqrt(r.mulPointwise(r).rowSums.scalar)

}
