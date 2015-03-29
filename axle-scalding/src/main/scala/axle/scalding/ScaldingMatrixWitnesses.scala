package axle.scalding

import com.twitter.algebird.{Field => AlgebirdField}
import com.twitter.algebird.{Monoid => AlgebirdMonoid}
import com.twitter.scalding.mathematics.Matrix

import axle.Show
import axle.algebra.Endofunctor
import axle.algebra.LinearAlgebra
import spire.algebra.AdditiveAbGroup
import spire.algebra.AdditiveCSemigroup
import spire.algebra.AdditiveMonoid
import spire.algebra.Field
import spire.algebra.Module
import spire.algebra.MultiplicativeMonoid
import spire.algebra.MultiplicativeSemigroup
import spire.algebra.Ring
import spire.algebra.Rng

/**
 *
 * See https://github.com/twitter/scalding/tree/master/tutorial
 *
 */

object ScaldingMatrixWitnesses {

  implicit def endoFunctorMatrix[RowT, ColT, T: AlgebirdField]: Endofunctor[Matrix[RowT, ColT, T], T] =
    new Endofunctor[Matrix[RowT, ColT, T], T] {
      def map(m: Matrix[RowT, ColT, T])(f: T => T): Matrix[RowT, ColT, T] = {
        m mapValues f
      }
    }

  implicit def moduleMatrix[RowT, ColT, T: AlgebirdField]: Module[Matrix[RowT, ColT, T], T] =
    new Module[Matrix[RowT, ColT, T], T] {

      // Members declared in spire.algebra.AdditiveGroup
      def negate(x: Matrix[RowT, ColT, T]): Matrix[RowT, ColT, T] =
        additiveAbGroupMatrix.negate(x)

      def zero: Matrix[RowT, ColT, T] =
        additiveCMonoidMatrix.zero

      // Members declared in spire.algebra.AdditiveSemigroup
      def plus(x: Matrix[RowT, ColT, T], y: Matrix[RowT, ColT, T]): Matrix[RowT, ColT, T] =
        additiveCSemigroupMatrix.plus(x, y)

      // Members declared in spire.algebra.Module
      implicit def scalar: Rng[T] = ??? // TODO convert AlgebirdField to Rng

      def timesl(r: T, v: Matrix[RowT, ColT, T]): Matrix[RowT, ColT, T] =
        ??? //v.mapValues(_ * r)

    }

  implicit def additiveCSemigroupMatrix[RowT, ColT, T: AlgebirdMonoid]: AdditiveCSemigroup[Matrix[RowT, ColT, T]] =
    new AdditiveCSemigroup[Matrix[RowT, ColT, T]] {

      def plus(x: Matrix[RowT, ColT, T], y: Matrix[RowT, ColT, T]): Matrix[RowT, ColT, T] =
        ??? //x.+(y)
    }

  implicit def additiveCMonoidMatrix[RowT, ColT, T: AlgebirdMonoid]: AdditiveMonoid[Matrix[RowT, ColT, T]] =
    new AdditiveMonoid[Matrix[RowT, ColT, T]] {

      lazy val semigroup = additiveCSemigroupMatrix[RowT, ColT, T]

      def plus(x: Matrix[RowT, ColT, T], y: Matrix[RowT, ColT, T]) =
        semigroup.plus(x, y)

      def zero: Matrix[RowT, ColT, T] =
        ???
    }

  implicit def multiplicativeSemigroupMatrix[RowT, ColT, T]: MultiplicativeSemigroup[Matrix[RowT, ColT, T]] =
    new MultiplicativeSemigroup[Matrix[RowT, ColT, T]] {

      def times(x: Matrix[RowT, ColT, T], y: Matrix[RowT, ColT, T]): Matrix[RowT, ColT, T] =
        ??? //x.*(y)
    }

  implicit def multiplicativeMonoidMatrix[RowT, ColT, T: AlgebirdField]: MultiplicativeMonoid[Matrix[RowT, ColT, T]] =
    new MultiplicativeMonoid[Matrix[RowT, ColT, T]] {

      lazy val semigroup = multiplicativeSemigroupMatrix[RowT, ColT, T]

      def times(x: Matrix[RowT, ColT, T], y: Matrix[RowT, ColT, T]) =
        semigroup.times(x, y)

      def one: Matrix[RowT, ColT, T] =
        ??? // Matrix.eye(???) // TODO: dimension m
    }

