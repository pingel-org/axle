package axle.game.poker

import spire.implicits._

object PokerPlayerDealer {

  def move(state: PokerState, game: Poker): PokerMove = {
    val dealer = state.mover
    state.numShown match {
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
  }
}
