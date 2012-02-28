
package org.pingel.axle.graph {

  import scala.collection._

  trait Graph {

    trait GraphVertex[E]

    trait GraphEdge[V] {}

    type V <: GraphVertex[E]
    type E <: GraphEdge[V]

    var vertices = Set[V]()
    var edges = Set[E]()

    def size() = vertices.size

    def getEdges() = edges

    def getVertices() = vertices

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


*/
