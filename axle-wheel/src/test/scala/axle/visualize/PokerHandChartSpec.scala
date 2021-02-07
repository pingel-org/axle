package axle.visualize

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

import cats.implicits._

import spire.algebra._

//import axle.algebra.Talliable
import axle.syntax.talliable.talliableOps

class PokerHandChartSpec extends AnyFunSuite with Matchers {

  test("poker hand chart") {

    import axle.game.cards.Deck
    import axle.game.poker.PokerHand
    import axle.game.poker.PokerHandCategory

    def winnerFromHandSize(handSize: Int) =
      Deck().cards.take(handSize).toVector.combinations(5).map(PokerHand(_)).toList.max

    val data: Seq[(PokerHandCategory, Int)] =
      for {
        handSize <- 5 to 9
        trial <- 1 to 1000
      } yield (winnerFromHandSize(handSize).category, handSize)

    // TODO the inner Int should be (n: Int) => s"5 from $n"

    import axle.visualize.BarChartGrouped

    implicit val ringInt: CRing[Int] = spire.implicits.IntAlgebra
    val chart = BarChartGrouped[PokerHandCategory, Int, Int, Map[(PokerHandCategory, Int), Int], String](
      () => data.tally.withDefaultValue(0),
      colorOf = (g: PokerHandCategory, s: Int) => Color.black,
      title = Some("Poker Hands"),
      yAxisLabel = Some("instances of hand category by initial hand size (1000 trial for each hand size)"),
      keyTitle = Some("Initial Hand Size"))

    import axle.web._
    import cats.effect._
    // SVG[BarChartGrouped[PokerHandCategory, Int, Int, Map[(PokerHandCategory, Int), Int], String]]
    val svgName = "poker.svg"
    chart.svg[IO](svgName).unsafeRunSync()

    // import axle.awt._
    // val pngName = "poker.png"
    // chart.png[IO](pngName).unsafeRunSync()

    new java.io.File(svgName).exists should be(true)
    // new java.io.File(pngName).exists should be(true)
  }

}
