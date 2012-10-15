package axle.matrix

import math.sqrt

// not necessarily a bijection, but related
trait FunctionPair[A, B] {
  val forward: A => B
  val backward: B => A
}

import org.jblas.DoubleMatrix

object JblasMatrixFactory extends JblasMatrixFactory {

}

trait JblasMatrixFactory extends MatrixFactory {

  implicit val double2double = new FunctionPair[Double, Double] {
    val forward = (d: Double) => d
    val backward = (t: Double) => t
  }

  implicit val formatDouble = (d: Double) => "%.6f".format(d)

  implicit val double2int = new FunctionPair[Double, Int] {
    val forward = (d: Double) => d.toInt
    val backward = (t: Int) => t.toDouble
  }

  implicit val formatInt = (i: Int) => i.toString

  implicit val double2boolean = new FunctionPair[Double, Boolean] {
    val forward = (d: Double) => d != 0.0
    val backward = (t: Boolean) => t match { case true => 0.0 case false => 1.0 }
  }

  implicit val formatBoolean = (b: Boolean) => b.toString

  type M[T] = JblasMatrix[T]

  class JblasMatrixImpl[T](_storage: DoubleMatrix)(fp: FunctionPair[Double, T], fmt: T => String)
    extends JblasMatrix[T] {
    def storage = _storage
    val functionPair = fp
    val format = fmt
  }

