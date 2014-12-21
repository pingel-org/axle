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
import axle.algebra.Vertex
import spire.algebra.Eq
import scala.reflect.ClassTag

trait LinearAlgebraSyntax {

  def matrix[M, T](m: Int, n: Int, f: (Int, Int) => T)(implicit la: LinearAlgebra[M, T]) =
    la.matrix(m, n, f)

  def cov[M, T](m: M)(implicit la: LinearAlgebra[M, T]) = la.cov(m)

  def std[M, T](m: M)(implicit la: LinearAlgebra[M, T]) = la.std(m)

  def zscore[M, T](m: M)(implicit la: LinearAlgebra[M, T]) = la.zscore(m)

  def pca[M, T](m: M, cutoff: Double = 0.95)(implicit la: LinearAlgebra[M, T]) = la.pca(m, cutoff)

  def numComponentsForCutoff[M, T](m: M, cutoff: Double)(implicit la: LinearAlgebra[M, T]) = la.numComponentsForCutoff(m, cutoff)

  implicit def matrixOps[M, T](m: M)(implicit la: LinearAlgebra[M, T]) = new LinearAlgebraOps(m)
}

trait DirectedGraphSyntax {

  def directedGraph[DG[_, _]: DirectedGraph, VP, EP](vps: Seq[VP], ef: Seq[Vertex[VP]] => Seq[(Vertex[VP], Vertex[VP], EP)]) =
    implicitly[DirectedGraph[DG]].make(vps, ef)

  implicit def directedGraphOps[DG[_, _]: DirectedGraph, VP: Eq, EP](dg: DG[VP, EP]) =
    new DirectedGraphOps(dg)
}

trait UndirectedGraphSyntax {

  def undirectedGraph[UG[_, _]: UndirectedGraph, VP, EP](vps: Seq[VP], ef: Seq[Vertex[VP]] => Seq[(Vertex[VP], Vertex[VP], EP)]) =
    implicitly[UndirectedGraph[UG]].make(vps, ef)

  implicit def undirectedGraphOps[UG[_, _]: UndirectedGraph, VP: Eq, EP](ug: UG[VP, EP]) =
    new UndirectedGraphOps(ug)
}

trait FunctorSyntax {

  implicit def functorOps[F[_]: Functor, A](fa: F[A]) =
    new FunctorOps(fa)
}

trait EndofunctorSyntax {

  implicit def endofunctorOps[E, A](e: E)(implicit ev: Endofunctor[E, A]) =
    new EndofunctorOps(e)
}

trait AggregatableSyntax {

  implicit def aggregatableOps[A[_]: Aggregatable, T](at: A[T]) =
    new AggregatableOps(at)
}

trait FiniteSyntax {

  implicit def finiteOps[F[_]: Finite, A: ClassTag](fa: F[A]) =
    new FiniteOps(fa)
}

trait IndexedSyntax {

  implicit def indexedOps[F[_]: Indexed, A: ClassTag](fa: F[A]) =
    new IndexedOps(fa)
}

trait MapReducibleSyntax {

  implicit def mapReducibleOps[F[_]: MapReducible, A: ClassTag](fa: F[A]) =
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