package axle.stats

import org.scalatest._

import edu.uci.ics.jung.graph.DirectedSparseGraph
import cats.Order.catsKernelOrderingForOrder
import spire.math.Rational
import spire.implicits.DoubleAlgebra
import spire.implicits._
import axle.jung.directedGraphJung
import axle.quanta.Information
import axle.quanta.UnittedQuantity

class EntropySpec extends FunSuite with Matchers {

  test("entropy of coin") {

    implicit val id = Information.converterGraphK2[Double, DirectedSparseGraph]

    val biasToEntropy = new collection.immutable.TreeMap[Rational, UnittedQuantity[Information, Double]]() ++
      (0 to 100).map(i => {
        val r = Rational(i.toLong, 100)
        val e = entropy[({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ, Symbol, Rational](coin(r))
        r -> e
      }).toMap

    // implicit val bitp = bit.plottable

    // val plot = Plot(List(("h", biasToEntropy)),
    //   drawKey = false,
    //   xAxisLabel = Some("p(x='HEAD)"),
    //   title = Some("Entropy"))

    val lhs: UnittedQuantity[Information, Double] = biasToEntropy(Rational(1, 100))
    val rhs: UnittedQuantity[Information, Double] = biasToEntropy(Rational(1, 2))
    // val base = id.bit

    lhs should be < rhs
  }
}