  trait JblasMatrix[T]
    extends Matrix[T] {

    type S = DoubleMatrix

    val functionPair: FunctionPair[Double, T]

    val format: T => String

    def rows() = storage.rows
    def columns() = storage.columns
    def length() = storage.length

    def apply(i: Int, j: Int): T = functionPair.forward(storage.get(i, j))
    def update(i: Int, j: Int, v: T) = storage.put(i, j, functionPair.backward(v))

    def toList(): List[T] = storage.toArray.toList.map(functionPair.forward(_))

    def column(j: Int) = matrix(storage.getColumn(j))(functionPair, format)
    def row(i: Int) = matrix(storage.getRow(i))(functionPair, format)

    def isEmpty() = storage.isEmpty
    def isRowVector() = storage.isRowVector
    def isColumnVector() = storage.isColumnVector
    def isVector() = storage.isVector
    def isSquare() = storage.isSquare
    def isScalar() = storage.isScalar

    def dup() = matrix(storage.dup())(functionPair, format)
    def negate() = matrix(storage.neg())(functionPair, format)
    def transpose() = matrix(storage.transpose())(functionPair, format)
    def diag() = matrix(storage.diag())(functionPair, format)
    def invert() = matrix(org.jblas.Solve.solve(storage, DoubleMatrix.eye(storage.rows)))(functionPair, format)
    def ceil() = matrix(org.jblas.MatrixFunctions.ceil(storage))(functionPair, format)
    def floor() = matrix(org.jblas.MatrixFunctions.floor(storage))(functionPair, format)
    def log() = matrix(org.jblas.MatrixFunctions.log(storage))(functionPair, format)
    def log10() = matrix(org.jblas.MatrixFunctions.log10(storage))(functionPair, format)

    def fullSVD() = {
      val usv = org.jblas.Singular.fullSVD(storage).map(matrix(_)(functionPair, format))
      (usv(0), usv(1), usv(2))
    }

    def addScalar(x: T) = matrix(storage.add(functionPair.backward(x)))(functionPair, format)
    def subtractScalar(x: T) = matrix(storage.sub(functionPair.backward(x)))(functionPair, format)
    def multiplyScalar(x: T) = matrix(storage.mul(functionPair.backward(x)))(functionPair, format)
    def divideScalar(x: T) = matrix(storage.div(functionPair.backward(x)))(functionPair, format)
    def mulRow(i: Int, x: T) = matrix(storage.mulRow(i, functionPair.backward(x)))(functionPair, format)
    def mulColumn(i: Int, x: T) = matrix(storage.mulColumn(i, functionPair.backward(x)))(functionPair, format)

    def pow(p: Double) = matrix(org.jblas.MatrixFunctions.pow(storage, p))(functionPair, format)

    def addMatrix(other: JblasMatrix[T]) = matrix(storage.add(other.jblas))(functionPair, format)
    def subtractMatrix(other: JblasMatrix[T]) = matrix(storage.sub(other.jblas))(functionPair, format)
    def multiplyMatrix(other: JblasMatrix[T]) = matrix(storage.mmul(other.jblas))(functionPair, format)

    def mulPointwise(other: JblasMatrix[T]) = matrix(storage.mul(other.jblas))(functionPair, format)
    def divPointwise(other: JblasMatrix[T]) = matrix(storage.div(other.jblas))(functionPair, format)

    def concatenateHorizontally(right: JblasMatrix[T]) = matrix(DoubleMatrix.concatHorizontally(storage, right.jblas))(functionPair, format)
    def concatenateVertically(under: JblasMatrix[T]) = matrix(DoubleMatrix.concatVertically(storage, under.jblas))(functionPair, format)
    def solve(B: JblasMatrix[T]) = matrix(org.jblas.Solve.solve(storage, B.jblas))(functionPair, format)

    def addRowVector(row: JblasMatrix[T]) = matrix(storage.addRowVector(row.jblas))(functionPair, format)
    def addColumnVector(column: JblasMatrix[T]) = matrix(storage.addRowVector(column.jblas))(functionPair, format)
    def subRowVector(row: JblasMatrix[T]) = matrix(storage.subRowVector(row.jblas))(functionPair, format)
    def subColumnVector(column: JblasMatrix[T]) = matrix(storage.subRowVector(column.jblas))(functionPair, format)
    def mulRowVector(row: JblasMatrix[T]) = matrix(storage.mulRowVector(row.jblas))(functionPair, format)
    def mulColumnVector(column: JblasMatrix[T]) = matrix(storage.mulRowVector(column.jblas))(functionPair, format)
    def divRowVector(row: JblasMatrix[T]) = matrix(storage.divRowVector(row.jblas))(functionPair, format)
    def divColumnVector(column: JblasMatrix[T]) = matrix(storage.divRowVector(column.jblas))(functionPair, format)

    def lt(other: JblasMatrix[T]) = matrix(storage.lt(other.jblas))(double2boolean, formatBoolean)
    def le(other: JblasMatrix[T]) = matrix(storage.le(other.jblas))(double2boolean, formatBoolean)
    def gt(other: JblasMatrix[T]) = matrix(storage.gt(other.jblas))(double2boolean, formatBoolean)
    def ge(other: JblasMatrix[T]) = matrix(storage.ge(other.jblas))(double2boolean, formatBoolean)
    def eq(other: JblasMatrix[T]) = matrix(storage.eq(other.jblas))(double2boolean, formatBoolean)
    def ne(other: JblasMatrix[T]) = matrix(storage.ne(other.jblas))(double2boolean, formatBoolean)

    def and(other: JblasMatrix[T]) = matrix(storage.and(other.jblas))(double2boolean, formatBoolean)
    def or(other: JblasMatrix[T]) = matrix(storage.or(other.jblas))(double2boolean, formatBoolean)
    def xor(other: JblasMatrix[T]) = matrix(storage.xor(other.jblas))(double2boolean, formatBoolean)

    def not() = matrix(storage.not())(double2boolean, formatBoolean)

    def max() = functionPair.forward(storage.max())

    def argmax() = {
      val i = storage.argmax()
      (i % columns, i / columns)
    }

    def min() = functionPair.forward(storage.min())

    def argmin() = {
      val i = storage.argmin()
      (i % columns, i / columns)
    }

    def rowSums() = matrix(storage.rowSums)(functionPair, format)
    def columnSums() = matrix(storage.columnSums())(functionPair, format)

    def columnMins() = matrix(storage.columnMins())(functionPair, format)
    def columnMaxs() = matrix(storage.columnMaxs())(functionPair, format)
    def columnMeans() = matrix(storage.columnMeans())(functionPair, format)
    def sortColumns() = matrix(storage.sortColumns())(functionPair, format)

    def rowMins() = matrix(storage.rowMins())(functionPair, format)
    def rowMaxs() = matrix(storage.rowMaxs())(functionPair, format)
    def rowMeans() = matrix(storage.rowMeans())(functionPair, format)
    def sortRows() = matrix(storage.sortRows())(functionPair, format)

    // in-place operations

    def addi(x: T) = storage.addi(functionPair.backward(x))
    def subtracti(x: T) = storage.subi(functionPair.backward(x))
    def multiplyi(x: T) = storage.muli(functionPair.backward(x))
    def matrixMultiplyi(x: T) = storage.mmuli(functionPair.backward(x))
    def dividei(x: T) = storage.divi(functionPair.backward(x))
    def ceili() = org.jblas.MatrixFunctions.ceili(storage)
    def floori() = org.jblas.MatrixFunctions.floori(storage)
    def logi() = org.jblas.MatrixFunctions.logi(storage)
    def log10i() = org.jblas.MatrixFunctions.log10i(storage)
    def powi(p: Double) = org.jblas.MatrixFunctions.powi(storage, p)
    def addMatrixi(other: JblasMatrix[T]) = storage.addi(other.jblas)
    def subtractMatrixi(other: JblasMatrix[T]) = storage.subi(other.jblas)
    def addiRowVector(row: JblasMatrix[T]) = storage.addiRowVector(row.jblas)
    def addiColumnVector(column: JblasMatrix[T]) = storage.addiColumnVector(column.jblas)
    def subiRowVector(row: JblasMatrix[T]) = storage.subiRowVector(row.jblas)
    def subiColumnVector(column: JblasMatrix[T]) = storage.subiColumnVector(column.jblas)

    override def toString() =
      (0 until rows).map(i => (0 until columns).map(j => format(functionPair.forward(storage.get(i, j)))).mkString(" ")).mkString("\n")

    def jblas() = storage
  }

