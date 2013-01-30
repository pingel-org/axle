package axle.matrix

import axle._

object ArrayMatrixFactory extends ArrayMatrixFactory {}

/**
 *
 * Discussion of ClassManifest here:
 *
 * http://www.scala-lang.org/docu/files/collections-api/collections_38.html
 *
 */

abstract class ArrayMatrixFactory extends MatrixFactory {

  factory =>

  type M[T] = ArrayMatrix[T]

  type E[T] = Unit

  class ArrayMatrixImpl[T: ClassManifest](_storage: Array[T], nRows: Int, nColumns: Int) extends ArrayMatrix[T] {

    def storage = _storage

    def rows = nRows
    def columns = nColumns
    def length = storage.length

    def apply(r: Int, c: Int): T = _storage(r * nColumns + c)
    // def updat(r: Int, c: Int, v: T) = _storage(r * nColumns + c) = v
    def toList(): List[T] = _storage.toList

    def column(c: Int) = matrix((0 until nRows).map(this(_, c)).toArray, nRows, 1)

    def row(r: Int) = matrix((0 until nColumns).map(this(r, _)).toArray, 1, nColumns)

    def isEmpty(): Boolean = false // TODO
    def isRowVector(): Boolean = columns == 1
    def isColumnVector(): Boolean = rows == 1
    def isVector(): Boolean = isRowVector || isColumnVector
    def isSquare(): Boolean = columns == rows
    def isScalar(): Boolean = isRowVector && isColumnVector

    def dup(): M[T] = matrix(_storage.clone, nRows, nColumns)
    def negate(): M[T] = ???
    def transpose(): M[T] = ???
    def diag(): M[T] = ???
    def invert(): M[T] = ???
    def ceil(): M[T] = ???
    def floor(): M[T] = ???
    def log(): M[T] = ???
    def log10(): M[T] = ???
    def fullSVD(): (M[T], M[T], M[T]) = ??? doesn't really make sense

    def pow(p: Double): M[T] = ???

    def addScalar(x: T): M[T] = ???
    def addAssignment(r: Int, c: Int, v: T): M[T] = {
      val length = rows * columns
      val arr = storage.clone
      arr(r * columns + c) = v
      matrix(arr, rows, columns)
    }
    def subtractScalar(x: T): M[T] = ???
    def multiplyScalar(x: T): M[T] = ???
    def divideScalar(x: T): M[T] = ???
    def mulRow(i: Int, x: T): M[T] = ???
    def mulColumn(i: Int, x: T): M[T] = ???

    // Operations on pairs of matrices

    def addMatrix(other: M[T]): M[T] = ???
    def subtractMatrix(other: M[T]): M[T] = ???
    def multiplyMatrix(other: M[T]): M[T] = ???
    def mulPointwise(other: M[T]) = ???
    def divPointwise(other: M[T]) = ???
    def concatenateHorizontally(right: M[T]): M[T] = ???
    def concatenateVertically(under: M[T]): M[T] = ???
    def solve(B: M[T]): M[T] = ??? // returns X, where this == A and A x X = B

    // Operations on a matrix and a column/row vector

    def addRowVector(row: M[T]): M[T] = ???
    def addColumnVector(column: M[T]): M[T] = ???
    def subRowVector(row: M[T]): M[T] = ???
    def subColumnVector(column: M[T]): M[T] = ???

    // Operations on pair of matrices that return M[Boolean]

    def lt(other: M[T]): Matrix[Boolean] = ???
    def le(other: M[T]): Matrix[Boolean] = ???
    def gt(other: M[T]): Matrix[Boolean] = ???
    def ge(other: M[T]): Matrix[Boolean] = ???
    def eq(other: M[T]): Matrix[Boolean] = ???
    def ne(other: M[T]): Matrix[Boolean] = ???

    def and(other: M[T]): Matrix[Boolean] = ???
    def or(other: M[T]): Matrix[Boolean] = ???
    def xor(other: M[T]): Matrix[Boolean] = ???
    def not(): Matrix[Boolean] = ???

    // various mins and maxs

    def max(): T = null.asInstanceOf[T] // TODO
    def argmax(): (Int, Int) = ???
    def min(): T = null.asInstanceOf[T] // TODO
    def argmin(): (Int, Int) = ???
    def columnMins(): M[T] = ???
    def columnMaxs(): M[T] = ???

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

    // higher order fuctions

    def map[B](f: T => B)(implicit elementAdapter: E[B]): M[B] = ???
    // matrix(rows, columns, storage.map(f(_)))

    def flatMapColumns[A](f: M[T] => M[A])(implicit elementAdapter: E[A]): M[A] = ???

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

  def zeros[T](m: Int, n: Int)(implicit elementAdapter: E[T]): M[T] = ???

  def matrix[T](m: Int, n: Int, values: Array[T])(implicit elementAdapter: E[T]): M[T] = ???

  def matrix[T](m: Int, n: Int, topleft: => T, left: Int => T, top: Int => T, fill: (Int, Int, T, T, T) => T)(implicit elementAdapter: E[T]): M[T] = ???

  def matrix[T](m: Int, n: Int, f: (Int, Int) => T)(implicit elementAdapter: E[T]): M[T] = ???

}
