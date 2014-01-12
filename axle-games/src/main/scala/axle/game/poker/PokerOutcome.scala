package axle.game.poker

import axle.game._

case class PokerOutcome(winner: PokerPlayer, hand: Option[PokerHand])(implicit game: Poker)
  extends Outcome[Poker](Some(winner)) {

  override def toString: String =
    "Winner: " + winner.description + "\n" +
      "Hand  : " + hand.map(h => h.toString + " " + h.description).getOrElse("not shown") + "\n"

}
