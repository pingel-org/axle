package axle.algebra

import scala.annotation.implicitNotFound
import spire.algebra._
import org.scalacheck.Gen
import org.scalacheck.Gen.Choose
import scala.reflect.ClassTag

@implicitNotFound("Witness not found for LinearAlgebra[${M}, ${R}, ${C}, ${T}]")
trait LinearAlgebra[M, R, C, T] {

  def additive: AdditiveCSemigroup[M]

  def minus(x: M, y: M): M

  def multiplicative: MultiplicativeSemigroup[M]

  def elementRng: Rng[T]

  def endofunctor: Endofunctor[M, T]

  def rows(m: M): R

  def columns(m: M): C

  def length(m: M): Int // TODO

  def get(m: M)(i: R, j: C): T

  def slice(m: M)(rs: Seq[R], cs: Seq[C]): M

  def toList(m: M): List[T]

  def column(m: M)(j: C): M
  def row(m: M)(i: R): M

  def isEmpty(m: M): Boolean
  def isRowVector(m: M): Boolean
  def isColumnVector(m: M): Boolean
  def isVector(m: M): Boolean
  def isSquare(m: M): Boolean
  def isScalar(m: M): Boolean

  def dup(m: M): M

  def negate(m: M): M

  def transpose(m: M): M
  def diag(m: M): M
  def invert(m: M): M
  def ceil(m: M): M
  def floor(m: M): M
  def log(m: M): M
  def log10(m: M): M

  def fullSVD(m: M): (M, M, M) // (U, S, V) such that A = U * diag(S) * V' // TODO: all Matrix[Double] ?
  // def truth: M[Boolean]

  def pow(m: M)(p: Double): M

  def addScalar(m: M)(x: T): M
  def subtractScalar(m: M)(x: T): M

  def multiplyScalar(m: M)(x: T): M
  def divideScalar(m: M)(x: T): M

  def addAssignment(m: M)(r: R, c: C, v: T): M
  def mulRow(m: M)(i: R, x: T): M
  def mulColumn(m: M)(i: C, x: T): M

  // Operations on pairs of matrices

  def mulPointwise(m: M)(other: M): M
  def divPointwise(m: M)(other: M): M

  def zipWith(m: M)(op: (T, T) => T)(other: M): M
  def reduceToScalar(m: M)(op: (T, T) => T): T

  def concatenateHorizontally(m: M)(right: M): M
  def concatenateVertically(m: M)(under: M): M
  def solve(m: M)(B: M): M // returns X, where this === A and A x X = B

  // Operations on a matrix and a column/row vector

  def addRowVector(m: M)(row: M): M
  def addColumnVector(m: M)(column: M): M
  def subRowVector(m: M)(row: M): M
  def subColumnVector(m: M)(column: M): M
  def mulRowVector(m: M)(row: M): M
  def mulColumnVector(m: M)(column: M): M
  def divRowVector(m: M)(row: M): M
  def divColumnVector(m: M)(column: M): M

  // Operations on pair of matrices that return M[Boolean]

  def lt(m: M)(other: M): M
  def le(m: M)(other: M): M
  def gt(m: M)(other: M): M
  def ge(m: M)(other: M): M
  def eq(m: M)(other: M): M
  def ne(m: M)(other: M): M

  def and(m: M)(other: M): M
  def or(m: M)(other: M): M
  def xor(m: M)(other: M): M
  def not(m: M): M

  // various mins and maxs

  def max(m: M): T
  def argmax(m: M): (R, C)
  def min(m: M): T
  def argmin(m: M): (R, C)

  def rowSums(m: M): M
  def columnSums(m: M): M
  def columnMins(m: M): M
  def columnMaxs(m: M): M
  // def columnArgmins
  // def columnArgmaxs

  def columnMeans(m: M): M
  def sortColumns(m: M): M

  def rowMins(m: M): M
  def rowMaxs(m: M): M
  def rowMeans(m: M): M
  def sortRows(m: M): M

  def fromColumnMajorArray(r: R, c: C, values: Array[T]): M

  def fromRowMajorArray(r: R, c: C, values: Array[T]): M

  def matrix(m: R, n: C, topleft: => T, left: R => T, top: C => T, fill: (R, C, T, T, T) => T): M

  def matrix(m: R, n: C, f: (R, C) => T): M

  // Higher-order methods

  def flatMap(m: M)(f: T => M): M

  def foldLeft(m: M)(zero: M)(f: (M, M) => M): M

  def foldTop(m: M)(zero: M)(f: (M, M) => M): M

  /**
   * Hilbert matrix
   */
  //  def hilb(n: Int) = matrix(n, n,
  //    (r: Int, c: Int) => elementField.one / (r + c + elementField.one))

  def centerRows(m: M): M
  def centerColumns(m: M): M

  def rowRange(m: M): M
  def columnRange(m: M): M

  def sumsq(m: M): M

  def cov(m: M): M

  def std(m: M): M

  def zscore(m: M): M

  def pca(Xnorm: M): (M, M)

  def numComponentsForCutoff(s: M, cutoff: Double)(implicit field: Field[T]): Int

  // TODO:
  def zeros(laRows: R, laColumns: C): M
  def eye(laRows: R): M
  def I(laRows: R): M = eye(laRows)
  def ones(laRows: R, laColumns: C): M
  def rand(laRows: R, laColumns: C): M
  def randn(laRows: R, laColumns: C): M

}

object LinearAlgebra {

  final def apply[M, R, C, T](implicit ev: LinearAlgebra[M, R, C, T]): LinearAlgebra[M, R, C, T] = ev

  def genMatrix[M, T: Choose: ClassTag](
    m:       Int,
    n:       Int,
    minimum: T,
    maximum: T)(
    implicit
    la: LinearAlgebra[M, Int, Int, T]): Gen[M] =
    Gen.listOfN[T](m * n, Gen.choose[T](minimum, maximum)) map { numList =>
      la.fromColumnMajorArray(m, n, numList.toArray)
    }

}
