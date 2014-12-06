package axle.syntax

import axle.algebra.Matrix
import axle.algebra.DirectedGraph
import axle.algebra.UndirectedGraph
import spire.algebra.Eq

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
