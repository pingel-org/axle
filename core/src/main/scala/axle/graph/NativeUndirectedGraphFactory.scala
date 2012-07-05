package axle.graph

import collection._
import axle.Enrichments._

object NativeUndirectedGraphFactory extends NativeUndirectedGraphFactory

trait NativeUndirectedGraphFactory extends UndirectedGraphFactory {

  type G[VP, EP] = NativeUndirectedGraph[VP, EP]

  def graph[A, B](): G[A, B] = new NativeUndirectedGraph[A, B]() {}

  trait NativeUndirectedGraph[VP, EP] extends UndirectedGraph[VP, EP] {

    import collection._

    type V = NativeUndirectedGraphVertex[VP]
    type E = NativeUndirectedGraphEdge[EP]

    type S = (mutable.Set[V], mutable.Set[E], Map[V, mutable.Set[E]])

    val vertices = mutable.Set[V]()
    val edges = mutable.Set[E]()
    val vertex2edges = mutable.Map[V, mutable.Set[E]]()

    def getStorage() = (vertices, edges, vertex2edges)

    def getVertices() = vertices.toSet

    def getEdges() = edges.toSet

    def size() = vertices.size

    trait NativeUndirectedGraphVertex[P] extends UndirectedGraphVertex[P] {
      def setPayload(p: P): Unit = {} // TODO payload = p // type erasure problem
    }

    trait NativeUndirectedGraphEdge[P] extends UndirectedGraphEdge[P] {
      def setPayload(p: P): Unit = {} // TODO payload = p // type erasure problem
    }

    class NativeUndirectedGraphVertexImpl[P](var payload: P) extends NativeUndirectedGraphVertex[P] {
      self: V =>
      vertices += this
      def getPayload(): P = payload
    }

    class NativeUndirectedGraphEdgeImpl[P](v1: V, v2: V, var payload: P) extends NativeUndirectedGraphEdge[P] {

      self: E =>

      // assume that this edge isn't already in our list of edges
      edges += this
      getEdges(v1).add(this)
      getEdges(v2).add(this)

      def getVertices(): (V, V) = (v1, v2)
      def getPayload(): P = payload
    }

    def vertex(payload: VP): NativeUndirectedGraphVertex[VP] = new NativeUndirectedGraphVertexImpl[VP](payload)

    def edge(v1: V, v2: V, payload: EP): NativeUndirectedGraphEdge[EP] = new NativeUndirectedGraphEdgeImpl[EP](v1, v2, payload)

    def copyTo(other: UndirectedGraph[VP, EP]) = {
      // TODO
    }

    def unlink(e: E): Unit = {
      val dble = e.getVertices()
      getEdges(dble._1).remove(e)
      getEdges(dble._2).remove(e)
      edges -= e
    }

    def unlink(v1: V, v2: V): Unit = getEdges(v1).filter(_.other(v1).equals(v2)).map(unlink(_))

    def areNeighbors(v1: V, v2: V) = getEdges(v1).exists(_.connects(v1, v2))

    override def isClique(vs: Set[V]): Boolean =
      vs.doubles.forall({ case (a, b) => ((a == b) || areNeighbors(a, b)) })

    override def getNumEdgesToForceClique(vs: Set[V], payload: (V, V) => EP) = {

      val N = mutable.ArrayBuffer[V]()
      N ++= vs

      var result = 0

      for (i <- 0 until (N.size - 1)) {
        val vi = N(i)
        for (j <- (i + 1) until N.size) {
          val vj = N(j)
          if (!areNeighbors(vi, vj)) {
            edge(vi, vj, payload(vi, vj))
            result += 1
          }
        }
      }

      result
    }

    override def forceClique(vs: Set[V], payload: (V, V) => EP): Unit = {

      val vList = mutable.ArrayBuffer[V]()
      vList ++= vs

      for (i <- 0 until (vList.size - 1)) {
        val vi = vList(i)
        for (j <- (i + 1) until vList.size) {
          val vj = vList(j)
          if (!areNeighbors(vi, vj)) {
            edge(vi, vj, payload(vi, vj))
          }
        }
      }

    }

    override def vertexWithFewestEdgesToEliminateAmong(among: Set[V], payload: (V, V) => EP): Option[V] = {

      // assert: among is a subset of vertices

      var result: Option[V] = None
      var minSoFar = Integer.MAX_VALUE

      for (v <- among) {
        val x = getNumEdgesToForceClique(getNeighbors(v), payload)
        if (result == None) {
          result = Some(v)
          minSoFar = x
        } else if (x < minSoFar) {
          result = Some(v)
          minSoFar = x
        }
      }
      result
    }

    override def vertexWithFewestNeighborsAmong(among: Set[V]): Option[V] = {
      // assert: among is a subset of vertices

      var result: Option[V] = None
      var minSoFar = Integer.MAX_VALUE

      for (v <- among) {
        val x = getNeighbors(v).size
        if (result == None) {
          result = Some(v)
          minSoFar = x
        } else if (x < minSoFar) {
          result = Some(v)
          minSoFar = x
        }
      }

      result
    }

    def degree(v: V) = getEdges(v).size

    def getEdges(v: V) = {
      if (!vertex2edges.contains(v)) {
        vertex2edges += v -> scala.collection.mutable.Set[E]()
      }
      vertex2edges(v)
    }

    def getNeighbors(v: V): Set[V] = getEdges(v).map(_.other(v)).toSet

    def delete(v: V) = {
      val es = getEdges(v)
      vertices -= v
      vertex2edges.remove(v)
      for (e <- es) {
        edges -= e
        vertex2edges.get(e.other(v)) map { otherEdges => otherEdges.remove(e) }
      }
    }

    // a "leaf" is vertex with only one neighbor
    def firstLeafOtherThan(r: V) = vertices.find({ v => getNeighbors(v).size == 1 && !v.equals(r) })

    def eliminate(v: V, payload: (V, V) => EP) = {
      // "decompositions" page 3 (Definition 3, Section 9.3)
      // turn the neighbors of v into a clique

      val es = getEdges(v)
      val vs = getNeighbors(v)

      vertices -= v
      vertex2edges.remove(v)
      edges --= es

      forceClique(vs.asInstanceOf[Set[V]], payload)
    }

    // TODO there is probably a more efficient way to do this:
    def eliminate(vs: immutable.List[V], payload: (V, V) => EP): Unit = vs.map(eliminate(_, payload))

    //    def draw(): Unit = {
    //      // TODO: remove this cast
    //      val thisAsUG = this.asInstanceOf[JungUndirectedGraphFactory.UndirectedGraph[VP, EP]]
    //      JungUndirectedGraphFactory.graphFrom[VP, EP, VP, EP](thisAsUG)(vp => vp, ep => ep).draw()
    //    }

  }

}