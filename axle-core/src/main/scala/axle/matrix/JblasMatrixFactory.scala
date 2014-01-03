package axle.matrix

import math.sqrt
import axle.algebra.FunctionPair
import org.jblas.DoubleMatrix

object JblasMatrixModule extends JblasMatrixModule

trait JblasMatrixModule extends MatrixModule {

  type C[T] = FunctionPair[Double, T]

  implicit val convertDouble: C[Double] = new FunctionPair[Double, Double] {
    val forward = (d: Double) => d
    val backward = (t: Double) => t
  }

  implicit val convertInt: C[Int] = new FunctionPair[Double, Int] {
    val forward = (d: Double) => d.toInt
    val backward = (t: Int) => t.toDouble
  }

  implicit val convertBoolean: C[Boolean] = new FunctionPair[Double, Boolean] {
    val forward = (d: Double) => d != 0d
    val backward = (t: Boolean) => t match { case true => 0d case false => 1d }
  }

  class Matrix[T: C](_storage: DoubleMatrix) extends MatrixLike[T] {

    val fp = implicitly[C[T]]

    type S = DoubleMatrix

    def storage: S = _storage

    implicit val format = (t: T) => t match {
      case d: Double => """%.6f""".format(d)
      case _ => t.toString
    }

    def rows: Int = storage.rows
    def columns: Int = storage.columns
    def length: Int = storage.length

    def apply(i: Int, j: Int): T = fp.forward(storage.get(i, j))

    def apply(rs: Seq[Int], cs: Seq[Int]): Matrix[T] = {
      val jblas = DoubleMatrix.zeros(rs.length, cs.length)
      import fp._
      rs.zipWithIndex foreach {
        case (fromRow, toRow) =>
          cs.zipWithIndex foreach {
            case (fromCol, toCol) =>
              jblas.put(toRow, toCol, backward(this(fromRow, fromCol)))
          }
      }
      matrix[T](jblas)
    }
    // def update(i: Int, j: Int, v: T) = storage.put(i, j, elementAdapter.fp.backward(v))

    def toList: List[T] = storage.toArray.toList.map(fp.forward)

    def column(j: Int): Matrix[T] = matrix(storage.getColumn(j))
    def row(i: Int): Matrix[T] = matrix(storage.getRow(i))

    def isEmpty: Boolean = storage.isEmpty
    def isRowVector: Boolean = storage.isRowVector
    def isColumnVector: Boolean = storage.isColumnVector
    def isVector: Boolean = storage.isVector
    def isSquare: Boolean = storage.isSquare
    def isScalar: Boolean = storage.isScalar

    def dup: Matrix[T] = matrix(storage.dup)
    def negate: Matrix[T] = matrix(storage.neg)
    def transpose: Matrix[T] = matrix(storage.transpose)
    def diag: Matrix[T] = matrix(storage.diag)
    def invert: Matrix[T] = matrix(org.jblas.Solve.solve(storage, DoubleMatrix.eye(storage.rows)))
    def ceil: Matrix[Int] = matrix(org.jblas.MatrixFunctions.ceil(storage))(convertInt)
    def floor: Matrix[Int] = matrix(org.jblas.MatrixFunctions.floor(storage))(convertInt)
    def log: Matrix[Double] = matrix(org.jblas.MatrixFunctions.log(storage))(convertDouble)
    def log10: Matrix[Double] = matrix(org.jblas.MatrixFunctions.log10(storage))(convertDouble)

    def fullSVD: (Matrix[T], Matrix[T], Matrix[T]) = {
      val usv = org.jblas.Singular.fullSVD(storage).map(matrix(_)(fp))
      (usv(0), usv(1), usv(2))
    }

    def addScalar(x: T): Matrix[T] = matrix(storage.add(fp.backward(x)))
    def addAssignment(r: Int, c: Int, v: T): Matrix[T] = {
      val jblas = storage.dup()
      import fp._
      jblas.put(r, c, backward(v))
      matrix(jblas)(fp)
    }
    def subtractScalar(x: T): Matrix[T] = matrix(storage.sub(fp.backward(x)))
    def multiplyScalar(x: T): Matrix[T] = matrix(storage.mul(fp.backward(x)))
    def divideScalar(x: T): Matrix[T] = matrix(storage.div(fp.backward(x)))
    def mulRow(i: Int, x: T): Matrix[T] = matrix(storage.mulRow(i, fp.backward(x)))
    def mulColumn(i: Int, x: T): Matrix[T] = matrix(storage.mulColumn(i, fp.backward(x)))

    def pow(p: Double): Matrix[T] = matrix(org.jblas.MatrixFunctions.pow(storage, p))

    def addMatrix(other: Matrix[T]): Matrix[T] = matrix(storage.add(other.jblas))
    def subtractMatrix(other: Matrix[T]): Matrix[T] = matrix(storage.sub(other.jblas))
    def multiplyMatrix(other: Matrix[T]): Matrix[T] = matrix(storage.mmul(other.jblas))

