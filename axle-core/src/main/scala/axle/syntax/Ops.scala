package axle.syntax

import axle.algebra.Matrix
import axle.algebra.FunctionPair
import axle.algebra.Vertex
import axle.algebra.DirectedGraph
import axle.algebra.DirectedEdge
import axle.algebra.UndirectedGraph
import axle.algebra.UndirectedEdge
import spire.algebra.Eq

final class MatrixOps[M[_]: Matrix, A](val lhs: M[A]) {

  val ev = implicitly[Matrix[M]]

  def get(i: Int, j: Int) = ev.get(lhs)(i, j)

  def slice(rs: Seq[Int], cs: Seq[Int]) = ev.slice(lhs)(rs, cs)

  def toList = ev.toList(lhs)

  def row(i: Int) = ev.row(lhs)(i)

  def column(j: Int) = ev.column(lhs)(j)

  def length = ev.length(lhs)

  def rows: Int = ev.rows(lhs)

  def columns: Int = ev.columns(lhs)

  def negate = ev.negate(lhs)

  //def fullSVD[T](m: M[A]) // (U, S, V) such that A = U * diag(S) * V' // TODO: all Matrix[Double] ?

  def pow(p: Double) = ev.pow(lhs)(p)

  def addScalar(x: A) = ev.addScalar(lhs)(x)
  def addAssignment(r: Int, c: Int, v: A) = ev.addAssignment(lhs)(r, c, v)
  def subtractScalar(x: A) = ev.subtractScalar(lhs)(x)
  def multiplyScalar(x: A) = ev.multiplyScalar(lhs)(x)
  def divideScalar(x: A) = ev.divideScalar(lhs)(x)
  def mulRow(i: Int, x: A) = ev.mulRow(lhs)(i, x)
  def mulColumn(i: Int, x: A) = ev.mulColumn(lhs)(i, x)

  // Operations on pairs of matrices
  // TODO: add and subtract don't make sense for T = Boolean

  def addMatrix(rhs: M[A]) = ev.addMatrix(lhs)(rhs)
  def subtractMatrix(rhs: M[A]) = ev.subtractMatrix(lhs)(rhs)
  def multiplyMatrix(rhs: M[A]) = ev.multiplyMatrix(lhs)(rhs)
  def mulPointwise(rhs: M[A]) = ev.mulPointwise(lhs)(rhs)
  def divPointwise(rhs: M[A]) = ev.divPointwise(lhs)(rhs)
  def concatenateHorizontally(rhs: M[A]) = ev.concatenateHorizontally(lhs)(rhs)
  def concatenateVertically(under: M[A]) = ev.concatenateVertically(lhs)(under)
  def solve(B: M[A]) = ev.solve(lhs)(B)

  // Operations on a matrix and a column/row vector

  def addRowVector(row: M[A]) = ev.addRowVector(lhs)(row)
  def addColumnVector(column: M[A]) = ev.addColumnVector(lhs)(column)
  def subRowVector(row: M[A]) = ev.subRowVector(lhs)(row)
  def subColumnVector(column: M[A]) = ev.subColumnVector(lhs)(column)
  def mulRowVector(row: M[A]) = ev.mulRowVector(lhs)(row)
  def mulColumnVector(column: M[A]) = ev.mulColumnVector(lhs)(column)
  def divRowVector(row: M[A]) = ev.divRowVector(lhs)(row)
  def divColumnVector(column: M[A]) = ev.divColumnVector(lhs)(column)

  // various mins and maxs

  def max = ev.max(lhs)
  def argmax = ev.argmax(lhs)
  def min = ev.min(lhs)
  def argmin = ev.argmin(lhs)

  def rowSums = ev.rowSums(lhs)
  def columnSums = ev.columnSums(lhs)
  def columnMins = ev.columnMins(lhs)
  def columnMaxs = ev.columnMaxs(lhs)
  // def columnArgmins
  // def columnArgmaxs

  def columnMeans = ev.columnMeans(lhs)
  def sortColumns = ev.sortColumns(lhs)

  def rowMins = ev.rowMins(lhs)
  def rowMaxs = ev.rowMaxs(lhs)
  def rowMeans = ev.rowMeans(lhs)
  def sortRows = ev.sortRows(lhs)

  // higher order methods

  def map[B](f: A => B)(implicit fpB: FunctionPair[Double, B]) = ev.map(lhs)(f)

  def flatMapColumns[B](f: M[A] => M[B])(implicit fpB: FunctionPair[Double, B]) = ev.flatMapColumns(lhs)(f)

  def foldLeft[B](zero: M[B])(f: (M[B], M[A]) => M[B]) = ev.foldLeft(lhs)(zero)(f)

  def foldTop[B](zero: M[B])(f: (M[B], M[A]) => M[B]) = ev.foldTop(lhs)(zero)(f)

