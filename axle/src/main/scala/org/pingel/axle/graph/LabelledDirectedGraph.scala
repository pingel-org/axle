
package org.pingel.axle.graph {

  trait LabelledDirectedGraph extends DirectedGraph {

    type E = LabelledDirectedEdge[V]

    trait LabelledDirectedEdge[V] extends DirectedGraphEdge[V] {
      def getLabel(): String
    }

    class LabelledDirectedEdgeImpl[V](source: V, label: String, dest: V) extends LabelledDirectedEdge[V] {
      def getSource = source
      def getDest = dest
      def getLabel = label
    }

  }

  class LabelledDirectedGraphImpl extends LabelledDirectedGraph

}
