package axle.matrix

import scala.annotation.elidable
import scala.annotation.elidable.ASSERTION
import scala.math.sqrt
import scala.util.Random.nextDouble
import scala.util.Random.nextGaussian

import axle.algebra.FunctionPair
import no.uib.cipr.matrix.DenseMatrix
import no.uib.cipr.matrix.{Matrix => MtjMatrix}
import spire.implicits.IntAlgebra
import spire.implicits.eqOps

trait MtjMatrixModule extends MatrixModule {
  
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

  class Matrix[T: C](_mtj: MtjMatrix) extends MatrixLike[T] {

    val converter = implicitly[C[T]]

    type S = MtjMatrix

    def underlying: MtjMatrix = _mtj

    def mtj: MtjMatrix = _mtj

    implicit val format = (t: T) => t match {
      case d: Double => """%.6f""".format(d)
      case _ => t.toString
    }

    def rows: Int = mtj.numRows
    def columns: Int = mtj.numColumns
    def length: Int = rows * columns

    def apply(i: Int, j: Int): T = converter(mtj.get(i, j))

    def apply(rs: Seq[Int], cs: Seq[Int]): Matrix[T] = {
      val mtj = new DenseMatrix(rs.length, cs.length)
      rs.zipWithIndex foreach {
        case (fromRow, toRow) =>
          cs.zipWithIndex foreach {
            case (fromCol, toCol) =>
              mtj.set(toRow, toCol, converter.unapply(this(fromRow, fromCol)))
          }
      }
      matrix[T](mtj)
    }
    // def update(i: Int, j: Int, v: T) = mtj.put(i, j, elementAdapter.converter.unapply(v))

    def toList: List[T] = mtj match {
      case dm: DenseMatrix => dm.getData.toList.map(converter.apply _)
      case _ => ???
    }

    def column(j: Int): Matrix[T] = ??? //matrix(mtj.getColumn(j))
    def row(i: Int): Matrix[T] = ??? //matrix(mtj.getRow(i))

    def isEmpty: Boolean = mtj.numRows == 0 && mtj.numColumns == 0
    def isRowVector: Boolean = mtj.numRows == 1
    def isColumnVector: Boolean = mtj.numColumns == 1
    def isVector: Boolean = isRowVector || isColumnVector
    def isSquare: Boolean = mtj.isSquare
    def isScalar: Boolean = mtj.numRows == 1 && mtj.numColumns == 1

    def dup: Matrix[T] = matrix(mtj.copy)

    def negate: Matrix[T] = matrix(mtj.copy.scale(-1d))

    def transpose: Matrix[T] = matrix(mtj.copy.transpose)

    def diag: Matrix[T] = ???
    
    def invert: Matrix[T] = ??? //matrix(org.mtj.Solve.solve(mtj, DenseMatrix.eye(mtj.rows)))

    def ceil: Matrix[Int] = ??? //matrix(org.mtj.MatrixFunctions.ceil(mtj))(convertInt)
    def floor: Matrix[Int] = ??? //matrix(org.mtj.MatrixFunctions.floor(mtj))(convertInt)
    def log: Matrix[Double] = ??? //matrix(org.mtj.MatrixFunctions.log(mtj))(convertDouble)
    def log10: Matrix[Double] = ??? //matrix(org.mtj.MatrixFunctions.log10(mtj))(convertDouble)

    def fullSVD: (Matrix[T], Matrix[T], Matrix[T]) = {
      //      val usv = org.mtj.Singular.fullSVD(mtj).map(matrix(_)(converter))
      //      (usv(0), usv(1), usv(2))
      ???
    }

    def addScalar(x: T): Matrix[T] = ??? //matrix(mtj.add(converter.unapply(x)))

    def addAssignment(r: Int, c: Int, v: T): Matrix[T] = {
      val result = mtj.copy
      result.set(r, c, converter.unapply(v))
      matrix(result)(converter)
    }

    def subtractScalar(x: T): Matrix[T] = ??? //matrix(mtj.sub(converter.unapply(x)))
    def multiplyScalar(x: T): Matrix[T] = ??? //matrix(mtj.mul(converter.unapply(x)))
    def divideScalar(x: T): Matrix[T] = ??? //matrix(mtj.div(converter.unapply(x)))
    def mulRow(i: Int, x: T): Matrix[T] = ??? //matrix(mtj.mulRow(i, converter.unapply(x)))
    def mulColumn(i: Int, x: T): Matrix[T] = ??? //matrix(mtj.mulColumn(i, converter.unapply(x)))

    def pow(p: Double): Matrix[T] = ??? //matrix(org.mtj.MatrixFunctions.pow(mtj, p))

    def addMatrix(other: Matrix[T]): Matrix[T] = {
      val result = mtj.copy
      result.add(other.mtj)
      matrix(result)(converter)
    }

    def subtractMatrix(other: Matrix[T]): Matrix[T] = {
      val result = mtj.copy
      val otherCopy = other.mtj.copy
      otherCopy.scale(-1d)
      result.add(otherCopy)
      matrix(result)(converter)
    }

