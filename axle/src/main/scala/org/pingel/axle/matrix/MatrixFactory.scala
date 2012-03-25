package org.pingel.axle.matrix


trait MatrixFactory {

  /**
   * Type Parameters:
   *
   * T element type
   * S storage type
   * M subtype of Matrix that is backed by storage S and has elements of type T
   */

  type M <: Matrix

  protected def matrix(s: M#S): M

  trait Matrix {

    type BM <: Matrix { type T = Boolean }
    
    type S // Storage
    type T

    def getStorage: S
    
    def rows: Int
    def columns: Int
    def length: Int

    def valueAt(i: Int, j: Int): T
    def setValueAt(i: Int, j: Int, v: T): Unit

    def getColumn(j: Int): M
    def getRow(i: Int): M

    def isEmpty(): Boolean
    def isRowVector(): Boolean
    def isColumnVector(): Boolean
    def isVector(): Boolean
    def isSquare(): Boolean
    def isScalar(): Boolean
    // resize
    // reshape

    def dup(): M
    def negate(): M
    def transpose(): M
    def diag(): M
    def invert(): M
    def ceil(): M
    def floor(): M
    def log(): M
    def log10(): M
    def fullSVD(): (M, M, M) // (U, S, V) such that A = U * diag(S) * V' // TODO: all Matrix[Double] ?
    // def truth(): M[Boolean]

    def pow(p: Double): M

    def addScalar(x: T): M
    def subtractScalar(x: T): M
    def multiplyScalar(x: T): M
    def divideScalar(x: T): M
    def mulRow(i: Int, x: T): M
    def mulColumn(i: Int, x: T): M

    // Operations on pairs of matrices

    def addMatrix(other: M): M
    def subtractMatrix(other: M): M
    def multiplyMatrix(other: M): M
    def concatenateHorizontally(right: M): M
    def concatenateVertically(under: M): M
    def solve(B: M): M // returns X, where this == A and A x X = B

    // Operations on a matrix and a column/row vector

    def addRowVector(row: M): M
    def addColumnVector(column: M): M
    def subRowVector(row: M): M
    def subColumnVector(column: M): M

    // Operations on pair of matrices that return M[Boolean]

    def lt(other: M): BM
    def le(other: M): BM
    def gt(other: M): BM
    def ge(other: M): BM
    def eq(other: M): BM
    def ne(other: M): BM

    def and(other: M): BM
    def or(other: M): BM
    def xor(other: M): BM
    def not(): BM

    // various mins and maxs

    def max(): T
    def argmax(): (Int, Int)
    def min(): T
    def argmin(): (Int, Int)
    def columnMins(): M
    // def columnArgmins
    def columnMaxs(): M
    // def columnArgmaxs

    // In-place versions

    def ceili(): Unit
    def floori(): Unit
    def powi(p: Double): Unit

    def addi(x: T): Unit
    def subtracti(x: T): Unit
    def multiplyi(x: T): Unit
    def dividei(x: T): Unit

    def addMatrixi(other: M): Unit
    def subtractMatrixi(other: M): Unit
    def addiRowVector(row: M): Unit
    def addiColumnVector(column: M): Unit
    def subiRowVector(row: M): Unit
    def subiColumnVector(column: M): Unit

    // aliases

    def t() = transpose()
    def tr() = transpose()
    def inv() = invert()

    def scalar() = {
      assert(isScalar)
      valueAt(0, 0)
    }

    def +(x: T) = addScalar(x)
    def +=(x: T) = addi(x)
    def +(other: M) = addMatrix(other)
    def +=(other: M) = addMatrixi(other)

    def -(x: T) = subtractScalar(x)
    def -=(x: T) = subtracti(x)
    def -(other: M) = subtractMatrix(other)
    def -=(other: M) = subtractMatrixi(other)

    def *(x: T) = multiplyScalar(x)
    def *=(x: T) = multiplyi(x)
    def ⨯(other: M) = multiplyMatrix(other)
    def mm(other: M) = multiplyMatrix(other)

    def /(x: T) = divideScalar(x)
    def /=(x: T) = dividei(x)

    def +|+(right: M) = concatenateHorizontally(right)
    def +/+(under: M) = concatenateVertically(under)

    def <(other: M) = lt(other)
    def <=(other: M) = le(other)
    def ≤(other: M) = le(other)
    def >(other: M) = gt(other)
    def >=(other: M) = ge(other)
    def ≥(other: M) = ge(other)
    def ==(other: M) = eq(other)
    def !=(other: M) = ne(other)
    def ≠(other: M) = ne(other)
    def &(other: M) = and(other)
    def ∧(other: M) = and(other)
    def |(other: M) = or(other)
    def ∨(other: M) = or(other)
    def ⊕(other: M) = xor(other)
    def ⊻(other: M) = xor(other)
    def !() = not()
    def ~() = not()
    def ¬() = not()

  }

}
