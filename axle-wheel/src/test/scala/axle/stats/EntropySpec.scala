package axle.stats

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

import edu.uci.ics.jung.graph.DirectedSparseGraph

import cats.implicits._

import spire.algebra._
import spire.math.Rational

import axle.jung.directedGraphJung
import axle.quanta.Information
import axle.quanta.UnittedQuantity
import axle.data.Coin

class EntropySpec extends AnyFunSuite with Matchers {

  implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
  implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra

  test("entropy of coin") {

    implicit val id = Information.converterGraphK2[Double, DirectedSparseGraph]()

    val biasToEntropy = new collection.immutable.TreeMap[Rational, UnittedQuantity[Information, Double]]() ++
      (0 to 100).map(i => {
        val r = Rational(i.toLong, 100)
        val e = entropy[Symbol, Rational](Coin.flipModel(r))
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
