package axle.syntax

import scala.language.implicitConversions

import spire.algebra.Field

import axle.algebra.Aggregatable
import axle.algebra.Talliable
import axle.algebra.DirectedGraph
import axle.algebra.Endofunctor
import axle.algebra.Finite
import axle.algebra.Indexed
import axle.algebra.MapFrom
import axle.algebra.MapReducible
import axle.algebra.LinearAlgebra
import axle.algebra.SetFrom
import axle.algebra.UndirectedGraph
import axle.stats.ProbabilityModel

trait LinearAlgebraSyntax {

  def matrix[M, RowT, ColT, T](m: RowT, n: ColT, f: (RowT, ColT) => T)(
    implicit
    la: LinearAlgebra[M, RowT, ColT, T]) =
    la.matrix(m, n, f)

  def cov[M, RowT, ColT, T](m: M)(
    implicit
    la: LinearAlgebra[M, RowT, ColT, T]) = la.cov(m)

  def std[M, RowT, ColT, T](m: M)(
    implicit
    la: LinearAlgebra[M, RowT, ColT, T]) = la.std(m)

  def zscore[M, RowT, ColT, T](m: M)(
    implicit
    la: LinearAlgebra[M, RowT, ColT, T]) = la.zscore(m)

  def pca[M, RowT, ColT, T](m: M)(
    implicit
    la: LinearAlgebra[M, RowT, ColT, T]) = la.pca(m)

  def numComponentsForCutoff[M, RowT, ColT, T](m: M, cutoff: Double)(
    implicit
    la: LinearAlgebra[M, RowT, ColT, T], fieldT: Field[T]) =
    la.numComponentsForCutoff(m, cutoff)

  implicit def matrixOps[M, RowT, ColT, T](m: M)(
    implicit
    la: LinearAlgebra[M, RowT, ColT, T]) =
    new LinearAlgebraOps(m)
}

trait ProbabilityModelSyntax {

  implicit def probabilityModelOps[M[_, _], A, V](model: M[A, V])(
    implicit
    ev: ProbabilityModel[M]) =
    new ProbabilityModelOps(model)
}

trait DirectedGraphSyntax {

  def directedGraph[DG, V, E](vps: Seq[V], ef: Seq[(V, V, E)])(
    implicit
    ev: DirectedGraph[DG, V, E]) =
    ev.make(vps, ef)

  implicit def directedGraphOps[DG, V, E](dg: DG)(
    implicit
    ev: DirectedGraph[DG, V, E]) =
    new DirectedGraphOps(dg)
}

trait UndirectedGraphSyntax {

  def undirectedGraph[UG, V, E](vps: Seq[V], ef: Seq[(V, V, E)])(
    implicit
    ev: UndirectedGraph[UG, V, E]) =
    ev.make(vps, ef)

  implicit def undirectedGraphOps[UG, V, E](ug: UG)(
    implicit
    ev: UndirectedGraph[UG, V, E]) =
    new UndirectedGraphOps(ug)
}

trait EndofunctorSyntax {

  implicit def endofunctorOps[E, A](e: E)(
    implicit
    ev: Endofunctor[E, A]) =
    new EndofunctorOps(e)
}

trait AggregatableSyntax {

  implicit def aggregatableOps[F[_], A](at: F[A])(
    implicit
    agg: Aggregatable[F]) =
    new AggregatableOps(at)
}

trait TalliableSyntax {

  import spire.algebra.Ring

  implicit def talliableOps[F[_], A, N](at: F[A])(
    implicit
    tal: Talliable[F], ring: Ring[N]) =
    new TalliableOps(at)
}

trait FiniteSyntax {

  implicit def finiteOps[F[_], A, N](fa: F[A])(
    implicit
    finite: Finite[F, N]) =
    new FiniteOps(fa)
}

trait IndexedSyntax {

  implicit def indexedOps[F[_], IndexT, A](fa: F[A])(
    implicit
    index: Indexed[F, IndexT]) =
    new IndexedOps(fa)
}

trait MapReducibleSyntax {

  implicit def mapReducibleOps[F[_], A](fa: F[A])(
    implicit
    mr: MapReducible[F]) =
    new MapReducibleOps(fa)
}

trait SetFromSyntax {

  implicit def setFromOps[F, A](fa: F)(
    implicit
    sf: SetFrom[F, A]) =
    new SetFromOps(fa)
}

trait MapFromSyntax {

  implicit def mapFromOps[F, K, V](fkv: F)(
    implicit
    mf: MapFrom[F, K, V]) =
    new MapFromOps(fkv)
}
