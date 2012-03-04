
package org.pingel.axle.graph {

  trait LabelledDirectedGraph extends DirectedGraph {

    type E = LabelledDirectedEdge

    type V = LabelledDirectedVertex

    trait LabelledDirectedVertex extends DirectedGraphVertex {

    }

    trait LabelledDirectedEdge extends DirectedGraphEdge {

      def getLabel(): String
    }

  }

  class LabelledDirectedGraphImpl extends LabelledDirectedGraph {

    class LabelledDirectedVertexImpl(label: String) extends LabelledDirectedVertex {

    }

    def newVertex(name: String) = new LabelledDirectedVertexImpl(name)

    class LabelledDirectedEdgeImpl(source: V, label: String, dest: V) extends LabelledDirectedEdge {

      def getSource = source
      def getDest = dest
      def getLabel = label
    }

    def newEdge(source: V, dest: V) = {
      val label: String = "TODO" // TODO
      val edge = new LabelledDirectedEdgeImpl(source, label, dest)
      addEdge(edge)
      edge
    }

  }

}
