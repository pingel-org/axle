package axle.jblas

import org.jblas.DoubleMatrix
import axle.algebra.FunctionPair
import axle.algebra.Functor
import axle.algebra.LinearAlgebra
import spire.algebra.AdditiveMonoid
import spire.algebra.AdditiveCMonoid
import spire.algebra.AdditiveSemigroup
import spire.algebra.AdditiveCSemigroup
import spire.algebra.AdditiveGroup
import spire.algebra.AdditiveAbGroup
import spire.algebra.Field
import spire.algebra.MultiplicativeMonoid
import spire.algebra.MultiplicativeSemigroup
import spire.algebra.Ring
import spire.implicits.IntAlgebra
import spire.implicits.eqOps
import scala.reflect.ClassTag

object DoubleMatrixWitnesses {

  implicit val additiveCSemigroupDoubleMatrix: AdditiveCSemigroup[DoubleMatrix] =
    new AdditiveCSemigroup[DoubleMatrix] {

      def plus(x: DoubleMatrix, y: DoubleMatrix): DoubleMatrix =
        x.add(y)
    }

  // TODO dimension (m: Int, n: Int)
  implicit val additiveCMonoidDoubleMatrix: AdditiveMonoid[DoubleMatrix] =
    new AdditiveMonoid[DoubleMatrix] {

      lazy val semigroup = additiveCSemigroupDoubleMatrix

      def plus(x: DoubleMatrix, y: DoubleMatrix) =
        semigroup.plus(x, y)

      def zero: DoubleMatrix =
        DoubleMatrix.zeros(???, ???)
    }

  implicit val multiplicativeSemigroupDoubleMatrix: MultiplicativeSemigroup[DoubleMatrix] =
    new MultiplicativeSemigroup[DoubleMatrix] {

      def times(x: DoubleMatrix, y: DoubleMatrix): DoubleMatrix =
        x.mmul(y)
    }

  implicit val multiplicativeMonoidDoubleMatrix: MultiplicativeMonoid[DoubleMatrix] =
    new MultiplicativeMonoid[DoubleMatrix] {

      lazy val semigroup = multiplicativeSemigroupDoubleMatrix

      def times(x: DoubleMatrix, y: DoubleMatrix) =
        semigroup.times(x, y)

      def one: DoubleMatrix =
        DoubleMatrix.eye(???) // TODO: dimension m
    }

  implicit val additiveAbGroupDoubleMatrix: AdditiveAbGroup[DoubleMatrix] =
    new AdditiveAbGroup[DoubleMatrix] {

      lazy val additiveCMonoid = additiveCMonoidDoubleMatrix

      def zero: DoubleMatrix =
        additiveCMonoid.zero

      def plus(x: DoubleMatrix, y: DoubleMatrix): DoubleMatrix =
        additiveCMonoid.plus(x, y)

      def negate(x: DoubleMatrix): DoubleMatrix =
        x.neg
    }

  implicit val ringDoubleMatrix: Ring[DoubleMatrix] =
    new Ring[DoubleMatrix] {

      lazy val additiveAbGroup = additiveAbGroupDoubleMatrix

      def negate(x: DoubleMatrix): DoubleMatrix =
        additiveAbGroup.negate(x)

      def zero: DoubleMatrix =
        additiveAbGroup.zero

      def plus(x: DoubleMatrix, y: DoubleMatrix): DoubleMatrix =
        additiveAbGroup.plus(x, y)

      lazy val multiplicativeMonoid = multiplicativeMonoidDoubleMatrix

      def one: DoubleMatrix =
        multiplicativeMonoid.one

      def times(x: DoubleMatrix, y: DoubleMatrix): DoubleMatrix =
        multiplicativeMonoid.times(x, y)
    }

  //  implicit def endoFunctorDoubleMatrix: EndoFunctor[DoubleMatrix, Double] =
  //    new EndoFunctor[DoubleMatrix, Double] {
  //
  //      def map(m: DoubleMatrix)(f: Double => Double): DoubleMatrix = {
  //        val jdm = DoubleMatrix.zeros(ev.rows(m), ev.columns(m))
  //        (0 until ev.rows(m)) foreach { r =>
  //          (0 until ev.columns(m)) foreach { c =>
  //            val t = ev.get(m)(r, c)
  //            jdm.put(r, c, f(t))
  //          }
  //        }
  //        jdm
  //      }
  //
  //    }

  import axle.Show
  import axle.string

  implicit val showDoubleMatrix: Show[DoubleMatrix] =
    new Show[DoubleMatrix] {

      def text(m: DoubleMatrix): String =
        ((0 until m.getRows) map { i =>
          ((0 until m.getColumns) map { j =>
            string(m.get(i, j))
          }).mkString(" ")
        }).mkString("\n")
    }

