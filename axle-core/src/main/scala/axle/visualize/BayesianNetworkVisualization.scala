package axle.visualize

import axle.pgm.BayesianNetwork

case class BayesianNetworkVisualization[T, N, DG](
  bn: BayesianNetwork[T, N, DG],
  width: Int,
  height: Int,
  border: Int)
