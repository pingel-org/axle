package axle.stats

import org.specs2.mutable.Specification

import axle.quanta.Information
import axle.jung.JungDirectedGraph
import axle.jung.JungDirectedGraph.directedGraphJung // conversion graph
import axle.quanta.UnittedQuantity
import axle.quanta.Quantum
import spire.math.Rational
import spire.math.Real
import spire.algebra.Order
import axle.jung.JungDirectedGraph.directedGraphJung
import axle.jung.JungDirectedGraph
import spire.implicits.DoubleAlgebra
import spire.implicits._

class EntropySpec extends Specification {

  "entropy of coin" should {
    "work" in {

      implicit val id = Information.metadata[Double, JungDirectedGraph]

      val biasToEntropy = new collection.immutable.TreeMap[Rational, UnittedQuantity[Information, Double]]() ++
        (0 to 100).map(i => (Rational(i, 100), entropy(coin(Rational(i, 100))))).toMap

      // implicit val bitp = bit.plottable

      // val plot = Plot(List(("h", biasToEntropy)),
      //   drawKey = false,
      //   xAxisLabel = Some("p(x='HEAD)"),
      //   title = Some("Entropy"))

      val lhs: UnittedQuantity[Information, Double] = biasToEntropy(Rational(1, 100))
      val rhs: UnittedQuantity[Information, Double] = biasToEntropy(Rational(1, 2))
      implicit val base = id.bit
      implicit val ord = axle.quanta.unitOrder[Information, Double]
      // lhs < rhs
      orderOps(lhs).compare(rhs) == -1
    }
  }
}