package org.pingel.axle.matrix

// not necessarily a bijection, but related
trait FunctionPair {
  type A
  type B
  val forward: A => B
  val backward: B => A
}

import org.jblas.DoubleMatrix

trait JblasMatrixFactory extends MatrixFactory {

  type M <: JblasMatrix

  trait DoubleJblasMatrix extends JblasMatrix {

    type T = Double

    val functionPair = new FunctionPair {
      type A = Double
      type B = T
      val forward = (d: Double) => d
      val backward = (t: T) => t
    }

  }

  trait IntJblasMatrix extends JblasMatrix {

    type T = Int

    val functionPair = new FunctionPair {
      type A = Double
      type B = T
      val forward = (d: Double) => d.toInt
      val backward = (t: T) => t.toDouble
    }
  }

  trait BooleanJblasMatrix extends JblasMatrix {

    type T = Boolean

    val functionPair = new FunctionPair {
      type A = Double
      type B = T
      val forward = (d: Double) => d != 0.0
      val backward = (t: T) => t match { case true => 0.0 case false => 1.0 }
    }
  }

  class JblasMatrixImpl(storage: DoubleMatrix) extends JblasMatrix {
    def getStorage = storage
  }

  trait JblasMatrix extends Matrix {

    // self: M =>

    type S = DoubleMatrix

    type BM = BooleanJblasMatrixFactoryClass#BooleanJblasMatrixImpl

    val functionPair: FunctionPair {
      type A = Double
      type B = T
      val forward: (Double) => T
      val backward: (T) => Double
    }

    def rows() = getStorage.rows
    def columns() = getStorage.columns
    def length() = getStorage.length

    def valueAt(i: Int, j: Int): T = functionPair.forward(getStorage.get(i, j))
    def setValueAt(i: Int, j: Int, v: T) = getStorage.put(i, j, functionPair.backward(v))

    def getColumn(j: Int) = matrix(getStorage.getColumn(j))
    def getRow(i: Int) = matrix(getStorage.getRow(i))

    def isEmpty() = getStorage.isEmpty
    def isRowVector() = getStorage.isRowVector
    def isColumnVector() = getStorage.isColumnVector
    def isVector() = getStorage.isVector
    def isSquare() = getStorage.isSquare
    def isScalar() = getStorage.isScalar

    def dup() = matrix(getStorage.dup())
    def negate() = matrix(getStorage.neg())
    def transpose() = matrix(getStorage.transpose())
    def diag() = matrix(getStorage.diag())
    def invert() = matrix(org.jblas.Solve.solve(getStorage, DoubleMatrix.eye(getStorage.rows)))
    def ceil() = matrix(org.jblas.MatrixFunctions.ceil(getStorage))
    def floor() = matrix(org.jblas.MatrixFunctions.floor(getStorage))
    def log() = matrix(org.jblas.MatrixFunctions.log(getStorage))
    def log10() = matrix(org.jblas.MatrixFunctions.log10(getStorage))

    def fullSVD() = {
      val usv = org.jblas.Singular.fullSVD(getStorage)
      (matrix(usv(0)), matrix(usv(1)), matrix(usv(2)))
    }

    def addScalar(x: T) = matrix(getStorage.add(functionPair.backward(x)))
    def subtractScalar(x: T) = matrix(getStorage.sub(functionPair.backward(x)))
    def multiplyScalar(x: T) = matrix(getStorage.mul(functionPair.backward(x)))
    def divideScalar(x: T) = matrix(getStorage.div(functionPair.backward(x)))
    def mulRow(i: Int, x: T) = matrix(getStorage.mulRow(i, functionPair.backward(x)))
    def mulColumn(i: Int, x: T) = matrix(getStorage.mulColumn(i, functionPair.backward(x)))

    def pow(p: Double) = matrix(org.jblas.MatrixFunctions.pow(getStorage, p))

    def addMatrix(other: JblasMatrixImpl) = matrix(getStorage.add(other.getJblas))
    def subtractMatrix(other: JblasMatrixImpl) = matrix(getStorage.sub(other.getJblas))
    def multiplyMatrix(other: JblasMatrixImpl) = matrix(getStorage.mmul(other.getJblas))

    def concatenateHorizontally(right: JblasMatrixImpl) = matrix(DoubleMatrix.concatHorizontally(getStorage, right.getJblas))
    def concatenateVertically(under: JblasMatrixImpl) = matrix(DoubleMatrix.concatVertically(getStorage, under.getJblas))
    def solve(B: JblasMatrixImpl) = matrix(org.jblas.Solve.solve(getStorage, B.getJblas))

    def addRowVector(row: JblasMatrixImpl) = matrix(getStorage.addRowVector(row.getJblas))
    def addColumnVector(column: JblasMatrixImpl) = matrix(getStorage.addRowVector(column.getJblas))
    def subRowVector(row: JblasMatrixImpl) = matrix(getStorage.subRowVector(row.getJblas))
    def subColumnVector(column: JblasMatrixImpl) = matrix(getStorage.subRowVector(column.getJblas))

    def lt(other: JblasMatrixImpl) = BooleanJblasMatrixFactory.matrix(getStorage.lt(other.getJblas))
    def le(other: JblasMatrixImpl) = BooleanJblasMatrixFactory.matrix(getStorage.le(other.getJblas))
    def gt(other: JblasMatrixImpl) = BooleanJblasMatrixFactory.matrix(getStorage.gt(other.getJblas))
    def ge(other: JblasMatrixImpl) = BooleanJblasMatrixFactory.matrix(getStorage.ge(other.getJblas))
    def eq(other: JblasMatrixImpl) = BooleanJblasMatrixFactory.matrix(getStorage.eq(other.getJblas))
    def ne(other: JblasMatrixImpl) = BooleanJblasMatrixFactory.matrix(getStorage.ne(other.getJblas))

