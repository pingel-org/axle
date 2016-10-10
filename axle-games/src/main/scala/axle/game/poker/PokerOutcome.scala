package axle.game.poker

import spire.algebra.Eq
import axle.Show
import axle.game.Game
import axle.game.Outcome
import axle.game.Player
import axle.string

object PokerOutcome {

  implicit def showPokerOutcome: Show[PokerOutcome] = new Show[PokerOutcome] {
    def text(po: PokerOutcome): String = {
      import po._
      "Winner: " + winner.get.description + "\n" +
        "Hand  : " + hand.map(h => string(h) + " " + h.description).getOrElse("not shown") + "\n"
    }
  }
}

case class PokerOutcome(winner: Option[Player], hand: Option[PokerHand]) {

  def displayTo(player: Player, game: Game[Poker, PokerState, PokerOutcome, PokerMove])(
    implicit eqp: Eq[Player], sp: Show[Player]): String =
    string(this)

}
