package axle.algebra

import axle.string
import spire.algebra._
import spire.implicits._

trait LinearAlgebra[M, T] {

  def ring: Ring[M]

  def elementField: Field[T]

  def rows(m: M): Int

  def columns(m: M): Int

  def length(m: M): Int

  def get(m: M)(i: Int, j: Int): T

  def slice(m: M)(rs: Seq[Int], cs: Seq[Int]): M

  def toList(m: M): List[T]

  def column(m: M)(j: Int): M
  def row(m: M)(i: Int): M

  def isEmpty(m: M): Boolean
  def isRowVector(m: M): Boolean
  def isColumnVector(m: M): Boolean
  def isVector(m: M): Boolean
  def isSquare(m: M): Boolean
  def isScalar(m: M): Boolean

  def dup(m: M): M

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

  // TODO: from Module:
  def addScalar(m: M)(x: T): M
  def subtractScalar(m: M)(x: T): M
  def multiplyScalar(m: M)(x: T): M
  def divideScalar(m: M)(x: T): M

  def addAssignment(m: M)(r: Int, c: Int, v: T): M
  def mulRow(m: M)(i: Int, x: T): M
  def mulColumn(m: M)(i: Int, x: T): M

  // Operations on pairs of matrices

//  def addMatrix(m: M)(other: M): M
//  def subtractMatrix(m: M)(other: M): M
//  def multiplyMatrix(m: M)(other: M): M
  def mulPointwise(m: M)(other: M): M
  def divPointwise(m: M)(other: M): M
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
  def argmax(m: M): (Int, Int)
  def min(m: M): T
  def argmin(m: M): (Int, Int)

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

  def matrix(r: Int, c: Int, values: Array[T]): M

  def matrix(m: Int, n: Int, topleft: => T, left: Int => T, top: Int => T, fill: (Int, Int, T, T, T) => T): M

  def matrix(m: Int, n: Int, f: (Int, Int) => T): M

  // Higher-order methods

  def map(m: M)(f: T => T): M

  def flatMapColumns(m: M)(f: M => M): M

  def foldLeft(m: M)(zero: M)(f: (M, M) => M): M =
    (0 until columns(m)).foldLeft(zero)((x: M, c: Int) => f(x, column(m)(c)))

  def foldTop(m: M)(zero: M)(f: (M, M) => M): M =
    (0 until rows(m)).foldLeft(zero)((x: M, r: Int) => f(x, row(m)(r)))

  /**
   * Hilbert matrix
   */
  //  def hilb(n: Int) = matrix(n, n,
  //    (r: Int, c: Int) => elementField.one / (r + c + elementField.one))

  //  def median(m: M): M = {
  //    val sorted = sortColumns(m)
  //    if (rows(m) % 2 === 0) {
  //      val left = row(sorted)(rows(m) / 2 - 1)
  //      val right = row(sorted)(rows(m) / 2)
  //      divideScalar(plus(left)(right))(2d)
  //    } else {
  //      row(sorted)(rows(m) / 2)
  //    }
  //  }

  def centerRows(m: M): M
  def centerColumns(m: M): M

  def rowRange(m: M): M
  def columnRange(m: M): M

  def sumsq(m: M): M

  def cov(m: M): M

  def std(m: M): M

  def zscore(m: M): M

  def pca(Xnorm: M, cutoff: Double): (M, M)

  def numComponentsForCutoff(s: M, cutoff: Double): Int

//  def zeros(m: Int, n: Int): M
//  def eye(n: Int): M
//  def I(n: Int): M
  def ones(m: Int, n: Int): M
  def rand(m: Int, n: Int): M
  def randn(m: Int, n: Int): M
  def falses(m: Int, n: Int): M
  def trues(m: Int, n: Int): M

}
