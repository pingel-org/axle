package org.pingel.axle.matrix

trait Matrix[E] {

  def rows: Int
  def columns: Int
  def length: Int

  def valueAt(i: Int, j: Int): E
  def setValueAt(i: Int, j: Int, v: E): Unit

  def getColumn(j: Int): Matrix[E]
  def getRow(i: Int): Matrix[E]

  def isEmpty(): Boolean
  def isRowVector(): Boolean
  def isColumnVector(): Boolean
  def isVector(): Boolean
  def isSquare(): Boolean
  def isScalar(): Boolean
  // resize
  // reshape

  def dup(): Matrix[E]
  def negate(): Matrix[E]
  def transpose(): Matrix[E]
  def diag(): Matrix[E]
  def invert(): Matrix[E]
  def ceil(): Matrix[E]
  def floor(): Matrix[E]
  def log(): Matrix[E]
  def log10(): Matrix[E]
  def fullSVD(): (Matrix[E], Matrix[E], Matrix[E]) // (U, S, V) such that A = U * diag(S) * V' // TODO: all Matrix[Double] ?
  // def truth(): M[Boolean]

  def pow(p: Double): Matrix[E]

  def addScalar(x: E): Matrix[E]
  def subtractScalar(x: E): Matrix[E]
  def multiplyScalar(x: E): Matrix[E]
  def divideScalar(x: E): Matrix[E]
  def mulRow(i: Int, x: E): Matrix[E]
  def mulColumn(i: Int, x: E): Matrix[E]

  // Operations on pairs of matrices

  def addMatrix(other: Matrix[E]): Matrix[E]
  def subtractMatrix(other: Matrix[E]): Matrix[E]
  def multiplyMatrix(other: Matrix[E]): Matrix[E]
  def concatenateHorizontally(right: Matrix[E]): Matrix[E]
  def concatenateVertically(under: Matrix[E]): Matrix[E]
  def solve(B: Matrix[E]): Matrix[E] // returns X, where this == A and A x X = B

  // Operations on a matrix and a column/row vector

  def addRowVector(row: Matrix[E]): Matrix[E]
  def addColumnVector(column: Matrix[E]): Matrix[E]
  def subRowVector(row: Matrix[E]): Matrix[E]
  def subColumnVector(column: Matrix[E]): Matrix[E]

  // Operations on pair of matrices that return M[Boolean]

  def lt(other: Matrix[E]): Matrix[Boolean]
  def le(other: Matrix[E]): Matrix[Boolean]
  def gt(other: Matrix[E]): Matrix[Boolean]
  def ge(other: Matrix[E]): Matrix[Boolean]
  def eq(other: Matrix[E]): Matrix[Boolean]
  def ne(other: Matrix[E]): Matrix[Boolean]

  def and(other: Matrix[E]): Matrix[Boolean]
  def or(other: Matrix[E]): Matrix[Boolean]
  def xor(other: Matrix[E]): Matrix[Boolean]
  def not(): Matrix[Boolean]

  // various mins and maxs

  def max(): E
  def argmax(): (Int, Int)
  def min(): E
  def argmin(): (Int, Int)
  def columnMins(): Matrix[E]
  // def columnArgmins
  def columnMaxs(): Matrix[E]
  // def columnArgmaxs

  // In-place versions

  def ceili(): Unit
  def floori(): Unit
  def powi(p: Double): Unit

  def addi(x: E): Unit
  def subtracti(x: E): Unit
  def multiplyi(x: E): Unit
  def dividei(x: E): Unit

  def addMatrixi(other: Matrix[E]): Unit
  def subtractMatrixi(other: Matrix[E]): Unit
  def addiRowVector(row: Matrix[E]): Unit
  def addiColumnVector(column: Matrix[E]): Unit
  def subiRowVector(row: Matrix[E]): Unit
  def subiColumnVector(column: Matrix[E]): Unit

  // aliases

  def t() = transpose()
  def tr() = transpose()
  def inv() = invert()

  def scalar() = {
    assert(isScalar)
    valueAt(0, 0)
  }

  def +(x: E) = addScalar(x)
  def +=(x: E) = addi(x)
  def +(other: Matrix[E]) = addMatrix(other)
  def +=(other: Matrix[E]) = addMatrixi(other)

  def -(x: E) = subtractScalar(x)
  def -=(x: E) = subtracti(x)
  def -(other: Matrix[E]) = subtractMatrix(other)
  def -=(other: Matrix[E]) = subtractMatrixi(other)

  def *(x: E) = multiplyScalar(x)
  def *=(x: E) = multiplyi(x)
  def ⨯(other: Matrix[E]) = multiplyMatrix(other)
  def mm(other: Matrix[E]) = multiplyMatrix(other)

  def /(x: E) = divideScalar(x)
  def /=(x: E) = dividei(x)

  def +|+(right: Matrix[E]) = concatenateHorizontally(right)
  def +/+(under: Matrix[E]) = concatenateVertically(under)

  def <(other: Matrix[E]) = lt(other)
  def <=(other: Matrix[E]) = le(other)
  def ≤(other: Matrix[E]) = le(other)
  def >(other: Matrix[E]) = gt(other)
  def >=(other: Matrix[E]) = ge(other)
  def ≥(other: Matrix[E]) = ge(other)
  def ==(other: Matrix[E]) = eq(other)
  def !=(other: Matrix[E]) = ne(other)
  def ≠(other: Matrix[E]) = ne(other)
  def &(other: Matrix[E]) = and(other)
  def ∧(other: Matrix[E]) = and(other)
  def |(other: Matrix[E]) = or(other)
  def ∨(other: Matrix[E]) = or(other)
  def ⊕(other: Matrix[E]) = xor(other)
  def ⊻(other: Matrix[E]) = xor(other)
  def !() = not()
  def ~() = not()
  def ¬() = not()

}

abstract class MatrixFactory {

  /**
   * Type Parameters:
   *
   * T element type
   * S storage type
   * Matrix subtype of MatrixIntf that is backed by storage S and has elements of type T
   */

  type T
  type S
  type M <: Matrix[T]

  protected def pure(s: S): M
}
