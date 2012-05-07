package axle.matrix

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

  implicit val double2int = new FunctionPair[Double, Int] {
    val forward = (d: Double) => d.toInt
    val backward = (t: Int) => t.toDouble
  }

  implicit val double2boolean = new FunctionPair[Double, Boolean] {
    val forward = (d: Double) => d != 0.0
    val backward = (t: Boolean) => t match { case true => 0.0 case false => 1.0 }
  }

  type M[T] = JblasMatrix[T]

  class JblasMatrixImpl[T](storage: DoubleMatrix)(fp: FunctionPair[Double, T])
    extends JblasMatrix[T] {
    def getStorage = storage
    val functionPair = fp
  }

  trait JblasMatrix[T]
    extends Matrix[T] {

    type S = DoubleMatrix

    // type BM = JblasMatrix[Boolean]

    val functionPair: FunctionPair[Double, T]

    def rows() = getStorage.rows
    def columns() = getStorage.columns
    def length() = getStorage.length

    def valueAt(i: Int, j: Int): T = functionPair.forward(getStorage.get(i, j))
    def setValueAt(i: Int, j: Int, v: T) = getStorage.put(i, j, functionPair.backward(v))

    def getColumn(j: Int) = matrix(getStorage.getColumn(j))(functionPair)
    def getRow(i: Int) = matrix(getStorage.getRow(i))(functionPair)

    def isEmpty() = getStorage.isEmpty
    def isRowVector() = getStorage.isRowVector
    def isColumnVector() = getStorage.isColumnVector
    def isVector() = getStorage.isVector
    def isSquare() = getStorage.isSquare
    def isScalar() = getStorage.isScalar

    def dup() = matrix(getStorage.dup())(functionPair)
    def negate() = matrix(getStorage.neg())(functionPair)
    def transpose() = matrix(getStorage.transpose())(functionPair)
    def diag() = matrix(getStorage.diag())(functionPair)
    def invert() = matrix(org.jblas.Solve.solve(getStorage, DoubleMatrix.eye(getStorage.rows)))(functionPair)
    def ceil() = matrix(org.jblas.MatrixFunctions.ceil(getStorage))(functionPair)
    def floor() = matrix(org.jblas.MatrixFunctions.floor(getStorage))(functionPair)
    def log() = matrix(org.jblas.MatrixFunctions.log(getStorage))(functionPair)
    def log10() = matrix(org.jblas.MatrixFunctions.log10(getStorage))(functionPair)

    def fullSVD() = {
      val usv = org.jblas.Singular.fullSVD(getStorage)
      (matrix(usv(0))(functionPair), matrix(usv(1))(functionPair), matrix(usv(2))(functionPair))
    }

    def addScalar(x: T) = matrix(getStorage.add(functionPair.backward(x)))(functionPair)
    def subtractScalar(x: T) = matrix(getStorage.sub(functionPair.backward(x)))(functionPair)
    def multiplyScalar(x: T) = matrix(getStorage.mul(functionPair.backward(x)))(functionPair)
    def divideScalar(x: T) = matrix(getStorage.div(functionPair.backward(x)))(functionPair)
    def mulRow(i: Int, x: T) = matrix(getStorage.mulRow(i, functionPair.backward(x)))(functionPair)
    def mulColumn(i: Int, x: T) = matrix(getStorage.mulColumn(i, functionPair.backward(x)))(functionPair)

    def pow(p: Double) = matrix(org.jblas.MatrixFunctions.pow(getStorage, p))(functionPair)

    def addMatrix(other: JblasMatrix[T]) = matrix(getStorage.add(other.getJblas))(functionPair)
    def subtractMatrix(other: JblasMatrix[T]) = matrix(getStorage.sub(other.getJblas))(functionPair)
    def multiplyMatrix(other: JblasMatrix[T]) = matrix(getStorage.mmul(other.getJblas))(functionPair)

    def concatenateHorizontally(right: JblasMatrix[T]) = matrix(DoubleMatrix.concatHorizontally(getStorage, right.getJblas))(functionPair)
    def concatenateVertically(under: JblasMatrix[T]) = matrix(DoubleMatrix.concatVertically(getStorage, under.getJblas))(functionPair)
    def solve(B: JblasMatrix[T]) = matrix(org.jblas.Solve.solve(getStorage, B.getJblas))(functionPair)

    def addRowVector(row: JblasMatrix[T]) = matrix(getStorage.addRowVector(row.getJblas))(functionPair)
    def addColumnVector(column: JblasMatrix[T]) = matrix(getStorage.addRowVector(column.getJblas))(functionPair)
    def subRowVector(row: JblasMatrix[T]) = matrix(getStorage.subRowVector(row.getJblas))(functionPair)
    def subColumnVector(column: JblasMatrix[T]) = matrix(getStorage.subRowVector(column.getJblas))(functionPair)

    def lt(other: JblasMatrix[T]) = matrix(getStorage.lt(other.getJblas))(double2boolean)
    def le(other: JblasMatrix[T]) = matrix(getStorage.le(other.getJblas))(double2boolean)
    def gt(other: JblasMatrix[T]) = matrix(getStorage.gt(other.getJblas))(double2boolean)
    def ge(other: JblasMatrix[T]) = matrix(getStorage.ge(other.getJblas))(double2boolean)
    def eq(other: JblasMatrix[T]) = matrix(getStorage.eq(other.getJblas))(double2boolean)
    def ne(other: JblasMatrix[T]) = matrix(getStorage.ne(other.getJblas))(double2boolean)

    def and(other: JblasMatrix[T]) = matrix(getStorage.and(other.getJblas))(double2boolean)
    def or(other: JblasMatrix[T]) = matrix(getStorage.or(other.getJblas))(double2boolean)
    def xor(other: JblasMatrix[T]) = matrix(getStorage.xor(other.getJblas))(double2boolean)

    def not() = matrix(getStorage.not())(double2boolean)

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

    def columnMins() = matrix(getStorage.columnMins())(functionPair)
    def columnMaxs() = matrix(getStorage.columnMaxs())(functionPair)

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
    def addMatrixi(other: JblasMatrix[T]) = getStorage.addi(other.getJblas)
    def subtractMatrixi(other: JblasMatrix[T]) = getStorage.subi(other.getJblas)
    def addiRowVector(row: JblasMatrix[T]) = getStorage.addiRowVector(row.getJblas)
    def addiColumnVector(column: JblasMatrix[T]) = getStorage.addiColumnVector(column.getJblas)
    def subiRowVector(row: JblasMatrix[T]) = getStorage.subiRowVector(row.getJblas)
    def subiColumnVector(column: JblasMatrix[T]) = getStorage.subiColumnVector(column.getJblas)

    override def toString() =
      (0 until rows).map(i => (0 until columns).map(j => functionPair.forward(getStorage.get(i, j))).mkString(" ")).mkString("\n")

    def getJblas() = getStorage
  }

  def matrix[T](s: DoubleMatrix)(implicit fp: FunctionPair[Double, T]): JblasMatrix[T] = {
    new JblasMatrixImpl[T](s)(fp)
  }

  def matrix[T](r: Int, c: Int, values: Array[T])(implicit fp: FunctionPair[Double, T]): JblasMatrix[T] = {
    val jblas = new org.jblas.DoubleMatrix(values.map(fp.backward(_)))
    jblas.reshape(r, c)
    matrix[T](jblas)(fp)
  }

  def diag[T](row: JblasMatrix[T])(implicit fp: FunctionPair[Double, T]): JblasMatrix[T] = {
    assert(row.isRowVector)
    matrix[T](DoubleMatrix.diag(row.getJblas))
  }

  def zeros[T](m: Int, n: Int)(implicit fp: FunctionPair[Double, T]) = matrix[T](DoubleMatrix.zeros(m, n))(fp)
  def ones[T](m: Int, n: Int)(implicit fp: FunctionPair[Double, T]) = matrix[T](DoubleMatrix.ones(m, n))(fp)
  def eye[T](n: Int)(implicit fp: FunctionPair[Double, T]) = matrix[T](DoubleMatrix.eye(n))(fp)
  def I[T](n: Int)(implicit fp: FunctionPair[Double, T]) = eye[T](n)(fp)
  def rand[T](m: Int, n: Int)(implicit fp: FunctionPair[Double, T]) = matrix[T](DoubleMatrix.rand(m, n))(fp) // evenly distributed from 0.0 to 1.0
  def randn[T](m: Int, n: Int)(implicit fp: FunctionPair[Double, T]) = matrix[T](DoubleMatrix.randn(m, n))(fp) // normal distribution 

  def falses(m: Int, n: Int) = matrix[Boolean](DoubleMatrix.zeros(m, n))
  def trues(m: Int, n: Int) = matrix[Boolean](DoubleMatrix.ones(m, n))

  // TODO: Int jblas' rand and randn should probably floor the result

}

