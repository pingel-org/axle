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
  type M <: Matrix[T]

  trait Matrix[T] {

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

    def negate(): M
    def transpose(): M

    // Operations on pairs of matrices
    
    def add(other: M): M
    def subtract(other: M): M
    def multiply(other: M): M
    def matrixMultiply(other: M): M
    def divide(other: M): M
    def concatenateHorizontally(right: M): M
    def concatenateVertically(under: M): M

    // aliases
    def +(other: M) = add(other)
    def -(other: M) = subtract(other)
    // def .(other: M[T]) = multiply(other)
    def x(other: M) = matrixMultiply(other)
    def +|+ (right: M) = concatenateHorizontally(right)
    def +/+ (under: M) = concatenateVertically(under)

    // Operations on pair of matrices that return M[Boolean]

    def lt(other: M): M
    def le(other: M): M
    def gt(other: M): M
    def ge(other: M): M
    def eq(other: M): M
    def ne(other: M): M
    
    def and(other: M): M
    def or(other: M): M
    def xor(other: M): M
    def not(): M
    
    // aliases
    def <(other: M) = lt(other)
    def <=(other: M) = le(other)
    def >(other: M) = gt(other)
    def >=(other: M) = ge(other)
    def ==(other: M) = eq(other)
    def !=(other: M) = ne(other)

    def &(other: M) = and(other)
    def |(other: M) = or(other)
    def ^(other: M) = xor(other)
    def !() = not()
 
    // various mins and maxs
    
    def max(): T
    def argmax(): (Int, Int)
    def min(): T
    def argmin(): (Int, Int)
    def columnMins(): M
    // def columnArgmins
    def columnMaxs(): M
    // def columnArgmaxs
    
    // def truth(): M[Boolean]
  }

  protected def pure(s: S): M
}
