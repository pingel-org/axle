package axle.visualize

import edu.uci.ics.jung.graph.UndirectedSparseGraph

case class JungUndirectedSparseGraphVisualization[VP, EP](
  jusg: UndirectedSparseGraph[VP, EP],
  width: Int = 700,
  height: Int = 700,
  border: Int = 50)
