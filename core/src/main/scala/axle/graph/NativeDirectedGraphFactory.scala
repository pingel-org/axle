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

    val _vertices = mutable.Set[V]()
    val _edges = mutable.Set[E]()
    val vertex2outedges = mutable.Map[V, mutable.Set[E]]().withDefaultValue(mutable.Set[E]())
    val vertex2inedges = mutable.Map[V, mutable.Set[E]]().withDefaultValue(mutable.Set[E]())

    def storage() = (_vertices, _edges, vertex2outedges, vertex2inedges)

    def size() = _vertices.size

    trait NativeDirectedGraphVertex[P] extends DirectedGraphVertex[P] {
    }

    trait NativeDirectedGraphEdge[P] extends DirectedGraphEdge[P] {
    }

    class NativeDirectedGraphVertexImpl[P](_payload: P) extends NativeDirectedGraphVertex[P] {

      self: V =>

      _vertices += this

      def payload(): P = _payload
    }

    class NativeDirectedGraphEdgeImpl[P](source: V, dest: V, _payload: P) extends NativeDirectedGraphEdge[P] {

      self: E =>

      _edges += this // assume that this edge isn't already in our list of edges

      vertex2outedges(source) += this
      vertex2inedges(dest) += this

      def source(): V = source
      def dest(): V = dest

      def payload(): P = _payload
    }

    def edges() = edges.toSet // immutable copy

    def vertices() = vertices.toSet // immutable copy

    def findEdge(from: V, to: V): Option[E] = vertex2outedges(from).find(_.dest == to)

    def edge(source: V, dest: V, payload: EP): E = new NativeDirectedGraphEdgeImpl[EP](source, dest, payload)

    def vertex(payload: VP): V = new NativeDirectedGraphVertexImpl[VP](payload)

    def removeAllEdgesAndVertices(): Unit = {
      _vertices.clear()
      _edges.clear()
      vertex2outedges.clear()
      vertex2inedges.clear()
    }

    def deleteEdge(e: E): Unit = {
      _edges -= e
      vertex2outedges.get(e.source()).map(_.remove(e))
      vertex2inedges.get(e.dest()).map(_.remove(e))
    }

    def deleteVertex(v: V): Unit = {
      vertex2outedges.get(v).map(outEdges =>
        outEdges.map(e => {
          _edges -= e
          vertex2inedges.get(e.dest()).map(_.remove(e))
        }))
      vertex2outedges -= v

      vertex2inedges.get(v).map(inEdges =>
        inEdges.map(e => {
          _edges -= e
          vertex2outedges.get(e.source()).map(_.remove(e))
        }))
      vertex2inedges -= v

      _vertices -= v
    }

    def leaves(): Set[V] = vertices().filter(isLeaf(_))

    def neighbors(v: V): Set[V] = predecessors(v) ++ successors(v)

    def precedes(v1: V, v2: V): Boolean = predecessors(v2).contains(v1)

    def predecessors(v: V): Set[V] = vertex2inedges(v).map(_.source())

    def isLeaf(v: V): Boolean = {
      val outEdges = vertex2outedges(v)
      outEdges == null || outEdges.size == 0
    }

    def successors(v: V): Set[V] = vertex2outedges(v).map(_.dest())

    def outputEdgesOf(v: V): immutable.Set[E] = vertex2outedges(v).toSet

    def descendantsIntersectsSet(v: V, s: Set[V]): Boolean =
      s.contains(v) || s.exists(x => descendantsIntersectsSet(x, s))

    def removeInputs(vs: Set[V]): Unit = vs.map(v => {
      vertex2inedges.get(v).map(incoming => {
        incoming.map(_edges -= _)
        vertex2inedges += v -> null
      })
    })

    def removeOutputs(vs: Set[V]): Unit = vs.map(v => {
      vertex2outedges.get(v).map(outgoing => {
        outgoing.map(_edges -= _)
        vertex2outedges += v -> null
      })
    })

    //TODO remove this method
    def removeSuccessor(v: V, successor: V): Unit = {
      vertex2outedges.get(v) map { outgoing =>
        outgoing.find(_.dest().equals(successor)) map { edgeToRemove =>
          outgoing.remove(edgeToRemove)
          _edges -= edgeToRemove
        }
      }
    }

    //TODO remove this method
    def removePredecessor(v: V, predecessor: V): Unit = {
      vertex2inedges.get(v) map { incoming =>
        incoming.find(_.source().equals(predecessor)).map(edgeToRemove => {
          incoming.remove(edgeToRemove)
          _edges -= edgeToRemove // we should really only do this if it's the last of the pair of calls. ick.
        })
      }
    }

    // def moralGraph(): UndirectedGraph[_, _] = null // TODO !!!

    def isAcyclic() = true // TODO !!!

    // TODO: slow & complex:
    def _shortestPath(source: V, goal: V, visited: Set[V]): Option[List[E]] = (source == goal) match {
      case true => Some(List())
      case false => {
        val paths = (successors(source) -- visited).flatMap(newSuccessor => {
          findEdge(source, newSuccessor)
            .flatMap(edge => _shortestPath(newSuccessor, goal, visited + source).map(sp => edge :: sp))
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

  }

}
