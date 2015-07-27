package axle.syntax

import axle.algebra.Aggregatable
import axle.algebra.DirectedGraph
import axle.algebra.Endofunctor
import axle.algebra.FunctionPair
import axle.algebra.Functor
import axle.algebra.Finite
import axle.algebra.Indexed
import axle.algebra.MapFrom
import axle.algebra.MapReducible
import axle.algebra.LinearAlgebra
import axle.algebra.SetFrom
import axle.algebra.UndirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import scala.reflect.ClassTag
import scala.language.implicitConversions

trait LinearAlgebraSyntax {

  def matrix[M, RowT, ColT, T](m: RowT, n: ColT, f: (RowT, ColT) => T)(implicit la: LinearAlgebra[M, RowT, ColT, T]) =
    la.matrix(m, n, f)

  def cov[M, RowT, ColT, T](m: M)(implicit la: LinearAlgebra[M, RowT, ColT, T]) = la.cov(m)

  def std[M, RowT, ColT, T](m: M)(implicit la: LinearAlgebra[M, RowT, ColT, T]) = la.std(m)

  def zscore[M, RowT, ColT, T](m: M)(implicit la: LinearAlgebra[M, RowT, ColT, T]) = la.zscore(m)

  def pca[M, RowT, ColT, T](m: M, cutoff: Double = 0.95)(implicit la: LinearAlgebra[M, RowT, ColT, T]) = la.pca(m, cutoff)

  def numComponentsForCutoff[M, RowT, ColT, T](m: M, cutoff: Double)(implicit la: LinearAlgebra[M, RowT, ColT, T], fieldT: Field[T]) =
    la.numComponentsForCutoff(m, cutoff)

  implicit def matrixOps[M, RowT, ColT, T](m: M)(implicit la: LinearAlgebra[M, RowT, ColT, T]) = new LinearAlgebraOps(m)
}

trait DirectedGraphSyntax {

  def directedGraph[DG[_, _]: DirectedGraph, VP, EP](vps: Seq[VP], ef: Seq[VP] => Seq[(VP, VP, EP)]) =
    DirectedGraph[DG].make(vps, ef)

  implicit def directedGraphOps[DG[_, _]: DirectedGraph, VP: Eq, EP](dg: DG[VP, EP]) =
    new DirectedGraphOps(dg)
}

trait UndirectedGraphSyntax {

  def undirectedGraph[UG[_, _]: UndirectedGraph, VP, EP](vps: Seq[VP], ef: Seq[VP] => Seq[(VP, VP, EP)]) =
    UndirectedGraph[UG].make(vps, ef)

  implicit def undirectedGraphOps[UG[_, _]: UndirectedGraph, VP: Eq, EP](ug: UG[VP, EP]) =
    new UndirectedGraphOps(ug)
}

trait FunctorSyntax {

  implicit def functorOps[F, A, B, G](fa: F)(implicit functor: Functor[F, A, B, G]) =
    new FunctorOps(fa)
}

trait EndofunctorSyntax {

  implicit def endofunctorOps[E, A](e: E)(implicit ev: Endofunctor[E, A]) =
    new EndofunctorOps(e)
}

trait AggregatableSyntax {

  implicit def aggregatableOps[F, A, B](at: F)(implicit agg: Aggregatable[F, A, B]) =
    new AggregatableOps(at)
}

trait FiniteSyntax {

  implicit def finiteOps[F[_], S, A: ClassTag](fa: F[A])(
    implicit finite: Finite[F[A], S]) =
    new FiniteOps(fa)
}

trait IndexedSyntax {

  implicit def indexedOps[F, IndexT, A](fa: F)(
    implicit index: Indexed[F, IndexT, A]) =
    new IndexedOps(fa)
}

trait MapReducibleSyntax {

  implicit def mapReducibleOps[F, A, B, K](fa: F)(implicit mr: MapReducible[F, A, B, K]) =
    new MapReducibleOps(fa)
}

trait SetFromSyntax {

  implicit def setFromOps[F[_]: SetFrom, A: ClassTag](fa: F[A]) =
    new SetFromOps(fa)
}

trait MapFromSyntax {

  implicit def mapFromOps[F[_]: MapFrom, K: ClassTag, V: ClassTag](fkv: F[(K, V)]) =
    new MapFromOps(fkv)
}