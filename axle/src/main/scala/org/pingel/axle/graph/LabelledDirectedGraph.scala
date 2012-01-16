
package org.pingel.axle.graph {

  // trait LabelledDirectedGraph[VT, ET] extends DirectedGraph[VT, ET]

  // trait LabelledDirectedEdge[VT] extends DirectedGraphEdge[VT]

  class LabelledDirectedEdge[V](source: V, label: String, dest: V) extends DirectedGraphEdge[V](source, dest) {

    def getLabel = label

  }

  class LabelledDirectedGraph[V <: DirectedGraphVertex[E], E <: LabelledDirectedEdge[V]]
    extends DirectedGraph[V, E] {

  }

  object LabelledDirectedGraphTest {

    def main(args: Array[String]) {

      var ldg = new LabelledDirectedGraph()

    }

  }

}
