package axle.visualize

import axle.pgm.BayesianNetwork
import axle.pgm.BayesianNetworkNode
import edu.uci.ics.jung.graph.DirectedSparseGraph

case class BayesianNetworkVisualization[T, N](
  bn: BayesianNetwork[T, N, DirectedSparseGraph[BayesianNetworkNode[T, N], axle.pgm.Edge]],
  width: Int,
  height: Int,
  border: Int)
