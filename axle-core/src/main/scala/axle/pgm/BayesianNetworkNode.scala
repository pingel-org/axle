package axle.pgm

import cats.Show
import cats.kernel.Eq

import spire.algebra.Field

import axle.probability.Variable
import axle.probability.Factor

case class BayesianNetworkNode[T, N](variable: Variable[T], cpt: Factor[T, N])

object BayesianNetworkNode {

  implicit def bnnShow[T, N]: Show[BayesianNetworkNode[T, N]] = bnn =>
    bnn.variable.name + "\n\n" + bnn.cpt

  implicit def bnnEq[T: Eq, N: Field] = Eq.fromUniversalEquals[BayesianNetworkNode[T, N]]

}
