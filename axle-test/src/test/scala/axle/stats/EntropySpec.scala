package axle.stats

import org.specs2.mutable.Specification

import axle.quanta.Information3
import axle.quanta.Information3.bit
import axle.quanta.UnittedQuantity3
import axle.quanta.Quantum3
import spire.math.Rational
import spire.math.Real
import spire.algebra.Order
import axle.jung.JungDirectedGraph
import axle.jung.JungDirectedGraph.directedGraphJung

class EntropySpec extends Specification {

  "entropy of coin" should {
    "work" in {

      val biasToEntropy = new collection.immutable.TreeMap[Rational, UnittedQuantity3[Information3, Double]]() ++
        (0 to 100).map(i => (Rational(i, 100), entropy(coin(Rational(i, 100))))).toMap

      // implicit val bitp = bit.plottable

      // val plot = Plot(List(("h", biasToEntropy)),
      //   drawKey = false,
      //   xAxisLabel = Some("p(x='HEAD)"),
      //   title = Some("Entropy"))

      import spire.implicits._
      val lhs: UnittedQuantity3[Information3, Double] = biasToEntropy(Rational(1, 100))
      val rhs: UnittedQuantity3[Information3, Double] = biasToEntropy(Rational(1, 2))
      implicit val base = bit[Double]
      implicit val ord = axle.quanta.unit3Order[Information3, Double, JungDirectedGraph]
      // lhs < rhs
      orderOps(lhs).compare(rhs) == -1
    }
  }
}