package axle.jblas

import org.jblas.DoubleMatrix
import axle.algebra.FunctionPair
import axle.algebra.Matrix
import spire.implicits.IntAlgebra
import spire.implicits.eqOps

case class ConvertedJblasDoubleMatrix[T](jdm: DoubleMatrix)(implicit val fp: FunctionPair[Double, T])

object ConvertedJblasDoubleMatrix {

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

  implicit val jblasConvertedMatrix: Matrix[ConvertedJblasDoubleMatrix] =
    new Matrix[ConvertedJblasDoubleMatrix] {

      def rows[T](m: ConvertedJblasDoubleMatrix[T]): Int = m.jdm.getRows

      def columns[T](m: ConvertedJblasDoubleMatrix[T]): Int = m.jdm.getColumns

      def length[T](m: ConvertedJblasDoubleMatrix[T]): Int = m.jdm.getLength

      def get[T](m: ConvertedJblasDoubleMatrix[T])(i: Int, j: Int): T = m.fp(m.jdm.get(i, j))

      def slice[T](m: ConvertedJblasDoubleMatrix[T])(rs: Seq[Int], cs: Seq[Int]): ConvertedJblasDoubleMatrix[T] = {
        import m.fp
        val jblas = DoubleMatrix.zeros(rs.length, cs.length)
        rs.zipWithIndex foreach {
          case (fromRow, toRow) =>
            cs.zipWithIndex foreach {
              case (fromCol, toCol) =>
                jblas.put(toRow, toCol, m.fp.unapply(this.get(m)(fromRow, fromCol)))
            }
        }
        this.matrix[T](jblas)
      }

      def column[T](m: ConvertedJblasDoubleMatrix[T])(j: Int): ConvertedJblasDoubleMatrix[T] =
        matrix(m.jdm.getColumn(j))(m.fp)
        
      def row[T](m: ConvertedJblasDoubleMatrix[T])(i: Int): ConvertedJblasDoubleMatrix[T] =
        matrix(m.jdm.getRow(i))(m.fp)

      def isEmpty[T](m: ConvertedJblasDoubleMatrix[T]): Boolean = m.jdm.isEmpty
      def isRowVector[T](m: ConvertedJblasDoubleMatrix[T]): Boolean = m.jdm.isRowVector
      def isColumnVector[T](m: ConvertedJblasDoubleMatrix[T]): Boolean = m.jdm.isColumnVector
      def isVector[T](m: ConvertedJblasDoubleMatrix[T]): Boolean = m.jdm.isVector
      def isSquare[T](m: ConvertedJblasDoubleMatrix[T]): Boolean = m.jdm.isSquare
      def isScalar[T](m: ConvertedJblasDoubleMatrix[T]): Boolean = m.jdm.isScalar

      def dup[T](m: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = matrix(m.jdm.dup)(m.fp)
      def negate[T](m: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = matrix(m.jdm.neg)(m.fp)
      def transpose[T](m: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = matrix(m.jdm.transpose)(m.fp)
      def diag[T](m: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = matrix(m.jdm.diag)(m.fp)
      def invert[T](m: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[Double] = matrix(org.jblas.Solve.solve(m.jdm, DoubleMatrix.eye(m.jdm.rows)))
      def ceil[T](m: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[Int] = matrix(org.jblas.MatrixFunctions.ceil(m.jdm))
      def floor[T](m: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[Int] = matrix(org.jblas.MatrixFunctions.floor(m.jdm))
      def log[T](m: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[Double] = matrix(org.jblas.MatrixFunctions.log(m.jdm))
      def log10[T](m: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[Double] = matrix(org.jblas.MatrixFunctions.log10(m.jdm))

      def fullSVD[T](m: ConvertedJblasDoubleMatrix[T]): (ConvertedJblasDoubleMatrix[T], ConvertedJblasDoubleMatrix[T], ConvertedJblasDoubleMatrix[T]) =
        ??? // (U, S, V) such that A = U * diag(S) * V' // TODO: all Matrix[Double] ?

      def pow[T](m: ConvertedJblasDoubleMatrix[T])(p: Double): ConvertedJblasDoubleMatrix[Double] =
        matrix(org.jblas.MatrixFunctions.pow(m.jdm, p))

      def addScalar[T](m: ConvertedJblasDoubleMatrix[T])(x: T): ConvertedJblasDoubleMatrix[T] = matrix(m.jdm.add(m.fp.unapply(x)))(m.fp)
      def subtractScalar[T](m: ConvertedJblasDoubleMatrix[T])(x: T): ConvertedJblasDoubleMatrix[T] = matrix(m.jdm.sub(m.fp.unapply(x)))(m.fp)
      def multiplyScalar[T](m: ConvertedJblasDoubleMatrix[T])(x: T): ConvertedJblasDoubleMatrix[T] = matrix(m.jdm.mul(m.fp.unapply(x)))(m.fp)
      def divideScalar[T](m: ConvertedJblasDoubleMatrix[T])(x: T): ConvertedJblasDoubleMatrix[T] = matrix(m.jdm.div(m.fp.unapply(x)))(m.fp)
      def mulRow[T](m: ConvertedJblasDoubleMatrix[T])(i: Int, x: T): ConvertedJblasDoubleMatrix[T] = matrix(m.jdm.mulRow(i, m.fp.unapply(x)))(m.fp)
      def mulColumn[T](m: ConvertedJblasDoubleMatrix[T])(i: Int, x: T): ConvertedJblasDoubleMatrix[T] = matrix(m.jdm.mulColumn(i, m.fp.unapply(x)))(m.fp)

      // Operations on pairs of matrices
      // TODO: add and subtract don't make sense for T = Boolean

      def addMatrix[T](m: ConvertedJblasDoubleMatrix[T])(other: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = matrix(m.jdm.add(other.jdm))(m.fp)
      def subtractMatrix[T](m: ConvertedJblasDoubleMatrix[T])(other: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = matrix(m.jdm.sub(other.jdm))(m.fp)
      def multiplyMatrix[T](m: ConvertedJblasDoubleMatrix[T])(other: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = matrix(m.jdm.mmul(other.jdm))(m.fp)
      def mulPointwise[T](m: ConvertedJblasDoubleMatrix[T])(other: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = matrix(m.jdm.mul(other.jdm))(m.fp)
      def divPointwise[T](m: ConvertedJblasDoubleMatrix[T])(other: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = matrix(m.jdm.div(other.jdm))(m.fp)
      def concatenateHorizontally[T](m: ConvertedJblasDoubleMatrix[T])(right: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = matrix(DoubleMatrix.concatHorizontally(m.jdm, right.jdm))(m.fp)
      def concatenateVertically[T](m: ConvertedJblasDoubleMatrix[T])(under: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = matrix(DoubleMatrix.concatVertically(m.jdm, under.jdm))(m.fp)
      def solve[T](m: ConvertedJblasDoubleMatrix[T])(B: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = ??? // returns X, where this === A and A x X = B

      // Operations on a matrix and a column/row vector

      def addRowVector[T](m: ConvertedJblasDoubleMatrix[T])(row: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = ???
      def addColumnVector[T](m: ConvertedJblasDoubleMatrix[T])(column: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = ???
      def subRowVector[T](m: ConvertedJblasDoubleMatrix[T])(row: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = ???
      def subColumnVector[T](m: ConvertedJblasDoubleMatrix[T])(column: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = ???
      def mulRowVector[T](m: ConvertedJblasDoubleMatrix[T])(row: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = ???
      def mulColumnVector[T](m: ConvertedJblasDoubleMatrix[T])(column: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = ???
      def divRowVector[T](m: ConvertedJblasDoubleMatrix[T])(row: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = ???
      def divColumnVector[T](m: ConvertedJblasDoubleMatrix[T])(column: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = ???

      // Operations on pair of matrices that return M[Boolean]

      def lt[T](m: ConvertedJblasDoubleMatrix[T])(other: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[Boolean] = ???
      def le[T](m: ConvertedJblasDoubleMatrix[T])(other: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[Boolean] = ???
      def gt[T](m: ConvertedJblasDoubleMatrix[T])(other: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[Boolean] = ???
      def ge[T](m: ConvertedJblasDoubleMatrix[T])(other: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[Boolean] = ???
      def eq[T](m: ConvertedJblasDoubleMatrix[T])(other: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[Boolean] = ???
      def ne[T](m: ConvertedJblasDoubleMatrix[T])(other: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[Boolean] = ???

      def and[T](m: ConvertedJblasDoubleMatrix[T])(other: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[Boolean] = ???
      def or[T](m: ConvertedJblasDoubleMatrix[T])(other: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[Boolean] = ???
      def xor[T](m: ConvertedJblasDoubleMatrix[T])(other: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[Boolean] = ???
      def not[T](m: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[Boolean] = ???

      // various mins and maxs

      def max[T](m: ConvertedJblasDoubleMatrix[T]): T = ???
      def argmax[T](m: ConvertedJblasDoubleMatrix[T]): (Int, Int) = ???
      def min[T](m: ConvertedJblasDoubleMatrix[T]): T = ???
      def argmin[T](m: ConvertedJblasDoubleMatrix[T]): (Int, Int) = ???

      def rowSums[T](m: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = ???
      def columnSums[T](m: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = ???
      def columnMins[T](m: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = ???
      def columnMaxs[T](m: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = ???
      // def columnArgmins
      // def columnArgmaxs

      def columnMeans[T](m: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = ???
      def sortColumns[T](m: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = ???

      def rowMins[T](m: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = ???
      def rowMaxs[T](m: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = ???
      def rowMeans[T](m: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = ???
      def sortRows[T](m: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] = ???

      def plus[T](x: ConvertedJblasDoubleMatrix[T])(y: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] =
        ConvertedJblasDoubleMatrix[T](x.jdm.add(y.jdm))(x.fp)

      def matrix[T](jblas: DoubleMatrix)(implicit fp: FunctionPair[Double, T]): ConvertedJblasDoubleMatrix[T] =
        ConvertedJblasDoubleMatrix[T](jblas)

      def matrix[T](
        m: Int,
        n: Int,
        topleft: => T,
        left: Int => T,
        top: Int => T,
        fill: (Int, Int, T, T, T) => T)(implicit fp: FunctionPair[Double, T]): ConvertedJblasDoubleMatrix[T] = {

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

      def matrix[T](m: Int, n: Int, f: (Int, Int) => T)(implicit fp: FunctionPair[Double, T]): ConvertedJblasDoubleMatrix[T] = {
        val jblas = DoubleMatrix.zeros(m, n)
        (0 until m) foreach { r =>
          (0 until n) foreach { c =>
            jblas.put(r, c, fp.unapply(f(r, c)))
          }
        }
        matrix(jblas)
      }

      // Higher-order methods

      def map[B, T](m: ConvertedJblasDoubleMatrix[T])(f: T => B)(implicit fpB: FunctionPair[Double, B]): ConvertedJblasDoubleMatrix[B] = {
        val jblas = DoubleMatrix.zeros(rows(m), columns(m))
        val asdf = m.fp
        (0 until rows(m)) foreach { r =>
          (0 until columns(m)) foreach { c =>
            val t = this.get(m)(r, c)
            jblas.put(r, c, fpB.unapply(f(t)))
          }
        }
        matrix[B](jblas)
      }

      def flatMapColumns[A, T](m: ConvertedJblasDoubleMatrix[T])(f: ConvertedJblasDoubleMatrix[T] => ConvertedJblasDoubleMatrix[A])(implicit fpA: FunctionPair[A, Double]): ConvertedJblasDoubleMatrix[A] =
        ???

      def centerRows(m: ConvertedJblasDoubleMatrix[Double]): ConvertedJblasDoubleMatrix[Double] =
        ???

      def centerColumns(m: ConvertedJblasDoubleMatrix[Double]): ConvertedJblasDoubleMatrix[Double] =
        ???

      def rowRange(m: ConvertedJblasDoubleMatrix[Double]): ConvertedJblasDoubleMatrix[Double] =
        ???

      def columnRange(m: ConvertedJblasDoubleMatrix[Double]): ConvertedJblasDoubleMatrix[Double] =
        ???

      def sumsq(m: ConvertedJblasDoubleMatrix[Double]): ConvertedJblasDoubleMatrix[Double] =
        ???

      def cov(m: ConvertedJblasDoubleMatrix[Double]): ConvertedJblasDoubleMatrix[Double] =
        ???

      def std(m: ConvertedJblasDoubleMatrix[Double]): ConvertedJblasDoubleMatrix[Double] =
        ???

      def zscore(m: ConvertedJblasDoubleMatrix[Double]): ConvertedJblasDoubleMatrix[Double] =
        ???

      def pca(Xnorm: ConvertedJblasDoubleMatrix[Double], cutoff: Double = 0.95): (ConvertedJblasDoubleMatrix[Double], ConvertedJblasDoubleMatrix[Double]) =
        ???

      def numComponentsForCutoff(s: ConvertedJblasDoubleMatrix[Double], cutoff: Double): Int =
        ???

    }

}