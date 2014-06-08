package axle.stats

import axle.quanta.Information._
import org.specs2.mutable._
import axle._
import axle.game.Dice._
import spire.math._
import spire.implicits._
import spire.algebra._

class EntropySpec extends Specification {

  "entropy of coin" should {
    "work" in {

      val biasToEntropy = new collection.immutable.TreeMap[Rational, Q]() ++
        (0 to 100).map(i => (Rational(i, 100), entropy(coin(Rational(i, 100))))).toMap

      // implicit val bitp = bit.plottable
      //
      //  val plot = Plot(List(("h", biasToEntropy)),
      //    drawKey = false,
      //    xAxisLabel = Some("p(x='HEAD)"),
      //    title = Some("Entropy"))

      biasToEntropy(Rational(1, 100)) < biasToEntropy(Rational(1, 2))
    }
  }
}