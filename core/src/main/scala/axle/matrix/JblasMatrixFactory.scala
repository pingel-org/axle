package axle.matrix

import math.sqrt
import axle.square

// not necessarily a bijection, but related
trait FunctionPair[A, B] {
  val forward: A => B
  val backward: B => A
}

import org.jblas.DoubleMatrix

object JblasMatrixFactory extends JblasMatrixFactory {

}

trait JblasMatrixFactory extends MatrixFactory {

  factory =>

  case class JblasElementAdapter[I](fp: FunctionPair[Double, I], format: I => String)

  implicit val double2double = new FunctionPair[Double, Double] {
    val forward = (d: Double) => d
    val backward = (t: Double) => t
  }

  implicit val formatDouble = (d: Double) => "%.6f".format(d)

  implicit val elementAdapterDouble = JblasElementAdapter(double2double, formatDouble)

  implicit val double2int = new FunctionPair[Double, Int] {
    val forward = (d: Double) => d.toInt
    val backward = (t: Int) => t.toDouble
  }

  implicit val formatInt = (i: Int) => i.toString

  implicit val elementAdapterInt = JblasElementAdapter(double2int, formatInt)

  implicit val double2boolean = new FunctionPair[Double, Boolean] {
    val forward = (d: Double) => d != 0.0
    val backward = (t: Boolean) => t match { case true => 0.0 case false => 1.0 }
  }

  implicit val formatBoolean = (b: Boolean) => b.toString

  implicit val elementAdapterBoolean = JblasElementAdapter(double2boolean, formatBoolean)

  type M[T] = JblasMatrix[T]

  type E[T] = JblasElementAdapter[T]

  class JblasMatrixImpl[T](_storage: DoubleMatrix)(_ea: JblasElementAdapter[T])
    extends JblasMatrix[T] {
    def storage = _storage
    val elementAdapter = _ea
  }

