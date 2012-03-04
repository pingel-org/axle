package org.pingel.axle.graph {

  import scala.collection._

  trait DirectedGraph extends Graph {

    type V <: DirectedGraphVertex
    
    type E <: DirectedGraphEdge
    
    trait DirectedGraphVertex extends GraphVertex {
      // type E <: DirectedGraphEdge
    }

    trait DirectedGraphEdge extends GraphEdge {
      
      // type V <: DirectedGraphVertex
      
      def getSource(): V
      def getDest(): V
    }

    class DirectedGraphEdgeImpl(source: V, dest: V) extends DirectedGraphEdge {
      def getSource() = source
      def getDest() = dest
    }

    var vertex2outedges = Map[V, mutable.Set[E]]()
    var vertex2inedges = Map[V, mutable.Set[E]]()

    def getEdge(from: V, to: V): Option[E] = vertex2outedges(from).find(e => e.getDest == to)

    def addEdge(edge: E) = {

      val source = edge.getSource()
      val dest = edge.getDest()

      edges += edge

      if (!vertex2outedges.contains(source)) {
        vertex2outedges += source -> mutable.Set[E]()
      }
      vertex2outedges(source) += edge

      if (!vertex2inedges.contains(dest)) {
        vertex2inedges += dest -> mutable.Set[E]()
      }
      vertex2inedges(dest) += edge

      edge
    }

    def addVertex(v: V) = {
      vertices += v
      v
    }

    def removeAllEdgesAndVertices(): Unit = {
      vertices = Set[V]()
      edges = Set[E]()
      vertex2outedges = Map[V, mutable.Set[E]]()
      vertex2inedges = Map[V, mutable.Set[E]]()
    }

    def deleteEdge(e: E) = {

      edges -= e

      vertex2outedges.get(e.getSource()) map { outwards =>
        outwards.remove(e)
      }

      vertex2inedges.get(e.getDest()) map { inwards =>
        inwards.remove(e)
      }
    }

    def deleteVertex(v: V) {
      vertex2outedges.get(v) map { outEdges =>
        for (e <- outEdges) {
          edges -= e
          vertex2inedges.get(e.getDest()) map { out2in =>
            out2in.remove(e)
          }
        }
      }
      vertex2outedges -= v

      vertex2inedges.get(v) map { inEdges =>
        for (e <- inEdges) {
          edges -= e
          vertex2outedges.get(e.getSource()) map { in2out =>
            in2out.remove(e)
          }
        }
      }
      vertex2inedges -= v

      vertices -= v
    }

    def getLeaves() = {
      var result = Set[V]()
      for (v <- getVertices()) {
        if (isLeaf(v)) {
          result += v
        }
      }
      result
    }

    def getNeighbors(v: V) = {
      var result = Set[V]()
      vertex2outedges.get(v) map { outEdges =>
        for (edge <- outEdges) {
          result += edge.getDest()
        }
      }
      vertex2inedges.get(v) map { inEdges =>
        for (edge <- inEdges) {
          result += edge.getSource()
        }
      }
      result
    }

    def precedes(v1: V, v2: V) = getPredecessors(v2).contains(v1)

    def getPredecessors(v: V) = {
      var result = Set[V]()
      vertex2inedges.get(v) map { inEdges =>
        for (edge <- inEdges) {
          result += edge.getSource()
        }
      }
      result
    }

    def isLeaf(v: V) = {
      val outEdges = vertex2outedges.get(v)
      outEdges == null || outEdges.size == 0
    }

    def getSuccessors(v: V) = {
      var result = Set[V]()
      vertex2outedges.get(v) map { outEdges =>
        for (edge <- outEdges) {
          result += edge.getDest()
        }
      }
      result
    }

    def outputEdgesOf(v: V) = {
      var result = Set[E]()
      vertex2outedges.get(v) map { outEdges => result ++= outEdges }
      result
    }

    def descendantsIntersectsSet(v: V, s: Set[V]): Boolean = {

      if (s.contains(v)) {
        return true
      }
      for (x <- s) {
        if (descendantsIntersectsSet(x, s)) {
          return true
        }
      }
      return false
    }

    def collectDescendants(v: V, result: mutable.Set[V]): Unit = {
      // inefficient
      if (!result.contains(v)) {
        result.add(v)
        for (child <- getSuccessors(v)) {
          collectDescendants(child, result)
        }
      }
    }

    def collectAncestors(v: V, result: mutable.Set[V]): Unit = {
      // inefficient
      if (!result.contains(v)) {
        result.add(v)
        for (child <- getPredecessors(v)) {
          collectAncestors(child, result)
        }
      }
    }

    def collectAncestors(vs: Set[V], result: mutable.Set[V]): Unit = {
      for (v <- vs) {
        collectAncestors(v, result)
      }
    }

    def removeInputs(vs: Set[V]) {
      for (v <- vs) {
        vertex2inedges.get(v) map { incoming =>
          for (edge <- incoming) {
            edges -= edge
          }
          vertex2inedges += v -> null
        }
      }
    }

    def removeOutputs(vs: Set[V]) {
      for (v <- vs) {
        vertex2outedges.get(v) map { outgoing =>
          for (edge <- outgoing) {
            edges -= edge
          }
          vertex2outedges += v -> null
        }
      }
    }

    //TODO remove this method
    def removeSuccessor(v: V, successor: V) {
      vertex2outedges.get(v) map { outgoing =>
        outgoing.find({ _.getDest().equals(successor) }) map { edgeToRemove =>
          outgoing.remove(edgeToRemove)
          edges -= edgeToRemove
        }
      }
    }

    //TODO remove this method
    def removePredecessor(v: V, predecessor: V) {
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
        val paths = getSuccessors(source)
          .filter(!visited.contains(_)).flatMap(newSuccessor => {
            getEdge(source, newSuccessor).flatMap(edge =>
              _shortestPath(newSuccessor, goal, visited + source).map(sp => edge :: sp)
            )
          }
          )
        paths.size match {
          case 0 => None
          case _ => Some(paths.reduceLeft(
            (l1, l2) => (l1.length < l2.length) match { case true => l1 case false => l2 }
          ))
        }
      }
    }

    def shortestPath(source: V, goal: V) = _shortestPath(source, goal, Set())

  }

}