    def multiplyMatrix(other: Matrix[T]): Matrix[T] = ??? //matrix(mtj.mmul(other.mtj))

    def mulPointwise(other: Matrix[T]): Matrix[T] = ??? //matrix(mtj.mul(other.mtj))
    def divPointwise(other: Matrix[T]): Matrix[T] = ??? //matrix(mtj.div(other.mtj))

    def concatenateHorizontally(right: Matrix[T]): Matrix[T] = ??? //matrix(DenseMatrix.concatHorizontally(mtj, right.mtj))
    def concatenateVertically(under: Matrix[T]): Matrix[T] = ??? //matrix(DenseMatrix.concatVertically(mtj, under.mtj))
    def solve(B: Matrix[T]): Matrix[T] = ??? //matrix(org.mtj.Solve.solve(mtj, B.mtj))

    def addRowVector(row: Matrix[T]): Matrix[T] = ??? //matrix(mtj.addRowVector(row.mtj))
    def addColumnVector(column: Matrix[T]): Matrix[T] = ??? //matrix(mtj.addColumnVector(column.mtj))
    def subRowVector(row: Matrix[T]): Matrix[T] = ??? //matrix(mtj.subRowVector(row.mtj))
    def subColumnVector(column: Matrix[T]): Matrix[T] = ??? //matrix(mtj.subColumnVector(column.mtj))
    def mulRowVector(row: Matrix[T]): Matrix[T] = ??? //matrix(mtj.mulRowVector(row.mtj))
    def mulColumnVector(column: Matrix[T]): Matrix[T] = ??? //matrix(mtj.mulColumnVector(column.mtj))
    def divRowVector(row: Matrix[T]): Matrix[T] = ??? //matrix(mtj.divRowVector(row.mtj))
    def divColumnVector(column: Matrix[T]): Matrix[T] = ??? //matrix(mtj.divColumnVector(column.mtj))

    def lt(other: Matrix[T]): Matrix[Boolean] = ??? //matrix[Boolean](mtj.lt(other.mtj))(convertBoolean)
    def le(other: Matrix[T]): Matrix[Boolean] = ??? //matrix(mtj.le(other.mtj))(convertBoolean)
    def gt(other: Matrix[T]): Matrix[Boolean] = ??? //matrix(mtj.gt(other.mtj))(convertBoolean)
    def ge(other: Matrix[T]): Matrix[Boolean] = ??? //matrix(mtj.ge(other.mtj))(convertBoolean)
    def eq(other: Matrix[T]): Matrix[Boolean] = ??? //matrix(mtj.eq(other.mtj))(convertBoolean)
    def ne(other: Matrix[T]): Matrix[Boolean] = ??? //matrix(mtj.ne(other.mtj))(convertBoolean)
    def and(other: Matrix[T]): Matrix[Boolean] = ??? //matrix(mtj.and(other.mtj))(convertBoolean)
    def or(other: Matrix[T]): Matrix[Boolean] = ??? //matrix(mtj.or(other.mtj))(convertBoolean)
    def xor(other: Matrix[T]): Matrix[Boolean] = ??? //matrix(mtj.xor(other.mtj))(convertBoolean)

    def not: Matrix[Boolean] = ??? //matrix(mtj.not)(convertBoolean)

    def max: T = {
      var result = Double.MinValue
      (0 to rows) foreach { r =>
        (0 to columns) foreach { c =>
          val v = mtj.get(r, c)
          if (v > result) {
            result = v
          }
        }
      }
      converter(result)
    }

    /**
     * argmin: location (r, c) of the lowest value
     *
     */
    def argmax: (Int, Int) = {
      var (result, resultR, resultC) = (Double.MinValue, -1, -1)
      (0 to rows) foreach { r =>
        (0 to columns) foreach { c =>
          val v = mtj.get(r, c)
          if (v > result) {
            result = v
            resultR = r
            resultC = c
          }
        }
      }
      (resultR, resultC)
    }

    def min: T = {
      var result = Double.MaxValue
      (0 to rows) foreach { r =>
        (0 to columns) foreach { c =>
          val v = mtj.get(r, c)
          if (v < result) {
            result = v
          }
        }
      }
      converter(result)
    }

    def argmin: (Int, Int) = {
      var (result, resultR, resultC) = (Double.MaxValue, -1, -1)
      (0 to rows) foreach { r =>
        (0 to columns) foreach { c =>
          val v = mtj.get(r, c)
          if (v < result) {
            result = v
            resultR = r
            resultC = c
          }
        }
      }
      (resultR, resultC)
    }

    def rowSums: Matrix[T] = ??? //matrix(mtj.rowSums)
    def columnSums: Matrix[T] = ??? //matrix(mtj.columnSums)