  trait JblasMatrix[T]
    extends Matrix[T] {

    type S = DoubleMatrix

    val elementAdapter: JblasElementAdapter[T]

    //    val functionPair: FunctionPair[Double, T]
    //    val format: T => String

    def rows() = storage.rows
    def columns() = storage.columns
    def length() = storage.length

    def apply(i: Int, j: Int): T = elementAdapter.fp.forward(storage.get(i, j))

    def apply(rs: Seq[Int], cs: Seq[Int]): M[T] = {
      val jblas = DoubleMatrix.zeros(rs.length, cs.length)
      import elementAdapter.fp._
      for {
        (fromRow, toRow) <- rs.zipWithIndex
        (fromCol, toCol) <- cs.zipWithIndex
      } yield {
        jblas.put(toRow, toCol, backward(this(fromRow, fromCol)))
      }
      matrix[T](jblas)(elementAdapter)
    }
    // def update(i: Int, j: Int, v: T) = storage.put(i, j, elementAdapter.fp.backward(v))

    def toList(): List[T] = storage.toArray.toList.map(elementAdapter.fp.forward(_))

    def column(j: Int) = matrix(storage.getColumn(j))(elementAdapter)
    def row(i: Int) = matrix(storage.getRow(i))(elementAdapter)

    def isEmpty() = storage.isEmpty
    def isRowVector() = storage.isRowVector
    def isColumnVector() = storage.isColumnVector
    def isVector() = storage.isVector
    def isSquare() = storage.isSquare
    def isScalar() = storage.isScalar

    def dup() = matrix(storage.dup())(elementAdapter)
    def negate() = matrix(storage.neg())(elementAdapter)
    def transpose() = matrix(storage.transpose())(elementAdapter)
    def diag() = matrix(storage.diag())(elementAdapter)
    def invert() = matrix(org.jblas.Solve.solve(storage, DoubleMatrix.eye(storage.rows)))(elementAdapter)
    def ceil() = matrix(org.jblas.MatrixFunctions.ceil(storage))(elementAdapter)
    def floor() = matrix(org.jblas.MatrixFunctions.floor(storage))(elementAdapter)
    def log() = matrix(org.jblas.MatrixFunctions.log(storage))(elementAdapter)
    def log10() = matrix(org.jblas.MatrixFunctions.log10(storage))(elementAdapter)

    def fullSVD() = {
      val usv = org.jblas.Singular.fullSVD(storage).map(matrix(_)(elementAdapter))
      (usv(0), usv(1), usv(2))
    }

    def addScalar(x: T) = matrix(storage.add(elementAdapter.fp.backward(x)))(elementAdapter)
    def addAssignment(r: Int, c: Int, v: T): M[T] = {
      val jblas = storage.dup()
      import elementAdapter.fp._
      jblas.put(r, c, backward(v))
      matrix(jblas)(elementAdapter)
    }
    def subtractScalar(x: T) = matrix(storage.sub(elementAdapter.fp.backward(x)))(elementAdapter)
    def multiplyScalar(x: T) = matrix(storage.mul(elementAdapter.fp.backward(x)))(elementAdapter)
    def divideScalar(x: T) = matrix(storage.div(elementAdapter.fp.backward(x)))(elementAdapter)
    def mulRow(i: Int, x: T) = matrix(storage.mulRow(i, elementAdapter.fp.backward(x)))(elementAdapter)
    def mulColumn(i: Int, x: T) = matrix(storage.mulColumn(i, elementAdapter.fp.backward(x)))(elementAdapter)

    def pow(p: Double) = matrix(org.jblas.MatrixFunctions.pow(storage, p))(elementAdapter)

    def addMatrix(other: JblasMatrix[T]) = matrix(storage.add(other.jblas))(elementAdapter)
    def subtractMatrix(other: JblasMatrix[T]) = matrix(storage.sub(other.jblas))(elementAdapter)
    def multiplyMatrix(other: JblasMatrix[T]) = matrix(storage.mmul(other.jblas))(elementAdapter)

    def mulPointwise(other: JblasMatrix[T]) = matrix(storage.mul(other.jblas))(elementAdapter)
    def divPointwise(other: JblasMatrix[T]) = matrix(storage.div(other.jblas))(elementAdapter)

    def concatenateHorizontally(right: JblasMatrix[T]) = matrix(DoubleMatrix.concatHorizontally(storage, right.jblas))(elementAdapter)
    def concatenateVertically(under: JblasMatrix[T]) = matrix(DoubleMatrix.concatVertically(storage, under.jblas))(elementAdapter)
    def solve(B: JblasMatrix[T]) = matrix(org.jblas.Solve.solve(storage, B.jblas))(elementAdapter)

    def addRowVector(row: JblasMatrix[T]) = matrix(storage.addRowVector(row.jblas))(elementAdapter)
    def addColumnVector(column: JblasMatrix[T]) = matrix(storage.addColumnVector(column.jblas))(elementAdapter)
    def subRowVector(row: JblasMatrix[T]) = matrix(storage.subRowVector(row.jblas))(elementAdapter)
    def subColumnVector(column: JblasMatrix[T]) = matrix(storage.subColumnVector(column.jblas))(elementAdapter)
    def mulRowVector(row: JblasMatrix[T]) = matrix(storage.mulRowVector(row.jblas))(elementAdapter)
    def mulColumnVector(column: JblasMatrix[T]) = matrix(storage.mulColumnVector(column.jblas))(elementAdapter)
    def divRowVector(row: JblasMatrix[T]) = matrix(storage.divRowVector(row.jblas))(elementAdapter)
    def divColumnVector(column: JblasMatrix[T]) = matrix(storage.divColumnVector(column.jblas))(elementAdapter)

    def lt(other: JblasMatrix[T]) = matrix(storage.lt(other.jblas))(elementAdapterBoolean)
    def le(other: JblasMatrix[T]) = matrix(storage.le(other.jblas))(elementAdapterBoolean)
    def gt(other: JblasMatrix[T]) = matrix(storage.gt(other.jblas))(elementAdapterBoolean)
    def ge(other: JblasMatrix[T]) = matrix(storage.ge(other.jblas))(elementAdapterBoolean)
    def eq(other: JblasMatrix[T]) = matrix(storage.eq(other.jblas))(elementAdapterBoolean)
    def ne(other: JblasMatrix[T]) = matrix(storage.ne(other.jblas))(elementAdapterBoolean)

    def and(other: JblasMatrix[T]) = matrix(storage.and(other.jblas))(elementAdapterBoolean)
    def or(other: JblasMatrix[T]) = matrix(storage.or(other.jblas))(elementAdapterBoolean)
    def xor(other: JblasMatrix[T]) = matrix(storage.xor(other.jblas))(elementAdapterBoolean)

    def not() = matrix(storage.not())(elementAdapterBoolean)

    def max() = elementAdapter.fp.forward(storage.max())

    def argmax() = {
      val i = storage.argmax()
      (i % columns, i / columns)
    }

    def min() = elementAdapter.fp.forward(storage.min())

    def argmin() = {
      val i = storage.argmin()
      (i % columns, i / columns)
    }

    def rowSums() = matrix(storage.rowSums)(elementAdapter)
    def columnSums() = matrix(storage.columnSums())(elementAdapter)

    def columnMins() = matrix(storage.columnMins())(elementAdapter)
    def columnMaxs() = matrix(storage.columnMaxs())(elementAdapter)
    def columnMeans() = matrix(storage.columnMeans())(elementAdapter)
    def sortColumns() = matrix(storage.sortColumns())(elementAdapter)

    def rowMins() = matrix(storage.rowMins())(elementAdapter)
    def rowMaxs() = matrix(storage.rowMaxs())(elementAdapter)
    def rowMeans() = matrix(storage.rowMeans())(elementAdapter)
    def sortRows() = matrix(storage.sortRows())(elementAdapter)

    // in-place operations

    //    def addi(x: T) = storage.addi(elementAdapter.fp.backward(x))
    //    def subtracti(x: T) = storage.subi(elementAdapter.fp.backward(x))
    //    def multiplyi(x: T) = storage.muli(elementAdapter.fp.backward(x))
    //    def matrixMultiplyi(x: T) = storage.mmuli(elementAdapter.fp.backward(x))
    //    def dividei(x: T) = storage.divi(elementAdapter.fp.backward(x))
    //    def ceili() = org.jblas.MatrixFunctions.ceili(storage)
    //    def floori() = org.jblas.MatrixFunctions.floori(storage)
    //    def logi() = org.jblas.MatrixFunctions.logi(storage)
    //    def log10i() = org.jblas.MatrixFunctions.log10i(storage)
    //    def powi(p: Double) = org.jblas.MatrixFunctions.powi(storage, p)
    //    def addMatrixi(other: JblasMatrix[T]) = storage.addi(other.jblas)
    //    def subtractMatrixi(other: JblasMatrix[T]) = storage.subi(other.jblas)
    //    def addiRowVector(row: JblasMatrix[T]) = storage.addiRowVector(row.jblas)
    //    def addiColumnVector(column: JblasMatrix[T]) = storage.addiColumnVector(column.jblas)
    //    def subiRowVector(row: JblasMatrix[T]) = storage.subiRowVector(row.jblas)
    //    def subiColumnVector(column: JblasMatrix[T]) = storage.subiColumnVector(column.jblas)

    // higher order methods

    def map[B](f: T => B)(implicit elementAdapter: E[B]): M[B] = {
      val jblas = DoubleMatrix.zeros(rows, columns)
      import elementAdapter.fp._
      for {
        r <- 0 until rows
        c <- 0 until columns
      } yield {
        jblas.put(r, c, backward(f(this(r, c))))
      }
      matrix[B](jblas)
    }

    def flatMapColumns[A](f: M[T] => M[A])(implicit elementAdapter: E[A]): M[A] = {
      val jblas = DoubleMatrix.zeros(rows, columns)
      import elementAdapter.fp._
      for {
        c <- 0 until columns
      } yield {
        val fc = f(column(c))
        for {
          r <- (0 until rows) // assumes fc.rows == this.rows
        } yield {
          jblas.put(r, c, backward(fc(r, 0)))
        }
      }
      matrix[A](jblas)
    }

    override def toString() =
      (0 until rows).map(i => (0 until columns).map(j => elementAdapter.format(elementAdapter.fp.forward(storage.get(i, j)))).mkString(" ")).mkString("\n")

    def jblas() = storage
  }