    def mulPointwise(other: Matrix[T]): Matrix[T] = matrix(storage.mul(other.jblas))
    def divPointwise(other: Matrix[T]): Matrix[T] = matrix(storage.div(other.jblas))

    def concatenateHorizontally(right: Matrix[T]): Matrix[T] = matrix(DoubleMatrix.concatHorizontally(storage, right.jblas))
    def concatenateVertically(under: Matrix[T]): Matrix[T] = matrix(DoubleMatrix.concatVertically(storage, under.jblas))
    def solve(B: Matrix[T]): Matrix[T] = matrix(org.jblas.Solve.solve(storage, B.jblas))

    def addRowVector(row: Matrix[T]): Matrix[T] = matrix(storage.addRowVector(row.jblas))
    def addColumnVector(column: Matrix[T]): Matrix[T] = matrix(storage.addColumnVector(column.jblas))
    def subRowVector(row: Matrix[T]): Matrix[T] = matrix(storage.subRowVector(row.jblas))
    def subColumnVector(column: Matrix[T]): Matrix[T] = matrix(storage.subColumnVector(column.jblas))
    def mulRowVector(row: Matrix[T]): Matrix[T] = matrix(storage.mulRowVector(row.jblas))
    def mulColumnVector(column: Matrix[T]): Matrix[T] = matrix(storage.mulColumnVector(column.jblas))
    def divRowVector(row: Matrix[T]): Matrix[T] = matrix(storage.divRowVector(row.jblas))
    def divColumnVector(column: Matrix[T]): Matrix[T] = matrix(storage.divColumnVector(column.jblas))

    def lt(other: Matrix[T]): Matrix[Boolean] = matrix[Boolean](storage.lt(other.jblas))(convertBoolean)
    def le(other: Matrix[T]): Matrix[Boolean] = matrix(storage.le(other.jblas))(convertBoolean)
    def gt(other: Matrix[T]): Matrix[Boolean] = matrix(storage.gt(other.jblas))(convertBoolean)
    def ge(other: Matrix[T]): Matrix[Boolean] = matrix(storage.ge(other.jblas))(convertBoolean)
    def eq(other: Matrix[T]): Matrix[Boolean] = matrix(storage.eq(other.jblas))(convertBoolean)
    def ne(other: Matrix[T]): Matrix[Boolean] = matrix(storage.ne(other.jblas))(convertBoolean)
    def and(other: Matrix[T]): Matrix[Boolean] = matrix(storage.and(other.jblas))(convertBoolean)
    def or(other: Matrix[T]): Matrix[Boolean] = matrix(storage.or(other.jblas))(convertBoolean)
    def xor(other: Matrix[T]): Matrix[Boolean] = matrix(storage.xor(other.jblas))(convertBoolean)

    def not: Matrix[Boolean] = matrix(storage.not)(convertBoolean)

    def max: T = fp.forward(storage.max)

    def argmax: (Int, Int) = {
      val i = storage.argmax
      (i % columns, i / columns)
    }

    def min: T = fp.forward(storage.min)

    def argmin: (Int, Int) = {
      val i = storage.argmin
      (i % columns, i / columns)
    }

    def rowSums: Matrix[T] = matrix(storage.rowSums)
    def columnSums: Matrix[T] = matrix(storage.columnSums)

    def columnMins: Matrix[T] = matrix(storage.columnMins)
    def columnMaxs: Matrix[T] = matrix(storage.columnMaxs)
    def columnMeans: Matrix[T] = matrix(storage.columnMeans)
    def sortColumns: Matrix[T] = matrix(storage.sortColumns)

    def rowMins: Matrix[T] = matrix(storage.rowMins)
    def rowMaxs: Matrix[T] = matrix(storage.rowMaxs)
    def rowMeans: Matrix[T] = matrix(storage.rowMeans)
    def sortRows: Matrix[T] = matrix(storage.sortRows)

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

    def map[B: C](f: T => B): Matrix[B] = {
      val fpB = implicitly[C[B]]
      val jblas = DoubleMatrix.zeros(rows, columns)
      (0 until rows) foreach { r =>
        (0 until columns) foreach { c =>
          jblas.put(r, c, fpB.backward(f(this(r, c))))
        }
      }
      matrix[B](jblas)
    }

    def flatMapColumns[A: C](f: Matrix[T] => Matrix[A]): Matrix[A] = {
      val fpA = implicitly[C[A]]
      val jblas = DoubleMatrix.zeros(rows, columns)
      (0 until columns) foreach { c =>
        val fc = f(column(c))
        (0 until rows) foreach { r =>
          // assumes fc.rows == this.rows
          jblas.put(r, c, fpA.backward(fc(r, 0)))
        }
      }
      matrix[A](jblas)
    }

    override def toString: String =
      (0 until rows).map(i => (0 until columns).map(j => format(fp.forward(storage.get(i, j)))).mkString(" ")).mkString("\n")

