package axle.matrix

object ArrayMatrixFactory extends ArrayMatrixFactory {}

/**
 *
 * Discussion of ClassManifest here:
 *
 * http://www.scala-lang.org/docu/files/collections-api/collections_38.html
 *
 */

abstract class ArrayMatrixFactory extends MatrixFactory {

  type M[T] = ArrayMatrix[T]

  class ArrayMatrixImpl[T: ClassManifest](_storage: Array[T], nRows: Int, nColumns: Int) extends ArrayMatrix[T] {

    def storage = _storage

    def rows = nRows
    def columns = nColumns
    def length = storage.length

    def apply(r: Int, c: Int): T = _storage(r * nColumns + c)
    def update(r: Int, c: Int, v: T) = _storage(r * nColumns + c) = v
    def toList(): List[T] = _storage.toList

    def column(c: Int) = {
      val result = matrix(new Array[T](nRows), nRows, 1)
      for (r <- 0 until nRows) {
    	  result(r, 0) = this(r, c)
      }
      result
    }

    def row(r: Int) = {
      val result = matrix(new Array[T](nColumns), 1, nColumns)
      for (c <- 0 until nColumns) {
        result(0, c) = this(r, c)
      }
      result
    }

    def isEmpty(): Boolean = false // TODO
    def isRowVector(): Boolean = columns == 1
    def isColumnVector(): Boolean = rows == 1
    def isVector(): Boolean = isRowVector || isColumnVector
    def isSquare(): Boolean = columns == rows
    def isScalar(): Boolean = isRowVector && isColumnVector

    def dup(): M[T] = matrix(_storage.clone, nRows, nColumns)
    def negate(): M[T] = null // TODO
    def transpose(): M[T] = null // TODO
    def diag(): M[T] = null // TODO
    def invert(): M[T] = null // TODO
    def ceil(): M[T] = null // TODO
    def floor(): M[T] = null // TODO
    def log(): M[T] = null // TODO
    def log10(): M[T] = null // TODO
    def fullSVD(): (M[T], M[T], M[T]) = null // TODO doesn't really make sense

    def pow(p: Double): M[T] = null // TODO

    def addScalar(x: T): M[T] = null // TODO
    def subtractScalar(x: T): M[T] = null // TODO
    def multiplyScalar(x: T): M[T] = null // TODO
    def divideScalar(x: T): M[T] = null // TODO
    def mulRow(i: Int, x: T): M[T] = null // TODO
    def mulColumn(i: Int, x: T): M[T] = null // TODO

    // Operations on pairs of matrices

    def addMatrix(other: M[T]): M[T] = null // TODO
    def subtractMatrix(other: M[T]): M[T] = null // TODO
    def multiplyMatrix(other: M[T]): M[T] = null // TODO
    def mulPointwise(other: M[T]) = null // TODO
    def divPointwise(other: M[T]) = null // TODO
    def concatenateHorizontally(right: M[T]): M[T] = null // TODO
    def concatenateVertically(under: M[T]): M[T] = null // TODO
    def solve(B: M[T]): M[T] = null // TODO // returns X, where this == A and A x X = B

    // Operations on a matrix and a column/row vector

    def addRowVector(row: M[T]): M[T] = null // TODO
    def addColumnVector(column: M[T]): M[T] = null // TODO
    def subRowVector(row: M[T]): M[T] = null // TODO
    def subColumnVector(column: M[T]): M[T] = null // TODO

    // Operations on pair of matrices that return M[Boolean]

    def lt(other: M[T]): Matrix[Boolean] = null // TODO
    def le(other: M[T]): Matrix[Boolean] = null // TODO
    def gt(other: M[T]): Matrix[Boolean] = null // TODO
    def ge(other: M[T]): Matrix[Boolean] = null // TODO
    def eq(other: M[T]): Matrix[Boolean] = null // TODO
    def ne(other: M[T]): Matrix[Boolean] = null // TODO

    def and(other: M[T]): Matrix[Boolean] = null // TODO
    def or(other: M[T]): Matrix[Boolean] = null // TODO
    def xor(other: M[T]): Matrix[Boolean] = null // TODO
    def not(): Matrix[Boolean] = null // TODO

    // various mins and maxs

    def max(): T = null.asInstanceOf[T] // TODO
    def argmax(): (Int, Int) = null // TODO
    def min(): T = null.asInstanceOf[T] // TODO
    def argmin(): (Int, Int) = null // TODO
    def columnMins(): M[T] = null // TODO
    def columnMaxs(): M[T] = null // TODO

    // In-place versions

    def ceili(): Unit = {} // TODO
    def floori(): Unit = {} // TODO
    def powi(p: Double): Unit = {} // TODO

    def addi(x: T): Unit = {} // TODO
    def subtracti(x: T): Unit = {} // TODO
    def multiplyi(x: T): Unit = {} // TODO
    def dividei(x: T): Unit = {} // TODO

    def addMatrixi(other: M[T]): Unit = {} // TODO
    def subtractMatrixi(other: M[T]): Unit = {} // TODO
    def addiRowVector(row: M[T]): Unit = {} // TODO
    def addiColumnVector(column: M[T]): Unit = {} // TODO
    def subiRowVector(row: M[T]): Unit = {} // TODO
    def subiColumnVector(column: M[T]): Unit = {} // TODO

  }

  trait ArrayMatrix[T] extends Matrix[T] {

    type S = Array[T]

    def toList(): List[T]

  }

  def matrix[T: ClassManifest](arr: Array[T], r: Int, c: Int): ArrayMatrix[T] = new ArrayMatrixImpl(arr, r, c)

  def matrix[T: ClassManifest](r: Int, c: Int, default: T): ArrayMatrix[T] = {
    val length = r * c
    val arr = new Array[T](length)
    0.until(length).map(i => arr(i) = default)
    matrix(arr, r, c)
  }

}
