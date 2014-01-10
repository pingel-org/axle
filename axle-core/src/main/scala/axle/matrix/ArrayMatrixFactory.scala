package axle.matrix

import axle._

/**
 *
 * Discussion of ClassManifest here:
 *
 * http://www.scala-lang.org/docu/files/collections-api/collections_38.html
 *
 */

/*
trait ArrayMatrixModule extends MatrixModule {

  implicit val convertCM: C[T] = (v: ClassManifest[T]) => v

  type C[T] = ClassManifest[T] => ClassManifest[T]

  class Matrix[T](_storage: Array[T], nRows: Int, nColumns: Int) extends MatrixLike[T] {

    def storage = _storage

    type S = Array[T]

    def rows = nRows
    def columns = nColumns
    def length = storage.length

    def apply(r: Int, c: Int): T = _storage(r * nColumns + c)

    def apply(rs: Seq[Int], cs: Seq[Int]): Matrix[T] = ???

    // def updat(r: Int, c: Int, v: T) = _storage(r * nColumns + c) = v

    def toList: List[T] = _storage.toList

    def column(c: Int) = matrix((0 until nRows).map(this(_, c)).toArray, nRows, 1)

    def row(r: Int) = matrix((0 until nColumns).map(this(r, _)).toArray, 1, nColumns)

    def isEmpty: Boolean = ???
    def isRowVector: Boolean = columns === 1
    def isColumnVector: Boolean = rows === 1
    def isVector: Boolean = isRowVector || isColumnVector
    def isSquare: Boolean = columns === rows
    def isScalar: Boolean = isRowVector && isColumnVector

    def dup: Matrix[T] = matrix(_storage.clone, nRows, nColumns)
    def negate: Matrix[T] = ???
    def transpose: Matrix[T] = ???
    def diag: Matrix[T] = ???
    def invert: Matrix[T] = ???
    def ceil: Matrix[Int] = ???
    def floor: Matrix[Int] = ???
    def log: Matrix[Double] = ???
    def log10: Matrix[Double] = ???
    def fullSVD: (Matrix[T], Matrix[T], Matrix[T]) = ???

    def pow(p: Double): Matrix[T] = ???

    def addScalar(x: T): Matrix[T] = ???
    def addAssignment(r: Int, c: Int, v: T): Matrix[T] = {
      val length = rows * columns
      val arr = storage.clone
      arr(r * columns + c) = v
      matrix(arr, rows, columns)
    }
    def subtractScalar(x: T): Matrix[T] = ???
    def multiplyScalar(x: T): Matrix[T] = ???
    def divideScalar(x: T): Matrix[T] = ???
    def mulRow(i: Int, x: T): Matrix[T] = ???
    def mulColumn(i: Int, x: T): Matrix[T] = ???

    // Operations on pairs of matrices

    def addMatrix(other: Matrix[T]): Matrix[T] = ???
    def subtractMatrix(other: Matrix[T]): Matrix[T] = ???
    def multiplyMatrix(other: Matrix[T]): Matrix[T] = ???
    def mulPointwise(other: Matrix[T]) = ???
    def divPointwise(other: Matrix[T]) = ???
    def concatenateHorizontally(right: Matrix[T]): Matrix[T] = ???
    def concatenateVertically(under: Matrix[T]): Matrix[T] = ???
    def solve(B: Matrix[T]): Matrix[T] = ??? // returns X, where this === A and A x X = B

    // Operations on a matrix and a column/row vector

    def addRowVector(row: Matrix[T]): Matrix[T] = ???
    def addColumnVector(column: Matrix[T]): Matrix[T] = ???
    def subRowVector(row: Matrix[T]): Matrix[T] = ???
    def subColumnVector(column: Matrix[T]): Matrix[T] = ???

    // Operations on pair of matrices that return M[Boolean]

    def lt(other: Matrix[T]): Matrix[Boolean] = ???
    def le(other: Matrix[T]): Matrix[Boolean] = ???
    def gt(other: Matrix[T]): Matrix[Boolean] = ???
    def ge(other: Matrix[T]): Matrix[Boolean] = ???
    def eq(other: Matrix[T]): Matrix[Boolean] = ???
    def ne(other: Matrix[T]): Matrix[Boolean] = ???

    def and(other: Matrix[T]): Matrix[Boolean] = ???
    def or(other: Matrix[T]): Matrix[Boolean] = ???
    def xor(other: Matrix[T]): Matrix[Boolean] = ???
    def not: Matrix[Boolean] = ???

    // various mins and maxs

    def max: T = ???
    def argmax: (Int, Int) = ???
    def min: T = ???
    def argmin: (Int, Int) = ???
    def columnMins: Matrix[T] = ???
    def columnMaxs: Matrix[T] = ???

    // In-place versions

    def ceili: Unit = ???
    def floori: Unit = ???
    def powi(p: Double): Unit = ???

    def addi(x: T): Unit = ???
    def subtracti(x: T): Unit = ???
    def multiplyi(x: T): Unit = ???
    def dividei(x: T): Unit = ???

    def addMatrixi(other: Matrix[T]): Unit = ???
    def subtractMatrixi(other: Matrix[T]): Unit = ???
    def addiRowVector(row: Matrix[T]): Unit = ???
    def addiColumnVector(column: Matrix[T]): Unit = ???
    def subiRowVector(row: Matrix[T]): Unit = ???
    def subiColumnVector(column: Matrix[T]): Unit = ???

    def rowSums: Matrix[T] = ???
    def columnSums: Matrix[T] = ???

    // def columnArgmins
    // def columnArgmaxs

    def columnMeans: Matrix[T] = ???
    def sortColumns: Matrix[T] = ???

    def rowMins: Matrix[T] = ???
    def rowMaxs: Matrix[T] = ???
    def rowMeans: Matrix[T] = ???
    def sortRows: Matrix[T] = ???

    // higher order fuctions

    def map[B: C](f: T => B): Matrix[B] = ???
    // matrix(rows, columns, storage.map(f))

    def flatMapColumns[A: C](f: Matrix[T] => Matrix[A]): Matrix[A] = ???

  }

  def matrix[T: ClassManifest](arr: Array[T], r: Int, c: Int): Matrix[T] = new Matrix(arr, r, c)

  def matrix[T: ClassManifest](r: Int, c: Int, default: T): Matrix[T] = {
    val length = r * c
    val arr = new Array[T](length)
    0.until(length).map(i => arr(i) = default)
    matrix(arr, r, c)
  }

  def zeros[T](m: Int, n: Int): Matrix[T] = ???

  def matrix[T](m: Int, n: Int, values: Array[T]): Matrix[T] = ???

  def matrix[T](m: Int, n: Int, topleft: => T, left: Int => T, top: Int => T, fill: (Int, Int, T, T, T) => T): Matrix[T] = ???

  def matrix[T](m: Int, n: Int, f: (Int, Int) => T): Matrix[T] = ???

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
*/
