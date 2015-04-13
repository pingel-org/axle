package axle.syntax

import axle.algebra.Aggregatable
import axle.algebra.DirectedGraph
import axle.algebra.DirectedEdge
import axle.algebra.Endofunctor
import axle.algebra.Finite
import axle.algebra.FunctionPair
import axle.algebra.Functor
import axle.algebra.Indexed
import axle.algebra.MapFrom
import axle.algebra.MapReducible
import axle.algebra.LinearAlgebra
import axle.algebra.SetFrom
import axle.algebra.UndirectedGraph
import axle.algebra.UndirectedEdge
import axle.algebra.Vertex
import axle.algebra.Zero
import scala.reflect.ClassTag
import spire.algebra.Eq
import spire.algebra.Ring

final class LinearAlgebraOps[M, RowT, ColT, T](val lhs: M)(implicit la: LinearAlgebra[M, RowT, ColT, T]) {

  def get(i: RowT, j: ColT) = la.get(lhs)(i, j)

  def slice(rs: Seq[RowT], cs: Seq[ColT]) = la.slice(lhs)(rs, cs)

  def toList = la.toList(lhs)

  def row(i: RowT) = la.row(lhs)(i)

  def column(j: ColT) = la.column(lhs)(j)

  def length = la.length(lhs)

  def rows = la.rows(lhs)

  def columns = la.columns(lhs)

  def negate = la.negate(lhs)

  //def fullSVD[T](m: M[A]) // (U, S, V) such that A = U * diag(S) * V' // TODO: all Matrix[Double] ?

  def pow(p: Double) = la.pow(lhs)(p)

  def addScalar(x: T) = la.addScalar(lhs)(x)
  def subtractScalar(x: T) = la.subtractScalar(lhs)(x)

  //  def multiplyScalar(x: T) = la.multiplyScalar(lhs)(x)
  def divideScalar(x: T) = la.divideScalar(lhs)(x)

  def addAssignment(r: RowT, c: ColT, v: T) = la.addAssignment(lhs)(r, c, v)
  def mulRow(i: RowT, x: T) = la.mulRow(lhs)(i, x)
  def mulColumn(i: ColT, x: T) = la.mulColumn(lhs)(i, x)

  // Operations on pairs of matrices
  // TODO: add and subtract don't make sense for T = Boolean

  //def plus(rhs: M) = la.plus(lhs, rhs)
  //def +(rhs: M) = la.plus(lhs, rhs)
  //def minus(rhs: M) = la.minus(lhs, rhs)
  //def -(rhs: M) = la.minus(lhs, rhs)
  //def times(rhs: M) = la.times(lhs, rhs)
  //def ⨯(rhs: M) = la.times(lhs, rhs)
  //def *(rhs: M) = la.times(lhs, rhs)

  def mulPointwise(rhs: M) = la.mulPointwise(lhs)(rhs)
  def divPointwise(rhs: M) = la.divPointwise(lhs)(rhs)
  def concatenateHorizontally(rhs: M) = la.concatenateHorizontally(lhs)(rhs)
  def concatenateVertically(under: M) = la.concatenateVertically(lhs)(under)
  def solve(B: M) = la.solve(lhs)(B)

  // Operations on a matrix and a column/row vector

  def addRowVector(row: M) = la.addRowVector(lhs)(row)
  def addColumnVector(column: M) = la.addColumnVector(lhs)(column)
  def subRowVector(row: M) = la.subRowVector(lhs)(row)
  def subColumnVector(column: M) = la.subColumnVector(lhs)(column)
  def mulRowVector(row: M) = la.mulRowVector(lhs)(row)
  def mulColumnVector(column: M) = la.mulColumnVector(lhs)(column)
  def divRowVector(row: M) = la.divRowVector(lhs)(row)
  def divColumnVector(column: M) = la.divColumnVector(lhs)(column)

  // various mins and maxs

  def max = la.max(lhs)
  def argmax = la.argmax(lhs)
  def min = la.min(lhs)
  def argmin = la.argmin(lhs)

  def rowSums = la.rowSums(lhs)
  def columnSums = la.columnSums(lhs)
  def columnMins = la.columnMins(lhs)
  def columnMaxs = la.columnMaxs(lhs)
  // def columnArgmins
  // def columnArgmaxs

  def columnMeans = la.columnMeans(lhs)
  def sortColumns = la.sortColumns(lhs)

  def rowMins = la.rowMins(lhs)
  def rowMaxs = la.rowMaxs(lhs)
  def rowMeans = la.rowMeans(lhs)
  def sortRows = la.sortRows(lhs)

  // higher order methods

  def flatMapColumns(f: M => M) = la.flatMapColumns(lhs)(f)