  implicit def additiveAbGroupMatrix[RowT, ColT, T: AlgebirdField]: AdditiveAbGroup[Matrix[RowT, ColT, T]] =
    new AdditiveAbGroup[Matrix[RowT, ColT, T]] {

      lazy val additiveCMonoid = additiveCMonoidMatrix[RowT, ColT, T]

      def zero: Matrix[RowT, ColT, T] =
        additiveCMonoid.zero

      def plus(x: Matrix[RowT, ColT, T], y: Matrix[RowT, ColT, T]): Matrix[RowT, ColT, T] =
        additiveCMonoid.plus(x, y)

      def negate(x: Matrix[RowT, ColT, T]): Matrix[RowT, ColT, T] =
        ???
    }

  implicit def ringMatrix[RowT, ColT, T: AlgebirdField]: Ring[Matrix[RowT, ColT, T]] =
    new Ring[Matrix[RowT, ColT, T]] {

      lazy val additiveAbGroup = additiveAbGroupMatrix[RowT, ColT, T]

      def negate(x: Matrix[RowT, ColT, T]): Matrix[RowT, ColT, T] =
        additiveAbGroup.negate(x)

      def zero: Matrix[RowT, ColT, T] =
        additiveAbGroup.zero

      def plus(x: Matrix[RowT, ColT, T], y: Matrix[RowT, ColT, T]): Matrix[RowT, ColT, T] =
        additiveAbGroup.plus(x, y)

      lazy val multiplicativeMonoid = multiplicativeMonoidMatrix[RowT, ColT, T]

      def one: Matrix[RowT, ColT, T] =
        multiplicativeMonoid.one

      def times(x: Matrix[RowT, ColT, T], y: Matrix[RowT, ColT, T]): Matrix[RowT, ColT, T] =
        multiplicativeMonoid.times(x, y)
    }

  implicit def showMatrix[RowT, ColT, T]: Show[Matrix[RowT, ColT, T]] =
    new Show[Matrix[RowT, ColT, T]] {

      def text(m: Matrix[RowT, ColT, T]): String =
        ???
    }