    def columnMins: Matrix[T] = ??? //matrix(mtj.columnMins)
    def columnMaxs: Matrix[T] = ??? //matrix(mtj.columnMaxs)
    def columnMeans: Matrix[T] = ??? //matrix(mtj.columnMeans)
    def sortColumns: Matrix[T] = ??? //matrix(mtj.sortColumns)

    def rowMins: Matrix[T] = ??? //matrix(mtj.rowMins)
    def rowMaxs: Matrix[T] = ??? //matrix(mtj.rowMaxs)
    def rowMeans: Matrix[T] = ??? //matrix(mtj.rowMeans)
    def sortRows: Matrix[T] = ??? //matrix(mtj.sortRows)

    // higher order methods

    def map[B: C](f: T => B): Matrix[B] = {
      val fpB = implicitly[C[B]]
      val mtj = new DenseMatrix(rows, columns)
      (0 until rows) foreach { r =>
        (0 until columns) foreach { c =>
          mtj.set(r, c, fpB.unapply(f(this(r, c))))
        }
      }
      matrix[B](mtj)
    }

    def flatMapColumns[A: C](f: Matrix[T] => Matrix[A]): Matrix[A] = {
      val fpA = implicitly[C[A]]
      val mtj = new DenseMatrix(rows, columns)
      (0 until columns) foreach { c =>
        val fc = f(column(c))
        (0 until rows) foreach { r =>
          // assumes fc.rows === this.rows
          mtj.set(r, c, fpA.unapply(fc(r, 0)))
        }
      }
      matrix[A](mtj)
    }

    override def toString: String =
      (0 until rows).map(i => (0 until columns).map(j => format(converter(mtj.get(i, j)))).mkString(" ")).mkString("\n")

  }

  // methods for creating matrices

  def matrix[T: C](s: MtjMatrix): Matrix[T] = new Matrix[T](s)

  def matrix[T: C](r: Int, c: Int, values: Array[T]): Matrix[T] = {
    //    val fp = implicitly[C[T]]
    //    val mtj = new org.mtj.DenseMatrix(values.map(fp.unapply))
    //    mtj.reshape(r, c)
    //    matrix(mtj)
    ???
  }

  def matrix[T: C](m: Int, n: Int, topleft: => T, left: Int => T, top: Int => T, fill: (Int, Int, T, T, T) => T): Matrix[T] = {
    val fp = implicitly[C[T]]
    val mtj = new DenseMatrix(m, n)
    mtj.set(0, 0, fp.unapply(topleft))
    (0 until m) foreach { r => mtj.set(r, 0, fp.unapply(left(r))) }
    (0 until n) foreach { c => mtj.set(0, c, fp.unapply(top(c))) }
    (1 until m) foreach { r =>
      (1 until n) foreach { c =>
        val diag = fp(mtj.get(r - 1, c - 1))
        val left = fp(mtj.get(r, c - 1))
        val right = fp(mtj.get(r - 1, c))
        mtj.set(r, c, fp.unapply(fill(r, c, diag, left, right)))
      }
    }
    matrix(mtj)
  }

  def matrix[T](m: Int, n: Int, f: (Int, Int) => T)(implicit fp: FunctionPair[Double, T]): Matrix[T] = {
    val mtj = new DenseMatrix(m, n)
    (0 until m) foreach { r =>
      (0 until n) foreach { c =>
        mtj.set(r, c, fp.unapply(f(r, c)))
      }
    }
    matrix(mtj)
  }

  def diag[T: C](row: Matrix[T]): Matrix[T] = {
    assert(row.isRowVector)
    //    matrix(DenseMatrix.diag(row.mtj))
    ???
  }

  def zeros[T: C](m: Int, n: Int): Matrix[T] = {
    val mtj = new DenseMatrix(m, n)
    mtj.zero
    matrix(mtj)
  }

  def ones[T: C](m: Int, n: Int): Matrix[T] = {
    val convert = implicitly[C[T]]
    matrix(m, n, (r, c) => convert(1d))
  }

  def eye[T: C](n: Int): Matrix[T] = {
    val convert = implicitly[C[T]]
    val one = convert(1d)
    val zero = convert(0d)
    matrix(n, n, (r, c) => if (r == c) one else zero)
  }

  def I[T: C](n: Int): Matrix[T] = eye(n)

  // evenly distributed from 0.0 to 1.0
  def rand[T: C](m: Int, n: Int): Matrix[T] = {
    import scala.util.Random.nextDouble
    val convert = implicitly[C[T]]
    matrix(m, n, (r, c) => convert(nextDouble))
  }

  // normal distribution
  def randn[T: C](m: Int, n: Int): Matrix[T] = {
    import scala.util.Random.nextGaussian
    val convert = implicitly[C[T]]
    matrix(m, n, (r, c) => convert(nextGaussian))
  }

  def falses(m: Int, n: Int): Matrix[Boolean] = matrix(m, n, (r, c) => false)

  def trues(m: Int, n: Int): Matrix[Boolean] = matrix(m, n, (r, c) => true)

  // TODO: Int mtj' rand and randn should probably floor the result

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
