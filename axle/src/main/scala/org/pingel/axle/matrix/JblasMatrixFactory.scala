package org.pingel.axle.matrix


abstract class JblasMatrixFactoryClass extends MatrixFactory {

  type M = JblasMatrixImpl

  type S = org.jblas.DoubleMatrix

  def doubleToT(d: Double): T

  def tToDouble(t: T): Double

  class JblasMatrixImpl(jblas: org.jblas.DoubleMatrix) extends Matrix[T] {

    def rows() = jblas.rows

    def columns() = jblas.columns

    def length() = jblas.length

    def valueAt(i: Int, j: Int): T = doubleToT(jblas.get(i, j))

    def setValueAt(i: Int, j: Int, v: T) = jblas.put(i, j, tToDouble(v))

    def getColumn(j: Int) = pure(jblas.getColumn(j))
    def getRow(i: Int) = pure(jblas.getRow(i))

    def isEmpty() = jblas.isEmpty
    def isRowVector() = jblas.isRowVector
    def isColumnVector() = jblas.isColumnVector
    def isVector() = jblas.isVector
    def isSquare() = jblas.isSquare
    def isScalar() = jblas.isScalar
    
    def add(other: M) = pure(jblas.add(other.getJblas))
    def subtract(other: M) = pure(jblas.sub(other.getJblas))
    def multiply(other: M) = pure(jblas.mul(other.getJblas))
    def matrixMultiply(other: M) = pure(jblas.mmul(other.getJblas))
    // dot?
    def divide(other: M) = pure(jblas.div(other.getJblas))
    
    def concatenateHorizontally(right: M) = pure(org.jblas.DoubleMatrix.concatHorizontally(this.jblas, right.getJblas))
    def concatenateVertically(under: M) = pure(org.jblas.DoubleMatrix.concatVertically(this.jblas, under.getJblas))

    def negate() = pure(jblas.neg())
    def transpose() = pure(jblas.transpose())

    def lt(other: M) = pure(jblas.lt(other.getJblas))
    def le(other: M) = pure(jblas.le(other.getJblas))
    def gt(other: M) = pure(jblas.gt(other.getJblas))
    def ge(other: M) = pure(jblas.ge(other.getJblas))
    def eq(other: M) = pure(jblas.eq(other.getJblas))
    def ne(other: M) = pure(jblas.ne(other.getJblas))

    def and(other: M) = pure(jblas.and(other.getJblas))
    def or(other: M) = pure(jblas.or(other.getJblas))
    def xor(other: M) = pure(jblas.xor(other.getJblas))
    def not() = pure(jblas.not())
    
    def max() = doubleToT(jblas.max())
    def argmax() = {
      val i = jblas.argmax()
      (i % columns, i / columns)
    }
    def min() = doubleToT(jblas.min())
    def argmin() = {
      val i = jblas.argmin()
      (i % columns, i / columns)
    }
    def columnMins() = pure(jblas.columnMins())
    def columnMaxs() = pure(jblas.columnMaxs())

    // def truth() = BooleanJblasMatrixFactory.pure(jblas.truth())

    override def toString() =
      (0 until rows).map(i => (0 until columns).map(j => doubleToT(jblas.get(i, j))).mkString(" ")).mkString("\n")

    def getJblas() = jblas
  }

  def zeros(m: Int, n: Int) = pure(org.jblas.DoubleMatrix.zeros(m, n))
  def ones(m: Int, n: Int) = pure(org.jblas.DoubleMatrix.ones(m, n))
  def eye(n: Int) = pure(org.jblas.DoubleMatrix.eye(n))
  
  // evenly distributed from 0.0 to 1.0
  def rand(m: Int, n: Int) = pure(org.jblas.DoubleMatrix.rand(m, n))
  // normal distribution
  def randn(m: Int, n: Int) = pure(org.jblas.DoubleMatrix.randn(m, n))
  
  def pure(jblas: org.jblas.DoubleMatrix): M = new JblasMatrixImpl(jblas)
}

object DoubleJblasMatrixFactory extends DoubleJblasMatrixFactoryClass()

class DoubleJblasMatrixFactoryClass extends JblasMatrixFactoryClass {
  type T = Double
  def doubleToT(d: Double) = d
  def tToDouble(t: T) = t
}

object IntJblasMatrixFactory extends IntJblasMatrixFactoryClass()

class IntJblasMatrixFactoryClass extends JblasMatrixFactoryClass {
  type T = Int
  def doubleToT(d: Double) = d.toInt
  def tToDouble(t: T) = t
  // TOOD: rand and randn should probably floor the result
}

object BooleanJblasMatrixFactory extends BooleanJblasMatrixFactoryClass()

class BooleanJblasMatrixFactoryClass extends JblasMatrixFactoryClass {
  type T = Boolean
  def doubleToT(d: Double) = d > 0
  def tToDouble(t: T) = t match { case true => 0.0 case false => 1.0 }
  def falses(m: Int, n: Int) = pure(org.jblas.DoubleMatrix.zeros(m, n))
  def trues(m: Int, n: Int) = pure(org.jblas.DoubleMatrix.ones(m, n))
}

// class DoubleJblasMatrixImpl(jblas: org.jblas.DoubleMatrix) extends JblasMatrixImpl(jblas) {}
