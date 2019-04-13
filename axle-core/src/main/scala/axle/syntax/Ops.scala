package axle.syntax

import cats.kernel.Eq
import cats.kernel.Order

import spire.algebra.AdditiveMonoid
import spire.algebra.Field
import spire.algebra.Ring
import spire.random.Generator
import spire.random.Dist

import axle.algebra.Aggregatable
import axle.algebra.DirectedGraph
import axle.algebra.Endofunctor
import axle.algebra.Finite
import axle.algebra.Indexed
import axle.algebra.MapFrom
import axle.algebra.MapReducible
import axle.algebra.LinearAlgebra
import axle.algebra.SetFrom
import axle.algebra.Talliable
import axle.algebra.UndirectedGraph
import axle.stats.ProbabilityModel

final class LinearAlgebraOps[M, RowT, ColT, T](val lhs: M)(
  implicit
  la: LinearAlgebra[M, RowT, ColT, T]) {

  def get(i: RowT, j: ColT) = la.get(lhs)(i, j)

  def slice(rs: Seq[RowT], cs: Seq[ColT]) = la.slice(lhs)(rs, cs)

  def toList = la.toList(lhs)

  def row(i: RowT) = la.row(lhs)(i)

  def column(j: ColT) = la.column(lhs)(j)

  def length = la.length(lhs)

  def rows = la.rows(lhs)

  def columns = la.columns(lhs)

  def negate = la.negate(lhs)

  def isEmpty: Boolean = la.isEmpty(lhs)

  def isRowVector: Boolean = la.isRowVector(lhs)

  def isColumnVector: Boolean = la.isColumnVector(lhs)

  def isVector: Boolean = la.isVector(lhs)

  def isSquare: Boolean = la.isSquare(lhs)

  def isScalar: Boolean = la.isScalar(lhs)

  def dup = la.dup(lhs)

  def transpose = la.transpose(lhs)
  def diag = la.diag(lhs)
  def invert = la.invert(lhs)
  def ceil = la.ceil(lhs)
  def floor = la.floor(lhs)
  def log = la.log(lhs)
  def log10 = la.log10(lhs)

  //def fullSVD[T](m: M[A]) // (U, S, V) such that A = U * diag(S) * V' // TODO: all Matrix[Double] ?

  def pow(p: Double) = la.pow(lhs)(p)

  def addScalar(x: T) = la.addScalar(lhs)(x)
  def subtractScalar(x: T) = la.subtractScalar(lhs)(x)

  def minus(rhs: M): M = la.minus(lhs, rhs)
  def -(rhs: M): M = la.minus(lhs, rhs)

  def multiplyScalar(x: T): M = la.multiplyScalar(lhs)(x)
  def :*(x: T): M = la.multiplyScalar(lhs)(x)
  def divideScalar(x: T): M = la.divideScalar(lhs)(x)

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

  // comparisons

  def lt(other: M): M = la.lt(lhs)(other)
  def le(other: M): M = la.le(lhs)(other)
  def gt(other: M): M = la.gt(lhs)(other)
  def ge(other: M): M = la.ge(lhs)(other)
  def eq(other: M): M = la.eq(lhs)(other)
  def ne(other: M): M = la.ne(lhs)(other)

  // boolean

  def and(rhs: M) = la.and(lhs)(rhs)
  def or(rhs: M) = la.or(lhs)(rhs)
  def xor(rhs: M) = la.xor(lhs)(rhs)
  def not = la.not(lhs)

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

  def centerRows = la.centerRows(lhs)
  def centerColumns = la.centerColumns(lhs)

  def rowRange = la.rowRange(lhs)
  def columnRange = la.columnRange(lhs)

  def rowMins = la.rowMins(lhs)
  def rowMaxs = la.rowMaxs(lhs)
  def rowMeans = la.rowMeans(lhs)
  def sortRows = la.sortRows(lhs)

  // higher order methods

  def flatMap(f: T => M) = la.flatMap(lhs)(f)

  def foldLeft(zero: M)(f: (M, M) => M) = la.foldLeft(lhs)(zero)(f)

  def foldTop(zero: M)(f: (M, M) => M) = la.foldTop(lhs)(zero)(f)

  def sumsq = la.sumsq(lhs)

  // Aliases

  def t = la.transpose(lhs)
  def tr = la.transpose(lhs)
  def inv = la.invert(lhs)

  def scalar(implicit rz: AdditiveMonoid[RowT], cz: AdditiveMonoid[ColT]): T = {
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

final class ProbabilityModelOps[M[_, _], A, V](val model: M[A, V])(
  implicit
  ev: ProbabilityModel[M]) {

  def values: IndexedSeq[A] = ev.values(model)
  
  def P(a: A)(implicit fieldV: Field[V]): V = ev.probabilityOf(model, a)
  
  def P(predicate: A => Boolean)(implicit fieldV: Field[V]): V = ev.probabilityOfExpression(model, predicate)
  
  def |[B](predicate: A => Boolean, screen: A => B)(implicit fieldV: Field[V]): M[B, V] = ev.conditionExpression(model, predicate, screen)
  
  def observe(gen: Generator)(implicit spireDist: Dist[V], ringV: Ring[V], orderV: Order[V]): A = ev.observe(model, gen)

}
final class DirectedGraphOps[DG, V, E](val dg: DG)(
  implicit
  ev: DirectedGraph[DG, V, E]) {

  def findVertex(f: V => Boolean): Option[V] =
    ev.findVertex(dg, f)

  def vertices = ev.vertices(dg)

  def edges = ev.edges(dg)

  def filterEdges(f: E => Boolean): DG = ev.filterEdges(dg, f)

  def areNeighbors(v1: V, v2: V)(implicit eqV: Eq[V]): Boolean = ev.areNeighbors(dg, v1, v2)

  def isClique(vs: Iterable[V])(implicit eqV: Eq[V]): Boolean = ev.isClique(dg, vs)

  def forceClique(vs: Set[V], edgeFn: (V, V) => E)(implicit eqV: Eq[V], manV: Manifest[V]): DG =
    ev.forceClique(dg, vs, edgeFn)

  def edgesTouching(v: V) = ev.edgesTouching(dg, v)

  def other(e: E, v: V)(implicit eqV: Eq[V]): V = ev.other(dg, e, v)

  def source(e: E) = ev.source(dg, e)

  def destination(e: E) = ev.destination(dg, e)

  def connects(e: E, v1: V, v2: V)(implicit eqV: Eq[V]) = ev.connects(dg, e, v1, v2)

  def precedes(v1: V, v2: V) = ev.precedes(dg, v1, v2)

  def neighbors(v: V) = ev.neighbors(dg, v)

  def predecessors(v: V) = ev.predecessors(dg, v)

  def successors(v: V) = ev.successors(dg, v)

  def descendants(v: V) = ev.descendants(dg, v)

  def descendantsIntersectsSet(v: V, s: Set[V]) = ev.descendantsIntersectsSet(dg, v, s)

  def removeInputs(vs: Set[V]): DG = ev.removeInputs(dg, vs)

  def removeOutputs(vs: Set[V]): DG = ev.removeOutputs(dg, vs)

  // TODO: change first Edge type param:
  def shortestPath(source: V, goal: V)(implicit eqV: Eq[V]): Option[List[E]] =
    ev.shortestPath(dg, source, goal)

  def leaves = ev.leaves(dg)

  def outputEdgesOf(v: V) = ev.outputEdgesOf(dg, v)
}

final class UndirectedGraphOps[UG, V, E](val ug: UG)(
  implicit
  ev: UndirectedGraph[UG, V, E]) {

  def findVertex(f: V => Boolean) =
    ev.findVertex(ug, f)

  def vertices = ev.vertices(ug)

  def edges = ev.edges(ug)

  def vertices(e: E) = ev.vertices(ug, e)

  def neighbors(v: V) = ev.neighbors(ug, v)

  def areNeighbors(v1: V, v2: V)(implicit eqV: Eq[V]): Boolean = ev.areNeighbors(ug, v1, v2)

  def edgesTouching(v: V): Iterable[E] = ev.edgesTouching(ug, v)

  def other(e: E, v: V)(implicit eqV: Eq[V]): V = ev.other(ug, e, v)

  def connects(e: E, v1: V, v2: V)(implicit eqV: Eq[V]): Boolean = ev.connects(ug, e, v1, v2)

  def isClique(vs: Iterable[V])(implicit eqV: Eq[V]): Boolean = ev.isClique(ug, vs)

  def forceClique(vs: Set[V], edgeFn: (V, V) => E)(implicit eqV: Eq[V], manV: Manifest[V]): UG =
    ev.forceClique(ug, vs, edgeFn)

  def filterEdges(f: E => Boolean): UG = ev.filterEdges(ug, f)

  def firstLeafOtherThan(r: V)(implicit eqV: Eq[V]): Option[V] = ev.firstLeafOtherThan(ug, r)
}

final class EndofunctorOps[E, A](val e: E)(
  implicit
  endo: Endofunctor[E, A]) {

  def map(f: A => A) = endo.map(e)(f)

}

final class AggregatableOps[G[_], A](val ts: G[A])(
  implicit
  agg: Aggregatable[G]) {

  def aggregate[B](zeroValue: B)(seqOp: (B, A) => B, combOp: (B, B) => B) =
    agg.aggregate(ts)(zeroValue)(seqOp, combOp)
}

final class TalliableOps[F[_], A, N](val as: F[A])(
  implicit
  talliable: Talliable[F],
  ring:      Ring[N]) {

  def tally = talliable.tally(as)
}

final class FiniteOps[F[_], A, N](val as: F[A])(
  implicit
  finite: Finite[F, N]) {

  def size = finite.size(as)
}

final class IndexedOps[F[_], I, A](val as: F[A])(
  implicit
  index: Indexed[F, I]) {

  def at(i: I) = index.at(as)(i)

  def take(i: I) = index.take(as)(i)

  def drop(i: I) = index.drop(as)(i)
}

final class MapReducibleOps[M[_], A](val as: M[A])(
  implicit
  mr: MapReducible[M]) {

  def mapReduce[B, K](mapper: A => (K, B), zero: B, op: (B, B) => B): M[(K, B)] =
    mr.mapReduce(as, mapper, zero, op)
}

final class SetFromOps[F, A](val as: F)(
  implicit
  sf: SetFrom[F, A]) {

  def toSet = sf.toSet(as)
}

final class MapFromOps[F, K, V](val fkv: F)(
  implicit
  mf: MapFrom[F, K, V]) {

  def toMap = mf.toMap(fkv)
}
