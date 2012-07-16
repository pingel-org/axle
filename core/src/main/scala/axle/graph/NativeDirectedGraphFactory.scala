package axle.graph

object NativeDirectedGraphFactory extends NativeDirectedGraphFactory

trait NativeDirectedGraphFactory extends DirectedGraphFactory {

  import collection._

  type G[VP, EP] = NativeDirectedGraph[VP, EP]

  def graph[A, B](): G[A, B] = new NativeDirectedGraph[A, B]() {}

  trait NativeDirectedGraph[VP, EP] extends DirectedGraph[VP, EP] {

    type V = NativeDirectedGraphVertex[VP]
    type E = NativeDirectedGraphEdge[EP]

    type S = (mutable.Set[V], mutable.Set[E], Map[V, mutable.Set[E]], Map[V, mutable.Set[E]])

    val vertices = mutable.Set[V]()
    val edges = mutable.Set[E]()
    val vertex2outedges = mutable.Map[V, mutable.Set[E]]()
    val vertex2inedges = mutable.Map[V, mutable.Set[E]]()

    def getStorage() = (vertices, edges, vertex2outedges, vertex2inedges)

    def size() = vertices.size

    trait NativeDirectedGraphVertex[P] extends DirectedGraphVertex[P] {
      def setPayload(p: P): Unit = {} // TODO: payload = p // type erasure problem
    }

    trait NativeDirectedGraphEdge[P] extends DirectedGraphEdge[P] {
      def setPayload(p: P): Unit = {} // TODO: payload = p // type erasure problem
    }

    class NativeDirectedGraphVertexImpl[P](var payload: P) extends NativeDirectedGraphVertex[P] {

      self: V =>

      vertices += this

      def getPayload(): P = payload
    }

    class NativeDirectedGraphEdgeImpl[P](source: V, dest: V, var payload: P) extends NativeDirectedGraphEdge[P] {

      self: E =>

      edges += this // assume that this edge isn't already in our list of edges

      if (!vertex2outedges.contains(source)) {
        vertex2outedges += source -> mutable.Set[E]()
      }
      vertex2outedges(source) += this

      if (!vertex2inedges.contains(dest)) {
        vertex2inedges += dest -> mutable.Set[E]()
      }
      vertex2inedges(dest) += this

      def getSource(): V = source
      def getDest(): V = dest

      def getPayload(): P = payload
    }

    def getEdges() = edges.toSet // immutable copy

    def getVertices() = vertices.toSet // immutable copy

    def getEdge(from: V, to: V): Option[E] = vertex2outedges(from).find(_.getDest == to)

    def edge(source: V, dest: V, payload: EP): E = new NativeDirectedGraphEdgeImpl[EP](source, dest, payload)

    def vertex(payload: VP): V = new NativeDirectedGraphVertexImpl[VP](payload)

    def removeAllEdgesAndVertices(): Unit = {
      vertices.clear()
      edges.clear()
      vertex2outedges.clear()
      vertex2inedges.clear()
    }

    def deleteEdge(e: E): Unit = {
      edges -= e
      vertex2outedges.get(e.getSource()).map(_.remove(e))
      vertex2inedges.get(e.getDest()).map(_.remove(e))
    }

    def deleteVertex(v: V): Unit = {
      vertex2outedges.get(v).map(outEdges =>
        outEdges.map(e => {
          edges -= e
          vertex2inedges.get(e.getDest()).map(_.remove(e))
        }))
      vertex2outedges -= v

      vertex2inedges.get(v).map(inEdges =>
        inEdges.map(e => {
          edges -= e
          vertex2outedges.get(e.getSource()).map(_.remove(e))
        }))
      vertex2inedges -= v

      vertices -= v
    }

    def getLeaves(): Set[V] = getVertices().filter(isLeaf(_))

    def getNeighbors(v: V): Set[V] = getPredecessors(v) ++ getSuccessors(v)

    def precedes(v1: V, v2: V): Boolean = getPredecessors(v2).contains(v1)

    def getPredecessors(v: V): Set[V] = vertex2inedges(v).map(_.getSource())

    def isLeaf(v: V): Boolean = {
      val outEdges = vertex2outedges(v)
      outEdges == null || outEdges.size == 0
    }

    def getSuccessors(v: V): Set[V] = vertex2outedges(v).map(_.getDest())

    def outputEdgesOf(v: V): immutable.Set[E] = vertex2outedges(v).toSet

    def descendantsIntersectsSet(v: V, s: Set[V]): Boolean =
      s.contains(v) || s.exists(x => descendantsIntersectsSet(x, s))

    def collectDescendants(v: V, result: mutable.Set[V]): Unit = {
      // inefficient
      if (!result.contains(v)) {
        result.add(v)
        getSuccessors(v).map(collectDescendants(_, result))
      }
    }

    def collectAncestors(v: V, result: mutable.Set[V]): Unit = {
      // inefficient
      if (!result.contains(v)) {
        result.add(v)
        getPredecessors(v).map(collectAncestors(_, result))
      }
    }

    def collectAncestors(vs: Set[V], result: mutable.Set[V]): Unit = vs.map(collectAncestors(_, result))

    def removeInputs(vs: Set[V]): Unit = vs.map(v => {
      vertex2inedges.get(v).map(incoming => {
        incoming.map(edge => edges -= edge)
        vertex2inedges += v -> null
      })
    })

    def removeOutputs(vs: Set[V]): Unit = vs.map(v => {
      vertex2outedges.get(v).map(outgoing => {
        outgoing.map(edge => edges -= edge)
        vertex2outedges += v -> null
      })
    })

    //TODO remove this method
    def removeSuccessor(v: V, successor: V): Unit = {
      vertex2outedges.get(v) map { outgoing =>
        outgoing.find(_.getDest().equals(successor)) map { edgeToRemove =>
          outgoing.remove(edgeToRemove)
          edges -= edgeToRemove
        }
      }
    }

    //TODO remove this method
    def removePredecessor(v: V, predecessor: V): Unit = {
      vertex2inedges.get(v) map { incoming =>
        incoming.find(_.getSource().equals(predecessor)).map(edgeToRemove => {
          incoming.remove(edgeToRemove)
          edges -= edgeToRemove // we should really only do this if it's the last of the pair of calls. ick.
        })
      }
    }

    // def moralGraph(): UndirectedGraph[_, _] = null // TODO !!!

    def isAcyclic() = true // TODO !!!

    // not so efficient:
    def _shortestPath(source: V, goal: V, visited: Set[V]): Option[List[E]] = (source == goal) match {
      case true => Some(List())
      case false => {
        // .filter(!visited.contains(_))
        val paths = (getSuccessors(source) -- visited).flatMap(newSuccessor => {
          getEdge(source, newSuccessor)
            .flatMap(edge => _shortestPath(newSuccessor, goal, visited + source)) // map(sp => edge :: sp)
        })
        paths.size match {
          case 0 => None
          case _ => Some(paths.reduceLeft(
            (l1, l2) => (l1.length < l2.length) match { case true => l1 case false => l2 }
          ))
        }
      }
    }

    def shortestPath(source: V, goal: V): Option[List[E]] = _shortestPath(source, goal, Set())

    //    def draw(): Unit = {
    //      // TODO: remove this cast
    //      val thisAsDG = this.asInstanceOf[JungDirectedGraphFactory.DirectedGraph[VP, EP]]
    //      JungDirectedGraphFactory.graphFrom[VP, EP, VP, EP](thisAsDG)(vp => vp, ep => ep).draw()
    //    }

  }

}