    def jblas: S = storage
  }

  // methods for creating matrices

  def matrix[T: C](s: DoubleMatrix): Matrix[T] = new Matrix(s)

  def matrix[T: C](r: Int, c: Int, values: Array[T]): Matrix[T] = {
    val fp = implicitly[C[T]]
    val jblas = new org.jblas.DoubleMatrix(values.map(fp.backward))
    jblas.reshape(r, c)
    matrix(jblas)
  }

  def matrix[T: C](m: Int, n: Int, topleft: => T, left: Int => T, top: Int => T, fill: (Int, Int, T, T, T) => T): Matrix[T] = {
    val fp = implicitly[C[T]]
    import fp._
    val jblas = DoubleMatrix.zeros(m, n)
    jblas.put(0, 0, backward(topleft))
    (0 until m).map(r => jblas.put(r, 0, backward(left(r))))
    (0 until n).map(c => jblas.put(0, c, backward(top(c))))
    (1 until m) foreach { r =>
      (1 until n) foreach { c =>
        val diag = forward(jblas.get(r - 1, c - 1))
        val left = forward(jblas.get(r, c - 1))
        val right = forward(jblas.get(r - 1, c))
        jblas.put(r, c, backward(fill(r, c, diag, left, right)))
      }
    }
    matrix(jblas)
  }

  def matrix[T: C](m: Int, n: Int, f: (Int, Int) => T): Matrix[T] = {
    val fp = implicitly[C[T]]
    import fp._
    val jblas = DoubleMatrix.zeros(m, n)
    (0 until m) foreach { r =>
      (0 until n) foreach { c =>
        jblas.put(r, c, backward(f(r, c)))
      }
    }
    matrix(jblas)
  }

  def diag[T: C](row: Matrix[T]): Matrix[T] = {
    assert(row.isRowVector)
    matrix(DoubleMatrix.diag(row.jblas))
  }

  def zeros[T: C](m: Int, n: Int): Matrix[T] = matrix(DoubleMatrix.zeros(m, n))
  def ones[T: C](m: Int, n: Int): Matrix[T] = matrix(DoubleMatrix.ones(m, n))
  def eye[T: C](n: Int): Matrix[T] = matrix(DoubleMatrix.eye(n))
  def I[T: C](n: Int): Matrix[T] = eye(n)
  def rand[T: C](m: Int, n: Int): Matrix[T] = matrix(DoubleMatrix.rand(m, n)) // evenly distributed from 0.0 to 1.0
  def randn[T: C](m: Int, n: Int): Matrix[T] = matrix(DoubleMatrix.randn(m, n)) // normal distribution
  def falses(m: Int, n: Int): Matrix[Boolean] = matrix(DoubleMatrix.zeros(m, n))
  def trues(m: Int, n: Int): Matrix[Boolean] = matrix(DoubleMatrix.ones(m, n))

  // TODO: Int jblas' rand and randn should probably floor the result

  override def median(m: Matrix[Double]): Matrix[Double] = {
    val sorted = m.sortColumns
    if (m.rows % 2 == 0) {
      (sorted.row(m.rows / 2 - 1) + sorted.row(m.rows / 2)) / 2.0
    } else {
      sorted.row(m.rows / 2)
    }
  }

  def centerRows(m: Matrix[Double]): Matrix[Double] = m.subColumnVector(m.rowMeans)
  def centerColumns(m: Matrix[Double]): Matrix[Double] = m.subRowVector(m.columnMeans)

  def rowRange(m: Matrix[Double]): Matrix[Double] = m.rowMaxs - m.rowMins
  def columnRange(m: Matrix[Double]): Matrix[Double] = m.columnMaxs - m.columnMins

  def sumsq(m: Matrix[Double]): Matrix[Double] = m.mulPointwise(m).columnSums

  def cov(m: Matrix[Double]): Matrix[Double] = (centerColumns(m).t ⨯ centerColumns(m)) / m.columns

  def std(m: Matrix[Double]): Matrix[Double] = (sumsq(centerColumns(m)) / m.columns).map(sqrt)

  def zscore(m: Matrix[Double]): Matrix[Double] = centerColumns(m).divRowVector(std(m))

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

  def pca(Xnorm: Matrix[Double], cutoff: Double = 0.95): (Matrix[Double], Matrix[Double]) = {
    val (u, s, v) = cov(Xnorm).fullSVD
    (u, s)
  }

  def numComponentsForCutoff(s: Matrix[Double], cutoff: Double): Int = {
    val eigenValuesSquared = s.map((x: Double) => x * x).toList
    val eigenTotal = eigenValuesSquared.sum
    val numComponents = eigenValuesSquared.map(_ / eigenTotal).scan(0.0)(_ + _).indexWhere(cutoff<)
    numComponents
    // matrix(s.rows, 1, (0 until s.rows).map(r => if (r < numComponents) { s(r, 0) } else { 0.0 }).toArray)
  }

}