  // methods for creating matrices

  def matrix[T](s: DoubleMatrix)(implicit elementAdapter: E[T]): JblasMatrix[T] =
    new JblasMatrixImpl[T](s)(elementAdapter)

  def matrix[T](r: Int, c: Int, values: Array[T])(implicit elementAdapter: E[T]): JblasMatrix[T] = {
    val jblas = new org.jblas.DoubleMatrix(values.map(elementAdapter.fp.backward(_)))
    jblas.reshape(r, c)
    matrix[T](jblas)(elementAdapter)
  }

  def matrix[T](m: Int, n: Int, topleft: => T, left: Int => T, top: Int => T, fill: (Int, Int, T, T, T) => T)(implicit elementAdapter: E[T]): M[T] = {
    val jblas = DoubleMatrix.zeros(m, n)
    import elementAdapter.fp._
    jblas.put(0, 0, backward(topleft))
    (0 until m).map(r => jblas.put(r, 0, backward(left(r))))
    (0 until n).map(c => jblas.put(0, c, backward(top(c))))
    for {
      r <- 1 until m
      c <- 1 until n
    } yield {
      val diag = forward(jblas.get(r - 1, c - 1))
      val left = forward(jblas.get(r, c - 1))
      val right = forward(jblas.get(r - 1, c))
      jblas.put(r, c, backward(fill(r, c, diag, left, right)))
    }
    matrix[T](jblas)
  }