  def matrix[T](s: DoubleMatrix)(implicit fp: FunctionPair[Double, T], format: T => String): JblasMatrix[T] = {
    new JblasMatrixImpl[T](s)(fp, format)
  }

  def matrix[T](r: Int, c: Int, values: Array[T])(implicit fp: FunctionPair[Double, T], format: T => String): JblasMatrix[T] = {
    val jblas = new org.jblas.DoubleMatrix(values.map(fp.backward(_)))
    jblas.reshape(r, c)
    matrix[T](jblas)(fp, format)
  }

  def diag[T](row: JblasMatrix[T])(implicit fp: FunctionPair[Double, T], format: T => String): JblasMatrix[T] = {
    assert(row.isRowVector)
    matrix[T](DoubleMatrix.diag(row.jblas))
  }

  def zeros[T](m: Int, n: Int)(implicit fp: FunctionPair[Double, T], format: T => String) = matrix[T](DoubleMatrix.zeros(m, n))(fp, format)
  def ones[T](m: Int, n: Int)(implicit fp: FunctionPair[Double, T], format: T => String) = matrix[T](DoubleMatrix.ones(m, n))(fp, format)
  def eye[T](n: Int)(implicit fp: FunctionPair[Double, T], format: T => String) = matrix[T](DoubleMatrix.eye(n))(fp, format)
  def I[T](n: Int)(implicit fp: FunctionPair[Double, T], format: T => String) = eye[T](n)(fp, format)
  def rand[T](m: Int, n: Int)(implicit fp: FunctionPair[Double, T], format: T => String) = matrix[T](DoubleMatrix.rand(m, n))(fp, format) // evenly distributed from 0.0 to 1.0
  def randn[T](m: Int, n: Int)(implicit fp: FunctionPair[Double, T], format: T => String) = matrix[T](DoubleMatrix.randn(m, n))(fp, format) // normal distribution 

  def falses(m: Int, n: Int) = matrix[Boolean](DoubleMatrix.zeros(m, n))
  def trues(m: Int, n: Int) = matrix[Boolean](DoubleMatrix.ones(m, n))

  // TODO: Int jblas' rand and randn should probably floor the result

  // additional matrix functions

  def median(m: M[Double]): M[Double] = {
    val sorted = m.sortColumns
    if (m.rows % 2 == 0) {
      (sorted.row(m.rows / 2 - 1) + sorted.row(m.rows / 2)) / 2.0
    } else {
      sorted.row(m.rows / 2)
    }
  }

  def centerRows(m: M[Double]): M[Double] = m.subColumnVector(m.rowMeans)
  def centerColumns(m: M[Double]): M[Double] = m.subRowVector(m.columnMeans)

  def rowRange(m: M[Double]): M[Double] = m.rowMaxs - m.rowMins
  def columnRange(m: M[Double]): M[Double] = m.columnMaxs - m.columnMins

  def sumsq(m: M[Double]): M[Double] = m.mulPointwise(m).columnSums

  def cov(m: M[Double]): M[Double] = (centerColumns(m).t ⨯ centerColumns(m)) / m.columns

  def std(m: M[Double]): M[Double] = matrix(1, m.columns, (sumsq(centerColumns(m)) / m.columns).toList.map(sqrt(_)).toArray)

  def zscore(m: M[Double]): M[Double] = centerColumns(m).divRowVector(std(m))

}

