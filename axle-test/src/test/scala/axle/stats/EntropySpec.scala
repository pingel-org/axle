package axle.stats

import org.specs2.mutable.Specification

import axle.quanta.Information
import axle.quanta.InformationDouble
import axle.jung.JungDirectedGraph
import axle.jung.JungDirectedGraph.directedGraphJung // conversion graph
import axle.quanta.UnittedQuantity4
import axle.quanta.Quantum4
import spire.math.Rational
import spire.math.Real
import spire.algebra.Order
import axle.jung.JungDirectedGraph
import axle.jung.JungDirectedGraph.directedGraphJung

class EntropySpec extends Specification {

  "entropy of coin" should {
    "work" in {

      implicit val id = InformationDouble

      val biasToEntropy = new collection.immutable.TreeMap[Rational, UnittedQuantity4[Information[Double], Double]]() ++
        (0 to 100).map(i => (Rational(i, 100), entropy(coin(Rational(i, 100))))).toMap

      // implicit val bitp = bit.plottable

      // val plot = Plot(List(("h", biasToEntropy)),
      //   drawKey = false,
      //   xAxisLabel = Some("p(x='HEAD)"),
      //   title = Some("Entropy"))

      import axle.jung.JungDirectedGraph
      import axle.jung.JungDirectedGraph.directedGraphJung // conversion graph
      import spire.implicits.DoubleAlgebra

      implicit val cg = axle.quanta.conversionGraph[Information[Double], Double, JungDirectedGraph](InformationDouble)

      import spire.implicits._
      val lhs: UnittedQuantity4[Information[Double], Double] = biasToEntropy(Rational(1, 100))
      val rhs: UnittedQuantity4[Information[Double], Double] = biasToEntropy(Rational(1, 2))
      implicit val base = id.bit
      implicit val ord = axle.quanta.unitOrder[Information[Double], Double, JungDirectedGraph]
      // lhs < rhs
      orderOps(lhs).compare(rhs) == -1
    }
  }
}