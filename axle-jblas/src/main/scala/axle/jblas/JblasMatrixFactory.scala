package axle.jblas

import scala.math.sqrt

import org.jblas.DoubleMatrix

import axle.algebra.FunctionPair
import axle.Show
import axle.string
import axle.matrix.MatrixModule
import spire.implicits.IntAlgebra
import spire.implicits.eqOps

trait JblasMatrixModule extends MatrixModule {

  type C[T] = FunctionPair[Double, T]

  implicit val convertDouble: FunctionPair[Double, Double] = new FunctionPair[Double, Double] {
    def apply(d: Double) = d
    def unapply(t: Double) = t
  }

  implicit val convertInt: FunctionPair[Double, Int] = new FunctionPair[Double, Int] {
    def apply(d: Double) = d.toInt
    def unapply(t: Int) = t.toDouble
  }

  implicit val convertBoolean: FunctionPair[Double, Boolean] = new FunctionPair[Double, Boolean] {
    def apply(d: Double) = d != 0d
    def unapply(t: Boolean) = t match { case true => 0d case false => 1d }
  }

  class Matrix[T: C](val jblas: DoubleMatrix) extends MatrixLike[T] {

    val converter = implicitly[C[T]]

    type S = DoubleMatrix

    def underlying: DoubleMatrix = jblas

    def rows: Int = jblas.rows
    def columns: Int = jblas.columns
    def length: Int = jblas.length

    def apply(i: Int, j: Int): T = converter(jblas.get(i, j))

    def apply(rs: Seq[Int], cs: Seq[Int]): Matrix[T] = {
      val jblas = DoubleMatrix.zeros(rs.length, cs.length)
      rs.zipWithIndex foreach {
        case (fromRow, toRow) =>
          cs.zipWithIndex foreach {
            case (fromCol, toCol) =>
              jblas.put(toRow, toCol, converter.unapply(this(fromRow, fromCol)))
          }
      }
      matrix[T](jblas)
    }
    // def update(i: Int, j: Int, v: T) = jblas.put(i, j, elementAdapter.converter.unapply(v))

    def toList: List[T] = jblas.toArray.toList.map(converter.apply _)

    def column(j: Int): Matrix[T] = matrix(jblas.getColumn(j))
    def row(i: Int): Matrix[T] = matrix(jblas.getRow(i))

    def isEmpty: Boolean = jblas.isEmpty
    def isRowVector: Boolean = jblas.isRowVector
    def isColumnVector: Boolean = jblas.isColumnVector
    def isVector: Boolean = jblas.isVector
    def isSquare: Boolean = jblas.isSquare
    def isScalar: Boolean = jblas.isScalar

    def dup: Matrix[T] = matrix(jblas.dup)
    def negate: Matrix[T] = matrix(jblas.neg)
    def transpose: Matrix[T] = matrix(jblas.transpose)
    def diag: Matrix[T] = matrix(jblas.diag)
    def invert: Matrix[T] = matrix(org.jblas.Solve.solve(jblas, DoubleMatrix.eye(jblas.rows)))
    def ceil: Matrix[Int] = matrix(org.jblas.MatrixFunctions.ceil(jblas))(convertInt)
    def floor: Matrix[Int] = matrix(org.jblas.MatrixFunctions.floor(jblas))(convertInt)
    def log: Matrix[Double] = matrix(org.jblas.MatrixFunctions.log(jblas))(convertDouble)
    def log10: Matrix[Double] = matrix(org.jblas.MatrixFunctions.log10(jblas))(convertDouble)

    def fullSVD: (Matrix[T], Matrix[T], Matrix[T]) = {
      val usv = org.jblas.Singular.fullSVD(jblas).map(matrix(_)(converter))
      (usv(0), usv(1), usv(2))
    }

    def addScalar(x: T): Matrix[T] = matrix(jblas.add(converter.unapply(x)))
    def addAssignment(r: Int, c: Int, v: T): Matrix[T] = {
      val newJblas = jblas.dup()
      newJblas.put(r, c, converter.unapply(v))
      matrix(newJblas)(converter)
    }
    def subtractScalar(x: T): Matrix[T] = matrix(jblas.sub(converter.unapply(x)))
    def multiplyScalar(x: T): Matrix[T] = matrix(jblas.mul(converter.unapply(x)))
    def divideScalar(x: T): Matrix[T] = matrix(jblas.div(converter.unapply(x)))
    def mulRow(i: Int, x: T): Matrix[T] = matrix(jblas.mulRow(i, converter.unapply(x)))
    def mulColumn(i: Int, x: T): Matrix[T] = matrix(jblas.mulColumn(i, converter.unapply(x)))

