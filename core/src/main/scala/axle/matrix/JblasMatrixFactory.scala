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

  class JblasMatrixImpl[T](_storage: DoubleMatrix)(fp: FunctionPair[Double, T])
    extends JblasMatrix[T] {
    def storage = _storage
    val functionPair = fp
  }

  trait JblasMatrix[T]
    extends Matrix[T] {

    type S = DoubleMatrix

    // type BM = JblasMatrix[Boolean]

    val functionPair: FunctionPair[Double, T]

    def rows() = storage.rows
    def columns() = storage.columns
    def length() = storage.length

    def apply(i: Int, j: Int): T = functionPair.forward(storage.get(i, j))
    def update(i: Int, j: Int, v: T) = storage.put(i, j, functionPair.backward(v))

    def toList(): List[T] = storage.toArray.toList.map(functionPair.forward(_))

    def column(j: Int) = matrix(storage.getColumn(j))(functionPair)
    def row(i: Int) = matrix(storage.getRow(i))(functionPair)

    def isEmpty() = storage.isEmpty
    def isRowVector() = storage.isRowVector
    def isColumnVector() = storage.isColumnVector
    def isVector() = storage.isVector
    def isSquare() = storage.isSquare
    def isScalar() = storage.isScalar

    def dup() = matrix(storage.dup())(functionPair)
    def negate() = matrix(storage.neg())(functionPair)
    def transpose() = matrix(storage.transpose())(functionPair)
    def diag() = matrix(storage.diag())(functionPair)
    def invert() = matrix(org.jblas.Solve.solve(storage, DoubleMatrix.eye(storage.rows)))(functionPair)
    def ceil() = matrix(org.jblas.MatrixFunctions.ceil(storage))(functionPair)
    def floor() = matrix(org.jblas.MatrixFunctions.floor(storage))(functionPair)
    def log() = matrix(org.jblas.MatrixFunctions.log(storage))(functionPair)
    def log10() = matrix(org.jblas.MatrixFunctions.log10(storage))(functionPair)

    def fullSVD() = {
      val usv = org.jblas.Singular.fullSVD(storage)
      (matrix(usv(0))(functionPair), matrix(usv(1))(functionPair), matrix(usv(2))(functionPair))
    }

    def addScalar(x: T) = matrix(storage.add(functionPair.backward(x)))(functionPair)
    def subtractScalar(x: T) = matrix(storage.sub(functionPair.backward(x)))(functionPair)
    def multiplyScalar(x: T) = matrix(storage.mul(functionPair.backward(x)))(functionPair)
    def divideScalar(x: T) = matrix(storage.div(functionPair.backward(x)))(functionPair)
    def mulRow(i: Int, x: T) = matrix(storage.mulRow(i, functionPair.backward(x)))(functionPair)
    def mulColumn(i: Int, x: T) = matrix(storage.mulColumn(i, functionPair.backward(x)))(functionPair)

    def pow(p: Double) = matrix(org.jblas.MatrixFunctions.pow(storage, p))(functionPair)

    def addMatrix(other: JblasMatrix[T]) = matrix(storage.add(other.jblas))(functionPair)
    def subtractMatrix(other: JblasMatrix[T]) = matrix(storage.sub(other.jblas))(functionPair)
    def multiplyMatrix(other: JblasMatrix[T]) = matrix(storage.mmul(other.jblas))(functionPair)

    def concatenateHorizontally(right: JblasMatrix[T]) = matrix(DoubleMatrix.concatHorizontally(storage, right.jblas))(functionPair)
    def concatenateVertically(under: JblasMatrix[T]) = matrix(DoubleMatrix.concatVertically(storage, under.jblas))(functionPair)
    def solve(B: JblasMatrix[T]) = matrix(org.jblas.Solve.solve(storage, B.jblas))(functionPair)

    def addRowVector(row: JblasMatrix[T]) = matrix(storage.addRowVector(row.jblas))(functionPair)
    def addColumnVector(column: JblasMatrix[T]) = matrix(storage.addRowVector(column.jblas))(functionPair)
    def subRowVector(row: JblasMatrix[T]) = matrix(storage.subRowVector(row.jblas))(functionPair)
    def subColumnVector(column: JblasMatrix[T]) = matrix(storage.subRowVector(column.jblas))(functionPair)

    def lt(other: JblasMatrix[T]) = matrix(storage.lt(other.jblas))(double2boolean)
    def le(other: JblasMatrix[T]) = matrix(storage.le(other.jblas))(double2boolean)
    def gt(other: JblasMatrix[T]) = matrix(storage.gt(other.jblas))(double2boolean)
    def ge(other: JblasMatrix[T]) = matrix(storage.ge(other.jblas))(double2boolean)
    def eq(other: JblasMatrix[T]) = matrix(storage.eq(other.jblas))(double2boolean)
    def ne(other: JblasMatrix[T]) = matrix(storage.ne(other.jblas))(double2boolean)

    def and(other: JblasMatrix[T]) = matrix(storage.and(other.jblas))(double2boolean)
    def or(other: JblasMatrix[T]) = matrix(storage.or(other.jblas))(double2boolean)
    def xor(other: JblasMatrix[T]) = matrix(storage.xor(other.jblas))(double2boolean)

    def not() = matrix(storage.not())(double2boolean)

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

    def rowSums() = matrix(storage.rowSums)(functionPair)
    def columnSums() = matrix(storage.columnSums())(functionPair)
    
    def columnMins() = matrix(storage.columnMins())(functionPair)
    def columnMaxs() = matrix(storage.columnMaxs())(functionPair)

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
      (0 until rows).map(i => (0 until columns).map(j => functionPair.forward(storage.get(i, j))).mkString(" ")).mkString("\n")

    def jblas() = storage
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
    matrix[T](DoubleMatrix.diag(row.jblas))
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

