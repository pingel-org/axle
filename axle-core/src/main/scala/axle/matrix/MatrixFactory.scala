package axle.matrix

import spire.implicits.IntAlgebra
import spire.implicits.eqOps

trait MatrixModule {

  /**
   * Type Parameters:
   *
   * T element type
   * S storage type
   * M subtype of Matrix that is backed by storage S and has elements of type T
   */

  type C[T]

  implicit val convertDouble: C[Double]
  implicit val convertInt: C[Int]
  implicit val convertBoolean: C[Boolean]

  //  implicit val formatDouble = (d: Double) => "%.6f".format(d)
  //  implicit val formatInt = (i: Int) => i.toString
  //  implicit val formatBoolean = (b: Boolean) => b.toString

  type Matrix[T] <: MatrixLike[T]

  trait MatrixLike[T] { this: Matrix[T] =>

    type S

    def storage: S

    def rows: Int
    def columns: Int
    def length: Int

    def apply(i: Int, j: Int): T
    def apply(rs: Seq[Int], cs: Seq[Int]): Matrix[T]

    // def update(i: Int, j: Int, v: T): Unit
    def toList(): List[T]

    def column(j: Int): Matrix[T]
    def row(i: Int): Matrix[T]

    def isEmpty: Boolean
    def isRowVector: Boolean
    def isColumnVector: Boolean
    def isVector: Boolean
    def isSquare: Boolean
    def isScalar: Boolean
    // resize
    // reshape

    def dup: Matrix[T]
    def negate: Matrix[T]
    def transpose: Matrix[T]
    def diag: Matrix[T]
    def invert: Matrix[T]
    def ceil: Matrix[Int]
    def floor: Matrix[Int]
    def log: Matrix[Double]
    def log10: Matrix[Double]
    def fullSVD: (Matrix[T], Matrix[T], Matrix[T]) // (U, S, V) such that A = U * diag(S) * V' // TODO: all Matrix[Double] ?
    // def truth: M[Boolean]

    def pow(p: Double): Matrix[T]

    def addScalar(x: T): Matrix[T]
    def addAssignment(r: Int, c: Int, v: T): Matrix[T]
    def subtractScalar(x: T): Matrix[T]
    def multiplyScalar(x: T): Matrix[T]
    def divideScalar(x: T): Matrix[T]
    def mulRow(i: Int, x: T): Matrix[T]
    def mulColumn(i: Int, x: T): Matrix[T]

    // Operations on pairs of matrices

    def addMatrix(other: Matrix[T]): Matrix[T]
    def subtractMatrix(other: Matrix[T]): Matrix[T]
    def multiplyMatrix(other: Matrix[T]): Matrix[T]
    def mulPointwise(other: Matrix[T]): Matrix[T]
    def divPointwise(other: Matrix[T]): Matrix[T]
    def concatenateHorizontally(right: Matrix[T]): Matrix[T]
    def concatenateVertically(under: Matrix[T]): Matrix[T]
    def solve(B: Matrix[T]): Matrix[T] // returns X, where this === A and A x X = B

    // Operations on a matrix and a column/row vector

    def addRowVector(row: Matrix[T]): Matrix[T]
    def addColumnVector(column: Matrix[T]): Matrix[T]
    def subRowVector(row: Matrix[T]): Matrix[T]
    def subColumnVector(column: Matrix[T]): Matrix[T]
    def mulRowVector(row: Matrix[T]): Matrix[T]
    def mulColumnVector(column: Matrix[T]): Matrix[T]
    def divRowVector(row: Matrix[T]): Matrix[T]
    def divColumnVector(column: Matrix[T]): Matrix[T]

    // Operations on pair of matrices that return M[Boolean]

    def lt(other: Matrix[T]): Matrix[Boolean]
    def le(other: Matrix[T]): Matrix[Boolean]
    def gt(other: Matrix[T]): Matrix[Boolean]
    def ge(other: Matrix[T]): Matrix[Boolean]
    def eq(other: Matrix[T]): Matrix[Boolean]
    def ne(other: Matrix[T]): Matrix[Boolean]

    def and(other: Matrix[T]): Matrix[Boolean]
    def or(other: Matrix[T]): Matrix[Boolean]
    def xor(other: Matrix[T]): Matrix[Boolean]
    def not: Matrix[Boolean]

    // various mins and maxs

    def max: T
    def argmax: (Int, Int)
    def min: T
    def argmin: (Int, Int)

    def rowSums: Matrix[T]
    def columnSums: Matrix[T]
    def columnMins: Matrix[T]
    def columnMaxs: Matrix[T]
    // def columnArgmins
    // def columnArgmaxs

    def columnMeans: Matrix[T]
    def sortColumns: Matrix[T]

    def rowMins: Matrix[T]
    def rowMaxs: Matrix[T]
    def rowMeans: Matrix[T]
    def sortRows: Matrix[T]