  implicit def linearAlgebraMatrix[RowT, ColT, T: AlgebirdField]: LinearAlgebra[Matrix[RowT, ColT, T], RowT, ColT, T] =
    new LinearAlgebra[Matrix[RowT, ColT, T], RowT, ColT, T] {

      type M = Matrix[RowT, ColT, T]

      def elementField: Field[T] = ??? //implicitly[AlgebirdField[T]] // TODO convert to Spire field

      def ring = ringMatrix

      def module = moduleMatrix[RowT, ColT, T]

      def endofunctor = endoFunctorMatrix

      def rows(m: M): RowT = ???

      def columns(m: M): ColT = ???

      def length(m: M): Int = ???

      def get(m: M)(i: RowT, j: ColT): T = ???

      def slice(m: M)(rs: Seq[RowT], cs: Seq[ColT]): M =
        ???

      def toList(m: M): List[T] =
        ???

      def column(m: M)(j: ColT): M =
        ???

      def row(m: M)(i: RowT): M =
        ???

      def isEmpty(m: M): Boolean = ???
      def isRowVector(m: M): Boolean = ???
      def isColumnVector(m: M): Boolean = ???
      def isVector(m: M): Boolean = ???
      def isSquare(m: M): Boolean = ???
      def isScalar(m: M): Boolean = ???

      def dup(m: M): M = ???

      def addScalar(m: M)(x: T): M = ???

      def subtractScalar(m: M)(x: T): M = ???

      // def multiplyScalar(m: Matrix)(x: Double): Matrix = ???

      def divideScalar(m: M)(x: T): M = ???

      def negate(m: M): M = ring.negate(m)

      def transpose(m: M): M = ???

      def diag(m: M): M = ???

      def invert(m: M): M = ??? // org.jblas.Solve.solve(m, Matrix.eye(m.rows))

      def ceil(m: M): M = ???

      def floor(m: M): M = ???

      def log(m: M): M = ???

      def log10(m: M): M = ???

      /**
       *  (U, S, V) such that A = U * diag(S) * V' // TODO: all Matrix[Double] ?
       */
      def fullSVD(m: M): (M, M, M) =
        ???

      def pow(m: M)(p: Double): M =
        ???

      def addAssignment(m: M)(r: RowT, c: ColT, v: T): M =
        ???

      def mulRow(m: M)(i: RowT, x: T): M = ???

      def mulColumn(m: M)(i: ColT, x: T): M = ???

      // Operations on pairs of matrices

      //      def addMatrix(lhs: Matrix, rhs: Matrix): Matrix = ring.plus(lhs, rhs)
      //      def subtractMatrix(lhs: Matrix, rhs: Matrix): Matrix = ring.minus(lhs, rhs)
      //      def multiplyMatrix(lhs: Matrix, rhs: Matrix): Matrix = ring.times(lhs, rhs)

      def mulPointwise(m: M)(rhs: M): M =
        ???

      def divPointwise(m: M)(rhs: M): M =
        ???

      def concatenateHorizontally(m: M)(right: M): M =
        ???

      def concatenateVertically(m: M)(under: M): M =
        ???

      def solve(m: M)(B: M): M =
        ??? //org.jblas.Solve.solve(m, B) // returns X, where this === A and A x X = B

      // Operations on a matrix and a column/row vector

      def addRowVector(m: M)(row: M): M =
        ???

      def addColumnVector(m: M)(column: M): M =
        ???

      def subRowVector(m: M)(row: M): M =
        ???

      def subColumnVector(m: M)(column: M): M =
        ???

      def mulRowVector(m: M)(row: M): M =
        ???

      def mulColumnVector(m: M)(column: M): M =
        ???

      def divRowVector(m: M)(row: M): M =
        ???

      def divColumnVector(m: M)(column: M): M =
        ???

      // Operations on pair of matrices that return M[Boolean]

      def lt(m: M)(rhs: M): M =
        ???

      def le(m: M)(rhs: M): M =
        ???

      def gt(m: M)(rhs: M): M =
        ???

      def ge(m: M)(rhs: M): M =
        ???

      def eq(m: M)(rhs: M): M =
        ???

      def ne(m: M)(rhs: M): M =
        ???

      def and(m: M)(rhs: M): M =
        ???

      def or(m: M)(rhs: M): M =
        ???

      def xor(m: M)(rhs: M): M =
        ???

      def not(m: M): M =
        ???

      // various mins and maxs

      def max(m: M): T = ???

      def argmax(m: M): (RowT, ColT) = ???

      def min(m: M): T = ???

      def argmin(m: M): (RowT, ColT) =
        ???

      def rowSums(m: M): M = ???
      def columnSums(m: M): M = ???
      def columnMins(m: M): M = ???
      def columnMaxs(m: M): M = ???
      // def columnArgmins
      // def columnArgmaxs

      def columnMeans(m: M): M = ???
      def sortColumns(m: M): M = ???

      def rowMins(m: M): M = ???
      def rowMaxs(m: M): M = ???
      def rowMeans(m: M): M = ???
      def sortRows(m: M): M = ???

      def matrix(r: RowT, c: ColT, values: Array[T]): M =
        ???

      def matrix(
        m: RowT,
        n: ColT,
        topleft: => T,
        left: RowT => T,
        top: ColT => T,
        fill: (RowT, ColT, T, T, T) => T): M = ???

      def matrix(m: RowT, n: ColT, f: (RowT, ColT) => T): M = ???

      //def flatMapColumns[B](f: M[A] => M[B])(implicit fpB: FunctionPair[Double, B])
      def flatMapColumns(m: M)(f: M => M): M = ???

      def foldLeft(m: M)(zero: M)(f: (M, M) => M): M =
        ??? //(0 until columns(m)).foldLeft(zero)((x: M, c: ColT) => f(x, column(m)(c)))

      def foldTop(m: M)(zero: M)(f: (M, M) => M): M =
        ??? //(0 until rows(m)).foldLeft(zero)((x: M, r: RowT) => f(x, row(m)(r)))

      def centerRows(m: M): M =
        subColumnVector(m)(rowMeans(m))

      def centerColumns(m: M): M =
        subRowVector(m)(columnMeans(m))

      def rowRange(m: M): M =
        ???

      def columnRange(m: M): M =
        ???

      def sumsq(m: M): M =
        columnSums(mulPointwise(m)(m))

      def cov(m: M): M =
        ???

      def std(m: M): M =
        ???

      def zscore(m: M): M =
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

      def pca(Xnorm: M, cutoff: Double = 0.95): (M, M) = {
        val (u, s, v) = fullSVD(cov(Xnorm))
        (u, s)
      }

      def numComponentsForCutoff(s: M, cutoff: Double): Int = ???

      def zeros(laRows: RowT, laColumns: ColT): M = ???

      def ones(laRows: RowT, laColumns: ColT): M = ???

      def rand(laRows: RowT, laColumns: ColT): M = ??? // evenly distributed from 0.0 to 1.0

      def randn(laRows: RowT, laColumns: ColT): M = ??? // normal distribution

    }

}
