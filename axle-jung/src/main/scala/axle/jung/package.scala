package axle

import axle.algebra.DirectedGraph
import axle.algebra.Finite
import axle.algebra.Functor
import axle.algebra.UndirectedGraph
import edu.uci.ics.jung.graph.DirectedSparseGraph
import edu.uci.ics.jung.graph.UndirectedSparseGraph
import scala.collection.JavaConverters._
import cats.implicits._

package object jung {

  implicit def finiteDirectedSparseGraph[V, E]: Finite[DirectedSparseGraph[V, E], Int] =
    new Finite[DirectedSparseGraph[V, E], Int] {

      def size(jdsg: DirectedSparseGraph[V, E]): Int =
        jdsg.getVertexCount
    }

  implicit def vertexFunctorDSG[V, E, NV]: Functor[DirectedSparseGraph[V, E], V, NV, DirectedSparseGraph[NV, E]] =
    new Functor[DirectedSparseGraph[V, E], V, NV, DirectedSparseGraph[NV, E]] {

      def map(jdsg: DirectedSparseGraph[V, E])(f: V => NV): DirectedSparseGraph[NV, E] = {

        val vertexOld2New: Map[V, NV] =
          jdsg.getVertices.asScala.map({ v => v -> f(v) }).toMap

        val newEdges = jdsg.getEdges.asScala.toSeq.map(e => {
          val ends = jdsg.getEndpoints(e)
          val newV1 = vertexOld2New(ends.getFirst)
          val newV2 = vertexOld2New(ends.getSecond)
          (newV1, newV2, e)
        })

        directedGraphJung.make(
          vertexOld2New.values.toSeq,
          newEdges)
      }
    }

  implicit def edgeFunctorDSG[V, E, NE]: Functor[DirectedSparseGraph[V, E], E, NE, DirectedSparseGraph[V, NE]] =
    new Functor[DirectedSparseGraph[V, E], E, NE, DirectedSparseGraph[V, NE]] {

      def map(jdsg: DirectedSparseGraph[V, E])(f: E => NE): DirectedSparseGraph[V, NE] = {

        val newEdges = jdsg.getEdges.asScala.toSeq.map(e => {
          val ends = jdsg.getEndpoints(e)
          (ends.getFirst, ends.getSecond, f(e))
        })

        directedGraphJung.make(
          jdsg.getVertices.asScala.toSeq,
          newEdges)
      }
    }

  implicit def directedGraphJung[V, E]: DirectedGraph[DirectedSparseGraph[V, E], V, E] =
    new DirectedGraph[DirectedSparseGraph[V, E], V, E] {

      import scala.collection.JavaConverters.asScalaBufferConverter
      import scala.collection.JavaConverters.collectionAsScalaIterableConverter

      import axle.enrichIndexedSeq
      import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath
      import edu.uci.ics.jung.graph.DirectedSparseGraph
      import cats.kernel.Eq

      def make(vertices: Seq[V], ef: Seq[(V, V, E)]): DirectedSparseGraph[V, E] = {

        val jdsg = new DirectedSparseGraph[V, E]

        vertices foreach { jdsg.addVertex } // TODO check return value

        ef foreach {
          case (vi, vj, e) => {
            jdsg.addEdge(e, vi, vj) // TODO check return value
          }
        }

        jdsg
      }

      def vertices(jdsg: DirectedSparseGraph[V, E]): Iterable[V] =
        jdsg.getVertices.asScala

      def edges(jdsg: DirectedSparseGraph[V, E]): Iterable[E] =
        jdsg.getEdges.asScala

      def source(jdsg: DirectedSparseGraph[V, E], e: E): V =
        jdsg.getSource(e)

      def destination(jdsg: DirectedSparseGraph[V, E], e: E): V =
        jdsg.getDest(e)

      // TODO findVertex needs an index
      def findVertex(jdsg: DirectedSparseGraph[V, E], f: V => Boolean): Option[V] =
        vertices(jdsg).find(f)

      def filterEdges(jdsg: DirectedSparseGraph[V, E], f: E => Boolean): DirectedSparseGraph[V, E] =
        make(
          vertices(jdsg).toSeq,
          edges(jdsg).filter(f).toList.map({ case e => (source(jdsg, e), destination(jdsg, e), e) }))

      def areNeighbors(jdg: DirectedSparseGraph[V, E], v1: V, v2: V)(implicit eqV: Eq[V]): Boolean =
        edgesTouching(jdg, v1).exists(edge => connects(jdg, edge, v1, v2))

      def isClique(jdsg: DirectedSparseGraph[V, E], vs: Iterable[V])(implicit eqV: Eq[V]): Boolean =
        (for {
          vi <- vs
          vj <- vs
        } yield {
          (vi === vj) || areNeighbors(jdsg, vi, vj)
        }).forall(identity)

      def forceClique(
        jdsg: DirectedSparseGraph[V, E],
        among: Set[V],
        edgeFn: (V, V) => E)(implicit eqV: Eq[V], mV: Manifest[V]): DirectedSparseGraph[V, E] = {

        val newEdges: Iterable[(V, V, E)] = among.toVector.permutations(2)
          .map({ a =>
            val from = a(0)
            val to = a(1)
            val edge = Option(jdsg.findEdge(from, to)).getOrElse(edgeFn(from, to))
            (from, to, edge)
          })

        make(vertices(jdsg).toList, newEdges.toList)
      }

      def degree(jdsg: DirectedSparseGraph[V, E], v: V): Int =
        edgesTouching(jdsg, v).size

      def edgesTouching(jdsg: DirectedSparseGraph[V, E], v: V): Set[E] =
        jdsg.getIncidentEdges(v).asScala.toSet

      def neighbors(jdsg: DirectedSparseGraph[V, E], v: V): Set[V] =
        jdsg.getNeighbors(v).asScala.toSet

      // a "leaf" is vertex with only one neighbor
      def firstLeafOtherThan(jdsg: DirectedSparseGraph[V, E], r: V)(implicit eqV: Eq[V]): Option[V] =
        vertices(jdsg).find(v => neighbors(jdsg, v).size === 1 && (!(v === r)))

      /**
       * "decompositions" page 3 (Definition 3, Section 9.3)
       * turn the neighbors of v into a clique
       */

      def eliminate(jdsg: DirectedSparseGraph[V, E], v: V, payload: (V, V) => E): DirectedSparseGraph[V, E] = {

        // TODO
        //    val vs = neighbors(v)
        //    makeFunctional.jungGraph.removeVertex(v)
        //    forceClique(vs, payload)
        ???
      }

      def other(jdsg: DirectedSparseGraph[V, E], edge: E, u: V)(implicit eqV: Eq[V]): V =
        u match {
          case _ if (u === source(jdsg, edge))      => destination(jdsg, edge)
          case _ if (u === destination(jdsg, edge)) => source(jdsg, edge)
          case _                                    => throw new Exception("can't find 'other' of a vertex that isn't on the edge itself")
        }

      def connects(jdsg: DirectedSparseGraph[V, E], edge: E, a1: V, a2: V)(implicit eqV: Eq[V]): Boolean =
        (a1 === source(jdsg, edge) && a2 === destination(jdsg, edge)) || (a2 === source(jdsg, edge) && a1 === destination(jdsg, edge))

      def leaves(jdsg: DirectedSparseGraph[V, E]): Set[V] =
        vertices(jdsg).filter(v => isLeaf(jdsg, v)).toSet

      def precedes(jdsg: DirectedSparseGraph[V, E], v1: V, v2: V): Boolean =
        predecessors(jdsg, v2).contains(v1)

      def predecessors(jdsg: DirectedSparseGraph[V, E], v: V): Set[V] =
        jdsg.getPredecessors(v).asScala.toSet

      def isLeaf(jdsg: DirectedSparseGraph[V, E], v: V): Boolean =
        jdsg.getSuccessorCount(v) === 0

      def successors(jdsg: DirectedSparseGraph[V, E], v: V): Set[V] =
        jdsg.getSuccessors(v).asScala.toSet

      def outputEdgesOf(jdsg: DirectedSparseGraph[V, E], v: V): Set[E] =
        jdsg.getOutEdges(v).asScala.toSet

      def descendantsIntersectsSet(jdsg: DirectedSparseGraph[V, E], v: V, s: Set[V]): Boolean =
        s.contains(v) || s.exists(x => descendantsIntersectsSet(jdsg, x, s))

      def removeInputs(jdsg: DirectedSparseGraph[V, E], to: Set[V]): DirectedSparseGraph[V, E] =
        filterEdges(jdsg, edge => !to.contains(destination(jdsg, edge)))

      def removeOutputs(jdsg: DirectedSparseGraph[V, E], from: Set[V]): DirectedSparseGraph[V, E] =
        filterEdges(jdsg, edge => !from.contains(source(jdsg, edge)))

      // TODO def moralGraph(jdsg: DirectedSparseGraph[V, E]): Boolean

      // TODO def isAcyclic(jdsg: DirectedSparseGraph[V, E]): Boolean

      def shortestPath(jdsg: DirectedSparseGraph[V, E], source: V, goal: V)(implicit eqV: Eq[V]): Option[List[E]] =
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

      def map(jusg: UndirectedSparseGraph[V, E])(f: V => NV): UndirectedSparseGraph[NV, E] = {

        val vertexOld2New: Map[V, NV] =
          jusg.getVertices.asScala.map({ v => v -> f(v) }).toMap

        val newEdges = jusg.getEdges.asScala.toSeq.map(e => {
          val ends = jusg.getEndpoints(e)
          val newV1 = vertexOld2New(ends.getFirst)
          val newV2 = vertexOld2New(ends.getSecond)
          (newV1, newV2, e)
        })

        undirectedGraphJung.make(
          vertexOld2New.values.toSeq,
          newEdges)
      }
    }

  implicit def edgeFunctorUDSG[V, E, NE]: Functor[UndirectedSparseGraph[V, E], E, NE, UndirectedSparseGraph[V, NE]] =
    new Functor[UndirectedSparseGraph[V, E], E, NE, UndirectedSparseGraph[V, NE]] {
      def map(jusg: UndirectedSparseGraph[V, E])(f: E => NE): UndirectedSparseGraph[V, NE] = {

        val newEdges = jusg.getEdges.asScala.toSeq.map(e => {
          val ends = jusg.getEndpoints(e)
          (ends.getFirst, ends.getSecond, f(e))
        })

        undirectedGraphJung.make(
          jusg.getVertices.asScala.toSeq,
          newEdges)
      }
    }

  implicit def undirectedGraphJung[V, E]: UndirectedGraph[UndirectedSparseGraph[V, E], V, E] =
    new UndirectedGraph[UndirectedSparseGraph[V, E], V, E] {

      import scala.collection.JavaConverters.collectionAsScalaIterableConverter

      import edu.uci.ics.jung.graph.UndirectedSparseGraph
      import cats.kernel.Eq
      import spire.implicits.IntAlgebra
      import spire.implicits.eqOps

      def make(vertices: Seq[V], ef: Seq[(V, V, E)]): UndirectedSparseGraph[V, E] = {

        val jusg = new UndirectedSparseGraph[V, E]

        vertices foreach { jusg.addVertex } // TODO check return value

        ef foreach {
          case (vi, vj, e) =>
            jusg.addEdge(e, vi, vj) // TODO check return value
        }

        jusg
      }

      def vertices(jusg: UndirectedSparseGraph[V, E]): Iterable[V] =
        jusg.getVertices.asScala

      def edges(jusg: UndirectedSparseGraph[V, E]): Iterable[E] =
        jusg.getEdges.asScala

      def vertices(jusg: UndirectedSparseGraph[V, E], e: E): (V, V) = {
        val pair = jusg.getEndpoints(e)
        (pair.getFirst, pair.getSecond)
      }

      // TODO findVertex needs an index
      def findVertex(jusg: UndirectedSparseGraph[V, E], f: V => Boolean): Option[V] =
        vertices(jusg).find(f)

      def filterEdges(jusg: UndirectedSparseGraph[V, E], f: E => Boolean): UndirectedSparseGraph[V, E] =
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

      def areNeighbors(jug: UndirectedSparseGraph[V, E], v1: V, v2: V)(implicit eqV: Eq[V]): Boolean =
        edgesTouching(jug, v1).exists(edge => connects(jug, edge, v1, v2))

      def isClique(jug: UndirectedSparseGraph[V, E], vs: Iterable[V])(implicit eqV: Eq[V]): Boolean =
        (for {
          vi <- vs
          vj <- vs
        } yield {
          (vi === vj) || areNeighbors(jug, vi, vj)
        }).forall(identity)

      def forceClique(
        jug: UndirectedSparseGraph[V, E],
        among: Set[V],
        edgeFn: (V, V) => E)(implicit eqV: Eq[V], mv: Manifest[V]): UndirectedSparseGraph[V, E] = {

        val newEdges: Iterator[(V, V, E)] = among.toVector.combinations(2)
          .map({ both =>
            val v1 = both(0)
            val v2 = both(1)
            val edge = Option(jug.findEdge(v1, v2)).getOrElse(edgeFn(v1, v2))
            (v1, v2, edge)
          })

        make(vertices(jug).toList, newEdges.toList)
      }

      def degree(jusg: UndirectedSparseGraph[V, E], v: V): Int =
        edgesTouching(jusg, v).size

      def edgesTouching(jusg: UndirectedSparseGraph[V, E], v: V): Iterable[E] =
        jusg.getIncidentEdges(v).asScala

      def neighbors(jusg: UndirectedSparseGraph[V, E], v: V): Iterable[V] =
        jusg.getNeighbors(v).asScala

      //  def delete(v: Vertex[V]): UndirectedSparseGraph[V, E] =
      //    UndirectedSparseGraph(vertices.toSeq.filter(_ != v).map(_.payload), ef)

      // a "leaf" is vertex with only one neighbor
      def firstLeafOtherThan(jug: UndirectedSparseGraph[V, E], r: V)(implicit eqV: Eq[V]): Option[V] =
        vertices(jug).find(v => neighbors(jug, v).size === 1 && (!(v === r)))

      /**
       * "decompositions" page 3 (Definition 3, Section 9.3)
       * turn the neighbors of v into a clique
       */

      def eliminate(jug: UndirectedSparseGraph[V, E], v: V, payload: (V, V) => E): UndirectedSparseGraph[V, E] = {

        // TODO
        //    val vs = neighbors(v)
        //    makeFunctional.jungGraph.removeVertex(v)
        //    forceClique(vs, payload)
        ???
      }

      def other(jusg: UndirectedSparseGraph[V, E], edge: E, u: V)(implicit eqV: Eq[V]): V = {
        val (v1, v2) = vertices(jusg, edge)
        u match {
          case _ if (u === v1) => v2
          case _ if (u === v2) => v1
          case _               => throw new Exception("can't find 'other' of a vertex that isn't on the edge itself")
        }
      }

      def connects(jusg: UndirectedSparseGraph[V, E], edge: E, a1: V, a2: V)(implicit eqV: Eq[V]): Boolean = {
        val (v1, v2) = vertices(jusg, edge)
        (v1 === a1 && v2 === a2) || (v2 === a1 && v1 === a2)
      }

    }

}