  def matrix[T](m: Int, n: Int, f: (Int, Int) => T)(implicit elementAdapter: E[T]): M[T] = {
    val jblas = DoubleMatrix.zeros(m, n)
    import elementAdapter.fp._
    for {
      r <- 0 until m
      c <- 0 until n
    } yield {
      jblas.put(r, c, backward(f(r, c)))
    }
    matrix[T](jblas)
  }

  def diag[T](row: JblasMatrix[T])(implicit elementAdapter: E[T]): JblasMatrix[T] = {
    assert(row.isRowVector)
    matrix[T](DoubleMatrix.diag(row.jblas))
  }

  def zeros[T](m: Int, n: Int)(implicit elementAdapter: JblasElementAdapter[T]) = matrix[T](DoubleMatrix.zeros(m, n))(elementAdapter)
  def ones[T](m: Int, n: Int)(implicit elementAdapter: JblasElementAdapter[T]) = matrix[T](DoubleMatrix.ones(m, n))(elementAdapter)
  def eye[T](n: Int)(implicit elementAdapter: JblasElementAdapter[T]) = matrix[T](DoubleMatrix.eye(n))(elementAdapter)
  def I[T](n: Int)(implicit elementAdapter: JblasElementAdapter[T]) = eye[T](n)(elementAdapter)
  def rand[T](m: Int, n: Int)(implicit elementAdapter: JblasElementAdapter[T]) = matrix[T](DoubleMatrix.rand(m, n))(elementAdapter) // evenly distributed from 0.0 to 1.0
  def randn[T](m: Int, n: Int)(implicit elementAdapter: JblasElementAdapter[T]) = matrix[T](DoubleMatrix.randn(m, n))(elementAdapter) // normal distribution 

  def falses(m: Int, n: Int) = matrix[Boolean](DoubleMatrix.zeros(m, n))
  def trues(m: Int, n: Int) = matrix[Boolean](DoubleMatrix.ones(m, n))

  // TODO: Int jblas' rand and randn should probably floor the result

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

  def std(m: M[Double]): M[Double] = (sumsq(centerColumns(m)) / m.columns).map(sqrt(_))

  def zscore(m: M[Double]): M[Double] = centerColumns(m).divRowVector(std(m))

  /**
   * Principal Component Analysis (PCA)
   *
   * assumes that the input matrix, Xnorm, has been normalized, in other words:
   *   mean of each column == 0.0
   *   stddev of each column == 1.0 (I'm not clear if this is a strict requirement)
   *
   * http://folk.uio.no/henninri/pca_module/
   * http://public.lanl.gov/mewall/kluwer2002.html
   * https://mailman.cae.wisc.edu/pipermail/help-octave/2004-May/012772.html
   *
   * @return (U, S) where U = eigenvectors and S = eigenvalues (truncated to requested cutoff)
   *
   */

  def pca(Xnorm: M[Double], cutoff: Double = 0.95): (M[Double], M[Double]) = {
    val (u, s, v) = cov(Xnorm).fullSVD
    (u, s)
  }

  def numComponentsForCutoff(s: M[Double], cutoff: Double): Int = {
    val eigenValuesSquared = s.map(square(_)).toList
    val eigenTotal = eigenValuesSquared.sum
    val numComponents = eigenValuesSquared.map(_ / eigenTotal).scan(0.0)(_ + _).indexWhere(cutoff<)
    numComponents
    // matrix(s.rows, 1, (0 until s.rows).map(r => if (r < numComponents) { s(r, 0) } else { 0.0 }).toArray)
  }

}

