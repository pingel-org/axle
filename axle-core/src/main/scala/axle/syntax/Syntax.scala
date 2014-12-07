package axle.syntax

import axle.algebra.Aggregatable
import axle.algebra.DirectedGraph
import axle.algebra.Functor
import axle.algebra.Finite
import axle.algebra.Indexed
import axle.algebra.MapFrom
import axle.algebra.MapReducible
import axle.algebra.Matrix
import axle.algebra.SetFrom
import axle.algebra.UndirectedGraph
import spire.algebra.Eq
import scala.reflect.ClassTag

trait MatrixSyntax {

  implicit def matrixOps[M[_]: Matrix, A](ma: M[A]) = new MatrixOps(ma)
}

trait DirectedGraphSyntax {

  implicit def directedGraphOps[DG[_, _]: DirectedGraph, VP: Eq, EP](dg: DG[VP, EP]) =
    new DirectedGraphOps(dg)
}

trait UndirectedGraphSyntax {

  implicit def undirectedGraphOps[UG[_, _]: UndirectedGraph, VP: Eq, EP](ug: UG[VP, EP]) =
    new UndirectedGraphOps(ug)
}

trait FunctorSyntax {

  implicit def functorOps[F[_]: Functor, A](fa: F[A]) =
    new FunctorOps(fa)
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