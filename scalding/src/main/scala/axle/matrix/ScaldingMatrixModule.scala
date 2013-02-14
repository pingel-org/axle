
package axle.matrix

//import com.twitter.algebird._
//import com.twitter.algebird.Operators._
import com.twitter.scalding._
import com.twitter.scalding.mathematics.{ Matrix => ScaldingMatrix }
import axle._
import axle.algebra.FunctionPair

object ScaldingMatrixModule extends ScaldingMatrixModule

trait ScaldingMatrixModule extends MatrixModule {

  type RowT = Int
  type ColT = Int
  type C[T] = FunctionPair[Double, T]

  implicit val convertDouble: C[Double] = new FunctionPair[Double, Double] {
    val forward = (d: Double) => d
    val backward = (t: Double) => t
  }

  implicit val convertInt: C[Int] = new FunctionPair[Double, Int] {
    val forward = (d: Double) => d.toInt
    val backward = (t: Int) => t.toDouble
  }

  implicit val convertBoolean: C[Boolean] = new FunctionPair[Double, Boolean] {
    val forward = (d: Double) => d != 0.0
    val backward = (t: Boolean) => t match { case true => 0.0 case false => 1.0 }
  }

  class Matrix[T: C](_storage: ScaldingMatrix[RowT, ColT, T]) extends MatrixLike[T] {

    val fp = implicitly[C[T]]

    type S = ScaldingMatrix[RowT, ColT, T]

    def storage = _storage

    implicit val format = (t: T) => t.toString // TODO !!!

    def rows() = ???
    def columns() = ???
    def length() = ???

    def apply(i: Int, j: Int): T = ???

    def apply(rs: Seq[Int], cs: Seq[Int]): Matrix[T] = ???

    def toList(): List[T] = ???

    def column(j: Int) = ???
    def row(i: Int) = ???

    def isEmpty() = ???
    def isRowVector() = ???
    def isColumnVector() = ???
    def isVector() = ???
    def isSquare() = ???
    def isScalar() = ???

    def dup() = ???
    def negate() = ???
    def transpose() = ???
    def diag() = ???
    def invert() = ???
    def ceil() = ???
    def floor() = ???
    def log() = ???
    def log10() = ???

    def fullSVD() = ???

    def addScalar(x: T) = ???
    def addAssignment(r: Int, c: Int, v: T): Matrix[T] = ???

    def subtractScalar(x: T) = ???
    def multiplyScalar(x: T) = ???
    def divideScalar(x: T) = ???
    def mulRow(i: Int, x: T) = ???
    def mulColumn(i: Int, x: T) = ???

    def pow(p: Double) = ???

    def addMatrix(other: Matrix[T]) = ???
    def subtractMatrix(other: Matrix[T]) = ???
    def multiplyMatrix(other: Matrix[T]) = ???

    def mulPointwise(other: Matrix[T]) = ???
    def divPointwise(other: Matrix[T]) = ???

    def concatenateHorizontally(right: Matrix[T]) = ???
    def concatenateVertically(under: Matrix[T]) = ???
    def solve(B: Matrix[T]) = ???

    def addRowVector(row: Matrix[T]) = ???
    def addColumnVector(column: Matrix[T]) = ???
    def subRowVector(row: Matrix[T]) = ???
    def subColumnVector(column: Matrix[T]) = ???
    def mulRowVector(row: Matrix[T]) = ???
    def mulColumnVector(column: Matrix[T]) = ???
    def divRowVector(row: Matrix[T]) = ???
    def divColumnVector(column: Matrix[T]) = ???

    def lt(other: Matrix[T]) = ???
    def le(other: Matrix[T]) = ???
    def gt(other: Matrix[T]) = ???
    def ge(other: Matrix[T]) = ???
    def eq(other: Matrix[T]) = ???
    def ne(other: Matrix[T]) = ???
    def and(other: Matrix[T]) = ???
    def or(other: Matrix[T]) = ???
    def xor(other: Matrix[T]) = ???
    def not() = ???

    def max() = ???
    def argmax() = ???
    def min() = ???
    def argmin() = ???
    def rowSums() = ???
    def columnSums() = ???

    def columnMins() = ???
    def columnMaxs() = ???
    def columnMeans() = ???
    def sortColumns() = ???

    def rowMins() = ???
    def rowMaxs() = ???
    def rowMeans() = ???
    def sortRows() = ???

    // higher order methods

    def map[B: C](f: T => B): Matrix[B] = ???

    def flatMapColumns[A: C](f: Matrix[T] => Matrix[A]): Matrix[A] = ???

    override def toString() = ???

    def jblas() = storage
  }

  // methods for creating matrices

  def matrix[T: C](s: ScaldingMatrix[RowT, ColT, T]): Matrix[T] = ???
  def matrix[T: C](r: Int, c: Int, values: Array[T]): Matrix[T] = ???
  def matrix[T: C](m: Int, n: Int, topleft: => T, left: Int => T, top: Int => T, fill: (Int, Int, T, T, T) => T): Matrix[T] = ???
  def matrix[T: C](m: Int, n: Int, f: (Int, Int) => T): Matrix[T] = ???
  def diag[T: C](row: Matrix[T]): Matrix[T] = ???
  def zeros[T: C](m: Int, n: Int): Matrix[T] = ???
  def ones[T: C](m: Int, n: Int): Matrix[T] = ???
  def eye[T: C](n: Int): Matrix[T] = ???
  def I[T: C](n: Int): Matrix[T] = ???
  def rand[T: C](m: Int, n: Int): Matrix[T] = ???
  def randn[T: C](m: Int, n: Int): Matrix[T] = ???
  def falses(m: Int, n: Int): Matrix[Boolean] = ???
  def trues(m: Int, n: Int): Matrix[Boolean] = ???

  // TODO: Int jblas' rand and randn should probably floor the result

  override def median(m: Matrix[Double]): Matrix[Double] = ???

  def centerRows(m: Matrix[Double]): Matrix[Double] = ???
  def centerColumns(m: Matrix[Double]): Matrix[Double] = ???

  def rowRange(m: Matrix[Double]): Matrix[Double] = ???
  def columnRange(m: Matrix[Double]): Matrix[Double] = ???

  def sumsq(m: Matrix[Double]): Matrix[Double] = ???

  def cov(m: Matrix[Double]): Matrix[Double] = ???

  def std(m: Matrix[Double]): Matrix[Double] = ???

  def zscore(m: Matrix[Double]): Matrix[Double] = ???

  def pca(Xnorm: Matrix[Double], cutoff: Double = 0.95): (Matrix[Double], Matrix[Double]) = ???

  def numComponentsForCutoff(s: Matrix[Double], cutoff: Double): Int = ???

}
