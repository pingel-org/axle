package axle.visualize

import org.specs2.mutable.Specification

import axle.enrichGenSeq
import axle.game.cards.Deck
import axle.game.poker.PokerHand
import axle.game.poker.PokerHandCategory
import axle.smoosh
import spire.compat.ordering
import spire.implicits.IntAlgebra

class PokerHandChartSpec extends Specification {

  "poker hand chart" should {
    "work" in {

      val ns = 5 to 9

      def winnersFromHandSize(n: Int) =
        (1 to 20).map(i => Deck().cards.take(n).combinations(5).map(PokerHand(_)).toList.max.category)

      val data: Map[Tuple2[PokerHandCategory, Int], Int] =
        smoosh(ns.map(n => (n, winnersFromHandSize(n).tally)).toMap).withDefaultValue(0)

      // TODO the inner Int should be (n: Int) => s"5 from $n"

      val chart = BarChartGrouped[PokerHandCategory, Int, Int, Map[Tuple2[PokerHandCategory, Int], Int]](
        data,
        xAxis = 0,
        title = Some("Poker Hand Distribution"))

      // png(chart, "pokerhands.png")

      1 must be equalTo 1
    }
  }

}