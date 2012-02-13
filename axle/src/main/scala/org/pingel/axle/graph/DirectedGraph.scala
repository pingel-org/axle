package org.pingel.axle.graph {

  import scala.collection._

  trait DirectedGraphVertex[DE <: DirectedGraphEdge[_]] extends GraphVertex[DE]

  trait DirectedGraphEdge[DV <: DirectedGraphVertex[_]] extends GraphEdge[DV] {
    def getSource(): DV
    def getDest(): DV
  }

  class DirectedGraphEdgeImpl[V <: DirectedGraphVertex[_]](source: V, dest: V)
    extends DirectedGraphEdge[V] {
    def getSource() = source
    def getDest() = dest
  }

  trait DirectedGraph[DV <: DirectedGraphVertex[DE], DE <: DirectedGraphEdge[DV]]
    extends Graph[DV, DE] {

    var vertex2outedges = Map[DV, mutable.Set[DE]]()
    var vertex2inedges = Map[DV, mutable.Set[DE]]()

    def getEdge(from: DV, to: DV): Option[DE] = vertex2outedges(from).find( e => e.getDest == to )

    def addEdge(edge: DE) = {

      val source = edge.getSource()
      val dest = edge.getDest()

      edges += edge

      if (!vertex2outedges.contains(source)) {
        vertex2outedges += source -> mutable.Set[DE]()
      }
      vertex2outedges(source) += edge

      if (!vertex2inedges.contains(dest)) {
        vertex2inedges += dest -> mutable.Set[DE]()
      }
      vertex2inedges(dest) += edge

      edge
    }

    def addVertex(v: DV) = {
      vertices += v
      v
    }

    def removeAllEdgesAndVertices(): Unit = {
      vertices = Set[DV]()
      edges = Set[DE]()
      vertex2outedges = Map[DV, mutable.Set[DE]]()
      vertex2inedges = Map[DV, mutable.Set[DE]]()
    }

    def deleteEdge(e: DE) = {

      edges -= e

      vertex2outedges.get(e.getSource()) map { outwards =>
        outwards.remove(e)
      }

      vertex2inedges.get(e.getDest()) map { inwards =>
        inwards.remove(e)
      }
    }

    def deleteVertex(v: DV) {
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
      var result = Set[DV]()
      for (v <- getVertices()) {
        if (isLeaf(v)) {
          result += v
        }
      }
      result
    }

    def getNeighbors(v: DV) = {
      var result = Set[DV]()
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

    def precedes(v1: DV, v2: DV) = getPredecessors(v2).contains(v1)

    def getPredecessors(v: DV) = {
      var result = Set[DV]()
      vertex2inedges.get(v) map { inEdges =>
        for (edge <- inEdges) {
          result += edge.getSource()
        }
      }
      result
    }

    def isLeaf(v: DV) = {
      val outEdges = vertex2outedges.get(v)
      outEdges == null || outEdges.size == 0
    }

    def getSuccessors(v: DV) = {
      var result = Set[DV]()
      vertex2outedges.get(v) map { outEdges =>
        for (edge <- outEdges) {
          result += edge.getDest()
        }
      }
      result
    }

    def outputEdgesOf(v: DV) = {
      var result = Set[DE]()
      vertex2outedges.get(v) map { outEdges => result ++= outEdges }
      result
    }

    def descendantsIntersectsSet(v: DV, s: Set[DV]): Boolean = {

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

    def collectDescendants(v: DV, result: mutable.Set[DV]): Unit = {
      // inefficient
      if (!result.contains(v)) {
        result.add(v)
        for (child <- getSuccessors(v)) {
          collectDescendants(child, result)
        }
      }
    }

    def collectAncestors(v: DV, result: mutable.Set[DV]): Unit = {
      // inefficient
      if (!result.contains(v)) {
        result.add(v)
        for (child <- getPredecessors(v)) {
          collectAncestors(child, result)
        }
      }
    }

    def collectAncestors(vs: Set[DV], result: mutable.Set[DV]): Unit = {
      for (v <- vs) {
        collectAncestors(v, result)
      }
    }

    def removeInputs(vs: Set[DV]) {
      for (v <- vs) {
        vertex2inedges.get(v) map { incoming =>
          for (edge <- incoming) {
            edges -= edge
          }
          vertex2inedges += v -> null
        }
      }
    }

    def removeOutputs(vs: Set[DV]) {
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
    def removeSuccessor(v: DV, successor: DV) {
      vertex2outedges.get(v) map { outgoing =>
        outgoing.find({ _.getDest().equals(successor) }) map { edgeToRemove =>
          outgoing.remove(edgeToRemove)
          edges -= edgeToRemove
        }
      }
    }

    //TODO remove this method
    def removePredecessor(v: DV, predecessor: DV) {
      vertex2inedges.get(v) map { incoming =>
        incoming.find(_.getSource().equals(predecessor)).map(edgeToRemove => {
          incoming.remove(edgeToRemove)
          edges -= edgeToRemove // we should really only do this if it's the last of the pair of calls. ick.
        })
      }
    }

    // def moralGraph(): UndirectedGraph[_, _] = null // TODO !!!

    def isAcyclic() = true // TODO !!!

    // won't terminate for graphs w/cycles:
    def _shortestPath(source: DV, goal: DV, visited: Set[DV]): Option[List[DE]] = (source == goal) match {
      case true => Some(List())
      case false => {
        val paths = getSuccessors(source)
          .filter(!visited.contains(_)).flatMap(newSuccessor => {
            getEdge(source, newSuccessor).flatMap( edge => 
            _shortestPath(newSuccessor, goal, visited + source).map(sp => edge :: sp) 
            )
          }
          )
        paths.size match {
          case 0 => None
          case _ => Some( paths.reduceLeft(
            (l1, l2) => (l1.length < l2.length) match { case true => l1 case false => l2 }
          ))
        }
      }
    }

    def shortestPath(source: DV, goal: DV) = _shortestPath(source, goal, Set())

  }

}
