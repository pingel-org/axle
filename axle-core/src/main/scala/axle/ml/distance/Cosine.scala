package axle.ml.distance

import spire.algebra._
import spire.math._
import spire.implicits._
import axle.matrix.JblasMatrixModule._

/**
 *
 * Cosine space
 *
 * @param n = num columns in row vectors
 *
 * distance(r1, r2) = 1.0 - abs(rvDot(r1, r2) / (norm(r1) * norm(r2)))
 *
 * TODO: distance calcs could assert(r1.isRowVector && r2.isRowVector && r1.length == r2.length)
 *
 */

case class Cosine(n: Int) extends InnerProductSpace[Matrix[Double], Double] {

  def negate(x: Matrix[Double]) = x.negate

  def zero = zeros[Double](1, n)

  def plus(x: Matrix[Double], y: Matrix[Double]) = x + y

  def timesl(r: Double, v: Matrix[Double]) = v * r

  def scalar = DoubleAlgebra

  def dot(v: Matrix[Double], w: Matrix[Double]) = v.mulPointwise(w).rowSums().scalar

}
