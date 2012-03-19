package org.pingel.axle.matrix

trait FunctionPair[A, B] {
  // not necessarily a bijection, but related
  val forward: A => B
  val backward: B => A
}

abstract class JblasMatrixFactoryClass extends MatrixFactory {

  import org.jblas.{ MatrixFunctions, Solve, Singular, DoubleMatrix }

  type T
  
  type S = DoubleMatrix

  val functionPair: FunctionPair[Double, T]

  class JblasMatrixImpl(jblas: DoubleMatrix) extends Matrix[T] {

    def rows() = jblas.rows
    def columns() = jblas.columns
    def length() = jblas.length

    def valueAt(i: Int, j: Int): T = functionPair.forward(jblas.get(i, j))
    def setValueAt(i: Int, j: Int, v: T) = jblas.put(i, j, functionPair.backward(v))

    def getColumn(j: Int) = pure(jblas.getColumn(j))
    def getRow(i: Int) = pure(jblas.getRow(i))

    def isEmpty() = jblas.isEmpty
    def isRowVector() = jblas.isRowVector
    def isColumnVector() = jblas.isColumnVector
    def isVector() = jblas.isVector
    def isSquare() = jblas.isSquare
    def isScalar() = jblas.isScalar

    def dup() = pure(jblas.dup())
    def negate() = pure(jblas.neg())
    def transpose() = pure(jblas.transpose())
    def diag() = pure(jblas.diag())
    def invert() = pure(Solve.solve(jblas, DoubleMatrix.eye(jblas.rows)))
    def ceil() = pure(MatrixFunctions.ceil(jblas))
    def floor() = pure(MatrixFunctions.floor(jblas))
    def log() = pure(MatrixFunctions.log(jblas))
    def log10() = pure(MatrixFunctions.log10(jblas))

    def fullSVD() = {
      val usv = Singular.fullSVD(jblas)
      (pure(usv(0)), pure(usv(1)), pure(usv(2)))
    }

    def addScalar(x: T) = pure(jblas.add(functionPair.backward(x)))
    def subtractScalar(x: T) = pure(jblas.sub(functionPair.backward(x)))
    def multiplyScalar(x: T) = pure(jblas.mul(functionPair.backward(x)))
    def divideScalar(x: T) = pure(jblas.div(functionPair.backward(x)))
    def mulRow(i: Int, x: T) = pure(jblas.mulRow(i, functionPair.backward(x)))
    def mulColumn(i: Int, x: T) = pure(jblas.mulColumn(i, functionPair.backward(x)))

    def pow(p: Double) = pure(MatrixFunctions.pow(jblas, p))

    def addMatrix(other: JblasMatrixImpl) = pure(jblas.add(other.getJblas))
    def subtractMatrix(other: JblasMatrixImpl) = pure(jblas.sub(other.getJblas))
    def multiplyMatrix(other: JblasMatrixImpl) = pure(jblas.mmul(other.getJblas))

    def concatenateHorizontally(right: JblasMatrixImpl) = pure(DoubleMatrix.concatHorizontally(jblas, right.getJblas))
    def concatenateVertically(under: JblasMatrixImpl) = pure(DoubleMatrix.concatVertically(jblas, under.getJblas))
    def solve(B: JblasMatrixImpl) = pure(Solve.solve(jblas, B.getJblas))

    def addRowVector(row: JblasMatrixImpl) = pure(jblas.addRowVector(row.getJblas))
    def addColumnVector(column: JblasMatrixImpl) = pure(jblas.addRowVector(column.getJblas))
    def subRowVector(row: JblasMatrixImpl) = pure(jblas.subRowVector(row.getJblas))
    def subColumnVector(column: JblasMatrixImpl) = pure(jblas.subRowVector(column.getJblas))

    def lt(other: JblasMatrixImpl) = pure(jblas.lt(other.getJblas))
    def le(other: JblasMatrixImpl) = pure(jblas.le(other.getJblas))
    def gt(other: JblasMatrixImpl) = pure(jblas.gt(other.getJblas))
    def ge(other: JblasMatrixImpl) = pure(jblas.ge(other.getJblas))
    def eq(other: JblasMatrixImpl) = pure(jblas.eq(other.getJblas))
    def ne(other: JblasMatrixImpl) = pure(jblas.ne(other.getJblas))

    def and(other: JblasMatrixImpl) = pure(jblas.and(other.getJblas))
    def or(other: JblasMatrixImpl) = pure(jblas.or(other.getJblas))
    def xor(other: JblasMatrixImpl) = pure(jblas.xor(other.getJblas))
    def not() = BooleanJblasMatrixFactory.pure(jblas.not())

