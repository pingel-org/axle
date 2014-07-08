package axle.ml.distance

import scala.math.sqrt

import axle.matrix.JblasMatrixModule.Matrix
import axle.matrix.JblasMatrixModule.convertDouble
import axle.matrix.JblasMatrixModule.zeros
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

case class Euclidian(n: Int) extends NormedVectorSpace[Matrix[Double], Double] {

  def negate(x: Matrix[Double]): Matrix[Double] = x.negate

  def zero: Matrix[Double] = zeros[Double](1, n)

  def plus(x: Matrix[Double], y: Matrix[Double]): Matrix[Double] = x + y

  def timesl(r: Double, v: Matrix[Double]): Matrix[Double] = v * r

  def scalar: Field[Double] = DoubleAlgebra

  def norm(r: Matrix[Double]): Double = sqrt(r.mulPointwise(r).rowSums.scalar)

}
