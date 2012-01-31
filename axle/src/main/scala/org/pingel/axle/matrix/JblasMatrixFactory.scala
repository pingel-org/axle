package org.pingel.axle.matrix

trait FunctionPair[A, B] {
  // not necessarily a bijection, but related
  val forward: A => B
  val backward: B => A
}

abstract class JblasMatrixFactoryClass extends MatrixFactory {

  import org.jblas.{ MatrixFunctions, Solve, Singular, DoubleMatrix }

  type M = JblasMatrixImpl

  type S = DoubleMatrix

  val m: FunctionPair[Double, T]

  class JblasMatrixImpl(jblas: DoubleMatrix) extends Matrix[T] {

    def rows() = jblas.rows
    def columns() = jblas.columns
    def length() = jblas.length

    def valueAt(i: Int, j: Int): T = m.forward(jblas.get(i, j))
    def setValueAt(i: Int, j: Int, v: T) = jblas.put(i, j, m.backward(v))

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
    def invert() = pure(Solve.solve(jblas, DoubleMatrix.eye(jblas.rows)))
    def ceil() = pure(MatrixFunctions.ceil(jblas))
    def floor() = pure(MatrixFunctions.floor(jblas))
    def log() = pure(MatrixFunctions.log(jblas))
    def log10() = pure(MatrixFunctions.log10(jblas))

    def fullSVD() = {
      val usv = Singular.fullSVD(jblas)
      (pure(usv(0)), pure(usv(1)), pure(usv(2)))
    }

    def addScalar(x: T) = pure(jblas.add(m.backward(x)))
    def subtractScalar(x: T) = pure(jblas.sub(m.backward(x)))
    def multiplyScalar(x: T) = pure(jblas.mul(m.backward(x)))
    def divideScalar(x: T) = pure(jblas.div(m.backward(x)))
    def mulRow(i: Int, x: T) = pure(jblas.mulRow(i, m.backward(x)))
    def mulColumn(i: Int, x: T) = pure(jblas.mulColumn(i, m.backward(x)))

    def pow(p: Double) = pure(MatrixFunctions.pow(jblas, p))

    def addMatrix(other: M) = pure(jblas.add(other.getJblas))
    def subtractMatrix(other: M) = pure(jblas.sub(other.getJblas))
    def multiplyMatrix(other: M) = pure(jblas.mmul(other.getJblas))

    def concatenateHorizontally(right: M) = pure(DoubleMatrix.concatHorizontally(jblas, right.getJblas))
    def concatenateVertically(under: M) = pure(DoubleMatrix.concatVertically(jblas, under.getJblas))
    def solve(B: M) = pure(Solve.solve(jblas, B.getJblas))

    def addRowVector(row: M) = pure(jblas.addRowVector(row.getJblas))
    def addColumnVector(column: M) = pure(jblas.addRowVector(column.getJblas))
    def subRowVector(row: M) = pure(jblas.subRowVector(row.getJblas))
    def subColumnVector(column: M) = pure(jblas.subRowVector(column.getJblas))

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

    def max() = m.forward(jblas.max())
    def argmax() = {
      val i = jblas.argmax()
      (i % columns, i / columns)
    }
    def min() = m.forward(jblas.min())
    def argmin() = {
      val i = jblas.argmin()
      (i % columns, i / columns)
    }
    def columnMins() = pure(jblas.columnMins())
    def columnMaxs() = pure(jblas.columnMaxs())

    // in-place operations

    def addi(x: T) = jblas.addi(m.backward(x))
    def subtracti(x: T) = jblas.subi(m.backward(x))
    def multiplyi(x: T) = jblas.muli(m.backward(x))
    def matrixMultiplyi(x: T) = jblas.mmuli(m.backward(x))
    def dividei(x: T) = jblas.divi(m.backward(x))
    def ceili() = MatrixFunctions.ceili(jblas)
    def floori() = MatrixFunctions.floori(jblas)
    def logi() = MatrixFunctions.logi(jblas)
    def log10i() = MatrixFunctions.log10i(jblas)
    def powi(p: Double) = MatrixFunctions.powi(jblas, p)
    def addMatrixi(other: M) = jblas.addi(other.getJblas)
    def subtractMatrixi(other: M) = jblas.subi(other.getJblas)
    def addiRowVector(row: M) = jblas.addiRowVector(row.getJblas)
    def addiColumnVector(column: M) = jblas.addiRowVector(column.getJblas)
    def subiRowVector(row: M) = jblas.subiRowVector(row.getJblas)
    def subiColumnVector(column: M) = jblas.subiRowVector(column.getJblas)

    override def toString() =
      (0 until rows).map(i => (0 until columns).map(j => m.forward(jblas.get(i, j))).mkString(" ")).mkString("\n")

    def getJblas() = jblas
  }

  def fromArray(r: Int, c: Int, values: Array[T]) = {
    val jblas = new DoubleMatrix(values.map(m.backward(_)))
    jblas.reshape(r, c)
    pure(jblas)
  }

  def zeros(m: Int, n: Int) = pure(DoubleMatrix.zeros(m, n))
  def ones(m: Int, n: Int) = pure(DoubleMatrix.ones(m, n))
  def eye(n: Int) = pure(DoubleMatrix.eye(n))
  def diag(dsRow: M) = pure(DoubleMatrix.diag(dsRow.getJblas()))
  def rand(m: Int, n: Int) = pure(DoubleMatrix.rand(m, n)) // evenly distributed from 0.0 to 1.0
  def randn(m: Int, n: Int) = pure(DoubleMatrix.randn(m, n)) // normal distribution 

  def pure(jblas: DoubleMatrix): M = new JblasMatrixImpl(jblas)
}

object DoubleJblasMatrixFactory extends DoubleJblasMatrixFactoryClass()

class DoubleJblasMatrixFactoryClass extends JblasMatrixFactoryClass {
  type T = Double

  val m = new FunctionPair[Double, Double] {
    val forward = (d: Double) => d
    val backward = (t: T) => t
  }
}

object IntJblasMatrixFactory extends IntJblasMatrixFactoryClass()

class IntJblasMatrixFactoryClass extends JblasMatrixFactoryClass {

  type T = Int

  val m = new FunctionPair[Double, Int] {
    val forward = (d: Double) => d.toInt
    val backward = (t: T) => t.toDouble
  }

  // TOOD: rand and randn should probably floor the result
}

object BooleanJblasMatrixFactory extends BooleanJblasMatrixFactoryClass()

class BooleanJblasMatrixFactoryClass extends JblasMatrixFactoryClass {

  type T = Boolean

  val m = new FunctionPair[Double, Boolean] {
    val forward = (d: Double) => d != 0.0
    val backward = (t: T) => t match { case true => 0.0 case false => 1.0 }
  }

  import org.jblas.DoubleMatrix

  def falses(m: Int, n: Int) = pure(DoubleMatrix.zeros(m, n))
  def trues(m: Int, n: Int) = pure(DoubleMatrix.ones(m, n))
}

// class DoubleJblasMatrixImpl(jblas: org.jblas.DoubleMatrix) extends JblasMatrixImpl(jblas) {}