    def pow(p: Double): Matrix[T] = matrix(org.jblas.MatrixFunctions.pow(jblas, p))

    def addMatrix(other: Matrix[T]): Matrix[T] = matrix(jblas.add(other.jblas))
    def subtractMatrix(other: Matrix[T]): Matrix[T] = matrix(jblas.sub(other.jblas))
    def multiplyMatrix(other: Matrix[T]): Matrix[T] = matrix(jblas.mmul(other.jblas))

    def mulPointwise(other: Matrix[T]): Matrix[T] = matrix(jblas.mul(other.jblas))
    def divPointwise(other: Matrix[T]): Matrix[T] = matrix(jblas.div(other.jblas))

    def concatenateHorizontally(right: Matrix[T]): Matrix[T] = matrix(DoubleMatrix.concatHorizontally(jblas, right.jblas))
    def concatenateVertically(under: Matrix[T]): Matrix[T] = matrix(DoubleMatrix.concatVertically(jblas, under.jblas))
    def solve(B: Matrix[T]): Matrix[T] = matrix(org.jblas.Solve.solve(jblas, B.jblas))

    def addRowVector(row: Matrix[T]): Matrix[T] = matrix(jblas.addRowVector(row.jblas))
    def addColumnVector(column: Matrix[T]): Matrix[T] = matrix(jblas.addColumnVector(column.jblas))
    def subRowVector(row: Matrix[T]): Matrix[T] = matrix(jblas.subRowVector(row.jblas))
    def subColumnVector(column: Matrix[T]): Matrix[T] = matrix(jblas.subColumnVector(column.jblas))
    def mulRowVector(row: Matrix[T]): Matrix[T] = matrix(jblas.mulRowVector(row.jblas))
    def mulColumnVector(column: Matrix[T]): Matrix[T] = matrix(jblas.mulColumnVector(column.jblas))
    def divRowVector(row: Matrix[T]): Matrix[T] = matrix(jblas.divRowVector(row.jblas))
    def divColumnVector(column: Matrix[T]): Matrix[T] = matrix(jblas.divColumnVector(column.jblas))

    def lt(other: Matrix[T]): Matrix[Boolean] = matrix[Boolean](jblas.lt(other.jblas))(convertBoolean)
    def le(other: Matrix[T]): Matrix[Boolean] = matrix(jblas.le(other.jblas))(convertBoolean)
    def gt(other: Matrix[T]): Matrix[Boolean] = matrix(jblas.gt(other.jblas))(convertBoolean)
    def ge(other: Matrix[T]): Matrix[Boolean] = matrix(jblas.ge(other.jblas))(convertBoolean)
    def eq(other: Matrix[T]): Matrix[Boolean] = matrix(jblas.eq(other.jblas))(convertBoolean)
    def ne(other: Matrix[T]): Matrix[Boolean] = matrix(jblas.ne(other.jblas))(convertBoolean)
    def and(other: Matrix[T]): Matrix[Boolean] = matrix(jblas.and(other.jblas))(convertBoolean)
    def or(other: Matrix[T]): Matrix[Boolean] = matrix(jblas.or(other.jblas))(convertBoolean)
    def xor(other: Matrix[T]): Matrix[Boolean] = matrix(jblas.xor(other.jblas))(convertBoolean)

    def not: Matrix[Boolean] = matrix(jblas.not)(convertBoolean)

    def max: T = converter(jblas.max)

    def argmax: (Int, Int) = {
      val i = jblas.argmax
      (i % columns, i / columns)
    }

    def min: T = converter(jblas.min)

    def argmin: (Int, Int) = {
      val i = jblas.argmin
      (i % columns, i / columns)
    }

    def rowSums: Matrix[T] = matrix(jblas.rowSums)
    def columnSums: Matrix[T] = matrix(jblas.columnSums)

    def columnMins: Matrix[T] = matrix(jblas.columnMins)
    def columnMaxs: Matrix[T] = matrix(jblas.columnMaxs)
    def columnMeans: Matrix[T] = matrix(jblas.columnMeans)
    def sortColumns: Matrix[T] = matrix(jblas.sortColumns)

    def rowMins: Matrix[T] = matrix(jblas.rowMins)
    def rowMaxs: Matrix[T] = matrix(jblas.rowMaxs)
    def rowMeans: Matrix[T] = matrix(jblas.rowMeans)
    def sortRows: Matrix[T] = matrix(jblas.sortRows)

