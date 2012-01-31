package org.pingel.axle.matrix

abstract class MatrixFactory {

  /**
   * Type Parameters:
   * 
   * T element type
   * S storage type
   * M subtype of Matrix[T] that is backed by storage S
   */
  
  type T
  type S
  type Matrix <: MatrixIntf[T]

  trait MatrixIntf[T] {

    def rows: Int
    def columns: Int
    def length: Int

    def valueAt(i: Int, j: Int): T
    def setValueAt(i: Int, j: Int, v: T): Unit

    def getColumn(j: Int): Matrix
    def getRow(i: Int): Matrix

    def isEmpty(): Boolean
    def isRowVector(): Boolean
    def isColumnVector(): Boolean
    def isVector(): Boolean
    def isSquare(): Boolean
    def isScalar(): Boolean
    // resize
    // reshape

    def dup(): Matrix
    def negate(): Matrix
    def transpose(): Matrix
    def invert(): Matrix
    def ceil(): Matrix
    def floor(): Matrix
    def log(): Matrix
    def log10(): Matrix
    def fullSVD(): (Matrix, Matrix, Matrix) // (U, S, V) such that A = U * diag(S) * V' // TODO: all Matrix[Double] ?
    // def truth(): M[Boolean]

    def pow(p: Double): Matrix
    
    def addScalar(x: T): Matrix
    def subtractScalar(x: T): Matrix
    def multiplyScalar(x: T): Matrix
    def divideScalar(x: T): Matrix
    def mulRow(i: Int, x: T): Matrix
    def mulColumn(i: Int, x: T): Matrix

    // Operations on pairs of matrices

    def addMatrix(other: Matrix): Matrix
    def subtractMatrix(other: Matrix): Matrix
    def multiplyMatrix(other: Matrix): Matrix
    def concatenateHorizontally(right: Matrix): Matrix
    def concatenateVertically(under: Matrix): Matrix
    def solve(B: Matrix): Matrix // returns X, where this == A and A x X = B

    // Operations on a matrix and a column/row vector
    
    def addRowVector(row: Matrix): Matrix
    def addColumnVector(column: Matrix): Matrix
    def subRowVector(row: Matrix): Matrix
    def subColumnVector(column: Matrix): Matrix

    // Operations on pair of matrices that return M[Boolean]

    def lt(other: Matrix): Matrix // TODO Matrix[Boolean]
    def le(other: Matrix): Matrix
    def gt(other: Matrix): Matrix
    def ge(other: Matrix): Matrix
    def eq(other: Matrix): Matrix
    def ne(other: Matrix): Matrix
    
    def and(other: Matrix): Matrix
    def or(other: Matrix): Matrix
    def xor(other: Matrix): Matrix
    def not(): Matrix

    // various mins and maxs
    
    def max(): T
    def argmax(): (Int, Int)
    def min(): T
    def argmin(): (Int, Int)
    def columnMins(): Matrix
    // def columnArgmins
    def columnMaxs(): Matrix
    // def columnArgmaxs

    // In-place versions
    
    def ceili(): Unit
    def floori(): Unit
    def powi(p: Double): Unit
    
    def addi(x: T): Unit
    def subtracti(x: T): Unit
    def multiplyi(x: T): Unit
    def dividei(x: T): Unit

    def addMatrixi(other: Matrix): Unit
    def subtractMatrixi(other: Matrix): Unit
    def addiRowVector(row: Matrix): Unit
    def addiColumnVector(column: Matrix): Unit
    def subiRowVector(row: Matrix): Unit
    def subiColumnVector(column: Matrix): Unit

    
    // aliases

    def t() = transpose()
    def inv() = invert()

    def +(x: T) = addScalar(x)
    def +=(x: T) = addi(x)
    def +(other: Matrix) = addMatrix(other)
    def +=(other: Matrix) = addMatrixi(other)
    
    def -(x: T) = subtractScalar(x)
    def -=(x: T) = subtracti(x)
    def -(other: Matrix) = subtractMatrix(other)
    def -=(other: Matrix) = subtractMatrixi(other)
    
    def *(x: T) = multiplyScalar(x)
    def *=(x: T) = multiplyi(x)
    def ⨯(other: Matrix) = multiplyMatrix(other)
    def mm(other: Matrix) = multiplyMatrix(other)
    
    def /(x: T) = divideScalar(x)
    def /=(x: T) = dividei(x)

    def +|+ (right: Matrix) = concatenateHorizontally(right)
    def +/+ (under: Matrix) = concatenateVertically(under)

    def <(other: Matrix) = lt(other)
    def <=(other: Matrix) = le(other)
    def >(other: Matrix) = gt(other)
    def >=(other: Matrix) = ge(other)
    def ==(other: Matrix) = eq(other)
    def !=(other: Matrix) = ne(other)
    def &(other: Matrix) = and(other)
    def |(other: Matrix) = or(other)
    def ^(other: Matrix) = xor(other)
    def !() = not()

  }

  protected def pure(s: S): Matrix
}
