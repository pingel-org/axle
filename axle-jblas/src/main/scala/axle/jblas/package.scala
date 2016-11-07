package axle

import org.jblas.DoubleMatrix
import org.jblas.MatrixFunctions
import org.jblas.Solve

import cats.Show
import axle.algebra.Endofunctor
import axle.algebra.LinearAlgebra
import spire.algebra.AdditiveAbGroup
import spire.algebra.AdditiveCSemigroup
import spire.algebra.AdditiveMonoid
import spire.algebra.Eq
import spire.algebra.Field
import spire.algebra.InnerProductSpace
import spire.algebra.Module
import spire.algebra.MultiplicativeMonoid
import spire.algebra.MultiplicativeSemigroup
import spire.algebra.NRoot
import spire.algebra.Ring
import spire.algebra.Rng
import spire.implicits.convertableOps
import spire.implicits.multiplicativeGroupOps
import spire.math.ConvertableFrom
import spire.math.ConvertableTo

package object jblas {

  implicit def endoFunctorDoubleMatrix[N](implicit cfn: ConvertableFrom[N], ctn: ConvertableTo[N]): Endofunctor[DoubleMatrix, N] =
    new Endofunctor[DoubleMatrix, N] {
      def map(m: DoubleMatrix)(f: N => N): DoubleMatrix = {
        val jblas = DoubleMatrix.zeros(m.getRows, m.getColumns)
        (0 until m.getRows) foreach { r =>
          (0 until m.getColumns) foreach { c =>
            jblas.put(r, c, f(ctn.fromDouble(m.get(r, c))).toDouble)
          }
        }
        jblas
      }
    }

  implicit def eqDoubleMatrix = new Eq[DoubleMatrix] {

    def eqv(x: DoubleMatrix, y: DoubleMatrix): Boolean =
      x.equals(y)
  }

  // TODO put column count in type and make this implicit
  def rowVectorInnerProductSpace[R: MultiplicativeMonoid, C, N: Field](n: C)(
    implicit la: LinearAlgebra[DoubleMatrix, R, C, N],
    module: Module[DoubleMatrix, N],
    ctn: ConvertableTo[N]) =
    new InnerProductSpace[DoubleMatrix, N] {

      def negate(x: DoubleMatrix): DoubleMatrix = la.negate(x)

      def zero: DoubleMatrix = la.zeros(implicitly[MultiplicativeMonoid[R]].one, n)

      def plus(x: DoubleMatrix, y: DoubleMatrix): DoubleMatrix =
        la.ring.plus(x, y)

      def timesl(r: N, v: DoubleMatrix): DoubleMatrix =
        module.timesl(r, v)

      def scalar: Field[N] = Field[N]

      def dot(v: DoubleMatrix, w: DoubleMatrix): N =
        ctn.fromDouble(la.mulPointwise(v)(w).rowSums.scalar)
    }

  implicit def moduleDoubleMatrix[N](
    implicit rng: Rng[N],
    cfn: ConvertableFrom[N]): Module[DoubleMatrix, N] =
    new Module[DoubleMatrix, N] {

      def negate(x: DoubleMatrix): DoubleMatrix =
        additiveAbGroupDoubleMatrix.negate(x)

      def zero: DoubleMatrix =
        additiveCMonoidDoubleMatrix.zero

      def plus(x: DoubleMatrix, y: DoubleMatrix): DoubleMatrix =
        additiveCSemigroupDoubleMatrix.plus(x, y)

      implicit def scalar: Rng[N] = rng

      def timesl(r: N, v: DoubleMatrix): DoubleMatrix = v.mul(r.toDouble)

    }

  implicit def additiveCSemigroupDoubleMatrix: AdditiveCSemigroup[DoubleMatrix] =
    new AdditiveCSemigroup[DoubleMatrix] {

      def plus(x: DoubleMatrix, y: DoubleMatrix): DoubleMatrix =
        x.add(y)
    }

  implicit def additiveCMonoidDoubleMatrix: AdditiveMonoid[DoubleMatrix] =
    new AdditiveMonoid[DoubleMatrix] {

      lazy val semigroup = additiveCSemigroupDoubleMatrix

      def plus(x: DoubleMatrix, y: DoubleMatrix) =
        semigroup.plus(x, y)

      def zero: DoubleMatrix = ??? // DoubleMatrix.zeros(rows, columns)
    }

  implicit def multiplicativeSemigroupDoubleMatrix: MultiplicativeSemigroup[DoubleMatrix] =
    new MultiplicativeSemigroup[DoubleMatrix] {

      def times(x: DoubleMatrix, y: DoubleMatrix): DoubleMatrix =
        x.mmul(y)
    }

  implicit def multiplicativeMonoidDoubleMatrix: MultiplicativeMonoid[DoubleMatrix] =
    new MultiplicativeMonoid[DoubleMatrix] {

      lazy val semigroup = multiplicativeSemigroupDoubleMatrix

      def times(x: DoubleMatrix, y: DoubleMatrix) =
        semigroup.times(x, y)

      def one: DoubleMatrix = ??? // DoubleMatrix.eye(size)
    }

  implicit def additiveAbGroupDoubleMatrix: AdditiveAbGroup[DoubleMatrix] =
    new AdditiveAbGroup[DoubleMatrix] {

      lazy val additiveCMonoid = additiveCMonoidDoubleMatrix

      def zero: DoubleMatrix =
        additiveCMonoid.zero

      def plus(x: DoubleMatrix, y: DoubleMatrix): DoubleMatrix =
        additiveCMonoid.plus(x, y)

      def negate(x: DoubleMatrix): DoubleMatrix =
        x.neg
    }

  implicit def ringDoubleMatrix: Ring[DoubleMatrix] =
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

  implicit def showDoubleMatrix: Show[DoubleMatrix] =
    new Show[DoubleMatrix] {

      def show(m: DoubleMatrix): String =
        (0 until m.getRows) map { i =>
          (0 until m.getColumns) map { j =>
            string(m.get(i, j))
          } mkString (" ")
        } mkString ("\n")
    }

  implicit def linearAlgebraDoubleMatrix[N: Rng: NRoot](
    implicit cfn: ConvertableFrom[N],
    ctn: ConvertableTo[N]): LinearAlgebra[DoubleMatrix, Int, Int, N] =
    new LinearAlgebra[DoubleMatrix, Int, Int, N] {

      def elementRng: Rng[N] = Rng[N]

      def ring = ringDoubleMatrix

      def module = moduleDoubleMatrix

      def endofunctor = endoFunctorDoubleMatrix

      def rows(m: DoubleMatrix): Int = m.getRows

      def columns(m: DoubleMatrix): Int = m.getColumns

      def length(m: DoubleMatrix): Int = m.getLength

      def get(m: DoubleMatrix)(i: Int, j: Int): N = ctn.fromDouble(m.get(i, j))

      def slice(m: DoubleMatrix)(rs: Seq[Int], cs: Seq[Int]): DoubleMatrix = {
        val jblas = DoubleMatrix.zeros(rs.length, cs.length)
        rs.zipWithIndex foreach {
          case (fromRow, toRow) =>
            cs.zipWithIndex foreach {
              case (fromCol, toCol) =>
                jblas.put(toRow, toCol, this.get(m)(fromRow, fromCol).toDouble)
            }
        }
        jblas
      }

      def toList(m: DoubleMatrix): List[N] =
        m.toArray.toList.map(ctn.fromDouble)

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

      def addScalar(m: DoubleMatrix)(x: N): DoubleMatrix = m.add(x.toDouble)
      def subtractScalar(m: DoubleMatrix)(x: N): DoubleMatrix = m.sub(x.toDouble)
      // def multiplyScalar(m: DoubleMatrix)(x: N): DoubleMatrix = m.mul(x.toDouble)
      def divideScalar(m: DoubleMatrix)(x: N): DoubleMatrix = m.div(x.toDouble)

      def negate(m: DoubleMatrix): DoubleMatrix = ring.negate(m)

      def transpose(m: DoubleMatrix): DoubleMatrix = m.transpose
      def diag(m: DoubleMatrix): DoubleMatrix = m.diag
      def invert(m: DoubleMatrix): DoubleMatrix = Solve.solve(m, DoubleMatrix.eye(m.rows))
      def ceil(m: DoubleMatrix): DoubleMatrix = MatrixFunctions.ceil(m)
      def floor(m: DoubleMatrix): DoubleMatrix = MatrixFunctions.floor(m)
      def log(m: DoubleMatrix): DoubleMatrix = MatrixFunctions.log(m)
      def log10(m: DoubleMatrix): DoubleMatrix = MatrixFunctions.log10(m)

      /**
       *  (U, S, V) such that A = U * diag(S) * V' // TODO: all Matrix[Double] ?
       */
      def fullSVD(m: DoubleMatrix): (DoubleMatrix, DoubleMatrix, DoubleMatrix) = {
        val usv = org.jblas.Singular.fullSVD(m)
        (usv(0), usv(1), usv(2))
      }

      def pow(m: DoubleMatrix)(p: Double): DoubleMatrix =
        MatrixFunctions.pow(m, p)

      def addAssignment(m: DoubleMatrix)(r: Int, c: Int, v: N): DoubleMatrix = {
        val newJblas = m.dup
        newJblas.put(r, c, v.toDouble)
        newJblas
      }

      def mulRow(m: DoubleMatrix)(i: Int, x: N): DoubleMatrix =
        m.dup.mulRow(i, x.toDouble)

      def mulColumn(m: DoubleMatrix)(i: Int, x: N): DoubleMatrix =
        m.dup.mulColumn(i, x.toDouble)

      // Operations on pairs of matrices

      //      def addMatrix(lhs: DoubleMatrix, rhs: DoubleMatrix): DoubleMatrix = ring.plus(lhs, rhs)
      //      def subtractMatrix(lhs: DoubleMatrix, rhs: DoubleMatrix): DoubleMatrix = ring.minus(lhs, rhs)
      //      def multiplyMatrix(lhs: DoubleMatrix, rhs: DoubleMatrix): DoubleMatrix = ring.times(lhs, rhs)

      def mulPointwise(m: DoubleMatrix)(rhs: DoubleMatrix): DoubleMatrix = m.mul(rhs)
      def divPointwise(m: DoubleMatrix)(rhs: DoubleMatrix): DoubleMatrix = m.div(rhs)

      def zipWith(m: DoubleMatrix)(op: (N, N) => N)(rhs: DoubleMatrix): DoubleMatrix = {
        val numRows = m.getRows
        val numColumns = m.getColumns
        val jblas = DoubleMatrix.zeros(numRows, numColumns)
        (0 until numRows) foreach { r =>
          (0 until numColumns) foreach { c =>
            jblas.put(r, c, op(ctn.fromDouble(m.get(r, c)), ctn.fromDouble(rhs.get(r, c))).toDouble)
          }
        }
        jblas
      }

      def reduceToScalar(m: DoubleMatrix)(op: (N, N) => N): N = {
        val numRows = m.getRows
        val numColumns = m.getColumns
        ((0 until numRows) flatMap { r =>
          (0 until numColumns) map { c =>
            ctn.fromDouble(m.get(r, c))
          }
        }).reduce(op)
      }

      def concatenateHorizontally(m: DoubleMatrix)(right: DoubleMatrix): DoubleMatrix = DoubleMatrix.concatHorizontally(m, right)
      def concatenateVertically(m: DoubleMatrix)(under: DoubleMatrix): DoubleMatrix = DoubleMatrix.concatVertically(m, under)
      def solve(m: DoubleMatrix)(B: DoubleMatrix): DoubleMatrix = Solve.solve(m, B) // returns X, where this === A and A x X = B

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

      def max(m: DoubleMatrix): N = ctn.fromDouble(m.max)

      def argmax(m: DoubleMatrix): (Int, Int) = {
        val i = m.argmax
        (i % m.getRows, i / m.getRows)
      }

      def min(m: DoubleMatrix): N = ctn.fromDouble(m.min)

      def argmin(m: DoubleMatrix): (Int, Int) = {
        val i = m.argmin
        (i % m.getRows, i / m.getRows)
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

      def fromColumnMajorArray(r: Int, c: Int, values: Array[N]): DoubleMatrix = {
        val jdm = new org.jblas.DoubleMatrix(values.map(cfn.toDouble))
        jdm.reshape(r, c)
        jdm
      }

      def fromRowMajorArray(r: Int, c: Int, values: Array[N]): DoubleMatrix = {
        val jdm = new org.jblas.DoubleMatrix(values.map(cfn.toDouble))
        jdm.reshape(r, c)
        jdm.transpose
      }

      def matrix(
        m: Int,
        n: Int,
        topleft: => N,
        left: Int => N,
        top: Int => N,
        fill: (Int, Int, N, N, N) => N): DoubleMatrix = {

        val jblas = DoubleMatrix.zeros(m, n)
        jblas.put(0, 0, topleft.toDouble)
        (1 until m) foreach { r => jblas.put(r, 0, left(r).toDouble) }
        (1 until n) foreach { c => jblas.put(0, c, top(c).toDouble) }
        (1 until m) foreach { r =>
          (1 until n) foreach { c =>
            val diag = ctn.fromDouble(jblas.get(r - 1, c - 1))
            val left = ctn.fromDouble(jblas.get(r, c - 1))
            val right = ctn.fromDouble(jblas.get(r - 1, c))
            jblas.put(r, c, fill(r, c, diag, left, right).toDouble)
          }
        }
        jblas
      }

      def matrix(m: Int, n: Int, f: (Int, Int) => N): DoubleMatrix = {
        val jblas = DoubleMatrix.zeros(m, n)
        (0 until m) foreach { r =>
          (0 until n) foreach { c =>
            jblas.put(r, c, f(r, c).toDouble)
          }
        }
        jblas
      }

      // TODO this belongs in a Monad typeclass witness
      def flatMap(m: DoubleMatrix)(f: N => DoubleMatrix): DoubleMatrix =
        (0 until m.getRows).map(r => {
          (0 until m.getColumns).map(c => {
            f(ctn.fromDouble(m.get(r, c)))
          }).reduce(DoubleMatrix.concatHorizontally _)
        }).reduce(DoubleMatrix.concatVertically _)

      def foldLeft(m: DoubleMatrix)(zero: DoubleMatrix)(f: (DoubleMatrix, DoubleMatrix) => DoubleMatrix): DoubleMatrix =
        (0 until columns(m)).foldLeft(zero)((x: DoubleMatrix, c: Int) => f(x, column(m)(c)))

      def foldTop(m: DoubleMatrix)(zero: DoubleMatrix)(f: (DoubleMatrix, DoubleMatrix) => DoubleMatrix): DoubleMatrix =
        (0 until rows(m)).foldLeft(zero)((x: DoubleMatrix, r: Int) => f(x, row(m)(r)))

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
        endofunctor.map(centered)(n => spire.math.sqrt(n))
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

      def numComponentsForCutoff(s: DoubleMatrix, cutoff: Double)(implicit field: Field[N]): Int = {
        val eigenValuesSquared = toList(mulPointwise(s)(s))
        val eigenTotal = eigenValuesSquared.reduce(Ring[N].plus)
        val numComponents =
          eigenValuesSquared
            .map(_ / eigenTotal)
            .scan(Field[N].zero)(Field[N].plus)
            .indexWhere(v => cutoff < v.toDouble)
        numComponents
      }

      def zeros(laRows: Int, laColumns: Int): DoubleMatrix = DoubleMatrix.zeros(laRows, laColumns)
      def ones(laRows: Int, laColumns: Int): DoubleMatrix = DoubleMatrix.ones(laRows, laColumns)
      def rand(laRows: Int, laColumns: Int): DoubleMatrix = DoubleMatrix.rand(laRows, laColumns) // evenly distributed from 0.0 to 1.0
      def randn(laRows: Int, laColumns: Int): DoubleMatrix = DoubleMatrix.randn(laRows, laColumns) // normal distribution

    }

}