    // Higher-order methods

    def map[B: C](f: T => B): Matrix[B]

    def flatMapColumns[A: C](f: Matrix[T] => Matrix[A]): Matrix[A]

    def foldLeft[A](zero: Matrix[A])(f: (Matrix[A], Matrix[T]) => Matrix[A]): Matrix[A] =
      (0 until columns).foldLeft(zero)((m: Matrix[A], c: Int) => f(m, column(c)))

    def foldTop[A](zero: Matrix[A])(f: (Matrix[A], Matrix[T]) => Matrix[A]): Matrix[A] =
      (0 until rows).foldLeft(zero)((m: Matrix[A], r: Int) => f(m, row(r)))

    // aliases

    def t = transpose
    def tr = transpose
    def inv = invert

    def scalar = {
      assert(isScalar)
      this(0, 0)
    }

    def +(x: T) = addScalar(x)
    // def +=(x: T) = addi(x)
    def +(other: Matrix[T]) = addMatrix(other)
    // def +=(other: Matrix[T]) = addMatrixi(other)
    def +(rc2v: ((Int, Int), T)) = addAssignment(rc2v._1._1, rc2v._1._2, rc2v._2)

    def -(x: T) = subtractScalar(x)
    // def -=(x: T) = subtracti(x)
    def -(other: Matrix[T]) = subtractMatrix(other)
    // def -=(other: Matrix[T]) = subtractMatrixi(other)

    def *(x: T) = multiplyScalar(x)
    // def *=(x: T) = multiplyi(x)
    def ⨯(other: Matrix[T]) = multiplyMatrix(other)
    def mm(other: Matrix[T]) = multiplyMatrix(other)

    def /(x: T) = divideScalar(x)
    // def /=(x: T) = dividei(x)

    def +|+(right: Matrix[T]) = concatenateHorizontally(right)
    def +/+(under: Matrix[T]) = concatenateVertically(under)
    def aside(right: Matrix[T]) = concatenateHorizontally(right)
    def atop(under: Matrix[T]) = concatenateVertically(under)

    def <(other: Matrix[T]) = lt(other)
    def <=(other: Matrix[T]) = le(other)
    def ≤(other: Matrix[T]) = le(other)
    def >(other: Matrix[T]) = gt(other)
    def >=(other: Matrix[T]) = ge(other)
    def ≥(other: Matrix[T]) = ge(other)
    def ==(other: Matrix[T]) = eq(other)
    def !=(other: Matrix[T]) = ne(other)
    def ≠(other: Matrix[T]) = ne(other)
    def &(other: Matrix[T]) = and(other)
    def ∧(other: Matrix[T]) = and(other)
    def |(other: Matrix[T]) = or(other)
    def ∨(other: Matrix[T]) = or(other)
    def ⊕(other: Matrix[T]) = xor(other)
    def ⊻(other: Matrix[T]) = xor(other)
    def ! = not
    def ~ = not
    def ¬ = not

  }

  def zeros[T: C](m: Int, n: Int): Matrix[T]

  def ones[T: C](m: Int, n: Int): Matrix[T]

  def eye[T: C](n: Int): Matrix[T]

  def matrix[T: C](m: Int, n: Int, values: Array[T]): Matrix[T]

  def matrix[T: C](m: Int, n: Int, topleft: => T, left: Int => T, top: Int => T, fill: (Int, Int, T, T, T) => T): Matrix[T]

  def matrix[T: C](m: Int, n: Int, f: (Int, Int) => T): Matrix[T]

  /**
   * Hilbert matrix
   */
  def hilb(n: Int) = matrix[Double](n, n, (r: Int, c: Int) => 1D / (r + c + 1))

  def median(m: Matrix[Double]): Matrix[Double] = {
    val sorted = m.sortColumns
    if (m.rows % 2 === 0) {
      (sorted.row(m.rows / 2 - 1) + sorted.row(m.rows / 2)) / 2.0
    } else {
      sorted.row(m.rows / 2)
    }
  }

  def centerRows(m: Matrix[Double]): Matrix[Double]
  def centerColumns(m: Matrix[Double]): Matrix[Double]

  def rowRange(m: Matrix[Double]): Matrix[Double]
  def columnRange(m: Matrix[Double]): Matrix[Double]

  def sumsq(m: Matrix[Double]): Matrix[Double]

  def cov(m: Matrix[Double]): Matrix[Double]

  def std(m: Matrix[Double]): Matrix[Double]

  def zscore(m: Matrix[Double]): Matrix[Double]

  def pca(Xnorm: Matrix[Double], cutoff: Double = 0.95): (Matrix[Double], Matrix[Double])

  def numComponentsForCutoff(s: Matrix[Double], cutoff: Double): Int

}
