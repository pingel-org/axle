package axle.game.poker

import spire.implicits._
import axle.game.Game

object PokerPlayerDealer {

  def move(state: PokerState, game: Poker, evGame: Game[Poker, PokerState, PokerOutcome, PokerMove]): (PokerMove, PokerState) = {
    val dealer = state.mover
    val move = state.numShown match {
      case 0 =>
        if (state.inFors.size === 0) {
          Deal(dealer)
        } else {
          Flop(dealer)
        }
      case 3 => Turn(dealer)
      case 4 => River(dealer)
      case 5 => Payout(dealer)
    }
    (move, state(move, game).get) // TODO .get
  }
}
