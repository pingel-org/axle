package axle.ml.distance

import scala.math.abs
import scala.math.sqrt

import axle.algebra.Matrix
import axle.syntax.matrix._
import spire.algebra.Field
import spire.algebra.InnerProductSpace
import spire.algebra.MetricSpace
import spire.algebra.NormedVectorSpace
import spire.implicits.DoubleAlgebra

case class Manhattan[M[_]: Matrix]() extends MetricSpace[M[Double], Double] {

  def distance(r1: M[Double], r2: M[Double]): Double = (r1 - r2).map(abs).toList.sum
}

/**
 *
 * Cosine space
 *
 * @param n = num columns in row vectors
 *
 * distance(r1, r2) = 1.0 - abs(rvDot(r1, r2) / (norm(r1) * norm(r2)))
 *
 * TODO: distance calcs could assert(r1.isRowVector && r2.isRowVector && r1.length === r2.length)
 *
 */

case class Cosine[M[_]: Matrix](n: Int) extends InnerProductSpace[M[Double], Double] {

  val witness = implicitly[Matrix[M]]

  def negate(x: M[Double]): M[Double] = x.negate

  def zero: M[Double] = witness.zeros[Double](1, n)

  def plus(x: M[Double], y: M[Double]): M[Double] = x + y

  def timesl(r: Double, v: M[Double]): M[Double] = v * r

  def scalar: Field[Double] = DoubleAlgebra

  def dot(v: M[Double], w: M[Double]): Double = v.mulPointwise(w).rowSums.scalar

}

/**
 * Euclidean space
 *
 *   n = num columns in row vectors
 *
 *   distance(r1, r2) = norm(r1 - r2)
 *
 */

case class Euclidian[M[_]: Matrix](n: Int) extends NormedVectorSpace[M[Double], Double] {

  val witness = implicitly[Matrix[M]]

  def negate(x: M[Double]): M[Double] = x.negate

  def zero: M[Double] = witness.zeros[Double](1, n)

  def plus(x: M[Double], y: M[Double]): M[Double] = x + y

  def timesl(r: Double, v: M[Double]): M[Double] = v * r

  def scalar: Field[Double] = DoubleAlgebra

  def norm(r: M[Double]): Double = sqrt(r.mulPointwise(r).rowSums.scalar)

}

