package org.pingel.axle.matrix

abstract class MatrixFactory {

  /**
   * T: element type
   * S: Storage type
   * M: Matrix[T] that is backed by storage S
   */
  
  type T
  type S
  type M <: Matrix[T]

  // TODO: svd, solve, boolean operators, comparisons, max/min/argmax/...,
  // stuff in MatrixFunctions

  trait Matrix[T] {

    def rows: Int
    def columns: Int
    def length: Int

    def valueAt(i: Int, j: Int): T
    def setValueAt(i: Int, j: Int, v: T): Unit

    def getColumn(j: Int): M
    def getRow(i: Int): M

    def negate(): M
    def transpose(): M

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
    
    // def truth(): M[Boolean]
  }

  protected def pure(s: S): M
}
