package org.pingel.axle.graph {

  import scala.collection._

  trait DirectedGraphVertex[E]

  trait DirectedGraphEdge[V] {
    def getSource(): V
    def getDest(): V
  }
  
  class DirectedGraphEdgeImpl[V](source: V, dest: V) extends DirectedGraphEdge[V] {
    def getSource() = source
    def getDest() = dest
  }

  trait DirectedGraph[V <: DirectedGraphVertex[E], E <: DirectedGraphEdge[V]] {

    var vertices = Set[V]()
    var edges = Set[E]()
    var vertex2outedges = Map[V, mutable.Set[E]]()
    var vertex2inedges = Map[V, mutable.Set[E]]()

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

    def getEdges() = edges

    def getVertices() = vertices

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
        incoming.find({ _.getSource().equals(predecessor) }) map { edgeToRemove =>
          incoming.remove(edgeToRemove)
          edges -= edgeToRemove // we should really only do this if it's the last of the pair of calls. ick.
        }
      }
    }

    def moralGraph(): UndirectedGraph[_, _] = null // TODO !!!

    def isAcyclic() = true // TODO !!!

  }

}

/*

// http://ctp.di.fct.unl.pt/~amd/pmp/teoricas/10.html

trait GraphFamily {

    type G <: Graph
    type E <: Edge
    type V <: Vertex

    abstract class Graph {
//      this: G =>
//      private var vertexSet: Set[V] = Set()
//      private var edgeSet: Set[E] = Set()
      def vertices(): Set[V]
      def edges(): Set[E]
      def newNode(): V
    }
    
    abstract class Edge {
//        this: E =>
//       private var from: List[O] = List()
        def vertices(): Set[V]
        def contains(v: V): Boolean
//      def publish = for (obs <- observers) obs.notify(this)
    }
    
    abstract class Vertex(label: String) {
        def edges(): Set[E]
    }
}

object DirectedFamily extends GraphFamily {

  type G = DirectedGraph
  type E = DirectedEdge
  type V = DirectedVertex

  class DirectedGraph extends Graph {
    def vertices() = Set[V]() // TODO
    def edges() = Set[E]() // TODO
    def newNode(label: String) = new DirectedVertex(label)
  }
  
  class DirectedEdge extends Edge {
    def contains(v: V) = true // TODO
    def vertices() = {
      Set[V]() // TODO
    }
  }

  class DirectedVertex(label: String) extends Vertex(label) {
    def edges() = {
      Set[E]() // TODO
    }
  }
  
}

object UndirectedFamily extends GraphFamily {

  type G = UndirectedGraph
  type E = UndirectedEdge
  type V = UndirectedVertex

  class UndirectedGraph extends Graph {
    def vertices() = Set[V]() // TODO
    def edges() = Set[E]() // TODO
    def newNode(label: String) = new UndirectedVertex(label)
  }
  
  class UndirectedEdge extends Edge {
    def contains(v: V) = true // TODO
    def vertices() = {
      Set[V]() // TODO
    }
  }

  class UndirectedVertex(label: String) extends Vertex(label) {
    def edges() = {
      Set[E]() // TODO
    }
  }
  
}

object Tests {
 
  def main(args: Array[String]) {

    import DirectedFamily._
	  
    var g = new DirectedGraph()

    val n1 = g.newNode("1")
    val n2 = g.newNode("2")
    
    
  }

}

*/