  def sumsq = ev.sumsq(lhs)

  def cov = ev.cov(lhs)

  def std = ev.std(lhs)

  def zscore = ev.zscore(lhs)

  def pca(cutoff: Double = 0.95) = ev.pca(lhs, cutoff)

  def numComponentsForCutoff(cutoff: Double) = ev.numComponentsForCutoff(lhs, cutoff)

  // Aliases

  def t = ev.transpose(lhs)
  def tr = ev.transpose(lhs)
  def inv = ev.invert(lhs)

  def scalar: A = {
    assert(ev.isScalar(lhs))
    ev.get(lhs)(0, 0)
  }

  //def +(x: A) = ev.addScalar(lhs)(x)

  def +(rhs: M[A]) = ev.addMatrix(lhs)(rhs)

  //def -(x: A) = ev.subtractScalar(lhs)(x)

  def -(rhs: M[A]) = ev.subtractMatrix(lhs)(rhs)

  def *(x: A) = ev.multiplyScalar(lhs)(x)

  def ⨯(rhs: M[A]) = ev.multiplyMatrix(lhs)(rhs)
  def mm(rhs: M[A]) = ev.multiplyMatrix(lhs)(rhs)

  def /(x: A) = ev.divideScalar(lhs)(x)

  def +|+(right: M[A]) = ev.concatenateHorizontally(lhs)(right)

  def +/+(under: M[A]) = ev.concatenateVertically(lhs)(under)

  def aside(right: M[A]) = ev.concatenateHorizontally(lhs)(right)

  def atop(under: M[A]) = ev.concatenateVertically(lhs)(under)

  def <(rhs: M[A]) = ev.lt(lhs)(rhs)
  def <=(rhs: M[A]) = ev.le(lhs)(rhs)
  def ≤(rhs: M[A]) = ev.le(lhs)(rhs)
  def >(rhs: M[A]) = ev.gt(lhs)(rhs)
  def >=(rhs: M[A]) = ev.ge(lhs)(rhs)
  def ≥(rhs: M[A]) = ev.ge(lhs)(rhs)
  def ==(rhs: M[A]) = ev.eq(lhs)(rhs)
  def !=(rhs: M[A]) = ev.ne(lhs)(rhs)
  def ≠(rhs: M[A]) = ev.ne(lhs)(rhs)
  def &(rhs: M[A]) = ev.and(lhs)(rhs)
  def ∧(rhs: M[A]) = ev.and(lhs)(rhs)
  def |(rhs: M[A]) = ev.or(lhs)(rhs)
  def ∨(rhs: M[A]) = ev.or(lhs)(rhs)
  def ⊕(rhs: M[A]) = ev.xor(lhs)(rhs)
  def ⊻(rhs: M[A]) = ev.xor(lhs)(rhs)

  //  def ! = not
  //  def ~ = not
  //  def ¬ = not

}

final class DirectedGraphOps[DG[_, _]: DirectedGraph, VP: Eq, EP](val dg: DG[VP, EP]) {

  val ev = implicitly[DirectedGraph[DG]]

  def size = ev.size(dg)

  def findVertex(f: Vertex[VP] => Boolean): Option[Vertex[VP]] =
    ev.findVertex(dg, f)

  def vertices = ev.vertices(dg)

  def edges = ev.edges(dg)

  def precedes(v1: Vertex[VP], v2: Vertex[VP]) = ev.precedes(dg, v1, v2)
  
  def neighbors(v: Vertex[VP]) = ev.neighbors(dg, v)

  def predecessors(v: Vertex[VP]) = ev.predecessors(dg, v)
  
  def descendants(v: Vertex[VP]) = ev.descendants(dg, v)

  def descendantsIntersectsSet(v: Vertex[VP], s: Set[Vertex[VP]]) = ev.descendantsIntersectsSet(dg, v, s)
  
  // TODO: change first Edge type param:
  def shortestPath(source: Vertex[VP], goal: Vertex[VP]): Option[List[DirectedEdge[VP, EP]]] =
    ev.shortestPath(dg, source, goal)
    
  def leaves = ev.leaves(dg)
  
  def outputEdgesOf(v: Vertex[VP]) = ev.outputEdgesOf(dg, v)
}

final class UndirectedGraphOps[UG[_, _]: UndirectedGraph, VP: Eq, EP](val ug: UG[VP, EP]) {

  val ev = implicitly[UndirectedGraph[UG]]

  def size = ev.size(ug)

  def findVertex(f: Vertex[VP] => Boolean) =
    ev.findVertex(ug, f)

  def vertices = ev.vertices(ug)

  def neighbors(v: Vertex[VP]) = ev.neighbors(ug, v)

  def firstLeafOtherThan(r: Vertex[VP]) = ev.firstLeafOtherThan(ug, r)
}
