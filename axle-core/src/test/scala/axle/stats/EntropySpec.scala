package axle.stats

import org.specs2.mutable.Specification

import axle.quanta.Information.Q
import spire.math.Rational

class EntropySpec extends Specification {

  "entropy of coin" should {
    "work" in {

      val biasToEntropy = new collection.immutable.TreeMap[Rational, Q]() ++
        (0 to 100).map(i => (Rational(i, 100), entropy(coin(Rational(i, 100))))).toMap

//      implicit val bitp = bit.plottable
//
//      val plot = Plot(List(("h", biasToEntropy)),
//        drawKey = false,
//        xAxisLabel = Some("p(x='HEAD)"),
//        title = Some("Entropy"))

      biasToEntropy(Rational(1, 100)) < biasToEntropy(Rational(1, 2))
    }
  }
}