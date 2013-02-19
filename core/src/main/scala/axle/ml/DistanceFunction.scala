package axle.ml

import spire.math._
import spire.implicits._
import spire.algebra._
import axle.matrix._
import axle._

object DistanceFunctionModule {

  // TODO: distance calcs could assert(r1.isRowVector && r2.isRowVector && r1.length == r2.length)

  import JblasMatrixModule._

  type RowVector = Matrix[Double]

  lazy val manhattan = new MetricSpace[RowVector, Double] {

    def distance(r1: RowVector, r2: RowVector) = (r1 - r2).map(math.abs(_)).toList.sum
  }

  def rvDot(x: RowVector, y: RowVector): Double = (0 until x.columns).map(i => x(0, i) * y(0, i)).sum

  def rvNorm(r: RowVector) = math.sqrt(rvDot(r, r))

  /**
   * Euclidean space
   *
   *    n = num columns in row vectors
   *
   *    distance(r1, r2) = norm(r1 - r2)
   *
   */

  def euclidian(n: Int) = new NormedVectorSpace[RowVector, Double] {

    def negate(x: RowVector): RowVector = x.negate

    def zero: RowVector = zeros[Double](1, n)

    def plus(x: RowVector, y: RowVector): RowVector = x + y

    def timesl(r: Double, v: RowVector): RowVector = v * r

    implicit def scalar: Field[Double] = Field.DoubleIsField

    def norm(r: RowVector) = rvNorm(r)
  }

  /**
   *
   * cosine space
   *
   *   n = num columns in row vectors
   *
   *   distance(r1: RowVector, r2: RowVector) = 1.0 - (rvDot(r1, r2) / (norm(r1) * norm(r2)))
   *
   */

  def cosine(n: Int) = new NormedInnerProductSpace[RowVector, Double] {

    def nroot = NRoot.DoubleIsNRoot

    val _space = new InnerProductSpace[RowVector, Double] {

      def negate(x: RowVector) = x.negate

      def zero = zeros[Double](1, n)

      def plus(x: RowVector, y: RowVector) = x + y

      def timesl(r: Double, v: RowVector) = v * r

      implicit def scalar = Field.DoubleIsField

      def dot(v: RowVector, w: RowVector) = rvDot(v, w)
    }

    def space = _space
  }

}
