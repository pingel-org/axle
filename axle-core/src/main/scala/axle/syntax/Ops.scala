package axle.syntax

import axle.algebra.Aggregatable
import axle.algebra.DirectedGraph
import axle.algebra.Endofunctor
import axle.algebra.Finite
import axle.algebra.FunctionPair
import axle.algebra.Functor
import axle.algebra.Indexed
import axle.algebra.MapFrom
import axle.algebra.MapReducible
import axle.algebra.LinearAlgebra
import axle.algebra.SetFrom
import axle.algebra.Talliable
import axle.algebra.UndirectedGraph
import axle.algebra.Zero
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

  def zipWith(op: (T, T) => T)(rhs: M): M = la.zipWith(lhs)(op)(rhs)
  def reduceToScalar(op: (T, T) => T): T = la.reduceToScalar(lhs)(op)

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

final class DirectedGraphOps[DG, V, E](val dg: DG)(implicit ev: DirectedGraph[DG, V, E]) {

  def findVertex(f: V => Boolean): Option[V] =
    ev.findVertex(dg, f)

  def vertices = ev.vertices(dg)

  def edges = ev.edges(dg)

  def source(e: E) = ev.source(dg, e)

  def destination(e: E) = ev.destination(dg, e)

  def precedes(v1: V, v2: V) = ev.precedes(dg, v1, v2)

  def neighbors(v: V) = ev.neighbors(dg, v)

  def predecessors(v: V) = ev.predecessors(dg, v)

  def successors(v: V) = ev.successors(dg, v)

  def descendants(v: V) = ev.descendants(dg, v)

  def descendantsIntersectsSet(v: V, s: Set[V]) = ev.descendantsIntersectsSet(dg, v, s)

  // TODO: change first Edge type param:
  def shortestPath(source: V, goal: V)(implicit eqV: Eq[V]): Option[List[E]] =
    ev.shortestPath(dg, source, goal)

  def leaves = ev.leaves(dg)

  def outputEdgesOf(v: V) = ev.outputEdgesOf(dg, v)
}

final class UndirectedGraphOps[UG, V, E](val ug: UG)(implicit ev: UndirectedGraph[UG, V, E]) {

  def findVertex(f: V => Boolean) =
    ev.findVertex(ug, f)

  def vertices() = ev.vertices(ug)

  def vertices(e: E) = ev.vertices(ug, e)

  def neighbors(v: V) = ev.neighbors(ug, v)

  def firstLeafOtherThan(r: V)(implicit eqV: Eq[V]) = ev.firstLeafOtherThan(ug, r)
}

final class FunctorOps[F, A, B, G](val as: F)(implicit functor: Functor[F, A, B, G]) {

  def map(f: A => B) = functor.map(as)(f)

}

final class EndofunctorOps[E, A](val e: E)(implicit endo: Endofunctor[E, A]) {

  def map(f: A => A) = endo.map(e)(f)

}

final class AggregatableOps[G, A, B](val ts: G)(implicit agg: Aggregatable[G, A, B]) {

  def aggregate(zeroValue: B)(seqOp: (B, A) => B, combOp: (B, B) => B) =
    agg.aggregate(ts)(zeroValue)(seqOp, combOp)
}

final class TalliableOps[F, T, N](val ts: F)(implicit talliable: Talliable[F, T, N]) {

  def tally = talliable.tally(ts)
}

final class FiniteOps[F, S, A](val as: F)(implicit finite: Finite[F, S]) {

  def size = finite.size(as)
}

final class IndexedOps[F, I, A](val as: F)(implicit index: Indexed[F, I, A]) {

  def at(i: I) = index.at(as)(i)
}

final class MapReducibleOps[M, A, B, K, G](val as: M)(implicit mr: MapReducible[M, A, B, K, G]) {

  def mapReduce(mapper: A => (K, B), zero: B, op: (B, B) => B): G =
    mr.mapReduce(as, mapper, zero, op)
}

final class SetFromOps[F, A](val as: F)(implicit sf: SetFrom[F, A]) {

  def toSet = sf.toSet(as)
}

final class MapFromOps[F, K, V](val fkv: F)(implicit mf: MapFrom[F, K, V]) {

  def toMap = mf.toMap(fkv)
}