    def max() = functionPair.forward(jblas.max())
    
    def argmax() = {
      val i = jblas.argmax()
      (i % columns, i / columns)
    }
    
    def min() = functionPair.forward(jblas.min())
    
    def argmin() = {
      val i = jblas.argmin()
      (i % columns, i / columns)
    }
    
    def columnMins() = pure(jblas.columnMins())
    def columnMaxs() = pure(jblas.columnMaxs())

    // in-place operations

    def addi(x: T) = jblas.addi(functionPair.backward(x))
    def subtracti(x: T) = jblas.subi(functionPair.backward(x))
    def multiplyi(x: T) = jblas.muli(functionPair.backward(x))
    def matrixMultiplyi(x: T) = jblas.mmuli(functionPair.backward(x))
    def dividei(x: T) = jblas.divi(functionPair.backward(x))
    def ceili() = MatrixFunctions.ceili(jblas)
    def floori() = MatrixFunctions.floori(jblas)
    def logi() = MatrixFunctions.logi(jblas)
    def log10i() = MatrixFunctions.log10i(jblas)
    def powi(p: Double) = MatrixFunctions.powi(jblas, p)
    def addMatrixi(other: JblasMatrixImpl) = jblas.addi(other.getJblas)
    def subtractMatrixi(other: JblasMatrixImpl) = jblas.subi(other.getJblas)
    def addiRowVector(row: JblasMatrixImpl) = jblas.addiRowVector(row.getJblas)
    def addiColumnVector(column: JblasMatrixImpl) = jblas.addiRowVector(column.getJblas)
    def subiRowVector(row: JblasMatrixImpl) = jblas.subiRowVector(row.getJblas)
    def subiColumnVector(column: JblasMatrixImpl) = jblas.subiRowVector(column.getJblas)

    override def toString() =
      (0 until rows).map(i => (0 until columns).map(j => functionPair.forward(jblas.get(i, j))).mkString(" ")).mkString("\n")

    def getJblas() = jblas
  }

  def fromArray(r: Int, c: Int, values: Array[T]) = {
    val jblas = new DoubleMatrix(values.map(functionPair.backward(_)))
    jblas.reshape(r, c)
    pure(jblas)
  }

  def zeros(m: Int, n: Int) = pure(DoubleMatrix.zeros(m, n))
  def ones(m: Int, n: Int) = pure(DoubleMatrix.ones(m, n))
  def eye(n: Int) = pure(DoubleMatrix.eye(n))
  def I(n: Int) = eye(n)
  def rand(m: Int, n: Int) = pure(DoubleMatrix.rand(m, n)) // evenly distributed from 0.0 to 1.0
  def randn(m: Int, n: Int) = pure(DoubleMatrix.randn(m, n)) // normal distribution 

  def pure(jblas: DoubleMatrix): JblasMatrixImpl = new JblasMatrixImpl(jblas)
}

object DoubleJblasMatrixFactory extends DoubleJblasMatrixFactoryClass()

class DoubleJblasMatrixFactoryClass extends JblasMatrixFactoryClass {
  
  type T = Double

  val functionPair = new FunctionPair[Double, Double] {
    val forward = (d: Double) => d
    val backward = (t: T) => t
  }
}

object IntJblasMatrixFactory extends IntJblasMatrixFactoryClass()

class IntJblasMatrixFactoryClass extends JblasMatrixFactoryClass {

  type T = Int

  val functionPair = new FunctionPair[Double, Int] {
    val forward = (d: Double) => d.toInt
    val backward = (t: T) => t.toDouble
  }

  // TOOD: rand and randn should probably floor the result
}

object BooleanJblasMatrixFactory extends BooleanJblasMatrixFactoryClass()

class BooleanJblasMatrixFactoryClass extends JblasMatrixFactoryClass {

  type T = Boolean

  val functionPair = new FunctionPair[Double, Boolean] {
    val forward = (d: Double) => d != 0.0
    val backward = (t: T) => t match { case true => 0.0 case false => 1.0 }
  }

  import org.jblas.DoubleMatrix

  def falses(m: Int, n: Int) = pure(DoubleMatrix.zeros(m, n))
  def trues(m: Int, n: Int) = pure(DoubleMatrix.ones(m, n))
}

// class DoubleJblasMatrixImpl(jblas: org.jblas.DoubleMatrix) extends JblasMatrixImpl(jblas) {}
