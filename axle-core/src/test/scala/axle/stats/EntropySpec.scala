package axle.stats

import org.specs2.mutable.Specification

import axle.quanta2.Information
import axle.quanta2.Quantity
import axle.quanta2.UnittedQuantity
import axle.quanta2.Quantum
import spire.math.Rational
import spire.math.Real
import spire.algebra.Order

class EntropySpec extends Specification {

  "entropy of coin" should {
    "work" in {

      val biasToEntropy = new collection.immutable.TreeMap[Rational, UnittedQuantity[Information, Real]]() ++
        (0 to 100).map(i => (Rational(i, 100), entropy[Symbol, Rational](coin(Rational(i, 100))))).toMap

      // implicit val bitp = bit.plottable
      //
      // val plot = Plot(List(("h", biasToEntropy)),
      //   drawKey = false,
      //   xAxisLabel = Some("p(x='HEAD)"),
      //   title = Some("Entropy"))

      import spire.implicits.orderOps
      val lhs: UnittedQuantity[Information, Real] = biasToEntropy(Rational(1, 100))
      val rhs: UnittedQuantity[Information, Real] = biasToEntropy(Rational(1, 2))
      val or = implicitly[Order[UnittedQuantity[Information, Real]]]
      // lhs < rhs
      orderOps(lhs).compare(rhs) == -1
    }
  }
}