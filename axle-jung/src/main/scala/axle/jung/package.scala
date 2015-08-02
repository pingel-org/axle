package axle

import axle.algebra.DirectedGraph
import axle.algebra.Finite
import axle.algebra.Functor
import axle.algebra.UndirectedGraph
import edu.uci.ics.jung.graph.DirectedSparseGraph
import edu.uci.ics.jung.graph.UndirectedSparseGraph
import scala.reflect.ClassTag
import scala.collection.JavaConverters._

package object jung {

  implicit def finiteDirectedSparseGraph[V, E]: Finite[DirectedSparseGraph[V, E], Int] =
    new Finite[DirectedSparseGraph[V, E], Int] {

      def size(jdsg: DirectedSparseGraph[V, E]): Int =
        jdsg.getVertexCount
    }

  implicit def vertexFunctorDSG[V, E, NV]: Functor[DirectedSparseGraph[V, E], V, NV, DirectedSparseGraph[NV, E]] =
    new Functor[DirectedSparseGraph[V, E], V, NV, DirectedSparseGraph[NV, E]] {
      def map[A, B: ClassTag](jdsg: DirectedSparseGraph[A, E])(f: A => B): DirectedSparseGraph[B, E] = {
        directedGraphJung.make(jdsg.getVertices.asScala.map(f).toList, 42)
      }
    }

  implicit def edgeFunctorDSG[V, E, NE]: Functor[DirectedSparseGraph[V, E], E, NE, DirectedSparseGraph[V, NE]] =
    new Functor[DirectedSparseGraph[V, E], E, NE, DirectedSparseGraph[V, NE]] {
      def map[A, B: ClassTag](jdsg: DirectedSparseGraph[V, A])(f: A => B): DirectedSparseGraph[V, B] = {
        directedGraphJung.make(jdsg.getVertices.asScala.toList, 42)
      }
    }

  implicit def directedGraphJung: DirectedGraph[DirectedSparseGraph] =
    new DirectedGraph[DirectedSparseGraph] {

      import scala.collection.JavaConverters.asScalaBufferConverter
      import scala.collection.JavaConverters.collectionAsScalaIterableConverter

      import axle.algebra.DirectedGraph
      import axle.enrichIndexedSeq
      import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath
      import edu.uci.ics.jung.graph.DirectedSparseGraph
      import spire.algebra.Eq
      import spire.implicits.IntAlgebra
      import spire.implicits.eqOps

      def make[V, E](vertices: Seq[V], ef: Seq[(V, V, E)]): DirectedSparseGraph[V, E] = {

        val jdsg = new DirectedSparseGraph[V, E]

        vertices foreach { jdsg.addVertex } // TODO check return value

        ef foreach {
          case (vi, vj, e) => {
            jdsg.addEdge(e, vi, vj) // TODO check return value
          }
        }

        jdsg
      }

      def vertices[V, E](jdsg: DirectedSparseGraph[V, E]): Iterable[V] =
        jdsg.getVertices.asScala

      def edges[V, E](jdsg: DirectedSparseGraph[V, E]): Iterable[E] =
        jdsg.getEdges.asScala

      def source[V, E](jdsg: DirectedSparseGraph[V, E], e: E): V =
        jdsg.getSource(e)

      def destination[V, E](jdsg: DirectedSparseGraph[V, E], e: E): V =
        jdsg.getDest(e)

      // TODO findVertex needs an index
      def findVertex[V, E](jdsg: DirectedSparseGraph[V, E], f: V => Boolean): Option[V] =
        vertices(jdsg).find(f)

      def filterEdges[V, E](jdsg: DirectedSparseGraph[V, E], f: E => Boolean): DirectedSparseGraph[V, E] =
        make(
          vertices(jdsg).toSeq,
          edges(jdsg).filter(f).toList.map({ case e => (source(jdsg, e), destination(jdsg, e), e) }))

      def areNeighbors[V: Eq, E](jdg: DirectedSparseGraph[V, E], v1: V, v2: V): Boolean =
        edgesTouching(jdg, v1).exists(edge => connects(jdg, edge, v1, v2))

      def isClique[V: Eq, E](jdsg: DirectedSparseGraph[V, E], vs: collection.GenTraversable[V]): Boolean =
        (for {
          vi <- vs
          vj <- vs
        } yield {
          (vi === vj) || areNeighbors(jdsg, vi, vj)
        }).forall(identity)

      def forceClique[V: Eq: Manifest, E](jdsg: DirectedSparseGraph[V, E], among: Set[V], payload: (V, V) => E): DirectedSparseGraph[V, E] = {

        val cliqued = {

          val old2new: Map[V, V] = ??? // TODO _vertices.zip(newVs).toMap

          val newEdges = among.toVector.permutations(2)
            .map({ a => (a(0), a(1)) })
            .collect({
              case (vi: V, vj: V) if !areNeighbors(jdsg, vi, vj) =>
                val newVi = old2new(vi)
                val newVj = old2new(vj)
                (newVi, newVj, payload(newVi, newVj))
            })

          ??? // ef(newVs) ++ newEdges
        }

        make(vertices(jdsg).toList, cliqued)
      }

      def degree[V, E](jdsg: DirectedSparseGraph[V, E], v: V): Int =
        edgesTouching(jdsg, v).size

      def edgesTouching[V, E](jdsg: DirectedSparseGraph[V, E], v: V): Set[E] =
        jdsg.getIncidentEdges(v).asScala.toSet

      def neighbors[V, E](jdsg: DirectedSparseGraph[V, E], v: V): Set[V] =
        jdsg.getNeighbors(v).asScala.toSet

      // a "leaf" is vertex with only one neighbor
      def firstLeafOtherThan[V: Eq, E](jdsg: DirectedSparseGraph[V, E], r: V): Option[V] =
        vertices(jdsg).find(v => neighbors(jdsg, v).size === 1 && (!(v === r)))

      /**
       * "decompositions" page 3 (Definition 3, Section 9.3)
       * turn the neighbors of v into a clique
       */

      def eliminate[V, E](jdsg: DirectedSparseGraph[V, E], v: V, payload: (V, V) => E): DirectedSparseGraph[V, E] = {

        // TODO
        //    val vs = neighbors(v)
        //    makeFunctional.jungGraph.removeVertex(v)
        //    forceClique(vs, payload)
        ???
      }

      def other[V: Eq, E](jdsg: DirectedSparseGraph[V, E], edge: E, u: V): V =
        u match {
          case _ if (u === source(jdsg, edge))      => destination(jdsg, edge)
          case _ if (u === destination(jdsg, edge)) => source(jdsg, edge)
          case _                                    => throw new Exception("can't find 'other' of a vertex that isn't on the edge itself")
        }

      def connects[V: Eq, E](jdsg: DirectedSparseGraph[V, E], edge: E, a1: V, a2: V): Boolean =
        (a1 === source(jdsg, edge) && a2 === destination(jdsg, edge)) || (a2 === source(jdsg, edge) && a1 === destination(jdsg, edge))

      def leaves[V: Eq, E](jdsg: DirectedSparseGraph[V, E]): Set[V] =
        vertices(jdsg).filter(v => isLeaf(jdsg, v)).toSet

      def precedes[V, E](jdsg: DirectedSparseGraph[V, E], v1: V, v2: V): Boolean =
        predecessors(jdsg, v2).contains(v1)

      def predecessors[V, E](jdsg: DirectedSparseGraph[V, E], v: V): Set[V] =
        jdsg.getPredecessors(v).asScala.toSet

      def isLeaf[V, E](jdsg: DirectedSparseGraph[V, E], v: V): Boolean =
        jdsg.getSuccessorCount(v) === 0

      def successors[V, E](jdsg: DirectedSparseGraph[V, E], v: V): Set[V] =
        jdsg.getSuccessors(v).asScala.toSet

      def outputEdgesOf[V, E](jdsg: DirectedSparseGraph[V, E], v: V): Set[E] =
        jdsg.getOutEdges(v).asScala.toSet

      def descendantsIntersectsSet[V, E](jdsg: DirectedSparseGraph[V, E], v: V, s: Set[V]): Boolean =
        s.contains(v) || s.exists(x => descendantsIntersectsSet(jdsg, x, s))

      def removeInputs[V, E](jdsg: DirectedSparseGraph[V, E], to: Set[V]): DirectedSparseGraph[V, E] =
        filterEdges(jdsg, edge => !to.contains(destination(jdsg, edge)))

      def removeOutputs[V, E](jdsg: DirectedSparseGraph[V, E], from: Set[V]): DirectedSparseGraph[V, E] =
        filterEdges(jdsg, edge => !from.contains(source(jdsg, edge)))

      def moralGraph[V, E](jdsg: DirectedSparseGraph[V, E]): Boolean =
        ???

      def isAcyclic[V, E](jdsg: DirectedSparseGraph[V, E]): Boolean =
        ???

      def shortestPath[V: Eq, E](jdsg: DirectedSparseGraph[V, E], source: V, goal: V): Option[List[E]] =
        if (source === goal) {
          Some(Nil)
        } else {
          Option((new DijkstraShortestPath(jdsg)).getPath(source, goal)) flatMap { path =>
            if (path.size === 0)
              None
            else
              Some(path.asScala.toList)
          }
        }

    }

  implicit def finiteUndirectedSparseGraph[V, E]: Finite[UndirectedSparseGraph[V, E], Int] =
    new Finite[UndirectedSparseGraph[V, E], Int] {

      def size(jusg: UndirectedSparseGraph[V, E]): Int =
        jusg.getVertexCount
    }

  implicit def vertexFunctorUDSG[V, E, NV]: Functor[UndirectedSparseGraph[V, E], V, NV, UndirectedSparseGraph[NV, E]] =
    new Functor[UndirectedSparseGraph[V, E], V, NV, UndirectedSparseGraph[NV, E]] {
      def map[A, B: ClassTag](jusg: UndirectedSparseGraph[A, E])(f: A => B): UndirectedSparseGraph[B, E] = {
        undirectedGraphJung.make(jusg.getVertices.asScala.map(f).toList, 42)
      }
    }

  implicit def edgeFunctorUDSG[V, E, NE]: Functor[UndirectedSparseGraph[V, E], E, NE, UndirectedSparseGraph[V, NE]] =
    new Functor[UndirectedSparseGraph[V, E], E, NE, UndirectedSparseGraph[V, NE]] {
      def map[A, B: ClassTag](jusg: UndirectedSparseGraph[V, A])(f: A => B): UndirectedSparseGraph[V, B] = {
        undirectedGraphJung.make(jusg.getVertices.asScala.toList, 42)
      }
    }

  implicit def undirectedGraphJung: UndirectedGraph[UndirectedSparseGraph] =
    new UndirectedGraph[UndirectedSparseGraph] {

      import scala.collection.JavaConverters.collectionAsScalaIterableConverter

      import axle.algebra.UndirectedGraph
      import axle.enrichIndexedSeq
      import edu.uci.ics.jung.graph.UndirectedSparseGraph
      import spire.algebra.Eq
      import spire.implicits.IntAlgebra
      import spire.implicits.eqOps

      def make[V, E](vertices: Seq[V], ef: Seq[(V, V, E)]): UndirectedSparseGraph[V, E] = {

        val jusg = new UndirectedSparseGraph[V, E]

        vertices foreach { jusg.addVertex } // TODO check return value

        ef foreach {
          case (vi, vj, e) =>
            jusg.addEdge(e, vi, vj) // TODO check return value
        }

        jusg
      }

      def vertices[V, E](jusg: UndirectedSparseGraph[V, E]): Iterable[V] =
        jusg.getVertices.asScala

      def edges[V, E](jusg: UndirectedSparseGraph[V, E]): Iterable[E] =
        jusg.getEdges.asScala

      def vertices[V, E](jusg: UndirectedSparseGraph[V, E], e: E): (V, V) = {
        val pair = jusg.getEndpoints(e)
        (pair.getFirst, pair.getSecond)
      }

      // TODO findVertex needs an index
      def findVertex[V, E](jusg: UndirectedSparseGraph[V, E], f: V => Boolean): Option[V] =
        vertices(jusg).find(f)

      def filterEdges[V, E](jusg: UndirectedSparseGraph[V, E], f: E => Boolean): UndirectedSparseGraph[V, E] =
        make(
          vertices(jusg).toSeq,
          edges(jusg).filter(f).toList.map({
            case e => {
              val pair = vertices(jusg, e)
              (pair._1, pair._2, e)
            }
          }))

      //  def unlink(e: Edge[ES, E]): UndirectedSparseGraph[V, E] =
      //    filterEdges(t => {
      //      val v1 = e.storage._1
      //      val v2 = e.storage._2
      //      !((v1, v2, e.payload) === t || (v2, v1, e.payload) === t)
      //    })

      //  // UndirectedSparseGraph[V, E]
      //  def unlink(v1: Vertex[V], v2: Vertex[V]) =
      //    filterEdges(e => (e._1 === v1 && e._2 === v2) || (e._2 === v1 && e._1 === v2))

      def areNeighbors[V: Eq, E](jug: UndirectedSparseGraph[V, E], v1: V, v2: V): Boolean =
        edgesTouching(jug, v1).exists(edge => connects(jug, edge, v1, v2))

      def isClique[V: Eq, E](jug: UndirectedSparseGraph[V, E], vs: collection.GenTraversable[V]): Boolean =
        (for {
          vi <- vs
          vj <- vs
        } yield {
          (vi === vj) || areNeighbors(jug, vi, vj)
        }).forall(identity)

      def forceClique[V: Eq: Manifest, E](jug: UndirectedSparseGraph[V, E], among: Set[V], payload: (V, V) => E): UndirectedSparseGraph[V, E] = {

        val cliqued = {

          val old2new: Map[V, V] = ??? // TODO _vertices.zip(newVs).toMap

          val newEdges = among.toVector.permutations(2)
            .map({ a => (a(0), a(1)) })
            .collect({
              case (vi: V, vj: V) if !areNeighbors(jug, vi, vj) =>
                val newVi = old2new(vi)
                val newVj = old2new(vj)
                (newVi, newVj, payload(newVi, newVj))
            })

          ??? // ef(newVs) ++ newEdges
        }

        make(vertices(jug).toList, cliqued)
      }

      def degree[V, E](jusg: UndirectedSparseGraph[V, E], v: V): Int =
        edgesTouching(jusg, v).size

      def edgesTouching[V, E](jusg: UndirectedSparseGraph[V, E], v: V): Set[E] =
        jusg.getIncidentEdges(v).asScala.toSet

      def neighbors[V, E](jusg: UndirectedSparseGraph[V, E], v: V): Set[V] =
        jusg.getNeighbors(v).asScala.toSet

      //  def delete(v: Vertex[V]): UndirectedSparseGraph[V, E] =
      //    UndirectedSparseGraph(vertices.toSeq.filter(_ != v).map(_.payload), ef)

      // a "leaf" is vertex with only one neighbor
      def firstLeafOtherThan[V: Eq, E](jug: UndirectedSparseGraph[V, E], r: V): Option[V] =
        vertices(jug).find(v => neighbors(jug, v).size === 1 && (!(v === r)))

      /**
       * "decompositions" page 3 (Definition 3, Section 9.3)
       * turn the neighbors of v into a clique
       */

      def eliminate[V, E](jug: UndirectedSparseGraph[V, E], v: V, payload: (V, V) => E): UndirectedSparseGraph[V, E] = {

        // TODO
        //    val vs = neighbors(v)
        //    makeFunctional.jungGraph.removeVertex(v)
        //    forceClique(vs, payload)
        ???
      }

      def other[V: Eq, E](jusg: UndirectedSparseGraph[V, E], edge: E, u: V): V = {
        val (v1, v2) = vertices(jusg, edge)
        u match {
          case _ if (u === v1) => v2
          case _ if (u === v2) => v1
          case _               => throw new Exception("can't find 'other' of a vertex that isn't on the edge itself")
        }
      }

      def connects[V: Eq, E](jusg: UndirectedSparseGraph[V, E], edge: E, a1: V, a2: V): Boolean = {
        val (v1, v2) = vertices(jusg, edge)
        (v1 === a1 && v2 === a2) || (v2 === a1 && v1 === a2)
      }

    }

}