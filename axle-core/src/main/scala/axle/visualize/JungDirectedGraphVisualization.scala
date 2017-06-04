package axle.visualize

import edu.uci.ics.jung.graph.DirectedSparseGraph

case class JungDirectedSparseGraphVisualization[VP, EP](
  jdsg: DirectedSparseGraph[VP, EP],
  width: Int = 700,
  height: Int = 700,
  border: Int = 50)