  def foldLeft(zero: M)(f: (M, M) => M) = la.foldLeft(lhs)(zero)(f)

  def foldTop(zero: M)(f: (M, M) => M) = la.foldTop(lhs)(zero)(f)

  def sumsq = la.sumsq(lhs)

  // Aliases

  def t = la.transpose(lhs)
  def tr = la.transpose(lhs)
  def inv = la.invert(lhs)

  def scalar(implicit rz: Zero[RowT], cz: Zero[ColT]): T = {
    assert(la.isScalar(lhs))
    la.get(lhs)(rz.zero, cz.zero)
  }

  //def +(x: A) = la.addScalar(lhs)(x)
  //def *(x: T) = la.multiplyScalar(lhs)(x)

  // def ⨯(rhs: M) = la.multiplyMatrix(lhs)(rhs)

  //def /(x: T) = la.divideScalar(lhs)(x)

  def +|+(right: M) = la.concatenateHorizontally(lhs)(right)

  def +/+(under: M) = la.concatenateVertically(lhs)(under)

  def aside(right: M) = la.concatenateHorizontally(lhs)(right)

  def atop(under: M) = la.concatenateVertically(lhs)(under)

  def <(rhs: M) = la.lt(lhs)(rhs)
  def <=(rhs: M) = la.le(lhs)(rhs)
  def ≤(rhs: M) = la.le(lhs)(rhs)
  def >(rhs: M) = la.gt(lhs)(rhs)
  def >=(rhs: M) = la.ge(lhs)(rhs)
  def ≥(rhs: M) = la.ge(lhs)(rhs)
  def ==(rhs: M) = la.eq(lhs)(rhs)
  def !=(rhs: M) = la.ne(lhs)(rhs)
  def ≠(rhs: M) = la.ne(lhs)(rhs)
  def &(rhs: M) = la.and(lhs)(rhs)
  def ∧(rhs: M) = la.and(lhs)(rhs)
  def |(rhs: M) = la.or(lhs)(rhs)
  def ∨(rhs: M) = la.or(lhs)(rhs)
  def ⊕(rhs: M) = la.xor(lhs)(rhs)
  def ⊻(rhs: M) = la.xor(lhs)(rhs)

  //  def ! = not
  //  def ~ = not
  //  def ¬ = not

}

final class DirectedGraphOps[DG[_, _]: DirectedGraph, VP: Eq, EP](val dg: DG[VP, EP]) {

  val ev = DirectedGraph[DG]

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

  val ev = UndirectedGraph[UG]

  def size = ev.size(ug)

  def findVertex(f: Vertex[VP] => Boolean) =
    ev.findVertex(ug, f)

  def vertices = ev.vertices(ug)

  def neighbors(v: Vertex[VP]) = ev.neighbors(ug, v)

  def firstLeafOtherThan(r: Vertex[VP]) = ev.firstLeafOtherThan(ug, r)
}

final class FunctorOps[F[_]: Functor, A](val as: F[A]) {

  val ev = Functor[F]

  def map[B: ClassTag](f: A => B) = ev.map(as)(f)

}

final class EndofunctorOps[E, A](val e: E)(implicit ev: Endofunctor[E, A]) {

  def map(f: A => A) = ev.map(e)(f)

}

final class AggregatableOps[G[_]: Aggregatable, A](val ts: G[A]) {

  val ev = Aggregatable[G]

  def aggregate[B: ClassTag](zeroValue: B)(seqOp: (B, A) => B, combOp: (B, B) => B) =
    ev.aggregate(ts)(zeroValue)(seqOp, combOp)

  def tally[N: Ring](implicit aEq: Eq[A]) = ev.tally(ts)
}

final class FiniteOps[F[_], S, A: ClassTag](val as: F[A])(
  implicit finite: Finite[F, S]) {

  def size = finite.size(as)
}

final class IndexedOps[F[_], IndexT, A: ClassTag](
  val as: F[A])(
    implicit index: Indexed[F, IndexT]) {

  def at(i: IndexT) = index.at(as)(i)
}

final class MapReducibleOps[M[_]: MapReducible, A: ClassTag](val as: M[A]) {

  val ev = MapReducible[M]

  def mapReduce[B: ClassTag, K: ClassTag](mapper: A => (K, B), zero: B, op: (B, B) => B) =
    ev.mapReduce[A, B, K](as, mapper, zero, op)
}

final class SetFromOps[F[_]: SetFrom, A: ClassTag](val as: F[A]) {

  val ev = SetFrom[F]

  def toSet = ev.toSet(as)
}

final class MapFromOps[F[_]: MapFrom, K: ClassTag, V: ClassTag](val fkv: F[(K, V)]) {

  val ev = MapFrom[F]

  def toMap = ev.toMap(fkv)
}