  // TODO dimension (laRows: Int, laColumns: Int)
  implicit def linearAlrebraDoubleMatrix: LinearAlgebra[DoubleMatrix, Double] =
    new LinearAlgebra[DoubleMatrix, Double] {

      lazy val elementField = spire.implicits.DoubleAlgebra

      lazy val ring = ringDoubleMatrix

      def rows(m: DoubleMatrix): Int = m.getRows

      def columns(m: DoubleMatrix): Int = m.getColumns

      def length(m: DoubleMatrix): Int = m.getLength

      def get(m: DoubleMatrix)(i: Int, j: Int): Double = m.get(i, j)

      def slice(m: DoubleMatrix)(rs: Seq[Int], cs: Seq[Int]): DoubleMatrix = {
        val jblas = DoubleMatrix.zeros(rs.length, cs.length)
        rs.zipWithIndex foreach {
          case (fromRow, toRow) =>
            cs.zipWithIndex foreach {
              case (fromCol, toCol) =>
                jblas.put(toRow, toCol, this.get(m)(fromRow, fromCol))
            }
        }
        jblas
      }

      def toList(m: DoubleMatrix): List[Double] =
        m.toArray.toList

      def column(m: DoubleMatrix)(j: Int): DoubleMatrix =
        m.getColumn(j)

      def row(m: DoubleMatrix)(i: Int): DoubleMatrix =
        m.getRow(i)

      def isEmpty(m: DoubleMatrix): Boolean = m.isEmpty
      def isRowVector(m: DoubleMatrix): Boolean = m.isRowVector
      def isColumnVector(m: DoubleMatrix): Boolean = m.isColumnVector
      def isVector(m: DoubleMatrix): Boolean = m.isVector
      def isSquare(m: DoubleMatrix): Boolean = m.isSquare
      def isScalar(m: DoubleMatrix): Boolean = m.isScalar

      def dup(m: DoubleMatrix): DoubleMatrix = m.dup

      def negate(m: DoubleMatrix): DoubleMatrix = ring.negate(m)

      def transpose(m: DoubleMatrix): DoubleMatrix = m.transpose
      def diag(m: DoubleMatrix): DoubleMatrix = m.diag
      def invert(m: DoubleMatrix): DoubleMatrix = org.jblas.Solve.solve(m, DoubleMatrix.eye(m.rows))
      def ceil(m: DoubleMatrix): DoubleMatrix = org.jblas.MatrixFunctions.ceil(m)
      def floor(m: DoubleMatrix): DoubleMatrix = org.jblas.MatrixFunctions.floor(m)
      def log(m: DoubleMatrix): DoubleMatrix = org.jblas.MatrixFunctions.log(m)
      def log10(m: DoubleMatrix): DoubleMatrix = org.jblas.MatrixFunctions.log10(m)

      /**
       *  (U, S, V) such that A = U * diag(S) * V' // TODO: all Matrix[Double] ?
       */
      def fullSVD(m: DoubleMatrix): (DoubleMatrix, DoubleMatrix, DoubleMatrix) = {
        val usv = org.jblas.Singular.fullSVD(m)
        (usv(0), usv(1), usv(2))
      }

      def pow(m: DoubleMatrix)(p: Double): DoubleMatrix =
        org.jblas.MatrixFunctions.pow(m, p)

      def addAssignment(m: DoubleMatrix)(r: Int, c: Int, v: Double): DoubleMatrix = {
        val newJblas = m.dup
        newJblas.put(r, c, v)
        newJblas
      }

      // TODO: from Module
      def addScalar(m: DoubleMatrix)(x: Double): DoubleMatrix = m.add(x)
      def subtractScalar(m: DoubleMatrix)(x: Double): DoubleMatrix = m.sub(x)
      def multiplyScalar(m: DoubleMatrix)(x: Double): DoubleMatrix = m.mul(x)
      def divideScalar(m: DoubleMatrix)(x: Double): DoubleMatrix = m.div(x)

      def mulRow(m: DoubleMatrix)(i: Int, x: Double): DoubleMatrix = m.mulRow(i, x)
      def mulColumn(m: DoubleMatrix)(i: Int, x: Double): DoubleMatrix = m.mulColumn(i, x)

      // Operations on pairs of matrices

      //      def addMatrix(lhs: DoubleMatrix, rhs: DoubleMatrix): DoubleMatrix = ring.plus(lhs, rhs)
      //      def subtractMatrix(lhs: DoubleMatrix, rhs: DoubleMatrix): DoubleMatrix = ring.minus(lhs, rhs)
      //      def multiplyMatrix(lhs: DoubleMatrix, rhs: DoubleMatrix): DoubleMatrix = ring.times(lhs, rhs)

      def mulPointwise(m: DoubleMatrix)(rhs: DoubleMatrix): DoubleMatrix = m.mul(rhs)
      def divPointwise(m: DoubleMatrix)(rhs: DoubleMatrix): DoubleMatrix = m.div(rhs)

      def concatenateHorizontally(m: DoubleMatrix)(right: DoubleMatrix): DoubleMatrix = DoubleMatrix.concatHorizontally(m, right)
      def concatenateVertically(m: DoubleMatrix)(under: DoubleMatrix): DoubleMatrix = DoubleMatrix.concatVertically(m, under)
      def solve(m: DoubleMatrix)(B: DoubleMatrix): DoubleMatrix = org.jblas.Solve.solve(m, B) // returns X, where this === A and A x X = B

      // Operations on a matrix and a column/row vector

      def addRowVector(m: DoubleMatrix)(row: DoubleMatrix): DoubleMatrix = m.addRowVector(row)
      def addColumnVector(m: DoubleMatrix)(column: DoubleMatrix): DoubleMatrix = m.addColumnVector(column)
      def subRowVector(m: DoubleMatrix)(row: DoubleMatrix): DoubleMatrix = m.subRowVector(row)
      def subColumnVector(m: DoubleMatrix)(column: DoubleMatrix): DoubleMatrix = m.subColumnVector(column)
      def mulRowVector(m: DoubleMatrix)(row: DoubleMatrix): DoubleMatrix = m.mulRowVector(row)
      def mulColumnVector(m: DoubleMatrix)(column: DoubleMatrix): DoubleMatrix = m.mulColumnVector(column)
      def divRowVector(m: DoubleMatrix)(row: DoubleMatrix): DoubleMatrix = m.divRowVector(row)
      def divColumnVector(m: DoubleMatrix)(column: DoubleMatrix): DoubleMatrix = m.divColumnVector(column)

      // Operations on pair of matrices that return M[Boolean]

      def lt(m: DoubleMatrix)(rhs: DoubleMatrix): DoubleMatrix =
        m.lt(rhs)
      def le(m: DoubleMatrix)(rhs: DoubleMatrix): DoubleMatrix =
        m.le(rhs)
      def gt(m: DoubleMatrix)(rhs: DoubleMatrix): DoubleMatrix =
        m.gt(rhs)
      def ge(m: DoubleMatrix)(rhs: DoubleMatrix): DoubleMatrix =
        m.ge(rhs)
      def eq(m: DoubleMatrix)(rhs: DoubleMatrix): DoubleMatrix =
        m.eq(rhs)
      def ne(m: DoubleMatrix)(rhs: DoubleMatrix): DoubleMatrix =
        m.ne(rhs)

      def and(m: DoubleMatrix)(rhs: DoubleMatrix): DoubleMatrix =
        m.and(rhs)
      def or(m: DoubleMatrix)(rhs: DoubleMatrix): DoubleMatrix =
        m.or(rhs)
      def xor(m: DoubleMatrix)(rhs: DoubleMatrix): DoubleMatrix =
        m.xor(rhs)
      def not(m: DoubleMatrix): DoubleMatrix =
        m.not

      // various mins and maxs

      def max(m: DoubleMatrix): Double = m.max

      def argmax(m: DoubleMatrix): (Int, Int) = {
        val i = m.argmax
        (i % m.getColumns, i / m.getColumns)
      }

      def min(m: DoubleMatrix): Double = m.min

      def argmin(m: DoubleMatrix): (Int, Int) = {
        val i = m.argmin
        (i % m.getColumns, i / m.getColumns)
      }

      def rowSums(m: DoubleMatrix): DoubleMatrix = m.rowSums
      def columnSums(m: DoubleMatrix): DoubleMatrix = m.columnSums
      def columnMins(m: DoubleMatrix): DoubleMatrix = m.columnMins
      def columnMaxs(m: DoubleMatrix): DoubleMatrix = m.columnMaxs
      // def columnArgmins
      // def columnArgmaxs

      def columnMeans(m: DoubleMatrix): DoubleMatrix = m.columnMeans
      def sortColumns(m: DoubleMatrix): DoubleMatrix = m.sortColumns

      def rowMins(m: DoubleMatrix): DoubleMatrix = m.rowMins
      def rowMaxs(m: DoubleMatrix): DoubleMatrix = m.rowMaxs
      def rowMeans(m: DoubleMatrix): DoubleMatrix = m.rowMeans
      def sortRows(m: DoubleMatrix): DoubleMatrix = m.sortRows

      def matrix(r: Int, c: Int, values: Array[Double]): DoubleMatrix = {
        val jdm = new org.jblas.DoubleMatrix(values)
        jdm.reshape(r, c)
        jdm
      }

      def matrix(
        m: Int,
        n: Int,
        topleft: => Double,
        left: Int => Double,
        top: Int => Double,
        fill: (Int, Int, Double, Double, Double) => Double): DoubleMatrix = {

        val jblas = DoubleMatrix.zeros(m, n)
        jblas.put(0, 0, topleft)
        (0 until m) foreach { r => jblas.put(r, 0, left(r)) }
        (0 until n) foreach { c => jblas.put(0, c, top(c)) }
        (1 until m) foreach { r =>
          (1 until n) foreach { c =>
            val diag = jblas.get(r - 1, c - 1)
            val left = jblas.get(r, c - 1)
            val right = jblas.get(r - 1, c)
            jblas.put(r, c, fill(r, c, diag, left, right))
          }
        }
        jblas
      }

      def matrix(m: Int, n: Int, f: (Int, Int) => Double): DoubleMatrix = {
        val jblas = DoubleMatrix.zeros(m, n)
        (0 until m) foreach { r =>
          (0 until n) foreach { c =>
            jblas.put(r, c, f(r, c))
          }
        }
        jblas
      }

      // Higher-order methods

      def map(m: DoubleMatrix)(f: Double => Double): DoubleMatrix = {
        val jblas = DoubleMatrix.zeros(rows(m), columns(m))
        (0 until rows(m)) foreach { r =>
          (0 until columns(m)) foreach { c =>
            val t = this.get(m)(r, c)
            jblas.put(r, c, f(t))
          }
        }
        jblas
      }

      //def flatMapColumns[B](f: M[A] => M[B])(implicit fpB: FunctionPair[Double, B])
      def flatMapColumns(m: DoubleMatrix)(f: DoubleMatrix => DoubleMatrix): DoubleMatrix = {
        val jblas = DoubleMatrix.zeros(m.getRows, m.getColumns)
        (0 until m.getColumns) foreach { c =>
          val fc = f(column(m)(c))
          (0 until m.getRows) foreach { r =>
            // assumes fc.rows === this.rows
            jblas.put(r, c, get(fc)(r, 0))
          }
        }
        jblas
      }

      def centerRows(m: DoubleMatrix): DoubleMatrix =
        subColumnVector(m)(rowMeans(m))

      def centerColumns(m: DoubleMatrix): DoubleMatrix =
        subRowVector(m)(columnMeans(m))

      def rowRange(m: DoubleMatrix): DoubleMatrix =
        m.rowMaxs.sub(m.rowMins)

      def columnRange(m: DoubleMatrix): DoubleMatrix =
        m.columnMaxs.sub(m.columnMins)

      def sumsq(m: DoubleMatrix): DoubleMatrix =
        columnSums(mulPointwise(m)(m))

      def cov(m: DoubleMatrix): DoubleMatrix =
        centerColumns(m).transpose.mul(centerColumns(m)).div(m.getColumns)

      def std(m: DoubleMatrix): DoubleMatrix = {
        val centered = sumsq(centerColumns(m)).div(m.getColumns)
        map(centered)(scala.math.sqrt)
      }

      def zscore(m: DoubleMatrix): DoubleMatrix =
        divRowVector(centerColumns(m))(std(m))

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

      def pca(Xnorm: DoubleMatrix, cutoff: Double = 0.95): (DoubleMatrix, DoubleMatrix) = {
        val (u, s, v) = fullSVD(cov(Xnorm))
        (u, s)
      }

      def numComponentsForCutoff(s: DoubleMatrix, cutoff: Double): Int = {
        val eigenValuesSquared = toList(mulPointwise(s)(s))
        val eigenTotal = eigenValuesSquared.sum
        val numComponents = eigenValuesSquared.map(_ / eigenTotal).scan(0d)(_ + _).indexWhere(cutoff<)
        numComponents
      }

      def ones(laRows: Int, laColumns: Int): DoubleMatrix = DoubleMatrix.ones(laRows, laColumns)
      def rand(laRows: Int, laColumns: Int): DoubleMatrix = DoubleMatrix.rand(laRows, laColumns) // evenly distributed from 0.0 to 1.0
      def randn(laRows: Int, laColumns: Int): DoubleMatrix = DoubleMatrix.randn(laRows, laColumns) // normal distribution

    }

}
