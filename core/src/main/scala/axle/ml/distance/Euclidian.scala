package axle.ml.distance

import spire.algebra._
import spire.math._
import spire.implicits._
import axle.matrix.JblasMatrixModule._

import math.sqrt

/**
 * Euclidean space
 *
 *   n = num columns in row vectors
 *
 *   distance(r1, r2) = norm(r1 - r2)
 *
 */

case class Euclidian(n: Int) extends NormedVectorSpace[Matrix[Double], Double] {

  def negate(x: Matrix[Double]) = x.negate

  def zero = zeros[Double](1, n)

  def plus(x: Matrix[Double], y: Matrix[Double]) = x + y

  def timesl(r: Double, v: Matrix[Double]) = v * r

  implicit def scalar = Field.DoubleIsField

  def norm(r: Matrix[Double]) = sqrt(r.mulPointwise(r).rowSums().scalar)

}
