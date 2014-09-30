package axle.ml.distance

import scala.math.abs
import scala.math.sqrt

import axle.matrix.MatrixModule
import spire.algebra.Field
import spire.algebra.InnerProductSpace
import spire.algebra.MetricSpace
import spire.algebra.NormedVectorSpace
import spire.implicits.DoubleAlgebra

trait MatrixDistance extends MatrixModule {

  class Manhattan extends MetricSpace[Matrix[Double], Double] {

    def distance(r1: Matrix[Double], r2: Matrix[Double]): Double = (r1 - r2).map(abs).toList.sum
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

  case class Cosine(n: Int) extends InnerProductSpace[Matrix[Double], Double] {

    def negate(x: Matrix[Double]): Matrix[Double] = x.negate

    def zero: Matrix[Double] = zeros[Double](1, n)

    def plus(x: Matrix[Double], y: Matrix[Double]): Matrix[Double] = x + y

    def timesl(r: Double, v: Matrix[Double]): Matrix[Double] = v * r

    def scalar: Field[Double] = DoubleAlgebra

    def dot(v: Matrix[Double], w: Matrix[Double]): Double = v.mulPointwise(w).rowSums.scalar

  }

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

}