    // higher order methods

    def map[B: C](f: T => B): Matrix[B] = {
      val fpB = implicitly[C[B]]
      val jblas = DoubleMatrix.zeros(rows, columns)
      (0 until rows) foreach { r =>
        (0 until columns) foreach { c =>
          jblas.put(r, c, fpB.unapply(f(this(r, c))))
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
          // assumes fc.rows === this.rows
          jblas.put(r, c, fpA.unapply(fc(r, 0)))
        }
      }
      matrix[A](jblas)
    }

  }

  object Matrix {

    import axle.Show

    implicit def showMatrix[T: C: Show]: Show[Matrix[T]] = new Show[Matrix[T]] {

      def text(m: Matrix[T]): String = {
        import m._
        (0 until rows).map(i => (0 until columns).map(j => string(converter(jblas.get(i, j)))).mkString(" ")).mkString("\n")
      }
    }

  }

  // methods for creating matrices

  def matrix[T: C](s: DoubleMatrix): Matrix[T] = new Matrix[T](s)

  def matrix[T: C](r: Int, c: Int, values: Array[T]): Matrix[T] = {
    val fp = implicitly[C[T]]
    val jblas = new org.jblas.DoubleMatrix(values.map(fp.unapply))
    jblas.reshape(r, c)
    matrix(jblas)
  }

  def matrix[T: C](m: Int, n: Int, topleft: => T, left: Int => T, top: Int => T, fill: (Int, Int, T, T, T) => T): Matrix[T] = {
    val fp = implicitly[C[T]]
    val jblas = DoubleMatrix.zeros(m, n)
    jblas.put(0, 0, fp.unapply(topleft))
    (0 until m) foreach { r => jblas.put(r, 0, fp.unapply(left(r))) }
    (0 until n) foreach { c => jblas.put(0, c, fp.unapply(top(c))) }
    (1 until m) foreach { r =>
      (1 until n) foreach { c =>
        val diag = fp(jblas.get(r - 1, c - 1))
        val left = fp(jblas.get(r, c - 1))
        val right = fp(jblas.get(r - 1, c))
        jblas.put(r, c, fp.unapply(fill(r, c, diag, left, right)))
      }
    }
    matrix(jblas)
  }

  def matrix[T](m: Int, n: Int, f: (Int, Int) => T)(implicit fp: FunctionPair[Double, T]): Matrix[T] = {
    val jblas = DoubleMatrix.zeros(m, n)
    (0 until m) foreach { r =>
      (0 until n) foreach { c =>
        jblas.put(r, c, fp.unapply(f(r, c)))
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
    if (m.rows % 2 === 0) {
      (sorted.row(m.rows / 2 - 1).addMatrix(sorted.row(m.rows / 2))) / 2d
    } else {
      sorted.row(m.rows / 2)
    }
    matrix(sorted.underlying)
  }

  def centerRows(m: Matrix[Double]): Matrix[Double] = m.subColumnVector(m.rowMeans)
  def centerColumns(m: Matrix[Double]): Matrix[Double] = m.subRowVector(m.columnMeans)

  def rowRange(m: Matrix[Double]): Matrix[Double] = m.rowMaxs.subtractMatrix(m.rowMins)
  def columnRange(m: Matrix[Double]): Matrix[Double] = m.columnMaxs.subtractMatrix(m.columnMins)

  def sumsq(m: Matrix[Double]): Matrix[Double] = m.mulPointwise(m).columnSums

  def cov(m: Matrix[Double]): Matrix[Double] = (centerColumns(m).t ⨯ centerColumns(m)) / m.columns

  def std(m: Matrix[Double]): Matrix[Double] = (sumsq(centerColumns(m)) / m.columns).map(sqrt _)

  def zscore(m: Matrix[Double]): Matrix[Double] = centerColumns(m).divRowVector(std(m))

  /**
   * Principal Component Analysis (PCA)
   *
   * assumes that the input matrix, Xnorm, has been normalized, in other words:
   *   mean of each column === 0.0
   *   stddev of each column === 1.0 (I'm not clear if this is a strict requirement)
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
    val numComponents = eigenValuesSquared.map(_ / eigenTotal).scan(0d)(_ + _).indexWhere(cutoff<)
    numComponents
    // matrix(s.rows, 1, (0 until s.rows).map(r => if (r < numComponents) { s(r, 0) } else { 0.0 }).toArray)
  }

}