    def and(other: JblasMatrixImpl) = BooleanJblasMatrixFactory.matrix(getStorage.and(other.getJblas))
    def or(other: JblasMatrixImpl) = BooleanJblasMatrixFactory.matrix(getStorage.or(other.getJblas))
    def xor(other: JblasMatrixImpl) = BooleanJblasMatrixFactory.matrix(getStorage.xor(other.getJblas))

    def not() = BooleanJblasMatrixFactory.matrix(getStorage.not())

    def max() = functionPair.forward(getStorage.max())

    def argmax() = {
      val i = getStorage.argmax()
      (i % columns, i / columns)
    }

    def min() = functionPair.forward(getStorage.min())

    def argmin() = {
      val i = getStorage.argmin()
      (i % columns, i / columns)
    }

    def columnMins() = matrix(getStorage.columnMins())
    def columnMaxs() = matrix(getStorage.columnMaxs())

    // in-place operations

    def addi(x: T) = getStorage.addi(functionPair.backward(x))
    def subtracti(x: T) = getStorage.subi(functionPair.backward(x))
    def multiplyi(x: T) = getStorage.muli(functionPair.backward(x))
    def matrixMultiplyi(x: T) = getStorage.mmuli(functionPair.backward(x))
    def dividei(x: T) = getStorage.divi(functionPair.backward(x))
    def ceili() = org.jblas.MatrixFunctions.ceili(getStorage)
    def floori() = org.jblas.MatrixFunctions.floori(getStorage)
    def logi() = org.jblas.MatrixFunctions.logi(getStorage)
    def log10i() = org.jblas.MatrixFunctions.log10i(getStorage)
    def powi(p: Double) = org.jblas.MatrixFunctions.powi(getStorage, p)
    def addMatrixi(other: JblasMatrixImpl) = getStorage.addi(other.getJblas)
    def subtractMatrixi(other: JblasMatrixImpl) = getStorage.subi(other.getJblas)
    def addiRowVector(row: JblasMatrixImpl) = getStorage.addiRowVector(row.getJblas)
    def addiColumnVector(column: JblasMatrixImpl) = getStorage.addiRowVector(column.getJblas)
    def subiRowVector(row: JblasMatrixImpl) = getStorage.subiRowVector(row.getJblas)
    def subiColumnVector(column: JblasMatrixImpl) = getStorage.subiRowVector(column.getJblas)

    override def toString() =
      (0 until rows).map(i => (0 until columns).map(j => functionPair.forward(getStorage.get(i, j))).mkString(" ")).mkString("\n")

    def getJblas() = getStorage
  }

  def matrix[T](r: Int, c: Int, values: Array[T]): M

  def zeros(m: Int, n: Int) = matrix(DoubleMatrix.zeros(m, n))
  def ones(m: Int, n: Int) = matrix(DoubleMatrix.ones(m, n))
  def eye(n: Int) = matrix(DoubleMatrix.eye(n))
  def I(n: Int) = eye(n)
  def rand(m: Int, n: Int) = matrix(DoubleMatrix.rand(m, n)) // evenly distributed from 0.0 to 1.0
  def randn(m: Int, n: Int) = matrix(DoubleMatrix.randn(m, n)) // normal distribution 

}

object DoubleJblasMatrixFactory extends DoubleJblasMatrixFactoryClass()

class DoubleJblasMatrixFactoryClass extends JblasMatrixFactory {

  class DoubleJblasMatrixImpl(jblas: DoubleMatrix)
    extends JblasMatrixImpl(jblas)
    with DoubleJblasMatrix {
    //    self: M =>
  }

  type M = DoubleJblasMatrix

  def matrix(r: Int, c: Int, values: Array[Double]): M = {
    val jblas = new org.jblas.DoubleMatrix(values) // .map(functionPair.backward(_)))
    jblas.reshape(r, c)
    matrix(jblas)
  }

  def matrix(jblas: DoubleMatrix): M = new DoubleJblasMatrixImpl(jblas)
}

object IntJblasMatrixFactory extends IntJblasMatrixFactoryClass()

class IntJblasMatrixFactoryClass extends JblasMatrixFactory {

  // TODO: rand and randn should probably floor the result

  class IntJblasMatrixImpl(jblas: DoubleMatrix)
    extends JblasMatrixImpl(jblas)
    with IntJblasMatrix {
    //	  self: M =>
  }

  type M = IntJblasMatrix

  def matrix(jblas: DoubleMatrix): M = new IntJblasMatrixImpl(jblas)
}

object BooleanJblasMatrixFactory extends BooleanJblasMatrixFactoryClass()

class BooleanJblasMatrixFactoryClass extends JblasMatrixFactory {

  class BooleanJblasMatrixImpl(jblas: DoubleMatrix)
    extends JblasMatrixImpl(jblas)
    with BooleanJblasMatrix {
    //	  self: M =>
  }

  type M = BooleanJblasMatrix
  //  type M = BooleanJblasMatrix { type T = Boolean type S = DoubleMatrix}

  def falses(m: Int, n: Int) = matrix(DoubleMatrix.zeros(m, n))
  def trues(m: Int, n: Int) = matrix(DoubleMatrix.ones(m, n))

  def matrix(jblas: DoubleMatrix) = new BooleanJblasMatrixImpl(jblas)
}

// class DoubleJblasMatrixImpl(jblas: org.jblas.DoubleMatrix) extends JblasMatrixImpl(jblas) {}
