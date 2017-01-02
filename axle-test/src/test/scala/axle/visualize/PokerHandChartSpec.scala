package axle.visualize

import org.scalatest._
import cats.Order.catsKernelOrderingForOrder

class PokerHandChartSpec extends FunSuite with Matchers {

  test("poker hand chart") {

    import axle.game.cards.Deck
    import axle.game.poker.PokerHand
    import axle.game.poker.PokerHandCategory

    def winnerFromHandSize(handSize: Int) =
      Deck().cards.take(handSize).combinations(5).map(PokerHand(_)).toList.max

    val data: IndexedSeq[(PokerHandCategory, Int)] =
      for {
        handSize <- 5 to 9
        trial <- 1 to 1000
      } yield (winnerFromHandSize(handSize).category, handSize)

    // TODO the inner Int should be (n: Int) => s"5 from $n"

    import spire.implicits.IntAlgebra
    import axle.visualize.BarChartGrouped
    import cats.implicits._

    val chart = BarChartGrouped[PokerHandCategory, Int, Int, Map[(PokerHandCategory, Int), Int]](
      data.tally.withDefaultValue(0),
      title = Some("Poker Hands"),
      yAxisLabel = Some("instances of hand category by initial hand size (1000 trial for each hand size)"),
      keyTitle = Some("Initial Hand Size"))

    import axle.web._
    SVG[BarChartGrouped[PokerHandCategory, Int, Int, Map[(PokerHandCategory, Int), Int]]]
    val svgName = "poker.svg"
    svg(chart, svgName)

    import axle.awt._
    val pngName = "poker.png"
    png(chart, pngName)

    new java.io.File(svgName).exists should be(true)
    new java.io.File(pngName).exists should be(true)
  }

}
